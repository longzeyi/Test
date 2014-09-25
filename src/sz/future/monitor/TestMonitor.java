package sz.future.monitor;

import java.util.Map;

import sz.future.trader.comm.ServerParams;
import sz.future.trader.comm.Super;
import sz.future.util.TraderUtil;

/**
 * @author Sean
 * 行情监测线程
 */
public class TestMonitor extends Thread{

	private Map<String, double[]> tickData;
	private double[] lastTick;
	/**
	 * @param args
	 */
	public void run() {
		String[] instruments = ServerParams.instruments.clone();
		//查询资金使用率
		TraderUtil.qryTradingAccount();
		//查询持仓明细
		TraderUtil.qryPosition("IF1411");
		
		while(true){
			tickData = Super.TICK_DATA;
//			System.out.println(ServerParams.instruments[0]);
			//i=1,因为多线程读取数组第一个元素出现乱码，所以从1开始遍历
			for (int i = 1; i < instruments.length; i++) {
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
