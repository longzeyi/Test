package sz.future.md.spi;

import java.util.Date;
import java.util.TimerTask;

import org.hraink.futures.jctp.md.JCTPMdApi;

import sz.future.trader.comm.ServerParams;

public class TestMd extends TimerTask{

	/**
	 * 抓取日行情
	 * @param args
	 */
	static JCTPMdApi mdApi;
	
	public void run(){
		System.err.println(new Date().toLocaleString() + ": 开始执行!");
		//创建行情API
		mdApi = JCTPMdApi.createFtdcTraderApi();
		//产生一个事件处理的实例
		MyMdSpi mdSpi = new MyMdSpi(mdApi);
		//注册事件处理的实例
		mdApi.registerSpi(mdSpi);
		//注册前置机地址
		mdApi.registerFront("tcp://sh-front11.168qh.com:41213");
		mdApi.Init();
		mdApi.Join();
//		TimeUnit.SECONDS.sleep(5);
		mdApi.Release();
		System.err.println(new Date().toLocaleString() + ": 执行结束!");
	}

}

