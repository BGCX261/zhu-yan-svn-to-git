<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<head>
	<title>ѧ��ѡ��ϵͳ</title>
</head>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body bgcolor="#D9DFAA">
		<table width="400" border="1">
			<caption>p����ѡ����Ϣ����:</caption>
			<tr>
				<td>�γ̺�</td>
				<td>�γ���</td>
				<td>��ѧѧ��</td>
				<td>ѧʱ</td>
				<td>ѧ��</td>
				<td>����</td>
			</tr>
			<s:iterator value="#request.list" id="kc">
				<tr>
					<td align="center"><s:property value="#kc.kch"/> </td>
					<td align="center"><s:property value="#kc.kcm"/> </td>
					<td align="center"><s:property value="#kc.kxxq"/> </td>
					<td align="center"><s:property value="#kc.xs"/> </td>
					<td align="center"><s:property value="#kc.xf"/> </td>
					<td align="center"><a href="deleteKc.action?kcb.kch=<s:property value="#kc.kch"/>"
					onclick="if(!confirm('��ȷ����ѡ�ÿγ���?'))return false;else return true">��ѡ</a></td>
				</tr>
			</s:iterator>
		</table>
	</body>
</html>
