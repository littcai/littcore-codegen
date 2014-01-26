<#list domainList as domain>
<#if !domain.isHide>   
domain.${domain.code}=<#if ""==domain.menuName!"">${_StringUtils.toUnicode(domain.title)}<#else>${_StringUtils.toUnicode(domain.menuName)}</#if>
 <#list domain.subList as sub>
 <#if !sub.isHide>   
  <#if sub.isDomain>
domain.${sub.code}=<#if ""==sub.menuName!"">${_StringUtils.toUnicode(sub.title)}<#else>${_StringUtils.toUnicode(sub.menuName)}</#if>
	<#list sub.subList as module>
		<#if module.isMenu && !module.isHide> 
module.${module.code}=<#if ""==module.menuName!"">${_StringUtils.toUnicode(module.title)}<#else>${_StringUtils.toUnicode(module.menuName)}</#if>		
		<#list module.funcList as func>
func.${func.code}=${_StringUtils.toUnicode(func.title)}
		</#list>  
		</#if>
	</#list>		
  <#else>  
module.${sub.code}=<#if ""==sub.menuName!"">${_StringUtils.toUnicode(sub.title)}<#else>${_StringUtils.toUnicode(sub.menuName)}</#if>
	<#list sub.funcList as func>
func.${func.code}=${_StringUtils.toUnicode(func.title)}
	</#list>
  </#if>
 </#if> 
 </#list>
</#if> 	
</#list>