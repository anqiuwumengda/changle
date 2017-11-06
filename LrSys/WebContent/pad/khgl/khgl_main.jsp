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
<script src="../js/fastclick.js"></script>
<script src="../js/showPages.js"></script>
<script src="../js/myscript.js"></script>
<script src="../../js/common.js"></script>
<script src="../../js/jquery.ztree.core-3.5.min.js"></script>
<link rel="stylesheet" href="../css/bootstrap.min.css">
<link rel="stylesheet" href="../css/bootstrap-datepicker3.min.css">
<link rel="stylesheet" href="../css/pages.css">
<link rel="stylesheet" href="../../css/zTreeStyle/zTreeStyle.css">
<script>
$(function() {  
    FastClick.attach(document.body);  
});  
</script>
<style>
.search{
    position: relative;
    float: right;
    margin-top: -28px;
    right: 10px;
}
</style>
</head>
<body class="page-bg">
<div class="white-bg mb50">
<div class="clearfix tab-header" style="padding-bottom:0;">
	<input class="form-control left" name="keyword"id="keyword" style="width:268px;" placeholder="客户名/电话/证件号">
	<a href="javascript:;" class="btn btn-primary ml10 left" id="custSearchBtn">检索</a>
</div>
<div class="clearfix tab-header" style="float: left;">
	<b:lbutton buttonId="newport" buttonClass="btn btn-danger btn-sm" iClass="glyphicon glyphicon-plus" buttonName="新增" funcCd="010000000209"></b:lbutton>
	<button class="btn btn-primary btn-sm" type="button" id="search_conditions">
		<i class="glyphicon glyphicon-zoom-out"></i> 筛选
	</button>
	<b:lbutton buttonClass="btn btn-info btn-sm" iClass="glyphicon glyphicon-list-alt" buttonId="myapply" buttonName=" 审核" funcCd="010000000409"></b:lbutton>
	<b:lbutton buttonClass="btn btn-info btn-sm" iClass="glyphicon glyphicon-list-alt" buttonId="handover" buttonName=" 移交详情" funcCd="010000000509"></b:lbutton>	
	<b:lbutton buttonId="btnDel" buttonClass="btn btn-danger btn-sm" iClass="glyphicon glyphicon-trash" buttonName="删除" funcCd="010000000109"></b:lbutton>
	<b:lbutton buttonId="PhysicallyDel" buttonClass="btn btn-danger btn-sm" iClass="glyphicon glyphicon-remove" buttonName="&nbsp;物理删除" funcCd="010000000110"></b:lbutton>
	<b:lbutton buttonId="bSHOW" buttonClass="collapse" funcCd="010000000309"></b:lbutton>
	
