package sz.future.monitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import sz.future.dao.FutureDevDao;
import sz.future.domain.InverstorPosition;
import sz.future.trader.comm.ServerParams;
import sz.future.trader.comm.Super;
import sz.future.util.StatisticsUtil;
import sz.future.util.TraderUtil;

/**
 * 行情监测线程
 */
public class TestMonitor extends Thread{
	private String[] instruments ;
	private Map<String, double[]> tickData;
	private double[] lastTick;
	private FutureDevDao dao = new FutureDevDao();
	
	public TestMonitor(){
		//克隆合约数组
		instruments = new String[ServerParams.instruments2.length];
		for (int i = 0; i < ServerParams.instruments2.length; i++) {
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
				System.err.println("重新开始查询资金和全部持仓明细");
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
		
		if(TraderUtil.qryPositionDetail()==0){
			bool = true;
			System.out.println("查询全部持仓明细成功");
		} else {
			bool = false;
			System.err.println("查询全部持仓明细失败");
		}
		
		if(TraderUtil.qryTrade()==0){
			bool = true;
			System.out.println("查询全部成交成功");
		} else {
			bool = false;
			System.err.println("查询全部成交失败");
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
				lastTick = tickData.get(instruments[i]);//获取当前合约的最新行情
				if(lastTick == null){
					System.err.println(instruments[i]+" TICK行情丢失1");
					continue;
				} else if (lastTick[0] ==0){
					System.err.println(instruments[i]+" TICK行情丢失2");
					break;
				}
//				System.out.println("监测"+instruments[i] + " 价格：" +lastTick[0]);
				Double highestPpriceOfPeriod = dao.getLimitPrice(ServerParams.period, instruments[i], 1);
				Double lowestPriceOfPeriod = dao.getLimitPrice(ServerParams.period, instruments[i], 2);
				double preMA5 = StatisticsUtil.getClosePriceTotal(Super.HISTORY_CLOSE_PRICE.get(instruments[i]), 5)/5;//前一天的MA5
				double preMA10 = StatisticsUtil.getClosePriceTotal(Super.HISTORY_CLOSE_PRICE.get(instruments[i]), 10)/10;//前一天的MA10
				double currMA5 = StatisticsUtil.getClosePriceTotal(Super.HISTORY_CLOSE_PRICE.get(instruments[i]), 4)/5 + lastTick[0]/5;//当前的的MA5
				double currMA10 = StatisticsUtil.getClosePriceTotal(Super.HISTORY_CLOSE_PRICE.get(instruments[i]), 9)/10 + lastTick[0]/10;//当前的的MA10
				double [] historyClosePrices = Super.HISTORY_CLOSE_PRICE.get(instruments[i]);
				if(historyClosePrices.length < Super.historyDateRange){
					System.err.println(instruments[i]+"合约的历史数据不完整....");
				}
//				System.out.println("instrumentId: "+instruments[i]);
				//有持仓该合约
				boolean closeFlag1 = false ;//浮亏超过限定值Global.floatSpace
				boolean closeFlag2 = false ;//前一日MA5小于或大于MA10
				boolean closeFlag3 = false ;//当前利润小于最高利润百分比
				InverstorPosition ip0 = Super.INVESTOR_POSITION.get(instruments[i]+"_0");
				InverstorPosition ip1 = Super.INVESTOR_POSITION.get(instruments[i]+"_1");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Date openDate = null;
				if(ip0!=null){//多仓 
					try {
						openDate = sdf.parse(ip0.getOpenDate());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					double price = dao.getLimitPriceBy(openDate, instruments[i], 4, true);//持仓期中最高价
					if(price > 0 && (lastTick[0] - ip0.getOpenPrice()) < (price * ServerParams.retracement)){
						closeFlag3 = true;
					}
					if(preMA5 < preMA10){
						closeFlag2 = true;
					}
					closeFlag1 = (ip0.getOpenAvgPrice() - lastTick[0]) > ServerParams.floatSpace*lastTick[0];
					if(closeFlag1||(closeFlag2&&closeFlag3)){
						if(closeFlag1)System.out.println("【closeFlag1】 亏损超过止损值： " + (ip0.getOpenAvgPrice() - lastTick[0]) + ">" + (ServerParams.floatSpace*lastTick[0]));
						if(closeFlag2)System.out.println("【closeFlag2】 5日均线小于10日均线： " +  preMA5 + " < " + preMA10);
						if(closeFlag3)System.out.println("【closeFlag3】 盈利回撤到跟踪止损位： " +  (lastTick[0] - ip0.getOpenPrice()) + " < " + (price * ServerParams.retracement));
						if(ip0.getTdPosition() > 0){//平今仓
							TraderUtil.orderInsert(instruments[i], false, ip0.getTdPosition(), "3", lastTick[6]);
						} 
						if (ip0.getYdPosition() > 0){//平昨仓
							TraderUtil.orderInsert(instruments[i], false, ip0.getYdPosition(), "1", lastTick[6]);
						}
					}
				} else if(ip1!=null){//空仓
					try {
						openDate = sdf.parse(ip1.getOpenDate());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					double price = dao.getLimitPriceBy(openDate, instruments[i], 4, false);//持仓期中最高价
					if(price > 0 && (ip1.getOpenPrice() - lastTick[0]) < (price * ServerParams.retracement)){
						closeFlag3 = true;
					}
					if(preMA5 > preMA10){
						closeFlag2 = true;
					}
					closeFlag1 = (lastTick[0] - ip1.getOpenAvgPrice()) > ServerParams.floatSpace*lastTick[0];
					if(closeFlag1||(closeFlag2&&closeFlag3)){
						if(closeFlag1)System.out.println("【closeFlag1】 亏损超过止损值： " + (lastTick[0] - ip1.getOpenAvgPrice()) + ">" + (ServerParams.floatSpace*lastTick[0]));
						if(closeFlag2)System.out.println("【closeFlag2】 5日均线大于10日均线： " +  preMA5 + " > " + preMA10);
						if(closeFlag3)System.out.println("【closeFlag3】 盈利回撤到跟踪止损位： " +  (ip1.getOpenPrice() - lastTick[0]) + " < " + (price * ServerParams.retracement));
						if(ip1.getTdPosition() > 0){//平今仓
							TraderUtil.orderInsert(instruments[i], true, ip1.getTdPosition(), "3", lastTick[5]);
						}
						if (ip1.getYdPosition() > 0){//平昨仓
							TraderUtil.orderInsert(instruments[i], true, ip1.getYdPosition(), "1", lastTick[5]);
						}
					}
				} else {
					//没有持仓该合约
					if((lastTick[0] - highestPpriceOfPeriod) > ServerParams.breakPoint && (currMA5 > currMA10) && !Super.TODAY_TRADE.contains(instruments[i])) {
						//开仓买多
						TraderUtil.orderInsert(instruments[i], true, 3, "0", lastTick[5]);
					} else if ((lowestPriceOfPeriod - lastTick[0]) > ServerParams.breakPoint && (currMA10 > currMA5) && !Super.TODAY_TRADE.contains(instruments[i])) {
						//开仓卖空
						TraderUtil.orderInsert(instruments[i], false, 3, "0", lastTick[6]);
					}
				}
				System.err.print(instruments[i] + " : " + lastTick[0] + "\t");
//				System.err.println(instruments[i] + " : " + lastTick[0] + ":" + lastTick[1] + " : " + lastTick[2] + ":" + lastTick[3] + " : " + lastTick[4] + ":" + lastTick[5] + " : " + lastTick[6]);
			}
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
