BEGIN;
DELETE FROM DICT_PARAM_TYPE;
DELETE FROM DICT_PARAM;

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