package sz.future.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sz.future.conn.DBConnectionManager;
import sz.future.domain.InverstorPositionDetail;
import sz.future.domain.MdDay;

/**
 * @author Sean
 *
 */
public class FutureDevDao {
	protected static final Log log = LogFactory.getLog(FutureDevDao.class);
	private Connection conn;
	private ResultSet rs;
	private PreparedStatement pst;
	
//	private SimpleDateFormat sfDate1 = new SimpleDateFormat("yyyy-MM-dd");
//	private SimpleDateFormat sfDate2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public FutureDevDao() {
		conn = null;
		rs = null;
		pst = null;
	}

	public void saveMdDayHistory(
			Map<String, MdDay> dayData){
		conn = DBConnectionManager.getConnection();
		String sql = "INSERT INTO tb_md_day_history (instrument_id, trading_day, highest_price, lowest_price, open_price, close_price, volume, open_interest, create_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pst = (PreparedStatement) conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			Iterator<String> it = dayData.keySet().iterator();
//			while(it.hasNext()){
//				System.out.println("----"+it.next());
//			}
			while(it.hasNext()){
				MdDay data = dayData.get(it.next());
				pst.setString(1, data.getInstrumentID());
				pst.setDate(2, new java.sql.Date(data.getTradingDay().getTime()));
				pst.setDouble(3, data.getHighest_price());
				pst.setDouble(4, data.getLowest_price());
				pst.setDouble(5, data.getOpen_price());
				pst.setDouble(6, data.getClose_price());
				pst.setInt(7, data.getVolume());
				pst.setDouble(8, data.getOpen_interest());
//				pst.setDate(10, sfDate2.format(new java.sql.Date()));
				pst.setTimestamp(9, (Timestamp) new Timestamp(new Date().getTime()));
				pst.addBatch();
				System.out.println("TradingDate: "+data.getTradingDay().toLocaleString());
				System.out.println("InstrumentID: " + data.getInstrumentID());
			}
			pst.executeBatch();
			conn.commit();
			System.err.println("保存完成！");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closePreparedStatement(pst);
			DBConnectionManager.closeConnection(conn);
		}
	}
	
	
	/**
	 * 获取一段时间内的最高价或者最低价
	 * @param days 近多少天内
	 * @param instrumentId 合约代码
	 * @param type 1：最高价，2：最低价
	 * @return 最高价 or 最低价
	 */
	public double getLimitPrice(int days, String instrumentId, int type){
		double price = 0d;
		LinkedList<Double> list = new LinkedList<Double>();
		conn = DBConnectionManager.getConnection();
		String query1 = "";
		if (type == 1) {
			query1 = "SELECT highest_price FROM tb_md_day_history WHERE instrument_id = ? order by trading_day desc limit ? ";
		} else if (type == 2) {
			query1 = "SELECT lowest_price FROM tb_md_day_history WHERE instrument_id = ? order by trading_day desc limit ? ";
		}
		try {
			pst = conn.prepareStatement(query1);
			pst.setString(1, instrumentId);
			pst.setInt(2, days);
			rs = pst.executeQuery();
			while (rs.next()){
				list.add(rs.getDouble(1));
			}
			if(list.size()<1){
				System.err.println("没有取到合约"+instrumentId+"的历史记录");
			}
			Collections.sort(list);
			if(type == 1){
				price = list.getLast();//max
			} else if (type == 2){
				price = list.getFirst();//min
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closeResultSet(rs);
			DBConnectionManager.closePreparedStatement(pst);
			DBConnectionManager.closeConnection(conn);
		}
		return price;
	}
	
	
	/**
	 * 昨天的MA
	 * @param days
	 * @param instrumentId
	 * @return
	 */
	public double getMA(int days, String instrumentId){
		double price = 0d;
		conn = DBConnectionManager.getConnection();
		String query = "SELECT close_price FROM tb_md_day_history WHERE instrument_id = ?  and trading_day <  cast(now() as date) order by trading_day desc limit ?;";
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, instrumentId);
			pst.setInt(2, days);
			rs = pst.executeQuery();
			while (rs.next()){
				price = price + rs.getDouble(1);
			}
			price = price/days ;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closeResultSet(rs);
			DBConnectionManager.closePreparedStatement(pst);
			DBConnectionManager.closeConnection(conn);
		}
		return price;
	}
	
	public double getPreMA(int days, String instrumentId){
		double price = 0d;
		conn = DBConnectionManager.getConnection();
		String query = "SELECT close_price FROM tb_md_day_history WHERE instrument_id = ? order by create_date desc limit ?";
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, instrumentId);
			pst.setInt(2, days);
			rs = pst.executeQuery();
			while (rs.next()){
				price = price + rs.getDouble(1);
			}
			price = price/days ;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closeResultSet(rs);
			DBConnectionManager.closePreparedStatement(pst);
			DBConnectionManager.closeConnection(conn);
		}
		return price;
	}
	
	public double[] getHistoryClosePrice(int days, String instrumentId){
		double[] closePrices = new double [days];
		int i = 0;
		conn = DBConnectionManager.getConnection();
		String query = "SELECT close_price FROM tb_md_day_history WHERE instrument_id = ? order by create_date desc limit ?";
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, instrumentId);
			pst.setInt(2, days);
			rs = pst.executeQuery();
			while (rs.next()){
				closePrices[i] = rs.getDouble(1);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closeResultSet(rs);
			DBConnectionManager.closePreparedStatement(pst);
			DBConnectionManager.closeConnection(conn);
		}
		return closePrices;
	}
	
