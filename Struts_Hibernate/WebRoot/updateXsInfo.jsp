<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<head>
	<title>学生选课系统</title>
</head>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body bgcolor="#D9DFAA">
		<s:set name="xs" value="#request.xs"></s:set>
		<s:form action="updateXs.action" method="post"> <!-- enctype="multipart/form-data" -->
			<table width="400">
				<tr>
					<td>
						学号:
					</td>
					<td>
						<input type="text" name="xs.xh"
							value="<s:property value="#xs.xh"/>" readOnly />
					</td>
				</tr>
				<tr>
					<td>
						姓名:
					</td>
					<td>
						<input type="text" name="xs.xm"
							value="<s:property value="#xs.xm"/>
					" />
					</td>
				</tr>
				<tr>
					<s:radio list="#{1:'男',0:'女'}" value="#xs.xb" label="性别"
						name="xs.xb"></s:radio>
				</tr>
				<tr>
					<td>
						专业:
					</td>
					<td>
						<SELECT name="zyb.id">
							<s:iterator id="zy" value="#request.zys">
								<option value="<s:property value="#zy.id"/>">
									<s:property value="#zy.zym" />
								</option>
							</s:iterator>
						</SELECT>
					</td>
				</tr>
				<tr>
					<td>
						出生时间:
					</td>
					<td>
						<input type="text" name="xs.cssj"
							value="<s:date name="#xs.cssj" format="yyyy-MM-dd" />">
					</td>
				</tr>
				<tr>
					<td>
						总学分:
					</td>
					<td>
						<input type="text" name="xs.zxf"
							value="<s:property value="#xs.zxf" />" />
					</td>
				</tr>
				<tr>
					<td>
						备注:
					</td>
					<td>
						<input type="text" name="xs.bz"
							value="<s:property value="#xs.bz" />" />
					</td>
				</tr>
				<tr>
					<td>
						照片:
					</td>
					<td>

					</td>
				</tr>
				<tr>
					<td>
						<input type="submit" value="修改" />
					</td>

				</tr>

			</table>
		</s:form>
	</body>
</html>
