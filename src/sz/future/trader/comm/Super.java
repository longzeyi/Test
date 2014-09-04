package sz.future.trader.comm;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局变量类
 * @author Sean
 *
 */
public class Super {
	
	public static int count = 0;
	public static int requestID = 0;
	public static int currOrderRef = 0;//当前成交报单引用
	
	public static double lastPrice = 0d;
	public static double highestPriceDay = 0d;//当日最高价
	public static double lowestPriceDay = 0d;//当日最低价
	public static double openPrice = 0d;//当日开盘价
	public static double preClosePrice = 0d;//昨日收盘价
	public static double upperLimitPrice = 0d;//涨停价
	public static double lowerLimitPrice = 0d;//跌停价
	
	//Tick容器
	public static Map<String, double[]> TICK_DATA = new HashMap<String, double[]>();
	
	//策略信号
//	public static int signal = -1;
	public static Map<String, Integer> SIGNAL = new HashMap<String, Integer>();
	
	static{
		for (int i = 0; i < ServerParams.instruments.length; i++) {
			TICK_DATA.put(ServerParams.instruments[i], new double[7]);
			SIGNAL.put(ServerParams.instruments[i], -1);
		}
	}
//	public static boolean currDirection = true ;//当前交易方向
	
}
