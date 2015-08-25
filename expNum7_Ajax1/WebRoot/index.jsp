<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
	<head>
		<title>Ajax应用</title>
	</head>

	<script type="text/javascript">
  	var xmlHttp;
  	//创建XMLHttpRequest对象
  	function createHttpRequest(){
  		if(window.XMLHttpRequest){
  			xmlHttp=new XMLHttpRequest();
  		}
  		else if(window.ActiveXObject){
  			xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
  		}
  	}
  	
  	function beginCheck(){
  		var xh=document.all.xh.value;
  		if(xh==""){
  			alert("对不起，请输入注册学号!");
  			return;
  		}
  		createHttpRequest();	
  		xmlHttp.onreadystatechange=processor;
  		xmlHttp.open("get","CheckUser?xh="+xh);
  		xmlHttp.send(null);
  	}
  	
  	function processor(){
  		var responseContext;
  		if(xmlHttp.readyState==4){  			
  			if(xmlHttp.status==200){
  				responseContext=xmlHttp.responseText;
  				if(responseContext.indexOf("true")!=-1){
  					alert("恭喜你，该学号有效!");
  				}
  				else{
  					alert("对不起，该学号已经被注册!");
  				}
  			}
  		}
  	}
  	</script>
	<body>
		<form action="">
			学号:
			<input type="text" name="xh" onchange="beginCheck()" />
			口令：
			<input type="password" name="kl" />
			<input type="submit" value="注册" />
		</form>
	</body>
</html>







