package com.littcore.codegen;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.littcore.codegen.model.Domain;
import com.littcore.codegen.model.Func;
import com.littcore.codegen.model.Module;
import com.littcore.common.Utility;
import com.littcore.exception.BusiException;
import com.littcore.util.ResourceUtils;
import com.littcore.util.ValidateUtils;
import com.littcore.util.XmlUtils;

public class ModuleConfigManager {
	
	private String configFilePath;
	
	private Document document;
	
	public ModuleConfigManager(String configFilePath)throws Exception {
		super();
		this.configFilePath = configFilePath;
		this.init();
	}

	private void init() throws Exception
	{
		File funcFile = ResourceUtils.getFile(configFilePath);
		this.document = XmlUtils.readXml(funcFile);
		Element rootE = document.getRootElement();			
	}
	
	private Element findByCode(String code)
	{		
		String[] codes = Utility.splitStringAll(code, 2);
		String xpath = "/config";
		for(int i=0;i<codes.length;i++)
		{
			xpath += "/child::*[@code='"+ codes[i]  +"']";
		}
		Node node = document.selectSingleNode(xpath);
		return (Element)node;
	}
	
	/**
	 * 保存DOMAIN.
	 * @param domain
	 */
	public void saveDomain(Domain domain)
	{
		String code = domain.getCode();
		if(this.findByCode(code)!=null)
			throw new BusiException("编号已存在！");
		//根据CODE获取父元素
		String parentCode = StringUtils.substring(code, 0, code.length()-2);
		Element parentE = this.findByCode(parentCode);
		//将domain追加到父元素最后
		Element domainE = parentE.addElement("domain");
		domainE.addAttribute("code", code.substring(code.length()-2, code.length()));
		domainE.addAttribute("title", domain.getTitle());		
		domainE.addAttribute("menuName", domain.getMenuName());
		domainE.addAttribute("packageName", domain.getPackageName());
		domainE.addAttribute("parentPackageName", domain.getParentPackageName());
		domainE.addAttribute("descr", domain.getDescr());
		domainE.addAttribute("isHide", domain.getIsHide().toString());
		domainE.addAttribute("isMenu", domain.getIsMenu().toString());
		saveFile();
	}

	/**
	 * 
	 */
	private void saveFile() {
		try {
			XmlUtils.writeXml(new File(configFilePath), document);
			XmlUtils.formatXml(new File(configFilePath), XmlUtils.FORMAT_PREETY);
		} catch (Exception e) {
			throw new BusiException("Can't save conf file.", e);
		}
	}	
	
	public void updateDomain(Domain domain) 
	{
		this.updateDomain(domain, false);
	}
	
	/**
	 * 保存DOMAIN.
	 * @param domain
	 */
	public void updateDomain(Domain domain, boolean isSync) 
	{
		String code = domain.getCode();		
		
		Element domainE = this.findByCode(code);
		domainE.attribute("code").setText(code.substring(code.length()-2, code.length()));
		if(!isSync)	//非同步时才更新的属性
		{	
			domainE.attribute("title").setText(domain.getTitle());	
			if(domainE.attribute("menuName")!=null)	
				domainE.attribute("menuName").setText(domain.getMenuName());
			else
				domainE.addAttribute("menuName", domain.getMenuName());
			if(domainE.attribute("descr")!=null)	
				domainE.attribute("descr").setText(domain.getDescr());
			else
				domainE.addAttribute("descr", domain.getDescr());
		}
		domainE.attribute("packageName").setText(domain.getPackageName());
		if(!ValidateUtils.isEmpty(domain.getParentPackageName()))
		{
			if(domainE.attribute("parentPackageName")!=null)
				domainE.attribute("parentPackageName").setText(domain.getParentPackageName());
		}
		
		if(domainE.attribute("isHide")!=null)
			domainE.attribute("isHide").setText(domain.getIsHide().toString());
		else
			domainE.addAttribute("isHide",domain.getIsHide().toString());
		if(domainE.attribute("isMenu")!=null)
			domainE.attribute("isMenu").setText(domain.getIsMenu().toString());
		else
			domainE.addAttribute("isMenu",domain.getIsMenu().toString());
		
		saveFile();
	}
	
