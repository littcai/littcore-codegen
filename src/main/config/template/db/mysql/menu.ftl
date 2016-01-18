BEGIN;
TRUNCATE TABLE MENU;
<#list domainList as domain>
-- domain:${domain.title}
SET @TEMP_DOMAIN_SEQ = 0;
<#if domain.isMenu && !domain.isHide>
INSERT INTO MENU(MENU_CODE,MENU_NAME,PARENT_ID,IS_LEAF,MENU_URL,STATUS,POSITION,REMARK) VALUES('${domain.fullCode}','${domain.title}', 0, 0 , '', 1, ${domain.position},'${domain.descr}');
SELECT @TEMP_DOMAIN_SEQ_${domain.fullCode}:=MAX(MENU_ID) FROM MENU;
 <#list domain.subList as sub>
  <#if sub.isMenu && !sub.isHide>   
  <#if sub.isDomain>
-- --sub domain:${sub.title}
	INSERT INTO MENU(MENU_CODE,MENU_NAME,PARENT_ID,IS_LEAF,MENU_URL,STATUS,POSITION, REMARK) VALUES('${sub.fullCode}','${sub.menuName!sub.title}', @TEMP_DOMAIN_SEQ_${domain.fullCode}, 0 , '', 1,${sub.position}, '${sub.descr}');
	SELECT @TEMP_DOMAIN_SEQ_${sub.fullCode}:=MAX(MENU_ID) FROM MENU;
	<#list sub.subList as module>
	<#if module.isMenu && !module.isHide> 
-- --module:${module.title}  
	INSERT INTO MENU(MENU_CODE,MENU_NAME,PARENT_ID,IS_LEAF,MENU_URL,STATUS,POSITION, REMARK) VALUES('${module.fullCode}','${module.menuName!module.title}', @TEMP_DOMAIN_SEQ_${sub.fullCode}, 1, '/${sub.packageName}/${module.url}', 1,${module.position}, '${module.descr}');  
	</#if>
	</#list>  	
  <#else>	
  	<#if sub.isMenu && !sub.isHide> 
-- --module:${sub.title}     
	INSERT INTO MENU(MENU_CODE,MENU_NAME,PARENT_ID,IS_LEAF,MENU_URL,STATUS,POSITION, REMARK) VALUES('${sub.fullCode}','${sub.menuName!sub.title}', @TEMP_DOMAIN_SEQ_${domain.fullCode}, 1, '/${domain.packageName}/${sub.url}', 1,${sub.position}, '${sub.descr}');
  	</#if>
  </#if>
  </#if> 
 </#list>
</#if> 	
</#list>
COMMIT;