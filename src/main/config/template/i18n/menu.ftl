<#list domainList as domain>
<#if domain.isMenu && !domain.isHide>
menu.${domain.code}=<#if ""==domain.menuName!"">${_StringUtils.toUnicode(domain.title)}<#else>${_StringUtils.toUnicode(domain.menuName)}</#if>
menu.${domain.code}.remark=${_StringUtils.toUnicode(domain.descr)}
 <#list domain.subList as sub>
  <#if sub.isMenu && !sub.isHide>   
  <#if sub.isDomain>
menu.${sub.code}=<#if ""==sub.menuName!"">${_StringUtils.toUnicode(sub.title)}<#else>${_StringUtils.toUnicode(sub.menuName)}</#if>
menu.${sub.code}.remark=${_StringUtils.toUnicode(sub.descr)}
	<#list sub.subList as module>
	<#if module.isMenu && !module.isHide> 
menu.${module.code}=<#if ""==module.menuName!"">${_StringUtils.toUnicode(module.title)}<#else>${_StringUtils.toUnicode(module.menuName)}</#if>
menu.${module.code}.remark=${_StringUtils.toUnicode(module.descr)}
	</#if>
	</#list>  	
  <#else>	
menu.${sub.code}=<#if ""==sub.menuName!"">${_StringUtils.toUnicode(sub.title)}<#else>${_StringUtils.toUnicode(sub.menuName)}</#if> 
menu.${sub.code}.remark=${_StringUtils.toUnicode(sub.descr)}
  </#if>
  </#if> 
 </#list>
</#if> 	
</#list>