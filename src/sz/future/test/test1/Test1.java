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
		Global.dayMd = dao.loadDayData1(Global.test_instrument_id);
//		Iterator<Date> it = Global.dayMd.keySet().iterator();
//		while(it.hasNext()){
//			System.out.println(it.next());
//		}
//		System.out.println("Global.dayMd SIZE : " + Global.dayMd.size());
		queryMd();
		print();
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
		if(++Global.dayCount == 1){
			Global.startDate = Global.tradingDay;
		} else {
			Global.endDate = Global.tradingDay;
		}
	}
	
	private static void queryMd() {
		//20130104/AG01_20130104.csv
		String instrumentYear = "";
		for (int i = 0; i < Global.months.length; i++) {
			for (int j = 0; j < Global.days.length; j++) {
				StringBuffer sb = new StringBuffer("e:/BaiduYunDownload/");
				String dateDir = Global.year + Global.months[i] + Global.days[j];
				sb.append(dateDir).append("/");
				for (int k = 0; k < Global.strs.length; k++) {
					Map<Integer, String> map = new TreeMap<Integer, String>();
					for (int k2 = 0; k2 < Global.months.length; k2++) {
						StringBuffer sb2 = new StringBuffer();
						sb2.append(Global.strs[k]).append(Global.months[k2]).append("_")
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
					
				}
			}
		}

	}

	private static void strategy() {
		System.err.println("-----------------------------------------------"+Global.tradingDay.toLocaleString()+"-----------------------------------------------");
		//获取12天之内的收盘价
		List<Double> highestPpriceArray = dao.getPriceArray(Global.period, Global.test_instrument_id, Global.tradingDay, 2);
		List<Double> lowestPriceArray = dao.getPriceArray(Global.period, Global.test_instrument_id, Global.tradingDay, 3);
		if(highestPpriceArray.size() >= Global.period){
			Collections.sort(highestPpriceArray);
			Collections.sort(lowestPriceArray);
		} else {
			System.out.println("没有找到对应的结果...");
			return;
		}
		double lowestPrice = lowestPriceArray.get(0);//12天之内最低收盘价
		double highestPrice = highestPpriceArray.get(highestPpriceArray.size()-1);//12天之内最高收盘价
		
		for (int i = 100; i < Global.lastPriceArray.length; i=i+Global.interval) {
			//如果持仓为0
			if(Global.positionPrice == 0){
				//进场条件
				if(Global.lastPriceArray[i] > highestPrice) {
					System.out.println("大于"+Global.period+"天最高价"+ highestPrice);
					//买多开仓
					trader(Global.priceB1Array[i],Global.priceS1Array[i],true,true);
				} else if (Global.lastPriceArray[i] < lowestPrice) {
					System.out.println("小于"+Global.period+"天最低价"+ lowestPrice);
					//卖空开仓
					trader(Global.priceB1Array[i],Global.priceS1Array[i],true,false);
				}
			} else {
				boolean closeFlag1 = false ;
				boolean closeFlag2 = false ;
				boolean closeFlag3 = false ;
				//出场条件
				if(Global.bs){//持有多头头寸
					//更新为最大盈利值
					if(Global.highestProfit < (Global.lastPriceArray[i] - Global.positionPrice)){
						Global.highestProfit = Global.lastPriceArray[i] - Global.positionPrice;
					}
					//亏损超过最高盈利的50%
					
					//浮动亏损超过50点
					closeFlag1 = (Global.positionPrice - Global.lastPriceArray[i]) > Global.floatSpace;
//					System.out.println(Global.lastPriceArray[i]);
					if(StatisticsUtil.belowOrUnderMA(1)){
						closeFlag2 = false;//一天前收盘价在MA10之上
					} else {
						closeFlag2 = true;//一天前收盘价在MA10之下
					}
					if(StatisticsUtil.belowOrUnderMA(2)){
						closeFlag3 = false;//两天前收盘价在MA10之上
					} else {
						closeFlag3 = true;//两天前收盘价在MA10之下
					}
					if(closeFlag1||(closeFlag2&&closeFlag3)){
						if(closeFlag1)System.out.println("浮亏超过"+Global.floatSpace+"..............."+Global.lastPriceArray[i]);
						//多头平仓
						trader(Global.priceB1Array[i],Global.priceS1Array[i],false,true);
						break;//平仓当天不会再开仓
					}
				} else {//持有空头头寸
					//更新为最大盈利值
					if(Global.highestProfit < (Global.positionPrice - Global.lastPriceArray[i])){
						Global.highestProfit = Global.positionPrice - Global.lastPriceArray[i];
					}
					//浮动盈亏超过50点
					closeFlag1 = (Global.lastPriceArray[i] - Global.positionPrice) > Global.floatSpace;
					
					if(StatisticsUtil.belowOrUnderMA(1)){
						closeFlag2 = true;//一天前收盘价在MA10之上
					} else {
						closeFlag2 = false;//一天前收盘价在MA10之下
					}
					if(StatisticsUtil.belowOrUnderMA(2)){
						closeFlag3 = true;//两天前收盘价在MA10之上
					} else {
						closeFlag3 = false;//两天前收盘价在MA10之下
					}
					if(closeFlag1||(closeFlag2&&closeFlag3)){
						if(closeFlag1)System.out.println("浮亏超过"+Global.floatSpace+"..............."+Global.lastPriceArray[i]);
						//空头平仓
						trader(Global.priceB1Array[i],Global.priceS1Array[i],false,false);
						break;//平仓当天不会再开仓
					}
				}
			}
			
		}
//		print();
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
				} else if (profit < 0) {
					Global.lossCount++;
				} else {
					Global.balanceCount++;
				}
				mark();
			}
		} else {
			if (oc) {// 空头开仓
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
				} else if (profit < 0) {
					Global.lossCount++;
				} else {
					Global.balanceCount++;
				}
				mark();
			}
		}
	}

	private static void mark() {
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
	}
}
