package com.litt.core.codegen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litt.core.codegen.common.GenConstants;
import com.litt.core.codegen.model.Domain;
import com.litt.core.codegen.model.Func;
import com.litt.core.codegen.model.Module;
import com.litt.core.common.Utility;
import com.litt.core.exception.BusiException;
import com.litt.core.util.XmlUtils;

/**
 * .
 * 
 * <pre><b>Description：</b>
 *    
 * </pre>
 * 
 * <pre><b>Changelog：</b>
 *    2014-07-01 增加<import>节点，实现xml继承，在父子项目中不用再维护相同的模块配置
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2012-1-10
 * @version 1.0
 */
public class BaseModuleGen extends BaseGen {
	
	private static final Logger logger = LoggerFactory.getLogger(BaseModuleGen.class);
			
	private String moduleFilePath;
	
	private List<Domain> domainList = new ArrayList<Domain>();
	
	private int maxRootDomainIndex = 0; //最大自动排序根域排序号
	
	public BaseModuleGen(String projectPath, String moduleFilePath) throws Exception {
		super(projectPath);
		this.moduleFilePath = moduleFilePath;
		this.init();
	}
	
	private void init() throws Exception
	{
		File moduleFile = new File(moduleFilePath);
		init(moduleFile);
	}

	private void init(File moduleFile) throws Exception {
		Document document = XmlUtils.readXml(moduleFile);
		Element rootE = document.getRootElement();
		String importProject = rootE.attributeValue("import");
		if(!StringUtils.isEmpty(importProject))	//需要导入外部项目配置，约定导入配置与当前配置在同一目录
		{
			File importFile = new File(moduleFile.getParent(), importProject);
			this.init(importFile);
		}
		List<Element> domainEList = rootE.elements();
		
		for (int i=0;i<domainEList.size();i++) {
			Element element = domainEList.get(i);
			Domain domain = parseDomain(null, element, i+maxRootDomainIndex);				
			domainList.add(domain);
		}
	}
	
	private void parseSub(Domain parentDomain, Element element, int index)
	{		
		String tagName = element.getName();	//标签名称
		if(GenConstants.TAG_NAME_DOMAIN == tagName)	//类型为二级域
		{
			Domain domain = parseDomain(parentDomain, element, index);			
			parentDomain.addSub(domain);
		}
		else if(GenConstants.TAG_NAME_MODULE == tagName)	//类型为模块
		{
			logger.info("Parse module:{}, parent domain:{}", new Object[]{element.attributeValue("name"), parentDomain.getName()});
			Module module = new Module();
			boolean isHide = Utility.parseBoolean(element.attributeValue("isHide"), false);
			boolean isMenu = Utility.parseBoolean(element.attributeValue("isMenu"), true);
			int position = Utility.parseInt(element.attributeValue("position"));
			String moduleCode = element.attributeValue("code");
			String name = element.attributeValue("name");
			moduleCode = StringUtils.isEmpty(moduleCode)?name:moduleCode;
			String fullCode = StringUtils.isNumeric(parentDomain.getFullCode())?(parentDomain.getFullCode()+moduleCode):(parentDomain.getFullCode()+"."+moduleCode);
			
			module.setCode(moduleCode);
			module.setFullCode(fullCode);
			
			module.setName(name);
			module.setTitle(element.attributeValue("title"));
			module.setDescr(Utility.trimNull(element.attributeValue("descr"),""));
			module.setMenuName(element.attributeValue("menuName"));
			module.setTableName(element.attributeValue("tableName"));
			module.setModelName(element.attributeValue("modelName"));
			module.setUrl(element.attributeValue("url"));
			module.setIsMenu(isMenu);
			module.setIsHide(isHide);	
			module.setPosition(position==0?index:position);
			
			
			if(!element.isTextOnly())
			{
				List<Element> subEList = element.elements();
				for (Element subE : subEList) {
					Func func = new Func();
					String funcCode = subE.attributeValue("code");
					String funcName = subE.attributeValue("name");
					funcCode = StringUtils.isEmpty(funcCode)?funcName:funcCode;
					String funcFullCode = StringUtils.isNumeric(module.getFullCode())?(module.getFullCode()+funcCode):(module.getFullCode()+"."+funcCode);
					func.setCode(funcCode);
					func.setFullCode(funcFullCode);
					func.setName(funcName);
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
	private Domain parseDomain(Domain parentDomain, Element element, int index) {
		logger.info("Parse domain:{}", new Object[]{element.attributeValue("name")});
		
		Domain domain = new Domain();
		boolean isHide = Utility.parseBoolean(element.attributeValue("isHide"), false);
		boolean isMenu = Utility.parseBoolean(element.attributeValue("isMenu"), true);
		int position = Utility.parseInt(element.attributeValue("position"));
		String code = element.attributeValue("code");
		String name = element.attributeValue("name");
		code = StringUtils.isEmpty(code)?name:code;
		
		domain.setCode(code);
		domain.setFullCode(domain.getCode());
		domain.setName(name);
		domain.setTitle(element.attributeValue("title"));
		domain.setDescr(Utility.trimNull(element.attributeValue("descr"),""));
		domain.setMenuName(element.attributeValue("menuName"));
		domain.setPackageName(element.attributeValue("packageName"));
		domain.setParentPackageName(element.attributeValue("parentPackageName"));
		domain.setIsMenu(isMenu);
		domain.setIsHide(isHide);	
		if(parentDomain!=null)
		{
			String fullCode = StringUtils.isNumeric(parentDomain.getCode())?(parentDomain.getCode()+domain.getCode()):(parentDomain.getCode()+"."+domain.getCode());
			domain.setFullCode(fullCode);
			domain.setPackageName(parentDomain.getPackageName());
			domain.setParentPackageName(parentDomain.getParentPackageName());
		}
		maxRootDomainIndex = index>maxRootDomainIndex?index:maxRootDomainIndex;
		
		domain.setPosition(position==0?index:position);
		
		//validation
		domain.validate();
		
		if(!element.isTextOnly())
		{
			List<Element> subEList = element.elements();
			for (int j=0;j<subEList.size();j++) {
				Element subE = subEList.get(j);
				this.parseSub(domain, subE, j);
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
