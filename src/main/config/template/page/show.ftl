<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jspf"%>
<%@ include file="/common/taglibs.jspf"%>
<html lang="en">
  <head>		
  </head>
  <body>   
  <form id="theform" action="show.do" method="post" class="form-horizontal">
    <input type="hidden" name="id" value="${'$'}{${module.instanceName}.id}" />
    <fieldset>
      <legend><s:message code="${module.instanceName}.ui.fieldset.base" /></legend>
	<#if table??>
	<#list table.columnList as column>
	<#if (column_index%2)==0>
      <div class="row-fluid">
	</#if>	
        <div class="span6">
	  <div class="control-group">
	    <label class="control-label" for="${module.instanceName}.${column.humpName}"><s:message code="${module.instanceName}.${column.humpName}" /></label>
	    <div class="controls">
	      <input id="${module.instanceName}.${column.humpName}" name="${column.humpName}" type="text" value="<c:out values='${'$'}{${module.instanceName}.${column.humpName}}' />" readonly="readonly" />
	    </div>
	  </div>
	</div>								
	<#if ((column_index+1)%2)==0 || (column_index+1)==table.columnList?size>
      </div>
	</#if>
	</#list>
	</#if>	
    </fieldset>					
						
    <div class="form-actions">					
      <button type="button" class="btn" onclick="history.back();"><s:message code="btn.cancel" /></button>
    </div>					
				
  </form>		  
  </body>	
</html>