/**
 * 
 */
package sz.future.trader.comm;

public class Super {
	public static final String brokerId = "1038";
	public static final String userId = "00000015";
	public static final String pwd = "123456";
	private static final int size = 50000;
	
	public static int count = 0;
	public static double[] priceArray = new double[size];		//最新价
	public static double[] priceB1Array = new double[size];		//申买价1
	public static double[] priceS1Array = new double[size];		//申卖价1
	public static int[] volumeArray = null;			//成交量
	public static int[] volumeTotalArray = null;	//成交总量
	public static double lowerLimitPrice = 0d;//跌停价
	public static double upperLimitPrice = 0d;//涨停价
	
}
