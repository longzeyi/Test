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
		TestMd tm = new TestMd();//行情采集线程
		tm.start();
		Thread.sleep(3000);
		TestTrader tt = new TestTrader();//交易线程
		tt.start();
		Thread.sleep(3000);
		TestMonitor tmo = new TestMonitor();//行情监测线程
		Thread.sleep(3000);
		tmo.start();
	}

}
