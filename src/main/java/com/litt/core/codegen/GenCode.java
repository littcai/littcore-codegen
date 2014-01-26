package com.litt.core.codegen;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litt.core.codegen.common.DBManager;
import com.litt.core.codegen.model.Domain;
import com.litt.core.codegen.model.Module;
import com.litt.core.codegen.model.db.TableModel;
import com.litt.core.common.Utility;
import com.litt.core.util.ValidateUtils;

import freemarker.template.TemplateException;

/**
 * The Class GenCode.
 */
public class GenCode extends BaseModuleGen
{
    /** 日志工具. */
    private static final Logger logger = LoggerFactory.getLogger(GenCode.class);	
	
	/**
	 * 目标文件绝对路径.
	 */
	private String targetFilePath;	
	
	private DBManager dbManager = null;
	
	/**
	 * Instantiates a new gen code.
	 * 
	 * @param configFilePath the config file path
	 * 
	 * @throws Exception the exception
	 */
	public GenCode(String projectPath, String configFilePath) throws Exception
	{
		super(projectPath, configFilePath);		
		prepareData();
		
		String jdbcConfigFile = new File(new File(super.getModuleFilePath()).getParent(), "jdbc.properties").getAbsolutePath();
		dbManager = DBManager.getInstance(jdbcConfigFile);
	}
	
	public void prepareData()throws Exception
	{		
		addProp("domainList", super.getDomainList());
	}	
	
	/**
	 * 生成模块、功能点、权限
	 * @throws Exception
	 */
	public void genSpringConf() throws Exception
	{
		//生成实现
//		String implFilePath = this.targetFilePath;
//		String implFileName = "spring.xml";
//		this.genFile("code/spring-conf.ftl", super.getPropMap(), implFilePath, implFileName);
	}	
		
	
	/**
	 * 生成DAO类.
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws TemplateException the template exception
	 */
	private void genDao() throws IOException,TemplateException
	{		
		Domain domainProp = (Domain)super.getProp("domain");
		Module moduleProp = (Module)super.getProp("module");
		
		//生成实现
		String implFilePath = this.targetFilePath +  Utility.SEPARATOR + domainProp.getDomainPath() + Utility.SEPARATOR + "dao";
		String implFileName = moduleProp.getClassName()+"Dao.java";
		this.genFile("code/dao.ftl", super.getPropMap(), implFilePath, implFileName);
	}
	
	/**
	 * 生成Service类.
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws TemplateException the template exception
	 */
	private void genService() throws IOException,TemplateException
	{		
		Domain domainProp = (Domain)super.getProp("domain");
		Module moduleProp = (Module)super.getProp("module");
		//生成接口		
		String interfaceFilePath = this.targetFilePath +  Utility.SEPARATOR + domainProp.getDomainPath() + Utility.SEPARATOR + "service";
		String interfaceFileName = "I"+moduleProp.getClassName()+"Service.java";
		this.genFile("code/service.ftl", super.getPropMap(), interfaceFilePath, interfaceFileName);
		
		//生成实现
		String implFilePath = this.targetFilePath +  Utility.SEPARATOR + domainProp.getDomainPath() + Utility.SEPARATOR + "service" + Utility.SEPARATOR + "impl";
		String implFileName = moduleProp.getClassName()+"ServiceImpl.java";
		this.genFile("code/serviceImpl.ftl", super.getPropMap(), implFilePath, implFileName);
	}
	
	/**
	 * 生成Controller类.
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws TemplateException the template exception
	 */
	private void genController() throws IOException,TemplateException
	{
		Domain domainProp = (Domain)this.getProp("domain");
		Module moduleProp = (Module)this.getProp("module");
		//生成实现
		String implFilePath = this.targetFilePath +  Utility.SEPARATOR + domainProp.getDomainPath() + Utility.SEPARATOR + "web";
		String implFileName = moduleProp.getClassName()+"Controller.java";
		this.genFile("code/controller.ftl",super.getPropMap(), implFilePath, implFileName);
	}	
		
