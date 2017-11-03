<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@taglib prefix="b" uri="/WEB-INF/lrdButton.tld"%> 
<title>规章制度</title>
<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/showPages.js"></script>
<script src="../js/myscript.js"></script>
<link rel="stylesheet" href="../css/bootstrap.min.css">
<link rel="stylesheet" href="../css/pages.css">
</head>
<body class="white-bg" style="padding:10px;">
<div class="clearfix">
	<%-- <button class="btn btn-primary" type="button" id="btnAdd">
		<i class="glyphicon glyphicon-plus"></i> 新增
	</button>
	<button class="btn btn-info" type="button" id="btnEdit">
		<i class="glyphicon glyphicon-edit"></i> 修改
	</button>
	<button class="btn btn-danger" type="button" id="btnDel">
		<i class="glyphicon glyphicon-trash"></i> 删除
	</button> --%>
	<b:lbutton buttonId="btnAdd" buttonClass="btn btn-info" iClass="glyphicon glyphicon-plus" buttonName="新增" funcCd="010000000111"></b:lbutton>
	<b:lbutton buttonId="btnEdit" buttonClass="btn btn-primary" iClass="glyphicon glyphicon-edit" buttonName="修改" funcCd="010000000211"></b:lbutton>
	<b:lbutton buttonId="btnDel" buttonClass="btn btn-danger" iClass="glyphicon glyphicon-trash" buttonName="删除" funcCd="010000000311"></b:lbutton>
</div>
<table class="table table-hover bb1" id="gzzdlist">
<thead>
	<tr>
		<th>标题</th>
		<th>发布单位</th>
		<th>日期</th>
	</tr>
</thead>
<tbody id="tbody">
	
</tbody>
</table>
<ul class="pagination pagination-sm" id="pagination">
	<li class="prev"><a>← 上一页</a></li>
	<li class="active"><a href="#">1</a></li>
	<li class="next"><a>下一页 →</a></li>
</ul>
<script>
//列表
function pagerList (currentPage,pageSize,data){
	if(data){
		data1={
			"isPagination":"true",
			"currentPage":currentPage,
			"pageSize":pageSize
		}; 
	}else{
		data1={
			"isPagination":"true",
			"currentPage":currentPage,
			"pageSize":pageSize
		};
	} 
$.ajax({
	type:"post",
	url:baseurl+"lrdrule/queryList",
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
					html+='<tr rule_no="'+result.RULE_NO+'">';
					html+='<td><a href="./guizhangzhidu_detail.html?no='+result.RULE_NO+'">' + result.RULE_NAME+ '</td>';						
					html+='<td>' + result.PUB_DEPT + '</td>';						
					html+='<td>' + result.PUB_DATE + '</td>';
					html+='</tr>';
				}
				$("#tbody").html(html);
				$.showPages('pagination',data.page.totalRows,pageSize,currentPage,'pagerList');
			}
			
		}else if(data.code=="88"){
			eval(data.reLoad);
		}else{
			alert(data.errMsg);
		}
	}
})
}
pagerList(1,10);
//点击选中
Io.on("#tbody>tr","click",function(){
	$(this).addClass("on").siblings().removeClass("on");
})
$("#btnAdd").on("click",function(){
	location.href="./gzzd_add.html";
})
//删除
$("#btnDel").on("click",function(){
	var rule_no=$("#tbody").find("tr.on").attr("rule_no");
	var data1={"RULE_NO":rule_no};
	if(!rule_no){
		alert("请选择要删除的选项！");
	}else{
		if(confirm("确定删除吗？")){
			$.ajax({
				type:"post",
				url:baseurl+"lrdrule/delete",
				dataType:"json",
				contentType:"application/json",
				data:JSON.stringify(data1),
				success:function(data){
					if(data.code=="00"){
						pagerList(1,10);
					}else if(data.code=="88"){
						eval(data.reLoad);
					}else{
						console.log(data.errMsg);
					}
				}
			})
		}
	}
})
//修改
$("#btnEdit").on("click",function(){
	var rule_no=$("#tbody").find("tr.on").attr("rule_no");
	if(!rule_no){
		alert("请选择要修改的选项！");
	}else{
		location.href="./gzzd_edit.html?no="+rule_no;
	}
})
</script>
</body>
</html>