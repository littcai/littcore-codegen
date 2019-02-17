<#list domainList as domain>
<#if !domain.isHide>
<code>${domain.fullCode}</code>
 <#list domain.subList as sub>
  <#if !sub.isHide>   
  <#if sub.isDomain>
  	<!-- ${sub.fullCode} -->
  	<code>${sub.fullCode}</code>
	<#list sub.subList as module>
	<#if !module.isHide> 
		<!-- ${module.fullCode} -->
		<code>${module.fullCode}</code>
		<#list module.funcList as func>
			<code>${func.fullCode}</code>
		</#list>
	</#if>
	</#list>  	
  <#else>	
  	<!-- ${sub.fullCode} -->
  	<code>${sub.fullCode}</code>
  	<#list sub.funcList as func>
		<code>${func.fullCode}</code>
	</#list>
  </#if>
  </#if> 
 </#list>
</#if> 	
</#list>