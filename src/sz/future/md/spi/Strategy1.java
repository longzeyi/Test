package sz.future.md.spi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcDepthMarketDataField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcReqUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspInfoField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcSpecificInstrumentField;
import org.hraink.futures.jctp.md.JCTPMdApi;
import org.hraink.futures.jctp.md.JCTPMdSpi;

import sz.future.conn.DBConnectionManager;
import sz.future.domain.DepthMarketData;
import sz.future.test.Global;
import sz.future.trader.comm.M;
import sz.future.util.TraderUtil;

public class Strategy1 extends JCTPMdSpi {
	
	private JCTPMdApi mdApi;
	
	public Strategy1(JCTPMdApi mdApi) {
		this.mdApi = mdApi;
	}
	
	/**当客户端与交易后台建立起通信连接时（还未登录前），该方法被调用*/
	@Override
	public void onFrontConnected() {
		//登陆
		CThostFtdcReqUserLoginField userLoginField = new CThostFtdcReqUserLoginField();
		userLoginField.setBrokerID(M.brokerId);
		userLoginField.setUserID(M.userId);
		userLoginField.setPassword(M.pwd);
		mdApi.reqUserLogin(userLoginField, 112);
	}
	
	/**登录请求响应*/
	@Override
	public void onRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin, CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
//		System.out.println("登录回调");
//		System.out.println("登录时间："+pRspUserLogin.getLoginTime());
		//订阅
		int subResult = -1;
		subResult = mdApi.subscribeMarketData(M.instrumentId);
		System.out.println(subResult == 0 ? "订阅成功" : "订阅失败");
	}

	/**深度行情通知*/
	@Override
	public void onRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData) {
		System.out.println(pDepthMarketData.getUpdateTime() + " " + pDepthMarketData.getUpdateMillisec() + "   ");
		printMd(pDepthMarketData);
		if (M.lowerLimitPrice==0 || M.upperLimitPrice==0){
			M.lowerLimitPrice = pDepthMarketData.getLowerLimitPrice();
			M.upperLimitPrice = pDepthMarketData.getUpperLimitPrice();
		}
    	cached(pDepthMarketData.getLastPrice(), pDepthMarketData.getBidVolume1(), pDepthMarketData.getAskPrice1());
    	M.j++;
    	if(M.count > M.calculateThreshold && M.j > M.interval){
    		calculate();
    	}
    	
    	
//    	persistence(createData(pDepthMarketData));
	}

	/**订阅行情应答*/
	@Override
	public void onRspSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument, CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		System.out.println("订阅回报:" + bIsLast + "InstrumentID:" + pSpecificInstrument.getInstrumentID() + "ErrorID:" + pRspInfo.getErrorID());
	}

	public int calculate() {
		int returnMsg = 0;
		System.err.println("================================================+");
			int i = M.count;
			//第一次入场
			if (Global.positionPrice == 0){
				double enterFlag = M.priceArray[i-M.interval] - M.priceArray[i] ;
				if (enterFlag > 1) {
					returnMsg = TraderUtil.orderInsert(M.instrumentId, false, 1, "0", M.lowerLimitPrice);//开仓卖空
					System.out.println("a: "+ TraderUtil.qryTrade());
//					shortSelling(Global.priceB1Array[i]);//卖空
				} else if (enterFlag < 1) {
					returnMsg = TraderUtil.orderInsert(M.instrumentId, true, 1, "0", M.upperLimitPrice);//开仓买多
					System.out.println("b: "+ TraderUtil.qryTrade());
//					buyingLong(Global.priceS1Array[i]);//买多
				}
				return returnMsg;
			}
//    		TraderUtil.orderInsert(M.instrumentId, true, 1, "3", M.upperLimitPrice);//平今天卖空的订单
//    		TraderUtil.orderInsert(M.instrumentId, false, 1, "3", M.lowerLimitPrice);//平今天买多的订单
			
//			double b = Global.priceArray[i-12];
//			double c = Global.priceArray[i-6];
//			double d = Global.priceArray[i-2];
//			
			M.level3 = M.priceArray[i-12];
			M.level2 = M.priceArray[i-6];
			M.level1 = M.priceArray[i-2];
			
			if((M.level3 < M.level2) && (M.level2 < M.level1) && (M.level1 < Global.priceArray[i]) && (Global.priceArray[i] - M.level3) > Global.floatSpace){//up
//				TraderUtil.orderInsert(M.instrumentId, true, 1, "3", M.upperLimitPrice);
//				//平仓买多
//				closeOutPosition(Global.priceB1Array[i], Global.priceS1Array[i]);
//				buyingLong(Global.priceS1Array[i]);
			}
			if ((M.level3 > M.level2) && (M.level2 > M.level1) && (M.level1 > Global.priceArray[i]) && (Global.priceArray[i] - M.level3) < Global.floatSpace) {//down
//				TraderUtil.orderInsert(M.instrumentId, false, 1, "3", M.lowerLimitPrice);
//				//平仓卖空
//				closeOutPosition(Global.priceB1Array[i], Global.priceS1Array[i]);
//				shortSelling(Global.priceB1Array[i]);
			}
		return 0;
	}

	public void cached(double lastPrice, double bPrice, double sPrice) {
		M.priceArray[M.count] = lastPrice;
		M.priceB1Array[M.count] = bPrice;
		M.priceS1Array[M.count] = sPrice;
		M.count++;
	}

	public void persistence(Object obj) {
		Connection conn = DBConnectionManager.getConnection();
		DepthMarketData data = (DepthMarketData)obj;
		PreparedStatement pstm = null;
		try {
			String sql = "INSERT INTO tb_fu_market_data VALUES (NULL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			conn.setAutoCommit(false);
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, data.getInstrumentID());
			pstm.setString(2, data.getTradingDay());
			pstm.setString(3, data.getUpdateTime());
			pstm.setDouble(4, data.getAskPrice1());
			pstm.setDouble(5, data.getBidPrice1());
			pstm.setDouble(6, data.getClosePrice());
			pstm.setDouble(7, data.getAveragePrice());
			pstm.setDouble(8, data.getCurrDelta());
			pstm.setDouble(9, data.getHighestPrice());
			pstm.setDouble(10, data.getLowestPrice());
			pstm.setDouble(11, data.getLastPrice());
			pstm.setDouble(12, data.getLowerLimitPrice());
			pstm.setDouble(13, data.getOpenInterest());
			pstm.setDouble(14, data.getOpenPrice());
			pstm.setDouble(15, data.getPreClosePrice());
			pstm.setDouble(16, data.getPreDelta());
			pstm.setDouble(17, data.getPreOpenInterest());
			pstm.setDouble(18, data.getPreSettlementPrice());
			pstm.setDouble(19, data.getSettlementPrice());
			pstm.setDouble(20, data.getTurnover());
			pstm.setDouble(21, data.getUpperLimitPrice());
			pstm.setInt(22, data.getAskVolume1());
			pstm.setInt(23, data.getBidVolume1());
			pstm.setInt(24, data.getUpdateMillisec());
			pstm.setInt(25, data.getVolume());
			pstm.executeUpdate();
			conn.commit();
			System.err.println(data.getUpdateTime() +" : "+data.getInstrumentID()+"  data has been saved.......");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closePreparedStatement(pstm);
			DBConnectionManager.closeConnection(conn);
		}
	}

	private void printMd(CThostFtdcDepthMarketDataField pDepthMarketData){
		System.out.println("合约代码:"+pDepthMarketData.getInstrumentID());
    	System.out.println("申卖价1:"+pDepthMarketData.getAskPrice1());
    	System.out.println("申卖量1:"+pDepthMarketData.getAskVolume1());
    	System.out.println("申买价1:"+pDepthMarketData.getBidPrice1());
    	System.out.println("申买量1:"+pDepthMarketData.getBidVolume1());
//    	System.out.println("今收盘:"+pDepthMarketData.getClosePrice());
//    	System.out.println("当日均价:"+pDepthMarketData.getAveragePrice());
//    	System.out.println("今虚实度:"+pDepthMarketData.getCurrDelta());
//    	System.out.println("最高价:"+pDepthMarketData.getHighestPrice());
//    	System.out.println("最低价:"+pDepthMarketData.getLowestPrice());
    	System.out.println("最新价:"+pDepthMarketData.getLastPrice());
    	System.out.println("跌停板价:"+pDepthMarketData.getLowerLimitPrice());
    	System.out.println("涨停板价:"+pDepthMarketData.getUpperLimitPrice());
//    	System.out.println("持仓量:"+pDepthMarketData.getOpenInterest());
//    	System.out.println("今开盘:"+pDepthMarketData.getOpenPrice());
//    	System.out.println("昨收盘:"+pDepthMarketData.getPreClosePrice());
//    	System.out.println("昨虚实度:"+pDepthMarketData.getPreDelta());
//    	System.out.println("昨持仓量:"+pDepthMarketData.getPreOpenInterest());
//    	System.out.println("上次结算价:"+pDepthMarketData.getPreSettlementPrice());
//    	System.out.println("本次结算价:"+pDepthMarketData.getSettlementPrice());
    	System.out.println("交易日:"+pDepthMarketData.getTradingDay());
//    	System.out.println("成交金额:"+pDepthMarketData.getTurnover());
//    	System.out.println("最后修改毫秒:"+pDepthMarketData.getUpdateMillisec());
//    	System.out.println("最后修改时间:"+pDepthMarketData.getUpdateTime());
    	System.out.println("成交量:"+pDepthMarketData.getVolume());
	}
	
