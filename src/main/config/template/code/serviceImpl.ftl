package ${domain.parentPackageName}.${domain.packageName}.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;     
import org.slf4j.LoggerFactory; 

import com.litt.core.dao.page.IPageList;
import com.litt.core.dao.ql.PageParam;
import com.litt.core.exception.BusiException;
import com.litt.core.service.BaseService;

import ${domain.parentPackageName}.${domain.packageName}.po.${module.className};
import ${domain.parentPackageName}.${domain.packageName}.dao.${module.className}Dao;

/**
 * 
 * ${module.title} service impl.
 * <pre><b>Description：</b>
 *    ${module.descr}
 * </pre>
 * 
 * @author ${gobal.author}
 * @since ${gobal.date}
 * @version 1.0
 */
public class ${module.className}ServiceImpl extends BaseService implements I${module.className}Service
{ 
	/** Logger. */
    private static final Logger logger = LoggerFactory.getLogger(${module.className}ServiceImpl.class);
    
    @Resource
    private ${module.className}Dao ${module.instanceName}Dao;

   	/**
	 * Save.
	 * @param ${module.instanceName} ${module.className}
	 * @return id
	 */
	public Integer save(${module.className} ${module.instanceName})
	{
		return ${module.instanceName}Dao.save(${module.instanceName});
	}
	
   	/**
	 * Update.
	 * @param ${module.instanceName} ${module.className}
	 */
	public void update(${module.className} ${module.instanceName})
	{
		${module.instanceName}Dao.update(${module.instanceName});
	}			
   
   	/**
	 * Delete by id.
	 * @param id id
	 */
	public void delete(Integer id)
	{
		${module.instanceName}Dao.delete(${module.className}.class, "id", id);
	}
	
	/**
	 * Delete by instance.
	 * @param id id
	 */
	public void delete(${module.className} ${module.instanceName})
	{
		${module.instanceName}Dao.delete(${module.instanceName});
	}
	
	/**
	 * Load by id.
	 * @param id id
	 * @return ${module.className}
	 */
	public ${module.className} load(Integer id)
	{
		return ${module.instanceName}Dao.load(${module.className}.class, id);
	}
	
	/**
	 * list by page.
	 * 
	 * @param pageParam params
	 * @return IPageList IPageList
	 */
	public IPageList listPage(PageParam pageParam)
	{
		String listHql = "select obj from ${module.className} obj"
			+ "-- and obj.name={name}"
			;	
		return ${module.instanceName}Dao.listPage(listHql, pageParam);
	}
}