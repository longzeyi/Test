package sz.future.monitor;

import java.util.Iterator;
import java.util.Map;

import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInvestorPositionField;

import sz.future.domain.InverstorPosition;
import sz.future.trader.comm.ServerParams;
import sz.future.trader.comm.Super;
import sz.future.util.TraderUtil;

/**
 * @author Sean
 * 行情监测线程
 */
public class TestMonitor extends Thread{
	private String[] instruments ;//,"pp1501","sr1501","jd1501","pta1501","fg1501","rm1501"
	private Map<String, double[]> tickData;
	private double[] lastTick;
	
	public TestMonitor(){
		//克隆合约数组
		instruments = new String[ServerParams.instruments.length];
		for (int i = 0; i < ServerParams.instruments.length; i++) {
			instruments[i] = ServerParams.instruments[i];
		}
	}
	/**
	 * @param args
	 */
	public void run() {
//		String[] instruments = ServerParams.instruments.clone();
		//查询资金使用率
		TraderUtil.qryTradingAccount();
		TraderUtil.qryPosition();
//		requestAllInstrumentsInvestorPosition();
		
		while(true){
			tickData = Super.TICK_DATA;
//			System.out.println(ServerParams.instruments[0]);
			for (int i = 0; i < instruments.length; i++) {
//				System.out.println("instrumentId: "+instruments[i]);
				lastTick = tickData.get(instruments[i]);
				if(lastTick != null)
				System.err.println(instruments[i] + " : " + lastTick[0] + ":" + lastTick[1] + " : " + lastTick[2] + ":" + lastTick[3] + " : " + lastTick[4] + ":" + lastTick[5] + " : " + lastTick[6]);
				
//				TraderUtil.qryPosition(instruments[i]);
//				TraderUtil.orderInsert(instruments[i], false, 5, "0", lastTick[5]);
//				TraderUtil.qryOrder(instruments[i]);
//				TraderUtil.qryTradingAccount();
			}
			
			//遍历持仓
//			Iterator<String> it = Super.INVESTOR_POSITION.keySet().iterator();
//			while(it.hasNext()){
//				InverstorPosition pInvestorPosition = Super.INVESTOR_POSITION.get(it.next());
//				System.out.println("持仓合约: "+pInvestorPosition.getInstrumentID());
//				System.out.println("持仓多空方向: "+pInvestorPosition.getPosiDirectionType());
//				System.out.println("持仓量: "+(pInvestorPosition.getPosition() + pInvestorPosition.getYdposition()));
//				System.out.println("持仓日期: "+pInvestorPosition.getPositionDateType());
//				System.out.println("交易日: "+pInvestorPosition.getTradingDay());
//				System.out.println("持仓盈亏: "+pInvestorPosition.getPositionProfit());
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

//	private void requestAllInstrumentsInvestorPosition(){
//		for (int i = 0; i < instruments.length; i++) {
//			TraderUtil.qryPosition(instruments[i]);
//		}
//	}
}