//	private DepthMarketData createData(CThostFtdcDepthMarketDataField pDepthMarketData){
//		DepthMarketData data = new DepthMarketData();
//    	data.setInstrumentID(pDepthMarketData.getInstrumentID());
//    	data.setAskPrice1(pDepthMarketData.getAskPrice1());
//    	data.setAskVolume1(pDepthMarketData.getAskVolume1());
//    	data.setAveragePrice(pDepthMarketData.getAveragePrice());
//    	data.setBidPrice1(pDepthMarketData.getBidPrice1());
//    	data.setBidVolume1(pDepthMarketData.getBidVolume1());
////    	data.setClosePrice(pDepthMarketData.getClosePrice());
////    	data.setCurrDelta(pDepthMarketData.getCurrDelta());
//    	data.setHighestPrice(pDepthMarketData.getHighestPrice());
//    	data.setLowestPrice(pDepthMarketData.getLowestPrice());
//    	data.setLastPrice(pDepthMarketData.getLastPrice());
//    	data.setLowerLimitPrice(pDepthMarketData.getLowerLimitPrice());
//    	data.setOpenInterest(pDepthMarketData.getOpenInterest());
//    	data.setOpenPrice(pDepthMarketData.getOpenPrice());
//    	data.setPreClosePrice(pDepthMarketData.getPreClosePrice());
////    	data.setPreDelta(pDepthMarketData.getPreDelta());
////    	data.setPreOpenInterest(pDepthMarketData.getPreOpenInterest());
////    	data.setPreSettlementPrice(pDepthMarketData.getPreSettlementPrice());
////    	data.setSettlementPrice(pDepthMarketData.getSettlementPrice());
//    	data.setTradingDay(pDepthMarketData.getTradingDay());
////    	data.setTurnover(pDepthMarketData.getTurnover());
//    	data.setUpdateMillisec(pDepthMarketData.getUpdateMillisec());
//    	data.setUpdateTime(pDepthMarketData.getUpdateTime());
//    	data.setUpperLimitPrice(pDepthMarketData.getUpperLimitPrice());
//    	data.setVolume(pDepthMarketData.getVolume());
//		return data;
//	}
	

}
