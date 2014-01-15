package sz.future.trader.console;

import java.util.concurrent.TimeUnit;

import org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_TE_RESUME_TYPE;
import org.hraink.futures.ctp.thosttraderapi.CThostFtdcTraderApi;
import org.hraink.futures.ctp.thosttraderapi.ThosttraderapiLibrary;
import org.hraink.futures.jctp.trader.JCTPTraderApi;
import org.hraink.futures.jctp.trader.JCTPTraderSpi;
import org.junit.Test;

public class TestTrader {
	/** 前置机地址 **/
//	static String frontAddr = "tcp://gtja-front8.financial-trading-platform.com:41205";
//	static String frontAddr = "tcp://ctpmn1-front1.citicsf.com:51205";
//	static String frontAddr = "tcp://front111.ctp.gtjafutures.com:41205";//真实
	static String frontAddr = "tcp://mn104.ctp.gtja-futures.com:41205";//模拟
	
	/** 行情API **/
	static JCTPTraderApi traderApi;
	static JCTPTraderSpi traderSpi;
	

	public static void main(String[] args) throws InterruptedException {
		String dataPath = "c:/ctpdata/";
		
//		traderApi = JCTPTraderApi.createFtdcTraderApi();
		traderApi = JCTPTraderApi.createFtdcTraderApi(dataPath);
		traderSpi = new MyTraderSpi(traderApi);
		//注册traderpi
		traderApi.registerSpi(traderSpi);
		//订阅公有流
		traderApi.subscribePublicTopic(THOST_TE_RESUME_TYPE.THOST_TERT_RESTART);
		//订阅私有流
		traderApi.subscribePrivateTopic(THOST_TE_RESUME_TYPE.THOST_TERT_RESTART);
		//注册前置机地址
		traderApi.registerFront(frontAddr);
		//初始化API与CTP前置服务器连接
		traderApi.init();
		traderApi.join();
//		TimeUnit.SECONDS.sleep(2);
		//回收api和JCTP
		traderApi.release();
		
	}
}