	/**
	 * 保存Module.
	 * @param domain
	 */
	public void saveModule(Module module) 
	{
		String code = module.getCode();
		if(this.findByCode(code)!=null)
			throw new BusiException("编号已存在！");
		//根据CODE获取父元素
		String parentCode = StringUtils.substring(code, 0, code.length()-2);
		Element parentE = this.findByCode(parentCode);
		//将domain追加到父元素最后		
		Element domainE = parentE.addElement("module");
		domainE.addAttribute("code", code.substring(code.length()-2, code.length()));
		domainE.addAttribute("title", module.getTitle());		
		domainE.addAttribute("menuName", module.getMenuName());	
		domainE.addAttribute("descr", module.getDescr());
		
		domainE.addAttribute("tableName", module.getTableName());
		domainE.addAttribute("modelName", module.getModelName());
		domainE.addAttribute("url", module.getUrl());
		domainE.addAttribute("isHide", module.getIsHide().toString());
		domainE.addAttribute("isMenu", module.getIsMenu().toString());
		
		saveFile();
	}	
	
	public void updateModule(Module module) 
	{
		this.updateModule(module, false);
	}
	
	/**
	 * 保存Module.
	 * @param domain
	 */
	public void updateModule(Module module, boolean isSync) 
	{
		String code = module.getCode();			
		
		Element domainE = this.findByCode(code);
		domainE.attribute("code").setText(code.substring(code.length()-2, code.length()));		
		if(!isSync)	//非同步时才更新的属性
		{		
			domainE.attribute("title").setText(module.getTitle());		
			if(domainE.attribute("menuName")!=null)
				domainE.attribute("menuName").setText(module.getMenuName());
			else
				domainE.addAttribute("menuName", module.getMenuName());
			if(domainE.attribute("descr")!=null)	
				domainE.attribute("descr").setText(module.getDescr());
			else
				domainE.addAttribute("descr", module.getDescr());
		}	
		if(domainE.attribute("tableName")!=null)			
			domainE.attribute("tableName").setText(module.getTableName());
		if(domainE.attribute("modelName")!=null)			
			domainE.attribute("modelName").setText(module.getModelName());
		if(domainE.attribute("url")!=null)
			domainE.attribute("url").setText(module.getUrl());
		
		if(domainE.attribute("isHide")!=null)
			domainE.attribute("isHide").setText(module.getIsHide().toString());
		else
			domainE.addAttribute("isHide",module.getIsHide().toString());
		if(domainE.attribute("isMenu")!=null)
			domainE.attribute("isMenu").setText(module.getIsMenu().toString());
		else
			domainE.addAttribute("isMenu", module.getIsMenu().toString());
		
		saveFile();
	}	
	
	
	
	/**
	 * 保存Func.
	 * @param domain
	 */
	public void saveFunc(Func func) 
	{
		String code = func.getCode();
		if(this.findByCode(code)!=null)
			throw new BusiException("编号已存在！");
		//根据CODE获取父元素
		String parentCode = StringUtils.substring(code, 0, code.length()-2);
		Element parentE = this.findByCode(parentCode);
		//将domain追加到父元素最后		
		Element domainE = parentE.addElement("func");
		domainE.addAttribute("code", code.substring(code.length()-2, code.length()));
		domainE.addAttribute("title", func.getTitle());		
		domainE.addAttribute("descr", func.getDescr());			
		saveFile();
	}	
	
	public void updateFunc(Func func) 
	{
		this.updateFunc(func, false);
	}
	
	/**
	 * 保存Func.
	 * @param domain
	 */
	public void updateFunc(Func func, boolean isSync) 
	{
		String code = func.getCode();			
		Element domainE = this.findByCode(code);
		domainE.attribute("code").setText(code.substring(code.length()-2, code.length()));		
		if(!isSync)	//非同步时才更新的属性
		{	
			domainE.attribute("title").setText(func.getTitle());
			if(domainE.attribute("descr")!=null)	
				domainE.attribute("descr").setText(func.getDescr());
			else
				domainE.addAttribute("descr", func.getDescr());
		}	
		saveFile();
	}	
	
	/**
	 * 保存Func.
	 * @param domain
	 */
	public void deleteNode(String code) 
	{		
		Element element = this.findByCode(code);
		element.detach();
		saveFile();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		ModuleConfigManager manager = new ModuleConfigManager("D:\\CODEGEN\\trannms-2.3\\trannms-2.3-module-config-en.xml");
		Element element = manager.findByCode("");
		System.out.println(element);
	}

}
