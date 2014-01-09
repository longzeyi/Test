package sz.future.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class CsvDataUtil {
	
	/**
     * 读取CSV
     */
     public static List<String[]>  readeCsv(String path){
         try {
             ArrayList<String[]> csvList = new ArrayList<String[]>();
              CsvReader reader = new CsvReader(path,',',Charset.forName("UTF-8"));
              reader.readHeaders();
              while(reader.readRecord()){
                  csvList.add(reader.getValues());
              }
              reader.close();
//              for(int row=0;row<csvList.size();row++){
//                  String  cell = csvList.get(row)[0];
//                  String  cell2 = csvList.get(row)[1];
//                  System.out.println(cell);
//                  System.out.println(cell2);
//              }
              return csvList;
         }catch(Exception ex){
             System.out.println(ex);
         }
		return null;
     }
     
     
     /**
      * 更新CSV
      */
     public static void writeCsv(){
         try {
             String csvFilePath = "c:/test.csv";
              CsvWriter wr =new CsvWriter(csvFilePath,',',Charset.forName("SJIS"));
              String[] contents = {"aaaaa","bbbbb","cccccc","ddddddddd"};                    
              wr.writeRecord(contents);
              wr.close();
          } catch (IOException e) {
             e.printStackTrace();
          }
     }
     
     public static void main(String[] args) {
		readeCsv("E:/BaiduYunDownload/20131101/AG01_20131101.csv");
	}
}


