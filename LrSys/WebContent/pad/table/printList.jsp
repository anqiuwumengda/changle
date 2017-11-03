<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@taglib prefix="b" uri="/WEB-INF/lrdButton.tld"%> 
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no">
<title>审查审批表-乐融易贷管理系统</title>
<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/showPages.js"></script>
<script src="../js/myscript.js"></script>
<link rel="stylesheet" href="../css/bootstrap.min.css">
<link rel="stylesheet" href="../css/pages.css">
</head>
<body class="page-bg">
<div class="white-bg mb50">
<div class="clearfix tab-header">
	<input class="form-control left" name="keyword" id="keyword" style="width:268px;" placeholder="客户名/电话/证件号">
	<a href="javascript:;" class="btn btn-primary ml10 left" id="custSearchBtn">检索</a>
</div>
<div class="clearfix tab-header">	
	<b:lbutton buttonId="btnCreat" buttonClass="btn btn-info btn-sm" iClass="glyphicon glyphicon-list-alt"  buttonName=" 生成" funcCd="010000000510"></b:lbutton>
	<b:lbutton buttonId="btnPrint" buttonClass="btn btn-info btn-sm" iClass="glyphicon glyphicon-list-alt"  buttonName=" 查看" funcCd="010000000511"></b:lbutton>	
	<b:lbutton buttonId="btnBg"    buttonClass="btn btn-info btn-sm" iClass="glyphicon glyphicon-list-alt"  buttonName=" 报告" funcCd="010000000512"></b:lbutton>
</div>
<div class="table_wrapper">
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>客户姓名</th>
				<th>身份证号</th>
				<th>主营项目</th>
				<th>联系电话</th>
				<th>建议额度</th>
				<th>內评结果</th>
				<th>生成日期</th>
				<th>失效日期</th>
			</tr>
		</thead>
		<tbody id="listbody">
			<tr><td align="center" height="300" colspan="11 ">暂时没有内容</td></tr>
		</tbody>
	</table>
</div>
	<ul class="pagination pagination-sm" id="pagination">
		<li class="prev"><a href="javascript:;">← 上一页</a></li>
		<li class="active"><a href="javascript:;">1</a></li>
		<li class="next"><a href="javascript:;">下一页 →</a></li>
	</ul>