	/**
	 * 生成页面.
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws TemplateException the template exception
	 */
	private void genPage() throws IOException,TemplateException, SQLException
	{
		Domain domainProp = (Domain)this.getProp("domain");
		Module moduleProp = (Module)this.getProp("module");	
		
		String tableName = moduleProp.getTableName();
		if(!ValidateUtils.isEmpty(tableName))
		{			
			TableModel table = dbManager.getTableMetadata(tableName);			
			super.addProp("table", table);
		}
		//生成实现
		String filePath = this.targetFilePath +  Utility.SEPARATOR + "WebRoot" + Utility.SEPARATOR + domainProp.getPackageName() + Utility.SEPARATOR + moduleProp.getInstanceName();
		String fileName = "index.jsp";
		this.genFile("page/index.ftl", super.getPropMap(), filePath, fileName);
		fileName = "add.jsp";
		this.genFile("page/add.ftl", super.getPropMap(), filePath, fileName);
		fileName = "edit.jsp";
		this.genFile("page/edit.ftl", super.getPropMap(), filePath, fileName);
		fileName = "show.jsp";
		this.genFile("page/show.ftl", super.getPropMap(), filePath, fileName);
	}	
	
	/**
	 * 由配置文件生成.
	 * 
	 * @throws Exception the exception
	 */
	public void gen() throws Exception
	{	
		genSpringConf();
		
		List<Domain> domainList = super.getDomainList();
		for (Domain domain : domainList) {			
			genByDomain(domain);
		}
		
		dbManager.destroyDataSource();
	}

	/**
	 * @param domain
	 */
	public void genByDomain(Domain domain) throws Exception  {
		logger.info("Gen domain:{}", new Object[]{domain.getTitle()});
		List subList = domain.getSubList();
		for (Object obj : subList) {
			if(obj instanceof Domain)
			{
				Domain subDomain = (Domain)obj;
				genByDomain(subDomain);				
			}
			else {
				Module module = (Module)obj;
				logger.info("Gen module:{}", new Object[]{module.getTitle()});
				
				super.addProp("domain", domain);
				super.addProp("module", module);
				genDao();
				genService();
				genController();
				genPage();
			}
		}
	}	

	/**
	 * The main method.
	 * 
	 * @param args the args
	 * 
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception
	{
		GenCode genCode = new GenCode("D:\\CODEGEN\\saap", "D:\\CODEGEN\\saap\\saap-module-config-en.xml");
		//GenCode genCode = new GenCode("teamwork");
		genCode.setTargetFilePath("D:\\CODEGEN\\saap");
		genCode.gen();		
		
		
		//custom gen
//		Domain domain = new Domain();
//		domain.setCode("");
//		domain.setTitle("Application");
//		domain.setPackageName("network");
//		domain.setParentPackageName("com.transoft.nms");
//		
//		Module module = new Module();
//		module.setCode("");
//		module.setTitle("App Process");
//		module.setDescr("App Process");
//		module.setModelName("AppProcess");			
//		domain.addSub(module);
		
		//custom gen
//		Domain domain = new Domain();
//		domain.setCode("");
//		domain.setTitle("Notification");
//		domain.setPackageName("network");
//		domain.setParentPackageName("com.transoft.nms");
//		
//		Module module = new Module();
//		module.setCode("");
//		module.setTitle("NotifyUser");
//		module.setDescr("NotifyUser");
//		module.setModelName("NotifyUser");			
//		domain.addSub(module);
//		module = new Module();
//		module.setCode("");
//		module.setTitle("NotifyEvent");
//		module.setDescr("NotifyEvent");
//		module.setModelName("NotifyEvent");			
//		domain.addSub(module);
//		module = new Module();
//		module.setCode("");
//		module.setTitle("NotifyTime");
//		module.setDescr("NotifyTime");
//		module.setModelName("NotifyTime");			
//		domain.addSub(module);
//		module = new Module();
//		module.setCode("");
//		module.setTitle("NotifyObject");
//		module.setDescr("NotifyObject");
//		module.setModelName("NotifyObject");			
//		domain.addSub(module);
//		
//		module = new Module();
//		module.setCode("");
//		module.setTitle("NotifyProfile");
//		module.setDescr("NotifyProfile");
//		module.setModelName("NotifyProfile");			
//		domain.addSub(module);
//		
//		genCode.genByDomain(domain);
	}

	/**
	 * Gets the target file path.
	 * 
	 * @return the targetFilePath
	 */
	public String getTargetFilePath()
	{
		return targetFilePath;
	}

	/**
	 * Sets the target file path.
	 * 
	 * @param targetFilePath the targetFilePath to set
	 */
	public void setTargetFilePath(String targetFilePath)
	{
		this.targetFilePath = targetFilePath;
	}

}
