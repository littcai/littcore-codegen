package com.littcore.codegen.common;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import com.littcore.common.Utility;
import com.littcore.util.PropertiesUtils;
import com.littcore.util.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.littcore.codegen.model.db.ColumnModel;
import com.littcore.codegen.model.db.TableModel;
import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.PoolConfig;

/**
 * 数据库连接管理器.
 * 
 * <pre><b>描述：</b>
 * 统一管理数据库的连接和释放
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 * 
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-4-27
 * @version 1.0
 */
public class DBManager
{
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(DBManager.class);
	
	private static DBManager instance;	//静态单例

	/** 数据源. */
	private DataSource dataSource = null;	
	
	private String driverClassName;

	private String url;

	private String username;

	private String password;	

	private int initialPoolSize;
	
	private int minPoolSize;
	
	private int maxPoolSize;
	
	private int acquireIncrement;
	
	private int maxIdleTime;
	
	private int idleConnectionTestPeriod;
	
	static {
		instance = new DBManager();	//静态单例
		try
		{
			File configFile = ResourceUtils.getFile("classpath:jdbc.properties");
			instance.init(configFile);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("初始化数据源成功！");
		}
	}
	
	public static DBManager getInstance()
	{
		return instance;
	}
	
	public static DBManager getInstance(String configFilePath) throws IOException, SQLException
	{
		DBManager instance = new DBManager();	//静态单例
		
		instance.init(new File(configFilePath));
		
		if(logger.isDebugEnabled())
		{
			logger.debug("初始化数据源成功！");
		}
		return instance;		
	}
	
	/**
	 * 初始化
	 *
	 */
	public void init(File configFile) throws IOException, SQLException
	{
		Properties props = PropertiesUtils.loadProperties(configFile);
		this.driverClassName = props.getProperty("jdbc.driverClassName");
		this.url = props.getProperty("jdbc.url");
		this.username = props.getProperty("jdbc.username");
		this.password = props.getProperty("jdbc.password");
		
		this.initialPoolSize = Utility.parseInt(props.getProperty("jdbc.pool.c3p0.init_pool_size"));
		this.minPoolSize = Utility.parseInt(props.getProperty("jdbc.pool.c3p0.min_pool_size"));
		this.maxPoolSize = Utility.parseInt(props.getProperty("jdbc.pool.c3p0.max_pool_size"));
		this.acquireIncrement = Utility.parseInt(props.getProperty("jdbc.pool.c3p0.acquire_increment"));
		this.maxIdleTime = Utility.parseInt(props.getProperty("jdbc.pool.c3p0.max_idle_time"));
		this.idleConnectionTestPeriod = Utility.parseInt(props.getProperty("jdbc.pool.c3p0.idle_connection_test_period"));
		
		try
		{
			Class.forName(this.driverClassName);
		}
		catch (ClassNotFoundException e)
		{
			logger.error("未找到数据库驱动", e);
		}
		
		//创建C3P0的连接池工具
		PoolConfig pcfg = new PoolConfig();
	    // 设置初始的连接池连接数
		if(this.initialPoolSize>0)
			pcfg.setInitialPoolSize(this.initialPoolSize);
		// 设置最小的连接池连接数
		if(this.minPoolSize>0)
			pcfg.setMinPoolSize(this.minPoolSize);
		// 设置最大的连接池连接数
		if(this.maxPoolSize>0)
			pcfg.setMaxPoolSize(this.maxPoolSize);
	   	// 设置每次增长的连接池连接数的个数
		if(this.acquireIncrement>0)
			pcfg.setAcquireIncrement(this.acquireIncrement);
	   	//设置连接最大空闲时间
		if(this.maxIdleTime>0)
			pcfg.setMaxIdleTime(this.maxIdleTime);
	   	//设置空闲连接测试间隔
		if(this.idleConnectionTestPeriod>0)
			pcfg.setIdleConnectionTestPeriod(this.idleConnectionTestPeriod);
		pcfg.setMaxStatements(100);
		DataSource unpooled = DataSources.unpooledDataSource(this.url,this.username,this.password);
		dataSource = DataSources.pooledDataSource(unpooled,pcfg);	

	}
	
	/**
	* 创建静态方法，关闭连接池
	* 
	* @throws SQLException
	*/
	public void destroyDataSource() throws SQLException 
	{
	   // 判断连接池是否存在
	   if (dataSource != null) 
	   {	   
	    // 调用C3P0接口，关闭数据库连接池
	    DataSources.destroy(dataSource);
	   }

	}
	
	/**
	 * 安静的关闭结果集.
	 * 
	 * @param rs 结果集
	 * 
	 * @return true, if closed
	 */
	public static boolean closeQuietly(ResultSet rs)
	{
		try
		{
			if(rs!=null)
				rs.close();
			return true;
		}
		catch (SQLException e)
		{
			return false;
		}
	}
	
	/**
	 * 安静的关闭状态机.
	 * 
	 * @return true, if closed
	 */
	public static boolean closeQuietly(Statement stmt)
	{
		try
		{			
			if(stmt != null)
				stmt.close();
			return true;
		}
		catch (SQLException e)
		{
			return false;
		}
	}	
	
	/**
	 * 安静的关闭状态机.
	 * 
	 * @return true, if closed
	 */
	public static boolean closeQuietly(PreparedStatement stmt)
	{
		try
		{		
			if(stmt != null)
				stmt.close();
			return true;
		}
		catch (SQLException e)
		{
			logger.error("关闭stmt失败！",e);
			return false;
		}
	}
	
