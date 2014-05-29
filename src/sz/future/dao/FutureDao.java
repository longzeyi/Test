package sz.future.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sz.future.conn.DBConnectionManager;
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
		String query = "INSERT INTO tb_md_tick_2013 (instrument_id, trading_day, update_time, last_price, volume, property, bs, b1_price, s1_price, b1_volume, s1_volume, total_volume) VALUES "+insert.toString().substring(0,insert.length()-1);
		
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
	
//	public boolean saveTbDocument(TbDocument tbDocument) throws HttpGrasperException {
//		conn = DBConnectionManager.getConnection();
//		String sql = "INSERT INTO tb_document (data_source_id, cybermedia_id, title, publish_date, modified_date, download_uri, uri_doc_index, author, new_doc, master_doc_id, parent_doc_id, content, dsg_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//		boolean isSuccess = false;
////		System.out.println("tbDocument.getContent()-------------"+tbDocument.getContent());
//		try {
//			conn.setAutoCommit(false);
//			pstm = (PreparedStatement) conn.prepareStatement(sql);
//			pstm.setInt(1, tbDocument.getDataSourceId());
//			pstm.setByte(2, tbDocument.getTbCybermedia());
//			pstm.setString(3, tbDocument.getTitle());
//			pstm.setTimestamp(4, (Timestamp) new Timestamp(tbDocument.getPublishDate().getTime()));
//			pstm.setTimestamp(5, (Timestamp) new Timestamp(tbDocument.getModifiedDate().getTime()));
//			pstm.setString(6, tbDocument.getDownloadUri());
//			pstm.setInt(7, tbDocument.getUriDocIndex());
//			if(tbDocument.getAuthor() != null){
//				pstm.setString(8, tbDocument.getAuthor());
//			}else{
//				pstm.setNull(8, Types.VARCHAR);
//			}
//			if(tbDocument.isNewDoc()) pstm.setInt(9, 1); else pstm.setInt(9, 0);
//			if(tbDocument.getMasterDocId() != null){
//				pstm.setLong(10, tbDocument.getMasterDocId());
//			}else{
//				pstm.setNull(10, Types.BIGINT);
//			}
//			if(tbDocument.getParentDocId() != null){
//				pstm.setLong(11, tbDocument.getParentDocId());
//			}else{
//				pstm.setNull(11, Types.BIGINT);
//			}
//			pstm.setString(12, tbDocument.getContent());
//			if(tbDocument.getDsgId() != null){
//				pstm.setInt(13, tbDocument.getDsgId());
//			}else{
//				pstm.setNull(13, Types.INTEGER);
//			}
//			if(pstm.executeUpdate() > 0) isSuccess = true;
//			conn.commit();
//			conn.setAutoCommit(true);
//		} catch (SQLException e) {
//			if(e.getMessage().startsWith("Duplicate")){
//				log.warn(e);
//				throw new HttpGrasperException();
//			} else {
//				e.printStackTrace();
//			}
//			try {
//				conn.rollback();
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//			log.error("Save failed", e);
//		} finally{
//			closed();
//		}
//		return isSuccess;
//	}
}
