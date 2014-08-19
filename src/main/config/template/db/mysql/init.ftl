BEGIN;
DELETE FROM MODULE;
DELETE FROM FUNC;
DELETE FROM PERMISSION;
<#list domainList as domain>
<#if !domain.isHide>   
-- domain:${domain.title}
SET @TEMP_DOMAIN_SEQ = 0;
INSERT INTO MODULE(MODULE_CODE,MODULE_NAME,PARENT_ID,IS_LEAF,STATUS) VALUES('${domain.fullCode}', '${domain.title}', 0, 0, ${domain.status});
SELECT @TEMP_DOMAIN_SEQ_${domain.fullCode}:=MAX(MODULE_ID) FROM MODULE;
 <#list domain.subList as sub>
 <#if !sub.isHide>   
  <#if sub.isDomain>
-- --sub domain:${sub.title}  
	INSERT INTO MODULE(MODULE_CODE,MODULE_NAME,PARENT_ID,IS_LEAF,STATUS) VALUES('${sub.fullCode}', '${sub.title}', @TEMP_DOMAIN_SEQ_${domain.fullCode}, 0, ${domain.status});
	SELECT @TEMP_DOMAIN_SEQ_${sub.fullCode}:=MAX(MODULE_ID) FROM MODULE;
	<#list sub.subList as module>
		<#if module.isMenu && !module.isHide> 
	-- --module:${module.title}
		INSERT INTO MODULE(MODULE_CODE,MODULE_NAME,PARENT_ID,IS_LEAF,STATUS) VALUES('${module.fullCode}', '${module.title}', @TEMP_DOMAIN_SEQ_${sub.fullCode}, 1, ${sub.status});
		SELECT @TEMP_MODULE_SEQ:=MAX(MODULE_ID) FROM MODULE;
		<#list module.funcList as func>
		INSERT INTO FUNC(FUNC_CODE,FUNC_NAME,FUNC_TYPE,MODULE_ID) VALUES('${func.code}', '${func.title}', 5, @TEMP_MODULE_SEQ);
		SELECT @TEMP_FUNC_SEQ := MAX(ID) FROM FUNC;
		INSERT INTO PERMISSION(PERMISSION_CODE,MODULE_ID,FUNC_ID) VALUES('${func.fullCode}', @TEMP_MODULE_SEQ, @TEMP_FUNC_SEQ);
		</#list>  
		</#if>
	</#list>		
  <#else>
-- --module:${sub.title}    
	INSERT INTO MODULE(MODULE_CODE,MODULE_NAME,PARENT_ID,IS_LEAF,STATUS) VALUES('${sub.fullCode}', '${sub.title}', @TEMP_DOMAIN_SEQ_${domain.fullCode}, 1, ${domain.status});
	SELECT @TEMP_MODULE_SEQ:=MAX(MODULE_ID) FROM MODULE;
	<#list sub.funcList as func>
	INSERT INTO FUNC(FUNC_CODE,FUNC_NAME,FUNC_TYPE,MODULE_ID) VALUES('${func.code}', '${func.title}', 5, @TEMP_MODULE_SEQ);
	SELECT @TEMP_FUNC_SEQ := MAX(ID) FROM FUNC;
	INSERT INTO PERMISSION(PERMISSION_CODE,MODULE_ID,FUNC_ID) VALUES('${func.fullCode}', @TEMP_MODULE_SEQ, @TEMP_FUNC_SEQ);
	</#list>
  </#if>
 </#if> 
 </#list>
</#if> 	
</#list>
COMMIT;