package sz.future.test.test1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sz.future.dao.FutureDao;
import sz.future.util.CsvDataUtil;
import sz.future.util.StatisticsUtil;

public class Test1 {

	public static String instrument_id = "";
	public static String full_path = "";
	private static Pattern pt = Pattern.compile(
			"([A-Z]+)([0-9]+)_([0-9]+).CSV", Pattern.DOTALL
					+ Pattern.CASE_INSENSITIVE);
	
	private static Matcher mt = null;
	private static FutureDao dao = new FutureDao();
//	private static List<MdDay> dayList = new ArrayList<MdDay>();
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < Global.test_instrument_id_array.length; i++) {
			Global.init();
			Global.test_instrument_id = Global.test_instrument_id_array[i];
			Global.dayMd = dao.loadDayData1(Global.test_instrument_id);
			queryMd();
			print();
		}
	}

	private static void testTrading(String path) {
		// load csv
		List<String[]> csvList = CsvDataUtil.readeCsv(path);
		int size = csvList.size();
//		System.out.println(path + " : " + size);
		Global.initArray(size);
		try {
			Global.tradingDay = sdf.parse(csvList.get(0)[0]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// fill to array
		for (int row = 0; row < csvList.size(); row++) {
			Global.lastPriceArray[row] = Double.parseDouble(csvList.get(row)[2]);
			Global.volumeArray[row] = Integer.parseInt(csvList.get(row)[3]);
			Global.volumeTotalArray[row] = Integer.parseInt(csvList.get(row)[4]);
			Global.priceS1Array[row] = Double.parseDouble(csvList.get(row)[6]);
			Global.priceB1Array[row] = Double.parseDouble(csvList.get(row)[8]);
			// System.out.println(Global.priceArray[row]);
		}
		strategy();
		//记录测试合约的交易时间范围
		if(++Global.dayCount == 1){
			Global.startDate = Global.tradingDay;
		} else {
			Global.endDate = Global.tradingDay;
		}
	}
	
	private static void queryMd() {
		String str = Global.test_instrument_id.replaceAll("\\d+", "");
		//20130104/AG01_20130104.csv
		String instrumentYear = "";
		for (int i = 0; i < Global.months.length; i++) {
			for (int j = 0; j < Global.days.length; j++) {
				StringBuffer sb = new StringBuffer("e:/BaiduYunDownload/");
				String dateDir = Global.year + Global.months[i] + Global.days[j];
				sb.append(dateDir).append("/");
//				for (int k = 0; k < Global.strs.length; k++) {
					Map<Integer, String> map = new TreeMap<Integer, String>();
					for (int k2 = 0; k2 < Global.months.length; k2++) {
						StringBuffer sb2 = new StringBuffer();
						sb2.append(str).append(Global.months[k2]).append("_")
								.append(dateDir).append(".csv");
						String path = sb.toString() + sb2.toString();
						int total = CsvDataUtil.readCsvCount(path);
						if (total > 2000) {
							map.put(total, path);
						}
					}
					if (map.size() == 0) {
						continue;
					}
					Iterator<Entry<Integer, String>> it = map.entrySet()
							.iterator();
					Entry<Integer, String> entry = null;
					while (it.hasNext()) {
						entry = it.next();
						full_path = entry.getValue();
//						System.out.println("行数：" + entry.getKey() + "路径：" + entry.getValue());
						// 保存数据
						mt = pt.matcher(entry.getValue());
						if (mt.find()) {
//							System.out.println(months[i] +" : "+mt.group(2));
							// 如果行情月份小于合约月份，年份为13
							if (Integer.parseInt(Global.months[i]) < Integer.parseInt(mt
									.group(2))) {
								instrumentYear = "13";
							} else {
								instrumentYear = "14";
							}
							instrument_id = mt.group(1) + instrumentYear + mt.group(2);
//							System.out.println("合约名: "+mt.group(1) + instrumentYear
//									+ mt.group(2));
//							System.out.println("合约商品: " + mt.group(1));
//							System.out.println("合约月份: " + mt.group(2));
//							System.out.println("行情日期: " + mt.group(3));
						}
						//如果查找出的合约和要测试的合约相同就进行测试
						if(Global.test_instrument_id.equals(instrument_id)){
							testTrading(full_path);
						}
					}
					
//				}
			}
		}
		if(Global.positionPrice != 0){
			if(Global.bs){
				//合约结束，多头平仓
				trader(Global.endLastPrice,Global.endLastPrice,false,true);
			} else {
				//合约结束，空头平仓
				trader(Global.endLastPrice,Global.endLastPrice,false,false);
			}
		}
	}

	private static void strategy() {
		System.err.println("-----------------------------------------------"+Global.tradingDay.toLocaleString()+"-----------------------------------------------");
		List<Double> highestPpriceArray = dao.getPriceArray(Global.period, Global.test_instrument_id, Global.tradingDay, 2);
		List<Double> lowestPriceArray = dao.getPriceArray(Global.period, Global.test_instrument_id, Global.tradingDay, 3);
		if(highestPpriceArray.size() >= Global.period){
			Collections.sort(highestPpriceArray);
			Collections.sort(lowestPriceArray);
		} else {
			System.out.println("没有找到对应的结果...");
			return;
		}
		double lowestPrice = lowestPriceArray.get(0);//Global.period天之内最低收盘价
		double highestPrice = highestPpriceArray.get(highestPpriceArray.size()-1);//Global.period天之内最高收盘价
		
		double befor1Ma5 = StatisticsUtil.getBeforMA(1, 5);//一天前的MA5
		double befor1Ma10 = StatisticsUtil.getBeforMA(1, 10);//一天前的MA10
		
		for (int i = 100; i < Global.lastPriceArray.length; i=i+Global.interval) {
			//如果持仓为0
			if(Global.positionPrice == 0){
				double currMA5 = StatisticsUtil.getCurrentMA(5, Global.lastPriceArray[i]);
				double currMA10 = StatisticsUtil.getCurrentMA(10, Global.lastPriceArray[i]);
//				double currMA20 = StatisticsUtil.getCurrentMA(20, Global.lastPriceArray[i]);
				//进场条件
				if((Global.lastPriceArray[i] - highestPrice) > Global.breakPoint && (currMA5 > currMA10)) {
					System.out.println("大于"+Global.period+"天最高价"+ highestPrice);
					//买多开仓
					trader(Global.priceB1Array[i],Global.priceS1Array[i],true,true);
				} else if ((lowestPrice - Global.lastPriceArray[i]) > Global.breakPoint && (currMA10 > currMA5)) {
					System.out.println("小于"+Global.period+"天最低价"+ lowestPrice);
					//卖空开仓
					trader(Global.priceB1Array[i],Global.priceS1Array[i],true,false);
				}
			} else {
				boolean closeFlag1 = false ;//浮亏超过限定值Global.floatSpace
				boolean closeFlag2 = false ;//前一日MA5小于或大于MA10
				boolean closeFlag3 = false ;//当前利润小于最高利润百分比
				//出场条件
				if(Global.bs){//持有多头头寸
					//更新为最大盈利值
					if(Global.highestProfit < (Global.lastPriceArray[i] - Global.positionPrice)){
						Global.highestProfit = Global.lastPriceArray[i] - Global.positionPrice;
					}
					//回撤控制
					if(StatisticsUtil.daysBetween(Global.openPositionDate, Global.tradingDay) > 4){
						if((Global.lastPriceArray[i] - Global.positionPrice) < Global.highestProfit * Global.retracement){
							closeFlag3 = true;
						}
					}
					if(befor1Ma5 < befor1Ma10){
						closeFlag2 = true;
					}
					//浮动亏损超过Global.floatSpace
					closeFlag1 = (Global.positionPrice - Global.lastPriceArray[i]) > Global.floatSpace;
					if(closeFlag1||(closeFlag2&&closeFlag3)){
						if(closeFlag1)System.out.println(++Global.closePositionCount1 + " 浮亏超过"+Global.floatSpace+"..............."+Global.lastPriceArray[i]);
						if(closeFlag2&&closeFlag3)System.out.println(++Global.closePositionCount2 + " 盈利回撤..............."+Global.lastPriceArray[i]);
						//多头平仓
						trader(Global.priceB1Array[i],Global.priceS1Array[i],false,true);
						break;//平仓当天不会再开仓
					}
				} else {//持有空头头寸
					//更新为最大盈利值
					if(Global.highestProfit < (Global.positionPrice - Global.lastPriceArray[i])){
						Global.highestProfit = Global.positionPrice - Global.lastPriceArray[i];
					}
					//回撤控制
					if(StatisticsUtil.daysBetween(Global.openPositionDate, Global.tradingDay) > 4){
						if((Global.positionPrice - Global.lastPriceArray[i]) < Global.highestProfit * Global.retracement){
							closeFlag3 = true;
						}
					}
					
					if(befor1Ma5 > befor1Ma10){
						closeFlag2 = true;
					}
					
					//浮动盈亏超过50点
					closeFlag1 = (Global.lastPriceArray[i] - Global.positionPrice) > Global.floatSpace;
					
					if(closeFlag1||(closeFlag2&&closeFlag3)){
						if(closeFlag1)System.out.println(++Global.closePositionCount1 + " 浮亏超过"+Global.floatSpace+"..............."+Global.lastPriceArray[i]);
						if(closeFlag2&&closeFlag3)System.out.println(++Global.closePositionCount2 + " 盈利回撤..............."+Global.lastPriceArray[i]);
						//空头平仓
						trader(Global.priceB1Array[i],Global.priceS1Array[i],false,false);
						break;//平仓当天不会再开仓
					}
				}
			}
			
		}
		//记录每天盈利
		trackProfit(Global.lastPriceArray[Global.lastPriceArray.length-1]);
		//记录每天最后的价格
		Global.endLastPrice = Global.lastPriceArray[Global.lastPriceArray.length-1];
	}

	private static void trackProfit(double lastPrice){
		if(Global.positionPrice == 0){
			dao.saveDayProfit(Global.point);//未持仓
		} else if (Global.bs){
			dao.saveDayProfit(Global.point + (lastPrice - Global.positionPrice));//多头
		} else {
			dao.saveDayProfit(Global.point + (Global.positionPrice - lastPrice));//空头
		}
	}
	
	/**
	 * 交易
	 * 
	 * @param priceB1 买一价
	 * @param priceS1 卖一价
	 * @param oc 开平标志 open:true close:false
	 * @param bs 买卖方向 buy:true sell:false
	 */
	private static void trader(double priceB1, double priceS1, boolean oc,
			boolean bs) {
		double profit = 0;
		if (bs) {
			if (oc) {// 多头开仓
				Global.openPositionDate = Global.tradingDay;
				Global.bs = true;
				Global.openOrClose = true;
				Global.positionPrice = priceS1;
				Global.transactionCount++;
				Global.longCount++;
				System.out.println("多头开仓：第" + Global.transactionCount + "次交易： 多头持仓价："  + Global.positionPrice);
			} else {// 多头平仓
				profit = priceB1 - Global.positionPrice;
				Global.point = Global.point + profit;
				System.err.println("多头平仓： 第" + Global.transactionCount + "次交易： 多头持仓价：" + Global.positionPrice + "  多头平仓价：" + priceB1 + "  浮动盈亏：" + profit);
				if (profit > 0) {
					Global.profitCount++;
					Global.positiveProfit = Global.positiveProfit + profit;
				} else if (profit < 0) {
					Global.lossCount++;
					Global.negativeProfit = Global.negativeProfit + Math.abs(profit);
				} else {
					Global.balanceCount++;
				}
				mark();
			}
		} else {
			if (oc) {// 空头开仓
				Global.openPositionDate = Global.tradingDay;
				Global.bs = false;
				Global.openOrClose = false;
				Global.positionPrice = priceB1;
				Global.transactionCount++;
				Global.shortCount++;
				System.out.println("空头开仓：第" + Global.transactionCount + "次交易： 空头持仓价："  + Global.positionPrice);
			} else {// 空头平仓
				profit = Global.positionPrice - priceS1;
				Global.point = Global.point + profit;
				System.err.println("空头平仓： 第" + Global.transactionCount + "次交易： 空头持仓价：" + Global.positionPrice + "  空头平仓价：" + priceS1 + "  浮动盈亏：" + profit);
				if (profit > 0) {
					Global.profitCount++;
					Global.positiveProfit = Global.positiveProfit + profit;
				} else if (profit < 0) {
					Global.lossCount++;
					Global.negativeProfit = Global.negativeProfit + Math.abs(profit);
				} else {
					Global.balanceCount++;
				}
				mark();
			}
		}
	}

	private static void mark() {
		Global.highestProfit = 0;
		Global.positionPrice = 0;
		Global.transactionCount++;
		Global.closeCount++;
	}

	private static void print() {
		System.out.println("当前测试合约：" + Global.test_instrument_id);
		System.out.println("测试合约时间范围：" + Global.startDate.toLocaleString() + "-" + Global.endDate.toLocaleString());
		System.out.println("测试合约天数：" + Global.dayCount);
		System.out.println("总交易次数: " + Global.transactionCount);
		System.out.println("多头交易次数:" + Global.longCount);
		System.out.println("空头交易次数:" + Global.closeCount);
		System.out.println("盈亏点数:" + Global.point);
		System.out.println("盈次数：" + Global.profitCount);
		System.out.println("亏次数：" + Global.lossCount);
		System.out.println("平次数：" + Global.balanceCount);
		System.out.println("符合条件1平仓次数：" + Global.closePositionCount1);
		System.out.println("符合条件2平仓次数：" + Global.closePositionCount2);
		System.out.println("总盈利:" + Global.positiveProfit);
		System.out.println("总亏损:" + Global.negativeProfit);
	}
}
