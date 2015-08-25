<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=ISO-8859-1">
		<title>Insert title here</title>
	</head>
	<body bgcolor="#D9DFAA">
		<center>
			<s:form action="login.action" method="post">
				<table>
					<tr>
						<td colspan="2" align="center">
							学生选课系统
						</td>
					</tr>
					<tr>
						<s:textfield name="dlb.xh" label="学号" size="20"></s:textfield>
					</tr>
					<tr>
						<s:password name="dlb.kl" label="口令" size="20"></s:password>
					</tr>
					<tr>
						<td>
							<input type="submit" value="登录" />
						</td>
						<td>
							<input type="reset" value="重置" />
						</td>
					</tr>
				</table>
			</s:form>
		</center>
	</body>
</html>