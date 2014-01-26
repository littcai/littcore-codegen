package com.litt.core.codegen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.litt.core.codegen.common.GenConstants;
import com.litt.core.codegen.model.Domain;
import com.litt.core.codegen.model.Func;
import com.litt.core.codegen.model.Module;
import com.litt.core.common.Utility;
import com.litt.core.exception.BusiException;
import com.litt.core.util.ResourceUtils;
import com.litt.core.util.XmlUtils;

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
 * @since 2012-1-10
 * @version 1.0
 */
public class BaseModuleGen extends BaseGen {
			
	private String moduleFilePath;
	
	private List<Domain> domainList = new ArrayList<Domain>();

	public BaseModuleGen(String projectPath, String moduleFilePath) throws Exception {
		super(projectPath);
		this.moduleFilePath = moduleFilePath;
		this.init();
	}
	
	private void init() throws Exception
	{
		File funcFile = new File(moduleFilePath);
		Document document = XmlUtils.readXml(funcFile);
		Element rootE = document.getRootElement();	
		List<Element> domainEList = rootE.elements();
		for (Element element : domainEList) {
			Domain domain = parseDomain(null, element);				
			domainList.add(domain);
		}
	}
	
	private void parseSub(Domain parentDomain, Element element)
	{		
		String tagName = element.getName();	//标签名称
		if(GenConstants.TAG_NAME_DOMAIN == tagName)	//类型为二级域
		{
			Domain domain = parseDomain(parentDomain, element);			
			parentDomain.addSub(domain);
		}
		else if(GenConstants.TAG_NAME_MODULE == tagName)	//类型为模块
		{
			Module module = new Module();
			boolean isHide = Utility.parseBoolean(element.attributeValue("isHide"), false);
			boolean isMenu = Utility.parseBoolean(element.attributeValue("isMenu"), true);
			module.setCode(parentDomain.getCode()+ element.attributeValue("code"));
			
			module.setName(element.attributeValue("name"));
			module.setTitle(element.attributeValue("title"));
			module.setDescr(Utility.trimNull(element.attributeValue("descr"),""));
			module.setMenuName(element.attributeValue("menuName"));
			module.setTableName(element.attributeValue("tableName"));
			module.setModelName(element.attributeValue("modelName"));
			module.setUrl(element.attributeValue("url"));
			module.setIsMenu(isMenu);
			module.setIsHide(isHide);	
			
			if(!element.isTextOnly())
			{
				List<Element> subEList = element.elements();
				for (Element subE : subEList) {
					Func func = new Func();
					func.setCode(module.getCode()+subE.attributeValue("code"));
					func.setName(subE.attributeValue("name"));
					func.setTitle(subE.attributeValue("title"));
					func.setDescr(subE.attributeValue("descr"));
					module.addFunc(func);
				}
			}
			parentDomain.addSub(module);
		}	
		else
		{
			throw new BusiException("非法的标签类型，标签类型："+tagName);
		}
	}

	/**
	 * @param element
	 * @return
	 */
	private Domain parseDomain(Domain parentDomain, Element element) {
		Domain domain = new Domain();
		boolean isHide = Utility.parseBoolean(element.attributeValue("isHide"), false);
		boolean isMenu = Utility.parseBoolean(element.attributeValue("isMenu"), true);
		domain.setCode(element.attributeValue("code"));
		domain.setName(element.attributeValue("name"));
		domain.setTitle(element.attributeValue("title"));
		domain.setDescr(Utility.trimNull(element.attributeValue("descr"),""));
		domain.setMenuName(element.attributeValue("menuName"));
		domain.setPackageName(element.attributeValue("packageName"));
		domain.setParentPackageName(element.attributeValue("parentPackageName"));
		domain.setIsMenu(isMenu);
		domain.setIsHide(isHide);	
		if(parentDomain!=null)
		{
			domain.setCode(parentDomain.getCode()+domain.getCode());
			domain.setPackageName(parentDomain.getPackageName());
			domain.setParentPackageName(parentDomain.getParentPackageName());
		}
		//validation
		domain.validate();
		
		if(!element.isTextOnly())
		{
			List<Element> subEList = element.elements();
			for (Element subE : subEList) {
				this.parseSub(domain, subE);
			}
		}
		return domain;
	}

	/**
	 * @return the moduleFilePath
	 */
	public String getModuleFilePath() {
		return moduleFilePath;
	}

	/**
	 * @return the domainList
	 */
	public List<Domain> getDomainList() {
		return domainList;
	}

}
