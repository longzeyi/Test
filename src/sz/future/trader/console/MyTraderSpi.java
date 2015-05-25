package sz.future.trader.console;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

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
	/**
	 * 当前持仓明细
	 */
	private List<InverstorPositionDetail> investorPositionDetail = new ArrayList<InverstorPositionDetail>();
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
		System.out.println("$$查询持仓明细通知: " + 
				" 【结算编号：" + pInvestorPositionDetail.getSettlementID() + 
				"  合约：" + pInvestorPositionDetail.getInstrumentID()+ 
				"  买卖方向：" + pInvestorPositionDetail.getDirection() + 
				"  开仓价：" + pInvestorPositionDetail.getOpenPrice() + 
				"  数量：" + pInvestorPositionDetail.getVolume() + 
				"  保证金：" + pInvestorPositionDetail.getMargin() + 
				"  成交编号：" + pInvestorPositionDetail.getTradeID() + 
				"  昨结算：" + pInvestorPositionDetail.getLastSettlementPrice() + 
				"  平仓盈亏：" + pInvestorPositionDetail.getCloseProfitByDate() + 
				"  持仓盈亏：" + pInvestorPositionDetail.getPositionProfitByDate() + 
				"  开仓日期：" + pInvestorPositionDetail.getOpenDate() + 
				"  交易日：" + pInvestorPositionDetail.getTradingDay() + 
				"  保证金率：" + pInvestorPositionDetail.getMarginRateByMoney() + "】");
		InverstorPositionDetail ipd = new InverstorPositionDetail();
		ipd.setInstrumentID(pInvestorPositionDetail.getInstrumentID());
		ipd.setDirection(pInvestorPositionDetail.getDirection());
		ipd.setOpenDate(pInvestorPositionDetail.getOpenDate());
		ipd.setTradeID(Integer.parseInt(pInvestorPositionDetail.getTradeID().trim()));
		ipd.setVolume(pInvestorPositionDetail.getVolume());
		ipd.setOpenPrice(pInvestorPositionDetail.getOpenPrice());
		ipd.setTradingDay(pInvestorPositionDetail.getTradingDay());
		ipd.setSettlementID(pInvestorPositionDetail.getSettlementID());
		ipd.setMargin(pInvestorPositionDetail.getMargin());
		ipd.setExchMargin(pInvestorPositionDetail.getExchMargin());
		ipd.setMarginRateByMoney(pInvestorPositionDetail.getMarginRateByMoney());
		ipd.setLastSettlementPrice(pInvestorPositionDetail.getLastSettlementPrice());
		ipd.setSettlementPrice(pInvestorPositionDetail.getSettlementPrice());
		ipd.setHedgeFlag(pInvestorPositionDetail.getHedgeFlag());
		ipd.setCloseProfitByDate(pInvestorPositionDetail.getCloseProfitByDate());
		ipd.setPositionProfitByDate(pInvestorPositionDetail.getPositionProfitByDate());
		if (ipd != null && ipd.getVolume()>0){
			investorPositionDetail.add(ipd);
			if (bIsLast){
				//将明细合成持仓
				Super.INVESTOR_POSITION.clear();//将之前的持仓清空，准备更新新的持仓数据
				for (InverstorPositionDetail detail : investorPositionDetail) {
					String key = detail.getInstrumentID() + "_" + detail.getDirection();
					InverstorPosition ip = Super.INVESTOR_POSITION.get(key);
					if(ip == null){//第一笔明细
						ip = new InverstorPosition();
						ip.setInstrumentID(detail.getInstrumentID());
						ip.setDirection(detail.getDirection());
						ip.setPreSettlementPrice(detail.getLastSettlementPrice());
						ip.setPosition(detail.getVolume());
						ip.setTradingDay(detail.getTradingDay());
						ip.setOpenDate(detail.getOpenDate());
						ip.setOpenAvgPrice(detail.getOpenPrice());
						if(detail.getTradingDay().equals(detail.getOpenDate())){//今仓
							ip.setTdPosition(detail.getVolume());
							ip.setTdPostionCost(detail.getVolume() * detail.getOpenPrice());//持仓成本
							ip.setTdUseMargin(detail.getMargin());
						} else {//昨仓
							ip.setYdPosition(detail.getVolume());
							ip.setYdPostionCost(detail.getVolume() * detail.getLastSettlementPrice());//持仓成本
							ip.setYdUseMargin(detail.getMargin());
						}
						ip.setHedgeFlag(detail.getHedgeFlag());
						ip.setCloseProfitByDate(detail.getCloseProfitByDate());//平仓盈亏
						ip.setPositionProfit(detail.getPositionProfitByDate());//持仓盈亏
						System.out.println("ip: "+ip.getInstrumentID() + " : " + ip.getTdPosition());
						Super.INVESTOR_POSITION.put(key, ip);
					} else {//多笔明细,要在之前的明细上累加
						InverstorPosition tempIp = new InverstorPosition();
						tempIp.setInstrumentID(ip.getInstrumentID());
						tempIp.setDirection(ip.getDirection());
						tempIp.setPreSettlementPrice(ip.getPreSettlementPrice());
						tempIp.setPosition(detail.getVolume() + ip.getPosition());
						tempIp.setTradingDay(detail.getTradingDay());
						tempIp.setOpenDate(detail.getOpenDate());
						tempIp.setOpenAvgPrice((ip.getOpenAvgPrice()*ip.getPosition()+detail.getOpenPrice()*detail.getVolume())/(ip.getPosition()+detail.getVolume()));
						if(detail.getTradingDay().equals(detail.getOpenDate())){
							tempIp.setYdPosition(ip.getYdPosition());
							tempIp.setYdPostionCost(ip.getYdPostionCost());
							tempIp.setYdUseMargin(ip.getYdUseMargin());
							tempIp.setTdPosition(ip.getTdPosition()+detail.getVolume());
							tempIp.setTdPostionCost(ip.getTdPostionCost() + detail.getVolume() * detail.getOpenPrice());
							tempIp.setTdUseMargin(ip.getTdUseMargin() + detail.getMargin());
						} else {
							tempIp.setTdPosition(ip.getTdPosition());
							tempIp.setTdPostionCost(ip.getTdPostionCost());
							tempIp.setTdUseMargin(ip.getTdUseMargin());
							tempIp.setYdPosition(ip.getYdPosition() + detail.getVolume());
							tempIp.setYdPostionCost(ip.getYdPostionCost() + detail.getVolume() * detail.getLastSettlementPrice());
							tempIp.setYdUseMargin(ip.getYdUseMargin() + detail.getMargin());
						}
						tempIp.setHedgeFlag(detail.getHedgeFlag());
						tempIp.setCloseProfitByDate(detail.getCloseProfitByDate());
						tempIp.setPositionProfit(detail.getPositionProfitByDate());
						System.out.println("tempIp: "+tempIp.getInstrumentID() + " : " + tempIp.getTdPosition());
						Super.INVESTOR_POSITION.remove(key);
						Super.INVESTOR_POSITION.put(key, tempIp);
					}
				}
				investorPositionDetail.clear();
				Set<Entry<String, InverstorPosition>> set = Super.INVESTOR_POSITION.entrySet();
				Iterator<Entry<String, InverstorPosition>> it =set.iterator();
				System.out.println("合约代码\t"
						+"方向\t"
						+"总仓\t"
						+"今仓\t"
						+"昨仓\t"
						+"持仓成本\t"
						+"开仓均价\t"
						+"昨结算\t"
						+"持仓盈亏\t"
						+"平仓盈亏\t"
						+"保证金\t"
						+"开仓日");
				while(it.hasNext()){
					Entry<String, InverstorPosition> en = it.next();
//					double price =0d;
//					if(en.getValue().getYdPosition()>0&&en.getValue().getTdPosition()>0){
//						System.out.println("1:" + en.getValue().getYdPostionCost() + ":" + en.getValue().getTdPostionCost());
//						price = (en.getValue().getYdPostionCost()/(double)en.getValue().getYdPosition() + en.getValue().getTdPostionCost()/(double)en.getValue().getTdPosition())/2;
//					} else if(en.getValue().getYdPosition()>0){
////						System.out.println("2:" + en.getValue().getYdPostionCost() + ":" + en.getValue().getYdPosition());
//						price = en.getValue().getYdPostionCost()/en.getValue().getYdPosition();
//					} else if (en.getValue().getTdPosition()>0){
//						price = en.getValue().getTdPostionCost()/en.getValue().getTdPosition();
//					}
					System.out.println(en.getValue().getInstrumentID()+"\t"
					+en.getValue().getDirection()+"\t"
					+en.getValue().getPosition()+"\t"
					+en.getValue().getTdPosition()+"\t"
					+en.getValue().getYdPosition()+"\t"
					+(en.getValue().getYdPostionCost()+en.getValue().getTdPostionCost())+"\t"
					+en.getValue().getOpenAvgPrice() +"\t"
					+en.getValue().getPreSettlementPrice()+"\t"
					+en.getValue().getPositionProfit()+"\t"
					+en.getValue().getCloseProfitByDate()+"\t"
					+(en.getValue().getYdUseMargin()+en.getValue().getTdUseMargin())+"\t"
					+en.getValue().getOpenDate());
				}
			}
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
//			if((pInvestorPosition.getPosition() + pInvestorPosition.getYdPosition()) > 0){
//					InverstorPosition ip = new InverstorPosition();
//					ip.setInstrumentID(pInvestorPosition.getInstrumentID());
//					ip.setTradingDay(pInvestorPosition.getTradingDay());
//					ip.setPosiDirectionType(pInvestorPosition.getPosiDirection());
//					ip.setPositionDateType(pInvestorPosition.getPositionDate());
//					ip.setPosition(pInvestorPosition.getPosition());
//					ip.setYdposition(pInvestorPosition.getYdPosition());
//					ip.setUseMargin(pInvestorPosition.getUseMargin());
//					ip.setPositionProfit(pInvestorPosition.getPositionProfit());
//					ip.setMarginRateByMoney(pInvestorPosition.getMarginRateByMoney());
//					ip.setCloseProfitByDate(pInvestorPosition.getCloseProfitByDate());
//					ip.setCloseProfitByTrade(pInvestorPosition.getCloseProfitByTrade());
//					ip.setSettlementID(pInvestorPosition.getSettlementID());
//					Super.INVESTOR_POSITION.put(pInvestorPosition.getInstrumentID(), ip);
//					System.out.println("$$No."+nRequestID + "持仓合约：" + pInvestorPosition.getInstrumentID() );
////					System.out.println("交易日: "+pInvestorPosition.getTradingDay());
//			}
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
