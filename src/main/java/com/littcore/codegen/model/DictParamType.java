package com.littcore.codegen.model;

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
public class DictParamType {
	
	private String enumName;
	
	private String dictType;
	
	private String dictTypeName;
	
	private int alterMode = 1;
	
	private List<DictParam> dictParamList = new ArrayList<DictParam>();	

	/**
	 * @return the dictType
	 */
	public String getDictType() {
		return dictType;
	}

	/**
	 * @param dictType the dictType to set
	 */
	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	/**
	 * @return the dictTypeName
	 */
	public String getDictTypeName() {
		return dictTypeName;
	}

	/**
	 * @param dictTypeName the dictTypeName to set
	 */
	public void setDictTypeName(String dictTypeName) {
		this.dictTypeName = dictTypeName;
	}

	/**
	 * @return the alterMode
	 */
	public int getAlterMode() {
		return alterMode;
	}

	/**
	 * @param alterMode the alterMode to set
	 */
	public void setAlterMode(int alterMode) {
		this.alterMode = alterMode;
	}

	/**
	 * @return the paramList
	 */
	public List<DictParam> getDictParamList() {
		return dictParamList;
	}

	/**
	 * @param paramList the paramList to set
	 */
	public void setDictParamList(List<DictParam> paramList) {
		this.dictParamList = paramList;
	}

	/**
	 * @return the enumName
	 */
	public String getEnumName() {
		return enumName;
	}

	/**
	 * @param enumName the enumName to set
	 */
	public void setEnumName(String enumName) {
		this.enumName = enumName;
	}
	
	

}
