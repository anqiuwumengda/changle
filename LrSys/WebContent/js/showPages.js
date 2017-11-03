;(function($){
    $.showPages=function(domId,total,rows,currentPage,fun){
        var dom = document.getElementById(domId);
        var pages=Math.ceil(total/rows),
            first=1,
            prev=currentPage<=1?1:(currentPage-1),
            next=currentPage==pages?currentPage:(currentPage+1),
            last=pages;
        var html='',pageLen=pages>7?7:pages;
        if(currentPage==1){
        	html+='<li><a class="first">首页</a></li>';
        	html+='<li><a class="prev">&lt;上一页</a></li>';
        }else{
        	html+='<li><a class="first" href="javascript:'+fun+'('+first+','+rows+')" >首页</a></li>';
        	html+='<li><a class="prev" href="javascript:'+fun+'('+prev+','+rows+')">&lt;上一页</a></li>';
        }
        
        var sum=pages>7?7:pages,
            start=1;
        if(currentPage<=3){
            start=1;
        }else if(currentPage>3 && currentPage<=pages-3){
            start=currentPage-3;
        }else if(currentPage>pages-3){
            if(currentPage>6){
                start=pages-6;
            }else if(currentPage<=6){
                start=1;
            }
        }
        for(var i=0,start;i<sum;i++,start++){
            if(start==currentPage){
                html+='<li class="active"><a>'+start+'</a></li>';
            }else{
                html+='<li><a href="javascript:'+fun+'('+start+','+rows+')">'+start+'</a></li>';
            }
        }
        if(currentPage==pages){
        	html+='<li><a class="next" >下一页&gt;</a></li>';
            html+='<li><a class="last">尾页</a></li>';
        }else{
        	html+='<li><a class="next" href="javascript:'+fun+'('+next+','+rows+')" >下一页&gt;</a></li>';
            html+='<li><a class="last" href="javascript:'+fun+'('+last+','+rows+')">尾页</a></li>';
        }
        if(total>10){
        	var selectArr = [10,20];
        	if(total>20) selectArr.push(30);
        	if(total>30) selectArr.push(50);
        	if(total>50) selectArr.push(100);
        	html+='<li class="pl15 crblue">显示条数 <select id="stPage" class="ml5">';
        	for(var i=0;i<selectArr.length;i++){
        		html+='<option value="'+selectArr[i]+'">'+selectArr[i]+'</option>'; 		
        	}
        	html+='</select></li>';        	
        }
        dom.innerHTML=html;
        if(total>10){
        	$("#stPage").val(rows);
        	Io.on("#stPage","change",function(){
        		pagerList(1,$(this).val());
        	});
        }
    }
})(jQuery);