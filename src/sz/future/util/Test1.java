package sz.future.util;

import java.sql.Connection;
import java.sql.PreparedStatement;

import sz.future.conn.DBConnectionManager;

public class Test1 {
	
	public static void main(String[] args) {
		
	}
	
	private static void getData(){
		Connection conn = DBConnectionManager.getConnection();
		PreparedStatement pstm = null;
		String sqlQuery = "select created_date, last_price tb_md_index_fund";
	}
}
