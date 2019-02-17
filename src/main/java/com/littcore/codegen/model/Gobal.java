package com.littcore.codegen.model;

import java.util.Date;

import com.littcore.format.FormatDateTime;


/** 
 * 
 * 全局属性.
 * 
 * <pre><b>描述：</b>
 *     
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-11-30
 * @version 1.0
 *
 */
public class Gobal
{	
	/**
	 * 作者.
	 */
	private String author = "<a href=\"mailto:littcai@hotmail.com\">Bob.cai</a>";
	
	/**
	 * 创建日期.
	 */
	private String date = FormatDateTime.formatDate(new Date());
	

	/**
	 * @return the author
	 */
	public String getAuthor()
	{
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author)
	{
		this.author = author;
	}

	/**
	 * @return the date
	 */
	public String getDate()
	{
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date)
	{
		this.date = date;
	}
	
}
