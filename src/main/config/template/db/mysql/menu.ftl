BEGIN;
SET @TENANT_TYPE=1;
DELETE FROM MENU WHERE TENANT_TYPE=@TENANT_TYPE;
<#list domainList as domain>
-- domain:${domain.title}
SET @TEMP_DOMAIN_SEQ = 0;
<#if domain.isMenu && !domain.isHide>
INSERT INTO MENU(TENANT_TYPE, MENU_CODE,MENU_NAME,PARENT_ID,IS_LEAF,MENU_URL,STATUS,POSITION,ICON_URL,REMARK) VALUES(@TENANT_TYPE, '${domain.fullCode}','${domain.title}', 0, 0 , '', 1, ${domain.position},'${domain.icon!}','${domain.descr}');
SELECT @TEMP_DOMAIN_SEQ_${domain.fullCode}:=MAX(MENU_ID) FROM MENU;
 <#list domain.subList as sub>
  <#if sub.isMenu && !sub.isHide>   
  <#if sub.isDomain>
-- --sub domain:${sub.title}
	INSERT INTO MENU(TENANT_TYPE, MENU_CODE,MENU_NAME,PARENT_ID,IS_LEAF,MENU_URL,STATUS,POSITION,ICON_URL, REMARK) VALUES(@TENANT_TYPE, '${sub.fullCode}','${sub.menuName!sub.title}', @TEMP_DOMAIN_SEQ_${domain.fullCode}, 0 , '', 1,${sub.position},'${sub.icon!}', '${sub.descr}');
	SELECT @TEMP_DOMAIN_SEQ_${sub.fullCode}:=MAX(MENU_ID) FROM MENU;
	<#list sub.subList as module>
	<#if module.isMenu && !module.isHide> 
-- --module:${module.title}  
	INSERT INTO MENU(TENANT_TYPE, MENU_CODE,MENU_NAME,PARENT_ID,IS_LEAF,MENU_URL,STATUS,POSITION,ICON_URL, REMARK) VALUES(@TENANT_TYPE, '${module.fullCode}','${module.menuName!module.title}', @TEMP_DOMAIN_SEQ_${sub.fullCode}, 1, '/${sub.packageName}/${module.url}', 1,${module.position},'${domain.module!}', '${module.descr}');  
	</#if>
	</#list>  	
  <#else>	
  	<#if sub.isMenu && !sub.isHide> 
-- --module:${sub.title}     
	INSERT INTO MENU(TENANT_TYPE, MENU_CODE,MENU_NAME,PARENT_ID,IS_LEAF,MENU_URL,STATUS,POSITION,ICON_URL, REMARK) VALUES(@TENANT_TYPE, '${sub.fullCode}','${sub.menuName!sub.title}', @TEMP_DOMAIN_SEQ_${domain.fullCode}, 1, '/${domain.packageName}/${sub.url}', 1,${sub.position},'${sub.icon!}', '${sub.descr}');
  	</#if>
  </#if>
  </#if> 
 </#list>
</#if> 	
</#list>
COMMIT;