<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="l" uri="/WEB-INF/lrd.tld"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no">
<title>新增/编辑-客户管理-乐融微贷管理系统</title>
<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/myscript.js"></script>
<link rel="stylesheet" href="../css/bootstrap.min.css">
<link rel="stylesheet" href="../css/pages.css">
</head>
<body class="white-bg">
<form class="form-horizontal">
	<div class="clearfix">
		<div class="col-md-6 clearfix bs-form-group">
			<label class="bs-sm-l control-label">客户类型：</label>
			<l:lselect  divClass="bs-sm-r" selectClass="form-control" selectName="custType" selectId="custType" selected="CZJM" dicId="Cust_Type"></l:lselect>
			<!-- <div class="bs-sm-r">
				<select class="form-control" name="custType" id="custType">
					<option value="" selected>--请选择--</option>
					<option value="CZJM" >城镇居民</option>
					<option value="NCJM">农村居民</option>
					<option value="GTGS">个体工商户</option>
					<option value="XWQY">小微企业</option>
				</select>
			</div> -->
		</div>
	</div>
	<div id="jvalid">
		<div class="clearfix">
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label" id="labelName"><b class="red">*</b> 姓名：</label>
				<div class="bs-sm-r">
					<input type="text" class="form-control" id="nameA">
				</div>
			</div>
		</div>
		<div class="clearfix">
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label"><b class="red">*</b> 联系电话：</label>
				<div class="bs-sm-r">
					<input type="text" class="form-control" id="telephoneA">
				</div>
			</div>
		</div>
		<div class="clearfix">
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label"><b class="red">*</b> 所属区域：</label>
				<div class="bs-sm-r pt7">
					<select id="isArea1">
						<option value="" selected>-请选择-</option>
						<option value="城关街道">城关街道</option>
						<option value="宝城街道">宝城街道</option>
						<option value="朱刘街道">朱刘街道</option>
					</select> 
					<select id="isArea2">
						<option value="" selected>-请选择-</option>
						<option value="东南村社区">东南村社区</option>
						<option value="南关社区">南关社区</option>
						<option value="西村社区">西村社区</option>
					</select>
				</div>
			</div>
		</div>
	</div>
	
	<div class="none" id="jone">
		<div class="clearfix">
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label"><b class="red">*</b> 姓名：</label>
				<div class="bs-sm-r">
					<input class="form-control" id="isName" />
				</div>
			</div>
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">年龄：</label>
				<div class="bs-sm-r">
					<input class="form-control" id="age" />
				</div>
			</div>
		</div>
		<div class="clearfix">
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">性别：</label>
				<div class="bs-sm-r radio">
					<label><input type="radio" name="sex" value="01" checked>男</label>
					<label><input type="radio" name="sex" value="02">女</label>
				</div>
			</div>
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label"><b class="red">*</b> 联系电话：</label>
				<div class="bs-sm-r">
					<input class="form-control" id="telephone"/>
				</div>
			</div>
		</div>
		<div class="clearfix">
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">证件类型：</label>
				<div class="bs-sm-r">
					<select class="form-control" id="cartType">
						<option value="" selected>--请选择--</option>
						<option value="01">身份证</option>
						<option value="02">军官证</option>
						<option value="03">护照</option>
						<option value="04">港澳台通行证</option>
					</select>
				</div>
			</div>
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">证件号码：</label>
				<div class="bs-sm-r">
					<input class="form-control" id="cartNumber"/>
				</div>
			</div>
		</div>
		<div class="clearfix">
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">学历：</label>
				<div class="bs-sm-r">
					<select class="form-control" id="eduLevel">
						<option value="" selected>--请选择--</option>
						<option value="01">文盲或半文盲</option>
						<option value="02">小学</option>
						<option value="03">初中</option>
						<option value="04">高中</option>
						<option value="05">中等专业或技术学校</option>
						<option value="06">大学专科或专科学校</option>
						<option value="07">大学本科</option>
						<option value="08">研究生及以上</option>
					</select>
				</div>
			</div>
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label"><b class="red">*</b>职业：</label>
				<div class="bs-sm-r">
					<select class="form-control" id="vocation">
						<option value="" selected>--请选择--</option>
						<option value="01">国家公务员</option>
						<option value="02">公办学校在编教师</option>
						<option value="03">公立医院在编医护人员</option>
						<option value="04">其他事业单位在编人员</option>
						<option value="05">通讯、电力、烟草、石化、交通、金融、新闻出版等行业企业正式员工</option>
						<option value="06">具有律师、注册会计师等从业资格证的高级专业人才</option>
						<option value="07">其他优质企业中层以上管理人员</option>
						<option value="08">其他优质企业员工</option>
						<option value="09">一般企业中层以上管理人员</option>
						<option value="10">一般企业员工</option>
						<option value="11">下岗职工</option>
						<option value="12">个体经营</option>
						<option value="13">自由职业</option>
						<option value="14">农户</option>
						<option value="15">其他</option>
					</select>
				</div>
			</div>
		</div>
		<div class="clearfix">
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">健康状况：</label>
				<div class="bs-sm-r">
					<select class="form-control" id="health">
						<option value="" selected>--请选择--</option>
						<option value="01">良好</option>
						<option value="02">一般</option>
						<option value="03">较差</option>
					</select>
				</div>
			</div>
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">所属区域：</label>
				<div class="bs-sm-r">
					<span class="form-control" id="isArea"></span>
				</div>
			</div>
		</div>
		<div class="clearfix">
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">所属客户群：</label>
				<div class="bs-sm-r pt7">
					<select id="custGrp">
						<option value="" selected>--请选择--</option>
						<option value="01">居民小区</option>
						<option value="02">事业单位</option>
						<option value="03">专业市场</option>
						<option value="04">工业园区</option>
						<option value="05">沿街商户</option>
					</select> 
					<select id="custGrp2">
						<option value="" selected>--请选择--</option>
						<option value="乐港公寓">乐港公寓</option>
						<option value="中央帝景">中央帝景</option>
						<option value="帝都国际">帝都国际</option>
					</select>
				</div>
			</div>
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label small">所属客户经理：</label>
				<div class="bs-sm-r">
					<input class="form-control" id="custGrpJl"/>
				</div>
			</div>
		</div>
		<div class="clearfix">
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label small">家庭或经营地址：</label>
				<div class="bs-sm-r">
					<input class="form-control" id="myAdress" />
				</div>
			</div>
		</div>
	</div>
	<div class="none" id="jtwo">
		<div class="clearfix">
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label"><b class="red">*</b> 企业名称：</label>
				<div class="bs-sm-r">
					<input class="form-control" id="storeName" />
				</div>
			</div>
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">注册资本：</label>
				<div class="bs-sm-r">
					<input class="form-control" id="registCapital" />
				</div>
			</div>
		</div>
		<div class="clearfix">
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">法定代表人：</label>
				<div class="bs-sm-r">
					<input class="form-control" id="legalReprese" />
				</div>
			</div>
		</div>
		<div class="clearfix">
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">证件类型：</label>
				<div class="bs-sm-r">
					<select class="form-control" id="legalRepreseCertType">
						<option value="" selected>--请选择--</option>
						<option value="01">身份证</option>
						<option value="02">军官证</option>
						<option value="03">护照</option>
						<option value="04">港澳台通行证</option>
					</select>
				</div>
			</div>
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">证件号码：</label>
				<div class="bs-sm-r">
					<input class="form-control" id="legalRepreseCerdNumber" />
				</div>
			</div>
		</div>
		<div class="clearfix">
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">实际控制人：</label>
				<div class="bs-sm-r">
					<input class="form-control" id="acturlController" />
				</div>
			</div>
		</div>
		<div class="clearfix">
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">证件类型：</label>
				<div class="bs-sm-r">
					<select class="form-control" id="acturlControllerCertType">
						<option value="" selected>--请选择--</option>
						<option value="01">身份证</option>
						<option value="02">军官证</option>
						<option value="03">护照</option>
						<option value="04">港澳台通行证</option>
					</select>
				</div>
			</div>
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">证件号码：</label>
				<div class="bs-sm-r">
					<input class="form-control" id="acturlControllerCertNumber" />
				</div>
			</div>
		</div>
		<div class="clearfix">
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">联系人：</label>
				<div class="bs-sm-r">
					<input class="form-control" id="contactName"/>
				</div>
			</div>
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">联系电话：</label>
				<div class="bs-sm-r">
					<input class="form-control" id="telephoneB"/>
				</div>
			</div>
		</div>
		<div class="clearfix">
			<div class="col-md-6 clearfix bs-form-group">
				<label class="bs-sm-l control-label">基本户开户行：</label>
				<div class="bs-sm-r">
					<select id="openBank" class="form-control">
						<option value="" selected>-请选择-</option>
						<option value="01">中国银行</option>
						<option value="02">中国农业银行</option>
						<option value="03">中国工商银行</option>
						<option value="04">中国建设银行</option>
						<option value="05">中国交通银行</option>
						<option value="06">中国邮政储蓄银行</option>
						<option value="07">中国民生银行</option>
						<option value="08">招商银行</option>
						<option value="09">中信银行</option>
						<option value="10">农商行</option>
						<option value="11">潍坊银行</option>
						<option value="12">村镇银行</option>
						<option value="13">其他银行</option>
					</select>
				</div>
			</div>
		</div>
	</div>
	<div class="clearfix text-center tab-footer">
		<button class="btn btn-md btn-danger" type="reset">重置</button>
		<button class="btn btn-md btn-danger" type="button" id="submitAdd">验证</button>
		<button class="btn btn-md btn-danger" type="button" onclick="javascript:history.go(-1);">返回</button>
	</div>
	<p class="red text-center">（注：带 * 号的为必填项）</p>
	<div class="clearfix"></div>
