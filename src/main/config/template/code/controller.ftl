package ${domain.parentPackageName}.${domain.packageName}.web;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.littcore.cloud.core.exception.BusiException;
import com.littcore.cloud.core.module.annotation.Func;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.littcore.cloud.boot.system.vo.LoginUserVo;
import com.littcore.cloud.boot.util.LoginUtils;
import ${domain.parentPackageName}.${domain.packageName}.po.${module.className};
import ${domain.parentPackageName}.${domain.packageName}.service.${module.className}Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * ${module.title} controller.
 * <pre><b>Description：</b>
 *    ${module.descr}
 * </pre>
 * 
 * @author ${gobal.author}
 * @since ${gobal.date}
 * @version 1.0
 */
@RestController
@RequestMapping("/mgr/${domain.packageName}/${module.instanceName}")
public class ${module.className}MgrController
{
	private final static Logger logger = LoggerFactory.getLogger(${module.className}MgrController.class);

	@Resource
	private ${module.className}Service ${module.instanceName}Service;


	@Func(funcCode="query", moduleCode="${module.fullCode}", enableLog=false) 
	@ApiOperation(value = "分页查询")
    @GetMapping("/selectByPage")
    public IPage<${module.className}> selectByPage(
            @ApiParam(name = "name", value = "名称") @RequestParam(required = false) String name
            , @ApiParam(name = "pageSize", value = "默认10, 分页信息每页记录数") @RequestParam(required = false, defaultValue = "10") Integer pageSize
            , @ApiParam(name = "pageNumber", value = "默认1, 分页信息起始页") @RequestParam(required = false, defaultValue = "1") Integer pageNumber) {
        Page page = new Page(pageNumber, pageSize);
        Map<String, Object> params = new HashMap<>();
        //params.put("name", name);
 

        IPage<${module.className}> result = ${module.instanceName}Service.selectByPage(page, params);
        return result;
    }

	@ApiOperation("添加")
    @RequestMapping(value = "/save", method = {RequestMethod.GET,RequestMethod.POST})
    public void save(
    	<#assign ignoreColumns = ['id', 'tenantId', "createBy", "createDatetime", 'updateBy', 'updateDatetime', 'deleted'] />
		<#list columnList as column>
			<#if !_StringUtils.contains(column.humpName, ignoreColumns) >	
		 <#if column_index gt 2>,</#if>	@ApiParam(value = "${column.comment}") @RequestParam${column.nullable?string("(required = false)","")} ${column.javaType} ${column.humpName}
		 </#if>
		</#list>
	) {

		long tenantId = LoginUtils.getTenantId();
		long createBy = LoginUtils.getLoginOpId();
		LocalDateTime createDatetime = LocalDateTime.now();
		${module.className} ${module.instanceName} = new ${module.className}();
	<#assign ignoreColumns = ['id', 'updateBy', 'updateDatetime', 'deleted'] />
	<#list columnList as column>
		<#if !_StringUtils.contains(column.humpName, ignoreColumns) >	
		${module.instanceName}.set${_StringUtils.hump(column.name, true)}(${column.humpName});
		</#if>
	</#list>

        ${module.instanceName}Service.save(${module.instanceName});

    }
	

<#assign ignoreColumns = ["id", "tenantId", "createBy", "createDatetime", 'updateBy', 'updateDatetime', 'deleted'] />
    @Func(funcCode="edit", moduleCode="${module.fullCode}")
    @ApiOperation("修改")
    @RequestMapping(value = "/update", method = {RequestMethod.GET,RequestMethod.POST})
    public void update(@ApiParam(value = "ID") @RequestParam Long id    	
        <#list columnList as column>
		<#if !_StringUtils.contains(column.humpName, ignoreColumns) >, @ApiParam(value = "${column.comment}") @RequestParam${column.nullable?string("(required = false)","")} ${column.javaType} ${column.humpName}
		</#if>
		</#list>
	) {

		long tenantId = LoginUtils.getTenantId();
		long updateBy = LoginUtils.getLoginOpId();
		LocalDateTime updateDatetime = LocalDateTime.now();
		${module.className} ${module.instanceName} = ${module.instanceName}Service.load(id);
	<#assign ignoreColumns = ['id', 'tenantId', 'createBy', 'createDatetime', 'deleted'] />
	<#list columnList as column>
		<#if !_StringUtils.contains(column.humpName, ignoreColumns) >
		${module.instanceName}.set${_StringUtils.hump(column.name, true)}(${column.humpName});
		</#if>
	</#list>

        ${module.instanceName}Service.update(${module.instanceName});

    }

    /**
	 * Delete.
	 * @param id id
	 * @throws Exception 
	 */
	@Func(funcCode="delete",moduleCode="${module.fullCode}")
	@ApiOperation("删除")
	@RequestMapping(value = "/delete", method = {RequestMethod.GET,RequestMethod.POST})
	public void delete(@ApiParam(value = "ID") @RequestParam Long id) 
	{
		${module.instanceName}Service.delete(id);
	}

	@Func(funcCode="delete", moduleCode="${module.fullCode}")
    @ApiOperation("批量删除")
    @RequestMapping(value = "/deleteBatch", method = {RequestMethod.GET,RequestMethod.POST})
    public void deleteBatch(@ApiParam(value = "IDs") @RequestParam(name="ids") Long[] ids){
        if (ids == null || ids.length==0) {
            throw new BusiException("请至少选择一项！");
        }

        ${module.instanceName}Service.deleteBatch(ids);
    }
	

    @ApiOperation(value = "根据主键查询")
    @RequestMapping(value = "/load", method = {RequestMethod.POST, RequestMethod.GET})
    public ${module.className} load(@ApiParam(value = "id") @RequestParam long id){
        return ${module.instanceName}Service.load(id);
    }
}
