/**
 * Io.util.js
 * 1.qser
 * 2.qdata
 * 3.Io.on
 * 4.Io.is
 * 5.Io.ajax
 * 6.Io.totop
 * 7.Io.qrcode
 * 8.Io.end
 * 9.Io.cookie
 * 10.Io.search
 */
var Io = {};

/**
 * 将表单转为js对象
 */
$.fn.qser = function(){
	var obj = {};
	
	var objs = $(this).serializeArray();
	if(objs.length != 0){
		for(var i=0; i<objs.length; i++){
			obj[objs[i].name] = objs[i].value;
		}
	}

	return obj;
};

/** 
 * 将data属性转为js对象
 */
$.fn.qdata = function(){
	var res = {};
	
	var data = $(this).attr('data');
	if(data){
		var options = data.split(';');
		for(var i=0; i<options.length; i++){
			if(options[i]){
				var opt = options[i].split(':');
				res[opt[0]] = opt[1];
			}
		}
	}
	
	return res;
};

/**
 * Io.on
 * 事件绑定
 */
Io.on = function(obj, event, func){
	$(document).off(event, obj).on(event, obj, func);
};

/**
 * Io.is
 * 一些常用的判断，例如数字，手机号等
 */
Io.is = function(str, type){
	if(str && type){
		if(type == 'number') return /^\d+$/g.test(str);
		if(type == 'mobile') return /^1\d{10}$/g.test(str);
	}
};

/**
 * Io.ajax
 * 对$.ajax的封装
 */
Io.ajaxoptions = {
	url 		: '',
	data 		: {},
	type 		: 'post',
	dataType	: 'json',
	async 		: true,
	crossDomain	: false
};
Io.ajaxopt = function(options){
	var opt = $.extend({}, Io.ajaxoptions);
	if(typeof options == 'string'){
		opt.url = options;
	}else{
		$.extend(opt, options);
	}
	
	return opt;
};
Io.ajax = function(options, success, fail){
	if(options){
		var opt = Io.ajaxopt(options);
		if(typeof base != 'undefined') opt.url = base + opt.url;
		
		$.ajax(opt).done(function(obj){
			if(success) success(obj);
		}).fail(function(e){
			if(fail){
				fail(e);
			}else{
				alert('数据传输失败，请重试！');
			}
		});
	}
};

/**
 * Io.totop
 * 返回顶部的方法
 * 可以参考：plugins/_01_qtotop/qtotop.html
 */
Io.totop = function(el){
	var $el = $(el);
	$el.hide().click(function(){
		$('body, html').animate({scrollTop : '0px'});
	});
	
	var $window = $(window);
	$window.scroll(function(){
		if ($window.scrollTop()>0){
			$el.fadeIn();
		}else{
			$el.fadeOut();
		}
	});
};

/**
 * Io.qcode
 * 生成二维码
 * 注：需要引入qrcode，<script src="http://cdn.staticfile.org/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
 * text：待生成文字
 * type：中文还是英文，cn为中文
 * render：展示方式，table为表格方式
 * width：宽度
 * height：高度
 * 可以参考：plugins/_03_qcode/qcode.html
 */
$.fn.qcode = function(options){
	if(options){
		var opt = {};
		if(typeof options == 'string'){
			opt.text = options;
		}else{
			if(options.text) opt.text = options.text;
			if(options.type && options.type == 'ch') opt.text = Io.qrcodetochar(opt.text);
			if(options.render && options.render == 'table') opt.render = options.render;
			if(options.width) opt.width = options.width;
			if(options.height) opt.height = options.height;
		}

		$(this).qrcode(opt);
	}
};
Io.qrcodetochar = function(str){
    var out, i, len, c;
    out = "";
    len = str.length;
    for (i = 0; i < len; i++) {
        c = str.charCodeAt(i);
        if ((c >= 0x0001) && (c <= 0x007F)) {
            out += str.charAt(i);
        } else if (c > 0x07FF) {
            out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
            out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
            out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
        } else {
            out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
            out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
        }
    }
    return out;
};

/**
 * Io.end
 * 到达页面底部后自动加载内容
 * end：到达底部后的回调函数
 * $d：容器，默认是$(window)
 * $c：内容，默认是$(document)
 * 可以参考：plugins/_04_qend/qend.html
 */
