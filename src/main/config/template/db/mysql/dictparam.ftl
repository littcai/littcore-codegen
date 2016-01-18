BEGIN;
SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE DICT_PARAM_TYPE;
TRUNCATE TABLE DICT_PARAM;
SET FOREIGN_KEY_CHECKS=1;

<#list dictModuleList as dictModule>
-- module:${dictModule.tableName}
	<#list dictModule.dictParamTypeList as dictParamType>
	INSERT INTO DICT_PARAM_TYPE(DICT_TYPE,DICT_TYPE_NAME,ALTER_MODE) VALUES('${dictParamType.dictType}','${dictParamType.dictTypeName}', ${dictParamType.alterMode});
		<#list dictParamType.dictParamList as dictParam>
		INSERT INTO DICT_PARAM(DICT_TYPE,DICT_VALUE,DICT_CONTENT) VALUES('${dictParamType.dictType}','${dictParam.dictValue}','${dictParam.dictContent}');
		</#list>
	</#list>
</#list>
COMMIT;