<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<s:head/>
  </head>
  
  <body> 
    <h3>添加学生信息</h3>
    <s:form action="save.action" method="post" theme="simple">
    	<table>
    		<tr><td>学号:</td>
    		<td><s:textfield name="xs.xh"></s:textfield></td>
    		</tr><tr><td>姓名:</td>
    		<td><s:textfield name="xs.xm"></s:textfield></td>
    		</tr><tr><td>性别:</td>
    		<td><s:radio list="#{1:'男',2:'女'}" name="xs.xb" value="1"></s:radio></td>
    		</tr><tr><td>专业:</td>
    		<td><s:textfield name="xs.zy" label="专业"></s:textfield></td>
    		</tr><tr><td width="70">出生时间:</td>
    		<td><s:datetimepicker name="xs.cssj" id="cssj" displayFormat="yyyy-MM-dd"></s:datetimepicker></td>
    		</tr><tr><td>备注:</td>
    		<td><s:textarea name="xs.bz" label="备注"></s:textarea></td>
    		</tr><tr><td><s:submit value="添加"></s:submit></td>
    		<td><s:reset value="重置"></s:reset></td></tr>
    	</table>
    </s:form>
  </body>
</html>
