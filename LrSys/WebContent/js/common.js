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
var baseurl="http://67.39.0.122:9000/LrSys/";
if(isPC()){
	//baseurl="http://47.93.251.118:8080/LrSys/"; //电脑登录ip
	baseurl="http://127.0.0.1:8080/LrSys/"; //电脑登录ip
}else{
	//baseurl="http://172.17.64.70:9000/LrSys/"; //pad登录ip
	baseurl="http://192.168.116.132:8080/LrSys/"; //pad登录ip
}
getUrlParam = function(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
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
		selectEva(x,json[x]);
		
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
						
						if(length!="0"){
							if(n==length){
								func();
							}
						}
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
	var $modal;
	// add
	if(window.parent.frames[0]){
		window.top.$('body').append(Io.bs.modalstr(opt));
		$modal = window.top.$('#bsmodal'); 
	}else{
		$('body').append(Io.bs.modalstr(opt));
		$modal = $('#bsmodal');
	}
	
	$modal.modal(opt);
	
	// bind
	window.top.$('button.bsok').on('click', function(){
		if(func) func();
		$modal.remove();
		window.top.$(".modal-backdrop").remove();
	});
	Io.on("button.bsok","click",function(){
		if(func) func();
		$modal.remove();
		$(".modal-backdrop").remove();
	});
	$modal.on('hidden.bs.modal', function(){
		$modal.remove();
		window.top.$(".modal-backdrop").remove();
		$(".modal-backdrop").remove();
	});
	// show
	$modal.modal('show');
};