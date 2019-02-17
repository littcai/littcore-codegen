package com.littcore.codegen.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.littcore.util.DateUtils;

/** 
 * 
 * 数据库操作辅助类.
 * 
 * <pre><b>描述：</b>
 *    简化数据库操作 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-5-5
 * @version 1.0
 *
 */
public class DBUtils
{
	private final static Logger logger = LoggerFactory.getLogger(DBUtils.class);
	
	/**
	 * 操作失败重试间隔(默认：10秒).
	 */
	public static final long RETRY_TIME = 10000;
	
	/**
	 * 数据更新的操作
	 * @param sql
	 * @param params
	 * @throws SQLException
	 */
	public static void execute(String sql,Object[] params)
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try
		{
			conn = DBManager.getInstance().getConnection();			
			stmt = conn.prepareStatement(sql);			
			for(int i=0;i<params.length;i++)
			{
				if(params[i]==null)
					stmt.setNull(i+1, Types.VARCHAR);
				else if(params[i] instanceof Date)
				{
					Date d = (Date)params[i];
					stmt.setDate(i+1, DateUtils.convertDate2SqlDate(d));
				}
				else if(params[i] instanceof java.sql.Timestamp)
				{
					Timestamp d = (Timestamp)params[i];
					stmt.setTimestamp(i+1, d);
				}
				else
					stmt.setObject(i+1, params[i]);
				
				
			}
			stmt.execute();
		}
		catch (SQLException e)
		{
			logger.error("数据库执行失败！",e);
		}
		finally
		{
			DBManager.closeQuietly(stmt);
			DBManager.closeQuietly(conn);
		}		
	}
	
	/**
	 * 数据更新的操作
	 * @param sql
	 * @param params
	 * @throws SQLException
	 */
	public static void execute(String sql)
	{
		Connection conn = null;
		Statement stmt = null;
		try
		{
			conn = DBManager.getInstance().getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();	
			stmt.execute(sql);					
			conn.commit();
		}
		catch (SQLException e)
		{
			try
			{
				conn.rollback();
			}
			catch (SQLException e1)
			{				
			}			
		}
		finally
		{
			DBManager.closeQuietly(stmt);
			DBManager.closeQuietly(conn);
		}
	}	
	
	/**
	 * 数据查询的操作
	 * @param sql
	 * @param params
	 * @throws SQLException
	 */
	public static Map queryUnique(String sql) throws RetryableException
	{
		return queryUnique(sql,null);
	}	
	
	/**
	 * 数据查询的操作
	 * @param sql
	 * @param params
	 * @throws SQLException
	 */
	public static Map queryUnique(String sql,Object[] params) throws RetryableException
	{
		List<Map> result = query(sql,params);
		if(result.size()>0)
			return result.get(0);
		else
			return null;
	}	
	
	/**
	 * 数据查询的操作
	 * @param sql
	 * @param params
	 * @throws SQLException
	 */
	public static List<Map> query(String sql) throws RetryableException
	{
		return query(sql,null);
	}
	
	/**
	 * 数据查询的操作
	 * @param sql
	 * @param params
	 * @throws SQLException
	 */
	public static List<Map> query(String sql,Object[] params) throws RetryableException
	{
		List<Map> resultList = new ArrayList<Map>(); //结果集缓存	
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			conn = DBManager.getInstance().getConnection();
			stmt = conn.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
			if(params!=null)
			{
				for(int i=0;i<params.length;i++)
				{
					if(params[i] instanceof java.util.Date)
					{
						Date d = (Date)params[i];
						stmt.setDate(i+1, new java.sql.Date(d.getTime()));
					}
					else
						stmt.setObject(i+1, params[i]);
				}
			}
			rs = stmt.executeQuery();
			rs.setFetchSize(5000);
			//缓存结果集
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
		
			while (rs.next()) {
			 Map rowMap = new HashMap(columnCount);
			 for (int i = 1; i <= columnCount; i++) {
			  switch (rsmd.getColumnType(i)) {
				  case java.sql.Types.TIMESTAMP :
					  rowMap.put(rsmd.getColumnName(i),rs.getTimestamp(i));
					  break;	
				  case java.sql.Types.DATE :
					  rowMap.put(rsmd.getColumnName(i),rs.getDate(i));
					  break;	  
				  case java.sql.Types.CLOB :
					  rowMap.put(rsmd.getColumnName(i), rs.getString(i));
					  break;
				  default :
					  if(rs.getObject(i)==null)
						  rowMap.put(rsmd.getColumnName(i),"");
					  else
						  rowMap.put(rsmd.getColumnName(i), String.valueOf(rs.getObject(i)));
			  }
			 }
			 resultList.add(rowMap);
			}
		}
		catch (SQLException e)
		{
			throw new RetryableException(e);
		}
		finally
		{
			DBManager.closeQuietly(rs);
			DBManager.closeQuietly(stmt);
			DBManager.closeQuietly(conn);
		}
		return resultList;
	}

	
	/**
	 * 数据查询的操作.
	 * 
	 * @param sql SQL语句
	 * @param params 查询参数
	 * @param callback 回调函数
	 * 
	 * @return the object
	 * 
	 * @throws SQLException 	 */
	public static Object query(String sql,Object[] params, ResultSetCallback callback) throws RetryableException
	{		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			conn = DBManager.getInstance().getConnection();
			stmt = conn.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
			if(params!=null)
			{
				for(int i=0;i<params.length;i++)
				{
					if(params[i] instanceof java.util.Date)
					{
						Date d = (Date)params[i];
						stmt.setDate(i+1, DateUtils.convertDate2SqlDate(d));
					}
					else
						stmt.setObject(i+1, params[i]);
				}
			}		
			rs = stmt.executeQuery();
			rs.setFetchDirection(ResultSet.FETCH_FORWARD);
			rs.setFetchSize(5000);
			return callback.doInResultSet(rs);
		}
		catch (SQLException e)
		{	
			throw new RetryableException(e);
		}
		finally
		{
			DBManager.closeQuietly(rs);
			DBManager.closeQuietly(stmt);
			DBManager.closeQuietly(conn);
		}
	}	
	
	/**
	 * 数据统计查询的操作.
	 * 
	 * @param sql 统计的SQL语句
	 * @param params 查询参数
	 * 
	 * @return the object
	 * 
	 * @throws SQLException 	 */
	public static int count(String sql,Object[] params) throws RetryableException
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			conn = DBManager.getInstance().getConnection();
			stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			if(params!=null)
			{
				for(int i=0;i<params.length;i++)
				{
					if(params[i] instanceof java.util.Date)
					{
						Date d = (Date)params[i];
						stmt.setDate(i+1, new java.sql.Date(d.getTime()));
					}
					else
						stmt.setObject(i+1, params[i]);
				}
			}		
			rs = stmt.executeQuery();
			
			ResultSetCallback callback = new ResultSetCallback(){

				public Integer doInResultSet(ResultSet rs) throws SQLException
				{
					if(rs.last())
					{
						return rs.getInt(1);
					}
					else {
						return 0;
					}
				}
			};
			Integer count = (Integer)callback.doInResultSet(rs);
			return count.intValue();
		}
		catch (SQLException e)
		{
			throw new RetryableException(e);
		}
		finally
		{
			DBManager.closeQuietly(rs);
			DBManager.closeQuietly(stmt);
			DBManager.closeQuietly(conn);
		}
	}		
	
	/**
	 * 
	 * <b>标题:</b> SQL语句调用的回调函数.
	 * <pre><b>描述:</b> 
	 *   SQL语句调用的回调函数
	 * </pre>
	 * 
	 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
	 * @since 2007-8-10
	 * @version 1.0
	 *
	 */
	public interface PreparedStatementCallback {
	
		Object doInPreparedStatement(PreparedStatement ps) throws SQLException;
	}	
	
	/**
	 * 
	 * <b>标题:</b> SQL语句调用的回调函数.
	 * <pre><b>描述:</b> 
	 *   SQL语句调用的回调函数
	 * </pre>
	 * 
	 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
	 * @since 2007-8-10
	 * @version 1.0
	 *
	 */
	public interface ResultSetCallback {
	
		Object doInResultSet(ResultSet rs) throws SQLException, RetryableException;
	}
	
	/**
	 * 清理数据库锁定.
	 */
	public static void killLockWait()
	{
		String userName;
		userName=DBManager.getUserName().toUpperCase();
		final String sql="SELECT /*+ NO_MERGE(A) NO_MERGE(b)  */ A.USERNAME,A.SID,A.SERIAL#,A.SCHEMANAME, A.STATUS, A.OSUSER, A.MACHINE, A.TERMINAL,A.PROGRAM,A.TYPE,A.SQL_HASH_VALUE "	
			+" FROM v$session A , v$lock b "
			+" WHERE A.USERNAME='"+userName+"' and a.taddr = b.addr AND A.SID=B.SID and a.STATUS='INACTIVE'";

        DBUtils.retry(new Retry(){
			
			public Object doInRetry() throws RetryableException
			{					
				super.doInRetry();
				DBUtils.query(sql, null, new ResultSetCallback()
				{
					public Object doInResultSet(ResultSet rs) throws SQLException 
					{
						Long sid;
						Long serial;
						while(rs.next())
						{					
							sid=rs.getLong("SID");
							serial=rs.getLong("SERIAL#");
							String killSql;
							killSql="alter system kill session '"+sid+","+serial+"'";
							DBUtils.execute(killSql);
						}
						return null;
					}					
				});
				return null;
			}
		});	
        
		
		
	}
	
	/**
	 * 重试操作.
	 * 用于不断的重试操作
	 * @param callback 回调函数
	 */
	public static Object retry(Retry callback)
	{		
		try
		{
			return callback.doInRetry();
		}
		catch (RetryableException e)
		{			
			logger.error("发生异常，稍后重新尝试...重试次数："+callback.getRetryTimes(), e);
			try
			{
				Thread.sleep(RETRY_TIME);
			}
			catch (InterruptedException e1)
			{
				logger.error("重试等待失败！", e1);
			}
			return retry(callback);
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		long a = System.currentTimeMillis();
		System.out.println(DBUtils.query("SELECT * FROM MPNT_INFO").size());
		System.out.println(System.currentTimeMillis()-a);
	}
	
}
