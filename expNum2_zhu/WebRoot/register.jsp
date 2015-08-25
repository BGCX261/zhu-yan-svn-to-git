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
		<form action="registerServlet" method="post">
			<center>
				<table width="300" border="1">
					<caption>
						注册信息
					</caption>
					<tbody>
						<tr>
							<td>
								登录名：
							</td>
							<td>
								&nbsp;
								<input type="text" size="20" name="username">
							</td>
						</tr>
						<tr>
							<td>
								密&nbsp; &nbsp; 码：
								<br>
							</td>
							<td>
								&nbsp;
								<input type="password" size="20" name="pwd">
							</td>
						</tr>
						<tr>
							<td>
								&nbsp;
								<input type="submit" value="提交">
							</td>
							<td>
								&nbsp;
								<input type="reset" value="重置">
							</td>
						</tr>
					</tbody>
				</table>
			</center>
		</form>
	</body>
</html>
