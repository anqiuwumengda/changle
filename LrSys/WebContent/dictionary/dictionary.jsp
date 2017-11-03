<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<title>标准字典</title>
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
<script src="../js/common.js" type="text/javascript" charset="utf-8"></script>
<%@taglib prefix="b" uri="/WEB-INF/lrdButton.tld"%> 
</head>
<body class="pbody">
	<div class="col-md-4 col-xs-4">
		<div class="btn-group btn-tree" id="treeBtn">
				<b:lbutton buttonId="btnAdd" buttonClass="btn btn-primary btn-sm" iClass="fa fa-plus" buttonName="添加" funcCd="010000000106"></b:lbutton>
				<b:lbutton buttonId="btnEdit" buttonClass="btn btn-primary btn-sm" iClass="fa fa-edit" buttonName="修改" funcCd="010000000206"></b:lbutton>
				<b:lbutton buttonId="btnDel" buttonClass="btn btn-primary btn-sm" iClass="fa fa-trash" buttonName="删除" funcCd="010000000306"></b:lbutton>
			</div>	
		<div class="box" style="max-height:450px;overflow-y:auto">
			<ul id="tree" class="ztree"></ul>
		</div>
	</div>
	<div class="col-md-8 col-xs-8">
		<div class="box" style="max-height:491px;overflow-y:auto">
			<div class="box-header with-border">
				<h4 class="box-title"></h4>
			</div>
			<div class="box-body no-padding">
				<table class="table table-bordered table-hover">
				<thead>
					<tr>
						<th>序号</th>
						<th>Code ID</th>
						<th>代码名</th>
						<th>Sort</th>
						<th>中文备注</th>
					</tr>
				</thead>
				<tbody id="dictionary">					
				</tbody>
				</table>
			</div>
		</div>
	</div>
<div id="Modal1" class="modal fade" tabindex="-1" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content modal_content_sm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h5 class="modal-title" id="myModalLabel1">修改字典</h5>
			</div>
			<div id="tB0" class="modal-body clearfix">
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">Code ID：</label>
					<div class="bs-sm-r">
						<input type="text" class="form-control" id="DIC_ID" disabled="disabled">
						<input type="hidden" id="DIC_PID">
					</div>
				</div>
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">代码名：</label>
					<div class="bs-sm-r">
						<input type="text" class="form-control" id="DIC_NAME">
					</div>
				</div>
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">Sort：</label>
					<div class="bs-sm-r">
						<input type="text" class="form-control" id="DIC_SORT">
					</div>
				</div>
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">中文备注：</label>
					<div class="bs-sm-r">
						<input type="text" class="form-control" id="DIC_DESC">
					</div>
				</div>				
			</div>
			<div id="tB1" class="modal-body clearfix">
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">字典名称：</label>
					<div class="bs-sm-r">
						<input type="text" class="form-control" id="name">
						<input type="hidden" id="cid">
						<input type="hidden" id="pid">
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn btn-primary" id="btnsave1">保存</button>
			    <button class="btn btn-default" data-dismiss="modal">返回</button>
			</div>
		</div>
	</div>
