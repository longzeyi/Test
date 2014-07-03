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
		//生成各个合约的报告
		Iterator<String> it = tr.getInstrumentId().iterator();
		while(it.hasNext()){
			String instrumentId = it.next();
			System.out.println(instrumentId);
			Map<Date, Double> profits = tr.getProfit(instrumentId);
			tr.createReport(instrumentId, profits);
		}
		
		//汇总报告数据预处理
		Iterator<String> its = tr.getInstrumentId().iterator();
		while(its.hasNext()){
			tr.fillData(its.next());
		}
		
		//生成汇总报告
		tr.createTotalReport(tr.getTotalProfit());
		
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
	
	private void createTotalReport(Map<Date, Double> profits){
		HSSFSheet sheet = workbook.createSheet("Total");
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
	
	public void fillData(String instrumentId){
		List<Date> dateList = new ArrayList<Date>();
//		List<Double> array = new ArrayList<Double>();
		conn = DBConnectionManager.getConnection();
		String query1 = "SELECT trading_date,profit FROM `tb_day_profit` where instrument_id =? order by trading_date desc limit 1;";
		Date maxDate = null ;
		Double finalProfit = null;
		try {
			pst = conn.prepareStatement(query1);
			pst.setString(1, instrumentId);
			rs = pst.executeQuery();
			if(rs.next()){
				maxDate = rs.getDate("trading_date");
				finalProfit = rs.getDouble("profit");
				System.out.println("maxDate: "+maxDate);
				System.out.println("finalProfit: "+finalProfit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String query2 = "SELECT trading_date FROM `tb_day_profit` where trading_date>? group by trading_date";
		try {
			pst = conn.prepareStatement(query2);
			pst.setDate(1, maxDate);
			rs = pst.executeQuery();
			while(rs.next()){
				dateList.add(rs.getDate("trading_date"));
			}
			if(dateList.size()<1){
				System.out.println("没有需要填充的数据...");
				return;
			}
			System.out.println("dateList Size:" + dateList.size()) ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String query3 = "INSERT INTO tb_day_profit (trading_date, profit, instrument_id) VALUES (?, ?, ?)";
		Iterator<Date> it = dateList.iterator();
		
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(query3);
			while(it.hasNext()){
				pst.setDate(1, it.next());
				pst.setDouble(2, finalProfit);
				pst.setString(3, instrumentId);
				pst.addBatch();
			}
			pst.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closeResultSet(rs);
			DBConnectionManager.closePreparedStatement(pst);
			DBConnectionManager.closeConnection(conn);
		}
	}
	
	private Map<Date, Double> getTotalProfit(){
		Map<Date, Double> map = new LinkedHashMap<Date, Double>();
		conn = DBConnectionManager.getConnection();
		String query = "SELECT trading_date,sum(profit) as profit FROM `tb_day_profit` group by trading_date";
		try {
			pst = conn.prepareStatement(query);
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
}
