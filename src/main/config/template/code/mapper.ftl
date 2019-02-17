<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${domain.parentPackageName}.${domain.packageName}.mapper.${module.className}Mapper">

    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${domain.parentPackageName}.${domain.packageName}.po.${module.className}">
        <id column="ID" property="id" />
        <#assign ignoreColumns = ['id'] />
		<#list columnList as column>
			<#if !_StringUtils.contains(column.humpName, ignoreColumns) >	
		<result column="${column.name}" property="${column.humpName}" />
			</#if>
		</#list>
    </resultMap>

    <select id="selectByPage" resultMap="BaseResultMap">
        SELECT T.* FROM ${table.tableName} T WHERE 1=1
        <if test="params.tenantId != null">
            AND T.TENANT_ID = ${'#'}{params.tenantId}
        </if>
        <!--
        <if test="params.name != null and  ''!= params.name">
            AND T.NAME LIKE ${'#'}{params.name}
        </if>
        -->
    </select>

</mapper>
