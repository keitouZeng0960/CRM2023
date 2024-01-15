<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta charset="UTF-8">
<link href="${pageContext.request.contextPath}/jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(function () {
			//添加键盘按下事件
			$(window).keyup(function (event){
				if(event.keyCode===13){
					//在指定的元素上模拟发生一次指定的事件
					$("#loginButton").click();
				}
			})

			$("#loginButton").click(function () {
				let username = $("#userName").val();
				let usernameTrim = username.trim();
				let pwd = $("#pwd").val();
				let pwdTrim=pwd.trim();
				const isRemPwd = $("isRemPwd").prop("checked");
				//发送请求
				$.ajax({
					url:'${pageContext.request.contextPath}/settings/qx/user/login.do',
					data:{//key值跟后端controller接收参数的形式参数名一致
						loginAct:usernameTrim,
						loginPwd:pwdTrim,
						isRemPwd:isRemPwd
					},
					dataType:'json',
					type:'post',
					success:function (data){
						if(data.code==="1"){
							window.location.href="${pageContext.request.contextPath}/workbench/index.do"
						}else{$("#msg").html(data.message);}
					},
					beforeSend:function (){
						//当ajax向后台发送请求之前，会自动执行这个函数。该函数的返回值能够决定是ajax是否真的向后台发送请求
						//如果返回true，则Ajax会真正向后台发送请求；返回false，则放弃向后台发请求
						//表单验证
						if(usernameTrim===""){
							alert("用户名不能为空");
							return false;
						}
						if(pwdTrim===""){
							alert("密码不能为空");
							return false;
						}
						$("#msg").html("正在努力验证...");
						return true;
					}
				})
			})

		})
	</script>
</head>
<body>
	<div style="position: absolute; top: 0; left: 0; width: 60%;">
		<img src="${pageContext.request.contextPath}/image/IMG_7114.JPG"
			 style="width: 100%; height: 90%; position: relative; top: 50px;"
			 alt="图片不见喽">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman',serif">CRM &nbsp;<span style="font-size: 12px;">&copy;2019&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="${pageContext.request.contextPath}/workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input id="userName" class="form-control" value="${cookie.loginAct.value}" type="text" placeholder="用户名">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input id="pwd" class="form-control" value="${cookie.loginPwd.value}" type="password" placeholder="密码">
					</div>
					<div id="isRemPwd" class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						<label>
							<c:if test="${not empty cookie.loginAct and not empty cookie.loginPwd}">
								<input type="checkbox"  checked> 十天内免登录
							</c:if>
							<c:if test="${empty cookie.loginAct or empty cookie.loginPwd}">
								<input type="checkbox"> 十天内免登录
							</c:if>
						</label>
						&nbsp;&nbsp;
						<span id="msg"></span>
					</div>
					<button type="button"  id="loginButton" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>