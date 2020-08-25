//按钮样式
$(function(){
	$(".btn").mouseover(function(){
		$(this).find(".lf_btn").css("background-position","0 -60px");
		$(this).find(".rf_btn").css("background-position","right -60px");
	});
	$(".btn_sp").mouseover(function(){
		$(this).find(".lf_btn").css("background-position","0 -90px");
		$(this).find(".rf_btn").css("background-position","right -90px");
	});
	$(".btn").mouseleave(function(){
		$(this).find(".lf_btn").css("background-position","0 0");
		$(this).find(".rf_btn").css("background-position","right 0");
	});
	$(".btn_sp").mouseleave(function(){
		$(this).find(".lf_btn").css("background-position","0 -30px");
		$(this).find(".rf_btn").css("background-position","right -30px");
	});
	
	$(".btn").mousedown(function(){
		$(this).find(".lf_btn").css("background-position","0 -120px");
		$(this).find(".rf_btn").css("background-position","right -120px");
	});
	
	$(".btn_sp").mousedown(function(){
		$(this).find(".lf_btn").css("background-position","0 -150px");
		$(this).find(".rf_btn").css("background-position","right -150px");
	});
});

var menuObj = null;
var iframeObj = null;
var leftWrap = null;
var rightWrap = null;
//	var menuItem = $(".menuItem");
var maskObj = null;
$(function(){
	//变量
	menuObj = $(".lf_menu");
	iframeObj = $(".if_main");
	leftWrap = $(".leftWrap");
	rightWrap = $(".rightWrap");
//	var menuItem = $(".menuItem");
	maskObj = $(".maskWrap");
	//	var expandObj = $(".menu>dl>dt");
	//导航菜单
	var nav = $(".menu");
	nav.find("a").on("mouseenter",function(){
		if($(this).next().length>0){
			if(!$(this).hasClass("menuParentA")){
				var w = $(this).parent().parent().width();
				$(this).next().css({"position":"absolute","left":w,"top":0});
				console.log("二级");
			}
			//$(this).next().find("a").removeClass("on");
			$(this).next().show();
			$(this).siblings().removeClass("on");
		}
	});
	nav.find("a").on("mouseleave",function(){
		if($(this).next().length>0){
			$(this).next().hide();
			$(this).removeClass("on");
		}
	});

	nav.find("ul").on("mouseenter",function(){
		$(this).show();
		if(!$(this).prev().hasClass("menuParentA")){
			$(this).prev().addClass("on");
		}
	});

	nav.find("ul").on("mouseleave",function(){
		$(this).hide();
		$(this).prev().removeClass("on");
	});	
	 //点击菜单		
	$(".menuParentA").on("click",function(){
		$(this).addClass("active");
		$(this).parent().siblings().find("a").removeClass("active");
	});
	
   //切换背景
	$(".switch").click(function(){
		if($(".theme").is(":hidden")){
			$(".theme").slideDown("slow");
			$(this).find("i").css("background-position","0 -45px");			
		}else{
			$(".theme").slideUp("slow");
			$(this).find("i").css("background-position","0 -54px");		
		}
	});
	
	//首页系统公告斑马线
	$(".newList li:even").addClass("even");
    $(".newList li:odd").addClass("odd");

//    tabClick('.menu li'.first());
				//默认点击第一个
	
	//设置默认高度
	rightWrap.height(menuObj.height());
	
	//左侧菜单点击
	menuObj.on("click",".menuItem",function(){
		maskObj.show();
		$(this).parents(".lf_menu").find(".menuItem").removeClass("on");
		$(this).addClass("on");
		iframeObj.contents().find("body").remove();
		iframeObj.attr("src","");
		iframeObj.attr("src",$(this).find("a").attr("post"));
		iframeObj.load(function(){
			changeHeight();
			maskObj.hide();
		});
	});

	$(".menuParent :first").click();
//	changeWidth();
});

//对比得到最大高度并赋给各包块
function changeHeight(){
	var r_h = iframeObj.contents().find("body").height();
	// 菜单页面显示高度不低于500
	if(r_h < 500){
		r_h = 600;
	}
	$(".rightWrap").height(r_h);
};

function changeWidth(){
//	console.info($(".leftWrap").is(":hidden"));
	if($(".leftWrap").is(":hidden")){
		$(".rightWrap").width(980);
	}else{
//		console.info("here");
		$(".rightWrap").width(805);
	}
};

//导航栏点击
function tabClick(obj) {
	maskObj.show();
	var tabId = $(obj).attr('id');
	var menu = getMenuByTabId(tabId);
	if(menu.children.length > 0) {
		//有左菜单
		$('.leftWrap').removeClass('hide');
		$('.leftWrap').show().find('.lf_menu').empty();
		var children = menu.children;
		var content = '';
		for(var i = 0, n = children.length; i < n; i++) {
			if(children[i].children.length == 0) {
				content += '<li class="menuItem">'+
								'<i></i><a post="' + toUrl(children[i].menuAction) + '">' + children[i].menuName + '</a>'+
							'</li>';
			} 
		}
		$('.lf_menu').append(content);
		$(".menuItem :first").click();	
	} else {
		//没有左菜单
		$('.leftWrap').addClass('hide');
		$('.rightWrap').find('iframe').get(0).src = toUrl(menu.menuAction);
		iframeObj.load(function(){
			changeHeight();
			maskObj.hide();
		});
	}
	
	changeWidth();
	maskObj.hide();
}
//点击菜单，显示菜单
function toUrl(menuAction) {
	//防止菜单url为null或者为空造成菜单不显示，或者死循环
	if(!menuAction||$.trim(menuAction)==''){
		return ctx +"/leafMenuIsNullPlzFixIt.do";
	}
	//如果菜单不是以http开头，拼接上下文返回
	if(!(menuAction.substring(0,7).toUpperCase()=='HTTP://')) {
		return ctx + menuAction;
	}
	//直接返回http的url
	return menuAction;
}

function getMenuByTabId(tabId) {
	for(var i = 0, menus = authInfo.authMenus, n = menus.length; i < n; i++) {
		var menu = menus[i];
		if(menu.menuId + '_tab' == tabId) {
			return menu;
		}
	}
}

$(function(){ 
	//翻页代码
	$(".jump").focus(function(){
		$(this).addClass("jumphov");
		$(this).parent().next(".pagebtn_wrap").animate({"width":"50px"},"slow");
	}).blur(function(){
		$(this).removeClass("jumphov");
		$(this).parent().next(".pagebtn_wrap").animate({"width":"0"},"slow");
	});
	
	
});	

//校验提示
function showMsgTips(msg,time) {
	$(".msgTips").html(msg);
	$(".msgTips").show();
	setTimeout(function() {
		$('.msgTips').hide();
	}, time);
}