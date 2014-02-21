package sz.future.trader.console;


import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderActionField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInvestorPositionDetailField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInvestorPositionField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcReqUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspInfoField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcSettlementInfoConfirmField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcTradeField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcTradingAccountField;
import org.hraink.futures.jctp.trader.JCTPTraderApi;
import org.hraink.futures.jctp.trader.JCTPTraderSpi;

import sz.future.trader.comm.M;

/**
 * Custom TraderSpi
 * 
 * @author Hraink E-mail:Hraink@Gmail.com
 * @version 2013-1-25 下午11:46:13
 */
public class MyTraderSpi extends JCTPTraderSpi {

	JCTPTraderApi traderApi;
	public static int nRequestID = 0;
	
	//国泰君安
	public static final String brokerId = "1038";
	public static final String userId = "00000015";
	public static final String password = "123456"; 
	
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
		
//		CThostFtdcInputOrderField pInputOrder = new CThostFtdcInputOrderField();
		
		
//		traderApi.reqOrderInsert(pInputOrder, ++nRequestID);
	}
	
	@Override
	public void onRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		System.out.println("TradingDay:" + traderApi.getTradingDay());
		System.out.println("登录时间："+pRspUserLogin.getLoginTime());
		System.out.println("登录状态："+pRspInfo.getErrorID() + " : " + pRspInfo.getErrorMsg());
//		System.out.println(pRspUserLogin.getCZCETime());
//		System.out.println(pRspUserLogin.getDCETime());
//		System.out.println(pRspUserLogin.getFFEXTime());
//		System.out.println(pRspUserLogin.getSHFETime());
		System.out.println("最大OrderRef: "+pRspUserLogin.getMaxOrderRef());
		
		//查询持仓明细
//		CThostFtdcQryInvestorPositionDetailField positionField = new CThostFtdcQryInvestorPositionDetailField();
//		positionField.setBrokerID(brokerId);
//		positionField.setInstrumentID("m1405");
//		positionField.setInvestorID(userId);
//		System.out.println("查询持仓明细: "+traderApi.reqQryInvestorPositionDetail(positionField, ++nRequestID));
		
//		CThostFtdcQryTradingAccountField accountField = new CThostFtdcQryTradingAccountField ();
//		accountField.setBrokerID(brokerId);
//		accountField.setInvestorID(userId);
//		System.out.println("查询资金账户: "+traderApi.reqQryTradingAccount(accountField, ++nRequestID));
		
		//确认结算单
		CThostFtdcSettlementInfoConfirmField confirmField = new CThostFtdcSettlementInfoConfirmField();
		System.out.println("确认结算单: "+traderApi.reqSettlementInfoConfirm(confirmField, ++nRequestID));;

		
