package sz.future.test.test1;

public class Global {
	public static String[] updateTimeArray = null;
	public static double[] lastPriceArray = null;	//最新价
	public static double[] priceB1Array = null;		//申买价1
	public static double[] priceS1Array = null;		//申卖价1
	public static int[] volumeArray = null;			//成交量
	public static int[] volumeTotalArray = null;	//成交总量
	public static int[] volumeB1Array = null;
	public static int[] volumeS1Array = null;
	
//	public static int dianshu = 300;				//合约乘数
//	public static double bzjRate = 0.12 * dianshu;	//保证金比率/每点
//	public static double maiduo = 0 ;				//买多价
//	public static double maikong = 0 ;				//卖空价
	public static double money = 2000000d ;			//总资金
	public static double closePrice = 0;			//平仓价
	public static double positionPrice = 0 ;		//持仓价
	public static double handPrice = 0 ;			//每手价格
	public static double point = 0 ;				//点数
	public static int transactionCount = 0;			//总交易次数
	public static int longCount = 0;				//买多交易次数
	public static int shortCount = 0;				//卖空交易次数
	public static int closeCount = 0;				//平仓交易次数
	public static boolean openOrClose = true ; 		//交易类型
	public static boolean bs = true;					//交易方向
	
	public final static int interval = 100 ;			//间隙
	public final static double floatSpace = 2;		//忍受浮动空间
	
	public static int profitCount = 0;				//盈利次数
	public static int lossCount = 0;				//亏损次数
	public static int balanceCount = 0;				//平衡次数
	
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
//	public static void init(){
//		money = 2000000d ;			//总资金
//		closePrice = 0;			//平仓价
//		positionPrice = 0 ;		//持仓价
//		handPrice = 0 ;			//每手价格
//		point = 0 ;				//点数
//		transactionCount = 0;			//总交易次数
//		longCount = 0;				//买多交易次数
//		shortCount = 0;				//卖空交易次数
//		closeCount = 0;				//平仓交易次数
//		openOrClose = true ; 			//交易类型
//		bs = true;
//		
//		profitCount = 0;				//盈利次数
//		lossCount = 0;				//亏损次数
//		balanceCount = 0;				//平衡次数
//	}
}
