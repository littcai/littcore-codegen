package com.nestwork.cloud.platform.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * ${module.title}
 * <pre><b>Description：</b>
 *    ${module.descr}
 * </pre>
 *
 * @author ${gobal.author}
 * @since ${gobal.date}
 * @version 1.0
 */
@TableName("${table.tableName}")
public class ${module.className} extends Model<${module.className}> {

    private static final long serialVersionUID = 1L;

    <#list columnList as column>
    <#if column.humpName == "id" > 
    ${'@'}TableId("ID")
    <#else> 
    ${'@'}TableField("${column.name}")
    </#if>
    private ${column.javaType} ${column.humpName};

    </#list>

<#list columnList as column>
    public ${column.javaType} get${_StringUtils.hump(column.name, true)}() {
        return ${column.humpName};
    }

    public void set${_StringUtils.hump(column.name, true)}(${column.javaType} ${column.humpName}) {
        this.${column.humpName} = ${column.humpName};
    }
    
</#list>    


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    
}
