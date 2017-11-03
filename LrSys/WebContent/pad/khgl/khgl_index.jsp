<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@taglib prefix="b" uri="/WEB-INF/lrdButton.tld"%> 
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no">
<title>客户管理</title>
<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/bootstrap-datepicker.min.js"></script>
<script src="../../js/jquery.ztree.core-3.5.min.js"></script>
<script src="../js/fastclick.js"></script>
<script src="../js/showPages.js"></script>
<script src="../js/myscript.js"></script>
<link rel="stylesheet" href="../css/bootstrap.min.css">
<link rel="stylesheet" href="../../css/AdminLTE.min.css">
<link rel="stylesheet" href="../../css/skins/_all-skins.min.css">
<link rel="stylesheet" href="../../css/font-awesome.min.css">
<link rel="stylesheet" href="../css/bootstrap-datepicker3.min.css">
<link rel="stylesheet" href="../../css/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="../css/pages.css">
<script>
$(function() {  
    FastClick.attach(document.body);  
});  
</script>
<style>
#handover_search{
    position: relative;
    float: right;
    margin-top: -28px;
    right: 10px;
}
</style>
</head>
<body class="page-bg">
<div class="col-md-3 no-padding pr10" id="isL">
	<div class="box" id="left" style="overflow-y:auto;">		
		<div class="box-body no-padding">
			<ul id="tree" class="ztree"></ul>
		</div>
	</div>
</div>
<div class="col-md-9 no-padding" id="isR">
	<div class="box box-solid no-margin">
		<div class="box-body">
			<div class="input-group input-group-sm col-xs-5" style="height:auto;margin:0 !important;">
				<input class="form-control" type="text" name="keyword" id="keyword" placeholder="客户名/电话/证件号">
				<span class="input-group-btn">
					<button class="btn btn-primary btn-flat" type="button" id="custSearchBtn">检索</button>
				</span>
			</div>
			<p id="info" class="_info"></p>
		</div>
	</div>
	<div class="clearfix tab-header">
		<b:lbutton buttonId="newport" buttonClass="btn btn-danger btn-sm" iClass="glyphicon glyphicon-plus" buttonName="新增" funcCd="010000000209"></b:lbutton>
		<button class="btn btn-primary btn-sm" type="button" id="search_conditions">
			<i class="glyphicon glyphicon-zoom-out"></i> 筛选
		</button>
		<button class="btn btn-primary btn-sm" type="button" id="myapply">
			<i class="glyphicon glyphicon-log-out"></i> 审核
		</button>
		<button class="btn btn-info btn-sm" type="button" id="handover">
		<i class="glyphicon glyphicon-list-alt"></i> 移交详情
		</button>
		<!--<button class="btn btn-info btn-sm" type="button" id="remind">
		<i class="glyphicon glyphicon-bell"></i> 批量提醒
		</button>-->
		<b:lbutton buttonId="btnDel" buttonClass="btn btn-danger btn-sm" iClass="glyphicon glyphicon-trash" buttonName="删除" funcCd="010000000109"></b:lbutton>
	</div>
	<div class="table_wrapper box">
		<table class="table table-bordered table-striped table-hover" id="example">
			<thead>
				<tr>
					<th>客户名称</th>
					<th>电话号码</th>
					<th id="sort_type" up-down="off">客户类型</th>
					<th id="sort_date" up-down="on">数据更新日期</th>
					<th id="sort_jl" up-down="off">客户经理分类</th>
					<th>管户经理</th>
					<th style="min-width:93px;">操作</th>
				</tr>
			</thead>
			<tbody id="listbody">
				<tr><td align="center" height="300" colspan="7">暂时没有内容</td></tr>
			</tbody>
		</table>
	</div>
	<ul class="pagination pagination-sm" id="pagination"></ul>
