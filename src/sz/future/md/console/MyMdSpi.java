package sz.future.md.console;

import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcDepthMarketDataField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcReqUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspInfoField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcSpecificInstrumentField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcUserLogoutField;
import org.hraink.futures.jctp.md.JCTPMdApi;
import org.hraink.futures.jctp.md.JCTPMdSpi;

import sz.future.trader.comm.ServerParams;
import sz.future.trader.comm.Super;

public class MyMdSpi extends JCTPMdSpi {
	private JCTPMdApi mdApi;
	private int count ;
	
	public MyMdSpi(JCTPMdApi mdApi) {
		this.mdApi = mdApi;
	}
	
	/**当客户端与交易后台建立起通信连接时（还未登录前），该方法被调用*/
	@Override
	public void onFrontConnected() {
		System.out.println("onFrontConnected被调用");
		//登陆
		CThostFtdcReqUserLoginField userLoginField = new CThostFtdcReqUserLoginField();
//		userLoginField.setBrokerID("7090");
//		userLoginField.setUserID("81001426");
//		userLoginField.setPassword("");
		mdApi.reqUserLogin(userLoginField, 112);
	}
	
	/**登录请求响应*/
	@Override
	public void onRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin, CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		System.out.println("登录回调");
		System.out.println(pRspUserLogin.getLoginTime());
		//订阅
		int subResult = -1;
//		subResult = mdApi.subscribeMarketData("IF1303");
		//发送行情订阅请求
		subResult = mdApi.subscribeMarketData(ServerParams.instruments);
		System.out.println(subResult == 0 ? "订阅成功" : "订阅失败");
	}

	/**深度行情通知*/
	@Override
	public void onRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData) {
		boolean bool = false;
//		System.out.print(pDepthMarketData.getUpdateTime() + " ==== " + pDepthMarketData.getUpdateMillisec() + "   ");
		double[] md = Super.TICK_DATA.get(pDepthMarketData.getInstrumentID());
		if(md[0] != pDepthMarketData.getLastPrice() && pDepthMarketData.getLastPrice() < 100000){
			bool = true;
			md[0] = pDepthMarketData.getLastPrice();
			if(md[1] != pDepthMarketData.getHighestPrice() && pDepthMarketData.getHighestPrice() < 100000){
				md[1] = pDepthMarketData.getHighestPrice();
			}
			if(md[2] != pDepthMarketData.getLowestPrice() && pDepthMarketData.getLowestPrice() < 100000){
				md[2] = pDepthMarketData.getLowestPrice();
			}
		}
		count++;//计数
		if(count < 100){
//			System.out.println(count);
			md[3] = pDepthMarketData.getOpenPrice();
			md[4] = pDepthMarketData.getPreClosePrice();
			md[5] = pDepthMarketData.getUpperLimitPrice();
			md[6] = pDepthMarketData.getLowerLimitPrice();
		}
		if(bool){
//			System.out.println(pDepthMarketData.getInstrumentID()+"*****"+md[0]);
			Super.TICK_DATA.put(pDepthMarketData.getInstrumentID(), md);
		}
//		System.out.println(pDepthMarketData.getInstrumentID() + " : " + pDepthMarketData.getLastPrice());
		
//		System.out.println(pDepthMarketData.getInstrumentID());
//		System.out.println("最新价:"+pDepthMarketData.getLastPrice());
//		System.out.println("最高价:"+pDepthMarketData.getHighestPrice());
//		System.out.println("最低价:"+pDepthMarketData.getLowestPrice());
//		System.out.println("今开盘:"+pDepthMarketData.getOpenPrice());
//		System.out.println("昨收盘:"+pDepthMarketData.getPreClosePrice());
//		System.out.println("涨停板价:"+pDepthMarketData.getUpperLimitPrice());
//		System.out.println("跌停板价:"+pDepthMarketData.getLowerLimitPrice());
		
		
//    	System.out.println("申卖价1:"+pDepthMarketData.getAskPrice1());
//    	System.out.println("申卖量1:"+pDepthMarketData.getAskVolume1());
//    	System.out.println("申买价1:"+pDepthMarketData.getBidPrice1());
//    	System.out.println("申买量1:"+pDepthMarketData.getBidVolume1());
//    	System.out.println("当日均价:"+pDepthMarketData.getAveragePrice());
//    	System.out.println("今虚实度:"+pDepthMarketData.getCurrDelta());
//		System.out.println("今收盘:"+pDepthMarketData.getClosePrice());
//    	System.out.println("持仓量:"+pDepthMarketData.getOpenInterest());
//    	System.out.println("昨虚实度:"+pDepthMarketData.getPreDelta());
//    	System.out.println("昨持仓量:"+pDepthMarketData.getPreOpenInterest());
//    	System.out.println("上次结算价:"+pDepthMarketData.getPreSettlementPrice());
//    	System.out.println("本次结算价:"+pDepthMarketData.getSettlementPrice());
		
//    	System.out.println("交易日:"+pDepthMarketData.getTradingDay());
//    	System.out.println("成交金额:"+pDepthMarketData.getTurnover());
//    	System.out.println("最后修改毫秒:"+pDepthMarketData.getUpdateMillisec());
//    	System.out.println("最后修改时间:"+pDepthMarketData.getUpdateTime());
//    	System.out.println("成交量:"+pDepthMarketData.getVolume());
//    	System.err.println("--------------------------------------------------------------------------");
	}

	/**订阅行情应答*/
	@Override
	public void onRspSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument, CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		
//		System.out.println("订阅回报:" + bIsLast);
//		System.out.println(pRspInfo.getErrorID());
//		System.out.println("InstrumentID:" + pSpecificInstrument.InstrumentID());
	}
	
	@Override
	public void onHeartBeatWarning(int nTimeLapse) {
	}
	
	@Override
	public void onFrontDisconnected(int nReason) {
	}
	
	@Override
	public void onRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		// TODO Auto-generated method stub
	}
	
	/**取消订阅行情应答*/
	@Override
	public void onRspUnSubMarketData(
			CThostFtdcSpecificInstrumentField pSpecificInstrument,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		// TODO Auto-generated method stub
	}
	
	/**登出请求响应*/
	@Override
	public void onRspUserLogout(CThostFtdcUserLogoutField pUserLogout,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		// TODO Auto-generated method stub
	}
}