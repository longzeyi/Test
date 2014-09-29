package sz.future.monitor;

import java.util.Map;

import sz.future.trader.comm.Super;
import sz.future.util.TraderUtil;

/**
 * @author Sean
 * 行情监测线程
 */
public class TestMonitor extends Thread{
	public static final String[] instruments = new String[] {"rb1501","TA501","m1501","FG501","a1501","i1501"};//,"pp1501","sr1501","jd1501","pta1501","fg1501","rm1501"
	private Map<String, double[]> tickData;
	private double[] lastTick;
	/**
	 * @param args
	 */
	public void run() {
//		String[] instruments = ServerParams.instruments.clone();
		//查询资金使用率
		TraderUtil.qryTradingAccount();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		//查询持仓明细
//		TraderUtil.qryPosition("m1501");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		TraderUtil.qryPosition("IF1411");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		//查询组合持仓明细
		TraderUtil.qryInvestorPositionCombine();
		
		while(true){
			tickData = Super.TICK_DATA;
//			System.out.println(ServerParams.instruments[0]);
			for (int i = 0; i < instruments.length; i++) {
				System.out.println("instrumentId: "+instruments[i]);
				lastTick = tickData.get(instruments[i]);
				if(lastTick != null)
				System.err.println(instruments[i] + " : " + lastTick[0] + ":" + lastTick[1] + " : " + lastTick[2] + ":" + lastTick[3] + " : " + lastTick[4] + ":" + lastTick[5] + " : " + lastTick[6]);
				
//				TraderUtil.qryPosition(instruments[i]);
//				TraderUtil.orderInsert(instruments[i], false, 5, "0", lastTick[5]);
//				TraderUtil.qryOrder(instruments[i]);
//				TraderUtil.qryTradingAccount();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
