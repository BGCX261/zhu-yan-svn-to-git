<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
  <head>    
    <title>学生选课系统</title>
  </head>  
  <body bgcolor="#D9DFAA">
  	<table width="400" border="1">
  	<caption>所有课程信息</caption>
  	<tr>
  	<th>课程号</th><th>课程名</th><th>开学学期</th><th>学时</th>
  	<th>学分</th><th>操作</th>
  	</tr>
  	<s:iterator value="#request.list" id="kc">
  		<tr>
  		<td align="center"><s:property value="#kc.kch"/></td>
  		<td align="center"><s:property value="#kc.kcm"/></td>
  		<td align="center"><s:property value="#kc.kxxq"/></td>
  		<td align="center"><s:property value="#kc.xs"/></td>
  		<td align="center"><s:property value="#kc.xf"/></td>
  		<td align="center">
  			<!-- 选修该课程，这里用JavaScript来确定是否选修 -->
  			<a href="selectKc.action?kcb.kch=<s:property value="#kc.kch"/>"
  					onClick="if(!confirm('您确定选修该课程吗？'))
  					return false;else return ture;">选修</a>
  			</td>
  		</tr>
  	</s:iterator>	
  	</table>    
  </body>
</html>
