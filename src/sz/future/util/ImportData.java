package sz.future.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sz.future.dao.FutureDao;
import sz.future.dao.MgFutureDao;
import sz.future.domain.MdTick;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ImportData {

	private static final String[] strs = { "AG", "AU", "CU", "FG", "J", "JM",
			"L", "M", "ME", "OI", "P", "RB", "RM", "RU", "SR", "TA", "V", "Y" };
	private static final String[] months = { "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "10", "11", "12" };
	private static final String[] days = { "01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
			"29", "30", "31" };
	private static final int year = 2013;
	public static String instrument_id = "";
	public static String full_path = "";
	private static Pattern pt = Pattern.compile(
			"([A-Z]+)([0-9]+)_([0-9]+).CSV", Pattern.DOTALL
					+ Pattern.CASE_INSENSITIVE);
	private static Matcher mt = null;

	public static void main(String[] args) {
		queryDoc();
		// saveDataByMI();
	}

	// private static void saveDataByMI(){
	// List<String> list = getListFiles("E:/BaiduYunDownload/","", true);
	// for (String path : list) {
	// if(path.contains("MI_")){
	// System.out.println(path);
	// String fileName = path.substring(29);
	// mt = pt.matcher(fileName);
	// if(mt.find()){
	// INSTRUMENT_ID = mt.group(1);
	// }
	// // saveTickData(path);
	// saveDayData(path);
	// }
	// }
	// }

	private static void queryDoc() {
		String instrumentYear = "";
		for (int i = 0; i < months.length; i++) {
			for (int j = 0; j < days.length; j++) {
				StringBuffer sb = new StringBuffer("e:/BaiduYunDownload/");
				String dateDir = year + months[i] + days[j];
				sb.append(dateDir).append("/");
				for (int k = 0; k < strs.length; k++) {
					Map<Integer, String> map = new TreeMap<Integer, String>();
					for (int k2 = 0; k2 < months.length; k2++) {
						StringBuffer sb2 = new StringBuffer();
						sb2.append(strs[k]).append(months[k2]).append("_")
								.append(dateDir).append(".csv");
						String path = sb.toString() + sb2.toString();
						int count = CsvDataUtil.readCsvCount(path);
						if (count > 2000) {
							map.put(CsvDataUtil.readCsvCount(path), path);
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
						System.out.println("行数：" + entry.getKey() + "路径：" + entry.getValue());
						// 保存数据
						mt = pt.matcher(entry.getValue());
						if (mt.find()) {
//							System.out.println(months[i] +" : "+mt.group(2));
							// 如果行情月份小于合约月份，年份为13
							if (Integer.parseInt(months[i]) < Integer.parseInt(mt
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
//						saveTickData(full_path);
						saveDayData(full_path);
					}
					
				}
			}
		}

	}

	/**
	 * 保存Tick数据
	 * 
	 * @param path
	 */
	private static void saveTickData(String path) {
		MgFutureDao dao = MgFutureDao.getInstance();
		// load csv
		List<String[]> csvList = CsvDataUtil.readeCsv(path);
		List<DBObject> data = new ArrayList<DBObject>();
		int i = 0;
		// fill to array
		for (int row = 0; row < csvList.size(); row++) {
			DBObject tick = new BasicDBObject();
			tick.put("i_id", instrument_id);
			tick.put("t_day", csvList.get(row)[0]);
			tick.put("u_time", csvList.get(row)[1]);
			tick.put("l_price", Double.parseDouble(csvList.get(row)[2]));
			tick.put("vol", Integer.parseInt(csvList.get(row)[3]));
			tick.put("t_vol", Integer.parseInt(csvList.get(row)[4]));
			tick.put("property", Integer.parseInt(csvList.get(row)[5]));
			tick.put("b1_price", Double.parseDouble(csvList.get(row)[6]));
			tick.put("b1_vol", Integer.parseInt(csvList.get(row)[7]));
			tick.put("s1_price", Double.parseDouble(csvList.get(row)[8]));
			tick.put("s1_vol", Integer.parseInt(csvList.get(row)[9]));
			tick.put("bs", csvList.get(row)[10]);
			data.add(tick);
			i++;
			if (i == 1000 || row == csvList.size() - 1) {
				dao.saveMdTick(data);
				data.clear();
				i = 0;
			}
		}
		System.out.println("保存完成...");
	}

	/**
	 * 保存日数据
	 * 
	 * @param path
	 */
	private static void saveDayData(String path) {
		FutureDao dao = new FutureDao();
		List<Double> list = new ArrayList<Double>();
		// load csv
		List<String[]> csvList = CsvDataUtil.readeCsv(path);
		Iterator<String[]> it = csvList.iterator();
		//排序价格，取得最大值和最小值
		while(it.hasNext()){
			list.add(Double.parseDouble(it.next()[2]));
		}
		Collections.sort(list);
		double lowestPrice = list.get(0);
		double highestPrice = list.get(list.size()-1);
		
		int listSize = csvList.size() - 1;
		List<MdTick> data = new ArrayList<MdTick>();
		
		// fill to array
		// MdTick mt = new MdTick();
		// mt.setTradingDay(csvList.get(0)[0]);
		// mt.setUpdateTime(csvList.get(0)[1]);
		// mt.setLastPrice(Double.parseDouble(csvList.get(0)[2]));
		// mt.setVolume(Integer.parseInt(csvList.get(0)[3]));
		// mt.setTotalVolume(Integer.parseInt(csvList.get(0)[4]));
		// mt.setProperty(Integer.parseInt(csvList.get(0)[5]));
		// mt.setB1Price(Double.parseDouble(csvList.get(0)[6]));
		// mt.setB1Volume(Integer.parseInt(csvList.get(0)[7]));
		// mt.setS1Price(Double.parseDouble(csvList.get(0)[8]));
		// mt.setS1Volume(Integer.parseInt(csvList.get(0)[9]));
		// mt.setBs(csvList.get(0)[10]);
		// data.add(mt);
		// 只需要收盘价
		MdTick mt1 = new MdTick();
		mt1.setTradingDay(csvList.get(listSize)[0]);
		mt1.setUpdateTime(csvList.get(listSize)[1]);
		mt1.setLastPrice(Double.parseDouble(csvList.get(listSize)[2]));
		mt1.setVolume(Integer.parseInt(csvList.get(listSize)[3]));
		mt1.setTotalVolume(Integer.parseInt(csvList.get(listSize)[4]));
		mt1.setProperty(Integer.parseInt(csvList.get(listSize)[5]));
		mt1.setB1Price(Double.parseDouble(csvList.get(listSize)[6]));
		mt1.setB1Volume(Integer.parseInt(csvList.get(listSize)[7]));
		mt1.setS1Price(Double.parseDouble(csvList.get(listSize)[8]));
		mt1.setS1Volume(Integer.parseInt(csvList.get(listSize)[9]));
		mt1.setBs(csvList.get(listSize)[10]);
		mt1.setLowestPrice(lowestPrice);
		mt1.setHighestPrice(highestPrice);
		data.add(mt1);
		// System.out.println(csvList.get(0)[0] + " " + csvList.get(0)[1] + " "
		// + INSTRUMENT_ID+": " + csvList.get(0)[2]);
		System.out.println(csvList.get(listSize)[0] + " "
				+ csvList.get(listSize)[1] + " " + instrument_id + ": "
				+ csvList.get(listSize)[2]);
		dao.saveMdTick(data);
	}

	public static List<String> getListFiles(String path, String suffix,
			boolean isdepth) {
		List<String> lstFileNames = new ArrayList<String>();
		File file = new File(path);
		return listFile(lstFileNames, file, suffix, isdepth);
	}

	private static List<String> listFile(List<String> lstFileNames, File f,
			String suffix, boolean isdepth) {
		// 若是目录, 采用递归的方法遍历子目录
		if (f.isDirectory()) {
			File[] t = f.listFiles();

			for (int i = 0; i < t.length; i++) {
				if (isdepth || t[i].isFile()) {
					listFile(lstFileNames, t[i], suffix, isdepth);
				}
			}
		} else {
			String filePath = f.getAbsolutePath();
			if (!suffix.equals("")) {
				int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引
				String tempsuffix = "";

				if (begIndex != -1) {
					tempsuffix = filePath.substring(begIndex + 1,
							filePath.length());
					if (tempsuffix.equalsIgnoreCase(suffix)) {
						lstFileNames.add(filePath);
					}
				}
			} else {
				lstFileNames.add(filePath);
			}
		}
		return lstFileNames;
	}
}
