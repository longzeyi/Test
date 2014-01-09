package sz.future.md.console;

import org.hraink.futures.jctp.md.JCTPMdApi;

import sz.future.md.spi.Strategy1;

/**
 * @author Sean
 * 模拟环境交易时间:正常交易时间均可交易,每个交易日晚17：30到凌晨5：00也可进行交易，节假日正常情况下都可进行交易
 */
public class TestMd {
	/** 前置机地址 **/
//	static String frontAddr = "tcp://asp-sim2-md1.financial-trading-platform.com:26213";
	static String frontAddr = "tcp://front112.ctp.gtjafutures.com:41213";
//	static String frontAddr = "tcp://front111.ctp.gtjafutures.com:41213";
//	static String frontAddr = "tcp://180.169.30.170:41213";
//	static String frontAddr = "tcp://211.136.142.218:9116";
	/** 行情API **/
	static JCTPMdApi mdApi;
	
	public static void main(String[] args) throws InterruptedException {
		//创建行情API
		mdApi = JCTPMdApi.createFtdcTraderApi();
		//使用策略
		Strategy1 mdSpi = new Strategy1(mdApi);
		//注册spi
		mdApi.registerSpi(mdSpi);
		//注册前置机地址
		mdApi.registerFront(frontAddr);
		mdApi.Init();
		mdApi.Join();
//		TimeUnit.SECONDS.sleep(5);
		mdApi.Release();
	}
}
