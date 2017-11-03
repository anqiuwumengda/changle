<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
	<head>
		<meta charset="UTF-8">
		<title>操作员管理</title>
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
		<%@taglib prefix="b" uri="/WEB-INF/lrdButton.tld"%> 
		<link rel="stylesheet" href="../css/bootstrap.min.css">
		<link rel="stylesheet" href="../css/AdminLTE.min.css">
		<link rel="stylesheet" href="../css/skins/_all-skins.min.css">
		<link rel="stylesheet" href="../css/font-awesome.min.css">
		<link rel="stylesheet" href="../css/zTreeStyle/zTreeStyle.css">
		<link rel="stylesheet" href="../css/bootstrapValidator.css">
		<link rel="stylesheet" href="../css/demo.css">
		<script src="../js/jquery.min.js"></script>
		<script src="../js/bootstrap.min.js"></script>
		<script src="../js/bootstrapValidator.js"></script>
		<script src="../js/app.min.js"></script>
		<script src="../js/showPages.js"></script>
		<script src="../js/common.js"></script>
		<script src="../js/jquery.ztree.core-3.5.min.js"></script>
		
	</head>
	<body class="pbody">
		<div class="box box-solid pt-10 no-margin">
			<div class="box-body">
			<form class="form-horizontal clearfix">
			 <div class="form-group col-md-6 col-xs-6">
			    <label for="USER_ID" class="col-md-4 control-label">用户编号：</label>
			    <div class="col-md-8">
			      <input type="text" class="form-control" id="USER_ID" placeholder="请输入">
			    </div>
			  </div>
			  <div class="form-group col-md-6 col-xs-6">
			    <label for="USER_NAME" class="col-md-4 control-label">用户姓名：</label>
			    <div class="col-md-8">
			      <input type="text" class="form-control" id="USER_NAME" placeholder="请输入">
			    </div>
			  </div>
			  <div class="form-group col-md-6 col-xs-6">
			    <label for="ORG_NAME" class="col-md-4  control-label">所属机构：</label>
			    <div class="col-md-8 ">
			      <input type="text" class="form-control" id="ORG_NAME"  disabled="disabled" placeholder="请输入">
			      <span id='jg_search' class="form-group-addon"><i class="fa fa-search"></i></span>
			    </div>
			  </div>
			  <div class="form-group col-md-6 col-xs-6">
			    <label for="ROLE_CD" class="col-md-4 control-label">用户角色：</label>
			    <div class="col-md-8 ">
			      <input type="text" class="form-control"  id="ROLE_CD" disabled="disabled" placeholder="请输入">
			      <span id='js_search' class="form-group-addon"><i class="fa fa-search"></i></span>
			    </div>
			  </div>
			</form>
 			<div class="search_bottom col-md-12">
 				<button id="search" class="btn btn-primary btn-sm">查询</button>
				<button id="clear" class="btn btn-info btn-sm">清空</button>
 			</div>
 			</div>
		</div>
		<div class="pane_btn clearfix">
			<b:lbutton buttonId="btn_add" buttonClass="btn btn-primary btn-sm" iClass="fa fa-plus" buttonName="添加" funcCd="010000000103"></b:lbutton>
				<b:lbutton buttonId="btn_edit" buttonClass="btn btn-primary btn-sm" iClass="fa fa-edit" buttonName="修改" funcCd="010000000203"></b:lbutton>
				<b:lbutton buttonId="btn_del" buttonClass="btn btn-primary btn-sm" iClass="fa fa-trash" buttonName="删除" funcCd="010000000303"></b:lbutton>
				<b:lbutton buttonId="btnModifyPsw" buttonClass="btn btn-warning btn-sm" iClass="fa fa-edit" buttonName="修改密码" funcCd="010000000403"></b:lbutton>
				<b:lbutton buttonId="btnNewPsw" buttonClass="btn btn-warning btn-sm" iClass="fa fa-edit" buttonName="重置密码" funcCd="010000000503"></b:lbutton>
				
		</div>
		<div class="table-responsive box box-solid">
		<form >
			<table class="table table-bordered table-hover no-margin">
				<thead>
					<tr>
						<th>序号</th>
						<th>法人行号</th>
						<th>法人名称</th>
						<th>人员编号</th>
						<th>姓名</th>
						<th>所属机构</th>
						<th>用户角色</th>
					</tr>
				</thead>
				<tbody id="operator">
					
				</tbody>
			</table></form>
		</div>
		<div>
			<ul class="pagination pagination-sm" id="pagination">
			</ul>
		</div>

