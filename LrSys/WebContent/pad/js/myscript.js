//初始化page-inner高度
/*var bHeight = 0,bWidth = 0;
$(function(){
	bHeight = $("body").height();
	bWidth = $("body").width();
	$(".page-inner").height(bHeight-100);
	$("#tabFrame").height(bHeight-70);
});
//屏幕反转刷新page-inner高度
window.addEventListener("orientationchange", function() {
	if(window.orientation==0||window.orientation==180){
		$(".page-inner").height(bHeight-100);
		$("#tabFrame").height(bHeight-70);
	}else{
		$(".page-inner").height(bWidth-100);
		$("#tabFrame").height(bWidth-70);
	}
	
	
}, false);*/
//————————————————————————————————————————————————————————————//
//判断是否是PC登录
function isPC() {
    var userAgentInfo = navigator.userAgent;
    var Agents = ["Android", "iPhone",
                "SymbianOS", "Windows Phone",
                "iPad", "iPod"];
    var flag = true;
    for (var v = 0; v < Agents.length; v++) {
        if (userAgentInfo.indexOf(Agents[v]) > 0) {
            flag = false;
            break;
        }
    }
    return flag;
}
//获取url
var baseurl="http://67.39.0.122:9000/LrSys/"; //http://10.16.141.97:8080
if(isPC()){
	baseurl="http://127.0.0.1:8080/LrSys/"; //电脑登录ip
}else{
	//baseurl="http://172.17.64.70:9000/LrSys/"; //pad登录ip
	baseurl="http://192.168.0.157:8080/LrSys/"; //pad登录ip
}
function backkhgl(){
	var url = "";
	var unit = getFramesUrlParam("unit")||"";
	if(unit=="unitsearch"){
		url = "/LrSys/pad/khgl/unitsearch.html?unit=1";
	}else{
		url = "/LrSys/pad/khgl/khgl_main.jsp?swc=1";
	}
	window.top.frames["tabFrame"].location.href=url;
}
//当前日期
var date=new Date();
var y=date.getFullYear();
var m=date.getMonth()+1;
var d=date.getDate();
var DD=date.getDay();
var newDate=y +"-" + m + "-" + d;
//日期+0
function geTime(i){
	if(i<=9){
		return '0'+i;
	}else{
		return i;
	}
}

//iframe自适应高度
function iFrameHeight(frame) {   
	var ifm= document.getElementById(frame);
	if(ifm==null) return;
	var subWeb = document.frames ? document.frames[frame].document : ifm.contentDocument;    
	//console.log(1,subWeb.getElementsByTagName("html")[0].offsetHeight);
	if(ifm != null && subWeb != null) {
	   ifm.height = subWeb.getElementsByTagName("html")[0].offsetHeight;
	}
} 

//获取url的参数的值
getUrlParam = function(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r!=null) return decodeURI(r[2]); return null;
};
//获取父级url的参数的值
getParentUrlParam = function(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.top.location.search.substr(1).match(reg);
    if (r!=null) return decodeURI(r[2]); return null;
};
//获取framesurl的参数的值
getFramesUrlParam = function(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.top.frames["tabFrame"].location.href.match(reg);
    if (r!=null) return decodeURI(r[2]); return null;
};
//select枚举值选项
function selectOnload(settings){
	var defaultSetting = {
			json:{},//
			func:"",//回调函数名 string
			lenght:0,//json长度
			isDefault:true,//是否开启默认值
			isElse:true//联动默认值开启
	};
	$.extend(defaultSetting,settings);
	var json = defaultSetting.json,
		func = defaultSetting.func,
		length = defaultSetting.length,
		isDefault = defaultSetting.isDefault,
		isElse = defaultSetting.isElse;
	var n = 0;	
	for(var x in json){// id:val
		selectEva(x,json[x]);//
		
	}
	function selectEva(id,val){		
		var data1={"DIC_PARENTID":val};
		$.ajax({
			type:"post",
			url:baseurl+"lrddic/query",
			dataType:"json",
			contentType:"application/json",
			data:JSON.stringify(data1),
			success:function(data){
				if(data.code=="00"){
					n++;
					var ids=$("#"+id);
					if(data.result!=null && data.result.length){
						var result=data.result;
						var html="";
						if(!isDefault) html+='<option value="">-请选择-</option>';
						for(var i=0;i<result.length;i++){
							html+='<option value="'+ result[i].DIC_ID + '" >'+ result[i].DIC_NAME +'</option>';
						}
						ids.html(html);	
						
						$("#"+id).on("change",function(){
							var $this=$(this);
							var childVal=$this.val();
							var next=$this.next("select");
							var ida ={};
							if(next.size()>0){
								if(!next.attr("id")){
									next.attr("id",id+"2");
								}
								var id = next.attr("id");
								if(childVal==""){									
									return;
								}
								ida[id] = childVal;
								if(isElse){
									selectOnload({"json":ida}); 
								}else{
									selectOnload({"json":ida,"isDefault":false}); 
								}
																
							}
						});
					}else{
						ids.html("<option value=''>-请选择-</option>");
					}
				}
			}
		});
	}
	if(length!="0"&&func){
		func();
	}
}



