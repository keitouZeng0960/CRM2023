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
	<!-- pagination插件-->
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css"/>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.min.js"></script>

<script type="text/javascript">
	$(function () {
		//修改数据类型功能
		$("#editBtn").click(function(){
			let id=$("#tBody input[type='checkbox']:checked");
			if(id.size()!==1){
				alert("单次只能修改一个数据");
				return;
			}
			let selectedId=id.val();
			window.location.href="settings/dictionary/value/toValueEdit.do?id="+selectedId;
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

		//批量删除数据
		$("#removeBtn").click(function () {
			let idList=$("#tBody input[type='checkbox']:checked");
			if(idList.size()===0){
				alert("请选择需要删除的数据");
				return;
			}
			if(window.confirm("是否删除该数据吗？")) {
				let codeListStr = '';
				$.each(idList, function () {
					codeListStr += $.trim("id=" + this.value) + "&";
				})
				let trimIdListStr = codeListStr.substr(0, codeListStr.length - 1);
				//发送异步请求到后台
				$.ajax({
					url: "settings/dictionary/value/removeValueById.do",
					data: trimIdListStr,
					type: 'post',
					dataType: 'json',
					success: function (data) {
						if (data.code === '1') {
							//刷新页面
							queryAllValueByPage(1, 10);
						} else alert(data.message);
					}
				})
			}
		})


		queryAllValueByPage(1,10)
		function queryAllValueByPage (pageNo,pageSize){
			$.ajax({
				url:"settings/dictionary/value/queryAllValueByPage.do",
				data:{
					pageNo:pageNo,
					pageSize:pageSize
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					htmlStr="";
					$.each(data.valueList,function (index,obj){
						htmlStr+="<tr>";
						htmlStr+="<td><input type=\"checkbox\" value=\""+obj.id+"\"/></td>";
						htmlStr+="<td>"+(index+1)+"</td>";
						htmlStr+="<td>"+obj.value+"</td>";
						htmlStr+="<td>"+obj.text+"</td>";
						htmlStr+="<td>"+obj.orderNo+"</td>";
						htmlStr+="<td>"+obj.typeCode+"</td>";
						htmlStr+="</tr>";
					})
					$("#tBody").html(htmlStr);
					let totalPage =1;//计算总页数
					if(data.countNo % pageSize===0){
						totalPage=data.countNo/pageSize;
					}else{
						totalPage=parseInt(data.countNo/pageSize)+1;
					}
					//翻页插件
					$("#bottomPagination").bs_pagination({
						currentPage:pageNo,
						rowsPerPage:pageSize,
						totalRows:data.countNo,
						totalPages:totalPage,
						visiblePageLinks: 5,
						showGoToPage: true,
						showRowsInfo: true,
						showRowsPerPage: true,
						onChangePage:function (event,page) {
							queryAllValueByPage(page.currentPage,page.rowsPerPage);
							$("#selectAllCheckbox").prop("checked",false);
						}
					})
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
				<h3>字典值列表</h3>
			</div>
		</div>
	</div>
	<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;left: 30px;">
		<div class="btn-group" style="position: relative; top: 18%;">
		  <button type="button" class="btn btn-primary" onclick="window.location.href='settings/dictionary/value/toValueSave.do'"><span class="glyphicon glyphicon-plus"></span> 创建</button>
		  <button id="editBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
		  <button id="removeBtn" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	<div style="position: relative; left: 30px; top: 20px;">
		<table class="table table-hover">
			<thead>
				<tr style="color: #B3B3B3;">
					<td><input id="selectAllCheckbox" type="checkbox" /></td>
					<td>序号</td>
					<td>字典值</td>
					<td>文本</td>
					<td>排序号</td>
					<td>字典类型编码</td>
				</tr>
			</thead>
			<tbody id="tBody">
			</tbody>
		</table>

		<div id="bottomPagination"></div>
	</div>
	
</body>
</html>