package com.litt.core.codegen;

import java.util.List;

import com.litt.core.codegen.common.GenConstants;
import com.litt.core.codegen.model.DictConfig;
import com.litt.core.codegen.model.DictModule;
import com.litt.core.codegen.model.DictParamType;
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
public class GenDictParamCode extends BaseGen
{	
	private String configFilePath;
	private DictConfig config;	
	
	public GenDictParamCode(String projectPath, String configFilePath) throws Exception
	{
		super(projectPath);
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
		String templateFileName = "code/dictparam.ftl";
		
		DictModule[] dictModules = config.getDictModuleList();
		for (DictModule dictModule : dictModules) {
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
