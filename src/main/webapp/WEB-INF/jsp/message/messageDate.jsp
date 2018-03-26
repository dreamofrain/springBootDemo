<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>数据管理</title>
<%@include file="../common/common.jsp"%>
</head>

<body>
	<a class="easyui-linkbutton" href="massage" iconCls="icon-return">返回</a>
	<table id="dt"></table>

	<script type="text/javascript">
		$(function() {
			$('#changeDig').dialog('close');
			$('#importFileDialog').dialog('close');
			
			$("#dt").datagrid({
				loadMsg:'数据加载中,请稍后...',
				rownumbers:true,
				fitColumns:true,
				toolbar:'#tb',
				striped : true,
				pagination : true,
				singleSelect : true,
				remoteSort:false,
				url : 'showMessage?messageName='+'<%=request.getParameter("messageName") %>',
				pageSize:10,
				pageList : [ 10, 15 ,20],
				columns : [ [ 
					{field : 'id',title : 'ID',width : 100,hidden:true}, 
					{field : 'name',title : '姓名',width : 100}, 
					{field : 'sex',title : '性别',sortable:true,width : 100},
					{field : 'type',title : '类别',sortable:true,width : 100},
					{field : 'card',title : '编号',sortable:true,width : 100}
				]]
			});
		});

	</script>
</body>
</html>