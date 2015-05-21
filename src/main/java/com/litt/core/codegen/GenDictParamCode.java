package com.litt.core.codegen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import com.litt.core.codegen.common.GenConstants;
import com.litt.core.codegen.model.DictConfig;
import com.litt.core.codegen.model.DictModule;
import com.litt.core.codegen.model.DictParamType;
import com.litt.core.codegen.util.ConfigUtils;
import com.litt.core.exception.BusiException;
import com.litt.core.util.XmlUtils;


/** 
 * 
 * 生成参数字典的初始化SQL语句.
 * 
 * <pre><b>描述：</b>
 *    生成参数字典的初始化SQL语句 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-2-24
 * @version 1.0
 *
 */
public class GenDictParamCode extends BaseGen
{	
	private String configFilePath;
	private List<DictModule> dictModuleList = new ArrayList<DictModule>();	
	
	public GenDictParamCode(String projectPath, String configFilePath) throws Exception
	{
		super(projectPath);
		this.configFilePath = configFilePath;	
	}
	
	public void prepareData()throws Exception
	{
		File configFile = new File(configFilePath);
		this.importData(configFile);
		
		addProp("dictModuleList", dictModuleList);
	}
		
	private void importData(File configFile)throws Exception
	{
		Document document = XmlUtils.readXml(configFile);
		Element rootE = document.getRootElement();
		String importProject = rootE.attributeValue("import");
		if(!StringUtils.isEmpty(importProject))	//需要导入外部项目配置，约定导入配置与当前配置在同一目录或同级项目目录，递归导入
		{
			File importFile = new File(configFile.getParent(), importProject);
			if(!importFile.exists())
			{
				String importProjectName = StringUtils.substringBefore(importProject, "-dict");
				importFile = new File(new File(configFile.getParentFile().getParent(), importProjectName), importProject);
			}
			if(!importFile.exists())
			{
				throw new BusiException("import file not found："+importFile.getPath());
			}
			this.importData(importFile);
		}
		
		DictConfig config = ConfigUtils.loadByCastor(DictConfig.class, "classpath:dict-conf-mapping.xml", configFile.getPath());
		CollectionUtils.addAll(this.dictModuleList, config.getDictModuleList());		
	}
	
	public void gen() throws Exception
	{
		prepareData();
		String templateFileName = "code/dictparam.ftl";
		
		for (DictModule dictModule : dictModuleList) {
			addProp("dictModule", dictModule);					
			List<DictParamType> dictParamTypeList = dictModule.getDictParamTypeList();
			for (DictParamType dictParamType : dictParamTypeList) {
				addProp("dictParamType", dictParamType);	
				this.genFile(templateFileName, super.getPropMap(), "D:\\CODEGEN", dictParamType.getEnumName()+".java");
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		//GenDictParamSql gen = new GenDictParamSql("classpath:crm_dict_config.xml", GenConstants.DATABASE_TYPE_ORACLE);
		//GenDictParamSql gen = new GenDictParamSql("classpath:crm_dict_config.xml", GenConstants.DATABASE_TYPE_MYSQL5);
		//GenDictParamSql gen = new GenDictParamSql("classpah:project_dict_config.xml", GenConstants.DATABASE_TYPE_SQLSERVER2000);
		//GenDictParamSql gen = new GenDictParamSql("classpath:cidp2.0_dict_config.xml", GenConstants.DATABASE_TYPE_ORACLE);
		//GenDictParamSql gen = new GenDictParamSql("classpath:cidp2.0_dict_config.xml", GenConstants.DATABASE_TYPE_MYSQL5);
		//GenDictParamSql gen = new GenDictParamSql("classpath:cidp2.0_dict_config.xml", GenConstants.DATABASE_TYPE_SQLSERVER2000);
		//GenDictParamSql gen = new GenDictParamSql("classpath:littcrm_dict_config.xml", GenConstants.DATABASE_TYPE_ORACLE);
		GenDictParamCode gen = new GenDictParamCode("", "classpath:conf/trannms-dict-config-code.xml");
		gen.gen();
	}

}
