package com.litt.core.codegen;

import com.litt.core.codegen.common.GenConstants.DatabaseType;
import com.litt.core.codegen.common.GenConstants.LangType;
import com.litt.core.codegen.model.DictConfig;
import com.litt.core.codegen.util.ConfigUtils;


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
	private DictConfig config;	
	
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
		this.config = ConfigUtils.loadByCastor(DictConfig.class, "classpath:dict-conf-mapping.xml", configFilePath);
		addProp("dictModuleList", config.getDictModuleList());
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
