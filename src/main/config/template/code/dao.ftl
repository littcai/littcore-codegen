package ${domain.parentPackageName}.${domain.packageName}.mapper;

import org.slf4j.Logger;     
import org.slf4j.LoggerFactory;  


import ${domain.parentPackageName}.${domain.packageName}.po.${module.className};

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 
 * ${module.title} Dao.
 * <pre><b>Description：</b>
 *    ${module.descr}
 * </pre>
 * 
 * @author ${gobal.author}
 * @since ${gobal.date}
 * @version 1.0
 */
public interface ${module.className}Mapper extends BaseMapper<${module.className}>
{  
    
    /**
     * 分页查询.
     * @param page
     * @param params
     * @return
     */
	List<${module.className}> selectByPage(Page<${module.className}> page, @Param("params") Map params);

	@Select("SELECT * FROM ${table.tableName} WHERE TENANT_ID = ${'#'}{tenantId}")
    List<${module.className}> selectAll(@Param("tenantId") Long tenantId);

    @Select("SELECT COUNT(*) FROM ${table.tableName} WHERE TENANT_ID=${'#'}{tenantId} AND CODE=${'#'}{code} AND (ID>${'#'}{id} OR ID<${'#'}{id})")
    Integer selectCountByCode(@Param("tenantId") long tenantId, @Param("code")long code, @Param("id")long id);

    @Select("SELECT COUNT(*) FROM ${table.tableName} WHERE TENANT_ID=${'#'}{tenantId} AND NAME=${'#'}{name} AND (ID>${'#'}{id} OR ID<${'#'}{id})")
    Integer selectCountByName(@Param("tenantId") long tenantId, @Param("name")String name, @Param("id")long id);

}