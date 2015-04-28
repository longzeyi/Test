package sz.future.trader.comm;

/**
 * 参数设置常量类
 * @author Sean
 *
 */
public  class ServerParams {
//	public static final String[] instruments = new String[] {"rb1501","TA501","m1501","FG501","a1501","i1501"};//,"pp1501","sr1501","jd1501","pta1501","fg1501","rm1501"
//	public static final String[] instruments = new String[] {"rb1510","TA509","m1509","RM509","SR509"};//全部监测合约，上下一致
//	public static final String[] instruments2 = new String[] {"rb1510","TA509","m1509","RM509","SR509"};//全部监测合约，上下一致
	
	public static final String[] instruments = new String[] {"rb1510","m1509","ag1506","p1509"};//全部监测合约，上下一致（夜盘）
	public static final String[] instruments2 = new String[] {"rb1510","m1509","ag1506","p1509"};//全部监测合约，上下一致（夜盘）
	
//	public static final String FRONT_ADDR_TR = "tcp://mn104.ctp.gtja-futures.com:41205";//国泰君安交易CTP模拟
//	public static final String BROKER_ID = "1038";
//	public static final String USER_ID = "00000015";
//	public static final String PWD = "123456";
	
//	public static final String BROKER_ID = "7030";
//	public static final String USER_ID = "11801031";
//	public static final String PWD = "";
//	public static final String FRONT_ADDR_TR = "tcp://sh-front12.mfc.com.cn:41205";//美尔雅交易真实
	
	public static final String BROKER_ID = "0295";
	public static final String USER_ID = "00134";
	public static final String PWD = "2803855";
	public static final String FRONT_ADDR_TR = "tcp://180.168.146.181:10200";//美尔雅交易模拟
	
//	public static final String FRONT_ADDR_MD = "tcp://mn101.ctp.gtja-futures.com:41213";//国泰君安行情模拟
	public static final String FRONT_ADDR_MD = "tcp://180.168.146.181:10210";//美尔雅行情模拟
//	public static final String FRONT_ADDR_MD = "tcp://sh-front11.168qh.com:41213";//美尔雅行情
	
	public final static int period = 5;				//这段时间内的收盘价
	public final static int interval = 20 ;			//间隙
	public final static double floatSpace = 0.003d;;	//忍受浮动空间
	public final static double retracement = 0.65d; //回撤比例
	public final static double breakPoint = 3d;	//突破period天内的最低价或最高价的限度
	
}
