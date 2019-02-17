<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.inc"%>
<%@ include file="/common/operator_login.inc"%>
<%@ include file="/common/taglibs.inc"%>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
	<title>${module.menuName }</title>	
</head>
 <body class="ui-main">
	<form name="searchform" action="index.do" method="get">	
	<div class="ui-query-bar">
		<table>
			<tr>	
			<!-- 屏蔽查询条件					
				<th>名称:</th>
				<td><input type="text" id="name" name="name" value="${'$'}{name }" /></td>	
			-->				
				<td><button type="submit" class="btn">查询</button>&nbsp;&nbsp;
					<li:p code="${module.code}01">
						<button type="button" class="btn" onclick="addInfo();">新建</button>
					</li:p>
				</td>
			</tr>
		</table>
	</div>
	<div id="resultGrid">				
		<table id="gridData" cellpadding="0" cellspacing="0"></table>
		<div id="gridPager"></div>
	</div>				
	</form>
	<script type="text/javascript">
	function formatShow(cellValue, options, rowObject) 
	{
		return "<button type=\"button\" class=\"iconBtn btnShow\" onclick=\"showInfo("+ cellValue +");\"></button>";
	}
			
	function formatEdit(cellValue, options, rowObject) 
	{
		return "<button type=\"button\" class=\"iconBtn btnEdit\" onclick=\"editInfo("+ cellValue +");\"></button>";
	}
			
	function formatDelete(cellValue, options, rowObject) 
	{		  
		return "<button type=\"button\" class=\"iconBtn btnDel\" onclick=\"deleteInfo("+ cellValue +");\"></button>";
	}
			
	$("#gridData").jqGrid({pager:"#gridPager",
		url:"pageGrid.json",
		datatype: "json",
		//postData: {name: $("#name").val() },
		colModel:[
	        <#list columnList as column>
			{name:'${column.name}',index:'${column.name}',label:'${column.comment}'},
			</#list>	        
	        {label: '查看',name:'id', align:"center",width:50, formatter:formatShow, resizable: false, sortable:false}	        
	        <li:p code="${module.code}02">
	        	,{label: '编辑',name:'id', align:"center",width:50, formatter:formatEdit, resizable: false, sortable:false}
	        </li:p>		        
	        <li:p code="${module.code}03">
	        	,{label: '删除',name:'id', align:"center",width:50, formatter:formatDelete, resizable: false, sortable:false}	
	        </li:p>		        			       
	       ]
	});        					
	 
	function addInfo(id){
	  	littCommon.openPop("add.do","新建${module.title }", 600, 400); 	  		
	}
		
	function editInfo(id){
		littCommon.openPop("edit.do?id="+id,"编辑${module.title }", 600, 400); 	
	}
	  	
	function showInfo(id){
		littCommon.openPop("show.do?id="+id,"查看${module.title }", 600, 400); 	
	}
	  	
	function deleteInfo(id){
		if(littCommon.confirmEx("删除记录"))
		{ 
			$.post("delete.json", {"id":id}, function(reply){			
				littCommon.ajaxSubmit(reply, "删除成功！", refresh);
			});			
		}	
	}
	
	function query()
	{					
		var postData = $("#gridData").getGridParam("postData");
		//$.extend(postData, {name: $("#name").val()}); 				
		$("#gridData").trigger("reloadGrid", [{page:1}]);
	}
	
	function refresh()
	{
		$("#gridData").trigger("reloadGrid");
		littCommon.closePop();
	}
	</script>		
  </body>
</html>
