/**
 * 
 */
package sz.future.trader.comm;

import java.util.HashMap;
import java.util.Map;

public class Super {
	public static final String[] instruments = new String[]{"a1501","m1501","pp1501","sr1501","jd1501","rb1501","pta1501","fg1501","rm1501","y1501"};
	
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
	
	public static Map<String, double[]> mddata = new HashMap<String, double[]>();
	static {
		for (int i = 0; i < instruments.length; i++) {
			mddata.put(instruments[i], null);
		}
	}
//	public static boolean currDirection = true ;//当前交易方向
	
}
