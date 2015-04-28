package sz.future.md.spi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcDepthMarketDataField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcReqUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspInfoField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcSpecificInstrumentField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcUserLogoutField;
import org.hraink.futures.jctp.md.JCTPMdApi;
import org.hraink.futures.jctp.md.JCTPMdSpi;

import sz.future.dao.FutureDevDao;
import sz.future.domain.MdDay;

public class MyMdSpi extends JCTPMdSpi {
	private JCTPMdApi mdApi;
	//保存入库的行情合约
	public static String[] instruments = new String[]{"rb1510","rb1601","cu1506","al1506","au1506","ag1506","bu1506",
		"ru1509","m1509","m1601","y1509","y1601","a1505","a1509","a1601","p1509","p1601","c1509","c1601",
		"jd1509","jd1601","l1509","l1601","pp1509","pp1601","i1509","i1601","SR509","SR601","FG509","FG601","TA509","TA601","RM509","RM601"};
	public static Map<String,MdDay> dayData = new HashMap<String,MdDay>();
	public static FutureDevDao dao = new FutureDevDao();
	
	public MyMdSpi(JCTPMdApi mdApi) {
		this.mdApi = mdApi;
		SaveMd  smd = new SaveMd();
		smd.start();
	}
	
	/**当客户端与交易后台建立起通信连接时（还未登录前），该方法被调用*/
	@Override
	public void onFrontConnected() {
//		System.out.println("sdfsdf");
		//登陆
		CThostFtdcReqUserLoginField userLoginField = new CThostFtdcReqUserLoginField();
//		userLoginField.setBrokerID("005202");
//		userLoginField.setUserID("81001426");
//		userLoginField.setPassword("156613");
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
//		String[] str = new String[]{"IF1401","bu1402"};
//		subResult = mdApi.subscribeMarketData("IF1303");
		subResult = mdApi.subscribeMarketData(instruments);
//		subResult = mdApi.subscribeMarketData("a1405");
		System.out.println(subResult == 0 ? "订阅成功" : "订阅失败");
	}

	private SimpleDateFormat sfDate = new SimpleDateFormat("yyyyMMdd");
	/**深度行情通知*/
	@Override
	public void onRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData) {
		MdDay md = new MdDay();
		md.setClose_price(pDepthMarketData.getClosePrice());
		md.setHighest_price(pDepthMarketData.getHighestPrice());
		md.setInstrumentID(pDepthMarketData.getInstrumentID());
//		md.setLastPrice(pDepthMarketData.getLastPrice());
		md.setLowest_price(pDepthMarketData.getLowestPrice());
		md.setOpen_interest(pDepthMarketData.getOpenInterest());
		md.setOpen_price(pDepthMarketData.getOpenPrice());
		try {
			md.setTradingDay(sfDate.parse(pDepthMarketData.getTradingDay()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		md.setVolume(pDepthMarketData.getVolume());
		
//		System.out.println("dayData.size(): "+this.dayData.size());
//		System.out.println("instruments.length: "+(instruments.length));
		this.dayData.put(md.getInstrumentID(), md);
//		System.out.print(pDepthMarketData.getUpdateTime() + " " + pDepthMarketData.getUpdateMillisec() + "   ");
//		System.out.println(pDepthMarketData.getInstrumentID());
//		TestMd.list.add(pDepthMarketData.getUpdateTime());
		System.out.println("合约代码:"+pDepthMarketData.getInstrumentID());
//    	System.out.println("申卖价1:"+pDepthMarketData.getAskPrice1());
//    	System.out.println("申卖量1:"+pDepthMarketData.getAskVolume1());
//    	System.out.println("申买价1:"+pDepthMarketData.getBidPrice1());
//    	System.out.println("申买量1:"+pDepthMarketData.getBidVolume1());
//    	System.out.println("当日均价:"+pDepthMarketData.getAveragePrice());
//    	System.out.println("今虚实度:"+pDepthMarketData.getCurrDelta());
    	System.out.println("最高价:"+pDepthMarketData.getHighestPrice());
    	System.out.println("最低价:"+pDepthMarketData.getLowestPrice());
//    	System.out.println("最新价:"+pDepthMarketData.getLastPrice());
//    	System.out.println("跌停板价:"+pDepthMarketData.getLowerLimitPrice());
//    	System.out.println("持仓量:"+pDepthMarketData.getOpenInterest());
    	System.out.println("今开盘:"+pDepthMarketData.getOpenPrice());
    	System.out.println("今收盘:"+pDepthMarketData.getClosePrice());
//    	System.out.println("昨收盘:"+pDepthMarketData.getPreClosePrice());
//    	System.out.println("昨虚实度:"+pDepthMarketData.getPreDelta());
//    	System.out.println("昨持仓量:"+pDepthMarketData.getPreOpenInterest());
//    	System.out.println("上次结算价:"+pDepthMarketData.getPreSettlementPrice());
//    	System.out.println("本次结算价:"+pDepthMarketData.getSettlementPrice());
//    	System.out.println("交易日:"+pDepthMarketData.getTradingDay());
//    	System.out.println("成交金额:"+pDepthMarketData.getTurnover());
//    	System.out.println("最后修改毫秒:"+pDepthMarketData.getUpdateMillisec());
//    	System.out.println("最后修改时间:"+pDepthMarketData.getUpdateTime());
//    	System.out.println("涨停板价:"+pDepthMarketData.getUpperLimitPrice());
//    	System.out.println("成交量:"+pDepthMarketData.getVolume());
//    	System.out.println("LIST SIZE: " + TestMd.list.size());
//    	System.err.println("--------------------------------------------------------------------------");
	}

	/**订阅行情应答*/
	@Override
	public void onRspSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument, CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		
		System.out.println("订阅回报:" + bIsLast);
		System.out.println(pRspInfo.getErrorID());
//		System.out.println("InstrumentID:" + pSpecificInstrument.InstrumentID());
	}
	
	@Override
	public void onHeartBeatWarning(int nTimeLapse) {
		System.err.println("onHeartBeatWarning" + nTimeLapse);
	}
	
	@Override
	public void onFrontDisconnected(int nReason) {
		System.err.println("onFrontDisconnected" + nReason);
	}
	
	@Override
	public void onRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		// TODO Auto-generated method stub
		System.err.println("onRspError" + nRequestID + pRspInfo.getErrorMsg());
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

class SaveMd extends Thread {
	public void run(){
		while(true){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(MyMdSpi.dayData.size() == MyMdSpi.instruments.length){
//				Iterator<Entry<String, MdDay>> it = MyMdSpi.dayData.entrySet().iterator();
//				while(it.hasNext()){
//					Entry<String, MdDay> en = it.next();
//					System.out.println(en.getKey() +":"+((MdDay)en.getValue()).getInstrumentID());
//				}
				MyMdSpi.dao.saveMdDayHistory(MyMdSpi.dayData);
				System.exit(0);
			}
		}
	}
}