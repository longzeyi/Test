package sz.future.trader.comm;

public final class ServerParams extends Super {
	
	public static final String BROKER_ID = "1038";
	public static final String USER_ID = "00000015";
	public static final String PWD = "123456";
	public static final String FRONT_ADDR_TR = "tcp://mn104.ctp.gtja-futures.com:41205";
	public static final String FRONT_ADDR_MD = "tcp://mn101.ctp.gtja-futures.com:41213";
	
	public final static int period = 5;				//这段时间内的收盘价
	public final static int interval = 20 ;			//间隙
	public final static double floatSpace = 16d;	//忍受浮动空间
	public final static double retracement = 0.65d; //回撤比例
	public final static double breakPoint = 3d;	//突破period天内的最低价或最高价的限度
	
}
