package com.littcore.codegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;

import com.littcore.codegen.model.Gobal;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * .
 * 
 * <pre><b>Description：</b>
 *    
 * </pre>
 * 
 * <pre><b>Changelog：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2012-1-5
 * @version 1.0
 */
public class BaseGen {
	
	//项目配置定义根路径
	private String projectPath; 
	
	private Map<String, Object> propMap = new HashMap<String, Object>();
	
	/**
	 * The freemarker cfg.
	 */
	private Configuration freemarkerCfg;
	
	public BaseGen(String projectPath) throws Exception
	{
		this.projectPath = projectPath;
		this.initFreemaker();
	}	
	
	/**
	 * Inits the freemaker.
	 * 
	 * @throws TemplateException the template exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ConfigurationException the configuration exception
	 */
	private void initFreemaker() throws TemplateException,IOException,ConfigurationException
	{
		freemarkerCfg = new Configuration();
		freemarkerCfg.setClassForTemplateLoading(this.getClass(), "/");
		freemarkerCfg.setDirectoryForTemplateLoading(new File(projectPath, "template"));
		BeansWrapper wrapper = (BeansWrapper)BeansWrapper.BEANS_WRAPPER;   
		wrapper.setExposureLevel(BeansWrapper.EXPOSE_ALL);
		TemplateHashModel tempStatics = wrapper.getStaticModels();
		PropertiesConfiguration config = new PropertiesConfiguration("freemarkerstatic.properties");
		Iterator keyIterator = config.getKeys();
		while(keyIterator.hasNext())
		{
			String key = keyIterator.next().toString();
			freemarkerCfg.setSharedVariable(key, tempStatics.get(config.getString(key)));
		}		
		// 指定模版如何查看数据模型.
		freemarkerCfg.setObjectWrapper(new DefaultObjectWrapper());	
		
		Gobal gobal = new Gobal();
		propMap.put("gobal", gobal);
		
		propMap.put("static", ((BeansWrapper)freemarkerCfg.getObjectWrapper()).getStaticModels());  
		propMap.put("thread", useClass("java.lang.Thread"));  
		propMap.put("system", useClass("java.lang.System")); 
	}
	
	//拿到静态Class的Model  
	public TemplateModel useClass(String className) throws TemplateModelException  
	{  
	    BeansWrapper wrapper = (BeansWrapper) freemarkerCfg.getObjectWrapper();  
	    TemplateHashModel staticModels = wrapper.getStaticModels();  
	    return staticModels.get(className);  
	}
	
	//拿到目标对象的model  
	public TemplateModel useObjectModel(Object target) throws TemplateModelException  
	{  
	    ObjectWrapper wrapper = freemarkerCfg.getObjectWrapper();  
	    TemplateModel model = wrapper.wrap(target);  
	    return model;         
	}  
	  
	//拿到目标对象某个方法的Model    
	public TemplateModel useObjectMethod(Object target, String methodName) throws TemplateModelException  
	{     
	    TemplateHashModel model = (TemplateHashModel) useObjectModel(target);  
	    return model.get(methodName);         
	}  
	
	public void addProp(String key, Object value)
	{
		propMap.put(key, value);
	}
	
	public Object getProp(String key)
	{
		return propMap.get(key);
	}
	
	public void genFile(String templateFileName, Map propMap, String filePath, String fileName) throws IOException,TemplateException
	{
		Template template = freemarkerCfg.getTemplate(templateFileName,"UTF-8");
		template.setEncoding("UTF-8");
		File path = new File(filePath);
		if(!path.exists())
			path.mkdirs();
		File file = new File(filePath,fileName);
		//if(file.exists())
			//logger.info("目标文件已存在，不覆盖！");
		//else
		{
	        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
	        template.process(propMap,out);
	        out.flush();
	        IOUtils.closeQuietly(out);
		}
	}
	
	

	/**
	 * @return the propMap
	 */
	public Map<String, Object> getPropMap() {
		return propMap;
	}

	/**
	 * @return the projectPath
	 */
	public String getProjectPath() {
		return projectPath;
	}

	/**
	 * @param projectPath the projectPath to set
	 */
	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

}
