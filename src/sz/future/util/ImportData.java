package sz.future.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sz.future.dao.FutureDao;
import sz.future.domain.MdTick;

public class ImportData {

	public static String INSTRUMENT_ID = "";
	public static Pattern pt = Pattern.compile("(.*?)_([0-9]+).CSV", Pattern.DOTALL);
	public static Matcher mt = null;
	public static void main(String[] args) {
//		saveData("E:/baiduyundownload/20131202/M05_20131202.csv");
		List<String> list = getListFiles("E:/baiduyundownload/","CSV",true);
		for (String path : list) {
			String fileName = path.substring(29);
//			System.out.println(fileName);
			mt = pt.matcher(fileName);
			if(mt.find()){
				String str1 = mt.group(1);
//				String str2 = mt.group(2);
				StringBuffer tmp = new StringBuffer();
				boolean flag = true;
				for (int i = 0; i < str1.length(); i++) {
//					System.out.println(str1.charAt(i));
					if(Character.isDigit(str1.charAt(i)) && flag){
						tmp.append("13");
						tmp.append(str1.charAt(i));
						flag = false;
					} else {
						tmp.append(str1.charAt(i));
					}
				}
				if(!flag){
					System.out.println(path);
					INSTRUMENT_ID = tmp.toString();
//					System.out.println(INSTRUMENT_ID);
					saveData(path);
				}
			}
		}
	}
	private static void saveData(String path) {
		FutureDao dao = new FutureDao();
		// load csv
		List<String[]> csvList = CsvDataUtil
				.readeCsv(path);
		List<MdTick> data = new ArrayList<MdTick>();
		int i =  0 ;
		// fill to array
		for (int row = 0; row < csvList.size(); row++) {
			MdTick mt = new MdTick();
			mt.setTradingDay(csvList.get(row)[0]);
			mt.setUpdateTime(csvList.get(row)[1]);
			mt.setLastPrice(Double.parseDouble(csvList.get(row)[2]));
			mt.setVolume(Integer.parseInt(csvList.get(row)[3]));
			mt.setTotalVolume(Integer.parseInt(csvList.get(row)[4]));
			mt.setProperty(Integer.parseInt(csvList.get(row)[5]));
			mt.setB1Price(Double.parseDouble(csvList.get(row)[6]));
			mt.setB1Volume(Integer.parseInt(csvList.get(row)[7]));
			mt.setS1Price(Double.parseDouble(csvList.get(row)[8]));
			mt.setS1Volume(Integer.parseInt(csvList.get(row)[9]));
			mt.setBs(csvList.get(row)[10]);
			data.add(mt);
			i++;
			if(i==1000 || row == csvList.size()-1){
				dao.saveMdTick(data);
				data.clear();
				i = 0;
			}
		}
	}
	
	public static List<String> getListFiles(String path, String suffix, boolean isdepth) {
		  List<String> lstFileNames = new ArrayList<String>();
		  File file = new File(path);
		  return listFile(lstFileNames, file, suffix, isdepth);
	}
	
	private static List<String> listFile(List<String> lstFileNames, File f, String suffix, boolean isdepth) {
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
		     tempsuffix = filePath.substring(begIndex + 1, filePath.length());
		     if (tempsuffix.equals(suffix)) {
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



