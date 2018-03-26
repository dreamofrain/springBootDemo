<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>用户管理</title>
<%@include file="../common/common.jsp"%>
</head>

<body>
	<a class="easyui-linkbutton" iconCls="icon-remove"
		onclick="removeDemo()">封停用户</a>&nbsp;
	<a class="easyui-linkbutton" iconCls="icon-remove"
		onclick="deblockingDemo()">解封用户</a>&nbsp;
	<a class="easyui-linkbutton" iconCls="icon-remove"
		onclick="jurisdictionUser()">权限分配</a>&nbsp;
	<table id="dt"></table>
	
	<div style="visibility: hidden;">
		<div id="perDig" class="easyui-dialog" title="权限分配" data-options="iconCls:'icon-save',toolbar:'#tbForAssignRole'" style="width: 800px; height: 500px;padding: 10px">
			<div id="tbForAssignRole" style="padding-left: 30px; padding-top: 10px">
				<a class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="save()">保存</a>&nbsp;&nbsp;
				<a class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="cancel()">取消</a>
			</div>

			<table id="AssignRoleForTable" class="easyui-datagrid" fitColumns="true" style="width:760px;height:400px" data-options="checkOnSelect:true,autoRowHeight:false,pagination:true,rownumbers:true,idField:'id',pageSize:50,singleSelect:true">
				<thead>
						<tr>
							<th field="ck" checkbox="true"></th>
							<th data-options="field:'id',width:230,align:'center',hidden:true"></th>
							<th data-options="field:'perCode',width:230,align:'center'">权限代码</th>
							<th data-options="field:'perName',width:230,align:'center'">权限名称</th>
						</tr>
			   </thead>
		   </table>
	   </div>
   </div>

	<script type="text/javascript">
		$(function() {
			$('#perDig').dialog('close');
			
			$("#dt").datagrid({
				loadMsg:'数据加载中,请稍后...',
				rownumbers:true,
				fitColumns:true,
				toolbar:'#tb',
				striped : true,
				pagination : true,
				singleSelect : true,
				remoteSort:false,
				url : "users.page.action",
				pageSize:10,
				pageList : [ 10, 15 ,20],
				columns : [ [ 
					{field : 'id',title : 'ID',width : 100,hidden:true}, 
					{field : 'userName',title : '姓名',width : 100}, 
					{field : 'loginId',title : '账号',sortable:true,width : 100},
					{field : 'passWord',title : '密码',width : 100},
					{field : 'idCard',title : '编号',sortable:true,width : 100},
					{field : 'status',title : '状态',sortable:true,width : 100}
				]]
			});
		});

		//点击删除记录数据
		function removeDemo() {
			var row = $("#dt").datagrid('getSelected');
			if (row) {
				if(row.loginId=='admin'){
					$.messager.alert("提示", "无法对管理员用户进行操作");
				}else{
					if(row.status=='已停封'){
						$.messager.alert("提示", "该用户已被停封");
					}else{
						$.messager.confirm('确定', '您确定要封停用户吗', function(t) {
							if (t) {
								$.ajax({
									url : 'users.update.action',
									method : 'POST',
									data : {
										"id" : row.id,
										"status" : 'stop'
									},
									success : function(date) {
										if (date == "1") {
											$.messager.alert("", "成功", 'info', function(r) {
												window.location.href = "userList";
											});
										} else {
											$.messager.alert("提示", "失败");
										}
									}
								});
							}
						});
					}
				}
			} else {
				$.messager.alert("提示", "请选择一行");
			}
		}
		
		//点击删除记录数据
		function deblockingDemo() {
			var row = $("#dt").datagrid('getSelected');
			if (row) {
				if(row.loginId=='admin'){
					$.messager.alert("提示", "无法对管理员用户进行操作");
				}else{
					if(row.status!='已停封'){
						$.messager.alert("提示", "该用户未被停封");
					}else{
						$.messager.confirm('确定', '您确定要解封该用户吗', function(t) {
							if (t) {
								$.ajax({
									url : 'users.update.action',
									method : 'POST',
									data : {
										"id" : row.id,
										"status" : 'deblock'
									},
									success : function(date) {
										if (date == "1") {
											$.messager.alert("", "成功", 'info', function(r) {
												window.location.href = "userList";
											});
										} else {
											$.messager.alert("提示", "失败");
										}
									}
								});
							}
						});
					}
				}
			} else {
				$.messager.alert("提示", "请选择一行");
			}
		}
		
		function jurisdictionUser() {
			var row = $("#dt").datagrid('getSelected');
			if (row) {
				if(row.loginId=='admin'){
					$.messager.alert("提示", "无法对管理员用户进行操作");
				}else{
					$("#AssignRoleForTable").datagrid({
						loadMsg:'数据加载中,请稍后...',
						rownumbers:true,
						fitColumns:true,
						toolbar:'#tb',
						striped : true,
						pagination : true,
						singleSelect : false,
						remoteSort:false,
						url : "per.list.action",
						pageSize:10,
						pageList : [ 10, 15 ,20],
						columns : [ [ 
							{field:'ck',checkbox:true},
							{field : 'id',title : 'ID',width : 100}, 
							{field : 'perCode',title : '权限代码',width : 100}, 
							{field : 'perName',title : '权限名称',width : 100},
						]],
						onLoadSuccess : function(data){
							var roleResourceList = data.rows;
							$.ajax({
								url : 'users.per.action',
								method : 'POST',
								data : {
									"id" : row.id
								},
								success : function(date) {
									$('#AssignRoleForTable').datagrid('unselectAll');
									if(date!=null&&date!=undefined){
										for(var j = 0;j<date.length;j++){
											$('#AssignRoleForTable').datagrid('selectRow',date[j].perid-1);
										}
									}
								}
							});
						}
					});
					
					$('#perDig').dialog('open');
					$('#perDig').dialog({modal : true});
				}
			} else {
				$.messager.alert("提示", "请选择一行");
			}
		}
		
		//点击取消按钮
		function cancel() {
			$('#perDig').dialog('close');
		}
		
		//点击角色资源分配中的保存按钮
		function save(){
			var rows = $('#AssignRoleForTable').datagrid('getSelections');
			var row = $("#dt").datagrid('getSelected');
			if(rows.length>0){
				var pers = "";
				//将ID数组转化为字符串
		    	for(var i = 0;i<rows.length;i++){
		    		pers = pers+rows[i].id+"-";
		    	}
		    	pers = pers.substring(0, pers.length - 1);
				$.ajax({
					url : 'users.setper.action',
					method : 'POST',
					data : {
						"user" : row.id,
						"pers" : pers
					},
					success : function(data) {
						if(data == '1') { //如果返回1
							$.messager.alert("", "成功", 'info', function(r) {
								window.location.href = "userList";
							});
						} else { ////提交失败
							$.messager.alert('操作提示', '分配失败', 'error');
						}
					}
				});
			}else{
				$.messager.alert("提示", "请选择一行");
			}
		}
	</script>
</body>
</html>