package sz.future.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import sz.future.conn.DBConnectionManager;

public class Test1 {
	
	public static void main(String[] args) {
		Map map = new TreeMap();
		map.put(31, "31313131");
		map.put(8, "888888888");
		map.put(1, "11");
		map.put(11, "1111111111");
		map.put(11, "1111111111");
		Iterator it = map.entrySet().iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
	}
	
	private static void getData(){
		Connection conn = DBConnectionManager.getConnection();
		PreparedStatement pstm = null;
		String sqlQuery = "select created_date, last_price tb_md_index_fund";
	}
}
