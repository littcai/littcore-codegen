<#list domainList as domain>
	<#if domain.isMenu && !domain.isHide>
		<#list domain.subList as sub>
		  <#if sub.isMenu && !sub.isHide>   
		  <#if sub.isDomain>
			  <!-- ignore -->
			  <#list sub.subList as module>
				<#if module.isMenu && !module.isHide && module.name??> 
				public static final String ${module.name} = "${module.code}";
				</#if>
			  </#list>  	
          <#else>	
          	   <#if sub.name??>
				public static final String ${sub.name} = "${sub.code}";
				</#if>
		</#if>
  </#if> 
 </#list>
</#if> 
</#list>