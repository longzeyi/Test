package sz.future.trader.console;


import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderActionField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInvestorPositionCombineDetailField;
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

import sz.future.domain.InverstorPosition;
import sz.future.trader.comm.ServerParams;
import sz.future.trader.comm.Super;

/**
 * Custom TraderSpi
 * 
 * @author Hraink E-mail:Hraink@Gmail.com
 * @version 2013-1-25 下午11:46:13
 */
public class MyTraderSpi extends JCTPTraderSpi {

	JCTPTraderApi traderApi;
//	public static int nRequestID = 0;
	
	//国泰君安
//	public static final String brokerId = "1038";
//	public static final String userId = "00000015";
//	public static final String password = "123456"; 
	
	public MyTraderSpi(JCTPTraderApi traderApi) {
		this.traderApi = traderApi;
	}
	@Override
	public void onFrontConnected() {
		System.out.println("前置机连接");
		CThostFtdcReqUserLoginField userLoginField = new CThostFtdcReqUserLoginField();
		userLoginField.setBrokerID(ServerParams.BROKER_ID);
		userLoginField.setUserID(ServerParams.USER_ID);
		userLoginField.setPassword(ServerParams.PWD);
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
		if(traderApi.reqSettlementInfoConfirm(confirmField, nRequestID) == 0){
			System.out.println("确认结算单成功！");
		} else {
			System.out.println("确认结算单异常！");
		}
	}
	
	/* 
	 * 请求查询资金账户响应。当客户端发出请求查询资金账户指令后，交易托管系统返回响应时，该方法会被调用。
	 */
	@Override
	public void onRspQryTradingAccount(
			CThostFtdcTradingAccountField pTradingAccount,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		double fundsUsage = pTradingAccount.getCurrMargin()/(pTradingAccount.getAvailable()+pTradingAccount.getCurrMargin());
		System.out.println("nRequestID: "+nRequestID);
		System.out.println("bIsLast: "+bIsLast);
		System.out.println("可用资金："+pTradingAccount.getAvailable());
		System.out.println("当前保证金总额："+pTradingAccount.getCurrMargin());
//		System.out.println("可取资金："+pTradingAccount.getWithdrawQuota());
		System.out.println("资金使用率：" + fundsUsage);
		Super.fundsUsage = fundsUsage;
	}
	
	
	/* 
	 * 报单录入应答。当客户端发出过报单录入指令后，交易托管系统返回响应时，该方法会被调用。
	 */
	@Override
	public void onRspOrderInsert(CThostFtdcInputOrderField pInputOrder,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		System.out.println("No."+nRequestID + " $$报单录入: "+pInputOrder.getInstrumentID()+" "+pRspInfo.getErrorMsg() + " 价格：" + pInputOrder.getLimitPrice() + " 手数：" + pInputOrder.getVolumeTotalOriginal());
	}
	
	/* 
	 * 报单操作应答。报单操作包括报单的撤销、报单的挂起、报单的激活、报单的修改。当客户端发出过报单操作指令后，交易托管系统返回响应时，该方法会被调用。
	 */
	@Override
	public void onRspOrderAction(
			CThostFtdcInputOrderActionField pInputOrderAction,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		System.out.println("No."+nRequestID + "$$报单操作请求响应: "+pRspInfo.getErrorMsg());
	}
	
	/* (non-Javadoc)
	 * @see org.hraink.futures.jctp.trader.JCTPTraderSpi#onRspQryOrder(org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcOrderField, org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspInfoField, int, boolean)
	 * 查询报单响应
	 */
	@Override
	public void onRspQryOrder(CThostFtdcOrderField pOrder,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
//		System.out.println("合约代码: " + pOrder.getInstrumentID());
//		super.onRspQryOrder(pOrder, pRspInfo, nRequestID, bIsLast);
	}
	@Override
	public void onRspQryTrade(CThostFtdcTradeField pTrade,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
//		super.onRspQryTrade(pTrade, pRspInfo, nRequestID, bIsLast);
		System.out.println("$$No."+nRequestID + "成交通知: "+pTrade.getInstrumentID() + "价格：" + pTrade.getPrice() + "数量：" + pTrade.getVolume() + "订单引用：" + pTrade.getOrderRef());
	}
	/* 
	 * 请求查询投资者持仓明细响应。当客户端发出请求请求查询投资者持仓明细指令后，交易托管系统返回响应时，该方法会被调用
	 */
	@Override
	public void onRspQryInvestorPositionDetail(
			CThostFtdcInvestorPositionDetailField pInvestorPositionDetail,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		if(pInvestorPositionDetail != null){
				Super.INVESTOR_POSITION_OPEN_PRICE.put(pInvestorPositionDetail.getInstrumentID(), pInvestorPositionDetail.getOpenPrice());
				System.out.println("$$No."+nRequestID + "持仓合约开仓价: "+pInvestorPositionDetail.getInstrumentID()+" "+pInvestorPositionDetail.getOpenPrice());
//				Super.INVESTOR_POSITION_DETAIL.put(pInvestorPositionDetail.getInstrumentID(), pInvestorPositionDetail);
//				System.err.println("查询投资者持仓明细响应错误： " + pRspInfo.getErrorID() + "--" +pRspInfo.getErrorMsg());
		} else {
			System.out.println("$$没有持仓该合约");
		}
	}
	