<div id="myModa2" class="modal fade" tabindex="-1" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content modal_content_sm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h5 class="modal-title" >用户角色</h5>
			</div>
			<div class="">
				<table class="table table-bordered table-hover no-margin">
				<thead>
					<tr>
						<th>选择带回</th>
						<th>序号</th>
						<th>角色代码</th>
						<th>角色名称</th>
						<th>角色说明</th>
					</tr>
				</thead>
				<tbody id="tree2">
				</tbody>
				</table>
			</div>
				
			<div class="modal-footer">
			 	<button class="btn btn-default" id="take_back">选择带回</button>
			    <button class="btn btn-default" data-dismiss="modal">返回</button>
			</div>
		</div>
	</div>
</div>
<div id="myModal" class="modal fade" tabindex="-1" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content modal_content_sm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h5 class="modal-title" >所属机构</h5>
			</div>
			<div class="">
				<ul id="tree1" class="ztree"></ul>
			</div>
				
			<div class="modal-footer">
			 	
			    <button class="btn btn-default" data-dismiss="modal">返回</button>
			</div>
		</div>
	</div>
</div>
<div id="myModa0" class="modal fade" tabindex="-1" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content modal_content_sm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h5 class="modal-title" >修改密码</h5>
			</div>
			<div class="modal-body">
			<form class="form-horizontal" id="form_pwd">
				<div class="form-group">
					<label class="col-md-3 control-label no-padding">人员编号:</label>
					<div class="col-md-6">
						<input class="bs-form-control" type="text" id="uId" disabled="disabled" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-3 control-label no-padding">原密码:</label>
					<div class="col-md-6">
						<input class="bs-form-control" type="password" id="oPd" name="oPd" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-3 control-label no-padding">新密码:</label>
					<div class="col-md-6">
						<input class="bs-form-control" type="password" id="xPd" name="xPd"  />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-3 control-label no-padding">确认密码:</label>
					<div class="col-md-6">
						<input class="bs-form-control" type="password" id="nPd" name="nPd"  />
					</div>
				</div>
			</form>
			</div>				
			<div class="modal-footer">
			 	<button class="btn btn-default" id="uPwd">确认</button>
			    <button class="btn btn-default" data-dismiss="modal">返回</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
Io.on("#take_back","click",function(){
	var checkboxs=$("[name=checkBox]:checked");
	var roleStr="",roleNameStr="";
	if(checkboxs.size()>0){
		if(checkboxs.size()==1){
			roleStr=checkboxs.attr("role_cd");
			roleNameStr=checkboxs.attr("role_name");
			}else{
				for(var i=0;i<checkboxs.size();i++){
					if(i==checkboxs.size()-1){
						roleStr+=checkboxs.eq(i).attr("role_cd");
						roleNameStr+=checkboxs.eq(i).attr("role_name");
					}else{
						roleStr+=checkboxs.eq(i).attr("role_cd")+",";
						roleNameStr+=checkboxs.eq(i).attr("role_name")+",";
					}
				}
			}
		
		$("#ROLE_CD").val(roleNameStr).attr("role_id",roleStr);
		$("#myModa2").modal("hide");
	}
});
var setting = {
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			beforeClick: function(treeId, treeNode) {
				var zTree = $.fn.zTree.getZTreeObj("tree");
				/* if (treeNode.isParent) {
					return false;
				} else {
					$("#ORG_NAME").val(treeNode.name).attr('org_cd',treeNode.id);
					$("#myModal").modal("hide").on("hidden.bs.modal");
					return true;
				} */
				$("#ORG_NAME").val(treeNode.name).attr('org_cd',treeNode.id);
				$("#myModal").modal("hide").on("hidden.bs.modal");
				return true;
			},
		onClick:function(treeId, treeNode) {
			
		}
		}
	};
