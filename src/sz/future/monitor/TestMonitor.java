package sz.future.monitor;

import java.util.Map;

import sz.future.trader.comm.ServerParams;
import sz.future.trader.comm.Super;
import sz.future.util.TraderUtil;

public class TestMonitor extends Thread{

	private Map<String, double[]> tickData;
	private double[] lastTick;
	/**
	 * @param args
	 */
	public void run() {
		while(true){
			tickData = Super.TICK_DATA;
//			System.out.println(ServerParams.instruments[0]);
			for (int i = 0; i < ServerParams.instruments.length; i++) {
//				System.out.println(ServerParams.instruments[i]);
				lastTick = tickData.get(ServerParams.instruments[i]);
				if(lastTick != null)
				System.err.println(ServerParams.instruments[i] + " : " + lastTick[0]);
				
//				TraderUtil.qryPosition(ServerParams.instruments[i]);
				
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
