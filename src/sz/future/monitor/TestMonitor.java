package sz.future.monitor;

import java.util.List;
import java.util.Map;

import sz.future.dao.FutureDevDao;
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
		instruments = new String[ServerParams.instruments.length];
		for (int i = 0; i < ServerParams.instruments.length; i++) {
			instruments[i] = ServerParams.instruments[i];
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
		while(true){
			tickData = Super.TICK_DATA;
//			System.out.println(ServerParams.instruments[0]);
			for (int i = 0; i < instruments.length; i++) {
				Double highestPpriceOfPeriod = dao.getLimitPrice(Global.period, instruments[i], 1);
				Double lowestPriceOfPeriod = dao.getLimitPrice(Global.period, instruments[i], 2);
				double preMa5 = dao.getPreMA(5, instruments[i]);//前一天的MA5
				double preMa10 = dao.getPreMA(10, instruments[i]);//前一天的MA10
				double [] historyClosePrices = Super.HISTORY_CLOSE_PRICE.get(instruments[i]);
				if(historyClosePrices.length < Super.historyDateRange){
					System.err.println(instruments[i]+"合约的历史数据不完整....");
				}
				lastTick = tickData.get(instruments[i]);//获取当前合约的最新行情
				if(lastTick == null){
					continue;
				}
				//写几个统计各种ma的方法
//				System.out.println("instrumentId: "+instruments[i]);
				if(Super.INVESTOR_POSITION.get(instruments[i]) == null){
					//没有持仓该合约
					if((lastTick[0] - highestPpriceOfPeriod) > Global.breakPoint && (currMA5 > currMA10)) {
							trader(Global.priceB1Array[i],Global.priceS1Array[i],true,true);
					} else if ((lowestPriceOfPeriod - lastTick[0]) > Global.breakPoint && (currMA10 > currMA5)) {
							trader(Global.priceB1Array[i],Global.priceS1Array[i],true,false);
					}
				} else {
					//有持仓该合约
					
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
