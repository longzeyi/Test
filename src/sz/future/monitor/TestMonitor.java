package sz.future.monitor;

import java.util.Map;

import sz.future.dao.FutureDevDao;
import sz.future.domain.InverstorPosition;
import sz.future.test.test1.Global;
import sz.future.trader.comm.ServerParams;
import sz.future.trader.comm.Super;
import sz.future.util.StatisticsUtil;
import sz.future.util.TraderUtil;

/**
 * @author Sean
 * 行情监测线程
 */
public class TestMonitor extends Thread{
	private String[] instruments ;//,"pp1501","sr1501","jd1501","pta1501","fg1501","rm1501"
	private Map<String, double[]> tickData;
	private double[] lastTick;
	private FutureDevDao dao = new FutureDevDao();
	
	public TestMonitor(){
		//克隆合约数组
		instruments = new String[ServerParams.instruments2.length];
		for (int i = 0; i < ServerParams.instruments2.length; i++) {
			System.out.println("ServerParams.instruments2[i]: "+ServerParams.instruments2[i]);
			instruments[i] = ServerParams.instruments2[i];
			//装载历史收盘价（一段时间内）
			Super.HISTORY_CLOSE_PRICE.put(instruments[i], dao.getHistoryClosePrice(Super.historyDateRange, instruments[i]));
		}
		while(true){
			if(init()){
				break;
			} else {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private boolean init(){
		boolean bool = false;
		if(TraderUtil.qryTradingAccount()==0){
			bool = true;
			System.out.println("查询资金成功");
		} else {
			bool =false;
			System.err.println("查询资金失败");
		}
		if(TraderUtil.qryPosition()==0){
			bool = true;
			System.out.println("查询持仓成功");
		} else {
			bool = false;
			System.err.println("查询持仓失败");
		}
		return bool;
	}
	/**
	 * @param args
	 */
	public void run() {
		System.err.println("------------启动行情监测线程-------------");
		while(true){
			tickData = Super.TICK_DATA;
//			System.out.println(ServerParams.instruments[0]);
			for (int i = 0; i < instruments.length; i++) {
				System.out.println("监测"+instruments[i]);
				lastTick = tickData.get(instruments[i]);//获取当前合约的最新行情
				if(lastTick == null){
					System.err.println("lastTick is null");
					continue;
				}
				Double highestPpriceOfPeriod = dao.getLimitPrice(Global.period, instruments[i], 1);
				Double lowestPriceOfPeriod = dao.getLimitPrice(Global.period, instruments[i], 2);
				double preMA5 = StatisticsUtil.getClosePriceTotal(Super.HISTORY_CLOSE_PRICE.get(instruments[i]), 5)/5;//前一天的MA5
				double preMA10 = StatisticsUtil.getClosePriceTotal(Super.HISTORY_CLOSE_PRICE.get(instruments[i]), 10)/10;//前一天的MA10
				double currMA5 = StatisticsUtil.getClosePriceTotal(Super.HISTORY_CLOSE_PRICE.get(instruments[i]), 4)/5 + lastTick[0]/5;//当前的的MA5
				double currMA10 = StatisticsUtil.getClosePriceTotal(Super.HISTORY_CLOSE_PRICE.get(instruments[i]), 9)/10 + lastTick[0]/10;//当前的的MA10
				double [] historyClosePrices = Super.HISTORY_CLOSE_PRICE.get(instruments[i]);
				if(historyClosePrices.length < Super.historyDateRange){
					System.err.println(instruments[i]+"合约的历史数据不完整....");
				}
				
//				System.out.println("instrumentId: "+instruments[i]);
				if(Super.INVESTOR_POSITION.get(instruments[i]) == null){
					//没有持仓该合约
					if((lastTick[0] - highestPpriceOfPeriod) > Global.breakPoint && (currMA5 > currMA10)) {
							//买多
							TraderUtil.orderInsert(instruments[i], true, 5, "0", lastTick[5]);
							System.out.println(instruments[i] + "： 买多1手");
					} else if ((lowestPriceOfPeriod - lastTick[0]) > Global.breakPoint && (currMA10 > currMA5)) {
							//卖空
							TraderUtil.orderInsert(instruments[i], false, 5, "0", lastTick[5]);
							System.out.println(instruments[i] + "： 卖空1手");
					}
				} else {
					//有持仓该合约
					boolean closeFlag1 = false ;//浮亏超过限定值Global.floatSpace
					boolean closeFlag2 = false ;//前一日MA5小于或大于MA10
					boolean closeFlag3 = false ;//当前利润小于最高利润百分比
					InverstorPosition inverstorPostion = Super.INVESTOR_POSITION.get(instruments[i]);
					char c = inverstorPostion.getPosiDirectionType();
					if(c=='2'){//多仓 
						System.out.println(inverstorPostion.getCloseProfitByDate() + " : " + inverstorPostion.getCloseProfitByTrade());
						System.out.println("SettlementID: "+inverstorPostion.getSettlementID());
						System.out.println("多仓 " + instruments[i]);
					} else if(c=='3'){//空仓
						System.out.println(inverstorPostion.getCloseProfitByDate() + " : " + inverstorPostion.getCloseProfitByTrade());
						System.out.println("SettlementID: "+inverstorPostion.getSettlementID());
						System.out.println("空仓" + instruments[i]);
					}
				}
//				lastTick = tickData.get(instruments[i]);
//				if(lastTick != null)
//				System.err.println(instruments[i] + " : " + lastTick[0] + ":" + lastTick[1] + " : " + lastTick[2] + ":" + lastTick[3] + " : " + lastTick[4] + ":" + lastTick[5] + " : " + lastTick[6]);
				
//				TraderUtil.qryPosition(instruments[i]);
//				TraderUtil.orderInsert(instruments[i], false, 5, "0", lastTick[5]);
//				TraderUtil.qryOrder(instruments[i]);
//				TraderUtil.qryTradingAccount();
			}
			
			//遍历持仓
//			Iterator<String> it = Super.INVESTOR_POSITION.keySet().iterator();
//			while(it.hasNext()){
//				InverstorPosition pInvestorPosition = Super.INVESTOR_POSITION.get(it.next());
//				System.out.println("持仓合约: "+pInvestorPosition.getInstrumentID());
//				System.out.println("持仓多空方向: "+pInvestorPosition.getPosiDirectionType());
//				System.out.println("持仓量: "+(pInvestorPosition.getPosition() + pInvestorPosition.getYdposition()));
//				System.out.println("持仓日期: "+pInvestorPosition.getPositionDateType());
//				System.out.println("交易日: "+pInvestorPosition.getTradingDay());
//				System.out.println("持仓盈亏: "+pInvestorPosition.getPositionProfit());
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
