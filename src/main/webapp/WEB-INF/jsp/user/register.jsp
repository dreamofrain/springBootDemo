<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>注册</title>
<%@include file="../common/common.jsp"%>
</head>

<body>
<div id="formbackground" style="position:absolute;z-index:-1; left:0;margin:auto;height:100%;width:100%;background-size: 100% 100%;">
          <font color="red">${massage }</font>
          <br><br>
          <form id="form1">
               <div style="background: rgb(255,255,255); margin:auto; border: 1px solid rgb(231, 231, 231); border-image: none; width: 400px; height: 200px; text-align: center;">
                   	<table>
						<input type="hidden" name="id" id="id" />
						<tr>
							<td>账号:</td>
							<td><input name="loginId" class="easyui-textbox"
								style="height: 32px; width: 173px" missingMessage="请输入账号!"
								data-options="required:true" /></td>
						</tr>
		                
						<tr>
							<td>密码:</td>
							<td><input name="passWord" class="easyui-textbox"
								style="height: 32px; width: 173px" missingMessage="请输入密码!"
								data-options="required:true" /></td>
						</tr>
						<tr>
							<td>姓名:</td>
							<td><input name="userName" class="easyui-textbox"
								style="height: 32px; width: 173px" missingMessage="请输入姓名!"
								data-options="required:true" /></td>
						</tr>
						<tr>
							<td>编号:</td>
							<td><input name="idCard" class="easyui-textbox"
								style="height: 32px; width: 173px" missingMessage="请输入编号!"
								data-options="required:true" /></td>
						</tr>
					</table>
				</div>
                    
                <div style="height: 50px; line-height: 50px; margin-top: 30px; border-top-color: rgb(231, 231, 231); border-top-width: 1px; border-top-style: solid;">
                	<p style="margin: 0px 35px 20px 45px;">
			            <span style="float: right;">
			            	<a style="color: rgb(204, 204, 204); margin-right: 10px;" href="login">返回主页</A>
			                <a style="background: rgb(0, 142, 173); padding: 5px 10px; border-radius: 4px; border: 1px solid rgb(26, 117, 152); border-image: none; color: rgb(255, 255, 255); font-weight: bold;" href="javascript:;" onclick="register()" id="register">注册</a> 
			            </span>         
               		</p>
               </div>
          </form>
    </div>


<script type="text/javascript">
 //点击注册按钮
   function register(){
   $("#form1").form('submit', {
		url : "register.demo.action",
		onSubmit : function() {
			return $("#form1").form("validate");
		},
		success : function(data) {
			if (data == '1') {
				$.messager.alert('','保存成功','info',function(r){
					window.location.href="login";
				});
			} else if (data == '3') {
				$.messager.alert('','保存失败!账号或用户名已被使用!','info');
			} else {
				$.messager.alert('','保存失败','info');
			}
		}
	});
   }
</script>

</body>
</html>