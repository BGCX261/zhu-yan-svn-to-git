<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
  <head>
  	<title>登录界面</title>
  <body>
    <s:form action="login.action" method="post">
    	<s:textfield name="xh" label="学号"/>
    	<s:password name="kl" label="口令"/>
    	<s:submit value="登录"/>
    </s:form>    	
  </body>
</html>