</div>
<form method="get" id="documentForm" class="clearfix tab-header">
        <input id="exportToExcel" class="btn btn-primary btn-sm" type="button" value="导出数据"/>
        <input id="exportParams" type="hidden" name="exportParams"/>
    </form>
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
				
				
				<!-- 添加一个测试 -->
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">评级结果：</label>
					<div class="bs-sm-r">
						<select id="PJ_JB" class="form-control" name="PJ_JB" >							
						</select>
					</div>
				</div>
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">授信额度区间：</label>
					<div class="bs-sm-r">
						<input type="number" id="ED_LMT_START" class="form-control" placeholder="单位：万元"
						 onchange="checkFieldStart(this)" style="width:48%;float:left;">							
						 
						<input  type="number" id="ED_LMT_END" class="form-control" placeholder="单位：万元"
						onchange="checkFieldEnd(this)" style="width:48%;float:right;">		
					</div>
				</div>
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">录入日期区间：</label>
					<div class="bs-sm-r">
						<input type="text" id="CRT_DATE_START" class="form-control" 
						onclick="datePickerFunc(this)" style="width:48%;float:left;" >							
						 
						<input  type="text" id="CRT_DATE_END" class="form-control" 
						onclick="datePickerFunc(this)" style="width:48%;float:right;" >		
					</div>
				</div>
				<div class="clearfix bs-form-group" id="ORG_CD_SELECT_DIV">
					<label class="bs-sm-l control-label">所属机构：</label>
					<div class="bs-sm-r">
						<select id="ORG_CD_SELECT" class="form-control" name="ORG_CD_SELECT" style="width:35%;float:left;">							
						</select>
						<label class="bs-sm-l control-label">客户经理：</label>
						<select id="USER_ID_SELECT" class="form-control" name="USER_ID_SELECT" style="width:35%;float:left;">							
						</select>
					</div>
				</div>
				
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">拜访日期区间：</label>
					<div class="bs-sm-r">
						<input type="text" id="BF_CRT_DATE_START" class="form-control" 
						onclick="datePickerFunc(this)" style="width:48%;float:left;" >							
						 
						<input  type="text" id="BF_CRT_DATE_END" class="form-control" 
						onclick="datePickerFunc(this)" style="width:48%;float:right;" >		
					</div>
				</div>
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">拜访次数：</label>
					<div class="bs-sm-r">
						<input type="number" id="BF_COUNT" class="form-control" placeholder="单位：次"
						 style="width:48%;float:left;">							
					</div>
				</div>
				<div class="clearfix bs-form-group" id="HF_DIV">
					<label class="bs-sm-l control-label">回访：</label>
					<div class="bs-sm-r">
						<select id="ISHF" class="form-control" name="ISHF" style="width:35%;float:left;">							
						</select>
						<label class="bs-sm-l control-label">回访结果：</label>
						<select id="HF_RESULT" class="form-control" name="HF_RESULT" style="width:35%;float:left;">							
							</select>
					</div>
					
				</div>
				<div class="clearfix bs-form-group" id="HF_DATE_DIV">
					<label class="bs-sm-l control-label">回访日期区间：</label>
					<div class="bs-sm-r">
						<input type="text" id="HF_DATE_START" class="form-control" 
						onclick="datePickerFunc(this)" style="width:48%;float:left;" >							
						 
						<input  type="text" id="HF_DATE_END" class="form-control" 
						onclick="datePickerFunc(this)" style="width:48%;float:right;" >		
					</div>
				</div>
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label">所属村：</label>
					<div class="bs-sm-r">
					<input id="AREA_CD" type="text" class="form-control" disabled="disabled">
			      		<span id="cunzi_search" class="form-group-addon">
			      			<i class="glyphicon glyphicon-search search"></i>
			      		</span>
			      		<div class="none" id="cunzi" style="max-height:150px; overflow-y:scroll;">
							<ul id="tree3" class="ztree"></ul>
						</div>
						<!--  <select id="AREA_CD" class="form-control" style="width:48%;float:left;">							
						</select> 
						<select id="AREA_CD2" class="form-control" style="width:48%;float:right;">
							<option value="">-请选择-</option>							
						</select>-->
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
						<select id="JL_TYPE" class="form-control" name="JL_TYPE" style="width:35%;float:left;">							
						</select>
						<label class="bs-sm-l control-label">状态标记：</label>
						<select id="ZTBJ" class="form-control" name="ZTBJ" style="width:35%;float:left;">							
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
					<label class="bs-sm-l control-label">查看时效(天)：</label>
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
			      		<span id="handover_search" class="form-group-addon search">
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
				<div class="clearfix none bs-form-group">
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
<div class="table_wrapper">
<div>
	<span style="float:right;margin-right: 10px;">单位：万元</span>
</div>
<table class="table table-bordered table-striped table-hover" id="example">
<thead>
	<tr>
		<th>所属机构</th>
		<th>客户名称</th>
		<th>电话号码</th>
		<th id="sort_type" up-down="off">客户类型</th>
		<th id="sort_date" up-down="on">数据更新日期</th>
		<!-- <th id="sort_jl" up-down="off">客户经理分类</th> -->
		 <th id="sort_jl" up-down="off">状态标记</th>
		 <th>最高额度</th> 
		 <th>信用额度</th> 
		 <th>亲情额度</th> 
		<th>管户经理</th>
		<th style="width:10%;" id="showb">操作</th>
	</tr>
</thead>
<tbody id="listbody">
	<tr><td align="center" height="300" colspan="9">暂时没有内容</td></tr>
</tbody>
</table>
</div>
<ul class="pagination pagination-sm" id="pagination">
	<li class="prev"><a href="javascript:;">← 上一页</a></li>
	<li class="active"><a href="javascript:;">1</a></li>
	<li class="next"><a href="javascript:;">下一页 →</a></li>
