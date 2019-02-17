package ${domain.parentPackageName}.${domain.packageName}.service;

import javax.annotation.Resource;

import org.slf4j.Logger;     
import org.slf4j.LoggerFactory; 

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.littcore.cloud.boot.util.LoginUtils;
import com.littcore.cloud.core.uid.IdGenerator;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import ${domain.parentPackageName}.${domain.packageName}.po.${module.className};
import ${domain.parentPackageName}.${domain.packageName}.mapper.${module.className}Mapper;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Service
public class ${module.className}Service {

	/** Logger. */
    private static final Logger logger = LoggerFactory.getLogger(${module.className}Service.class);

    IdGenerator generator = new IdGenerator(1);
    
    @Resource
    private ${module.className}Mapper ${module.instanceName}Dao;

   	/**
	 * Save.
	 * @param ${module.instanceName} ${module.className}
	 * @return id
	 */
	public Long save(${module.className} ${module.instanceName})
	{
		${module.instanceName}.setId(generator.nextId());

        ${module.instanceName}.setTenantId(LoginUtils.getTenantId());

        <#list columnList as column>
		<#if "createBy"==column.humpName>
        ${module.instanceName}.setCreateBy(LoginUtils.getLoginOpId());
        ${module.instanceName}.setCreateDatetime(LocalDateTime.now());    
        </#if>
        <#if "updateBy"==column.humpName>
        ${module.instanceName}.setUpdateBy(${module.instanceName}.getCreateBy());
        ${module.instanceName}.setUpdateDatetime(${module.instanceName}.getCreateDatetime());
        </#if>
        </#list>

		${module.instanceName}Dao.insert(${module.instanceName});

		return ${module.instanceName}.getId();
	}
	
   	/**
	 * Update.
	 * @param ${module.instanceName} ${module.className}
	 */
	public void update(${module.className} ${module.instanceName}) 
	{
		//校验租户权限
		LoginUtils.validateTenant(${module.instanceName}.getTenantId());
		<#list columnList as column>
		<#if "updateBy"==column.humpName>
		${module.instanceName}.setUpdateBy(LoginUtils.getLoginOpId());
        ${module.instanceName}.setUpdateDatetime(LocalDateTime.now());
		</#if>
		</#list>
		${module.instanceName}Dao.updateById(${module.instanceName});
	}			
   
   	/**
	 * Delete by id.
	 * @param id id
	 */
	public void delete(Long id) 
	{
		${module.className} ${module.instanceName} = this.load(id);
		this.delete(${module.instanceName});
	}
	
	/**
	 * Batch delete by ids.
	 * @param ids ids
	 */
	public void deleteBatch(Long[] ids) 
	{
		if(ids!=null)
		{
			for (Long id : ids) {
				this.delete(id);
			}
		}
	}
	
	/**
	 * Delete by instance.
	 * @param ${module.instanceName} ${module.instanceName}
	 */
	public void delete(${module.className} ${module.instanceName}) 
	{
		//校验租户权限
		LoginUtils.validateTenant(${module.instanceName}.getTenantId());
	
		${module.instanceName}Dao.deleteById(${module.instanceName}.getId());
	}
	
	/**
	 * Load by id.
	 * @param id id
	 * @return ${module.className}
	 */
	public ${module.className} load(Long id) 
	{
		${module.className} ${module.instanceName} = ${module.instanceName}Dao.selectById(id);
		//校验租户权限
		LoginUtils.validateTenant(${module.instanceName}.getTenantId());
	
		return ${module.instanceName};
	}


	/**
	 * select by page.
	 * 
	 * @param params params
	 * @return IPage IPage
	 */
	public IPage<${module.className}> selectByPage(Page<${module.className}> page, Map<String, Object> params) {
         
        params.put("tenantId", LoginUtils.getTenantId());
        List<${module.className}> rsList = ${module.instanceName}Dao.selectByPage(page, params);
        page.setRecords(rsList);
        return page;
    }

}