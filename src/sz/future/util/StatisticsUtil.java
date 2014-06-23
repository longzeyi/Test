package sz.future.util;

import java.util.Calendar;
import java.util.Date;

import sz.future.dao.FutureDao;

public class StatisticsUtil {
	
     /**
     * @param closePriceArray 连续一段交易日的收盘价集合
     * @return 移动平均值
     */
    public static double getMovingAverage(double[] closePriceArray){
    	 double ma = 0d;
    	 for (int i = 0; i < closePriceArray.length; i++) {
    		 ma += closePriceArray[i];
		 }
    	 ma = ma/closePriceArray.length;
    	 return ma;
     }
     
     
     /**
     * @param date 需要修改的日期
     * @param count 增加或减少多少天，为1表示加1天，为-1表示减1天
     * @return 修改后的日期
     */
    public static Date moveDate(Date date, int count){
    	 Calendar cal=Calendar.getInstance();
    	 cal.setTime(date);
    	 cal.add(Calendar.DATE, count);
    	 return cal.getTime();
     }
    
    public static boolean belowOrUnderMA(Date date){
    	boolean bool = false;
    	double maPrice = 0d;
    	double closePrice = 0d;
    	FutureDao dao = new FutureDao();
    	//还是加到内存中吧，map
    	return bool;
    }
}


