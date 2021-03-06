<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>功能管理</title>
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<link rel="stylesheet" href="../css/bootstrap.min.css">
<link rel="stylesheet" href="../css/AdminLTE.min.css">
<link rel="stylesheet" href="../css/skins/_all-skins.min.css">
<link rel="stylesheet" href="../css/font-awesome.min.css">
<link rel="stylesheet" href="../css/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="../css/demo.css">
<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/app.min.js"></script>
<script src="../js/jquery.ztree.core-3.5.min.js"></script>
<script src="../js/common.js"></script>
<%@taglib prefix="b" uri="/WEB-INF/lrdButton.tld"%>
</head>
<body class="pbody">
<div class="tree-work">				
		<div class="tree-work1">
			<div class="btn-group btn-tree" id="treeBtn">
				<b:lbutton buttonId="btnAdd" buttonClass="btn btn-primary btn-sm" iClass="fa fa-plus" buttonName="添加" funcCd="010000000105"></b:lbutton>
				<b:lbutton buttonId="btnEdit" buttonClass="btn btn-primary btn-sm" iClass="fa fa-edit" buttonName="修改" funcCd="010000000205"></b:lbutton>
				<b:lbutton buttonId="btnDel" buttonClass="btn btn-primary btn-sm" iClass="fa fa-trash" buttonName="删除" funcCd="010000000305"></b:lbutton>
				
			</div>					
			<div class="box box-solid">
				<div class="box-header with-border">
					<h3 class="box-title">系统列表</h3>
					<div class="box-tools">
						<button class="btn btn-box-tool" data-widget="collapse">
						<i class="fa fa-minus"></i>
						</button>
					</div>
				</div>
				<div class="box-body no-padding">
					<ul class="nav nav-pills nav-stacked" id="sysList">								
					</ul>
				</div>
			</div>
		</div>
		<div class="tree-work2">
			<div class="box box-solid">
				<ul id="tree" class="ztree"></ul>
			</div>
		</div>
	</div>
	<div class="tree-cont">
		<div class="box">
			<div class="box-header with-border">
				<h3 class="box-title" id="treeTit"></h3>
			</div>
			<form class="form-horizontal">
				<div class="box-body">
					<div class="form-group">
						<label class="col-md-2 control-label">系统代码:</label>
						<div class="col-md-8">
							<input class="form-control" type="text" id="SYS_CD" disabled="disabled" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label">功能代号:</label>
						<div class="col-md-8">
							<input class="form-control" type="text" id="FUNC_CD" disabled="disabled" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label">功能名称:</label>
						<div class="col-md-8">
							<input class="form-control" type="text" id="FUNC_NAME" disabled="disabled" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label">显示顺序:</label>
						<div class="col-md-8">
							<input class="form-control" type="text" id="FUNC_NUM" disabled="disabled" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label">功能类型:</label>
						<div class="col-md-8">
							<input class="form-control" type="text" id="FUNC_TYPE" disabled="disabled" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label">上级功能:</label>
						<div class="col-md-8">
							<input class="form-control" type="text" id="FUNC_PCD" disabled="disabled" />
						</div>
					</div>					
					<div class="form-group">
						<label class="col-md-2 control-label">地　　址:</label>
						<div class="col-md-8">
							<input class="form-control" type="text" id="FUNC_URL" disabled="disabled" />
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
<script>
var sysId = getUrlParam("s")||"",
	fucId = getUrlParam("i")||"";
var setting = {
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		beforeClick: function(treeId, treeNode) {
			treeCont(treeNode.id);
			if($("#btnAdd").is(":visible")){
				$("#btnAdd").attr("pid",treeNode.id);
			}
			if($("#btnEdit").is(":visible")){
				$("#btnEdit").attr("zt",treeNode.id);
			}
			if($("#btnDel").is(":visible")){
				$("#btnDel").attr("zt",treeNode.id);
			}
		}
	}
};
function treeWork(id,x){
	var data0 = {"SYS_CD":id};
	$.ajax({
		type:"post",
		url:baseurl+"lrdfunc/querymenu",
		dataType:"json",
		contentType:"application/json",
		data:JSON.stringify(data0),
		success:function(data){
			if(data.code=="00"){
				if(data.result!=null && data.result.length){
					var resultArr=data.result;
					$.fn.zTree.init($("#tree"), setting, resultArr);					
					if(x!=''&&x!=null){
						var treeObj = $.fn.zTree.getZTreeObj("tree");
						var node = treeObj.getNodeByParam("id", x);
						treeObj.selectNode(node);
						treeCont(node.id);
					}					
				}
			}else if (data.code=="88"){
				eval(data.reLoad);
			}else{
				alert(data.errMsg);
			}
		},error:function(data){
			console.log("错误");
		}
	})
}
function treeCont(id){
	var data1 = {"FUNC_CD":id};
	$.ajax({
		type:"post",
		url:baseurl+"lrdfunc/query",
		dataType:"json",
		contentType:"application/json",
		data:JSON.stringify(data1),
		success:function(data){
			if(data.code=="00"){
				if(data.result!=null && data.result.length){
					var result=data.result[0];
					$("#SYS_CD").val(result.SYS_CD);
					$("#FUNC_CD").val(result.FUNC_CD);
					$("#FUNC_NAME").val(result.FUNC_NAME);
					$("#FUNC_NUM").val(result.FUNC_NUM);
					$("#FUNC_TYPE").val(result.FUNC_TYPEC);
					$("#FUNC_PCD").val(result.FUNC_PCD);
					$("#FUNC_URL").val(result.FUNC_URL);
					$("#treeTit").html(result.FUNC_NAME);					
				}
			}else if (data.code=="88"){
				eval(data.reLoad);
			}else{
				alert(data.errMsg);
			}
		},error:function(data){
			console.log("错误");
		}
	})
}

