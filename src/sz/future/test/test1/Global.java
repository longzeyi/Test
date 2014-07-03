package sz.future.test.test1;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Global {
//	private static final String[] strs = { "AG", "AU", "CU", "FG", "J", "JM", "L", "M", "ME", "OI", "P", "RB", "RM", "RU", "SR", "TA", "V", "Y" };
	//M1401、P1401、RB1310、RM1309、TA1401、Y1401
	
//	public static final String[] strs = { "Y" };
	public static final String[] test_instrument_id_array = {"M1401","P1401","RB1310","RM1309","TA1401","Y1401"};
	public static final String[] months = { "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "10", "11", "12" };
	public static final String[] days = { "01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
			"29", "30", "31" };
	public static final int year = 2013;
	
	public final static int period = 5;				//这段时间内的收盘价
	public final static int interval = 20 ;			//间隙
	public final static double floatSpace = 16d;	//忍受浮动空间
	public final static double retracement = 0.65d; //回撤比例
	public final static double breakPoint = 3d;	//突破period天内的最低价或最高价的限度
	
	public static String[] updateTimeArray = null;
	public static double[] lastPriceArray = null;	//最新价
	public static double[] priceB1Array = null;		//申买价1
	public static double[] priceS1Array = null;		//申卖价1
	public static int[] volumeArray = null;			//成交量
	public static int[] volumeTotalArray = null;	//成交总量
	public static int[] volumeB1Array = null;
	public static int[] volumeS1Array = null;
	
//	public static double money = 2000000d ;			//总资金
//	public static double handPrice = 0 ;			//每手价格
	public static double closePrice = 0;			//平仓价
	public static double positionPrice = 0 ;		//持仓价
	public static double point = 0 ;				//点数
	public static int transactionCount = 0;			//总交易次数
	public static int longCount = 0;				//买多交易次数
	public static int shortCount = 0;				//卖空交易次数
	public static int closeCount = 0;				//平仓交易次数
	public static boolean openOrClose = true ; 		//交易类型
	public static boolean bs = true;				//交易方向
	public static double highestProfit = 0;			//盈利最大值 
	
	public static int dayCount = 0;
	public static Date startDate = null;
	public static Date endDate = null;
	public static Date openPositionDate = null;
	public static Date tradingDay = null;
	public static String test_instrument_id = "";
	
	public static int closePositionCount1 = 0;		//符合条件1平仓次数
	public static int closePositionCount2 = 0;		//符合条件2平仓次数
	public static int profitCount = 0;				//盈利次数
	public static int lossCount = 0;				//亏损次数
	public static int balanceCount = 0;				//平衡次数
	
	public static Map<Date, Double> dayMd = new LinkedHashMap<Date, Double>();//存储当前合约全部日行情
	
	public static void initArray(int size){
		updateTimeArray = new String[size];
		lastPriceArray = new double[size];
		priceB1Array = new double[size];
		priceS1Array = new double[size];
		volumeArray = new int[size];
		volumeTotalArray = new int[size];
		volumeB1Array = new int[size];
		volumeS1Array = new int[size];
	}
	
	public static void init(){
		updateTimeArray = null;
		lastPriceArray = null;
		priceB1Array = null;
		priceS1Array = null;
		volumeArray = null;
		volumeTotalArray = null;
		volumeB1Array = null;
		volumeS1Array = null;
		
		closePrice = 0;
		positionPrice = 0 ;
		point = 0 ;	
		transactionCount = 0;	
		longCount = 0;
		shortCount = 0;
		closeCount = 0;
		openOrClose = true ;
		bs = true;
		highestProfit = 0;
		
		dayCount = 0;
		startDate = null;
		endDate = null;
		openPositionDate = null;
		tradingDay = null;
		test_instrument_id = "";
		
		closePositionCount1 = 0;
		closePositionCount2 = 0;
		profitCount = 0;
		lossCount = 0;
		balanceCount = 0;
		dayMd.clear();
	}
}
