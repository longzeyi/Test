package sz.future.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import sz.future.conn.DBConnectionManager;

public class Test1 {
	private static final String str62keys = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println(str62to10("MTA0MjgxNTA4NTczMjAyNw=="));
//		String []str = new String[10];
//		System.out.println(str.length);
	}
	
	private static void getData(){
		Connection conn = DBConnectionManager.getConnection();
		PreparedStatement pstm = null;
		String sqlQuery = "select created_date, last_price tb_md_index_fund";
	}
	
	private static int str62to10(String str62){
		int i10=0;
		for (int i = 0; i < str62.length(); i++) {
            int n = str62.length() - i - 1;
            String s = str62.substring(i, i+1);  
            i10 += str62keys.indexOf(s)*Math.pow(62, n);
        }
		return i10;
	}
}