</div>
<form class="form-horizontal">
<!-- 弹窗-->
<div id="myModal" class="modal fade" tabindex="-1" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content modal_content_sm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h5 class="modal-title" >按条件筛选</h5>
			</div>
			<div class="modal-body clearfix">
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">客户类型：</label>
					<div class="bs-sm-r">
						<select id="CUST_TYPE" class="form-control" name="CUST_TYPE" >							
						</select>
					</div>
				</div>
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">所属区域：</label>
					<div class="bs-sm-r">
						<select id="AREA_CD" class="form-control" style="width:48%;float:left;">							
						</select> 
						<select id="AREA_CD2" class="form-control" style="width:48%;float:right;">
							<option value="">-请选择-</option>							
						</select>
					</div>
				</div>
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">所属客户群：</label>
					<div class="bs-sm-r">
						<select id="CUST_GRP" class="form-control" style="width:48%;float:left;">							
						</select> 
						<select id="CUST_GRP2" class="form-control" style="width:48%;float:right;">		
							<option value="">-请选择-</option>					
						</select>
					</div>
				</div>
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">管户经理分类：</label>
					<div class="bs-sm-r">
						<select id="JL_TYPE" class="form-control" name="JL_TYPE" >							
						</select>
					</div>
				</div>
				
			</div>
			<div class="modal-footer">
				<button class="btn btn-primary" type="button" id="go_search">搜索</button>
			    <button class="btn btn-default" data-dismiss="modal">返回</button>
			</div>
		</div>
	</div>
</div>
<!-- 弹窗modal 1-->
<div id="myModal1" class="modal fade" tabindex="-1" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content modal_content_sm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h5 class="modal-title" >申请查看客户(<span id='kehu'></span>)</h5>
			</div>
			<div class="modal-body clearfix">
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">授权人：</label>
					<div class="bs-sm-r">
						<input type="text" class="form-control" Disabled id="guanhuJL">
					</div>
				</div>
				<div class="clearfix bs-form-group">
					<div class="bs-sm-r">
						<input type="hidden" class="form-control"  id="USER_ID_SQ">
					</div>
				</div>
				<div class="clearfix bs-form-group">
					<div class="bs-sm-r">
						<input type="hidden" class="form-control"  id="CUST_ID">
					</div>
				</div>
				<div class="clearfix">
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">查看时效：</label>
					<div class="bs-sm-r">
						<input type="text" class="form-control" value="3" id ="VALID_DAY" Disabled>				
					</div>
				</div></div>
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">申请状态：</label>
					<div class="bs-sm-r checkbox">			
						<label><input type="radio" name="state" value="0" checked>待审核</label>
						<label><input type="radio" name="state" value="1"disabled>审核中</label>
						<label><input type="radio" name="state" value="2" disabled>已审核</label>
					</div>
				</div>			
			</div>
			<div class="modal-footer">
				<button class="btn btn-primary" id="apply">提交</button>
			    <button class="btn btn-default" data-dismiss="modal">返回</button>
			</div>
		</div>
	</div>
</div>
<!-- 移交弹窗modal 2-->
<div id="myModal2" class="modal fade" tabindex="-1" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content modal_content_sm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h5 class="modal-title" >移交客户</h5>
			</div>
			<div class="modal-body clearfix">
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">移交客户：</label>
					<div class="bs-sm-r">
						<input type="text" class="form-control" Disabled id="handover_CUST_ID">
					</div>
				</div>
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">接收人：</label>
					<div class="bs-sm-r">
						<input id="handover_USER_ID_SQ" type="text" class="form-control" disabled="disabled">
			      		<span id="handover_search" class="form-group-addon">
			      			<i class="glyphicon glyphicon-search"></i>
			      		</span>
			      		<div class="none" id="HO" style="max-height:150px; overflow-y:scroll;">
							<ul id="tree1" class="ztree"></ul>
						</div>
					</div>
				</div>
				<div class="clearfix bs-form-group">
				<label class="bs-sm-l control-label">移交人：</label>
					<div class="bs-sm-r">
						<input type="text" class="form-control" Disabled id="handover_USER_ID">
					</div>
				</div>
				<div class="clearfix bs-form-group none">
					<label class="bs-sm-l control-label">移交状态：</label>
					<div class="bs-sm-r checkbox">			
						<label><input type="radio" name="handover_state" value="0" checked>待接收</label>
						<label><input type="radio" name="handover_state" value="1"disabled>已接收</label>
					</div>
				</div>			
			</div>
			<div class="modal-footer">
				<button class="btn btn-primary" id="handover_Save">提交</button>
			    <button class="btn btn-default" data-dismiss="modal">返回</button>
			</div>
		</div>
	</div>