//	public void saveInvestorPosition(
//			InverstorPosition ip){
//		conn = DBConnectionManager.getConnection();
//		String sql = "INSERT INTO tb_investor_position (instrument_id, direction_type, open_date, volume, open_price, stop_price) VALUES (?, ?, ?, ?, ?, ?)";
//		try {
//			pst = (PreparedStatement) conn.prepareStatement(sql);
//			conn.setAutoCommit(false);
//			Iterator<String> it = dayData.keySet().iterator();
////			while(it.hasNext()){
////				System.out.println("----"+it.next());
////			}
//			pst.setString(1, ip.getInstrumentID());
//			while(it.hasNext()){
//				MdDay data = dayData.get(it.next());
//				pst.setDate(2, new java.sql.Date(data.getTradingDay().getTime()));
//				pst.setDouble(3, data.getHighest_price());
//				pst.setDouble(4, data.getLowest_price());
//				pst.setDouble(5, data.getOpen_price());
//				pst.setDouble(6, data.getClose_price());
//				pst.setInt(7, data.getVolume());
//				pst.setDouble(8, data.getOpen_interest());
////				pst.setDate(10, sfDate2.format(new java.sql.Date()));
//				pst.setTimestamp(9, (Timestamp) new Timestamp(new Date().getTime()));
//				pst.addBatch();
//				System.out.println("TradingDate: "+data.getTradingDay().toLocaleString());
//				System.out.println("InstrumentID: " + data.getInstrumentID());
//			}
//			pst.executeBatch();
//			conn.commit();
//			System.err.println("保存完成！");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			DBConnectionManager.closePreparedStatement(pst);
//			DBConnectionManager.closeConnection(conn);
//		}
//	}
	
	/**
	 * 根据合约ID获取止损价
	 * @param instrumentId
	 * @return
	 */
	public double getStopPrice(String instrumentId){
		double stopPrice = 0;
		conn = DBConnectionManager.getConnection();
		String query = "SELECT stop_price FROM tb_investor_position WHERE instrument_id = ?";
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, instrumentId);
			rs = pst.executeQuery();
			if (rs.next()){
				stopPrice = rs.getDouble(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closeResultSet(rs);
			DBConnectionManager.closePreparedStatement(pst);
			DBConnectionManager.closeConnection(conn);
		}
		return stopPrice;
	}
	
	
	/**
	 * 保存持仓明细
	 * @param ipd
	 */
