package sz.future.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import sz.future.conn.DBConnectionManager;

public class Test1 {
	
	public static void main(String[] args) {
		String a = "02";
		String b = "03";
		System.out.println(Integer.parseInt(a));
	}
	
	private static void getData(){
		Connection conn = DBConnectionManager.getConnection();
		PreparedStatement pstm = null;
		String sqlQuery = "select created_date, last_price tb_md_index_fund";
	}
}