</ul>
<script>
var swc = getUrlParam("swc")||"";
if(swc!="1"){
	localStorage.page = "1";
	localStorage.num = "10";
	localStorage.url = "custmgtree/queryList";
	localStorage.CUST_TYPE = "";
	localStorage.C_ID = "";
	localStorage.C_NAME = "";
	localStorage.CUST_GRP = "";
	localStorage.CUST_GRP2 = "";
	localStorage.JL_TYPE = "";
	localStorage.ZTBJ = "";
	localStorage.keyword = "";
	//新增条件
	localStorage.PJ_JB = "";
	localStorage.ED_LMT_START = "";
	localStorage.ED_LMT_END = "";
	localStorage.CRT_DATE_START = "";
	localStorage.CRT_DATE_END = "";
	localStorage.BF_CRT_DATE_START = "";
	localStorage.BF_CRT_DATE_END = "";
	localStorage.BF_COUNT = "";
	localStorage.ISHF = "";
	localStorage.HF_DATE_START = "";
	localStorage.HF_DATE_END = "";
	localStorage.HF_RESULT = "";
	$("#keyword").val("");
}

function pageLoad(resultArr){
	var html="";
	for(var i=0;i<resultArr.length;i++){
		var result = resultArr[i];
		//var jlTypec=result.JL_TYPEC||"";
		var telNo=result.TEL_NO;
		if(result.CUST_TYPE=="XWQY"){
			telNo=result.LXR_TEL;
		}
		var btnbj ="编辑";
		if(result.OTHER){btnbj="申请";}
		html+='<tr class="btSee" zt="'+result.CUST_ID+'">';
		html+='<td>' + result.ORG_ABB + '</td>';
		html+='<td>' + result.CUST_NAME + '</td>';
		html+='<td>' + telNo + '</td>';
		html+='<td>' + result.CUST_TYPEC + '</td>';
		html+='<td>' + result.MTN_DATE + '</td>';
		html+='<td>' + result.CREATE_TYPEC + '</td>';
		html+='<td>' + (result.ED_LMT==""?"暂无":parseFloat(result.ED_LMT/10000).toFixed(1)) + '</td>';
		html+='<td>' + (result.CREALIMIT==""?"暂无":parseFloat(result.CREALIMIT).toFixed(1)) + '</td>';
		html+='<td>' + (result.QQLIMIT==""?"暂无":parseFloat(result.QQLIMIT).toFixed(1)) + '</td>';
		html+='<td>' + result.CUST_GRP_JLC + '</td>';
		html+='<td>';
		if(!($("#bSHOW").is("button"))){//是否禁止操作  是：取消编辑按钮
			html+='<button mz="'+result.CUST_NAME+'"JLID="'+result.CUST_GRP_JL+'" khid="'+result.CUST_ID+'" yt="'+result.CUST_GRP_JLC+'" class="btn btn-danger btn-xs" type="button" name="btnEdit" >'+btnbj+'</button> ';
		}			
		if(!result.OTHER&&$("#bSHOW").is("button")){//不是他行客户并禁止操作显示查看
			html+='<button mz="'+result.CUST_NAME+'"JLID="'+result.CUST_GRP_JL+'" khid="'+result.CUST_ID+'" yt="'+result.CUST_GRP_JLC+'" class="btn btn-danger btn-xs" type="button" name="btnSee" >查看</button> ';			
		}else if(!result.OTHER){
			html+='<button mz="'+result.CUST_NAME+'"JLID="'+result.CUST_GRP_JL+'" khid="'+result.CUST_ID+'" JLIDC="'+result.CUST_GRP_JLC+'" class="btn btn-danger btn-xs" type="button" name="handover" >移交</button>';
		}
		html+='</td>';
		html+='</tr>';
	}
	return html;
}
</script>
<script id="列表模板">
$.fn.serializeJson = function () {
    var resultJson = {};
    var array = this.serializeArray();
    $(array).each(function () {
        resultJson[this.name]=this.value;
    });
    return resultJson;
}; 
data2 = {};
function pagerList(currentPage,pageSize){
	 var data1 = {
		 "isPagination":"true",
		 "currentPage":currentPage,
		 "pageSize":pageSize
	 };
	 if(localStorage.CUST_TYPE != ""||localStorage.C_ID != ""||localStorage.CUST_GRP != ""||localStorage.CUST_GRP2 != ""||localStorage.JL_TYPE != ""||localStorage.ZTBJ != "" ||
			 		localStorage.PJ_JB != "" || localStorage.BF_COUNT != "" || localStorage.ISHF != ""){
		 data2 = {
			"CUST_TYPE":localStorage.CUST_TYPE,
			"C_ID":localStorage.C_ID,
			"CUST_GRP":localStorage.CUST_GRP,
			"CUST_GRP2":localStorage.CUST_GRP2,
			"JL_TYPE":localStorage.JL_TYPE,
			"CREATE_TYPE":localStorage.ZTBJ,
			"PJ_JB":localStorage.PJ_JB,
			//下列没有在if条件中
			"ED_LMT_START":localStorage.ED_LMT_START,
			"ED_LMT_END":localStorage.ED_LMT_END,
			"CRT_DATE_START":localStorage.CRT_DATE_START,
			"CRT_DATE_END":localStorage.CRT_DATE_END,
			"ORG_CD_SELECT":localStorage.ORG_CD_SELECT,
			"USER_ID_SELECT":localStorage.USER_ID_SELECT,
			"BF_CRT_DATE_START":localStorage.BF_CRT_DATE_START,
			"BF_CRT_DATE_END":localStorage.BF_CRT_DATE_END,
			"BF_COUNT":localStorage.BF_COUNT,
			"ISHF":localStorage.ISHF,
			"HF_DATE_START":localStorage.HF_DATE_START,
			"HF_DATE_END":localStorage.HF_DATE_END,
			"HF_RESULT":localStorage.HF_RESULT
		 };
	 }
	 if(localStorage.keyword!=""){
		 data2 = {
			"keyword":localStorage.keyword	 
		 };
	 }
	 $.extend(data2,data1);//合并参数，并赋值给data2
	 //参数值赋值给“导出控件” 
	$("#exportParams").val(JSON.stringify(data2)); 
	 var URL = localStorage.url;
	 $.ajax({
		beforeSend:function(){
			$("#listbody").html("<tr><td colspan='9'>正在加载...</td></tr>");
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
					/*Io.on("#sort_type","click",function(){//按客户类型排序
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
					});
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
					});*/
					$("#listbody").html(pageLoad(resultArr));
					$.showPages('pagination',data.page.totalRows,pageSize,currentPage,'pagerList');
					localStorage.page = currentPage;
					localStorage.num = pageSize;
					iFrameHeight('myTabsFrame');
				}else{
					$("#listbody").html('<tr><td align="center" height="200" colspan="11">暂时没有内容</td></tr>');
				}
			}else if (data.code=="88"){
				eval(data.reLoad);
			}else{
				alert(data.errMsg);
				$("#listbody").html('<tr><td align="center" height="200" colspan="9">'+data.errMsg+'</td></tr>');
			}
		},error:function(data){
			console.log("错误");
		}
	});
}
pagerList(localStorage.page,localStorage.num);
$("#custSearchBtn").on("click",function(){
	var keyword = $("#keyword").val();
	localStorage.keyword = keyword;
	localStorage.CUST_TYPE = "";
	localStorage.C_ID = "";
	localStorage.C_NAME = "";
	localStorage.CUST_GRP = "";
	localStorage.CUST_GRP2 = "";
	localStorage.JL_TYPE = "";	
	localStorage.ZTBJ = "";
	$("#AREA_CD").attr("area_cd","");
	$("#AREA_CD").val("");
	$("#CUST_TYPE").val("");
	$("#CUST_GRP").val("");
	$("#CUST_GRP2").val("");
	$("#JL_TYPE").val("");
	$("#ZTBJ").val("");
	if(keyword!=""){			
		localStorage.url = "custbase/querybylike";			
	}else{
		localStorage.url = "custmgtree/queryList";
	}
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
		var data0 = {"CUST_ID":$(this).attr("khid")};
		$.ajax({
			type:"post",
			url:baseurl+"custapply/deleteGq",
			dataType:"json",
			contentType:"application/json",
			data:JSON.stringify(data0),
			success:function(data){
				if(data.code=="00"){
					
				}else if(data.code=="88"){
					eval(data.reLoad);
				}else{
					alert(data.errMsg);
				}
			}
		});
		$("#myModal1").modal({
			backdrop:false
		});
	}
	
});
Io.on("[name=btnSee]","click",function(){
	var id =$(this).parent().parent().attr("zt");
	location.href="kehuguanli_see.html?cust_id="+id;	
});
var ClickNum = 0;
Io.on(".btSee","click",function(){
	var id = $(this).attr("zt");
	var btn=$(this).find('button').html();
	ClickNum++;
	setTimeout(function(){ClickNum=0;},500);
	if(ClickNum==2){
		if($("#bSHOW").is("button")){//是否禁止操作  是：双击终止
			return false;
		}
		if(btn=="编辑"){
			location.href="kehuguanli_see.html?cust_id="+id;
		}else{
			$("#kehu").html( $(this).find('button').attr("mz"));
			$("#guanhuJL").val( $(this).find('button').attr("yt"));
			$("#CUST_ID").val( $(this).find('button').attr("khid"));
			$("#USER_ID_SQ").val( $(this).find('button').attr("JLID"));
			$("#myModal1").modal({
				backdrop:false
			});
		}
	}
	
});
//移交
Io.on("[name=handover]","click",function(){
		$("#handover_CUST_ID").val( $(this).attr("mz")).attr('handover_cust_id',$(this).attr("khid"));
		$("#handover_USER_ID").val( $(this).attr("JLIDC")).attr('handover_user_id',$(this).attr("JLID"));
		$("#myModal2").modal({
			backdrop:false
		});
	
});
//新增客户
$("#newport").on("click",function(){
	location.href="../khgl/kehuguanli_add.html";
});
//审核跳转
$("#myapply").on("click",function(){
	location.href="./apply.html";
});
//点击筛选按钮
$("#search_conditions").on("click",function(){
	$("#keyword").val("");
	localStorage.keyword = "";
	$("#CUST_TYPE").val(localStorage.CUST_TYPE);
	$("#CUST_GRP").val(localStorage.CUST_GRP);
	$("#CUST_GRP2").val(localStorage.CUST_GRP2);
	$("#JL_TYPE").val(localStorage.JL_TYPE);
	$("#ZTBJ").val(localStorage.ZTBJ);
	$("#keyword").val(localStorage.keyword);
	$("#AREA_CD").attr("area_cd",localStorage.C_ID);
	$("#AREA_CD").val(localStorage.C_NAME);
	$("#BF_COUNT").val(""); 
	$("#HF_DATE_START").val("");
	$("#HF_DATE_END").val("");
	$("#myModal").modal({
		backdrop:false
	});	
	//村子树加载
	var setting3 = {
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
						//选择村子后，回显
						$("#AREA_CD").val(treeNode.name).attr("area_cd",treeNode.id);
						//选择村子后，隐藏
						$("#cunzi").toggleClass("none");
						return true;
					}
					
				}
			}
		};
	//获取村子列表
		$.ajax({
			type:"post",
			url:baseurl+"custmgtree/queryTreebyJl",
			dataType:"json",
			contentType:"application/json",
			data:JSON.stringify(),
			success:function(data){
				if(data.code=="00"){
					result = data.result;
					result.unshift({id:"",name:""});//在result头部插入
					$.fn.zTree.init($("#tree3"), setting3,result);
				}else if(data.code=="88"){
					eval(data.reLoad);
				}else{
					alert(data.errMsg);
				}
			}
		});
		//获取当前登录用户信息
		getOperatorInfoFunc();
		//获取客户经理列表和机构表
		//删除下拉项中的内容
		document.getElementById("ORG_CD_SELECT").options.length=0;
		document.getElementById("USER_ID_SELECT").options.length=0;
		getUserInfo();
		getOrgInfo();
});
//筛选-村子弹出
Io.on("#cunzi_search","click",function(){	
		$("#cunzi").toggleClass("none");
});
//筛选
$("#go_search").on("click",function(){
	localStorage.CUST_TYPE = $("#CUST_TYPE").val();
	localStorage.C_ID = $("#AREA_CD").attr("area_cd");
	localStorage.C_NAME = $("#AREA_CD").val();
	localStorage.CUST_GRP = $("#CUST_GRP").val();
	localStorage.CUST_GRP2 = $("#CUST_GRP2").val();
	localStorage.JL_TYPE = $("#JL_TYPE").val();
	localStorage.ZTBJ = $("#ZTBJ").val();
	//新增条件
	localStorage.PJ_JB = $("#PJ_JB").val()+".0";
	//额度区间在onchange事件中赋值，不在此处
	localStorage.CRT_DATE_START = $("#CRT_DATE_START").val().replace(/\//g,'');
	localStorage.CRT_DATE_END = $("#CRT_DATE_END").val().replace(/\//g,'');
	localStorage.ORG_CD_SELECT = $("#ORG_CD_SELECT").val();
	localStorage.USER_ID_SELECT = $("#USER_ID_SELECT").val();
	localStorage.BF_CRT_DATE_START = $("#BF_CRT_DATE_START").val().replace(/\//g,'-');
	localStorage.BF_CRT_DATE_END = $("#BF_CRT_DATE_END").val().replace(/\//g,'-');
	localStorage.BF_COUNT = $("#BF_COUNT").val();
	localStorage.ISHF = $("#ISHF").val();
	localStorage.HF_DATE_START = $("#HF_DATE_START").val().replace(/\//g,'-');
	localStorage.HF_DATE_END = $("#HF_DATE_END").val().replace(/\//g,'-');
	localStorage.HF_RESULT = $("#HF_RESULT").val();
	localStorage.url = "custmgtree/queryList";
	pagerList(1,10);
	$("#myModal").modal("hide").on("hidden.bs.modal");
	
				
});
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
					alert("申请成功");
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
		});
	});
</script>
<script id="删除">
Io.on("#listbody>tr","click",function(){
	var $this=$(this);
	$this.addClass("on").siblings().removeClass("on");
});
$("#PhysicallyDel").on("click",function(){//物理删除
	var $on=$("#listbody").find("tr.on");
	var cust_id=$on.attr("zt");
	if($on&&$on.size()>0){
		Io.bs.alert("此操作将物理删除该客户所有信息，确定删除？",function(){
			$.ajax({
				type:"post",
				url:baseurl+"custbase/deleteReal",
				dataType:"json",
				async:false,
				contentType:"application/json",
				data:JSON.stringify({"CUST_ID":cust_id}),
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
			})
	})
	}else{
		Io.bs.alert("请选择一条信息")
	}
});
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
				})
			//})
		})
	}else{
		alert("请选择要删除的客户");
	}
});
</script>
<script type="text/javascript"id="移交">
//移交详情跳转
$("#handover").on("click",function(){
	location.href="./khgl_handover.html";
});
//移交——树加载
var setting = {
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
				$.fn.zTree.init($("#tree1"), setting, data.result);
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
	if($("#handover_USER_ID_SQ").val()==""){
		Io.bs.alert("接收人必填！");
		return false;
	}
	$.ajax({
		type:"post",
		url:baseurl+"custyj/custYj",
		dataType:"json",
		async:false,
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

//键：控件ID，值：字典项DIC_PARENTID
var json={//(字典枚举)筛选
		"CUST_TYPE":"CUST_TYPE",//客户类型
		"CUST_GRP":"CUST_GRP1",//所属客户群		
		"JL_TYPE":"JL_TYPE",//客户经理分类
		"ZTBJ":"ZTBJ",//标记状态
		"ISHF":"GDR_SALARY_FLAG",//是否回访 
		"HF_RESULT":"HF_RESULT"//回访结果
};
selectOnload({"json":json,"isDefault":false,"isElse":false});
</script>
<script >
	
	<!--评级结果-->
	var sel = document.getElementById("PJ_JB").options;
	sel.add(new Option("请选择",0));
	for(var i=1;i<=10;i++){
		sel.add(new Option(i, i));
	}
	<!--授信额度区间-->
	//1.第一个数值小于第二个
	//2.最大值小于500
	
	//alert("开始值："+start+",结束值:"+end); 
	var start,end;
	function checkFieldStart(obj) {
		//alert("输入值已更改。新值是：" + val);
		if(obj.value > 500){
			alert("数值太大,请重新输入!");
			obj.value = '';
		}else{
			start = obj.value;
			localStorage.ED_LMT_START = start;
		}
	}
	function checkFieldEnd(obj) {
		//alert("输入值已更改。新值是：" + val);
		if(obj.value > 500){
			alert("数值太大,请重新输入!");
			obj.value = '';
		}else{
			end = obj.value;
			//alert("开始值："+parseFloat(start)+",结束值:"+parseFloat(end));
			 if(parseFloat(start) > parseFloat(end)){
				alert("开始值不能大于结束值");
				obj.value = '';
			}else{
				//给授信区间赋值
				localStorage.ED_LMT_START = start;
				localStorage.ED_LMT_END = end;
				
			} 
		}
	}
	//录入/拜访日期区间
	function datePickerFunc(obj) {
		//alert(obj.value)
		//初始化时间显示
		if(obj.id == "CRT_DATE_START"){
			$("#CRT_DATE_START").datepicker("show");
			$("#CRT_DATE_START").datepicker("update",newDate);
		}else if(obj.id == "CRT_DATE_END"){
			$("#CRT_DATE_END").datepicker("show");
			$("#CRT_DATE_END").datepicker("update",newDate);
		}else if(obj.id == "BF_CRT_DATE_START"){
			$("#BF_CRT_DATE_START").datepicker("show");
			$("#BF_CRT_DATE_START").datepicker("update",newDate);
		}else if(obj.id == "BF_CRT_DATE_END"){
			$("#BF_CRT_DATE_END").datepicker("show");
			$("#BF_CRT_DATE_END").datepicker("update",newDate);
		}else if(obj.id == "HF_DATE_START"){
			$("#HF_DATE_START").datepicker("show");
			$("#HF_DATE_START").datepicker("update",newDate);
		}else if(obj.id == "HF_DATE_END"){
			$("#HF_DATE_END").datepicker("show");
			$("#HF_DATE_END").datepicker("update",newDate);
		}
	}
	//获取当前登录用户信息
	function getOperatorInfoFunc(){
		var data1 = {};
		$.ajax({
			type:"post",
			url:baseurl+"OperatorInfo/getUserInfo",
			dataType:"json",
			async:false,
			contentType:"application/json",
			data:JSON.stringify(data1),
			success:function(data){
				if(data.code=="00"){
					var result = data.result;
					if(result[0].ROLE_FW == "00"){
						//客户经理不显示“所属机构”和“客户经理”筛选
						$("#ORG_CD_SELECT_DIV").hide();
						$("#USER_ID_SELECT_DIV").hide();
						//客户经理不显示回访
						$("#HF_DIV").hide();
						$("#HF_DATE_DIV").hide(); 
					}
				}
			}
		});
	}
	//获取机构列表
	function getOrgInfo(){
		var data1 = {};
		$.ajax({
			type:"post",
			url:baseurl+"lrdorg/querybyrela",
			dataType:"json",
			async:false,
			contentType:"application/json",
			data:JSON.stringify(data1),
			success:function(data){
				if(data.code=="00"){
					//alert("信息列表："+JSON.stringify(data));
					var result = data.result;
					//机构下拉
					var sel = document.getElementById("ORG_CD_SELECT").options;
					sel.add(new Option("-请选择-",""));
					for(var i=0;i<result.length;i++){
						if(result[i].pId != ""){
							sel.add(new Option(result[i].ORG_ABB,result[i].id));
						}
					}
				}else if(data.code=="88"){
					eval(data.reLoad);
				}
			}
		});
	}
	//获取客户经理列表
	function getUserInfo(){
		var data1 = {};
		$.ajax({
			type:"post",
			url:baseurl+"lrduser/querylist",
			dataType:"json",
			async:false,
			contentType:"application/json",
			data:JSON.stringify(data1),
			success:function(data){
				//alert("信息列表："+JSON.stringify(data));
				if(data.code=="00"){
					var result = data.result;
					var userResult = result.result;
					//alert(JSON.stringify(userResult));
					//客户经理下拉
					var sel = document.getElementById("USER_ID_SELECT").options;
					sel.add(new Option("-请选择-",""));
					for (var i = 0; i <= userResult.length; i++) {
						if(userResult[i].name == "客户经理"){
							sel.add(new Option(userResult[i].USER_NAME,userResult[i].USER_ID));
						}
					}
				}else if(data.code=="88"){
					eval(data.reLoad);
				}
			}
		});
	}
	$("#exportToExcel").on("click", function(){
		$("#documentForm").attr("action",baseurl+"custExport/exportCustBase");
		$("#documentForm").submit();
	});
	
</script>
</div>
</body>
</html>