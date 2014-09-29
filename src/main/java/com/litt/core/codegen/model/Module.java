package com.litt.core.codegen.model;

import java.util.ArrayList;
import java.util.List;

import com.litt.core.codegen.util.StringUtils;
import com.litt.core.util.ValidateUtils;

/**
 * .
 * 
 * <pre><b>Description：</b>
 *    
 * </pre>
 * 
 * <pre><b>Changelog：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2012-1-5
 * @version 1.0
 */
public class Module {
	
	private String code;
	
	private String fullCode; 
	
	/**
	 * 英文名称
	 */
	private String name; 
	
	/**
	 * 显示名称
	 */
	private String title;
	
	private String descr;
	
	private String url;
	
	private String tableName;

	private String modelName;
	
	private String menuName;
	
	/** 是否生成菜单. */
	private Boolean isMenu = true;
	
	/** 是否隐藏. */
	private Boolean isHide = false;
	
	private int position;
	
	/**
	 * 功能项列表.
	 */
	private List<Func> funcList = new ArrayList<Func>();	
	
	public String getClassName()
	{
		if(!ValidateUtils.isEmpty(modelName))
			return modelName;
		else
		{			
			return StringUtils.hump(tableName, true);
		}
	}
	
	public String getInstanceName()
	{
		if(!ValidateUtils.isEmpty(modelName))
			return modelName.substring(0, 1).toLowerCase() + modelName.substring(1);
		else
		{
			return StringUtils.hump(tableName, false);
		}
	}
	
	
	public boolean getIsDomain()
	{
		return false;
	}
	
	public void addFunc(Func func)
	{
		this.funcList.add(func);
	}

	/**
	 * @return the funcList
	 */
	public List<Func> getFuncList() {
		return funcList;
	}

	/**
	 * @param funcList the funcList to set
	 */
	public void setFuncList(List<Func> funcList) {
		this.funcList = funcList;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the modelName
	 */
	public String getModelName() {
		return modelName;
	}

	/**
	 * @param modelName the modelName to set
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	/**
	 * @return the menuName
	 */
	public String getMenuName() {
		return menuName;
	}

	/**
	 * @param menuName the menuName to set
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	/**
	 * @return the isMenu
	 */
	public Boolean getIsMenu() {
		return isMenu;
	}

	/**
	 * @param isMenu the isMenu to set
	 */
	public void setIsMenu(Boolean isMenu) {
		this.isMenu = isMenu;
	}

	/**
	 * @return the isHide
	 */
	public Boolean getIsHide() {
		return isHide;
	}

	/**
	 * @param isHide the isHide to set
	 */
	public void setIsHide(Boolean isHide) {
		this.isHide = isHide;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the fullCode
	 */
	public String getFullCode() {
		return fullCode;
	}

	/**
	 * @param fullCode the fullCode to set
	 */
	public void setFullCode(String fullCode) {
		this.fullCode = fullCode;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	

}
