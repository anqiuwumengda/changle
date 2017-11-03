<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
	<head>
		<meta charset="UTF-8">
		<title>报表管理</title>
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
		<%@taglib prefix="b" uri="/WEB-INF/lrdButton.tld"%> 
		<link rel="stylesheet" href="../css/bootstrap.min.css">
		<link rel="stylesheet" href="../css/AdminLTE.min.css">
		<link rel="stylesheet" href="../css/skins/_all-skins.min.css">
		<link rel="stylesheet" href="../css/font-awesome.min.css">
		<link rel="stylesheet" href="../css/zTreeStyle/zTreeStyle.css">
		<link rel="stylesheet" href="../css/bootstrapValidator.css">
		<link rel="stylesheet" href="../css/bootstrap-datepicker3.min.css">
		<link rel="stylesheet" href="../css/demo.css">
		<script src="../js/jquery.min.js"></script>
		<script src="../js/bootstrap.min.js"></script>
		<script src="../js/bootstrapValidator.js"></script>
		<script src="../js/bootstrap-datepicker.min.js"></script>
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
			    <label for="USER_ID" class="col-md-4 control-label">报表编号：</label>
			    <div class="col-md-8">
			      <input type="text" class="form-control" id="BBID">
			    </div>
			  </div>
			  <div class="form-group col-md-6 col-xs-6">
			    <label for="USER_NAME" class="col-md-4 control-label">报表名称：</label>
			    <div class="col-md-8">
			      <input type="text" class="form-control" id="BBZWMC">
			    </div>
			  </div>
			  <div class="form-group col-md-6 col-xs-6">
			    <label for="ORG_NAME" class="col-md-4  control-label">报表类型：</label>
			    <div class="col-md-8 ">
			      <input type="text" class="form-control" id="BBLX" >			      
			    </div>
			  </div>
			  <div class="form-group col-md-6 col-xs-6">
			    <label for="ROLE_CD" class="col-md-4 control-label">启用日期：</label>
			    <div class="col-md-8 ">
			      <input type="text" class="form-control"  id="STARTDATE">			     
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
		</div>
		<div class="table-responsive box box-solid">
		<form >
			<table class="table table-bordered table-hover no-margin">
				<thead>
					<tr>
						<th class="text-center">序号</th>
						<th>报表编号</th>
						<th>报表名称</th>
						<th>文件名</th>
						<th>报送频率</th>						
						<th>报表类型</th>
						<th>启用日期</th>
						<th>止用日期</th>
					</tr>
				</thead>
				<tbody id="bbList">					
				</tbody>
			</table></form>
		</div>
		<div>
			<ul class="pagination pagination-sm" id="pagination">
			</ul>
		</div>
<script type="text/javascript">
//清空
$("#clear").on("click",function(){
	$("form").find("input").val('');
	CQ = {};
});

//新增
Io.on("#btn_add","click",function(){
	location.href="./reportForms_add.html";
});
Io.on("#bbList tr","click",function(){
	$(this).addClass("on").siblings().removeClass("on");
});
var CQ = {};
function pagerList (currentPage,pageSize){
	data1={
		"isPagination":"true",
		"currentPage":currentPage,
		"pageSize":pageSize
	};
	$.extend(data1,CQ);
	$.ajax({
		type:"post",
		url:baseurl+"ReportManager/query",
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
						html+='<tr BBID="'+result.BBID+'">';
						html+='<td class="text-center">' + (i+1)+ '</td>';	
						html+='<td>' + result.BBID + '</td>';	
						html+='<td>' + result.BBZWMC + '</td>';	
						html+='<td>' + result.WJM + '</td>';
						html+='<td>' + result.BSPL + '</td>';
						html+='<td>' + result.BBLX + '</td>';						
						html+='<td>' + result.STARTDATE + '</td>';
						html+='<td>' + result.ENDDATE + '</td>';
						html+='</tr>';
					}					
					$("#bbList").html(html);
					$.showPages('pagination',data.page.totalRows,pageSize,currentPage,'pagerList');
				}else{$("#bbList").html('<tr><td align="center" height="150" colspan="8">暂时没有内容</td></tr>');}				
			}else{
				alert(data.errMsg);
			}
		}
	});
}
pagerList (1,10);

//查询
Io.on("#search","click",function(){
	if($("#BBID").val()!=""){
		CQ.BBIDTOLIKE=$("#BBID").val();    
	}else{
		delete CQ.BBIDTOLIKE;
	}
	if($("#BBZWMC").val()!=""){
		CQ.NAMETOLIKE=$("#BBZWMC").val();
	}else{
		delete CQ.NAMETOLIKE;
	}
	if($("#BBLX").val()!=""){
		CQ.BBLX=$("#BBLX").val();
	}else{
		delete CQ.BBLX;
	}
	if($("#STARTDATE").val()!=""){
		CQ.STARTDATE=$("#STARTDATE").val();
	}else{
		delete CQ.STARTDATE;
	}
	pagerList (1,10);
});
//删除
$("#btn_del").on("click",function(){
	var bid=$("#bbList").find("tr.on").attr("BBID");
	if(bid!=''&&bid!=undefined){
		Io.bs.alert("确认删除吗？", function(){	
			var data1 = {
				"BBID":bid
			};
			$.ajax({
				type:"post",
				url:baseurl+"ReportManager/delete",
				dataType:"json",
				contentType:"application/json",
				data:JSON.stringify(data1),
				success:function(data){
					if(data.code=="00"){
						alert("删除成功");
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
		alert("请选择一条信息");
	}
	
});
//修改
$("#btn_edit").on("click",function(){
	var bid=$("#bbList").find("tr.on").attr("BBID");
	if(bid!=''&&bid!=undefined){
		location.href="./reportForms_edit.html?b="+bid;
	}else{
		alert("请选择一条信息");
	}
	
});
$("#STARTDATE").datepicker({format:"yyyymmdd"});
</script>	
</body>

</html>