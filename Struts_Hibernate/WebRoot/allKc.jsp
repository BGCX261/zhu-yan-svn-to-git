<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<head>
	<title>学生选课系统</title>
</head>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body bgcolor="#D9DFAA">
		<table width="400" border="1">
			<caption>所有课程信息:</caption>
			<tr>
				<td>课程号</td>
				<td>课程名</td>
				<td>开学学期</td>
				<td>学时</td>
				<td>学分</td>
				<td>操作</td>
			</tr>
			<s:iterator value="#request.list" id="kc">
				<tr>
					<td align="center"><s:property value="#kc.kch"/> </td>
					<td align="center"><s:property value="#kc.kcm"/> </td>
					<td align="center"><s:property value="#kc.kxxq"/> </td>
					<td align="center"><s:property value="#kc.xs"/> </td>
					<td align="center"><s:property value="#kc.xf"/> </td>
					<td align="center"><a href="selectKc.action?kcb.kch=<s:property value="#kc.kch"/>"
					onclick="if(!confirm('您确定选修该课程吗?'))return false;else return true">选修</a></td>
				</tr>
			</s:iterator>
		</table>
	</body>
</html>