//	public void savePositionDetail(
//			InverstorPositionDetail ipd){
//		conn = DBConnectionManager.getConnection();
//		String sql = "INSERT INTO tb_position_detail (trade_id, instrument_id, direction, volume, price, open_date, trading_day, " +
//				"exch_margin, margin_rate,create_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//		try {
//			pst = (PreparedStatement) conn.prepareStatement(sql);
//			conn.setAutoCommit(false);
//			pst.setInt(1, ipd.getTradeID());
//			pst.setString(2, ipd.getInstrumentID());
//			pst.setBoolean(3, ipd.isDirection());
//			pst.setInt(4, ipd.getVolume());
//			pst.setDouble(5, ipd.getPrice());
//			pst.setString(6, ipd.getOpenDate());
//			pst.setString(7, ipd.getTradingDay());
//			pst.setDouble(8, ipd.getExchMargin());
//			pst.setDouble(9, ipd.getMarginRateByMoney());
//			pst.setTimestamp(10, new Timestamp(new Date().getTime()));
//			if(pst.executeUpdate()>0){
//				System.out.println(ipd.getInstrumentID() + " ：持仓明细保存成功！");
//			} else {
//				System.err.println(ipd.getInstrumentID() + " ：持仓明细保存失败！");
//			}
//			conn.commit();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			DBConnectionManager.closePreparedStatement(pst);
//			DBConnectionManager.closeConnection(conn);
//		}
//	}
	
	public void delPositionDetail(){
		conn = DBConnectionManager.getConnection();
		String sql = "delete  from tb_position_detail";
		try {
			pst = (PreparedStatement) conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			pst.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closePreparedStatement(pst);
			DBConnectionManager.closeConnection(conn);
		}
	}
	
	/**
	 * 查询是否持有这个合约
	 * @param instrumentId
	 * @return
	 */
	public boolean hasPosition(String instrumentId){
		boolean flag = false;
		conn = DBConnectionManager.getConnection();
		String query = "SELECT * FROM tb_position_detail WHERE instrument_id = ?";
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, instrumentId);
			rs = pst.executeQuery();
			if (rs.next()){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closeResultSet(rs);
			DBConnectionManager.closePreparedStatement(pst);
			DBConnectionManager.closeConnection(conn);
		}
		return flag;
	}
	
	/**
	 * 查询该合约持仓天数和这段时间的最高价
	 * @param date 开仓日
	 * @param instrumentId 合约名称
	 * @param day 统计多少天内的最高价
	 * @param flag true为获取最高价，false为获取最低价 
	 * @return
	 */
	public double getLimitPriceBy(Date date, String instrumentId, int day, boolean flag){
		int dayCount = 0;
		double limitPrice = 0d;
		conn = DBConnectionManager.getConnection();
		String query = null;
		if(flag){
			query = "SELECT count(*),max(highest_price) FROM `tb_md_day_history` where instrument_id=? and trading_day>? group by instrument_id";
		} else {
			query = "SELECT count(*),min(lowest_price) FROM `tb_md_day_history` where instrument_id=? and trading_day>? group by instrument_id";
		}
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, instrumentId);
			pst.setDate(2, new java.sql.Date(date.getTime()));
			rs = pst.executeQuery();
			rs.next();
			dayCount = rs.getInt(1);
			if(dayCount > day){
				limitPrice = rs.getDouble(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closeResultSet(rs);
			DBConnectionManager.closePreparedStatement(pst);
			DBConnectionManager.closeConnection(conn);
		}
		return limitPrice;
	}
	
	public static void main(String[] args) {
		FutureDevDao dao = new FutureDevDao();
		System.out.println(dao.getMA(8, "m1501"));
	}
}
