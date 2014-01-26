package com.litt.core.codegen;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.litt.core.codegen.model.DictModule;
import com.litt.core.codegen.model.DictParam;
import com.litt.core.codegen.model.DictParamType;
import com.litt.core.exception.BusiException;
import com.litt.core.util.ResourceUtils;
import com.litt.core.util.XmlUtils;

public class DictConfigManager {
	
	private String configFilePath;
	
	private Document document;
	
	public DictConfigManager(String configFilePath)throws Exception {
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
	
	public Element findModule(String table)
	{		
		String xpath = "/dict/module[@table='"+ table +"']";		
		Node node = document.selectSingleNode(xpath);
		return (Element)node;
	}
	
	public Element findDictType(String code)
	{		
		String xpath = "/dict/module/type[@code='"+ code +"']";		
		Node node = document.selectSingleNode(xpath);
		return (Element)node;
	}
	
	public Element findDictParam(String code, String value)
	{		
		String xpath = "/dict/module/type[@code='"+ code +"']/param[@value='"+value+"']";		
		Node node = document.selectSingleNode(xpath);
		return (Element)node;
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
	
	/**
	 * 保存DOMAIN.
	 * @param domain
	 */
	public void saveModule(DictModule module)
	{
		String table = module.getTableName();
		if(this.findModule(table)!=null)
			throw new BusiException("模块已存在！");
		//根据CODE获取父元素
		Element parentE = document.getRootElement();
		//将domain追加到父元素最后
		Element moduleE = parentE.addElement("module");
		moduleE.addAttribute("table", module.getTableName());
		saveFile();
	}	
	
	public void deleteModule(String table) 
	{		
		Element element = this.findModule(table);
		element.detach();
		saveFile();
	}	
	
	/**
	 * 保存Module.
	 * @param domain
	 */
	public void saveDictType(String table, DictParamType dictType) 
	{
		String code = dictType.getDictType();
		if(this.findDictType(code)!=null)
			throw new BusiException("编号已存在！");
		//根据CODE获取父元素
		Element parentE = this.findModule(table);
		//将domain追加到父元素最后		
		Element typeE = parentE.addElement("type");
		typeE.addAttribute("code", dictType.getDictType());
		typeE.addAttribute("name", dictType.getDictTypeName());	
		
		saveFile();
	}	
		
	/**
	 * 保存Module.
	 * @param domain
	 */
	public void updateDictType(String table, DictParamType dictType, boolean isSync) 
	{
		String code = dictType.getDictType();		
		Element typeE = this.findDictType(code);
		
		if(!isSync)	//非同步时才更新的属性
		{		
			typeE.attribute("name").setText(dictType.getDictTypeName());					
		}	
				
		saveFile();
	}	
	
	public void deleteDictType(String code) 
	{		
		Element element = this.findDictType(code);
		element.detach();
		saveFile();
	}	
	
	/**
	 * 保存Module.
	 * @param domain
	 */
	public void saveDictParam(String table, String code, DictParam dictParam) 
	{
		String value = dictParam.getDictValue();
		if(this.findDictParam(code, value)!=null)
			throw new BusiException("参数已存在！");
		//根据CODE获取父元素
		Element parentE = this.findDictType(code);
		//将domain追加到父元素最后		
		Element paramE = parentE.addElement("param");
		paramE.addAttribute("value", value);
		paramE.addAttribute("content", dictParam.getDictContent());	
		paramE.addAttribute("remark", dictParam.getRemark());	
		saveFile();
	}	
		
	/**
	 * 保存Module.
	 * @param domain
	 */
	public void updateDictParam(String table, String code, DictParam dictParam, boolean isSync) 
	{
		String value = dictParam.getDictValue();
		Element paramE = this.findDictParam(code, value);		
		if(!isSync)	//非同步时才更新的属性
		{		
			paramE.attribute("content").setText(dictParam.getDictContent());		
			if(paramE.attribute("remark")!=null)
				paramE.attribute("remark").setText(dictParam.getRemark());	
			else
				paramE.addAttribute("remark", dictParam.getRemark());	
		}					
		saveFile();
	}	
	
	public void deleteDictParam(String code, String value) 
	{		
		Element element = this.findDictParam(code, value);
		element.detach();
		saveFile();
	}	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		DictConfigManager manager = new DictConfigManager("D:\\CODEGEN\\trannms-2.4\\trannms-2.4-dict-config-zh_CN.xml");
		Element element = manager.findModule("SYSTEM_INFO");
		System.out.println(element);
		System.out.println(manager.findDictType("0001"));
		System.out.println(manager.findDictParam("4001", "0"));
	}

}
