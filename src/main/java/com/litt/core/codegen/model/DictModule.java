package com.litt.core.codegen.model;

import java.util.ArrayList;
import java.util.List;

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
public class DictModule {
	
	private String packageName;
	
	private String tableName;
	
	private List<DictParamType> dictParamTypeList = new ArrayList<DictParamType>();

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
	 * @return the dictParamTypeList
	 */
	public List<DictParamType> getDictParamTypeList() {
		return dictParamTypeList;
	}

	/**
	 * @param dictParamTypeList the dictParamTypeList to set
	 */
	public void setDictParamTypeList(List<DictParamType> dictParamTypeList) {
		this.dictParamTypeList = dictParamTypeList;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
