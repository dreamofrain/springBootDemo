<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>用户管理</title>
<%@include file="../common/common.jsp"%>
</head>

<body>
	<a class="easyui-linkbutton" iconCls="icon-remove"
		onclick="showMessage()">报文数据</a>&nbsp;
	<c:if test="${sessionScope.user.loginId=='admin'}">
		<a class="easyui-linkbutton" iconCls="icon-download" onclick="downloadMessages()">下载报文</a>&nbsp;
		<a class="easyui-linkbutton" iconCls="icon-remove" onclick="removeMassages()">打回报文</a>
	</c:if>
	<c:if test="${sessionScope.user.loginId!='admin'}">
		<c:forEach var="per" items="${sessionScope.perList }">
			<c:if test="${per=='8' }">
				<a class="easyui-linkbutton" iconCls="icon-download" onclick="downloadMessages()">下载报文</a>&nbsp;
			</c:if>
			<c:if test="${per=='9' }">
				<a class="easyui-linkbutton" iconCls="icon-remove" onclick="removeMassages()">打回报文</a>
			</c:if>
		</c:forEach>
	</c:if>
	<div style="width: 250px; height: 5px;"></div>
	<table id="dt"></table>
	
	<script type="text/javascript">
		$(function() {
			$("#dt").datagrid({
				loadMsg:'数据加载中,请稍后...',
				rownumbers:true,
				fitColumns:true,
				toolbar:'#tb',
				striped : true,
				pagination : true,
				singleSelect : true,//单行选择
				remoteSort:false,
				url : "massage.page.action",
				pageSize:10,
				pageList : [ 10, 15 ,20],
				columns : [ [ 
					{field : 'id',title : 'ID',sortable:true,width : 100},
					{field : 'name',title : '报文名',sortable:true,width : 100},
					{field : 'path',title : '路径',width : 100,hidden:true}
				]]
			});
		});
		
		//点击删除记录数据
		function removeMassages() {
			var row = $("#dt").datagrid('getSelected'); //获取选中行对象
			if(row){
				$.messager.confirm('确定', '您确定要打回报文么？', function(t) {
					if (t) {
						$.ajax({
							url : 'messages.remove.action',
							method : 'POST',
							data:{
								"id": row.id
							},
							success : function(date) {
								if (date == "1") {
									$.messager.alert("", "打回成功", 'info', function(r) {
										window.location.href = "massage";
									});
								} else {
									$.messager.alert("提示", "打回失败");
								}
							}
						});
					}
				});
			}else{
				$.messager.alert("提示", "请选择一条报文打回");
			}
		}
		
		//点击下载报文按钮
		function downloadMessages(){
			var row = $("#dt").datagrid('getSelected'); //获取选中行对象
			if(row){
				window.location.href = "downloadMessages?id="+row.id;
			}else{
				$.messager.alert("提示", "请选择一条报文下载");
			}
		}
		
		//报文数据
		function showMessage(){
			var row = $("#dt").datagrid('getSelected'); //获取选中行对象
			if(row){
				window.location.href = "messageDate?messageName="+row.name;
			}else{
				$.messager.alert("提示", "请选择一条报文显示数据");
			}
		}
	</script>
</body>
</html>