package sz.future.md.console;

import org.hraink.futures.jctp.md.JCTPMdApi;

import sz.future.trader.console.TestTrader;

/**
 * @author Sean
 * 模拟环境交易时间:正常交易时间均可交易,每个交易日晚17：30到凌晨5：00也可进行交易，节假日正常情况下都可进行交易
 */
public class TestMd {
	/** 行情前置机地址 **/
//	static String frontAddr = "tcp://asp-sim2-md1.financial-trading-platform.com:26213";
//	static String frontAddr = "tcp://front112.ctp.gtjafutures.com:41213";
//	static String frontAddr = "tcp://front111.ctp.gtjafutures.com:41213";
	static String frontAddr = "tcp://mn101.ctp.gtja-futures.com:41213";
//	static String frontAddr = "tcp://gtja-md3.financial-trading-platform.com:41213";
//	static String frontAddr = "tcp://27.115.57.57:41213";
	//美尔雅期货行情前置
//	static String frontAddr = "tcp://sh-front11.168qh.com:41213";
//	static String frontAddr = "tcp://sh-front12.168qh.com:41213";
//	static String frontAddr = "tcp://sh-front13.168qh.com:41213";
//	static String frontAddr = "tcp://sh-front14.168qh.com:41213";
//	static String frontAddr = "tcp://sh-front19.168qh.com:41213";
//	static String frontAddr = "tcp://218.104.106.85:41213";
//	static String frontAddr = "tcp://218.104.106.82:41213";
//	static String frontAddr = "tcp://sh-front11.mfc.com.cn:41213";
//	static String frontAddr = "tcp://sh-front12.mfc.com.cn:41213";
//	static String frontAddr = "tcp://sh-front13.mfc.com.cn:41213";
//	static String frontAddr = "tcp://sh-front14.mfc.com.cn:41213";
//	static String frontAddr = "tcp://sh-front19.mfc.com.cn:41213";
	
	/** 行情API **/
	static JCTPMdApi mdApi;
	
	public static void main(String[] args) throws InterruptedException {
		TestTrader tt = new TestTrader();
		//启动交易线程
		tt.start();
		Thread.sleep(2000);
		//创建行情API
		mdApi = JCTPMdApi.createFtdcTraderApi();
		//使用策略
		MyMdSpi mdSpi = new MyMdSpi(mdApi);
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
