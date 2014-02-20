package sz.future.trader.comm;

public class M extends Super{
	public static final String instrumentId = "m1405";											//合约名称
	
//	public static int dianshu = 300;																		//合约乘数
//	public static double bzjRate = 0.12 * dianshu;													//保证金比率/每点
//	public static double maiduo = 0 ;																	//买多价
//	public static double maikong = 0 ;																	//卖空价
	public static int calculateThreshold = 10;														//缓存多少次后，开始计算
//	public static double money = 2000000d ;															//总资金
	public static double closePrice = 0;																//平仓价
	public static double positionPrice = 0 ;															//持仓价
	public static double handPrice = 0 ;																//每手价格
	public static double point = 0 ;																		//点数
	public static int transactionCount = 0;															//总交易次数
	public static int longCount = 0;																		//买多交易次数
	public static int shortCount = 0;																		//卖空交易次数
	public static int closeCount = 0;																		//平仓交易次数
	public static boolean type = true ; 																//交易类型
	
	public final static int interval = 5 ;																	//间隙
	public final static double floatSpace = 2;														//忍受浮动空间
	
	public static int profitCount = 0;																	//盈利次数
	public static int lossCount = 0;																		//亏损次数
	public static int balanceCount = 0;																	//平衡次数
	
	public static double level1 = 0;	
	public static double level2 = 0;	
	public static double level3 = 0;	
	public static double enterFlag = 0;	
	public static int j = 0;	
}