//清空
$("#clear").on("click",function(){
	$("form").find("input").val('');
	$("#ROLE_CD").attr("role_id","");
	$("#ORG_NAME").attr("org_cd","");
});
$("#jg_search").on("click",function(){
	$("#myModal").modal({
		backdrop:false
	});
});
$("#js_search").on("click",function(){
	$("#myModa2").modal({
		backdrop:false
	});
});
//新增
//Io.on("#btn_add","click",function(){
	$("#btn_add").on("click",function(){
	location.href="./operator_add.html";
});

Io.on("#operator tr","click",function(){
	$(this).addClass("on").siblings().removeClass("on");
});
var CQ = {};
function pagerList (currentPage,pageSize){
	data1={
			"isPagination":"true",
			"currentPage":currentPage,
			"pageSize":pageSize,
			"ORG_RELA_TYPE":"00"
		};
	$.extend(data1,CQ);
		console.log(data1);
	$.ajax({
		type:"post",
		url:baseurl+"lrduser/querylist",
		dataType:"json",
		contentType:"application/json",
		data:JSON.stringify(data1),
		success:function(data){
			if(data.code=="00"){
				if(data.result.result!=null && data.result.result.length){
					var resultArr=data.result.result;
					var html="";
					for(var i=0;i<resultArr.length;i++){
						var result = resultArr[i];
						html+='<tr USER_ID="'+result.USER_ID+'">';
						html+='<td class="text-center">' + (i+1)+ '</td>';						
						html+='<td>' + result.CORP_CD + '</td>';						
						html+='<td>' + result.CORP_NAME + '</td>';
						html+='<td>' + result.USER_ID + '</td>';
						html+='<td>' + result.USER_NAME + '</td>';
						html+='<td>' + result.ORG_NAME + '</td>';
						html+='<td>' + result.name + '</td>';
						html+='</tr>';
					}
					$("#operator").html(html);
					$.showPages('pagination',data.page.totalRows,pageSize,currentPage,'pagerList');
				}else{$("#operator").html('<tr><td align="center" height="150" colspan="7">暂时没有内容</td></tr>');}
				var ORG=data.result.ORG;
				var ROLE=data.result.ROLE;
				$.fn.zTree.init($("#tree1"), setting, ORG);
				var html="";
				for(var i=0;i<ROLE.length;i++){
					var result = ROLE[i];
					html+='<tr>';
					html+='<td><input type="checkbox" name="checkBox" role_cd="'+result.ROLE_CD+'" role_name="'+result.name+'"/></td>';
					html+='<td class="text-center">' + (i+1)+ '</td>';						
					html+='<td>' + result.ROLE_CD + '</td>';						
					html+='<td>' + result.name + '</td>';
					html+='<td>' + result.DESC + '</td>';
					html+='</tr>';
				}
				$("#tree2").html(html);
				
			}else{
				alert(data.errMsg);
			}
		}
	})
}
pagerList (1,10);
	var CORP_CD = $("#myid"),
	USER_NAME = $("#USER_NAME"),
	USER_ID=$("#USER_ID"),
	ORG_NAME = $("#ORG_NAME"),
	ROLE_CD = $("#myrole");
//查询
	Io.on("#search","click",function(){
		if($("#USER_NAME").val()!=""){
			CQ.USER_NAME=$("#USER_NAME").val();
		}else{
			delete CQ.USER_NAME;
		}
		if($("#ORG_NAME").attr("org_cd")!=""){
			CQ.ORG_CD=$("#ORG_NAME").attr("org_cd");
		}else{
			delete CQ.ORG_CD;
		}
		if($("#USER_ID").val()!=""){
			CQ.USER_ID=$("#USER_ID").val();
		}else{
			delete CQ.USER_ID;
		}
		if($("#ROLE_CD").attr('role_id')!=""){
			CQ.ROLE_CD=$("#ROLE_CD").attr('role_id');
		}else{
			delete CQ.ROLE_CD;
		}
		pagerList (1,10);
	});
	//删除
