<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
		<title>系统参数</title>
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
		<div class="pane_btn col-md-12">
			<b:lbutton buttonId="btnadd" buttonClass="btn btn-primary btn-sm" iClass="fa fa-plus" buttonName="添加" funcCd="010000000104"></b:lbutton>
				<b:lbutton buttonId="edit" buttonClass="btn btn-primary btn-sm" iClass="fa fa-edit" buttonName="修改" funcCd="010000000204"></b:lbutton>
			
		</div>
		<div class="table-responsive col-md-12" style="padding: 0;">
			<table class="table table-bordered table-hover">
				<thead>
					<tr>
						<th>序号</th>
						<th>法人号</th>
						<th>系统参数标识符</th>
						<th>系统参数值</th>
						<th>系统参数说明</th>
					</tr>
				</thead>
				<tbody id="parameter">					
				</tbody>
			</table>
		</div>
<div id="myModal" class="modal fade" tabindex="-1" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content modal_content_sm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h5 class="modal-title" id="myModalLabel1">修改参数值</h5>
			</div>
			
				<div class="clearfix bs-form-group">
					<div class="bs-sm-r">
						<input type='hidden' class="form-control"  id="CHPARAKEY"/>
					</div>
				</div>
			
			<div class="modal-body clearfix">
				<div class="clearfix bs-form-group">
					<label class="bs-sm-l control-label" id="CHPARADESC"></label>
					<div class="bs-sm-r">
						<textarea class="form-control"  id="CHPARAVALUE"></textarea>
					</div>
				</div>
			</div>
		    
				<div class="clearfix bs-form-group">
					<div class="bs-sm-r">
						<input type='hidden' class="form-control"  id="CORP_CD"/>
					</div>
				</div>
			
			
			<div class="modal-footer">
				<button class="btn btn-primary" id="btnsave">保存</button>
			    <button class="btn btn-default" data-dismiss="modal">返回</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
Io.on("#btnadd","click",function(){	
	location.href="./sysParameter_add.html";
});
Io.on("#parameter tr","click",function(){
	$(this).addClass("on").siblings().removeClass("on");
});
function upview(){
		$.ajax({
			type:"post",
			url:baseurl+"lrdsyspara/queryByPK",
			dataType:"json",
			contentType:"application/json",
			data:JSON.stringify(),
			success:function(data){
				if(data.code=="00"){
					if(null!=data.result && data.result.length){
						var resultArr=data.result;
						var html="";
						for(var i=0;i<resultArr.length;i++){
							var result = resultArr[i];
							html+='<tr CHPARAKEY="'+result.CHPARAKEY+'" CHPARADESC="'+result.CHPARADESC+'" CHPARAVALUE="'+result.CHPARAVALUE+'" CORP_CD="'+result.CORP_CD+'">';
							html+='<td class="text-center">' + (i+1) + '</td>';						
							html+='<td>' + result.CORP_CD + '</td>';						
							html+='<td>' + result.CHPARAKEY + '</td>';
							html+='<td>' + result.CHPARAVALUE + '</td>';
							html+='<td>' + result.CHPARADESC + '</td>';
							html+='</tr>';
						}
						$("#parameter").html(html);
					}
				}else{
					alert(data.errMsg);
				}
			}
	})
}
upview();
$("#edit").on("click",function(){
			var zthis = $("#parameter").find("tr.on");
			if(zthis.attr("CHPARADESC")){
			$("#CHPARADESC").html(zthis.attr("CHPARADESC")+" :");//系统参数说明
			$("#CHPARAVALUE").val(zthis.attr("CHPARAVALUE"));//系统参数值
			$("#CHPARAKEY").val(zthis.attr("CHPARAKEY"));//系统参数标识符
			$("#CORP_CD").val(zthis.attr("CORP_CD"));//法人号
			$("#myModal").modal({
				backdrop:false
			});
			}else{
				alert("请选择一条(参数说明和值不能为空)");
			}
		});
		
$("#btnsave").on("click",function(){
			var data={
				"CHPARAVALUE":$("#CHPARAVALUE").val(),//系统参数值
				"CHPARAKEY":$("#CHPARAKEY").val(),//系统参数标识符
				"CORP_CD":$("#CORP_CD").val()//法人号
			};
			$.ajax({
				type:"post",
				url:baseurl+"lrdsyspara/update",
				dataType:"json",
				contentType:"application/json",
				data:JSON.stringify(data),
				success:function(data){
					if(data.code=="00"){
						alert("修改成功")
						$("#myModal").modal("hide").on("hidden.bs.modal");
						upview();
					}else{
						alert(data.errMsg);
					}
				}
			})
});
		</script>
	</body>
</html>