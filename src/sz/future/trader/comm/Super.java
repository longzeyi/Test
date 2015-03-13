package sz.future.trader.comm;

import java.util.HashMap;
import java.util.Map;

import sz.future.domain.InverstorPosition;
import sz.future.domain.InverstorPositionDetail;

/**
 * 全局变量类
 * @author Sean
 *
 */
public class Super {
	
	public static int count = 0;
	public static int requestID = 0;
	public static int currOrderRef = 0;//当前成交报单引用
	public static int historyDateRange = 20;//初始化时每个合约取多少天内的历史收盘价
	
	public static double lastPrice = 0d;
	public static double highestPriceDay = 0d;//当日最高价
	public static double lowestPriceDay = 0d;//当日最低价
	public static double openPrice = 0d;//当日开盘价
	public static double preClosePrice = 0d;//昨日收盘价
	public static double upperLimitPrice = 0d;//涨停价
	public static double lowerLimitPrice = 0d;//跌停价
	
	/**
	 * 资金使用率
	 */
	public static double fundsUsage = -1d;
	
	/**
	 * Tick容器
	 */
	public static Map<String, double[]> TICK_DATA = new HashMap<String, double[]>();
	
	/**
	 * 持仓
	 */
	public static Map<String, InverstorPosition> INVESTOR_POSITION = new HashMap<String, InverstorPosition>();
	
	/**
	 * 持仓明细
	 */
	public static Map<String, InverstorPositionDetail> INVESTOR_POSITION_DETAIL = new HashMap<String, InverstorPositionDetail>();
	
	/**
	 * 持仓开仓价
	 */
	public static Map<String, Double> INVESTOR_POSITION_OPEN_PRICE = new HashMap<String, Double>();
	
	/**
	 * 历史收盘价（日期降序排列）
	 */
	public static Map<String, double[]> HISTORY_CLOSE_PRICE = new HashMap<String, double[]>();
	
	/**
	 * 策略信号
	 */
	public static Map<String, Integer> SIGNAL = new HashMap<String, Integer>();
	
	static{
		for (int i = 0; i < ServerParams.instruments.length; i++) {
			if(!ServerParams.instruments[i].equals("")){
				TICK_DATA.put(ServerParams.instruments[i], new double[7]);
				SIGNAL.put(ServerParams.instruments[i], -1);
			}
		}
	}
//	public static boolean currDirection = true ;//当前交易方向
	
}
