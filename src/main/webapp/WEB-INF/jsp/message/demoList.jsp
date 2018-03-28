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
	  类别：
	<select class="easyui-combobox" name="otherValue" id="otherValue" editable=false>
		<option value="" selected="selected">请选择</option>
		<option value="一级">一级</option>
		<option value="二级">二级</option>
		<option value="三级">三级</option>
	</select>&nbsp;&nbsp;
	<!-- <a class="easyui-linkbutton" iconCls="icon-search" onclick="selectRecordByDate()">查询</a> -->
	<c:if test="${sessionScope.user.loginId=='admin'}">
		<a class="easyui-linkbutton" iconCls="icon-add" onclick="addDemo()">增加</a>&nbsp;
		<a class="easyui-linkbutton" iconCls="icon-edit" onclick="editDemo()">修改</a>&nbsp;
		<a class="easyui-linkbutton" iconCls="icon-remove"
			onclick="removeDemo()">删除记录</a>&nbsp;
		<a class="easyui-linkbutton" iconCls="icon-page-excel"
			onclick="exportExcel()">导出</a>&nbsp;
		<a class="easyui-linkbutton" iconCls="icon-page-excel"
			onclick="importExcel()">导入</a>&nbsp;
		<a class="easyui-linkbutton" iconCls="icon-remove"
			onclick="removeAll()">一键删除</a>&nbsp;
		<a class="easyui-linkbutton" iconCls="icon-application-get"
			onclick="generateMessage()">生成报文</a>
	</c:if>
	<c:if test="${sessionScope.user.loginId!='admin'}">
		<c:forEach var="per" items="${sessionScope.perList }">
			<c:if test="${per=='1' }">
				<a class="easyui-linkbutton" iconCls="icon-add" onclick="addDemo()">增加</a>&nbsp;
			</c:if>
			<c:if test="${per=='2' }">
				<a class="easyui-linkbutton" iconCls="icon-edit" onclick="editDemo()">修改</a>&nbsp;
			</c:if>
			<c:if test="${per=='3' }">
				<a class="easyui-linkbutton" iconCls="icon-remove"
					onclick="removeDemo()">删除记录</a>&nbsp;
				<a class="easyui-linkbutton" iconCls="icon-remove"
					onclick="removeAll()">一键删除</a>&nbsp;
			</c:if>
			<c:if test="${per=='4' }">
			<a class="easyui-linkbutton" iconCls="icon-page-excel"
				onclick="exportExcel()">导出</a>&nbsp;
			</c:if>
			<c:if test="${per=='5' }">
				<a class="easyui-linkbutton" iconCls="icon-page-excel"
					onclick="importExcel()">导入</a>&nbsp;
			</c:if>
			<c:if test="${per=='6' }">
				<a class="easyui-linkbutton" iconCls="icon-application-get"
					onclick="generateMessage()">生成报文</a>
			</c:if>
		</c:forEach>
	</c:if>
	<div style="width: 250px; height: 5px;"></div>
	<table id="dt"></table>
	<div style="visibility: hidden;">
		<div id="changeDig" class="easyui-dialog"
			style="width: 300px; height: 300px; top: 120px"
			data-options="iconCls:'icon-',toolbar:'#tbForDialog'">
			<!-- 对话框的工具栏  -->
			<div id="tbForDialog" style="padding-left: 10px;">
				<a class="easyui-linkbutton" data-options="iconCls:'icon-save'"
					onclick="saveForAddDialog()">保存</a>&nbsp;&nbsp;
			</div>
			<form id="changeFrom" method="post">
				<table>
					<input type="hidden" name="id" id="id" />
					<input type="hidden" name="status" id="status" />
					<tr>
						<td>姓名:</td>
						<td><input name="name" class="easyui-textbox"
							style="height: 32px; width: 173px" missingMessage="请输入姓名!"
							data-options="required:true" /></td>
					</tr>
					<tr>
						<td>性别:</td>
						<td><input type="radio" name="sex" value="♂" checked="checked">♂
							<input type="radio" name="sex" value="♀">♀</td>
					</tr>
					<tr>
						<td>类别:</td>
						<td><select id="type" name="type" class="easyui-combobox"
							editable=false panelHeight=90 style="height: 32px; width: 173px">
							<option value="一级" selected="selected">一级</option>
							<option value="二级">二级</option>
							<option value="三级">三级</option>
						</select></td>
					</tr>
					<tr>
						<td>编号:</td>
						<td><input id="card" name="card" class="easyui-textbox"
							style="height: 32px; width: 173px" missingMessage="请输入编号!"
							data-options="required:true" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	
	<!-- 导入文件dialog -->
	<div style="visibility: hidden;">
		<div id="importFileDialog" class="easyui-dialog"
			style="width: 500px; height:160px; padding-left: 30px" title="导入数据">
			<h2>请选择要导入的Excel文件</h2>
			<table id="importTable" border="0">
				<tr>
					<td><input type="file" name="file_info" id="file_info" size="60" onchange="fileSelected()" />&nbsp;</td>
				</tr>
				<tr>
					<td><input type="button" id="importId" value="提交" size="60" onclick="fileUp()"></td>
				</tr>
			</table>
		</div>
	</div>

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
				url : "demo.page.action",
				pageSize:10,
				pageList : [ 10, 15 ,20],
				columns : [ [ 
					{field : 'id',title : 'ID',width : 100,hidden:true}, 
					{field : 'name',title : '姓名',width : 100}, 
					{field : 'sex',title : '性别',sortable:true,width : 100},
					{field : 'type',title : '类别',sortable:true,width : 100},
					{field : 'card',title : '编号',sortable:true,width : 100},
					{field : 'status',title : '状态',width : 100,hidden:true}
				]]
			});
		});
		
		//动态查询
		function selectRecordByDate() {
			var otherValue = $("#otherValue").combobox("getValue");//其他条件

			$("#dt").datagrid("load", {
				otherValue: otherValue
			});
		}

		//新增
		function addDemo() {
			$(".easyui-textbox").textbox("setValue", "");
			//渲染下拉列表
			/* $('#type').combobox({
				url : 'typename.select.action',
				valueField : 'tid',
				textField : 'tname',
				editable : false,
				//加载完毕后默认选中第一行
				onLoadSuccess : function() { //加载完成后,设置选中第一项
					var val = $(this).combobox("getData");
					for ( var item in val[0]) {
						if (item == "tid") {
							$(this).combobox("select", val[0][item]);
						}
					}
				}
			}); */
			$("#id").val(""); //清空新增和修改弹出框的id文本框
			
			$('#type').combobox("select","一级");
			$('#card').textbox({validType: 'subjectA'});
			$("#changeDig").dialog({title : "新增"});
			$('#changeDig').dialog('open');
			$('#changeDig').dialog({modal : true});
			$("#changeDig").show();
		}

		//点击修改按钮
		function editDemo() {
			var row = $("#dt").datagrid('getSelected');
			if (row) {
				$('#changeFrom').form('load', row);
				/* $('#type').combobox({
					url : 'typename.select.action',
					valueField : 'tid',
					textField : 'tname',
					editable : false,
					//加载完毕后选中当前类别
					onLoadSuccess : function() {
						$(this).combobox("select", row.type.tid);
					}
				}); */
				$("#changeDig").dialog({title : "修改"});
				$('#changeDig').dialog('open');
				$('#changeDig').dialog({modal : true});
				$("#changeDig").show();
			}else {
				$.messager.alert("提示", "请选择一行");
			}
		}

		$("#type").combobox({
			onChange: function (n,o) {
				if(n=='一级'){
					$('#card').textbox({validType: 'subjectA'});
				}
				if(n=='二级'){
					$('#card').textbox({validType: 'subjectB'});
				}
				if(n=='三级'){
					$('#card').textbox({validType: 'subjectC'});
				}
			}
		});
		
		$("#otherValue").combobox({
			onChange: function (n,o) {
				$("#dt").datagrid("load", {
					otherValue: n
				});
			}
		});
		
		//点击保存按钮
		function saveForAddDialog() {
			$("#changeFrom").form('submit', {
				url : "demo.update.action",
				onSubmit : function() {
					return $("#changeFrom").form("validate");
				},
				success : function(data) {
					if (data == 1) {
						$.messager.alert("", "保存成功", 'info', function(r) {
							window.location.href = "demoList";
						});
					} else {
						$.messager.alert("提示", "保存失败");
					}
				}
			});
		}

		//点击删除记录数据
		function removeDemo() {
			var row = $("#dt").datagrid('getSelected');
			if (row) {
				$.messager.confirm('确定', '您确定要删除吗', function(t) {
					if (t) {
						$.ajax({
							url : 'demo.delete.action',
							method : 'POST',
							data : {
								"id" : row.id
							},
							success : function(date) {
								if (date == "1") {
									$.messager.alert("", "删除成功", 'info', function(r) {
										window.location.href = "demoList";
									});
								} else {
									$.messager.alert("提示", "删除失败");
								}
							}
						});
					}
				});
			} else {
				var otherValue = $("#otherValue").combobox("getValue");//其他条件
				if(otherValue!=''&&otherValue!=null){
					$.messager.confirm('确定', '您确定要删除当前选中类别吗', function(t) {
						if (t) {
							$.ajax({
								url : 'demo.delete.action',
								method : 'POST',
								data : {
									"type" : otherValue
								},
								success : function(date) {
									if (date == "1") {
										$.messager.alert("", "删除成功", 'info', function(r) {
											window.location.href = "demoList";
										});
									} else {
										$.messager.alert("提示", "删除失败");
									}
								}
							});
						}
					});
				}else{
					$.messager.alert("提示", "请选择一行");
				}
			}
		}
		
		//点击删除全部
		function removeAll() {
			$.messager.confirm('确定', '您确定要删除所有么？', function(t) {
				if (t) {
					$.ajax({
						url : 'demo.deleteAll.action',
						method : 'POST',
						success : function(date) {
							if (date == "1") {
								$.messager.alert("", "删除成功", 'info', function(r) {
									window.location.href = "demoList";
								});
							} else {
								$.messager.alert("提示", "删除失败");
							}
						}
					});
				}
			});
		}
		
		//导出到excel
		function exportExcel() {
			var otherValue = $("#otherValue").combobox("getValue");//其他条件
			$.messager.confirm('操作提示', '确认将数据导出到Excel表格吗？', function(t) {
				if (t) {
					window.location.href="demo.export.action?type="+otherValue;
				}
			});
		}
		//===============与导入相关的js============================

		function importExcel() {
			$("#file_info").val("");
			$('#importFileDialog').dialog('open');
			$('#importFileDialog').dialog({modal : true});
			document.getElementById("importId").disabled = "disabled";
		}

		function fileSelected() {
			var file = document.getElementById("file_info").files[0];
			var fileName = file.name;
			var fileType = fileName.substring(fileName.lastIndexOf('.'),fileName.length);
			if (fileType == '.xls' || fileType == '.xlsx') {
				if (file) {
					document.getElementById("importId").disabled = "";
				}
			} else {
				$.messager.alert('警告', "导入文件应该是.xls为后缀而不是" + fileType + "请重新选择文件！","error");
				document.getElementById("importId").disabled = "disabled";
			}
		}
		
		//导入Excel
		function fileUp(){
			$.messager.confirm('操作提示','确认要导入Excel表格吗？',function(r){
				if(r){
					$.messager.progress({
						title:'请稍等',
						msg:'数据正在导入中......'
					});
					$.ajaxFileUpload({
						url : 'demo.import.action',
						fileElementId : 'file_info',
						type:"post",
						secureuri: false,
						dataType: 'json',
						success : function(data, status){
							$.messager.progress('close');
							if(data){
								if(new RegExp('导入文件成功！').test(data.msg)){
									$.messager.alert("", "导入成功", 'info', function(r) {
										window.location.href = "demoList";
									});
								}else{
									//$.messager.alert('操作提示',data.msg.replace(/,/g,'<br>'),'info');
									$.messager.alert({
										title: '操作提示',
										height: 370,
										width: 400,
										msg: "<div style='overflow-y:scroll;height:275px;width:375px;mapping-top:20px;'>" + data.msg.replace(/,/g, '<br>') + "</div>"
									});
								}
							}
						},
						error:function(data, status, e){
				            $.messager.alert('提示', e);
						} 
					})
				}
			});
		}
		
		//生成报文
		function generateMessage() {
			var otherValue = $("#otherValue").combobox("getValue");//其他条件
			if(otherValue!=''&&otherValue!=null){
				$.messager.confirm('操作提示', '确定要生成报文吗', function(r) {
					if(r) {
						$.messager.progress({
							title: '请稍等',
							msg: '正在生成报文中......'
						});
						$.ajax({
							url: 'demo.generateMessage.action',
							type: 'POST', //GET
							async: true, //或false,是否异步
							contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
							data : {
								"otherValue":otherValue
							},
							dataType: 'json', //返回的数据格式：json/xml/html/script/jsonp/text
							success: function(data) {
								$.messager.progress('close');
								if(data == '1') {
									$.messager.alert("", "报文生成成功", 'info', function(r) {
										window.location.href = "demoList";
									});
								} else {
									$.messager.alert('提示', '报文生成失败！', 'error');
								}
							},
							error: function(e) {
								$.messager.alert('提示', '报文生成失败！', 'error');
							}
						});
					}
				});
			}else{
				$.messager.alert('提示', '请选择类别！', 'info');
			}
		}
	</script>
</body>
</html>