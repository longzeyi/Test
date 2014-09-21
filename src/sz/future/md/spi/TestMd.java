package sz.future.md.spi;

import org.hraink.futures.jctp.md.JCTPMdApi;

import sz.future.md.spi.MyMdSpi;
import sz.future.trader.comm.ServerParams;

public class TestMd {

	/**
	 * 抓取日行情
	 * @param args
	 */
	static JCTPMdApi mdApi;
	
	public static void main(String[] args) {
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
