package com.littcore.codegen.model;

import org.apache.commons.lang.ArrayUtils;

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
public class DictConfig {
	
	private DictModule[] dictModuleList;

	/**
	 * @return the dictModuleList
	 */
	public DictModule[] getDictModuleList() {
		return dictModuleList;
	}

	/**
	 * @param dictModuleList the dictModuleList to set
	 */
	public void setDictModuleList(DictModule[] dictModuleList) {
		this.dictModuleList = dictModuleList;
	}

}
