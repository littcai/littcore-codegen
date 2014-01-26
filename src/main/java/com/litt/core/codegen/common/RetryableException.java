package com.litt.core.codegen.common;


/**
 * 重试异常.
 * 
 * <pre><b>描述：</b>
 * 配合重试实现，当捕获该异常时需要进行重试操作
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 * 
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2010-6-23
 * @version 1.0
 */
public class RetryableException extends Exception
{
	
	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The Constructor.
	 */
	public RetryableException()
	{
		super();
	}	

	/**
	 * The Constructor.
	 * 
	 * @param e 异常
	 */
	public RetryableException(Throwable e)
	{
		super(e);
	}	
}
