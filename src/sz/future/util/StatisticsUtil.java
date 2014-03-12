package sz.future.util;

public class StatisticsUtil {
	
     public static double getMovingAverage(int day, double[] closePriceArray){
    	 double ma = 0d;
    	 for (int i = 0; i < day; i++) {
    		 ma += closePriceArray[i];
		 }
    	 ma = ma/day;
    	 return ma;
     }
     
}