</div>
</form>
<script>
var rosterJOSN = {};
var URL = "custmgtree/queryList";
var setting = {
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		beforeClick: function(treeId, treeNode) {
			URL = "custmgtree/queryList";
			if(treeNode.C_ID){
				rosterJOSN = {"C_ID":treeNode.C_ID,"JL_ID":treeNode.JL_ID,"ZH_ID":treeNode.ZH_ID};
				pagerList(1,10);
			}
			else if(treeNode.JL_ID){
				rosterJOSN = {"JL_ID":treeNode.JL_ID,"ZH_ID":treeNode.ZH_ID};
				pagerList(1,10);
			}else if(treeNode.ZH_ID){
				rosterJOSN = {"ZH_ID":treeNode.ZH_ID};
				pagerList(1,10);
			}
			
		}
	}
};
$.ajax({
	type:"post",
	url:baseurl+"custmgtree/queryTree",
	dataType:"json",
	contentType:"application/json",
	success:function(data){
		if(data.code=="00"){
			if(data.result!=null && data.result.length){
				$.fn.zTree.init($("#tree"), setting, data.result);
			}else{
				$("#isL").hide();
				$("#isR").removeClass();
			}		
		}else if (data.code=="88"){
			eval(data.reLoad);
		}else{
			alert(data.errMsg);
			}
	},error:function(data){
		alert("错误");
		console.log(data);
	}
});

