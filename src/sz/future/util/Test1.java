package sz.future.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import sz.future.conn.DBConnectionManager;

public class Test1 {
	
	public static void main(String[] args) throws InterruptedException {
		String []str = new String[10];
		System.out.println(str.length);
	}
	
	private static void getData(){
		Connection conn = DBConnectionManager.getConnection();
		PreparedStatement pstm = null;
		String sqlQuery = "select created_date, last_price tb_md_index_fund";
	}
}
