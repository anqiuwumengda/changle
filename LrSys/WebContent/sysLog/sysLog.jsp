<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
		<title>系统日志</title>
		<%@taglib prefix="b" uri="/WEB-INF/lrdButton.tld"%> 
		<link rel="stylesheet" href="../css/bootstrap.min.css">
		<link rel="stylesheet" href="../css/AdminLTE.min.css">
		<link rel="stylesheet" href="../css/skins/_all-skins.min.css">
		<link rel="stylesheet" href="../css/font-awesome.min.css">
		<link rel="stylesheet" href="../css/bootstrapValidator.css">
		<link rel="stylesheet" href="../css/zTreeStyle/zTreeStyle.css">
		<link rel="stylesheet" href="../css/bootstrap-datepicker3.min.css">
		<link rel="stylesheet" href="../css/demo.css">
		
		<script src="../js/jquery.min.js"></script>
		<script src="../js/bootstrap.min.js"></script>
		<script src="../js/app.min.js"></script>
		<script src="../js/jquery.ztree.core-3.5.min.js"></script>
		<script src="../js/common.js"></script>
		<script src="../js/showPages.js"></script>
		<script src="../js/bootstrap-datepicker.min.js"></script>
		
	</head>

	<body class="pbody">
		<div class="tip_search col-md-12">
			<form class="form-horizontal" role="form">
				 <div class="form-group col-md-6 col-xs-6">
			    <label for="operating" class="col-md-4  control-label">操作机构：</label>
			    <div class="col-md-8 ">
			      <input id="ORG_CD" type="text" class="form-control" disabled="disabled" placeholder="请输入">
			      <span id="operating" class="form-group-addon"><i class="fa fa-search"></i></span>
			    </div>
			  </div>
			  <div class="form-group col-md-6 col-xs-6">
			    <label for="operater" class="col-md-4 control-label">操作人员：</label>
			    <div class="col-md-8 ">
			      <input id="USER_ID" type="text" class="form-control" disabled="disabled" placeholder="请输入">
			      <span id="operater" class="form-group-addon"><i class="fa fa-search"></i></span>
			    </div>
			  </div>
			 <div class="form-group col-md-6 col-xs-6">
			    <label for="CRT_DATE" class="col-md-4 control-label">开始时间：</label>
			    <div class="col-md-8">
			      <input type="text" class="form-control" id="CRT_DATE">
			    </div>
			  </div>
			  <div class="form-group col-md-6 col-xs-6">
			    <label for="endtime" class="col-md-4 control-label">结束时间：</label>
			    <div class="col-md-8">
			      <input type="text" class="form-control" id="MTN_DATE">
			    </div>
			  </div>
			 
			</form>
			<div class="search_bottom col-md-12 pb-10">
				<button id="search" class="btn btn-primary btn-sm">查询</button>
				<button id="clear" class="btn btn-info btn-sm">清空</button>
			</div>
		</div>
		<div class="pane_btn col-md-12">
			<b:lbutton buttonId="btn_del" buttonClass="btn btn-danger btn-sm" iClass="fa fa-trash" buttonName=" 清除日志" funcCd="010000000107"></b:lbutton>
		</div>
		<div class="table-responsive col-md-12 no-padding">
			<table class="table table-bordered table-hover">
				<thead>
					<tr>
						<th class="text-center" style="width:50px">序号</th>
						<th>法人号</th>
						<th>机构号</th>
						<th>用户号</th>
						<th>操作时间</th>
						<th>操作名称</th>
						<th>操作结果</th>
					</tr>
				</thead>
				<tbody id="syslog">
				</tbody>
			</table>
		</div>
		<ul class="pagination pagination-sm" id="pagination">
			<li class="prev"><a href="javascript:;">← 上一页</a></li>
			<li class="active"><a href="javascript:;">1</a></li>
			<li class="next"><a href="javascript:;">下一页 →</a></li>
		</ul>
<div id="myModal1" class="modal fade" tabindex="-1" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content modal_content_sm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h5 class="modal-title" >操作机构</h5>
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
<div id="myModa2" class="modal fade" tabindex="-1" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content modal_content_sm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h5 class="modal-title" >操作人员</h5>
			</div>
			<div style="max-height:350px;overflow-y:scroll">
				<table class="table table-bordered table-hover no-margin" >
				<thead>
					<tr>
						<th>选择带回</th>
						<th>序号</th>
						<th>人员代码</th>
						<th>人员名称</th>
					</tr>
				</thead>
				<tbody id="tree2" >
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
<script type="text/javascript">
var ORG_CD = $("#ORG_CD"),//机构
USER_ID = $("#USER_ID"),//renyuan
CRT_DATE=$("#CRT_DATE"),//开始时间
MTN_DATE = $("#MTN_DATE");//结束时间
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
	url:baseurl+"syslog/query",
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
					html+='<tr ORG_CD="'+result.ORG_CD+'"CORP_CD="'+result.CORP_CD+'"USER_ID="'+result.USER_ID+'">';
					html+='<td class="text-center">' + (i+1)+ '</td>';						
					html+='<td>' + result.CORP_CD + '</td>';						
					html+='<td>' + result.ORG_CD + '</td>';
					html+='<td>' + result.USER_ID + '</td>';
					html+='<td>' + result.OPER_DATE + '</td>';
					html+='<td>' + result.OPER_NAME + '</td>';
					html+='<td>' + result.OPER_IG + '</td>';
					html+='</tr>';
				}
				$("#syslog").html(html);
				$.showPages('pagination',data.page.totalRows,pageSize,currentPage,'pagerList');
			}else{$("#syslog").html('<tr><td align="center" height="150" colspan="9">暂时没有内容</td></tr>');}
			
		}else{
			alert(data.errMsg);
		}
	}
})
}
pagerList (1,10);

