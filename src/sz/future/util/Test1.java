package sz.future.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import sz.future.conn.DBConnectionManager;

public class Test1 {
	
	public static void main(String[] args) {
		String [] strs = {"11","22"};
		System.out.println(strs.length);
	}
	
	private static void getData(){
		Connection conn = DBConnectionManager.getConnection();
		PreparedStatement pstm = null;
		String sqlQuery = "select created_date, last_price tb_md_index_fund";
	}
}