</div>
<script>
Io.on("#btnAdd","click",function(){
	var i = $(this).attr("pid");
	var n = $(this).attr("zt");
	location.href="./dictionary_add.html?p="+i+"&n="+n;	
})
var setting = {
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		beforeClick: function(treeId, treeNode) {
			$(".box-title").html(treeNode.name);
			tablelist(treeNode.id);	
			if($("#btnAdd").is(":visible")){
				$("#btnAdd").attr("pid",treeNode.id);
				$("#btnAdd").attr("zt",treeNode.name);
			}
			if($("#btnEdit").is(":visible")){
				$("#btnEdit").attr("zt",treeNode.id);
				$("#btnEdit").attr("pid",treeNode.pid);
				$("#btnEdit").attr("nam",treeNode.name);
			}
			if($("#btnDel").is(":visible")){
				$("#btnDel").attr("pid",treeNode.pid);
				$("#btnDel").attr("zt",treeNode.id);				
			}
		},
		onRightClick: function(treeId, treeNode) {
			$("#tree a").removeClass("curSelectedNode");
			$("#btnAdd").attr("pid","");
			$("#btnAdd").attr("zt","");
			$("#btnEdit").attr("zt","");
			$("#btnEdit").attr("pid","");
			$("#btnEdit").attr("nam","");
			$("#btnDel").attr("zt","");
			$("#btnDel").attr("pid","");
			$("#dictionary").html("");
		}
	}
};
function list(){
	$.ajax({
		type:"post",
		url:baseurl+"slrddic/queryTree",
		dataType:"json",
		contentType:"application/json",
		data:JSON.stringify(),
		success:function(data){
			if(data.code=="00"){	
				var resultArr=data.result;				
				$.fn.zTree.init($("#tree"), setting, resultArr);
			}else{
				alert(data.errMsg);
			}
		}
	})
}
list();
function tablelist(id){
	var data1 = {"pId":id};
	$.ajax({
		type:"post",
		url:baseurl+"slrddic/queryByPK",
		dataType:"json",
		contentType:"application/json",
		data:JSON.stringify(data1),
		success:function(data){
			if(data.code=="00"){
				if(data.result!=null && data.result.length){
					var resultArr=data.result;
					var html="";
					for(var i=0;i<resultArr.length;i++){
						var result = resultArr[i];
						html+='<tr zt="'+ result.DIC_ID +'" nam="'+ result.name +'" sot="'+ result.DIC_SORT +'" des="'+ result.DIC_DESC +'">';
						html+='<td class="text-center">' + (i+1)+ '</td>';						
						html+='<td>' + result.DIC_ID + '</td>';						
						html+='<td>' + result.name + '</td>';
						html+='<td>' + result.DIC_SORT + '</td>';
						html+='<td>' + result.DIC_DESC + '</td>';
						html+='</tr>';
					}
					$("#dictionary").html(html);
				}else{
					$("#dictionary").html("");
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
//选择
Io.on("#dictionary tr","click",function(){
	$(this).addClass("on").siblings().removeClass("on");
})
//修改
Io.on("#btnEdit","click",function(){
	var pId=$(this).attr("pid");
	var sId=$(this).attr("zt");
	var nAm=$(this).attr("nam");
	var cId=$("#dictionary").find("tr.on").attr("zt");
	if(cId!=undefined){
		var nam = $("#dictionary").find("tr.on").attr("nam");
		var sot = $("#dictionary").find("tr.on").attr("sot");
		var des = $("#dictionary").find("tr.on").attr("des");
		$("#tB0").show();
		$("#tB1").hide();
		$("#DIC_ID").val(cId);
		$("#DIC_NAME").val(nam);
		$("#DIC_SORT").val(sot);
		$("#DIC_DESC").val(des);
		$("#DIC_PID").val(sId);	
	}else if(sId!=""&&sId!=null&&sId!=undefined){
		$("#tB1").show();
		$("#tB0").hide();
		$("#cid").val(sId);
		$("#name").val(nAm);
		$("#pid").val(pId);
	}else{
		alert("请选择一个项目");
		return false;
	}
	$("#Modal1").modal({
		backdrop:false
	});		
});
//提交
Io.on("#btnsave1","click",function(){
	if($("#tB0").is(":visible")){
		var data0 = {
			"DIC_ID":$("#DIC_ID").val(),
			"DIC_PARENTID":$("#DIC_PID").val(),
			"DIC_NAME":$("#DIC_NAME").val(),
			"DIC_SORT":$("#DIC_SORT").val(),
			"DIC_DESC":$("#DIC_DESC").val()
		};
	}else{
		var data0 = {
			"DIC_ID":$("#cid").val(),
			"DIC_PARENTID":$("#pid").val(),
			"DIC_NAME":$("#name").val()
		};
	}
	$.ajax({
		type:"post",
		url:baseurl+"slrddic/update",
		dataType:"json",
		contentType:"application/json",
		data:JSON.stringify(data0),
		success:function(data){
			if(data.code=="00"){
				if($("#tB0").is(":visible")){
					list();	
					var treeObj = $.fn.zTree.getZTreeObj("tree");
					var node = treeObj.getNodeByParam("id",$("#DIC_PID").val());
					treeObj.selectNode(node);
					tablelist($("#DIC_PID").val());
				}else{
					list();	
					var treeObj = $.fn.zTree.getZTreeObj("tree");
					var node = treeObj.getNodeByParam("id",$("#cid").val());
					treeObj.selectNode(node);				
				}
				$("#Modal1").find("input").val("");
				$("#Modal1").modal("hide").on("hidden.bs.modal");
				//list();
			}else if (data.code=="88"){
				eval(data.reLoad);
			}else{
				alert(data.errMsg);
			}
		},error:function(data){
			console.log("错误");
		}
	})
});
//删除
Io.on("#btnDel","click",function(){
	var pId=$(this).attr("pid");
	var sId=$(this).attr("zt");
	var cId=$("#dictionary").find("tr.on").attr("zt");	
	if(cId!=undefined){
		var data0 = {"DIC_ID":cId,"DIC_PARENTID":sId};
		var txt = "确定删除吗？";
	}else if(sId!=""&&sId!=null&&sId!=undefined){
		var data0 = {"DIC_ID":sId,"DIC_PARENTID":pId};
		var txt = "确定删除整列吗？";
	}else{
		alert("请选择一个项目");
		return false;
	}
	console.log(data0);
	Io.bs.alert(txt, function(){
		$.ajax({
			type:"post",
			url:baseurl+"slrddic/delete",
			dataType:"json",
			contentType:"application/json",
			data:JSON.stringify(data0),
			success:function(data){
				if(data.code=="00"){
					location.href="./dictionary.jsp";
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
});
</script>
</body>
</html>