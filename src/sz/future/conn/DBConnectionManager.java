package sz.future.conn;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBConnectionManager {
	
	private static Connection conn = null;
	private static ComboPooledDataSource connectionPool = null;
	private static boolean initialized = false;
	private static boolean haserror = false;
	
	
	/**
	 * Get a connection from connection pool
	 * @return Connection
	 */
	public synchronized static Connection getConnection() {
		try{
			while(true) {
				try {
				/* first initialize the connection ,if not initialized the connection pool */
			        if(!initialized){
			            initializePool(); 
			            if(!initialized){
			                System.err.println("Important Notice: Create ConnectionPool Error!");
			                Thread.sleep(10000);
			            } 
			        }
					conn = (Connection) connectionPool.getConnection();
					//test
					if (conn == null) {
						System.err.println("Important Notice: Get connection failure...");
						Thread.sleep(10000);
					} else {
						break;
					}
				} catch (SQLException e) {
					System.err.println("Important Notice: Get connection occur exception." + e.toString());
					Thread.sleep(10000);
				}
			}
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	
	/** 
     * initialize the <code>ComboPooledDataSource</code>
     *   
     */ 
   private static void initializePool(){
	   DBConfigConstant.initConfig();
	   connectionPool = new ComboPooledDataSource();
	   try {
		   connectionPool.setDriverClass(DBConfigConstant.driver);
		   connectionPool.setJdbcUrl(DBConfigConstant.dburl);
		   connectionPool.setUser(DBConfigConstant.user);
		   connectionPool.setPassword(DBConfigConstant.password);
		   connectionPool.setCheckoutTimeout(DBConfigConstant.checkout_timeout);
		   connectionPool.setMaxPoolSize(DBConfigConstant.max_poolsize);
		   connectionPool.setMinPoolSize(DBConfigConstant.min_poolsize);
		   connectionPool.setMaxStatements(DBConfigConstant.max_statements);
		   connectionPool.setAcquireIncrement(DBConfigConstant.increment_num);
		   connectionPool.setAcquireRetryAttempts(DBConfigConstant.retryattempt_num);
		   connectionPool.setIdleConnectionTestPeriod(DBConfigConstant.idleConnectionTestPeriod);
		   connectionPool.setMaxIdleTime(DBConfigConstant.maxIdleTime);
		   connectionPool.setTestConnectionOnCheckin(DBConfigConstant.testConnectionOnCheckin);
		   connectionPool.setAutomaticTestTable(DBConfigConstant.automaticTestTable);
	   } catch (PropertyVetoException e) {
		   haserror = true;
		   e.printStackTrace();
	   } catch (Exception ex){
		   haserror = true;
		   ex.printStackTrace();
	   }
	   /*	initialize the connection pool is successful	*/
       if(!haserror) initialized = true; 
   }
   
   /**
    * close the <code>ResultSet</code>  
	* @param stm
	*/
   public static void closeResultSet(ResultSet rs) {
       if (rs != null) {
           try {
               rs.close();
               rs = null;
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
   }
   
   
   /**
    * close the <code>Statement</code>  
	* @param stm
	*/
	public static void closeStatement(Statement stm) {
	       if (stm != null) {
	           try {
	               stm.close();
	               stm = null;
	           } catch (SQLException e) {
	               e.printStackTrace();
	           }
	       }
	   }
	
   /**
    * close the <code>PreparedStatement</code>  
	* @param pstm
	*/
	public static void closePreparedStatement(PreparedStatement pstm) {
	       if (pstm != null) {
	           try {
	               pstm.close();
	               pstm = null;
	           } catch (SQLException e) {
	               e.printStackTrace();
	           }
	       }
	   }
	
   /**
    * close the <code>Connection</code>  
	* @param con
    */
    public static void closeConnection(Connection con) {
	       if (con != null) {
	           try {
	               con.close();
	               con = null;
	           } catch (SQLException e) {
	               e.printStackTrace();
	           }
	           con = null;
	       }
	   }

   //for test
	public static void main(String[] args) {
		DBConnectionManager.getConnection();
	}
}
