<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.inc"%>
<%@ include file="/common/operator_login.inc"%>
<%@ include file="/common/taglibs.inc"%>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
	<title>编辑${module.title }</title>
  </head>
  <body class="ui-main">		 
	<form name="theform" method="post" action="update.json" class="ui-form">	
		<input type="hidden" name="id" value="${'$'}{${module.instanceName}.id }" />
		<fieldset>	
			<legend>编辑${module.title }</legend>			
			<table class="ui-form-content">	
				<#list columnList as column>
				<tr>					
					<th>${column.comment}:</th>
					<td><input type="text" name="${_StringUtils.hump(column.name, false)}" value="${'$'}{${module.instanceName}.${_StringUtils.hump(column.name, false)}}"/></td>
				</tr>	
				</#list>										
			</table>
		</fieldset>	
		<div class="ui-form-func-bar">
			<button type="button" class="btn"  onclick="ajaxSubmit(this.form);">保存</button>&nbsp;&nbsp;&nbsp;
			<button type="reset" class="btn">重置</button>								
		</div>			
	</form>
  </body>	
</html>
