package sz.future.util;

import java.text.ParseException;
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
     * 获取前几天收盘价的总和
     * @param closePriceArray
     * @param param
     * @return
     */
    public static double getClosePriceTotal(double[] closePriceArray, int param){
   	 double ma = 0d;
   	 for (int i = 0; i < param; i++) {
   		 ma += closePriceArray[i];
		 }
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
     * @param day 准备取哪一天的MA
     * @param ma 多少天MA
     * @return 高于或等于MA返回true,低于MA返回false
     */
    public static boolean belowOrUnderMA(int day, int ma) {
//    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//    	String strDate = sdf.format(date);
//    	System.out.println("date:  "+strDate);
    	boolean bool = false;
    	double [] prices = new double[ma];
    	double closePrice = 0;
    	double maPrice = 0d;
    	Iterator<Date> it = Global.dayMd.keySet().iterator();
    	int i = 0;
    	int j = 0;
    	boolean falg = false;
    	//取10天的收盘价
    	while(it.hasNext()){
    		Date da = it.next();
    		if(da.getTime() < Global.tradingDay.getTime()){//交易当天的前1天
    			j++;
    			if(j==day){
    				closePrice = Global.dayMd.get(da);//day天前的收盘价
    				falg = true;
    			}
    			if (falg){
    				if(i == prices.length){
    	    			break;
    	    		}
    				prices[i] = Global.dayMd.get(da);
    				i++;
    			}
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
    
    /**
     * @param day 准备取哪一天的MA
     * @param ma 多少天MA
     * @return 获取这一天的MA
     */
    public static double getBeforMA(int day, int ma){
    	double [] prices = new double[ma];
    	double maPrice = 0d;
    	Iterator<Date> it = Global.dayMd.keySet().iterator();
    	int i = 0;
    	int j = 0;
    	boolean falg = false;
    	//取ma天的收盘价
    	while(it.hasNext()){
    		Date da = it.next();
    		if(da.getTime() < Global.tradingDay.getTime()){//交易当天的前1天
    			j++;
    			if(j==day){
    				falg = true;
    			}
    			if (falg){
    				if(i == prices.length){
    	    			break;
    	    		}
    				prices[i] = Global.dayMd.get(da);
    				i++;
    			}
    		}
    	}
    	maPrice = getMovingAverage(prices);
    	return maPrice;
    }
    
    
    /**
     * 获取当前的MA
     * @param ma 多少天MA
     * @param currentPrice 当前最新价格
     * @return 当前的MA
     */
    public static double getCurrentMA(int ma, double currentPrice){
    	double [] prices = new double[ma];
    	double maPrice = 0d;
    	Iterator<Date> it = Global.dayMd.keySet().iterator();
    	prices[0] = currentPrice;
    	int i = 1;
    	//取ma天的收盘价
    	while(it.hasNext()){
    		Date da = it.next();
    		if(da.getTime() < Global.tradingDay.getTime()){//交易当天的前1天
				if(i == prices.length){
	    			break;
	    		}
				prices[i] = Global.dayMd.get(da);
				i++;
    		}
    	}
    	if(i < prices.length-1){
//    		System.out.println("交易数据不够！");
    		return 0d;
    	}
    	maPrice = getMovingAverage(prices);
    	return maPrice;
    }
    
    /**
     * @param ma 多少天MA
     * @param currentPrice 当前最新价格
     * @param type 1:Close 2:High 3:Low
     * @return
     */
    public static double getCurrentHCL(int ma, double currentPrice, int type){
    	double [] prices = new double[ma];
    	double maPrice = 0d;
    	Iterator<Date> it = null;
    	if(type == 1){
    		it = sz.future.test.test2.Global.dayMdC.keySet().iterator();
    		prices[0] = currentPrice;
    	} else if(type == 2){
    		it = sz.future.test.test2.Global.dayMdH.keySet().iterator();
    		prices[0] = sz.future.test.test2.Global.dayHighestPrice;
    	} else if (type == 3){
    		it = sz.future.test.test2.Global.dayMdL.keySet().iterator();
    		prices[0] = sz.future.test.test2.Global.dayLowestPrice;
    	}
    	
    	int i = 1;
    	while(it.hasNext()){
    		Date da = it.next();
    		if(da.getTime() < sz.future.test.test2.Global.tradingDay.getTime()){//交易当天的前1天
				if(i == prices.length){
	    			break;
	    		}
				if(type == 1){
					prices[i] = sz.future.test.test2.Global.dayMdC.get(da);
				} else if(type == 2){
					prices[i] = sz.future.test.test2.Global.dayMdH.get(da);
				} else if (type == 3){
					prices[i] = sz.future.test.test2.Global.dayMdL.get(da);
				}
				i++;
    		}
    	}
    	maPrice = getMovingAverage(prices);
    	return maPrice;
    }
    
    /**
     * @param day 准备取前几天的MA
     * @param ma 多少天MA
     * @param type 1:Close 2:High 3:Low
     * @return
     */
    public static double getBeforeHCL(int day, int ma, int type){
    	double [] prices = new double[ma];
    	double maPrice = 0d;
    	Iterator<Date> it = null;
    	if(type == 1){
    		it = sz.future.test.test2.Global.dayMdC.keySet().iterator();
    	} else if(type == 2){
    		it = sz.future.test.test2.Global.dayMdH.keySet().iterator();
    	} else if (type == 3){
    		it = sz.future.test.test2.Global.dayMdL.keySet().iterator();
    	}
    	
    	int i = 0;
    	int j = 0;
    	boolean falg = false;

    	while(it.hasNext()){
    		Date da = it.next();
    		if(da.getTime() < sz.future.test.test2.Global.tradingDay.getTime()){//交易当天的前1天
    			j++;
    			if(j==day){
    				falg = true;
    			}
    			if (falg){
    				if(i == prices.length){
    	    			break;
    	    		}
    				if(type == 1){
    					prices[i] = sz.future.test.test2.Global.dayMdC.get(da);
    				} else if(type == 2){
    					prices[i] = sz.future.test.test2.Global.dayMdH.get(da);
    				} else if (type == 3){
    					prices[i] = sz.future.test.test2.Global.dayMdL.get(da);
    				}
    				i++;
    			}
    		}
    	}
    	maPrice = getMovingAverage(prices);
    	return maPrice;
    }
    
    public static int daysBetween(Date smdate,Date bdate){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
			smdate=sdf.parse(sdf.format(smdate));
			bdate=sdf.parse(sdf.format(bdate));
		} catch (ParseException e) {
			e.printStackTrace();
		}  
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }
}


