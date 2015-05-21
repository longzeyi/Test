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

import sz.future.dao.FutureDevDao;
import sz.future.domain.InverstorPosition;
import sz.future.domain.InverstorPositionDetail;
import sz.future.trader.comm.ServerParams;
import sz.future.trader.comm.Super;
import sz.future.util.TraderUtil;

/**
 * Custom TraderSpi
 * 
 * @author Hraink E-mail:Hraink@Gmail.com
 * @version 2013-1-25 下午11:46:13
 */
public class MyTraderSpi extends JCTPTraderSpi {

	JCTPTraderApi traderApi;
	private FutureDevDao dao = new FutureDevDao();
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
		System.out.println("No."+nRequestID + " $$报单录入应答: "+ pRspInfo.getErrorMsg() + 
			"【报单引用：" + pInputOrder.getOrderRef() + 
			"  合约：" + pInputOrder.getInstrumentID() + 
			"  买卖方向：" + pInputOrder.getDirection() + 
			"  价格：" + pInputOrder.getLimitPrice() + 
			"  数量：" + pInputOrder.getVolumeTotalOriginal() + 
			"  报单请求编号：" + pInputOrder.getRequestID() + 
			"  手数：" + pInputOrder.getVolumeTotalOriginal() + "】");
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
	
	/* 
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
		if(pInvestorPositionDetail != null && pInvestorPositionDetail.getVolume()>0){
				Super.INVESTOR_POSITION_OPEN_PRICE.put(pInvestorPositionDetail.getInstrumentID(), pInvestorPositionDetail.getOpenPrice());
//				System.out.println("$$No."+nRequestID + "持仓合约开仓价: "+pInvestorPositionDetail.getInstrumentID()+" "+pInvestorPositionDetail.getOpenPrice() + " " + pInvestorPositionDetail.getDirection());
				System.out.println("$$查询持仓明细通知: " + 
						" 【结算编号：" + pInvestorPositionDetail.getSettlementID() + 
						"  合约：" + pInvestorPositionDetail.getInstrumentID()+ 
						"  买卖方向：" + pInvestorPositionDetail.getDirection() + 
						"  开仓价：" + pInvestorPositionDetail.getOpenPrice() + 
						"  数量：" + pInvestorPositionDetail.getVolume() + 
						"  交易所保证金：" + pInvestorPositionDetail.getExchMargin() + 
						"  成交编号：" + pInvestorPositionDetail.getTradeID() + 
						"  开仓日期：" + pInvestorPositionDetail.getOpenDate() + 
						"  交易日：" + pInvestorPositionDetail.getTradingDay() + 
						"  保证金率：" + pInvestorPositionDetail.getMarginRateByMoney() + "】");
				InverstorPositionDetail ipd = new InverstorPositionDetail();
				ipd.setTradeID(Integer.parseInt(pInvestorPositionDetail.getTradeID().trim()));
				ipd.setInstrumentID(pInvestorPositionDetail.getInstrumentID());
				if(pInvestorPositionDetail.getDirection() == '0'){
					ipd.setDirection(true);
				}else{
					ipd.setDirection(false);
				}
				ipd.setVolume(pInvestorPositionDetail.getVolume());
				ipd.setPrice(pInvestorPositionDetail.getOpenPrice());
				ipd.setOpenDate(pInvestorPositionDetail.getOpenDate());
				ipd.setTradingDay(pInvestorPositionDetail.getTradingDay());
				ipd.setExchMargin(pInvestorPositionDetail.getExchMargin());
				ipd.setMarginRateByMoney(pInvestorPositionDetail.getMarginRateByMoney());
				dao.savePositionDetail(ipd);
				Super.INVESTOR_POSITION_DETAIL.add(ipd);
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
			System.out.println("$$查询持仓通知: " + 
					" 【结算编号：" + pInvestorPosition.getSettlementID() +
					"  合约：" + pInvestorPosition.getInstrumentID()+ 
					"  持仓日期：" + pInvestorPosition.getPositionDate()+ 
					"  上日持仓：" + pInvestorPosition.getYdPosition()+ 
					"  今日持仓：" + pInvestorPosition.getPosition()+ 
					"  买卖方向：" + pInvestorPosition.getPosiDirection() +
					"  开仓金额：" + pInvestorPosition.getOpenAmount() +
					"  开仓量：" + pInvestorPosition.getOpenVolume() +
					"  交易日：" + pInvestorPosition.getTradingDay() +
					"  交易所保证金：" + pInvestorPosition.getExchangeMargin() +
					"  今日持仓：" + pInvestorPosition.getTodayPosition() +
					"  保证金率：" + pInvestorPosition.getMarginRateByMoney() + "】");
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
		System.out.println("$$结算单确认回调: "+ pRspInfo.getErrorMsg() + " " + pSettlementInfoConfirm.getConfirmDate() + " " + pSettlementInfoConfirm.getConfirmTime());
	}
	
	@Override
	public void onRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		System.out.println("$$错误回调信息："+pRspInfo.getErrorID() + "  " + pRspInfo.getErrorMsg() + " " + bIsLast);
	}
	
	/* 
	 * 报单回报。当客户端进行报单录入、报单操作及其它原因（如部分成交）导致报单状态发生变化时，交易托管系统会主动通知客户端，该方法会被调用。
	 */
	@Override
	public void onRtnOrder(CThostFtdcOrderField pOrder) {
		System.out.println("$$报单回报通知: "+pOrder.getStatusMsg() + 
				" 【报单引用：" + pOrder.getOrderRef() +
				"  合约：" + pOrder.getInstrumentID()+ 
				"  买卖方向：" + pOrder.getDirection() +
				"  价格：" + pOrder.getLimitPrice() +
				"  数量：" + pOrder.getVolumeTotalOriginal() +
				"  报单编号：" + pOrder.getOrderSysID() +
				"  报单状态：" + pOrder.getOrderStatus() + "】");
	}
	
	/* 
	 * 成交回报。当发生成交时交易托管系统会通知客户端，该方法会被调用
	 */
	@Override
	public void onRtnTrade(CThostFtdcTradeField pTrade) {
		TraderUtil.qryPositionDetail();//每次成交后查询持仓明细来更新持仓情况
		System.out.println("$$成交回报通知：" +
				" 【报单引用：" + pTrade.getOrderRef() +
				"  合约：" + pTrade.getInstrumentID() + 
				"  买卖方向：" + pTrade.getDirection() +
				"  成交价格：" + pTrade.getPrice() +
				"  数量：" + pTrade.getVolume() +
				"  报单编号：" + pTrade.getOrderSysID() +"】");
	}
	
	@Override
	public void onErrRtnOrderInsert(CThostFtdcInputOrderField pInputOrder,
			CThostFtdcRspInfoField pRspInfo) {
		System.out.println(" $$报单录入错误回调响应: "+ pRspInfo.getErrorMsg() + 
				"【报单引用：" + pInputOrder.getOrderRef() + 
				"  合约：" + pInputOrder.getInstrumentID() + 
				"  买卖方向：" + pInputOrder.getDirection() + 
				"  价格：" + pInputOrder.getLimitPrice() + 
				"  数量：" + pInputOrder.getVolumeTotalOriginal() + 
				"  报单请求编号：" + pInputOrder.getRequestID() + 
				"  手数：" + pInputOrder.getVolumeTotalOriginal() + "】");
	}
	
}
