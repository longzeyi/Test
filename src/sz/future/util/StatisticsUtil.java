package sz.future.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import sz.future.test.test1.Global;

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
    
    /**
     * 判断指定日期的收盘价是高于MA10还是低于MA10
     * @param date 指定日期
     * @return 高于或等于MA10返回true,低于MA10返回false
     */
    public static boolean belowOrUnderMA(Date date){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	boolean bool = false;
    	double [] prices = new double[10];
    	double closePrice = Global.dayMd.get(date);
    	double maPrice = 0d;
    	Iterator<Date> it = Global.dayMd.keySet().iterator();
    	while(it.hasNext()){
    		Date da = it.next();
    		int i = 0;
    		if(da.getTime()<date.getTime()){
    			prices[i] = Global.dayMd.get(da);
    			i++;
    		}
    		if(i>prices.length){
    			break;
    		}
    	}
    	maPrice = getMovingAverage(prices);
    	if(closePrice<maPrice){
    		bool = false;
    	} else {
    		bool = true;
    	}
    	return bool;
    }
}


