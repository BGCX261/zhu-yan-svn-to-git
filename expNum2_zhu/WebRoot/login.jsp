<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>简易留言板</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

	</head>

	<body bgcolor="#E3E3E3">
		<form action="mainServlet" method="post">
			<table>
				<caption>
					用户登录
				</caption>
				<tr>
					<td>
						登录名：
					</td>
					<td>
						<input type="text" name="username" size="20" />
					</td>
				<tr>
					<td>
						密码：
					</td>
					<td>
						<input type="password" name="pwd" size="22" />
					</td>
				</tr>
			</table>
			<input type="submit" value="登录" />
			<input type="reset" value="重置" />
		</form>
		如果没注册单击<a href="register.jsp">这里</a>注册!
	</body>
</html>
