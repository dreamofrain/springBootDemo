/**
 * @param input<br>
 * {<br>
 * data<br>
 * successCallback<br>
 * failedCallback<br>
 * }
 * @param showSuccessMsg
 */
function showResponseMsg(input, showSuccessMsg) {
	$.messager.progress('close');
	var data = input.data;
	var successCallback = input.successCallback;
	var failedCallback = input.failedCallback;
	
	if (!data) {
		$.messager.alert("警告", "服务器没有响应!", "warning");
		return;
	}
	
	if (typeof data == "string") {
		if (data.indexOf("{") == 0 || data.indexOf("[") == 0) {
			data = $.evalJSON(data);
		} else if(data.indexOf('id="pageLogin"') != -1) {
			top.location.href = "login.do";
		} else {
			top.Exception = data;
			document.getElementById('winException').style.display = "block";
			$('#winException').window('open');
//			$.messager.alert("错误", "系统发生错误,请与开发商联系!", "error", failedCallback);
			return;
		}
	}/* else if (data instanceof Object) {
		if (data.success == undefined && data.failed == undefined) {
			top.Exception = data;
			document.getElementById('winException').style.display = "block";
			$('#winException').window('open');
//			$.messager.alert("错误", "系统发生错误,请与开发商联系!", "error", failedCallback);
			return;
		}
	}*/
	
	if (data.success) {
		if (showSuccessMsg) {
			$.messager.alert("信息", data.msg, "info", successCallback);
		} else {
			if (successCallback)
				successCallback();
		}
	}
	
	if (data.failed) {
		$.messager.alert("错误", data.msg, "error", failedCallback);
	}
}
