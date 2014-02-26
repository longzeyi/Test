package sz.future.test;

import java.util.List;

import sz.future.util.CsvDataUtil;

public class Test1 {


	private static double ds = 0;
	private static int count = 0;
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		for (int i = 100; i <= 900; i+=100) {
			for (int j = 1; j <= 31; j++) {
				try {
					init("F:/baiduyundownload/20130"+(i+j)+"/M05_20130"+(i+j)+".csv");
				} catch (Exception e) {
					continue;
				}
				strategy();
				ds += Global.point;
				count += Global.transactionCount;
				Global.init();
				Thread.sleep(500);
			}
			
//			init("F:/baiduyundownload/20131114/M05_20131114.csv");
		}
		System.out.println(ds);
		System.out.println(count);
//		init("E:/NEW/Book1.csv");
	}

	private static void init(String path) {
		// load csv
		List<String[]> csvList = CsvDataUtil
				.readeCsv(path);
		int size = csvList.size();
		Global.priceArray = new double[size];
		Global.priceB1Array = new double[size];
		Global.priceS1Array = new double[size];
		Global.volumeArray = new int[size];
		Global.volumeTotalArray = new int[size];

		// fill to array
		for (int row = 0; row < csvList.size(); row++) {
			Global.priceArray[row] = Double.parseDouble(csvList.get(row)[2]);
			Global.volumeArray[row] = Integer.parseInt(csvList.get(row)[3]);
			Global.volumeTotalArray[row] = Integer.parseInt(csvList.get(row)[4]);
			Global.priceS1Array[row] = Double.parseDouble(csvList.get(row)[6]);
			Global.priceB1Array[row] = Double.parseDouble(csvList.get(row)[8]);
//			System.out.println(Global.priceArray[row]);
		}
	}
	
	private static void strategy(){
		System.err.println("====================================================================================================");
		for (int i = 40; i < Global.priceArray.length; i=i+Global.interval) {
			//第一次入场
			if (Global.positionPrice == 0){
				double a = Global.priceArray[i-Global.interval] - Global.priceArray[i] ;
				if (a > 0) {
					shortSelling(Global.priceB1Array[i]);//卖空
				} else if (a < 0) {
					buyingLong(Global.priceS1Array[i]);//买多
				} else {
					continue;
				}
				continue;
			}
			
			double b = Global.priceArray[i-30];
			double c = Global.priceArray[i-20];
			double d = Global.priceArray[i-10];
			
			if((b < c) && (c < d) && (d < Global.priceArray[i]) && (Global.priceArray[i] - b) > Global.floatSpace){//up
				//平仓买多
				closeOutPosition(Global.priceB1Array[i], Global.priceS1Array[i]);
				buyingLong(Global.priceS1Array[i]);
			}
			if ((b > c) && (c > d) && (d > Global.priceArray[i]) && Math.abs(Global.priceArray[i] - b) < Global.floatSpace) {//down
				//平仓卖空
				closeOutPosition(Global.priceB1Array[i], Global.priceS1Array[i]);
				shortSelling(Global.priceB1Array[i]);
			}
		}
		print();
	}

	/**
	 * 买多
	 * @param currPrice
	 */
	private static void buyingLong(double currPrice){
		Global.type = true ;
		Global.positionPrice = currPrice ;
		Global.transactionCount ++ ;
		Global.longCount ++ ;
	}
	
	/**
	 * 卖空
	 * @param currPrice
	 */
	private static void shortSelling(double currPrice){
		Global.type = false ;
		Global.positionPrice = currPrice ;
		Global.transactionCount ++ ;
		Global.shortCount ++ ;
	}
	
	/**
	 * 平仓
	 * @param currPrice
	 */
	private static void closeOutPosition(double priceB1, double priceS1){
		double profit = 0;
		if (Global.type) {//买多平仓
			profit = priceB1 - Global.positionPrice;
			Global.point = Global.point + profit;
			System.out.println("第"+Global.transactionCount+"次交易：" + profit);
			if(profit>0){
				Global.profitCount ++;
			} else if (profit<0) {
				Global.lossCount ++;
			} else {
				Global.balanceCount ++;
			}
		} else {//卖空平仓
			profit = Global.positionPrice - priceS1;
			Global.point = Global.point + profit;
			System.out.println("第"+Global.transactionCount+"次交易：" + profit);
			if(profit>0){
				Global.profitCount ++;
			} else if (profit<0) {
				Global.lossCount ++;
			} else {
				Global.balanceCount ++;
			}
		}
		Global.positionPrice = 0 ;
		Global.transactionCount ++ ;
		Global.closeCount ++ ;
	}
	
	private static void print(){
		System.out.println("总交易次数: "+Global.transactionCount);
		System.out.println("买多交易次数:"+Global.longCount);
		System.out.println("卖空交易次数:"+Global.closeCount);
		System.out.println("点数:"+Global.point);
		System.out.println("盈次数："+Global.profitCount);
		System.out.println("亏次数："+Global.lossCount);
		System.out.println("平次数："+Global.balanceCount);
	}
}
