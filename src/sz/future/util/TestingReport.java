package sz.future.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import sz.future.conn.DBConnectionManager;

public class TestingReport {

	private Connection conn;
	private ResultSet rs;
	private PreparedStatement pst;
	private static HSSFWorkbook workbook = new HSSFWorkbook();
	private static String outputFile="E:/report.xls";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestingReport tr = new TestingReport();
		Iterator<String> it = tr.getInstrumentId().iterator();
		while(it.hasNext()){
			String instrumentId = it.next();
			System.out.println(instrumentId);
			Map<Date, Double> profits = tr.getProfit(instrumentId);
			tr.createReport(instrumentId, profits);
		}
	}

	private List<String> getInstrumentId(){
		List<String> list = new ArrayList<String>();
		conn = DBConnectionManager.getConnection();
		String query = "SELECT instrument_id FROM tb_day_profit GROUP BY instrument_id";
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("instrument_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closeResultSet(rs);
			DBConnectionManager.closePreparedStatement(pst);
			DBConnectionManager.closeConnection(conn);
		}
		return list;
	}
	
	private Map<Date, Double> getProfit(String instrumentId){
		Map<Date, Double> map = new LinkedHashMap<Date, Double>();
		conn = DBConnectionManager.getConnection();
		String query = "SELECT trading_date,profit FROM tb_day_profit where instrument_id=?";
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, instrumentId);
			rs = pst.executeQuery();
			while (rs.next()) {
				map.put(rs.getDate("trading_date"), rs.getDouble("profit"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closeResultSet(rs);
			DBConnectionManager.closePreparedStatement(pst);
			DBConnectionManager.closeConnection(conn);
		}
		return map;
	}
	
	private void createReport(String instrumentId, Map<Date, Double> profits){
		HSSFSheet sheet = workbook.createSheet(instrumentId);
		Set<Entry<Date, Double>>  set = profits.entrySet();
		Iterator<Entry<Date, Double>> it = set.iterator();
		HSSFRow row ;
		HSSFCell cell;
		int i = 0;
		while(it.hasNext()){
			Entry<Date, Double> entry = it.next();
			row = sheet.createRow(i);
			cell = row.createCell(0);
			cell.setCellValue(entry.getKey().toLocaleString().replace(" 0:00:00", ""));
			cell = row.createCell(1);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(entry.getValue());
			System.out.println(entry.getKey() + " : " + entry.getValue());
			i++;
		}
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream(outputFile);
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void fillData(){
		List<Double> array = new ArrayList<Double>();
		conn = DBConnectionManager.getConnection();
		String query1 = "SELECT last_price FROM tb_day_profit WHERE instrument_id = ? and trading_day = ?";
		String query2 = "";
//		switch (type){
//			case 1:query2 = "SELECT last_price FROM tb_md_day_2013 WHERE instrument_id = ? and trading_day < ? ORDER BY trading_day desc limit ?";
//			case 2:query2 = "SELECT highest_price FROM tb_md_day_2013 WHERE instrument_id = ? and trading_day < ? ORDER BY trading_day desc limit ?";
//			case 3:query2 = "SELECT lowest_price FROM tb_md_day_2013 WHERE instrument_id = ? and trading_day < ? ORDER BY trading_day desc limit ?";
//		}
//		SELECT max(trading_date) FROM `tb_day_profit` where instrument_id='RB1310';
//
//		SELECT trading_date FROM `tb_day_profit` where trading_date>'2013-08-22' group by trading_date;
//
//		INSERT INTO tb_day_profit (trading_date, profit, instrument_id) VALUES (?, ?, ?);
//		if(type == 1){
//			query2 = "SELECT last_price FROM tb_md_day_2013 WHERE instrument_id = ? and trading_day < ? ORDER BY trading_day desc limit ?";
//		} else if (type == 2) {
//			query2 = "SELECT highest_price FROM tb_md_day_2013 WHERE instrument_id = ? and trading_day < ? ORDER BY trading_day desc limit ?";
//		} else if (type == 3) {
//			query2 = "SELECT lowest_price FROM tb_md_day_2013 WHERE instrument_id = ? and trading_day < ? ORDER BY trading_day desc limit ?";
//		}
//		try {
//			pst = conn.prepareStatement(query1);
//			pst.setString(1, instrumentId);
//			pst.setDate(2, new java.sql.Date(tradingDay.getTime()));
//			rs = pst.executeQuery();
//			if(rs.next()){
//				pst = conn.prepareStatement(query2);
//				pst.setString(1, instrumentId);
//				pst.setDate(2, new java.sql.Date(tradingDay.getTime()));
//				pst.setInt(3, days);
//				rs = pst.executeQuery();
//				while (rs.next()) {
//					if(type == 1){
//						array.add(rs.getDouble("last_price"));
//					} else if (type == 2) {
//						array.add(rs.getDouble("highest_price"));
//					} else if (type == 3) {
//						array.add(rs.getDouble("lowest_price"));
//					}
////					switch (type){
////						case 1:array.add(rs.getDouble("last_price"));
////						case 2:array.add(rs.getDouble("highest_price"));
////						case 3:array.add(rs.getDouble("lowest_price"));
////					}
//				}
//			} else {
//				return null;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			DBConnectionManager.closeResultSet(rs);
//			DBConnectionManager.closePreparedStatement(pst);
//			DBConnectionManager.closeConnection(conn);
//		}
//		return array;
	}
}
