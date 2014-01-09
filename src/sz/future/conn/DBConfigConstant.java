package sz.future.conn;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class DBConfigConstant {

	public static int checkout_timeout = 0;
	public static int max_poolsize = 0;
	public static int min_poolsize = 0;
	public static int max_statements = 0;
	public static int increment_num = 0;
	public static int retryattempt_num = 0;
	public static int idleConnectionTestPeriod = 120;
	public static int maxIdleTime =60;
	public static boolean testConnectionOnCheckin = true;
	public static String automaticTestTable = "c3p0TestTable";
	
	public static String driver = "";
	public static String dburl = "";
	public static String user = "";
	public static String password = "";

	public static void initConfig(){
		try {
			Configuration config = new PropertiesConfiguration("dbconfig.properties");
			driver = config.getString("driver");
			dburl = config.getString("dburl");
			user = config.getString("user");
			password = config.getString("password");
			
			checkout_timeout = config.getInt("checkout_timeout");
			max_poolsize = config.getInt("max_poolsize");
			min_poolsize = config.getInt("min_poolsize");
			max_statements = config.getInt("max_statements");
			increment_num = config.getInt("increment_num");
			retryattempt_num = config.getInt("retryattempt_num");
			idleConnectionTestPeriod = config.getInt("idleConnectionTestPeriod");
			maxIdleTime = config.getInt("maxIdleTime");
			testConnectionOnCheckin = config.getBoolean("testConnectionOnCheckin");
			automaticTestTable = config.getString("automaticTestTable");
			
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
}
