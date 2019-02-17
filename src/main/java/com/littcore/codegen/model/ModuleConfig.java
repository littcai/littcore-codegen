package com.littcore.codegen.model;

import java.util.List;

/**
 * 利用castor加载.
 * 
 * @author Administrator
 *
 */
public class ModuleConfig {
	
	private List<Domain> domainList;	

	/**
	 * @return the domainList
	 */
	public List<Domain> getDomainList() {
		return domainList;
	}

	/**
	 * @param domainList the domainList to set
	 */
	public void setDomainList(List<Domain> domainList) {
		this.domainList = domainList;
	}

}
