<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>Ajax应用</title>
	</head>
	<script type="text/javascript">
var xmlHttp;
function createHttpRequest() {
	if (window.ActiveXObject) {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xmlHttp = new XMLHttpRequest();
	}
}

function beginCheck() {
	var reg = /\s/g;
	var xh = document.all.xh.value;
	if (xh.replace(reg,"") == "") {
		alert("对不起，请输入学号");
		return;
	}
	alert("|" + xh + "|");
	createHttpRequest();
	xmlHttp.onreadystatechange = processor;
	xmlHttp.open("get", "CheckUser?xh=" + xh);
	xmlHttp.send(null);
}

function processor() {
	var responseContext;
	if (xmlHttp.readyState == 4) {
		if (xmlHttp.status == 200) {
			responseContext = xmlHttp.responseText;
			if (responseContext.indexOf("true") != -1) {
				alert("恭喜你，该学号有效");
			} else {
				alert("对不起，该学号已经存在");
			}
		}
	}
}
</script>
	<body>
		<form action="">
			学号：
			<input type="text" name="xh" onchange="beginCheck()">
			<br />
			口令：
			<input type="password" name="kl">
			<br>
			<input type="submit" value="注册">
		</form>
	</body>
</html>