Io.end = function(end, $d, $c){
	if(end){
		var $d = $d || $(window);
		var $c = $c || $(document);
		
		$d.scroll(function(){if($d.scrollTop() + $d.height() >= $c.height()) end();});
	}else{
		$(window).scroll(null);
	}
};

/**
 * Io.cookie
 * 对jquery.cookie.js稍作封装
 * 注：需要引入jquery.cookie.js，<script src="http://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
 * Io.cookie(key)：返回key对应的value
 * Io.cookie(key, null)： 删除key对应的cookie
 * Io.cookie(key, value)：设置key-value的cookie
 * 可以参考：plugins/_05_qcookie/qcookie.html
 */
Io.cookie = function(key, value){
	if(typeof value == 'undefined'){
		return $.cookie(key);
	}else if(value == null){
		$.cookie(key, value, {path:'/', expires: -1});
	}else{
		$.cookie(key, value, {path:'/', expires:1});
	}
};

/**
 * Io.search
 * 获取url后参数中的value
 * Io.search(key)：返回参数中key对应的value
 * 可以参考：plugins/_06_qsearch/qsearch.html
 */
Io.search = function(key){
	var res;
	
	var s = location.search;
	if(s){
		s = s.substr(1);
		if(s){
			var ss = s.split('&');
			for(var i=0; i<ss.length; i++){
				var sss = ss[i].split('=');
				if(sss && sss[0] == key) res = sss[1]; 
			}
		}
	}
	
	return res;
};

/**
 * Io.bs.js
 * 1.alert
 * 2.confirm
 * 3.dialog
 * 4.msg
 * 5.tooltip
 * 6.popover
 * 7.tree
 * 8.scrollspy
 * 9.initimg
 * 10.bsdate
 * 11.bstro
 */
Io.bs 	= {};
Io.bs.modaloptions = {
	url 	: '',
	fade	: 'fade',
	close	: true,
	title	: 'title',
	head	: true,
	foot	: true,
	btn		: false,
	okbtn	: '确定',
	qubtn	: '取消',
	msg		: 'msg',
	big		: false,
	show	: false,
	remote	: false,
	backdrop: 'static',
	keyboard: true,
	style	: '',
	mstyle	: ''
};
Io.bs.modalstr = function(opt){
	var start = '<div class="modal '+opt.fade+'" id="bsmodal" tabindex="-1" role="dialog" aria-labelledby="bsmodaltitle" aria-hidden="true" style="position:fixed;top:0px;'+opt.style+'">';
	if(opt.big){
		start += '<div class="modal-dialog modal-lg" style="'+opt.mstyle+'"><div class="modal-content">';
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
		head += '<h3 class="modal-title" id="bsmodaltitle">'+opt.title+'</h3></div>';
	}
	
	var body = '<div class="modal-body"><p><h4>'+opt.msg+'</h4></p></div>';
	
	var foot = '';
	if(opt.foot){
		foot += '<div class="modal-footer"><button type="button" class="btn btn-primary bsok">'+opt.okbtn+'</button>';
		if(opt.btn){
			foot += '<button type="button" class="btn btn-default bscancel">'+opt.qubtn+'</button>';
		}
		foot += '</div>';
	}
	
	return start + head + body + foot + end;
};
Io.bs.alert = function(options, func){
	// options
	var opt = $.extend({}, Io.bs.modaloptions);
	
	opt.title = '提示';
	if(typeof options == 'string'){
		opt.msg = options;
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
	})
	$modal.on('hidden.bs.modal', function(){
		$modal.remove();
		window.top.$(".modal-backdrop").remove();
		$(".modal-backdrop").remove();
	});
	// show
	$modal.modal('show');
	
};

