package sz.future.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcDepthMarketDataField;

import sz.future.conn.DBConnectionManager;

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
			Map<String, CThostFtdcDepthMarketDataField> dayData){
		conn = DBConnectionManager.getConnection();
		String sql = "INSERT INTO tb_md_day_history (instrument_id, trading_day, last_price, highest_price, lowest_price, open_price, close_price, volume, open_interest, create_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pst = (PreparedStatement) conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			Iterator<String> it = dayData.keySet().iterator();
			while(it.hasNext()){
				CThostFtdcDepthMarketDataField data = dayData.get(it.next());
				pst.setString(1, data.getInstrumentID());
				pst.setDate(2, new java.sql.Date(sfDate1.parse(data.getTradingDay()).getTime()));
				pst.setDouble(3, data.getLastPrice());
				pst.setDouble(4, data.getHighestPrice());
				pst.setDouble(5, data.getLowestPrice());
				pst.setDouble(6, data.getOpenPrice());
				pst.setDouble(7, data.getClosePrice());
				pst.setInt(8, data.getVolume());
				pst.setDouble(9, data.getOpenInterest());
//				pst.setDate(10, sfDate2.format(new java.sql.Date()));
				pst.setTimestamp(5, (Timestamp) new Timestamp(new Date().getTime()));
				pst.addBatch();
			}
			pst.executeBatch(); 
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closePreparedStatement(pst);
			DBConnectionManager.closeConnection(conn);
		}
	}
}