	/**
	 * 安静的连接.
	 * 
	 * @param conn 链接
	 * 
	 * @return true, if closed
	 */
	public static boolean closeQuietly(Connection conn)
	{
		try
		{		
			if(conn!= null && !conn.isClosed())
				conn.close();
			return true;
		}
		catch (SQLException e)
		{
			logger.error("关闭conn失败！",e);
			return false;
		}
	}		


	/**
	 * 获得一个数据库连接.
	 * @return Connection
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException
	{
		return dataSource.getConnection();
	}
	
	/**
	 * 获得数据源.
	 * @return DataSource
	 * @throws SQLException
	 */
	public DataSource getDataSource() throws SQLException
	{
		return dataSource;
	}	
	
	/**
	 * 获得用户名.
	 * @return
	 */
	public static String getUserName()
	{
		return DBManager.getInstance().username;
	}
	
	/**
	 * 获得数据库元数据描述.
	 * 
	 * @return DatabaseMetaData
	 * 
	 * @throws SQLException the SQL exception
	 */
	public DatabaseMetaData getDatabaseMetaData() throws SQLException
	{
		Connection conn = getConnection();
		DatabaseMetaData metadata = conn.getMetaData();		
		closeQuietly(conn);
		return metadata;
	}
	
	public TableModel getTableMetadata(String tableName) throws SQLException
	{
		Connection conn = getConnection();		
		DatabaseMetaData databaseMetadata = conn.getMetaData();
		ResultSet rs = databaseMetadata.getColumns(null, "%", tableName, "%");
		
		TableModel table = new TableModel(tableName);
		while(rs.next())
		{			
			String columnName = rs.getString("COLUMN_NAME"); 
			String dataType = rs.getString("DATA_TYPE"); 
			String columnType = rs.getString("TYPE_NAME"); 
			int dataLength = rs.getInt("COLUMN_SIZE"); 
			int digits = rs.getInt("DECIMAL_DIGITS"); 
			int nullable = rs.getInt("NULLABLE"); 	//rs.getInt("IS_NULLABLE");			
			String autoincrement = rs.getString("IS_AUTOINCREMENT");	//YES, NO			
			String comment = rs.getString("REMARKS");		

			System.out.println("dataType："+dataType+ ", columnType:"+ columnType);

			ColumnModel column = new ColumnModel(columnName, columnType);

			column.setDataLength(dataLength);
			column.setNullable(nullable==1);
			column.setComment(comment);
			
			table.addColumn(column);
		}
		closeQuietly(rs);
		closeQuietly(conn);
		return table;
	}
	
	public static void main(String[] args) throws Exception
	{
		DBManager instance = DBManager.getInstance();
		
		TableModel table = instance.getTableMetadata("USER_INFO");
		System.out.println(table.toString());
		
		
		int i = 0;
//		while(true)
//		{	
//			Thread.sleep(1000);
//			Connection conn = DBManager.getConnection();
//			System.out.println(conn.getMetaData().getDatabaseProductName()+ i++);	
//			
//			//DBUtils.execute("UPDATE TEST_UPDATE SET NAME='3' WHERE ID=1");
//		}
//		Connection conn = DBManager.getConnection();
//		int MAX_RETRY_ATTEMPTS = 1;
//		int RETRY_WAIT_TIME = 1000;
//		for (int i = 0; i < MAX_RETRY_ATTEMPTS; i++)
//		{
//		  // 以下代码模拟一次事务
//		  try { 
//			  Statement stmt = conn.createStatement(); 
//		  System.out.println("Transaction started..."); 
//		  stmt.executeUpdate("UPDATE TEST_UPDATE SET NAME='3' WHERE ID=1"); // SQL语句1
//		  //stmt.executeUpdate("UPDATE 2..."); // SQL语句2
//		  //stmt.executeUpdate("UPDATE 3..."); // SQL语句3
//		  //stmt.executeUpdate("UPDATE 3..."); // SQL语句4
//		  // 提交所有更改
//		  conn.commit(); 
//		  System.out.println("事务已完成。"); 
//		  // 确保只运行了一次。
//		  i = MAX_RETRY_ATTEMPTS;
//		  } catch (SQLException e) { 
//		  /**
//		   * 如果返回的SQL代码为-911，回滚会自动完成，程序回滚至前一次的提交状态。 程序将进行重试。
//		   */ 
//		  if (-911 == e.getErrorCode()) {
//		   // 等待RETRY_WAIT_TIME
//		   try { 
//		   Thread.sleep(RETRY_WAIT_TIME); 
//		   } catch (InterruptedException e1) { 
//		    // 即使休眠被打断，但仍要重试。
//		   System.out.println("休眠被打断。"); 
//		   }
//		  }
//		  /**
//		   * 如果返回的SQL代码为-912，表示死锁及超时。 如果是-904，代表已达到资源限度。 在这种情况下，程序将回滚并进行重试。
//		   */ 
//		  else if (-912 == e.getErrorCode() || -904 == e.getErrorCode()) { 
//		   try { 
//		    // 需要回滚
//		    conn.rollback();
//		    } catch (SQLException e1) { 
//		    System.out.println("无法回滚。" + e); 
//		   } 
//		   try { 
//		    // 等待RETRY_WAIT_TIME
//		    Thread.sleep(RETRY_WAIT_TIME); 
//		    } catch (InterruptedException e1) { 
//		     // 即使休眠被打断，但仍要重试。
//		    System.out.println("休眠被打断。" + e1); 
//		   } 
//		  } else { 
//		   // 如果是其他错误，就不进行重试。
//		   i = MAX_RETRY_ATTEMPTS;
//		   System.out.println("有错误发生，错误代码：" 
//		   + e.getErrorCode() + " SQL状态：" 
//		   + e.getSQLState() + "其他信息：" + e.getMessage()); 
//		  }
//		  }
//		}
	}
	
}
