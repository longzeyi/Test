package sz.future.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import sz.future.conn.DBConnectionManager;

public class Test1 {
	
	public static void main(String[] args) {
		List list = new ArrayList();
		list.add("bbb");
		list.add("ccc");
		list.add("333");
		list.add("aaa");
		list.add("ddd");
		list.add("eee");
		list.add("111");
		for (Object object : list) {
			System.out.println(object.toString());
		}
	}
	
	private static void getData(){
		Connection conn = DBConnectionManager.getConnection();
		PreparedStatement pstm = null;
		String sqlQuery = "select created_date, last_price tb_md_index_fund";
	}
}
