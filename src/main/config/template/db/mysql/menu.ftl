BEGIN;
DELETE FROM MENU;
<#list domainList as domain>
-- domain:${domain.title}
SET @TEMP_DOMAIN_SEQ = 0;
<#if domain.isMenu && !domain.isHide>
INSERT INTO MENU(MENU_CODE,MENU_NAME,PARENT_ID,IS_LEAF,MENU_URL,STATUS, REMARK) VALUES('${domain.code}','${domain.title}', 0, 0 , '', 1, '${domain.descr}');
SELECT @TEMP_DOMAIN_SEQ_${domain.code}:=MAX(MENU_ID) FROM MENU;
 <#list domain.subList as sub>
  <#if sub.isMenu && !sub.isHide>   
  <#if sub.isDomain>
-- --sub domain:${sub.title}
	INSERT INTO MENU(MENU_CODE,MENU_NAME,PARENT_ID,IS_LEAF,MENU_URL,STATUS, REMARK) VALUES('${sub.code}','${sub.menuName!sub.title}', @TEMP_DOMAIN_SEQ_${domain.code}, 0 , '', 1, '${sub.descr}');
	SELECT @TEMP_DOMAIN_SEQ_${sub.code}:=MAX(MENU_ID) FROM MENU;
	<#list sub.subList as module>
	<#if module.isMenu && !module.isHide> 
-- --module:${module.title}  
	INSERT INTO MENU(MENU_CODE,MENU_NAME,PARENT_ID,IS_LEAF,MENU_URL,STATUS, REMARK) VALUES('${module.code}','${module.menuName!module.title}', @TEMP_DOMAIN_SEQ_${sub.code}, 1, '/${sub.packageName}/${module.url}', 1, '${module.descr}');  
	</#if>
	</#list>  	
  <#else>	
  	<#if sub.isMenu && !sub.isHide> 
-- --module:${sub.title}     
	INSERT INTO MENU(MENU_CODE,MENU_NAME,PARENT_ID,IS_LEAF,MENU_URL,STATUS, REMARK) VALUES('${sub.code}','${sub.menuName!sub.title}', @TEMP_DOMAIN_SEQ_${domain.code}, 1, '/${domain.packageName}/${sub.url}', 1, '${sub.descr}');
  	</#if>
  </#if>
  </#if> 
 </#list>
</#if> 	
</#list>
COMMIT;