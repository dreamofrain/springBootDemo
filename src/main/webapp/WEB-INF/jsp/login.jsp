<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>登录</title>
<script src="jquery/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript">
	if(top.location!=self.location){
	    top.location = "login";
	}
</script>
<style type="text/css">
     html {
  /* background: url(images/background3.jpg) no-repeat center center fixed; */
  -webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;
}
.ipt{
	border: 1px solid #d3d3d3;
	padding: 10px 10px;
	width: 290px;
	border-radius: 4px;
	padding-left: 35px;
	-webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
	box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
	-webkit-transition: border-color ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;
	-o-transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
	transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s
}
 .ipt:focus{
	border-color: #66afe9;
	outline: 0;
	-webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(102,175,233,.6);
	box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(102,175,233,.6)
} 
.u_logo{
	/* background: url("images/username.png") no-repeat; */
	padding: 10px 10px;
	position: absolute;
	top: 23px;
	left: 40px;

}
.p_logo{
	/* background: url("images/password.png") no-repeat; */
	padding: 10px 10px;
	position: absolute;
	top: 12px;
	left: 40px;
}
a{
	text-decoration: none;
}
span{
   display:block;
}
</style>

</head>

<body>
<div id="formbackground" style="position:absolute;z-index:-1; left:0; top:0px;height:100%;width:100%;background-size: 100% 100%;">
    
    <div style="padding-top: 100px;text-align: center">
         <span style="color: red;font-size: xx-large;padding-top: 100px">${message}</span>
    </div>
    
    <div style="position:absolute;left:39%;transform:translateX(-50%);top:45%;transform:translateY(-50%);text-align: center;margin: 0 auto;height: 200px;width: 400px">
          <form id="form1" method="post" action="login.demo.action" style="margin: 0 auto">
               <div style="background: rgb(255,255,255); margin: 0 auto; border: 1px solid rgb(231, 231, 231); border-image: none; width: 400px; height: 200px; text-align: center;">
                    <p style="padding: 10px 0px 10px; position: relative;">
		                <span class="u_logo"></span>
		                <input class="ipt" type="text" id="loginId" name="loginId" placeholder="请输入登录账号"/>
	                </p>
	                
	                <p style="position: relative;">
				         <span class="p_logo"></span>         
				         <input class="ipt" id="password" name="password" type="password" placeholder="请输入密码"/>
	                </p>
                    
                    
                    <div style="height: 50px; line-height: 50px; margin-top: 30px; border-top-color: rgb(231, 231, 231); border-top-width: 1px; border-top-style: solid;">
	                    <p style="margin: 0px 35px 20px 45px;">
		                      <span style="float: left;">
		                         <a style="color: rgb(204, 204, 204);" href="javascript:void(0)">忘记密码?</a>
	                          </span> 
	              
				              <span style="float: right;">
				                   <a style="color: rgb(204, 204, 204); margin-right: 10px;" href="register">注册</A>
				                   <a style="background: rgb(0, 142, 173); padding: 5px 10px; border-radius: 4px; border: 1px solid rgb(26, 117, 152); border-image: none; color: rgb(255, 255, 255); font-weight: bold;" href="javascript:;" onclick="login()" id="login">登录</a> 
				              </span>         
	                    </p>
	                </div>
               </div>
          </form>
    </div>
</div>


<script type="text/javascript">
 //点击登录按钮
   function login(){
	   $("#form1").submit();
   }
   
   $(function(){
		//登录页绑定enter键登录
	   	$(document).keydown(function(event) { 
	   		if (($("#loginId").val() != "" && $("#password").val() != "")
	   			&& event.keyCode === 13) {
	   			$("#login").trigger("click");
	   			return false;
	   		}
	   	});
	});
</script>

</body>
</html>