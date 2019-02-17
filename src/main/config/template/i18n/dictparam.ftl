<#list dictModuleList as dictModule>
<#list dictModule.dictParamTypeList as dictParamType>
dictparam.${dictParamType.dictType}=${_StringUtils.toUnicode(dictParamType.dictTypeName)}
		<#list dictParamType.dictParamList as dictParam>
dictparam.${dictParamType.dictType}.${dictParam.dictValue}=${_StringUtils.toUnicode(dictParam.dictContent)}
		</#list>
	</#list>
</#list>