/**
 * Io.on
 * 事件绑定
 */
var Io = {};
Io.on = function(obj, event, func){
	$(document).off(event, obj).on(event, obj, func);
};

/**
 * Io.bs
 * Io.bs.modaloptions
 * Io.bs.modalstr
 * 1.alert
 * 2.confirm
 * 3.dialog
 * 4.msg
 */
Io.bs 	= {};
Io.bs.modaloptions = {
	url 	: '',
	fade	: 'fade',
	close	: true,
	title	: '弹窗',
	head	: true,
	foot	: true,
	btn		: false,
	okbtn	: '确定',
	qubtn	: '取消',
	msg		: 'msg',
	size	: false,
	show	: false,
	remote	: false,
	backdrop: false,
	keyboard: false,
	style	: '',
	mstyle	: ''
};
Io.bs.modalstr = function(opt){
	var start = '<div class="modal '+opt.fade+'" id="bsmodal" tabindex="-1" role="dialog" aria-labelledby="bsmodaltitle" aria-hidden="true" style="'+opt.style+'">';
	if(opt.size){
		start += '<div class="modal-dialog modal-'+opt.size+'" style="'+opt.mstyle+'"><div class="modal-content">';
	}else{
		start += '<div class="modal-dialog" style="'+opt.mstyle+'"><div class="modal-content">';
	}
	var end = '</div></div></div>';
	
	var head = '';
	if(opt.head){
		head += '<div class="modal-header">';
		if(opt.close){
			head += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
		}
		head += '<h4 class="modal-title" id="bsmodaltitle">'+opt.title+'</h4></div>';
	}
	
	var body = '<div class="modal-body"><div class="tipMsg">'+opt.msg+'</div></div>';
	
	var foot = '';
	if(opt.foot){
		foot += '<div class="modal-footer"><button type="button" class="btn btn-primary bsok">'+opt.okbtn+'</button>';
		
		if(opt.qubtn){
			foot += '<button type="button" class="btn btn-default bscancel" data-dismiss="modal">'+opt.qubtn+'</button>';
		}
		foot += '</div>';
	}
	
	return start + head + body + foot + end;
};
Io.bs.alert=function(options, func){
	var opt=$.extend({},Io.bs.modaloptions);
	opt.title = '提示';
	if(typeof options=="string"){
		opt.msg=options;
	}else{
		$.extend(opt, options);
	}
	//append body
	$('body').append(Io.bs.modalstr(opt));
	var $modal = $('#bsmodal');
	$modal.modal(opt);
	//bind
	Io.on("button.bsok","click",function(){
		if(func) func();
		$modal.modal('hide');
	});
	$modal.on("hidden.bs.modal",function(){
		$modal.remove();
		$(".modal-backdrop").remove();
	});
	//show
	$modal.modal('show');
};

//修改模态框插件的 默认参数 
$.fn.modal.Constructor.DEFAULTS.keyboard=false;