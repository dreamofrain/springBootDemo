<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>用户管理系统</title>
	<%@include file="common.jsp"%>
	<script type="text/javascript">
		$(function(){
	      fnDate();
	      //定时器每秒调用一次
	   	  setInterval(function(){
	   	     fnDate();
	   	  },1000);
	    })

	    
	    function fnDate(){
	    		var oDiv=document.getElementById("nowDateTimeSpan");
	    		var date=new Date();
	    		var year=date.getFullYear();//当前年份
	    		var month=date.getMonth();//当前月份
	    		var data=date.getDate();//天
	    		var hours=date.getHours();//小时
	    		var minute=date.getMinutes();//分
	    		var second=date.getSeconds();//秒
	    		var time=year+"-"+fnW((month+1))+"-"+fnW(data)+" "+fnW(hours)+":"+fnW(minute)+":"+fnW(second);
	    		oDiv.innerHTML=time;
	    }

	    //补位 当某个字段不是两位数时补0
	    function fnW(str){
	    	var num;
	    	str<10?num="0"+str:num=str;
	    	return num;
	    }
		
	    function addTab(title, href, icon) {
	    	var tt =$('#myTabs');
	    	if (tt.tabs('exists', title)) {// 如果tab已经存在,则选中并刷新该tab
	    		tt.tabs('select', title);
	    		refreshTab({
	    			tabTitle : title,
	    			url : href
	    		});
	    	} else {
	    		if (href) {
	    			var content = '<iframe frameborder="0"  src="'
	    					+ href + '" style="width:100%;height:100%;"></iframe>';
	    		} else {
	    			var content = '未实现';
	    		}
	    		tt.tabs('add', {
	    			title : title,
	    			closable : true,
	    			content : content
	    			//,iconCls : icon || 'icon-edit'
	    		});
	    	}
	    }
	    
	    function refreshTab(cfg) {
	    	var refresh_tab = cfg.tabTitle ? $('#myTabs').tabs('getTab', cfg.tabTitle)
	    			: $('#myTabs').tabs('getSelected');
	    	if (refresh_tab && refresh_tab.find('iframe').length > 0) {
	    		var _refresh_ifram = refresh_tab.find('iframe')[0];
	    		var refresh_url = cfg.url ? cfg.url : _refresh_ifram.src;
	    		// _refresh_ifram.src = refresh_url;
	    		_refresh_ifram.contentWindow.location.href = refresh_url;
	    	}
	    }
	    
		/* function addTab(title, url){
			if ($('#myTabs').tabs('exists', title)){
				$('#myTabs').tabs('select', title);
			} else {
				var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
				$('#myTabs').tabs('add',{
					title:title,
					content:content,
					closable:true
				});
			}
		} */
	</script>
  </head>
  
  <body>
    <div id="cc" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'north',title:'顶部',split:true" style="height:100px;">
	    	<div>
		    	<span style="font-size: 15px;color: red">当前用户：${sessionScope.user.userName }</span>&nbsp;&nbsp;
		    	<a href="loginOut">登出</a>
		    	<br>
	    		<span style="color: green;" id="nowDateTimeSpan"></span>
	    	</div>
	    </div>   
	    <div data-options="region:'south',title:'页尾',split:true" style="height:80px;"></div>
	    <div data-options="region:'west',title:'系统菜单',split:true" style="width:200px;">
	    	<!-- <ul id="tt"></ul> -->
			<c:if test="${sessionScope.user.loginId=='admin'}">
				<div class="easyui-accordion" style="height:100%;">
			    	<div title="用户管理" data-options="iconCls:'people'">
				    	<ul id="tree0" class="easyui-tree">
							<li data-options="iconCls:'people'">
								<a onclick="addTab('用户列表','userList')"><span>用户列表</span></a>
							</li>
							<li data-options="iconCls:'people'">
								<a onclick="addTab('个人信息','userData')"><span>个人信息</span></a>
							</li>
						</ul>
				    </div>
				    <div title="数据管理" data-options="iconCls:'icon-large-chart'">
				    	<ul id="tree1" class="easyui-tree">
							<li data-options="iconCls:'icon-large-chart'">
								<a onclick="addTab('数据列表','demoList')"><span>数据列表</span></a>
							</li>
						</ul>
				    </div>
				    <div title="报文管理" data-options="iconCls:'icon-generatemessage'">
				    	<ul id="tree2" class="easyui-tree">
							<li data-options="iconCls:'icon-generatemessage'">
								<a onclick="addTab('报文列表','massage')"><span>报文列表</span></a>
							</li>
						</ul>
				    </div>
		    	</div>
			</c:if>
			<c:if test="${sessionScope.user.loginId!='admin'}">
				<div class="easyui-accordion" style="height:100%;">
			    	<div title="用户管理" data-options="iconCls:'people'">
				    	<ul id="tree0" class="easyui-tree">
							<li data-options="iconCls:'people'">
								<a onclick="addTab('个人信息','userData')"><span>个人信息</span></a>
							</li>
						</ul>
					</div>
					
					<c:forEach var="per" items="${sessionScope.perList }">
						<c:if test="${per=='7' }">
							<div title="数据管理" data-options="iconCls:'icon-large-chart'">
						    	<ul id="tree1" class="easyui-tree">
									<li data-options="iconCls:'icon-large-chart'">
										<a onclick="addTab('数据列表','demoList')"><span>数据列表</span></a>
									</li>
								</ul>
							</div>
						</c:if>
						<c:if test="${per=='10' }">
							<div title="报文管理" data-options="iconCls:'icon-generatemessage'">
						    	<ul id="tree2" class="easyui-tree">
									<li data-options="iconCls:'icon-generatemessage'">
										<a onclick="addTab('报文列表','massage')"><span>报文列表</span></a>
									</li>
								</ul>
					    	</div>
						</c:if>
					</c:forEach>
		    	</div>
			</c:if>
	    </div>   
	    
	    <div data-options="region:'center',title:'功能区'" style="padding:5px;background:#eee;">
	    	<div id="myTabs" class="easyui-tabs" data-options="fit:true">   
			    <div title="主页" style="padding:20px">   
			        <h1>这里是没什么用的主页</h1>  
			    </div>
			</div>  
	    </div>
	</div>  
  </body>
</html>