</script>
<script>
function pageLoad(resultArr){
	var html="";
	for(var i=0;i<resultArr.length;i++){
		var result = resultArr[i];
		var jlTypec=result.JL_TYPEC||"";
		var telNo=result.TEL_NO;
		if(result.CUST_TYPE=="XWQY"){
			telNo=result.LXR_TEL;
		}
		var btnbj ="编辑";
		if(result.OTHER){btnbj="申请";}
		html+='<tr class="btSee" zt="'+result.CUST_ID+'">';
		html+='<td>' + result.CUST_NAME + '</td>';
		html+='<td>' + telNo + '</td>';
		html+='<td>' + result.CUST_TYPEC + '</td>';
		html+='<td>' + result.MTN_DATE + '</td>';
		html+='<td>' + jlTypec + '</td>';
		html+='<td>' + result.CUST_GRP_JLC + '</td>';
		html+='<td>';
		html+='<button mz="'+result.CUST_NAME+'"JLID="'+result.CUST_GRP_JL+'" khid="'+result.CUST_ID+'" yt="'+result.CUST_GRP_JLC+'" class="btn btn-danger btn-xs" type="button" name="btnEdit" >'+btnbj+'</button> ';
		if(URL =="custmgtree/queryList"){
			html+='<button mz="'+result.CUST_NAME+'"JLID="'+result.CUST_GRP_JL+'" khid="'+result.CUST_ID+'" JLIDC="'+result.CUST_GRP_JLC+'" class="btn btn-danger btn-xs" type="button" name="handover" >移交</button>';
		}
		html+='</td>';
		html+='</tr>';
	}
	return html;
}
</script>
<script id="列表模板">
function pagerList(currentPage,pageSize){
	 var data1 = {
		 "isPagination":"true",
		 "currentPage":currentPage,
		 "pageSize":pageSize
	 };
	 var data2 = rosterJOSN;
	 $.extend(data2,data1);
	 if(typeof(data)=="object") $.extend(data2,data);
	 $.ajax({
		beforeSend:function(){
			$("#listbody").html("<tr><td colspan='7'>正在加载...</td></tr>");
		},
		url:baseurl + URL ,
		type:"post",
		dataType:"json",
		contentType:"application/json",
		data:JSON.stringify(data2),
		success:function(data){
			if(data.code=="00"){
				if(data.result!=null && data.result.length){
					var resultArr=data.result;
					Io.on("#sort_type","click",function(){//按客户类型排序
						if($(this).attr("up-down")=="off"){
							$(this).attr("up-down","on");
							for(var i=0;i<resultArr.length;i++){
								for(var j=i;j<resultArr.length;j++){
									if(resultArr[i].CUST_TYPEC>resultArr[j].CUST_TYPEC){
										var x = resultArr[i];
										resultArr[i]=resultArr[j];
										resultArr[j] = x;										
									}
								}
							}
							$("#listbody").html(pageLoad(resultArr));
						}else{
							$("#listbody").html(pageLoad(resultArr.reverse()));
						}
						$("#sort_jl").attr("up-down","off");
						$("#sort_date").attr("up-down","off");
					});
					Io.on("#sort_date","click",function(){//按时间排序
						if($(this).attr("up-down")=="off"){
							$(this).attr("up-down","on");
							for(var i=0;i<resultArr.length;i++){
								for(var j=i;j<resultArr.length;j++){
									if(resultArr[i].MTN_DATE>resultArr[j].MTN_DATE){
										var x = resultArr[i];
										resultArr[i]=resultArr[j];
										resultArr[j] = x;										
									}
								}
							}
							$("#listbody").html(pageLoad(resultArr));
						}else{
							$("#listbody").html(pageLoad(resultArr.reverse()));
						}
						$("#sort_jl").attr("up-down","off");
						$("#sort_type").attr("up-down","off");
					})
					Io.on("#sort_jl","click",function(){//按经理类别排序
						if($(this).attr("up-down")=="off"){
							$(this).attr("up-down","on");
							for(var i=0;i<resultArr.length;i++){
								for(var j=i;j<resultArr.length;j++){
									if(resultArr[i].JL_TYPEC>resultArr[j].JL_TYPEC){
										var x = resultArr[i];
										resultArr[i]=resultArr[j];
										resultArr[j] = x;										
									}
								}
							}
							$("#listbody").html(pageLoad(resultArr));
						}else{
							$("#listbody").html(pageLoad(resultArr.reverse()));
						}
						$("#sort_type").attr("up-down","off");
						$("#sort_date").attr("up-down","off");
					});
					if(URL =="custmgtree/queryList"){						
						$("#info").html("已采集户数:"+data.page.totalRows);
					}else{
						$("#info").html("");
					}
					$("#listbody").html(pageLoad(resultArr));
					$.showPages('pagination',data.page.totalRows,pageSize,currentPage,'pagerList');
					iFrameHeight('myTabsFrame');
				}else{
					$("#info").html("");
					$("#listbody").html('<tr><td align="center" height="200" colspan="7">暂时没有内容</td></tr>');
				}
			}else if (data.code=="88"){
				eval(data.reLoad);
			}else{
				alert(data.errMsg);
				$("#listbody").html('<tr><td align="center" height="200" colspan="7">'+data.errMsg+'</td></tr>');
			}
		},error:function(data){
			console.log("错误");
		}
	});
}
pagerList(1,10);
$("#custSearchBtn").on("click",function(){
	var keyword = $("#keyword").val();
	URL = "custbase/querybylike";
	data = {};
	rosterJOSN = {"keyword":keyword};
	pagerList(1,10);
});
</script>	
<script>
//查看&编辑
Io.on("[name=btnEdit]","click",function(){
	var id =$(this).parent().parent().attr("zt");
	if($(this).html()=="编辑"){
		location.href="kehuguanli_edit.html?cust_id="+id;
	}else{
		$("#kehu").html( $(this).attr("mz"));
		$("#guanhuJL").val( $(this).attr("yt"));
		$("#CUST_ID").val( $(this).attr("khid"));
		$("#USER_ID_SQ").val( $(this).attr("JLID"));
		$("#myModal1").modal({
			backdrop:false
		})
	}
	
})
var ClickNum = 0;
Io.on(".btSee","click",function(){
	var id = $(this).attr("zt");
	var btn=$(this).find('button').html();
	ClickNum++;
	setTimeout(function(){ClickNum=0;},500);
	if(ClickNum==2){
		if(btn=="编辑"){
			location.href="kehuguanli_see.html?cust_id="+id;
		}else{
			$("#kehu").html( $(this).find('button').attr("mz"));
			$("#guanhuJL").val( $(this).find('button').attr("yt"));
			$("#CUST_ID").val( $(this).find('button').attr("khid"));
			$("#USER_ID_SQ").val( $(this).find('button').attr("JLID"));
			$("#myModal1").modal({
				backdrop:false
			})
		}
	}
	
})

