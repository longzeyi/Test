package sz.future.md.console;

import org.hraink.futures.jctp.md.JCTPMdApi;

import sz.future.trader.comm.ServerParams;

/**
 * @author Sean
 * 行情采集线程
 */
public class TestMd extends Thread{
	/** 行情API **/
	static JCTPMdApi mdApi;
	
	public void run() {
		//创建行情API
		mdApi = JCTPMdApi.createFtdcTraderApi();
		//产生一个事件处理的实例
		MyMdSpi mdSpi = new MyMdSpi(mdApi);
		//注册事件处理的实例
		mdApi.registerSpi(mdSpi);
		//注册前置机地址
		mdApi.registerFront(ServerParams.FRONT_ADDR_MD);
		mdApi.Init();
		mdApi.Join();
//		TimeUnit.SECONDS.sleep(5);
		mdApi.Release();
	}
}
