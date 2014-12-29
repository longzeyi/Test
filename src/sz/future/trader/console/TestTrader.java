package sz.future.trader.console;

import org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_TE_RESUME_TYPE;
import org.hraink.futures.jctp.trader.JCTPTraderApi;
import org.hraink.futures.jctp.trader.JCTPTraderSpi;

import sz.future.trader.comm.ServerParams;

/**
 * @author Sean
 * 交易线程
 */
public class TestTrader extends Thread{
	/** 前置机地址 **/
//	static String frontAddr = "tcp://gtja-front8.financial-trading-platform.com:41205";
//	static String frontAddr = "tcp://ctpmn1-front1.citicsf.com:51205";
//	static String frontAddr = "tcp://front111.ctp.gtjafutures.com:41205";//真实
	
//	static String frontAddr = "tcp://mn104.ctp.gtja-futures.com:41205";//模拟
	
	/** 行情API **/
	public static JCTPTraderApi traderApi;
	public static JCTPTraderSpi traderSpi;

	public void run()  {
		System.err.println("------------正在启动交易线程-------------");
		String dataPath = "c:/ctpdata/";
//		traderApi = JCTPTraderApi.createFtdcTraderApi();
		traderApi = JCTPTraderApi.createFtdcTraderApi(dataPath);//产生一个CThostFtdcTraderApi实例
		traderSpi = new MyTraderSpi(traderApi);//产生一个事件处理的实例
		//注册traderpi
		traderApi.registerSpi(traderSpi);//注册一个事件处理的实例
		//订阅公有流
		traderApi.subscribePublicTopic(THOST_TE_RESUME_TYPE.THOST_TERT_RESTART);
		//订阅私有流
		traderApi.subscribePrivateTopic(THOST_TE_RESUME_TYPE.THOST_TERT_RESTART);
		//注册前置机地址
		traderApi.registerFront(ServerParams.FRONT_ADDR_TR);
		//初始化API与CTP前置服务器连接
		traderApi.init();
		System.err.println("------------交易线程已启动-------------");
		traderApi.join();
//		TimeUnit.SECONDS.sleep(2);
		//回收api和JCTP
		traderApi.release();
	}
}