//新增客户
$("#newport").on("click",function(){
	location.href="../khgl/kehuguanli_add.html";
})
//审核跳转
$("#myapply").on("click",function(){
	location.href="./apply.html";
})
//筛选弹出+字典加载
$("#search_conditions").on("click",function(){
	$("#myModal").modal({
		backdrop:false
	})
	var json={//(字典枚举)
		"CUST_TYPE":"CUST_TYPE",//客户类型
		"AREA_CD":"AREA_CD1",//所属区域
		"CUST_GRP":"CUST_GRP1",//所属客户群		
		"JL_TYPE":"JL_TYPE"//客户经理分类
	}
	selectOnload({"json":json,"isDefault":false,"isElse":false,"func":cBack,"length":4});
});
function cBack(){
	if(typeof(data)=="object"){
		for(var x in data){
			$("#"+x).val(data[x]);
		}	
	}	
}
//筛选
$("#go_search").on("click",function(){
	data={
			"CUST_TYPE":$("#CUST_TYPE").val(),//客户类型
			"AREA_CD":$("#AREA_CD").val(),//所属区域
			"AREA_CD2":$("#AREA_CD2").val(),//所属区域2
			"CUST_GRP":$("#CUST_GRP").val(),//所属客户群
			"CUST_GRP2":$("#CUST_GRP2").val(),//所属客户群2
			"JL_TYPE ":$("#JL_TYPE").val()//客户经理分类
	};
	URL = "custmgtree/queryList";
	pagerList(1,10);
	$("#myModal").modal("hide").on("hidden.bs.modal");
				
})
</script>
<script type="text/javascript"id="申请提交">
	$("#apply").on("click",function(){
		var data={
			"USER_ID_SQ":$("#USER_ID_SQ").val(),//授权人
			"CUST_ID":$("#CUST_ID").val(),//客户编号
			"VALID_DAY":$("#VALID_DAY").val(),//查看时效
			"STAT":$("input[name=state]:checked").val()//状态
		};
		$.ajax({
			type:"post",
			url:baseurl+"custapply/save",
			dataType:"json",
			async:false,
			contentType:"application/json",
			data:JSON.stringify(data),
			success:function(data){
				if(data.code=="00"){
					alert("申请成功")
					$("#Modal1").modal("hide").on("hidden.bs.modal",function(){});					
				}else if(data.code=="01"){
					alert('正在审核中');
				}else if (data.code=="88"){
					eval(data.reLoad);
				}else{
					alert(data.errMsg);
					}
			},error:function(data){
				alert("错误");
				console.log(data);
			}
		})
	})
