package com.littcore.codegen.model;

public class Func
{
	
	/**
	 * 编号.
	 */
	private String code;
	
	private String fullCode; 
	
	/**
	 * 标题.
	 */
	private String title;
	
	private String descr; 
	
	/**
	 * 
	 */
	private boolean isPermission = true;
	
	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code)
	{
		this.code = code;
	}

	public String getFullCode() {
		return fullCode;
	}

	public void setFullCode(String fullCode) {
		this.fullCode = fullCode;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return the descr
	 */
	public String getDescr() {
		return descr;
	}

	/**
	 * @param descr the descr to set
	 */
	public void setDescr(String descr) {
		this.descr = descr;
	}

	/**
	 * @return the isPermission
	 */
	public boolean isPermission() {
		return isPermission;
	}

	/**
	 * @param isPermission the isPermission to set
	 */
	public void setPermission(boolean isPermission) {
		this.isPermission = isPermission;
	}
}
