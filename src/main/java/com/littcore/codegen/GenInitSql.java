package com.littcore.codegen;

import com.littcore.codegen.common.GenConstants.DatabaseType;
import com.littcore.codegen.common.GenConstants.LangType;

/** 
 * 
 * 生成模块功能点权限的初始化SQL语句.
 * 
 * <pre><b>描述：</b>
 *    生成模块功能点权限的初始化SQL语句 
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
public class GenInitSql extends BaseModuleGen
{
	private DatabaseType databaseType;
	private LangType langType;
	
	private String targetFilePath;	
	
	public GenInitSql(String projectPath, String configFilePath, String targetFilePath, DatabaseType databaseType, LangType langType) throws Exception
	{
		super(projectPath, configFilePath);		
		this.targetFilePath = targetFilePath;
		this.databaseType = databaseType;
		this.langType = langType;
		prepareData();
	}
	
	public void prepareData()throws Exception
	{		
		addProp("domainList", super.getDomainList());
	}


	/**
	 * 生成模块、功能点、权限
	 * @throws Exception
	 */
	public void gen() throws Exception
	{
		String templateFileName = "db/"+databaseType+"/init.ftl";
		this.genFile(templateFileName, super.getPropMap(), targetFilePath, "INIT_MODULE_FUNC_PERMISSION.SQL");
		
		templateFileName = "db/"+databaseType+"/menu.ftl";
		this.genFile(templateFileName, super.getPropMap(), targetFilePath, "INIT_MENU.SQL");
		
		templateFileName = "i18n/menu.ftl";
		this.genFile(templateFileName, super.getPropMap(), targetFilePath, "menu_"+langType.name()+".properties");
		
		templateFileName = "i18n/module.ftl";
		this.genFile(templateFileName, super.getPropMap(), targetFilePath, "module_"+langType.name()+".properties");

		templateFileName = "code/tenant-type.ftl";
		this.genFile(templateFileName, super.getPropMap(), targetFilePath, "tenant-type.txt");
	}
	

	

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		//GenInitSql gen = new GenInitSql("classpath:crm_module_config.xml", GenConstants.DATABASE_TYPE_ORACLE);
		//GenInitSql gen = new GenInitSql("classpath:crm_module_config.xml", GenConstants.DATABASE_TYPE_MYSQL5);
		//GenInitSql gen = new GenInitSql("classpath:project_module_config.xml", GenConstants.DATABASE_TYPE_SQLSERVER2000);
		//GenInitSql gen = new GenInitSql("classpath:cidp2.0_module_config.xml", GenConstants.DATABASE_TYPE_ORACLE);
		//GenInitSql gen = new GenInitSql("classpath:cidp2.0_module_config.xml", GenConstants.DATABASE_TYPE_MYSQL5);
		//GenInitSql gen = new GenInitSql("classpath:cidp2.0_module_config.xml", GenConstants.DATABASE_TYPE_SQLSERVER2000);
		//GenInitSql gen = new GenInitSql("classpath:littcrm_module_config.xml", GenConstants.DATABASE_TYPE_ORACLE);
		GenInitSql gen = new GenInitSql("", "classpath:conf/invoicing-module-config.xml", "D:\\", DatabaseType.mysql, LangType.en);
		//GenInitSql gen = new GenInitSql("classpath:conf/warehouse-module-config-en.xml", GenConstants.DATABASE_TYPE_MYSQL);
		
		//GenInitSql gen = new GenInitSql("classpath:conf/trannms-2.0-module-config-en.xml", GenConstants.DATABASE_TYPE_MYSQL);
		//GenInitSql gen = new GenInitSql("classpath:conf/trannms-1.0-module-config-en.xml", GenConstants.DATABASE_TYPE_MYSQL);
		gen.gen();
	}
}
