package sz.future.util;

public class StatisticsUtil {
	
     public static double getMovingAverage(double[] closePriceArray){
    	 double ma = 0d;
    	 for (int i = 0; i < closePriceArray.length; i++) {
    		 ma += closePriceArray[i];
		 }
    	 ma = ma/closePriceArray.length;
    	 return ma;
     }
     
}


