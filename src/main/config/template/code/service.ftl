package ${domain.parentPackageName}.${domain.packageName}.service;

import com.litt.core.dao.page.IPageList;
import com.litt.core.dao.ql.PageParam;

import ${domain.parentPackageName}.${domain.packageName}.po.${module.className};

/**
 * 
 * ${module.title} service interface.
 * <pre><b>Description：</b>
 *    ${module.descr}
 * </pre>
 * 
 * @author ${gobal.author}
 * @since ${gobal.date}
 * @version 1.0
 */
public interface I${module.className}Service
{ 

   	/**
	 * Save.
	 * @param ${module.instanceName} ${module.className}
	 * @return id
	 */
	public Integer save(${module.className} ${module.instanceName});
	
   	/**
	 * Update.
	 * @param ${module.instanceName} ${module.className}
	 */
	public void update(${module.className} ${module.instanceName});				
   
   	/**
	 * Delete by id.
	 * @param id id
	 */
	public void delete(Integer id);	
	
	/**
	 * Delete by instance.
	 * @param id id
	 */
	public void delete(${module.className} ${module.instanceName});
	
	/**
	 * Load by id.
	 * @param id id
	 * @return ${module.className}
	 */
	public ${module.className} load(Integer id);	
	
	/**
	 * list by page.
	 * 
	 * @param pageParam params
	 * @return IPageList IPageList
	 */
	public IPageList listPage(PageParam pageParam);	

}