</div>
<div id="loadgif" class="none" style=""><img alt="加载中..." src="../img/loading.gif" width="32" height="32"/></div>
<script>
Io.on("#listbody>tr","click",function(){
	var $this=$(this);
	$this.addClass("on").siblings().removeClass("on");
});
var scsp = getUrlParam("scsp")||"";
if(scsp!="1"){
	localStorage.scsppage = "1";
	localStorage.scspnum = "10";
	localStorage.scspkeyword = "";
}
function pageLoad(resultArr){
	var html="";
	for(var i=0;i<resultArr.length;i++){
		var result = resultArr[i];
		html+='<tr pj="'+result.PJ_JB+'" zyxm="'+result.ZYXM+'" zt="'+result.CUST_ID+'">';
		html+='<td>' + result.CUST_NAME + '</td>';
		html+='<td>' + result.ID_NO + '</td>';
		html+='<td>' + result.ZYXMC + '</td>';
		html+='<td>' + result.TEL_NO + '</td>';
		html+='<td>' + (result.ED_LMT==""?"暂无":result.ED_LMT) + '</td>';
		html+='<td>' + (result.PJ_JB==""?"暂无":result.PJ_JB) + '</td>';
		html+='<td>' + (result.DATE==""?"暂无":result.DATE) + '</td>';
		html+='<td>' + (result.LOSE_DATE==""?"暂无":result.LOSE_DATE) + '</td>';
		html+='</tr>';
	}
	return html;
}
function pagerList(currentPage,pageSize){
	 var data2 = {};
	 var data1 = {
		 "isPagination":"true",
		 "currentPage":currentPage,
		 "pageSize":pageSize
	 };
	 if(localStorage.scspkeyword!=""){
		 data2 = {
			"keyword":localStorage.scspkeyword	 
		 };
	 }
	 $.extend(data2,data1);
	 $.ajax({
		beforeSend:function(){
			$("#listbody").html("<tr><td colspan='11'>正在加载...</td></tr>");
		},
		url:baseurl + "SCSP/queryList",
		type:"post",
		dataType:"json",
		contentType:"application/json",
		data:JSON.stringify(data2),
		success:function(data){
			if(data.code=="00"){
				if(data.result!=null && data.result.length){
					var resultArr=data.result;
					$("#listbody").html(pageLoad(resultArr));
					$.showPages('pagination',data.page.totalRows,pageSize,currentPage,'pagerList');
					localStorage.scsppage = currentPage;
					localStorage.scspnum = pageSize;
					iFrameHeight('myTabsFrame');
				}else{
					$("#listbody").html('<tr><td align="center" height="200" colspan="11">暂时没有内容</td></tr>');
				}
			}else if (data.code=="88"){
				eval(data.reLoad);
			}else{
				alert(data.errMsg);
				$("#listbody").html('<tr><td align="center" height="200" colspan="11">'+data.errMsg+'</td></tr>');
			}
		},error:function(data){
			console.log("错误");
		}
	})
}
pagerList(localStorage.scsppage,localStorage.scspnum);
Io.on("#btnPrint","click",function(){
	
	var $on=$("#listbody").find("tr.on");
	var cust_id=$on.attr("zt");
	var pj=$on.attr("pj");
	var zyxm=$on.attr("zyxm");
	if(pj){
	if($on&&$on.size()>0){	
	var jumpURL="";
	switch(zyxm){
		case '01':
			jumpURL="./table_zzsc.html";
			break;
		case '02':
			jumpURL="./table_zzqt.html";
			break;
		case '03':
			jumpURL="./table_yzz.html";
			break;
		case '04':
			jumpURL="./table_yzqt.html";
			break;
		case '05':
			jumpURL="./table_zg.html";
			break;
		case '06':
			jumpURL="./table_zzy.html";
			break;
		case '07':
			jumpURL="./table_ys.html";
			break;
		case '08':
			jumpURL="./table_pfls.html";
			break;
		case '09':
			jumpURL="./table_cyzs.html";
			break;
		case '10':
			jumpURL="./table_qthy.html";
			break;
		case '11':
			jumpURL="./table_DG.html";
			break;
		default:
			jumpURL="";
	} 
	location.href=jumpURL+"?cust_id="+cust_id;
	}else{alert("请选择一条信息")}
	}else{alert("请先生成")}
});
Io.on("#btnBg","click",function(){
	var $on=$("#listbody").find("tr.on");
	var cust_id=$on.attr("zt");
	if($on&&$on.size()>0){		
	location.href="./table_bg.html?cust_id="+cust_id;
	}else{alert("请选择一条信息")}
});
</script>
<script id="生成==额度计算">
Io.on("#btnCreat","click",function(){
	var $on=$("#listbody").find("tr.on");
	var cust_id=$on.attr("zt");
	if($on&&$on.size()>0){
	var td4=$on.find("td").eq(4);
	var td5=$on.find("td").eq(5);
	var td6=$on.find("td").eq(6);
	var td7=$on.find("td").eq(7);
	Io.bs.alert("是否确认生成 审查审批表",function(){
	var data2={
			"CUST_ID":cust_id
	};
	$.ajax({
		beforeSend:function(){
			$("#loadgif").fadeIn(100);
		},
		type:"post",
		url:baseurl+"edjs/callrule_unit",
		dataType:"json",
		contentType:"application/json",
		data:JSON.stringify(data2),
		success:function(data){
			$("#loadgif").fadeOut(100);
			if(data.code=="00"){
				var result=data.result;
				var $on=$("#listbody").find("tr.on");
				$on.attr("pj",result.PJ_JB)
				td4.text(result.ED_LMT);
				td5.text(result.PJ_JB);
				td6.text(result.DATE);
				td7.text(result.LOSE_DATE);				
			}else if (data.code=="88"){
				eval(data.reLoad);
			}else{
				alert(data.errMsg);
				console.log(data.errMsg);
			}
		},error:function(data){
			$("#loadgif").fadeOut(100);
			console.log("错误");
		}
	}) 
	});
	}else{alert("请选择一条信息")}
});
//检索
$("#custSearchBtn").on("click",function(){
	var keyword = $("#keyword").val();
	localStorage.scspkeyword = keyword;
	pagerList(1,10);
});
</script>
</body>
</html>