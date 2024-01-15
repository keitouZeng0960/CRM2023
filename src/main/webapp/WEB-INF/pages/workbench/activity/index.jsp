<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"; %>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">
<%--bootstrap plugin--%>
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<%--jquery--%>
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<!-- bootstrap datetimepicker plugin -->
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<!-- bootstrap pagination plugin -->
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css"/>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.min.js"></script>

<script type="text/javascript">
	$(function(){
		$("#createBtn").click(function (){

			$("#createActivityModal").modal("show")
		})
		$("#saveActivityBtn").click(function (){
			//获取参数
			let owner=$("#create-marketActivityOwner").val()
			let activityName=$.trim($("#create-marketActivityName").val())
			let startTime=$("#create-startTime").val()
			let endTime=$("#create-endTime").val()
			let cost=$("#create-cost").val()
			let description=$("#create-describe").val()
			//表单验证
			if(owner===""||activityName===""){
				alert("所有者与活动名称未填写")
				return;
			}
			if(startTime!=="" && endTime!==""){
				if(endTime<startTime){
					alert("结束日期不能早于开始日期")
					return;
				}
			}
			let argExp =/^(([1-9]\d*)|0)$/
			if(!argExp.test(cost)){
				alert("成本仅能填写非负整数")
				return;
			}
			//发送Ajax请求
			$.ajax({
				url:'workbench/activity/toIndex.do',
				data:{
					owner:owner,
					name:activityName,
					startDate:startTime,
					endDate:endTime,
					cost:cost,
					description:description
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					if(data.code==="0"){
						alert(data.message)
					}
					if(data.code==="1"){
						//保存成功，关闭模态窗口
						$("#createActivityModal").modal("hide")
						//刷新页面，并且保证每页显示记录条数不变
						queryActivityByConditionForPage(1,$("#bottomPagination").bs_pagination('getOption','rowsPerPage'));
					}
				}
			})
		})
		//日历插件
		$(".calenderDate").datetimepicker({
			language:'zh-CN',
			format:'yyyy-mm-dd',
			minView:'month',
			initialDate:new Date(),
			autoclose:true,
			todayBtn:true,
			clearBtn:true
		})
		//点击市场活动页面，自动跳转到第一页，默认显示10条数据
		queryActivityByConditionForPage(1,10);
		//按照条件查询市场活动，要求页面刷新后每页显示记录条数不变
		$("#queryActivityBtn").click(function () {
			queryActivityByConditionForPage(1,$("#bottomPagination").bs_pagination('getOption','rowsPerPage'));
		});

		//全选与反选功能
		$("#selectAllCheckbox").click(function () {$("#tBody input[type='checkbox']").prop("checked",this.checked)});
		$("#tBody").on("click","input[type='checkbox']",function () {//动态标签需要使用on添加事件函数
			if($("#tBody input[type='checkbox']:checked").size()===$("#tBody input[type='checkbox']").size()){
				$("#selectAllCheckbox").prop("checked",true);
			}else{
				$("#selectAllCheckbox").prop("checked",false);
			}
		})
		//删除市场活动功能
		$("#deleteActivities").click(function () {
			let idList =$("#tBody input[type='checkbox']:checked");
			if(idList.size()===0){
				alert("请选择删除项目");
				return;
			}
			if(window.confirm("是否删除该数据？")){
					//发送Ajax请求
					let id='';
					$.each(idList,function () {
						id+="id="+this.value+"&";
					})
					let ids=id.substr(0,id.length-1);
					$.ajax({
						url:'workbench/activity/deleteActivityByIds.do',
						data:ids,
						type:'post',
						dataType:'json',
						success:function (data) {
							if(data.code==='1'){
								queryActivityByConditionForPage(1,$("#bottomPagination").bs_pagination('getOption','rowsPerPage'));
							}else alert(data.message);
						}
					})
				}
		})

		//修改市场活动前查询被修改的市场活动信息功能
		$("#editActivityBtn").click(function () {
			let checkedIds=$("#tBody input[type='checkbox']:checked");
			if(checkedIds.size()===0){
				alert("请选择需要修改的市场活动");
				return;
			}
			if(checkedIds.size()!==1){
				alert("每次仅能修改一条记录");
				return;
			}
			let selectedId=checkedIds.val();
			$.ajax({
				url:'workbench/activity/queryActivityByIdWhenUpdate.do',
				data:{id:selectedId},
				type:'post',
				dataType:'json',
				success:function (data){
					$("#edit-id").val(data.id);
					$("#edit-marketActivityOwner").val(data.owner);
					$("#edit-marketActivityName").val(data.name);
					$("#edit-startTime").val(data.startDate);
					$("#edit-endTime").val(data.endDate);
					$("#edit-cost").val(data.cost);
					$("#edit-describe").val(data.description)
					$("#editActivityModal").modal("show");
				}
			})
		})
		//修改市场活动
		$("#updateBtn").click(function () {
			let editor_id=$("#edit-id").val();
			let edited_owner=$("#edit-marketActivityOwner").val();
			let edited_name=$.trim($("#edit-marketActivityName").val());
			let edited_startDate=$("#edit-startTime").val();
			let edited_endDate=$("#edit-endTime").val();
			let edited_cost=$("#edit-cost").val();
			let edited_description=$("#edit-describe").val();
			//表单验证
			if(edited_owner===""||edited_name===""){
				alert("所有者与活动名称未填写")
				return;
			}
			if(edited_startDate!=="" && edited_endDate!==""){
				if(edited_endDate<edited_startDate){
					alert("结束日期不能早于开始日期")
					return;
				}
			}
			let argExp =/^(([1-9]\d*)|0)$/
			if(!argExp.test(edited_cost)){
				alert("成本仅能填写非负整数")
				return;
			}
			$.ajax({
				url:'workbench/activity/saveEditedActivity.do',
				data:{
					id:editor_id,
					owner:edited_owner,
					name:edited_name,
					description: edited_description,
					cost:edited_cost,
					startDate:edited_startDate,
					endDate:edited_endDate
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					if(data.code==="1"){
						//修改成功之后,关闭模态窗口,刷新市场活动列表,保持页号和每页显示条数都不变
						$("#editActivityModal").modal("hide");
						queryActivityByConditionForPage($("#bottomPagination").bs_pagination('getOption','currentPage'),$("#bottomPagination").bs_pagination('getOption','rowsPerPage'));
					}else{
						alert(data.message);
						$("#editActivityModal").modal("show");
						return;
					}
				}
			})
		})

		//批量导出市场活动(未完成)
		$("#exportActivityAllBtn").click(function () {
			window.location.href="workbench/activity/exportActivities.do";
		})
		//批量导出被选择的市场活动
		$("#exportActivityXzBtn").click(function () {
			let idList =$("#tBody input[type='checkbox']:checked");
			if(idList.size()===0){
				alert("请选择项目");
				return;
			}
			let id='';
			$.each(idList,function () {
				id+="id="+this.value+"&";
			})
			let ids=id.substr(0,id.length-1);
			$.get({
				url:'workbench/activity/exportActivitiesByIds.do',
				data:{
					ids:ids
				},
				success:function () {

				}
			})
		})

		//导入市场活动（未完成）


		//翻页查询市场活动函数
		function queryActivityByConditionForPage(pageNo,pageSize){
			let name=$("#query-name").val();
			let owner=$("#query-owner").val();
			let startDate=$("#query-startTime").val();
			let endDate=$("#query-endTime").val();
			$.ajax({
				url:"workbench/activity/queryActivityByConditionForPage.do",
				data:{
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					pageSize:pageSize,
					pageNo:pageNo
				},
				dataType: 'json',
				type: 'post',
				success:function(data){
						let htmlStr="";
						// 遍历json中的数据只能使用jquery，遍历作用域中的数据使用jstl标签
						$.each(data.activityList,function (index,object) {
							htmlStr+="<tr class='active'>"
							htmlStr+="<td><input type='checkbox' value='"+object.id +"'/></td>"
							htmlStr+="<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/query/ActivityByIdForRemark.do?id="+object.id+"'\">"+object.name +"</a>"
							htmlStr+="<td>"+object.owner+"</td>"
							htmlStr+="<td>"+object.startDate+"</td>"
							htmlStr+="<td>"+object.endDate+"</td>"
							htmlStr+="</tr>"
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
							queryActivityByConditionForPage(page.currentPage,page.rowsPerPage);
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

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>

				<div class="modal-body" id="activityForm">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
									<c:forEach items="${userList}" var="user">
								  <option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="calenderDate form-control " id="create-startTime" readonly >
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="calenderDate form-control " id="create-endTime" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveActivityBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
								  <c:forEach items="${userList}" var="u">
									  <option value="${u.id}">${u.name}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control calenderDate" id="edit-startTime" >
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control calenderDate" id="edit-endTime" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: rgb(128,128,128);">[仅支持.xls或.xlsx格式]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS/XLSX的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>

	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;" >
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon" >开始日期</div>
					  <input class="form-control calenderDate" type="text" id="query-startTime" readonly/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon" >结束日期</div>
					  <input class="form-control calenderDate" type="text" id="query-endTime" readonly/>
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="queryActivityBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editActivityBtn" data-target="#editActivityModal"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivities"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="selectAllCheckbox"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tBody">
					</tbody>
				</table>
			</div>
			<div id="bottomPagination"></div>
		</div>
		
	</div>
</body>
</html>