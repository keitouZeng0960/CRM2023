<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"; %>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script>
		//入口函数
		$(function(){
			//修改数据类型功能
			$("#editBtn").click(function(){
				let code=$("#tBody input[type='checkbox']:checked");
				if(code.size()!==1){
					alert("单次只能修改一个数据");
					return;
				}
				let selectedCode=code.val();
				window.location.href="settings/dictionary/type/toTypeEditWithData.do?code="+selectedCode;
			})

			//全选与反选
			$("#selectAllCheckbox").click(function () {$("#tBody input[type='checkbox']").prop("checked",this.checked)});
			$("#tBody").on("click","input[type='checkbox']",function () {//动态标签需要使用on添加事件函数
				if($("#tBody input[type='checkbox']:checked").size()===$("#tBody input[type='checkbox']").size()){
					$("#selectAllCheckbox").prop("checked",true);
				}else{
					$("#selectAllCheckbox").prop("checked",false);
				}
			})

			//根据code删除数据字典类型（批量删除）
			$("#deleteBtn").click(function () {
				let codeList=$("#tBody input[type='checkbox']:checked");
				if(codeList.size()===0){
					alert("请选择需要删除的数据");
					return;
				}
				if(window.confirm("是否删除该数据吗？")){
					let codeListStr='';
					$.each(codeList,function () {
						codeListStr+=$.trim("code="+this.value)+"&";
					})
					let trimCodeListStr=codeListStr.substr(0,codeListStr.length-1);
					//发送异步请求到后台
					$.ajax({
						url: "settings/dictionary/type/deleteDicTypeByCode.do",
						data:trimCodeListStr,
						type:'post',
						dataType: 'json',
						success:function (data) {
							if(data.code==='1'){
								//刷新页面
								queryAllDicTypeForPages(1,10);
							}else alert(data.message);
						}
					})

				}
			});

			//页面刷新完毕后时候自动查询所有的数据字典类型并显示在页面上(分页功能尚未做完)
			queryAllDicTypeForPages(1,10);
			function queryAllDicTypeForPages(pageNo,pageSize){
				$.ajax({
					url:"settings/dictionary/type/queryAllDicTypeByPages.do",
					data:{pageNo:pageNo,pageSize:pageSize},
					type:'post',
					dataType:'json',
					success:function (data){
						let htmlStr="";
						$.each(data.typeList,function (index, obj) {
							htmlStr+="<tr className=\"active\" >";
							htmlStr+="<td><input type=\"checkbox\" value=\""+obj.code+" \" /></td>";
							htmlStr+="<td>"+(index+1)+"</td>";
							htmlStr+="<td>"+obj.code+"</td>";
							htmlStr+="<td>"+obj.name+"</td>";
							htmlStr+="<td>"+obj.description+"</td>";
							htmlStr+="</tr>";
						})
						$("#tBody").html(htmlStr);
					}
				})
			}
		})

	</script>
</head>
<body>

	<div>
		<div style="position: relative; left: 30px; top: -10px;">
			<div class="page-header">
				<h3>字典类型列表</h3>
			</div>
		</div>
	</div>
	<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;left: 30px;">
		<div class="btn-group" style="position: relative; top: 18%;">
		  <button type="button" class="btn btn-primary" id="saveBtn" onclick="window.location.href='settings/dictionary/type/toTypeSave.do'"><span class="glyphicon glyphicon-plus"></span> 创建</button>
		  <button id="editBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
		  <button id="deleteBtn" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	<div style="position: relative; left: 30px; top: 20px;">
		<table class="table table-hover">
			<thead>
				<tr style="color: #B3B3B3;">
					<td><input id="selectAllCheckbox" type="checkbox" /></td>
					<td>序号</td>
					<td>编码</td>
					<td>名称</td>
					<td>描述</td>
				</tr>
			</thead>
			<tbody id="tBody">
			</tbody>
		</table>
	</div>
</body>
</html>