	/* 
	 * 投资者持仓查询应答。当客户端发出投资者持仓查询指令后，后交易托管系统返回响应时，该方法会被调用
	 */
	@Override
	public void onRspQryInvestorPosition(
			CThostFtdcInvestorPositionField pInvestorPosition,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
//		System.err.println("bIsLast: "+bIsLast + "  " + pInvestorPosition.getInstrumentID());
		if(pInvestorPosition != null){
			//如果今仓和昨仓总和不为0，表示持仓该合约
			if((pInvestorPosition.getPosition() + pInvestorPosition.getYdPosition()) > 0){
					InverstorPosition ip = new InverstorPosition();
					ip.setInstrumentID(pInvestorPosition.getInstrumentID());
					ip.setTradingDay(pInvestorPosition.getTradingDay());
					ip.setPosiDirectionType(pInvestorPosition.getPosiDirection());
					ip.setPositionDateType(pInvestorPosition.getPositionDate());
					ip.setPosition(pInvestorPosition.getPosition());
					ip.setYdposition(pInvestorPosition.getYdPosition());
					ip.setUseMargin(pInvestorPosition.getUseMargin());
					ip.setPositionProfit(pInvestorPosition.getPositionProfit());
					ip.setMarginRateByMoney(pInvestorPosition.getMarginRateByMoney());
					ip.setCloseProfitByDate(pInvestorPosition.getCloseProfitByDate());
					ip.setCloseProfitByTrade(pInvestorPosition.getCloseProfitByTrade());
					ip.setSettlementID(pInvestorPosition.getSettlementID());
					Super.INVESTOR_POSITION.put(pInvestorPosition.getInstrumentID(), ip);
					System.out.println("$$No."+nRequestID + "持仓合约：" + pInvestorPosition.getInstrumentID() );
//					System.out.println("交易日: "+pInvestorPosition.getTradingDay());
			}
		} else {
			System.out.println("$$没有持仓合约");
		}
	}

	@Override
	public void onRspSettlementInfoConfirm(
			CThostFtdcSettlementInfoConfirmField pSettlementInfoConfirm,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		System.out.println("$$No."+nRequestID + "结算单确认回调");
	}
	
	@Override
	public void onRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		System.out.println("$$No."+nRequestID + "错误回调信息"+pRspInfo.getErrorMsg());
	}
	
	/* 
	 * 报单回报。当客户端进行报单录入、报单操作及其它原因（如部分成交）导致报单状态发生变化时，交易托管系统会主动通知客户端，该方法会被调用。
	 */
	@Override
	public void onRtnOrder(CThostFtdcOrderField pOrder) {
		System.out.println("$$报单回报通知: "+pOrder.getStatusMsg() + "价格：" + pOrder.getLimitPrice() + "手数：" + pOrder.getVolumeTotalOriginal());
	}
	
	/* 
	 * 成交回报。当发生成交时交易托管系统会通知客户端，该方法会被调用
	 */
	@Override
	public void onRtnTrade(CThostFtdcTradeField pTrade) {
		System.out.println("$$成交回报通知："+pTrade.getInstrumentID()+" 价格："+pTrade.getPrice());
	}
	
	@Override
	public void onErrRtnOrderInsert(CThostFtdcInputOrderField pInputOrder,
			CThostFtdcRspInfoField pRspInfo) {
		System.out.println("$$报单录入错误回调： 方向："+pInputOrder.getDirection()+ "价格：" +pInputOrder.getLimitPrice()+ "手数：" +pInputOrder.getVolumeTotalOriginal()+ "返回信息：" +pRspInfo.getErrorMsg());
	}
	
}