$("#btn_del").on("click",function(){
	var uid=$("#operator").find("tr.on").attr("USER_ID");
	if(uid!=''&&uid!=undefined){
		Io.bs.alert("确认删除吗？", function(){	
			var data1 = {
					"USER_ID":uid
				};
			$.ajax({
				type:"post",
				url:baseurl+"lrduser/delete",
				dataType:"json",
				contentType:"application/json",
				data:JSON.stringify(data1),
				success:function(data){
					if(data.code=="00"){
						alert("成功");
						pagerList (1,10);
					}else if (data.code=="88"){
						eval(data.reLoad);
					}else{
						alert(data.errMsg);
					}
				},error:function(data){
					console.log("错误");
				}
			})
		})
	}else{
		alert("请选择一个项目(人员编号不能为空)");
	}
	
});
	//修改
$("#btn_edit").on("click",function(){
	var uid=$("#operator").find("tr.on").attr("USER_ID");
	if(uid!=''&&uid!=undefined){
		location.href="./operator_edit.html?s="+uid;
	}else{
		alert("请选择一个项目(人员编号不能为空)");
	}
	
});
//修改密码
$("#btnModifyPsw").on("click",function(){
	var uid=$("#operator").find("tr.on").attr("USER_ID");
	if(uid!=''&&uid!=undefined){
		$("#myModa0").modal({
			backdrop:false
		});
		$("#oPd").val("");
		$("#xPd").val("");
		$("#nPd").val("");
		$("#uId").val(uid);
	}else{
		alert("请选择一个项目");
	}	
});
$("#uPwd").on("click",function(){
	$('#form_pwd').bootstrapValidator('validate',"fields");
	if($('#form_pwd').data('bootstrapValidator').isValid()){
		var data0 = {"USER_ID":$("#uId").val(),"OLD_PASSWORD":$("#oPd").val(),"NEW_PASSWORD":$("#nPd").val()};
		$.ajax({
			type:"post",
			url:baseurl+"lrduser/updatePwd",
			dataType:"json",
			contentType:"application/json",
			data:JSON.stringify(data0),
			success:function(data){
				if(data.code=="00"){					
					$("#myModa0").modal("hide").on("hidden.bs.modal");
				}else if (data.code=="88"){
					eval(data.reLoad);
				}else{
					alert(data.errMsg);
					console.log(data.errMsg);
				}
			},error:function(data){
				console.log("错误");
			}
		}) 
	}
});
//重置密码
$("#btnNewPsw").on("click",function(){
	var uid=$("#operator").find("tr.on").attr("USER_ID");
	if(uid!=''&&uid!=undefined){
		Io.bs.alert("确定重置密码吗？", function(){
			var data2 = {"USER_ID":uid};
			$.ajax({
				type:"post",
				url:baseurl+"lrduser/resetPwd",
				dataType:"json",
				contentType:"application/json",
				data:JSON.stringify(data2),
				success:function(data){
					if(data.code=="00"){
						alert("重置成功");
					}else if (data.code=="88"){
						eval(data.reLoad);
					}else{
						alert(data.errMsg);
					}
				},error:function(data){
					console.log("错误");
				}
			})
		})
	}else{
		alert("请选择一个项目");
	}	
});
$("#form_pwd").bootstrapValidator({
	message:"This value is not valid",
	fields:{
		oPd:{
			validators:{
				notEmpty:{
					message:'原密码不能为空'
				}
			}
		},	
		xPd:{
			validators:{
				notEmpty:{
					message:'新密码不能为空'
				}
			}
		},	
		nPd:{
			validators:{
				notEmpty:{
					message:'确认密码不能为空'
				},
				identical: {
					field: 'xPd',
					message: '两次输入密码不一致'
				}
			}
		}
	}
})
</script>	
</body>

</html>