//		//下单操作
//		CThostFtdcInputOrderField inputOrderField=new CThostFtdcInputOrderField();
//		//期货公司代码
//		inputOrderField.setBrokerID(brokerId);
//		//投资者代码
//		inputOrderField.setInvestorID(userId);
//		// 合约代码
//		inputOrderField.setInstrumentID("ru1405");
//		///报单引用
//		inputOrderField.setOrderRef("000000000001");
//		// 用户代码
//		inputOrderField.setUserID(userId);
//		// 报单价格条件
//		inputOrderField.setOrderPriceType(THOST_FTDC_OPT_LimitPrice);
//		// 买卖方向
////		inputOrderField.setDirection(THOST_FTDC_D_Buy);
//		//卖空
//		inputOrderField.setDirection(THOST_FTDC_D_Sell);
//		// 组合开平标志
//		inputOrderField.setCombOffsetFlag("3");
//		// 组合投机套保标志
//		inputOrderField.setCombHedgeFlag("1");
//		// 申买、申卖价格
//		inputOrderField.setLimitPrice(16570);
//		// 手数量
//		inputOrderField.setVolumeTotalOriginal(20);
//		// 有效期类型
//		inputOrderField.setTimeCondition(THOST_FTDC_TC_GFD);
//		// GTD日期
//		inputOrderField.setGTDDate("");
//		// 成交量类型
//		inputOrderField.setVolumeCondition(THOST_FTDC_VC_AV);
//		// 最小成交量
//		inputOrderField.setMinVolume(0);
//		// 触发条件
//		inputOrderField.setContingentCondition(THOST_FTDC_CC_Immediately);
//		// 止损价
//		inputOrderField.setStopPrice(0);
//		// 强平原因
//		inputOrderField.setForceCloseReason(THOST_FTDC_FCC_NotForceClose);
//		// 自动挂起标志
//		inputOrderField.setIsAutoSuspend(0);
//		
//		System.out.println("报单录入请求: "+traderApi.reqOrderInsert(inputOrderField, ++nRequestID));
	}
	
	/* 
	 * 请求查询资金账户响应。当客户端发出请求查询资金账户指令后，交易托管系统返回响应时，该方法会被调用。
	 */
	@Override
	public void onRspQryTradingAccount(
			CThostFtdcTradingAccountField pTradingAccount,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		System.out.println("可用资金："+pTradingAccount.getAvailable());
	}
	/* 
	 * 报单回报。当客户端进行报单录入、报单操作及其它原因（如部分成交）导致报单状态发生变化时，交易托管系统会主动通知客户端，该方法会被调用。
	 */
	@Override
	public void onRtnOrder(CThostFtdcOrderField pOrder) {
		System.out.println("报单通知: "+pOrder.getStatusMsg() + "价格：" + pOrder.getLimitPrice() + "手数：" + pOrder.getVolumeTotalOriginal());
	}
	
	/* 
	 * 报单录入应答。当客户端发出过报单录入指令后，交易托管系统返回响应时，该方法会被调用。
	 */
	@Override
	public void onRspOrderInsert(CThostFtdcInputOrderField pInputOrder,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		System.out.println("报单录入请求响应: "+pRspInfo.getErrorMsg() + "价格：" + pInputOrder.getLimitPrice() + "手数：" + pInputOrder.getVolumeTotalOriginal());
	}
	
	/* 
	 * 报单操作应答。报单操作包括报单的撤销、报单的挂起、报单的激活、报单的修改。当客户端发出过报单操作指令后，交易托管系统返回响应时，该方法会被调用。
	 */
	@Override
	public void onRspOrderAction(
			CThostFtdcInputOrderActionField pInputOrderAction,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		System.out.println("报单操作请求响应: "+pRspInfo.getErrorMsg());
	}
	
	/* 
	 * 成交回报。当发生成交时交易托管系统会通知客户端，该方法会被调用
	 */
	@Override
	public void onRtnTrade(CThostFtdcTradeField pTrade) {
		System.out.println("成交回报: "+pTrade.getInstrumentID() + "价格：" + pTrade.getPrice() + "数量：" + pTrade.getVolume() + "订单引用：" + pTrade.getOrderRef());
		M.positionPrice = pTrade.getPrice();
	}
	
	@Override
	public void onRspQryTrade(CThostFtdcTradeField pTrade,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		super.onRspQryTrade(pTrade, pRspInfo, nRequestID, bIsLast);
		System.out.println("成交通知: "+pTrade.getInstrumentID() + "价格：" + pTrade.getPrice() + "数量：" + pTrade.getVolume() + "订单引用：" + pTrade.getOrderRef());
	}
	/* 
	 * 请求查询投资者持仓明细响应。当客户端发出请求请求查询投资者持仓明细指令后，交易托管系统返回响应时，该方法会被调用
	 */
	@Override
	public void onRspQryInvestorPositionDetail(
			CThostFtdcInvestorPositionDetailField pInvestorPositionDetail,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		System.out.println("请求查询投资者持仓明细响应"+pInvestorPositionDetail.getDirection()+"+"+pInvestorPositionDetail.getOpenDate()+"+"+pInvestorPositionDetail.getOpenPrice()+"+"+pInvestorPositionDetail.getTradingDay());
	}
	
	
	/* 
	 * 投资者持仓查询应答。当客户端发出投资者持仓查询指令后，后交易托管系统返回响应时，该方法会被调用
	 */
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
