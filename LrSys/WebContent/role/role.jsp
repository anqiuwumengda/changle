<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<title>角色管理</title>
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
<script src="../js/showPages.js"></script>
<script src="../js/common.js"></script>
<%@taglib prefix="b" uri="/WEB-INF/lrdButton.tld"%> 
</head>
<body class="pbody">
	<div class="box box-solid pt-10">
		<div class="box-body">
			<form class="form-horizontal">
				<div class="form-group col-md-6 col-xs-6">
					<label for="usernum" class="col-md-4 control-label">角色名称：</label>
					<div class="col-md-8">
						<input type="text" class="form-control" id="ROLE_NM" >
					</div>
				 </div>
				 <div class="form-group col-md-6 col-xs-6">
					<label for="username" class="col-md-4 control-label">应用范围：</label>
					<div class="col-md-8">
						<select class="form-control" id="ROLE_FW">							
						</select>
					</div>
				 </div>
			</form>
			<div class="search_bottom col-md-12">
				<button id="btnC" class="btn btn-primary btn-sm">查询</button>
				<button id="btnQ" class="btn btn-info btn-sm">清空</button>
			</div>
		</div>
	</div>
	<div class="pane_btn clearfix">
		<b:lbutton buttonId="btnAdd" buttonClass="btn btn-primary btn-sm" iClass="fa fa-plus" buttonName="添加" funcCd="010000000102"></b:lbutton>
				<b:lbutton buttonId="btnEdit" buttonClass="btn btn-primary btn-sm" iClass="fa fa-edit" buttonName="修改" funcCd="010000000202"></b:lbutton>
				<b:lbutton buttonId="btnDel" buttonClass="btn btn-primary btn-sm" iClass="fa fa-trash" buttonName="删除" funcCd="010000000302"></b:lbutton>
						
	</div>
	<div class="table-responsive box box-solid">
		<table class="table table-bordered table-hover no-margin">
			<thead>
				<tr>
					<th class="text-center">序号</th>
					<th>角色代码</th>
					<th>角色名称</th>
					<th>角色说明</th>
					<th>创建机构</th>
					<th>应用范围</th>					
				</tr>
			</thead>
			<tbody id="rList">				
			</tbody>
		</table>
	</div>
	<div>
		<ul class="pagination pagination-sm" id="pagination">
		</ul>
	</div>
<script>
var CQ = {};
function pagerList (currentPage,pageSize){
	var data1={
		"isPagination":"true",
		"currentPage":currentPage,
		"pageSize":pageSize
	};
	$.extend(data1,CQ);
	$.ajax({
		type:"post",
		url:baseurl+"lrdrole/querylist",
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
						html+='<tr zt="'+result.ROLE_CD+'">';
						html+='<td class="text-center">' + (i+1)+ '</td>';						
						html+='<td>' + result.ROLE_CD + '</td>';						
						html+='<td>' + result.ROLE_NAME + '</td>';
						html+='<td>' + result.DESC + '</td>';
						html+='<td>' + result.ORG_NAME + '</td>';
						html+='<td>' + result.ROLE_FWC + '</td>';
						html+='</tr>';
					}
					$("#rList").html(html);
					$.showPages('pagination',data.page.totalRows,pageSize,currentPage,'pagerList');
				}else{
					$("#rList").html("");
				}				
			}else{
				alert(data.errMsg);
			}
		}
	})
}
pagerList (1,10);
Io.on("#rList tr","click",function(){
	$(this).addClass("on").siblings().removeClass("on");
});
Io.on("#btnEdit","click",function(){
	if($("#rList .on").is("tr")){
		var id = $("#rList .on").attr("zt");
		location.href="role_edit.html?i="+id;
	}
});
Io.on("#btnAdd","click",function(){
		location.href="role_add.html";
});
Io.on("#btnDel","click",function(){
	if($("#rList .on").is("tr")){
		var id = $("#rList .on").attr("zt");
		Io.bs.alert("确定删除吗？", function(){
			var data2 = {"ROLE_CD":id};
			$.ajax({
				type:"post",
				url:baseurl+"lrdrole/delete",
				dataType:"json",
				contentType:"application/json",
				data:JSON.stringify(data2),
				success:function(data){
					if(data.code=="00"){
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
		alert("请选择一个项目");
	}
});
Io.on("#btnC","click",function(){	
	if($("#ROLE_NM").val()!=""){
		CQ.ROLE_NAME=$("#ROLE_NM").val();
	}else{
		delete CQ.ROLE_NAME;
	}
	if($("#ROLE_FW").val()!=""){
		CQ.ROLE_FW=$("#ROLE_FW").val();
	}else{
		delete CQ.ROLE_FW;
	}
	pagerList (1,10);
});
Io.on("#btnQ","click",function(){	
	CQ = {};
	$("#ROLE_NM").val("");
	$("#ROLE_FW").val("");
	pagerList (1,10);
});
</script>
<script id="字典">
var json={"ROLE_FW":"SYS_ORG_YYFW"};
selectOnload({"json":json,"isDefault":false});
</script>
</body>
</html>