Io.bs.confirm = function(options, ok, cancel){
	// options
	var opt = $.extend({}, Io.bs.modaloptions);

	opt.title = '确认操作';
	if(typeof options == 'string'){
		opt.msg = options;
	}else{
		$.extend(opt, options);
	}
	opt.btn = true;
	
	// append
	var $modal;
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
		if(ok) ok();
		$modal.modal('hide');
	});
	window.top.$('button.bscancel').on('click', function(){
		//if(cancel) cancel();
		if(cancel) cancel();
		$modal.modal('hide');
		window.top.$(".modal-backdrop").remove();
	});
	$modal.on('hidden.bs.modal', function(){
		$modal.remove();
		window.top.$(".modal-backdrop").remove();
		$(".modal-backdrop").remove();
	});
	// show
	$modal.modal('show');
};
Io.bs.dialog = function(options, func){
	// options
	var opt = $.extend({}, Io.bs.modaloptions, options);
	opt.big = true;
	
	// append
	$('body').append(Io.bs.modalstr(opt));
	
	// ajax page
	Io.ajax({url:options.url,dataType:'html'}, function(html){$('#bsmodal div.modal-body').empty().append(html);});
		
	// init
	var $modal = $('#bsmodal'); 
	$modal.modal(opt);
	
	// bind
	Io.on('button.bsok', 'click', function(){
		var flag = true;
		if(func){
			flag = func();
		}
		
		if(flag){
			$modal.modal('hide');
		}
	});
	Io.on('#bsmodal', 'hidden.bs.modal', function(){
		$modal.remove();
	});
	
	// show
	$modal.modal('show');
};
Io.bs.msgoptions = {
	msg  : 'msg',
	type : 'info',
	time : 2000,
	position : 'top',
};
Io.bs.msgstr = function(msg, type, position){
	return '<div class="alert alert-'+type+' alert-dismissible" role="alert" style="display:none;position:fixed;' + position + ':0;left:0;width:100%;z-index:2001;margin:0;text-align:center;" id="bsalert"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'+msg+'</div>';
};
Io.bs.msg = function(options){
	var opt = $.extend({},Io.bs.msgoptions);
	
	if(typeof options == 'string'){
		opt.msg = options;
	}else{
		$.extend(opt, options);
	}
	
	$('body').prepend(Io.bs.msgstr(opt.msg, opt.type , opt.position));
	$('#bsalert').slideDown();
	setTimeout(function(){
		$('#bsalert').slideUp(function(){
			$('#bsalert').remove();
		});
	},opt.time);
};
Io.bs.popoptions = {
	animation 	: true,
	container 	: 'body',
	content		: 'content',
	html		: true,
	placement	: 'bottom',
	title		: '',
	trigger		: 'hover'//click | hover | focus | manual.
};
$.fn.bstip = function(options){
	var opt = $.extend({}, Io.bs.popoptions);
	if(typeof options == 'string'){
		opt.title = options;
	}else{
		$.extend(opt, options);
	}
	
	$(this).data(opt).tooltip();
};
$.fn.bspop = function(options){
	var opt = $.extend({}, Io.bs.popoptions);
	if(typeof options == 'string'){
		opt.content = options;
	}else{
		$.extend(opt, options);
	}
	
	$(this).popover(opt);
};
Io.bs.tree = {};
Io.bs.tree.options = {
	url 	: '',
	height 	: '600px',
	open	: true,
	edit	: false,
	checkbox: false,
	showurl	: true
};
$.fn.bstree = function(options){
	var opt = $.extend({}, Io.bs.tree.options);
	if(options){
		if(typeof options == 'string'){
			opt.url = options;
		}else{
			$.extend(opt, options);
		}
	}
	
	var res = '加载失败！';
	var $this = $(this);
	Io.ajax(opt.url + '/tree', function(json){
		if(json && json.object){
			var tree = json.object;
			
			var start = '<div class="panel panel-info"><div class="panel-body" ';
			if(opt.height != 'auto') 
				start += 'style="height:600px;overflow-y:auto;"';
			start += '><ul class="nav nav-list sidenav" id="treeul" data="url:' + opt.url +';">';
			var children = Io.bs.tree.sub(tree, opt);
			var end = '</ul></div></div>';
			res = start + children + end;
		}
		
		$this.empty().append(res);
		Io.bs.tree.init();
	});
};
Io.bs.tree.sub = function(tree, opt){
	var res = '';
	if(tree){
		var res = 
			'<li>' + 
				'<a href="javascript:void(0);" data="id:' + tree.id + ';url:' + tree.url + ';">' + 
					'<span class="glyphicon glyphicon-minus"></span>';
		if(opt.checkbox){
			res += '<input type="checkbox" class="treecheckbox" ';
			if(tree.checked){
				res += 'checked';
			}
			res += '/>';
		}
			res += tree.text;
		if(opt.showurl){
			res += '(' + tree.url + ')';
		}
		if(opt.edit)
			res += 
				'&nbsp;&nbsp;<span class="label label-primary bstreeadd">添加子菜单</span>' + 
				'&nbsp;&nbsp;<span class="label label-primary bstreeedit">修改</span>' + 
				'&nbsp;&nbsp;<span class="label label-danger  bstreedel">删除</span>';
			res += '</a>';
		var children = tree.children;
		if(children && children.length > 0){
				res += '<ul style="padding-left:20px;" id="treeid_' + tree.id + '" class="nav collapse ';
			if(opt.open) 
				res += 'in';
				res += '">';
			for(var i=0; i<children.length; i++){
				res += Io.bs.tree.sub(children[i], opt);
			}
				res += '</ul>';
		}
		res += '</li>';
	}
	
	return res;
};
Io.bs.tree.init = function(){
	Io.on('#treeul .glyphicon-minus', 'click', function(){
		if($(this).parent().next().length > 0){
			$('#treeid_' + $(this).parents('a').qdata().id).collapse('hide');
			$(this).removeClass('glyphicon-minus').addClass('glyphicon-plus');
		}
	});
	Io.on('#treeul .glyphicon-plus', 'click', function(){
		if($(this).parent().next().length > 0){
			$('#treeid_' + $(this).parents('a').qdata().id).collapse('show');
			$(this).removeClass('glyphicon-plus').addClass('glyphicon-minus');
		}
	});
	Io.on('input.treecheckbox', 'change', function(){
		// 检测子级的
		var subFlag = $(this).prop('checked');
		$(this).parent().next().find('input.treecheckbox').each(function(){
			$(this).prop('checked', subFlag);
		});
		
		// 检测父辈的
		var parentFlag = true;
		var $ul = $(this).parent().parent().parent(); 
		$ul.children().each(function(){
			var checked = $(this).children().children('input').prop('checked');
			if(!checked) parentFlag = false;
		});
		$ul.prev().children('input').prop('checked', parentFlag);
	});
	
	Io.bs.tree.url = $('#treeul').qdata().url;
	if(Io.bs.tree.url){
		Io.on('.bstreeadd', 'click', Io.bs.tree.addp);
		Io.on('.bstreedel', 'click', Io.bs.tree.del);
		Io.on('.bstreeedit', 'click', Io.bs.tree.editp);
	}
};
Io.bs.tree.addp = function(){
	Io.bs.dialog({
		url 	: Io.bs.tree.url + '/add/' + $(this).parent().qdata().id,
		title 	: '添加子菜单',
		okbtn 	: '保存'
	}, Io.bs.tree.add);
};
Io.bs.tree.add = function(){
	var res;
	Io.ajax({url:Io.bs.tree.url + '/save',data:$('#bsmodal').find('form').qser(),async: false}, function(obj){res = obj;});
	
	Io.bs.msg(res);
	if(res && res.type == 'success'){
		Io.crud.url = Io.bs.tree.url;
		Io.crud.reset();
		return true;
	}else{
		return false;
	}
};
Io.bs.tree.del = function(){
	Io.ajax({
		url:Io.bs.tree.url + '/del/' + $(this).parent().qdata().id,
	}, function(res){
		Io.bs.msg(res);
		
		if(res && res.type == 'success'){
			Io.crud.url = Io.bs.tree.url;
			Io.crud.reset();
		}
	});
};
Io.bs.tree.editp = function(){
	Io.bs.dialog({
		url 	: Io.bs.tree.url + '/savep?id=' + $(this).parent().qdata().id,
		title 	: '修改菜单',
		okbtn 	: '保存'
	}, Io.bs.tree.edit);
};
Io.bs.tree.edit = function(){
	Io.crud.url = Io.bs.tree.url;
	return Io.crud.save();
};
Io.bs.spy = function(target,body){
	var $body = 'body';
	var $target = '.scrolldiv';
	
	if(body) $body = body;
	if(target) $target = target;
	
	$($body).scrollspy({target:$target});
};
Io.bs.initimg = function(){
	$('img').each(function(){
		var clazz = $(this).attr('class');
		if(clazz){
			if(clazz.indexOf('img-responsive') == -1) $(this).addClass('img-responsive');
		}else{
			$(this).addClass('img-responsive');
		}
	});
};
Io.bs.bsdateoptions = {
	autoclose: true,
	language : 'zh-CN',
	format: 'yyyy-mm-dd'
};
Io.bs.bsdate = function(selector, options){
	if(selector){
		var $element = $(selector);
		if($element.length > 0){
			var opt = $.extend({}, Io.bs.bsdateoptions, options);
			$element.each(function(){
				$(this).datepicker(opt);
			});
		}
	}
};
Io.bs.bstrooptions = {
	width 	: '500px',
	html 	: 'true',
	nbtext	: '下一步',
	place 	: 'bottom',
	title 	: '网站使用引导',
	content : 'content'
};
Io.bs.bstroinit = function(selector, options, step){
	if(selector){
		var $element = $(selector);
		if($element.length > 0){
			var opt = $.extend({}, Io.bs.bstrooptions, options);
			if(typeof options == 'string'){
				opt.content = options;
			}else{
				$.extend(opt, options);
			}
			
			$element.each(function(){
				$(this).attr({
					'data-bootstro-width'			: opt.width, 
					'data-bootstro-title' 			: opt.title, 
					'data-bootstro-html'			: opt.html,
					'data-bootstro-content'			: opt.content, 
					'data-bootstro-placement'		: opt.place,
					'data-bootstro-nextButtonText'	: opt.nbtext,
					'data-bootstro-step'			: step
				}).addClass('bootstro');
			});
		}
	}
};
Io.bs.bstroopts = {
	prevButtonText : '上一步',
	finishButton : '<button class="btn btn-lg btn-success bootstro-finish-btn"><i class="icon-ok"></i>完成</button>',
	stopOnBackdropClick : false,
	stopOnEsc : false
};
Io.bs.bstro = function(bss, options){
	if(bss && bss.length > 0){
		for(var i=0; i<bss.length; i++){
			Io.bs.bstroinit(bss[i][0], bss[i][1], i);
		}
		
		var opt = $.extend({}, Io.bs.bstroopts);
		if(options){
			if(options.hasOwnProperty('pbtn')){
				opt.prevButtonText = options.pbtn;
			}
			if(options.hasOwnProperty('obtn')){
				if(options.obtn == ''){
					opt.finishButton = '';
				}else{
					opt.finishButton = '<button class="btn btn-mini btn-success bootstro-finish-btn"><i class="icon-ok"></i>'+options.obtn+'</button>';
				}
			}
			if(options.hasOwnProperty('stop')){
				opt.stopOnBackdropClick = options.stop;
				opt.stopOnEsc = options.stop;
			}
			if(options.hasOwnProperty('exit')){
				opt.onExit = options.exit;
			}
		}
		
		bootstro.start('.bootstro', opt);
	}
};
Io.bs.search = function(selector, options){
	if(!selector || !options || !options.url || !options.make || !options.back) return;
	
	var $this = $(selector);
	var zIndex = options.zIndex || 900;
	var bgColor = options.bgColor || '#fff';
	
	var $table = $('<table class="table table-bordered table-hover" style="position:absolute;display:none;margin-top:10px;width:95%;z-index:' + zIndex + ';background-color:' + bgColor + ';"></table>');
	$this.after($table);
	
	var tid,xhr;
	Io.on(selector, 'keyup', function(){
		if(tid) clearTimeout(tid);
		if(xhr) xhr.abort();
		tid = setTimeout(function(){
			var code = $this.val();
			if(code){
				xhr = $.ajax({
					url: base + options.url + '?code=' + code,
					type:'get',
					dataType:'json'
				}).done(function(json){
					if(json && json.type == 'success'){
						var codes = json.object;
						if(codes && codes.length > 0){
							$table.empty();
							$.each(codes, function(i, item){
								if(item) $table.append('<tr class="qsearchtr" style="cursor:pointer;" data-id="' + item.id + '"><td>' + options.make(item) + '</td></tr>');
							});
						}
					}
					
					$table.show();
				});
			}
		}, 500);
	});
	
	Io.on('tr.qsearchtr', 'click', function(){
		options.back($(this).data('id'));
		
		$this.val($(this).find('td').text());
		$table.hide();
	});
};