</script>
<script id="删除">
Io.on("#listbody>tr","click",function(){
	var $this=$(this);
	$this.addClass("on").siblings().removeClass("on");
})
$("#btnDel").on("click",function(){
	var $on=$("#listbody").find("tr.on");
	var cust_id=$on.attr("zt");
	if($on&&$on.size()>0){
		Io.bs.alert("此操作将删除该客户所有信息，确定删除？",function(){
			//$(".bsok").on("click",function(){
				var data1={"CUST_ID":cust_id};
				$.ajax({
					type:"post",
					url:baseurl+"custbase/deleteAll",
					dataType:"json",
					async:false,
					contentType:"application/json",
					data:JSON.stringify(data1),
					success:function(data){
						if(data.code=="00"){
							alert("删除成功");
							pagerList(1,10);
						}else if(data.code=="88"){
							eval(data.reLoad);
						}else{
							alert(data.errMsg);
						}
					}
				});
			//})
		});
	}else{
		alert("请选择要删除的客户");
	}
});

function getHl(){
	var bHeight = $("body").height();
	var ht = bHeight;
	$("#left").height(ht);
}
$(getHl);
window.addEventListener("orientationchange",getHl, false);
</script>
<script type="text/javascript"id="移交">
//移交弹出
Io.on("[name=handover]","click",function(){
		$("#handover_CUST_ID").val( $(this).attr("mz")).attr('handover_cust_id',$(this).attr("khid"));
		$("#handover_USER_ID").val( $(this).attr("JLIDC")).attr('handover_user_id',$(this).attr("JLID"));
		$("#myModal2").modal({
			backdrop:false
		});
	
});
//移交详情跳转
$("#handover").on("click",function(){
	location.href="./khgl_handover.html";
});
//移交——树加载
var setting2 = {
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			beforeClick: function(treeId, treeNode) {
				var zTree = $.fn.zTree.getZTreeObj("tree");
				if (treeNode.isParent) {
					return false;
				} else {
					$("#handover_USER_ID_SQ").val(treeNode.name).attr("handover_user_id_sq",treeNode.id);
					$("#HO").toggleClass("none");
					return true;
				}
				
			}
		}
	};
//Io.on("#handover_search","click",function(){
	$.ajax({
		type:"post",
		url:baseurl+"custyj/custYjTree",
		dataType:"json",
		contentType:"application/json",
		data:JSON.stringify(),
		success:function(data){
			if(data.code=="00"){
				$.fn.zTree.init($("#tree1"), setting2, data.result);
			}else if(data.code=="88"){
				eval(data.reLoad);
			}else{
				alert(data.errMsg);
			}
		}
	});
Io.on("#handover_search","click",function(){	
		//$("#HO").show();
		$("#HO").toggleClass("none");
});
//移交——提交
Io.on("#handover_Save","click",function(){
	var data1={
			"CUST_ID":$("#handover_CUST_ID").attr("handover_cust_id"),//客户ID
			"USER_ID_SQ":$("#handover_USER_ID_SQ").attr("handover_user_id_sq"),//接收经理
			"STAT":$("input[name=handover_state]:checked").val()//状态
			};
	console.log(data1);
	$.ajax({
		type:"post",
		url:baseurl+"custyj/custYj",
		dataType:"json",
		async:'false',
		contentType:"application/json",
		data:JSON.stringify(data1),
		success:function(data){
			if(data.code=="00"){
				alert("成功");
				$("#myModal1").modal("hide");
				$("#handover_CUST_ID").val("").attr("handover_cust_id","");
				$("#handover_USER_ID_SQ").val("").attr("handover_user_id_sq","");
				$("#HO").hide();
			}else if(data.code=="88"){
				eval(data.reLoad);
			}else{
				alert(data.errMsg);
			}
		}
	});
});

Io.on("#remind","click",function(){
	location.href="khgl_remind.html";
});

</script>
</body>
</html>