package com.littcore.codegen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.littcore.common.Utility;
import com.littcore.exception.BusiException;
import com.littcore.util.XmlUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.littcore.codegen.common.GenConstants;
import com.littcore.codegen.model.Domain;
import com.littcore.codegen.model.Func;
import com.littcore.codegen.model.Module;

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
		if(!StringUtils.isEmpty(importProject))	//需要导入外部项目配置，约定导入配置与当前配置在同一目录或与该项目同级目录
		{
			File importFile = new File(moduleFile.getParent(), importProject);
			if(!importFile.exists())
			{
				String importProjectName = StringUtils.substringBefore(importProject, "-module");
				importFile = new File(new File(moduleFile.getParentFile().getParent(), importProjectName), importProject);
			}
			if(!importFile.exists())
			{
				throw new BusiException("import file not found："+importFile.getPath());
			}
			this.init(importFile);
		}
		List<Element> domainEList = rootE.elements();
		
		for (int i=0;i<domainEList.size();i++) {
			Element element = domainEList.get(i);
			Domain domain = parseDomain(null, element, i+maxRootDomainIndex);	
			//检查是否已存在同名的，同名的则覆盖
			boolean isFound = false;
			{				
				for (int j=0;j<domainList.size();j++) {
					if(domainList.get(j).getCode().equalsIgnoreCase(domain.getCode()))
					{
						domainList.set(j, domain);
						isFound = true;
						break;
					}
				}
			}
			if(!isFound)
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
			logger.info("Parse module:{}, parent domain:{}", new Object[]{element.attributeValue("code"), parentDomain.getFullCode()});
			Module module = new Module();
			boolean isHide = Utility.parseBoolean(element.attributeValue("isHide"), false);
			boolean isMenu = Utility.parseBoolean(element.attributeValue("isMenu"), true);
			int position = Utility.parseInt(element.attributeValue("position"));
			String moduleCode = element.attributeValue("code");
			String fullCode = StringUtils.isNumeric(parentDomain.getFullCode())?(parentDomain.getFullCode()+moduleCode):(parentDomain.getFullCode()+"."+moduleCode);
			
			module.setCode(moduleCode);
			module.setFullCode(fullCode);
			module.setMenuType(element.attributeValue("menuType"));
			module.setTitle(element.attributeValue("title"));
			module.setDescr(Utility.trimNull(element.attributeValue("descr"),""));
			module.setMenuName(element.attributeValue("menuName"));
			module.setTableName(element.attributeValue("tableName"));
			module.setModelName(element.attributeValue("modelName"));
			module.setUrl(element.attributeValue("url"));
			module.setIsMenu(isMenu);
			module.setIsHide(isHide);	
			module.setPosition(position==0?index:position);
			module.setIcon(element.attributeValue("icon"));
			
			if(!element.isTextOnly())
			{
				List<Element> subEList = element.elements();
				for (Element subE : subEList) {
					Func func = new Func();
					String funcCode = subE.attributeValue("code");
					String funcFullCode = StringUtils.isNumeric(module.getFullCode())?(module.getFullCode()+funcCode):(module.getFullCode()+"."+funcCode);
					func.setCode(funcCode);
					func.setFullCode(funcFullCode);
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
		logger.info("Parse domain:{}", new Object[]{element.attributeValue("code")});
		
		Domain domain = new Domain();
		boolean isHide = Utility.parseBoolean(element.attributeValue("isHide"), false);
		boolean isMenu = Utility.parseBoolean(element.attributeValue("isMenu"), true);
		int position = Utility.parseInt(element.attributeValue("position"));
		String code = element.attributeValue("code");
		
		domain.setCode(code);
		domain.setFullCode(domain.getCode());
		domain.setTitle(element.attributeValue("title"));
		domain.setDescr(Utility.trimNull(element.attributeValue("descr"),""));
		domain.setMenuName(element.attributeValue("menuName"));
		domain.setPackageName(element.attributeValue("packageName"));
		domain.setParentPackageName(element.attributeValue("parentPackageName"));
		domain.setIsMenu(isMenu);
		domain.setIsHide(isHide);	
		boolean inherit = Utility.parseBoolean(element.attributeValue("inherit"), true);
		domain.setIcon(element.attributeValue("icon"));
		
		if(parentDomain!=null)
		{
			String fullCode = StringUtils.isNumeric(parentDomain.getCode())?(parentDomain.getCode()+domain.getCode()):(parentDomain.getCode()+"."+domain.getCode());
			domain.setFullCode(fullCode);
			if(inherit)
			{
				domain.setPackageName(parentDomain.getPackageName());
				domain.setParentPackageName(parentDomain.getParentPackageName());
			}
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