/**
 * Io.crud
 */
Io.crud = {};
Io.crud.url = '';
Io.crud.init = function(){
	// menu click
	Io.on('.menus', 'click', function(){
		var url = $(this).qdata().url;
		if(url){
			Io.crud.url = url;
			Io.crud.reset();
		}
	});
	Io.crud.bindcrud();
	Io.crud.bindpage();
};
Io.crud.bindcrud = function(){
	Io.on('.allcheck','change', function(){$('.onecheck').prop('checked',$(this).prop('checked'));});
	Io.on('.addBtn', 'click', function(){Io.crud.savep('添加')});
	Io.on('.editbtn','click', function(){Io.crud.savep('修改',$(this).parents('tr').qdata().id)});
	Io.on('.queBtn', 'click', function(){Io.crud.search('查询')});
	Io.on('.relBtn', 'click', function(){Io.crud.reset();});
	Io.on('.delBtn', 'click', function(){Io.crud.del();});
	Io.on('.delbtn', 'click', function(){Io.crud.del($(this).parents('tr').qdata().id);});
};
Io.crud.listopt = {pageNumber:1,pageSize:10};
Io.crud.list = function(data){
	var opt = {url : Io.crud.url + '/index'};
	if(data) $.extend(Io.crud.listopt, data);
	opt.data = Io.crud.listopt;
	opt.dataType = 'html';
	
	Io.ajax(opt, function(html){$('#cruddiv').empty().append(html);});
};
Io.crud.reset = function(){
	Io.crud.listopt = {pageNumber:1,pageSize:10};
	Io.crud.list();
};
Io.crud.search = function(title){
	Io.bs.dialog({title:title,url:Io.crud.url + '/search'}, function(){
		Io.crud.list($('#bsmodal').find('form').qser());
		return true;
	});
};
Io.crud.savep = function(title, id){
	var url = id ? (Io.crud.url + '/savep?id=' + id) : (Io.crud.url + '/savep');
	Io.bs.dialog({title:title,url:url}, function(){
		return Io.crud.save();
	});
};
Io.crud.save = function(){
	var res;
	Io.ajax({
		async: false,
		url:Io.crud.url+'/save',
		data:$('#bsmodal').find('form').qser()
	}, function(json){
		res = json;
	});
	
	Io.bs.msg(res);
	if(res && res.type == 'success'){
		Io.crud.list();
		return true;
	}else{
		return false;
	}
};
Io.crud.del = function(id){
	var ids = [];
	
	if(id){
		ids.push(id);
	}else{
		$('.onecheck:checked').each(function(){ids.push($(this).parents('tr').qdata().id);});
	}
	
	if(!ids.length){
		Io.bs.alert('请选择要删除的记录！');
	}else{
		Io.bs.confirm('确认要删除所选记录吗（若有子记录也会同时删除）？',function(){
			Io.ajax({
				url:Io.crud.url+'/del',
				data:{ids:ids.join(',')}
			}, function(res){
				Io.bs.msg(res);
				Io.crud.list();
			});
		});
	}
};
Io.crud.bindpage = function(){
	Io.on('.crudfirst', 'click', function(){
		if(!$(this).parent().hasClass('disabled')){
			Io.crud.reset();
		}
	});
	Io.on('.crudprev', 'click', function(){
		if(!$(this).parent().hasClass('disabled')){
			Io.crud.list({pageNumber:Io.crud.listopt.pageNumber - 1});
		}
	});
	Io.on('.crudnext', 'click', function(){
		if(!$(this).parent().hasClass('disabled')){
			Io.crud.list({pageNumber:Io.crud.listopt.pageNumber + 1});
		}
	});
	Io.on('.crudlast', 'click', function(){
		if(!$(this).parent().hasClass('disabled')){
			Io.crud.list({pageNumber:$(this).qdata().page});
		}
	});
	Io.on('.cruda', 'click', function(){
		if(!$(this).parent().hasClass('disabled')){
			Io.crud.list({pageNumber:parseInt($(this).text())});
		}
	});
	Io.on('.pagesize', 'change', function(){
		var value = $(this).val();
		if(value){
			Io.crud.listopt.pageSize = value;
		}
		
		Io.crud.list({pageSize:value});
	});
};