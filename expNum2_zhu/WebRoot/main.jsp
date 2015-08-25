<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="model.LyTable"%>
<%@ page import="db.DB"%>
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

		<title>留言板信息</title>

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
		<form action="message.jsp" method="post">
			<table border="1">
				<caption>
					所有留言信息
				</caption>
				<tr>
					<th>
						留言人姓名
					</th>
					<th>
						留言时间
					</th>
					<th>
						留言标题
					</th>
					<th>
						留言内容
					</th>
				</tr>
				<%
					ArrayList al = (ArrayList) session.getAttribute("al");
					Iterator iter = al.iterator();
					while (iter.hasNext()) {
						LyTable ly = (LyTable) iter.next();
				%>
				<tr>
					<td><%=new DB().getUserName(ly.getUserId())%></td>
					<td><%=ly.getDate().toString()%></td>
					<td><%=ly.getTitle()%></td>
					<td><%=ly.getContent()%></td>
				</tr>
				<%
					}
				%>
			</table>
			<input type="submit" value="留言" />
		</form>
	</body>
</html>