//选择带回
Io.on("#take_back","click",function(){
	var checkboxs=$("[name=checkBox]:checked");
	var roleStr="",roleNameStr="";
	if(checkboxs.size()>0){
		if(checkboxs.size()==1){
			roleStr=checkboxs.attr("user_id");
			roleNameStr=checkboxs.attr("user_name");
			}else{
				for(var i=0;i<checkboxs.size();i++){
					if(i==checkboxs.size()-1){
						roleStr+=checkboxs.eq(i).attr("user_id");
						roleNameStr+=checkboxs.eq(i).attr("user_name");
					}else{
						roleStr+=checkboxs.eq(i).attr("user_id")+",";
						roleNameStr+=checkboxs.eq(i).attr("user_name")+",";
					}
				}
			}
		
		$("#USER_ID").val(roleNameStr).attr("user_id",roleStr);
		$("#myModa2").modal("hide");
	}
});
//弹出
$("#operating").on("click",function(){
	$("#myModal1").modal({
		backdrop:false
	});
	upviewdata();
});
$("#operater").on("click",function(){
	$("#myModa2").modal({
		backdrop:false
	});
	upviewdata();
});
//查询加载
function upviewdata(){
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
					$("#ORG_CD").val(treeNode.name).attr("org_cd",treeNode.id);
					$("#myModal1").modal("hide");
					return true;
				}
				
			}
		}
	};
data2={
		"ORG_RELA_TYPE":"00"
	};
$.ajax({
	type:"post",
	url:baseurl+"syslog/queryPK",
	dataType:"json",
	contentType:"application/json",
	data:JSON.stringify(data2),
	success:function(data){
		//console.log(data);
		if(data.code=="00"){
			var ORG=data.result.ORG;
			var ROLE=data.result.USER;
			$.fn.zTree.init($("#tree1"), setting, ORG);
			var html="";
			for(var i=0;i<ROLE.length;i++){
				var result = ROLE[i];
				html+='<tr USER_ID="'+result.USER_ID+'"USER_NAME="'+result.USER_NAME+'">';
				html+='<td><input type="checkbox" name="checkBox" user_name="'+result.USER_NAME+'"user_id="'+result.USER_ID+'"/></td>';
				html+='<td class="text-center">' + (i+1)+ '</td>';						
				html+='<td>' + result.USER_ID + '</td>';						
				html+='<td>' + result.USER_NAME + '</td>';
				html+='</tr>';
			}
			$("#tree2").html(html);
			//console.log(data)
		}else if (data.code=="88"){
			eval(data.reLoad);
		}else{
			alert(data.errMsg);
		}
	},error:function(data){
		console.log("错误");
	}
});
}
//查询请求
Io.on("#search","click",function(){
	if(CRT_DATE.val()!=""){
		CQ.CRT_DATE=CRT_DATE.val();
	}else{
		delete CQ.CRT_DATE;
	}
	if($("#ORG_CD").attr("org_cd")!=""){
		CQ.ORG_CD=$("#ORG_CD").attr("org_cd");
	}else{
		delete CQ.ORG_CD;
	}
	if($("#USER_ID").attr('user_id')!=""){
		CQ.USER_ID=$("#USER_ID").attr('user_id');
	}else{
		delete CQ.USER_ID;
	}
	if(MTN_DATE.val()!=""){
		CQ.MTN_DATE=MTN_DATE.val();
	}else{
		delete CQ.MTN_DATE;
	}
		pagerList (1,10);
	});
//删除
Io.on("#btn_del","click",function(){
	Io.bs.alert("确认清除所有日志吗？", function(){	
		$.ajax({
			type:"post",
			url:baseurl+"syslog/delete",
			dataType:"json",
			contentType:"application/json",
			data:JSON.stringify(),
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
		});
	});
});
//清空
$("#clear").on("click",function(){
	$("form").find("input").val('');
	$("#USER_ID").attr("role_id","");
	$("#ORG_CD").attr("org_cd","");
});
//选择
Io.on("#syslog tr","click",function(){
	$(this).addClass("on").siblings().removeClass("on");
});
$("#CRT_DATE,#MTN_DATE").datepicker();
</script>
	</body>

</html>