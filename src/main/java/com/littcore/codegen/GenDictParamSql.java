package com.littcore.codegen;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import com.littcore.codegen.common.GenConstants.DatabaseType;
import com.littcore.codegen.common.GenConstants.LangType;
import com.littcore.codegen.model.DictConfig;
import com.littcore.codegen.model.DictModule;
import com.littcore.codegen.util.ConfigUtils;
import com.littcore.exception.BusiException;
import com.littcore.util.XmlUtils;


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
public class GenDictParamSql extends BaseGen
{
	private DatabaseType databaseType;	
	private LangType langType;
	private String configFilePath;
	private String targetFilePath;
	private List<DictModule> dictModuleList = new ArrayList<DictModule>();	
	private Map<String, DictModule> dictModuleCache = new HashMap<String, DictModule>();	//缓存，用于比较
	
	public GenDictParamSql(String projectPath, String configFilePath, String targetFilePath, DatabaseType databaseType, LangType langType) throws Exception
	{
		super(projectPath);
		this.targetFilePath = targetFilePath;
		this.databaseType = databaseType;
		this.langType = langType;
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
		if(!StringUtils.isEmpty(importProject))	//需要导入外部项目配置，约定导入配置与当前配置在同一目录，递归导入
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
		/*
		 * 叠加配置
		 * 1、若当前与import中的重名，则覆盖import中的数据，实现覆盖	
		 */
		for(DictModule module : config.getDictModuleList())
		{			
			//已存在则覆盖
			if(dictModuleCache.containsKey(module.getTableName()))
			{				
				for(int i=0;i<this.dictModuleList.size();i++)
				{
					DictModule row = this.dictModuleList.get(i);
					if(row.getTableName().equalsIgnoreCase(module.getTableName()))
					{
						this.dictModuleList.set(i, module);
					}
				}
			}
			else
			{
				this.dictModuleList.add(module);
			}
			dictModuleCache.put(module.getTableName(), module);
		}
		
	}
	
	public void gen() throws Exception
	{
		prepareData();
		String templateFileName = "db/"+databaseType+"/dictparam.ftl";
		this.genFile(templateFileName, super.getPropMap(), targetFilePath, "INIT_DICT_PARAM.SQL");
		
		templateFileName = "i18n/dictparam.ftl";
		this.genFile(templateFileName, super.getPropMap(), targetFilePath, "dictparam_"+langType.name()+".properties");
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
		//GenDictParamSql gen = new GenDictParamSql("classpath:conf/trannms-dict-config-en.xml", GenConstants.DATABASE_TYPE_MYSQL);
		GenDictParamSql gen = new GenDictParamSql("", "classpath:conf/invoicing-dict-config.xml", "D:\\", DatabaseType.mysql, LangType.en);
		gen.gen();
	}

}
