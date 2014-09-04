package sz.future;

import sz.future.md.console.TestMd;
import sz.future.monitor.TestMonitor;
import sz.future.trader.console.TestTrader;

public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		TestMd tm = new TestMd();
		TestTrader tt = new TestTrader();
		TestMonitor tmo = new TestMonitor();
		tm.start();
		Thread.sleep(2000);
		tt.start();
		Thread.sleep(2000);
		tmo.start();
	}

}
