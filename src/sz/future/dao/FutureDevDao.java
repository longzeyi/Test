package sz.future.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcDepthMarketDataField;

import sz.future.conn.DBConnectionManager;
import sz.future.domain.MdDay;

public class FutureDevDao {
	protected static final Log log = LogFactory.getLog(FutureDevDao.class);
	private Connection conn;
	private ResultSet rs;
	private PreparedStatement pst;
	
	private SimpleDateFormat sfDate1 = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sfDate2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public FutureDevDao() {
		conn = null;
		rs = null;
		pst = null;
	}

	public void saveMdDayHistory(
			Map<String, MdDay> dayData){
		conn = DBConnectionManager.getConnection();
		String sql = "INSERT INTO tb_md_day_history (instrument_id, trading_day, highest_price, lowest_price, open_price, close_price, volume, open_interest, create_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
				System.out.println("InstrumentID: " + data.getInstrumentID());
				pst.setDate(2, new java.sql.Date(data.getTradingDay().getTime()));
				System.out.println("tradedate: "+data.getTradingDay());
				pst.setDouble(3, data.getHighest_price());
				pst.setDouble(4, data.getLowest_price());
				pst.setDouble(5, data.getOpen_price());
				pst.setDouble(6, data.getClose_price());
				pst.setInt(7, data.getVolume());
				pst.setDouble(8, data.getOpen_interest());
//				pst.setDate(10, sfDate2.format(new java.sql.Date()));
				pst.setTimestamp(9, (Timestamp) new Timestamp(new Date().getTime()));
				pst.addBatch();
			}
			pst.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closePreparedStatement(pst);
			DBConnectionManager.closeConnection(conn);
		}
	}
}