</form>

<script>
var custType=$("#custType"); //客户类型
var isName=$("#isName"); //客户姓名
var telephone=$("#telephone"); //电话
var age=$("#age"); //年龄
var cartType=$("#cartType"); //证件类型
var cartNumber=$("#cartNumber"); //证件号码

var telephoneB=$("#telephoneB"); //联系电话

custType.on("change",function(){
	if(custType.val()=="XWQY"){
		$("#labelName").html('<b class="red">*</b> 企业名称：');
	}else{
		$("#labelName").html('<b class="red">*</b> 姓名：');
	}
}) 
Io.on("#submitAdd","click",function(){
	var namea=$("#nameA").val();
	var telephonea=$("#telephoneA").val();
	var isArea1=$("#isArea1").val();
	var isArea2=$("#isArea2").val();
	var reg = /^0?1[3|4|5|7|8][0-9]\d{8}$/;
	if(reg.test(telephonea)){
		if(namea&&custType.val()&&isArea1&&isArea2&&vocation.val()){
			$("#jvalid").hide();
			custType.attr("disabled","true");
			$("#isName,#telephone,#isArea,#storeName,#telephoneB").attr("disabled","true");
			var data1;
			if(custType.val()=="XWQY"){
				$("#jtwo").show();
				$("#jone").hide();
				$("#isArea").text(isArea1+isArea2);
				$("#storeName").val(namea);
				$("#telephoneB").val(telephonea);
				data1={
						"CUST_TYPE":"XWQY",
						"CUST_NAME":namea,
						"TEL_NO":telephonea,
						"ID_TYPE":cartType.val(),
						"ID_NO":cartNumber.val(),
						"CAPITAL_AMT":registCapital.val(),
						"FR_NAME":legalReprese.val(),
						"ACT_JL":acturlController.val(),
						"ACT_ID_TYPE":acturlControllerCertType.val(),
						"ACT_ID_NO":acturlControllerCertNumber.val(),
						"LXR":contactName.val(),
						"BANK_NAME":openBank.val()
				};
			}else{
				$("#jone").show();
				$("#jtwo").hide();
				$("#isName").val(namea);
				$("#telephone").val(telephonea);
				$("#isArea").text(isArea1+isArea1);
				data1={
						"CUST_TYPE":custType.val(),
						"CUST_NAME":namea,
						"TEL_NO":telephonea,
						"USER_AGE":age.val(),
						"ID_TYPE":cartType.val(),
						"ID_NO":cartNumber.val(),
						"SEX":$("[name=sex]:checked").val(),
						"EDU_LEVEL":eduLevel.val(),
						"VOCATION":vocation.val(),
						"HEALTHY":healthy.val(),
						"AREA_CD":isArea1,
						"AREA_CD2":isArea2,
						"CUST_GRP":custGrp.val(),
						"CUST_GRP2":custGrp2.val(),
						"CUST_GRP_JL":custGrpJl.val(),
						"ADDRESS":myAdress.val()
				};
			}
			
			$("#submitAdd").text("保存");
			$("#submitAdd").on("click",function(){
				$.ajax({
					type:"post",
					url:"custbase/saveCust",
					dataType:"json",
					contentType:"application/json",
					data:JSON.stringify(data1),
					success:function(data){
						if(data.code=="00"){
							window.top.location.href="../kehuguanli.html";
						}else{
							console.log(data.errMsg);
						}
					},error:function(data){
						alert("错误");
					}
				}) 
			})
		}else{
			alert("带 * 号的为必填项");
		}
	}else{
		alert("手机号不正确");
		return false;
	}
})
</script>
</body>
</html>