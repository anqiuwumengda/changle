<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>机构管理</title>
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
				<b:lbutton buttonId="btnAdd" buttonClass="btn btn-primary btn-sm" iClass="fa fa-plus" buttonName="添加" funcCd="010000000101"></b:lbutton>
				<b:lbutton buttonId="btnEdit" buttonClass="btn btn-primary btn-sm" iClass="fa fa-edit" buttonName="修改" funcCd="010000000201"></b:lbutton>
				<b:lbutton buttonId="btnDel" buttonClass="btn btn-primary btn-sm" iClass="fa fa-trash" buttonName="删除" funcCd="010000000301"></b:lbutton>
				
			</div>					
			<div class="box box-solid">
				<div class="box-header with-border">
					<h3 class="box-title">机构类型</h3>
					<div class="box-tools">
						<button class="btn btn-box-tool" data-widget="collapse">
						<i class="fa fa-minus"></i>
						</button>
					</div>
				</div>
				<div class="box-body no-padding">
					<ul class="nav nav-pills nav-stacked" id="sysList">
						<li><a href="javascript:;" class="sysBtn" zt="00">行政归属</a></li>	
						<li><a href="javascript:;" class="sysBtn" zt="01">报表归属</a></li>	
						<li><a href="javascript:;" class="sysBtn" zt="02">其他隶属</a></li>						
					</ul>
				</div>
			</div>
		</div>
		<div class="tree-work2">
			<div class="box box-solid" id="CC">
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
					<div class="clearfix">
						<div class="col-md-6 form-group">
							<label class="col-md-4 control-label">法人行号:</label>
							<div class="col-md-8 no-padding">
								<input class="form-control" type="text" id="CORP_CD" disabled="disabled" />
							</div>
						</div>
						<div class="col-md-6 form-group">
							<label class="col-md-4 control-label">机构代码:</label>
							<div class="col-md-8 no-padding">
								<input class="form-control" type="text" id="ORG_CD" disabled="disabled" />
							</div>
						</div>
					</div>
					<div class="clearfix">
						<div class="col-md-6 form-group">
							<label class="col-md-4 control-label">机构名称:</label>
							<div class="col-md-8 no-padding">
								<input class="form-control" type="text" id="ORG_NAME" disabled="disabled" />
							</div>
						</div>
						<div class="col-md-6 form-group">
							<label class="col-md-4 control-label">上级机构:</label>
							<div class="col-md-8 no-padding">
								<input class="form-control" type="text" id="ORG_PCD" disabled="disabled" />
							</div>
						</div>
					</div>
					<div class="clearfix">
						<div class="col-md-6 form-group">
							<label class="col-md-4 control-label">成立日期:</label>
							<div class="col-md-8 no-padding">
								<input class="form-control" type="text" id="CRT_DATE" disabled="disabled" />
							</div>
						</div>
						<div class="col-md-6 form-group">
							<label class="col-md-4 control-label">撤销日期:</label>
							<div class="col-md-8 no-padding">
								<input class="form-control" type="text" id="MTN_DATE" disabled="disabled" />
							</div>
						</div>
					</div>
					<div class="clearfix">
						<div class="col-md-6 form-group">
							<label class="col-md-4 control-label">状　　态:</label>
							<div class="col-md-8 no-padding">
								<input class="form-control" type="text" id="ORG_FLAG" disabled="disabled" />
							</div>
						</div>
						<div class="col-md-6 form-group">
							<label class="col-md-4 control-label">是否虚设:</label>
							<div class="col-md-8 no-padding">
								<input class="form-control" type="text" id="ORG_VFLAG" disabled="disabled" />
							</div>
						</div>
					</div>
					<div class="clearfix">
						<div class="col-md-6 form-group">
							<label class="col-md-4 control-label">机构简称:</label>
							<div class="col-md-8 no-padding">
								<input class="form-control" type="text" id="ORG_ABB" disabled="disabled" />
							</div>
						</div>
						<div class="col-md-6 form-group">
							<label class="col-md-4 control-label">说　　明:</label>
							<div class="col-md-8 no-padding">
								<input class="form-control" type="text" id="ORG_DESC" disabled="disabled" />
							</div>
						</div>
						<div class="col-md-6 form-group">
							<label class="col-md-4 control-label">网点代码:</label>
							<div class="col-md-8 no-padding">
								<input class="form-control" type="text" id="WD_ID" disabled="disabled" />
							</div>
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
	var data0 = {"ORG_RELA_TYPE":id};
	$.ajax({
		type:"post",
		url:baseurl+"lrdorg/querybyrela",
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
	var data1 = {"ORG_CD":id};
	$.ajax({
		type:"post",
		url:baseurl+"lrdorg/query",
		dataType:"json",
		contentType:"application/json",
		data:JSON.stringify(data1),
		success:function(data){
			if(data.code=="00"){
				if(data.result!=null && data.result.length){
					var result=data.result[0];
					$("#CORP_CD").val(result.CORP_CD);
					$("#ORG_CD").val(result.ORG_CD);
					$("#ORG_NAME").val(result.ORG_NAME);
					$("#ORG_PCD").val(result.ORG_PCD);
					$("#ORG_FLAG").val(result.ORG_FLAGC);
					$("#ORG_VFLAG").val(result.ORG_VFLAGC);
					$("#ORG_ABB").val(result.ORG_ABB);
					$("#ORG_DESC").val(result.ORG_DESC);
					$("#CRT_DATE").val(result.CRT_DATE);
					$("#MTN_DATE").val(result.MTN_DATE);
					$("#treeTit").html(result.ORG_NAME);
					$("#WD_ID").val(result.WD_ID);//网点代码
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
if(sysId!=''&&sysId!=null){
	$("#sysList a[zt='"+sysId+"']").parent("li").addClass("active");
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
	treeWork(sysId,fucId);
}
	
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
	location.href="department_add.html?s="+sid+"&p="+pid;
});
Io.on("#btnEdit","click",function(){
	var sid = $(this).attr("sid");
	var id = $(this).attr("zt");
	if(id!=''){
		location.href="department_edit.html?s="+sid+"&i="+id;
	}else{
		alert("请选择一个项目");
	}	
});
Io.on("#btnDel","click",function(){
	var sid = $(this).attr("sid");
	var id = $(this).attr("zt");
	if(id!=''){
		Io.bs.alert("确定删除吗？", function(){
			var data2 = {"ORG_CD":id};
			$.ajax({
				type:"post",
				url:baseurl+"lrdorg/delete",
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