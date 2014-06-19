package sz.future.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sz.future.conn.DBConnectionManager;
import sz.future.domain.MdDay;
import sz.future.domain.MdTick;
import sz.future.util.ImportData;



public class FutureDao {
	protected static final Log log = LogFactory.getLog(FutureDao.class);
	private Connection conn;
	private ResultSet rs;
	private PreparedStatement pst;
	
	private SimpleDateFormat sfDate = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sfTime = new SimpleDateFormat("HH:mm:ss");
	
	public FutureDao(){
		conn = null;
	    rs = null;
	    pst = null;
	}
	
	public void saveFutureHistory(List<String []> data) throws SQLException{
		conn = DBConnectionManager.getConnection();
		String sql = "INSERT INTO tb_qh_history_2013 (date, time, price, volume, volume_total, position_change, price_b1, volume_b1, price_s1, volume_s1, bs, name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Iterator<String []> it = data.iterator();
		while(it.hasNext()){
			String[] str = it.next();
			pst = (PreparedStatement) conn.prepareStatement(sql);
//			pstm.setDate(1, new Date().parse(str[0]));
		}
	}
	
	public void saveMdTick(List<MdTick> data){
		conn = DBConnectionManager.getConnection();
		int index = 0;
		StringBuffer insert = new StringBuffer("");
		for(int i=0; i < data.size(); i++){
			insert.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?),");
		}
		String query = "INSERT INTO tb_md_day_2013 (instrument_id, trading_day, update_time, last_price, volume, property, bs, b1_price, s1_price, b1_volume, s1_volume, total_volume) VALUES "+insert.toString().substring(0,insert.length()-1);
		
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(query);
			pst.execute("SET FOREIGN_KEY_CHECKS=0");
			for (MdTick tick : data) {
				pst.setString(index+1, ImportData.instrument_id);
				try {
					System.err.println("......."+tick.getTradingDay()); 
					pst.setDate(index+2, new java.sql.Date(sfDate.parse(tick.getTradingDay()).getTime()));
					pst.setTime(index+3, new java.sql.Time(sfTime.parse(tick.getUpdateTime()).getTime()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				pst.setDouble(index+4, tick.getLastPrice());
				pst.setInt(index+5, tick.getVolume());
				pst.setInt(index+6, tick.getProperty());
				pst.setString(index+7, tick.getBs());
				pst.setDouble(index+8, tick.getB1Price());
				pst.setDouble(index+9, tick.getS1Price());
				pst.setInt(index+10, tick.getB1Volume());
				pst.setInt(index+11, tick.getS1Volume());
				pst.setInt(index+12, tick.getTotalVolume());
				index = index+12;
			}
//			System.out.println(query);
			pst.executeUpdate();
			conn.commit();
//			System.err.println("LikesCount - Data has been saved.");
			System.err.println("MdTick - Saved: "+data.size());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closePreparedStatement(pst);
			DBConnectionManager.closeConnection(conn);
		}
	}
	
	
	
	public List<MdDay> loadDayData(String instrumentId){
		List<MdDay> list = new ArrayList<MdDay>();
		conn = DBConnectionManager.getConnection();
		String query = "SELECT instrument_id,trading_day,last_price,total_volume FROM tb_md_day_2013 WHERE instrument_id = ? ORDER BY trading_day";
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, instrumentId);
			rs = pst.executeQuery();
			while (rs.next()) {
				MdDay md = new MdDay();
				md.setInstrumentID(rs.getString("instrument_id"));
				md.setTradingDay(rs.getDate("trading_day"));
				md.setLastPrice(rs.getDouble("last_price"));
				md.setTotalVolume(rs.getInt("total_volume"));
				list.add(md);
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
	
	/**
	 * @param days 获取几天前的价格
	 * @param instrumentId 当前合约名
	 * @param tradingDay 当前交易日
	 * @return
	 */
	public double[] getPriceArray(int days, String instrumentId, Date tradingDay){
		double[] array = new double[days];
		conn = DBConnectionManager.getConnection();
		String query = "SELECT last_price FROM tb_md_day_2013 WHERE instrument_id = ? and trading_day <= ? ORDER BY trading_day desc limit ?";
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, instrumentId);
			pst.setDate(2, (java.sql.Date) tradingDay);
			pst.setInt(3, days);
			rs = pst.executeQuery();
			int i = 0;
			while (rs.next()) {
				array[i] = rs.getDouble("last_price");
				i ++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closeResultSet(rs);
			DBConnectionManager.closePreparedStatement(pst);
			DBConnectionManager.closeConnection(conn);
		}
		return array;
	}
}
