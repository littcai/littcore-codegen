<#list domainList as domain>
<#if domain.isMenu && !domain.isHide>
menu.${domain.fullCode}=<#if ""==domain.menuName!"">${_StringUtils.toUnicode(domain.title)}<#else>${_StringUtils.toUnicode(domain.menuName)}</#if>
menu.${domain.fullCode}.remark=${_StringUtils.toUnicode(domain.descr)}
 <#list domain.subList as sub>
  <#if sub.isMenu && !sub.isHide>   
  <#if sub.isDomain>
menu.${sub.fullCode}=<#if ""==sub.menuName!"">${_StringUtils.toUnicode(sub.title)}<#else>${_StringUtils.toUnicode(sub.menuName)}</#if>
menu.${sub.fullCode}.remark=${_StringUtils.toUnicode(sub.descr)}
	<#list sub.subList as module>
	<#if module.isMenu && !module.isHide> 
menu.${module.fullCode}=<#if ""==module.menuName!"">${_StringUtils.toUnicode(module.title)}<#else>${_StringUtils.toUnicode(module.menuName)}</#if>
menu.${module.fullCode}.remark=${_StringUtils.toUnicode(module.descr)}
	</#if>
	</#list>  	
  <#else>	
menu.${sub.fullCode}=<#if ""==sub.menuName!"">${_StringUtils.toUnicode(sub.title)}<#else>${_StringUtils.toUnicode(sub.menuName)}</#if> 
menu.${sub.fullCode}.remark=${_StringUtils.toUnicode(sub.descr)}
  </#if>
  </#if> 
 </#list>
</#if> 	
</#list>