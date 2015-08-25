<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<head>
	<title>学生选课系统</title>
</head>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body bgcolor="#D9DFAA">
		<table width="400">
			<s:set name="xs" value="#request.xs"></s:set>
			<tr>
				<td>
					学号:
				</td>
				<td>
					<s:property value="#xs.xh" />
				</td>
			</tr>
			<tr>
				<td>
					姓名:
				</td>
				<td>
					<s:property value="#xs.xm" />
				</td>
			</tr>
			<tr>
				<td>
					性别:
				</td>
				<td>
					<s:if test="#xs.xb==1">男</s:if>
					<s:else>女</s:else>
				</td>
			</tr>
			<tr>
				<td>
					专业:
				</td>
				<td>
					<s:property value="#xs.zyb.zym" />
				</td>
			</tr>
			<tr>
				<td>
					出生时间:
				</td>
				<td>
					<s:date name="#xs.cssj" format="yyyy-MM-dd"/>
				</td>
			</tr>
			<tr>
				<td>
					总学分:
				</td>
				<td>
					<s:property value="#xs.zxf" />
				</td>
			</tr>
			<tr>
				<td>
					备注:
				</td>
				<td>
					<s:property value="#xs.bz" />
				</td>
			</tr>
			<tr>
				<td>
					照片:
				</td>
				<td>
					
				</td>
			</tr>

		</table>
	</body>
	
</html>