$.ajax({
	type:"post",
	url:baseurl+"lrdsys/querymenu",
	dataType:"json",
	contentType:"application/json",
	success:function(data){
		if(data.code=="00"){
			if(data.result!=null && data.result.length){
				var resultArr=data.result;
				var html='';
				for(var i=0;i<resultArr.length;i++){
					var result = resultArr[i];
					html+= '<li><a href="javascript:;" class="sysBtn" zt="'+result.SYS_CD+'">'+result.SYS_NAME+'</a></li>';
				}
				$("#sysList").html(html);
				if(sysId==""||sysId==null){
					if($("#btnAdd").is(":visible")){
						$("#btnAdd").attr("sid",resultArr[0].SYS_CD);
						$("#btnAdd").attr("pid",'');
					}
					if($("#btnEdit").is(":visible")){
						$("#btnEdit").attr("sid",resultArr[0].SYS_CD);
						$("#btnEdit").attr("zt",'');
					}
					if($("#btnDel").is(":visible")){
						$("#btnDel").attr("sid",resultArr[0].SYS_CD);
						$("#btnDel").attr("zt",'');
					}
					$("#sysList li:first").addClass("active");
					treeWork(resultArr[0].SYS_CD);
				}else{
					if($("#btnAdd").is(":visible")){
						$("#btnAdd").attr("sid",sysId);
						$("#btnAdd").attr("pid",fucId);
					}
					if($("#btnEdit").is(":visible")){
						$("#btnEdit").attr("sid",sysId);
						$("#btnEdit").attr("zt",fucId);
					}
					if($("#btnDel").is(":visible")){
						$("#btnDel").attr("sid",sysId);
						$("#btnDel").attr("zt",fucId);
					}
					$("#sysList a[zt='"+sysId+"']").parent("li").addClass("active");
					treeWork(sysId,fucId);
				}		
			}
		}else if (data.code=="88"){
			eval(data.reLoad);
		}else{
			alert(data.errMsg);
		}
	},error:function(data){
		console.log("错误");
	}
})
Io.on(".sysBtn","click",function(){
	$(this).parent().addClass("active").siblings().removeClass("active");
	var sid = $(this).attr("zt");
	treeWork(sid);
	if($("#btnAdd").is(":visible")){
		$("#btnAdd").attr("sid",sid);
		$("#btnAdd").attr("pid",'');
	}
	if($("#btnEdit").is(":visible")){
		$("#btnEdit").attr("sid",sid);
		$("#btnEdit").attr("zt",'');
	}
	if($("#btnDel").is(":visible")){
		$("#btnDel").attr("sid",sid);
		$("#btnDel").attr("zt",'');
	}
});
Io.on("#btnAdd","click",function(){
	var sid = $(this).attr("sid");
	var pid = $(this).attr("pid");
	location.href="sysFunction_add.html?s="+sid+"&p="+pid;
});
Io.on("#btnEdit","click",function(){
	var sid = $(this).attr("sid");
	var id = $(this).attr("zt");
	if(id!=''){
		location.href="sysFunction_edit.html?s="+sid+"&i="+id;
	}else{
		alert("请选择一个项目");
	}
	
});
Io.on("#btnDel","click",function(){
	var sid = $(this).attr("sid");
	var id = $(this).attr("zt");
	if(id!=''){
		Io.bs.alert("确定删除吗？", function(){
			var data2 = {"FUNC_CD":id};
			$.ajax({
				type:"post",
				url:baseurl+"lrdfunc/delete",
				dataType:"json",
				contentType:"application/json",
				data:JSON.stringify(data2),
				success:function(data){
					if(data.code=="00"){
						treeWork(sid);
						$("#btnAdd").attr("pid",'');
						$("#btnEdit").attr("zt",'');
						$("#btnDel").attr("zt",'');
						$(".form-horizontal input").val('');
						$("#treeTit").html('');	
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
</script>
</body>
</html>