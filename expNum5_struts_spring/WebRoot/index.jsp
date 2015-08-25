<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<title>登录界面</title>
  <body>
  		<s:form action="login.action" method="post">
  			<s:textfield name="xh" label="学号"></s:textfield>
  			<s:password name="kl" label="口令"></s:password>
  			<s:submit value="登录"></s:submit>
  		</s:form>
   </body> 
</html>
