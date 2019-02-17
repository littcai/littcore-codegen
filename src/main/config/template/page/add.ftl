<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jspf"%>
<%@ include file="/common/taglibs.jspf"%>
<html lang="en">
  <head>		
  </head>
  <body>   
  <form id="theform" action="save.json" method="post" class="form-horizontal">
    <fieldset>
      <legend><s:message code="common.ui.fieldset.base" /></legend>
      <#if table??>
       <#list table.columnList as column>
        <#if (column_index%2)==0>
      <div class="row-fluid">
        </#if>	
      <div class="span6">
        <div class="control-group">
	  <label class="control-label" for="${module.instanceName}.${column.humpName}"><s:message code="${module.instanceName}.${column.humpName}" /></label>
	  <div class="controls">
	    <input id="${module.instanceName}.${column.humpName}" name="${column.humpName}" placeholder="" type="text" />
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
      <button type="submit" class="btn btn-primary"><i class="icon-ok"></i> <s:message code="btn.save" /></button>
      <button type="button" class="btn" onclick="history.back();"><s:message code="btn.cancel" /></button>
    </div>					
  </form>				
  <!--page specific plugin scripts-->				
  <script type="text/javascript">
  $(document).ready(function(){	
			
    $('#theform').littFormSubmit({
      rules : {
        name : {
          required : true
        }
      },			
      success: function(reply){
        location.href = <h:returnUrl value="index.do"></h:returnUrl>;					
      }
    });
  });
  </script>	  
  </body>	
</html>