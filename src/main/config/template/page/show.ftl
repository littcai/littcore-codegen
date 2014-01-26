<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.inc"%>
<%@ include file="/common/operator_login.inc"%>
<%@ include file="/common/taglibs.inc"%>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
	<title>查看${module.title }</title>
  </head>
  <body class="ui-main">		 
	<form name="theform" method="post" action="show" class="ui-form">			
		<fieldset>	
			<legend>编辑</legend>					
			<table class="ui-form-content">	
				<#list columnList as column>
				<tr>					
					<th>${column.comment}:</th>
					<td>${'$'}{${module.instanceName}.${_StringUtils.hump(column.name, false)}}</td>
				</tr>	
				</#list>												
			</table>
		</fieldset>					
	</form>
  </body>	
</html>
