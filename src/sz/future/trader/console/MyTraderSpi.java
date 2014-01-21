package sz.future.trader.console;


import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.*;

import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderActionField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInvestorPositionDetailField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInvestorPositionField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcQryInvestorPositionDetailField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcReqUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspInfoField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcSettlementInfoConfirmField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcTradeField;
import org.hraink.futures.jctp.trader.JCTPTraderApi;
import org.hraink.futures.jctp.trader.JCTPTraderSpi;

/**
 * Custom TraderSpi
 * 
 * @author Hraink E-mail:Hraink@Gmail.com
 * @version 2013-1-25 下午11:46:13
 */
public class MyTraderSpi extends JCTPTraderSpi {

	JCTPTraderApi traderApi;
	int nRequestID = 0;
	
	//国泰君安
	String brokerId = "7090";
	String userId = "5092581";
	String password = "888888";
	
	public MyTraderSpi(JCTPTraderApi traderApi) {
		this.traderApi = traderApi;
	}
	@Override
	public void onFrontConnected() {
		System.out.println("前置机连接");
		CThostFtdcReqUserLoginField userLoginField = new CThostFtdcReqUserLoginField();
		
		userLoginField.setBrokerID(brokerId);
		userLoginField.setUserID(userId);
		userLoginField.setPassword(password);
		
		traderApi.reqUserLogin(userLoginField, 112);
		
		CThostFtdcInputOrderField pInputOrder = new CThostFtdcInputOrderField();
		
		
		traderApi.reqOrderInsert(pInputOrder, ++nRequestID);
	}
	
	@Override
	public void onRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		System.out.println("TradingDay:" + traderApi.getTradingDay());
		System.out.println(pRspInfo.getErrorID());
		System.out.println(pRspUserLogin.getLoginTime());
		System.out.println(pRspUserLogin.getCZCETime());
		System.out.println(pRspUserLogin.getDCETime());
		System.out.println(pRspUserLogin.getFFEXTime());
		System.out.println(pRspUserLogin.getSHFETime());
		System.out.println(pRspUserLogin.getMaxOrderRef());
		
		//查询持仓明细
		CThostFtdcQryInvestorPositionDetailField positionField = new CThostFtdcQryInvestorPositionDetailField();
		positionField.setBrokerID(brokerId);
		positionField.setInstrumentID("cu1303");
		positionField.setInvestorID(userId);
		traderApi.reqQryInvestorPositionDetail(positionField, ++nRequestID);
		
		
		//确认结算单
		CThostFtdcSettlementInfoConfirmField confirmField = new CThostFtdcSettlementInfoConfirmField();
		traderApi.reqSettlementInfoConfirm(confirmField, ++nRequestID);

		
		//下单操作
		CThostFtdcInputOrderField inputOrderField=new CThostFtdcInputOrderField();
		//期货公司代码
		inputOrderField.setBrokerID(brokerId);
		//投资者代码
		inputOrderField.setInvestorID(userId);
		// 合约代码
		inputOrderField.setInstrumentID("ru1306");
		///报单引用
		inputOrderField.setOrderRef("000000000001");
		// 用户代码
		inputOrderField.setUserID(userId);
		// 报单价格条件
		inputOrderField.setOrderPriceType(THOST_FTDC_OPT_LimitPrice);
		// 买卖方向
		inputOrderField.setDirection(THOST_FTDC_D_Buy);
		// 组合开平标志
		inputOrderField.setCombOffsetFlag("0");
		// 组合投机套保标志
		inputOrderField.setCombHedgeFlag("1");
		// 价格
		inputOrderField.setLimitPrice(24490);
		// 数量
		inputOrderField.setVolumeTotalOriginal(10);
		// 有效期类型
		inputOrderField.setTimeCondition(THOST_FTDC_TC_GFD);
		// GTD日期
		inputOrderField.setGTDDate("");
		// 成交量类型
		inputOrderField.setVolumeCondition(THOST_FTDC_VC_AV);
		// 最小成交量
		inputOrderField.setMinVolume(0);
		// 触发条件
		inputOrderField.setContingentCondition(THOST_FTDC_CC_Immediately);
		// 止损价
		inputOrderField.setStopPrice(0);
		// 强平原因
		inputOrderField.setForceCloseReason(THOST_FTDC_FCC_NotForceClose);
		// 自动挂起标志
		inputOrderField.setIsAutoSuspend(0);
		
		traderApi.reqOrderInsert(inputOrderField, ++nRequestID);
	}
	
	@Override
	public void onRtnOrder(CThostFtdcOrderField pOrder) {
		System.out.println(pOrder.getStatusMsg());
	}
	
	@Override
	public void onRspOrderInsert(CThostFtdcInputOrderField pInputOrder,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		System.out.println(pRspInfo.getErrorMsg());
	}
	
	@Override
	public void onRspOrderAction(
			CThostFtdcInputOrderActionField pInputOrderAction,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		System.out.println(pRspInfo.getErrorMsg());
	}
	
	@Override
	public void onRtnTrade(CThostFtdcTradeField pTrade) {
		System.out.println(pTrade.getInstrumentID());
	}
	
	@Override
	public void onRspQryInvestorPositionDetail(
			CThostFtdcInvestorPositionDetailField pInvestorPositionDetail,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		System.out.println("持仓明细查询回调");
	}
	
	
	@Override
	public void onRspQryInvestorPosition(
			CThostFtdcInvestorPositionField pInvestorPosition,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		System.out.println("持仓查询回调");
	}

	@Override
	public void onRspSettlementInfoConfirm(
			CThostFtdcSettlementInfoConfirmField pSettlementInfoConfirm,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		System.out.println("结算单确认回调");
	}
	
	@Override
	public void onRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		System.out.println("错误回调");
	}
	@Override
	public void onErrRtnOrderInsert(CThostFtdcInputOrderField pInputOrder,
			CThostFtdcRspInfoField pRspInfo) {
		System.out.println("报单录入错误回调");
	}
}
