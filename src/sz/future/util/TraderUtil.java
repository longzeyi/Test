package sz.future.util;

import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_CC_Immediately;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_FCC_NotForceClose;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_OPT_LimitPrice;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_TC_GFD;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_VC_AV;

import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcOrderActionField;

import sz.future.trader.console.TestTrader;

public class TraderUtil {
	private static int requestID = 0;

	/**
	 * @param instrumentId 合约代码
	 * @param direction 买卖方向
	 * @param volume 申买/卖量
	 * @param offsetFlag 开平标志（开仓=0，平仓=1，强平=2，平今=3，平昨=4，强减=5，本地强平=6）
	 * @param limitPrice 价格
	 * @return 
	 */
	public static int orderInsert(String instrumentId,  char direction, int volume, String offsetFlag, double limitPrice){
				//下单操作
				CThostFtdcInputOrderField inputOrderField=new CThostFtdcInputOrderField();
				//期货公司代码
				inputOrderField.setBrokerID(ServerParams.BROKER_ID);
				//投资者代码
				inputOrderField.setInvestorID(ServerParams.USER_ID);
				// 合约代码
				inputOrderField.setInstrumentID(instrumentId);
				///报单引用
//				inputOrderField.setOrderRef("000000000001");
				// 用户代码
				inputOrderField.setUserID(ServerParams.USER_ID);
				// 报单价格条件
				inputOrderField.setOrderPriceType(THOST_FTDC_OPT_LimitPrice);
				// 买卖方向
//				inputOrderField.setDirection(THOST_FTDC_D_Buy);
				//卖空
				inputOrderField.setDirection(direction);
				// 组合开平标志
				inputOrderField.setCombOffsetFlag(offsetFlag);
				// 组合投机套保标志
				inputOrderField.setCombHedgeFlag("1");
				// 申买、申卖价格
				inputOrderField.setLimitPrice(16570);
				// 手数量
				inputOrderField.setVolumeTotalOriginal(20);
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
				
				int returnMsg = TestTrader.traderApi.reqOrderInsert(inputOrderField, ++requestID);
				return returnMsg;
	}
	
	public static int orderAction(){
		CThostFtdcOrderActionField actionField = new CThostFtdcOrderActionField();
		actionField.setBrokerID(ServerParams.BROKER_ID);
		actionField.setInvestorID(ServerParams.USER_ID);
		return 0;
	}
	
}
