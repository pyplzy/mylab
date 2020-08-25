var timer = null;

$(function(){ 
	//应用管理-上线下线
	$(".icon_lamp").click(function(){
		if($(this).parents(".appItem").find(".appPanel").hasClass("nonline")){
			var $obj=$(this);
			$.ajax({
				type: 'POST',
				async:false,
				url: 'menu_appReg_actApp.do',
				data:{ 'appCode':$(this).parents(".appItem").find(".appPanel").attr("v")},
				success : function(data) {
					if(data==true){
						$obj.parents(".appItem").find(".appPanel").removeClass("nonline");
						$obj.css("background-position","0 0");
						$obj.parent().find("img").removeClass("gray");
					}
				}
			});
		}else{
			var $obj=$(this);
			$.ajax({
				type: 'POST',
				async:false,
				url: 'menu_appReg_delApp.do',
				data:{ 'appCode':$(this).parents(".appItem").find(".appPanel").attr("v")},
				success : function(data) {
					if(data==true){
						$obj.parents(".appItem").find(".appPanel").addClass("nonline");	
						$obj.css("background-position","0 -35px");
						$obj.parent().find("img").addClass("gray");
					}
				}
			});
		}
	});
	
	//重新选择图标
	$(".imgBox").click(function(){
		$(this).addClass("boxhov");
		$(this).parent().siblings().find(".imgBox").removeClass("boxhov");
	});
	
	
});	

//校验appCode
function judgeApp(){
	if($("#appCode").val()!=""){
		$.ajax({
			type: 'POST',
			url: 'app/menu_appReg_judgeApp.do',
			data:{ 'appCode' : $.trim($("#appCode").val())  },
			success : function(data) {
				if(data==true){
					showDialog2('该应用编码可用');
				}else{
					showDialog2('该应用编码不可用');
					$("#appCode").focus(); 
					return;
				}
			}
		});
	}else{
		showDialog2('应用编码不可为空');
		$("#appCode").focus(); 
		return;
	}
}
//校验提示
function showDialog2(msg) {
	$("#msg ").html(msg);
	$("#msg").show();
	setTimeout(function() {
		$('#msg').hide();
	}, 1000);
}

//生成校验码
function app(){
	if(flag=="add"){
		var appCode=$("#appCode").val();
		if(appCode==""){
			showDialog2("应用编码不可为空");
			return;
		}
		var password="";
		password = (md5( appCode )).substr(0,16);
		$("#appChecksum").val(password);
	}else if(flag="update"){
		var appCode2=$("#appCode2").val();
		if(appCode2==""){
			showDialog3("应用编码不可为空");
			return;
		}
		var password2="";
		password2 = (md5( appCode2 )).substr(0,16);
		$("#appChecksum2").val(password2);
	}
	
}

//保存应用注册信息
function save(){
	var appImgPath=$("#appImgPath").val();
	var appCode=$("#appCode").val();
	var appName=$("#appName").val();
	var appUrl=$("#appUrl").val();
	var todoUrl=$("#todoUrl").val();
	var userGuidePath=$("#userGuidePath").val();
	var appChecksum=$("#appChecksum").val();
	var state=$("input[name='state']:checked").val();
	var appType=$("input[name='appType']:checked").val();
	var displayOrder=$("#displayOrder").val();
	var appMmemo=$("#appMmemo").val();
	
	if(appChecksum.length>16){
		showDialog2("校验码错误");
		return ;
	}
	
	if(appCode==""){
		showDialog2("应用编码不可为空");
		return;
	}else{
		if(displayOrder==""){
			showDialog2("显示顺序不可为空");
			return;
		}else{
			$.ajax({
				type: 'POST',
				url: 'app/menu_appReg_addApp.do',
				data:{
					'appImgPath':appImgPath,
					'appCode':appCode,
					'appName':appName,
					'appUrl':appUrl,
					'todoUrl':todoUrl,
					'userGuidePath':userGuidePath,
					'appChecksum':appChecksum,
					'state':state,
					'appType':appType,
					'displayOrder':displayOrder,
					'appMmemo':appMmemo
					},
				success : function(data) {
					if(data==true){
						showDialog2('注册成功');
						var i = 1;
						
						timer=setInterval(function() {
							if(i == 0) {
								clearInterval(timer);
								closeDiv2();
							} else {
							}
							i--;
						},1000);

						$('.rightWrap').find('iframe').get(0).src = ctx+"/app/menu_appReg_appReg.do";
					}else{
						showDialog2('注册失败');
					}
					
				}
			});
		}
	}
}


//应用管理添加应用弹窗
function showDiv() { 
	flag="add";
	var bg = document.getElementById("mask"); 
	var con = document.getElementById("popAdd");  
	var w = document.body.scrollWidth; //网页正文全文宽 
	var h = document.body.scrollHeight; //网页正文全文高 
	bg.style.display = "block"; 
	bg.style.width = w + "px"; 
	bg.style.height = h + "px"; 
	con.style.display = "block"; 
}; 
/*关闭*/ 
function closeDiv2() { 
	clearInterval(timer);
	document.appForm.reset(); 
	var bg = document.getElementById("mask"); 
	var con = document.getElementById("popAdd"); 
	bg.style.display = "none"; 
	con.style.display = "none"; 
	
};
//应用管理重新选择图标弹窗
$(function() {
	function Dialog(clickobj,popobj){  //clickobj被点击的元素，popobj弹出框
		var windoww = $(window).width(); //mask 遮罩层
		var windowh = $(window).height();
		var doch = $(document).height();
		if( windowh<doch ){ windowh=doch; }
		
		
		$(document).on("click",clickobj,function(){
			if(flag=="update"){
				$("#images li div ").each(function(index){
					if( $("#appImgPath2").val() ==$(this).attr("value") ){
						$(this).addClass("boxhov");
						$(this).parent().siblings().find(".imgBox").removeClass("boxhov");
					}
				});
			}else if(flag=="add"){
				$("#images li div ").each(function(index){
					if($(this).hasClass("boxhov")){
						$(this).removeClass("boxhov");
					}
				});
			}
			$(".mask1").width( windoww ).height( windowh ).css( "opacity",0.3 ).show();//定义遮罩层的宽高，遮罩层的透明度为0.3
			$(popobj).show();//
		});		
		$(".icon_close1").click(function(){//关闭
			$(".mask1").hide();//隐藏遮罩
			$(popobj).hide();//隐藏弹出框
		});
		
		$("#save1").click(function(){//选择应用图标：保存
			if(flag=="add"){
				$("#appImgPath").attr('value' , $(".boxhov").attr("value"));
				if($(".boxhov img ").attr('src')!=""){
					$("#appImg").attr('src' , $(".boxhov img ").attr('src'));
				}
				$(".mask1").hide();
				$(popobj).hide();
			}else if(flag="update"){
				$("#appImgPath2").attr('value' , $(".boxhov").attr("value"));
				if($(".boxhov img ").attr('src')!=""){
					$("#appImg2").attr('src' , $(".boxhov img ").attr('src'));
				}
				$(".mask1").hide();
				$(popobj).hide();
			}
			$(".imgBox").removeClass("boxhov");
		});
		
		$("#off1").click(function(){//选择应用图标：重置
			$(".imgBox").removeClass("boxhov");
		});
		
	}	
	Dialog(".btn_again","#addIcon");
});

//应用管理修改应用弹窗
function showDiv3() { 
	flag="update";
	var bg = document.getElementById("mask3"); 
	var con = document.getElementById("popImg");  
	var w = document.body.scrollWidth; //网页正文全文宽 
	var h = document.body.scrollHeight; //网页正文全文高 
	bg.style.display = "block"; 
	bg.style.width = w + "px"; 
	bg.style.height = h + "px"; 
	con.style.display = "block"; 
	
}; 

/*关闭*/ 
function closeDiv3() { 
	clearInterval(timer);
	document.appForm2.reset(); 
	var bg = document.getElementById("mask3"); 
	var con = document.getElementById("popImg"); 
	bg.style.display = "none"; 
	con.style.display = "none"; 
};

//根据应用编码查询应用信息
function queryApp(obj){
	var appcode=$(obj).parent().parent().attr("v");
	$.ajax({
		type: 'POST',
		url: 'menu_appReg_queryApp.do',
		data:{ 'appCode':appcode},
		success : function(data) {
			
			$('#appImg2', window.parent.document).attr("src",ctx+data.appImgPath);
			$('#appImgPath2', window.parent.document).val(data.appImgPath);
			$('#appCode2', window.parent.document).val(data.appCode);
			$('#appName2', window.parent.document).val(data.appName);
			$('#appUrl2', window.parent.document).val(data.appUrl);
			$('#todoUrl2', window.parent.document).val(data.todoUrl);
			$('#userGuidePath2', window.parent.document).val(data.userGuidePath);
			$('#appChecksum2', window.parent.document).val(data.appChecksum);
			$("input[name='state2'][value=" + data.state + "]" , window.parent.document).prop("checked",true);
			$("input[name='appType2'][value=" + data.appType + "]" , window.parent.document).prop("checked",true);
			$('#displayOrder2', window.parent.document).val(data.displayOrder);
			$('#appMmemo2', window.parent.document).val(data.appMmemo);
			
			parent.window.showDiv3();
		}
	});
}

//更新应用信息
function updApp(){
	var appImgPath=$("#appImgPath2").val();
	var appCode=$("#appCode2").val();
	var appName=$("#appName2").val();
	var appUrl=$("#appUrl2").val();
	var todoUrl=$("#todoUrl2").val();
	var userGuidePath=$("#userGuidePath2").val();
	var appChecksum=$("#appChecksum2").val();
	var state=$("input[name='state2']:checked").val();
	var appType=$("input[name='appType2']:checked").val();
	var displayOrder=$("#displayOrder2").val();
	var appMmemo=$("#appMmemo2").val();
		if(appChecksum.length>16){
			showDialog3("校验码错误");
			return ;
		}
		if(displayOrder==""){
			showDialog3("显示顺序不可为空！");
			return;
		}else{
			$.ajax({
				type: 'POST',
				url: 'app/menu_appReg_updApp.do',
				data:{
					'appImgPath':appImgPath,
					'appCode':appCode,
					'appName':appName,
					'appUrl':appUrl,
					'todoUrl':todoUrl,
					'userGuidePath':userGuidePath,
					'appChecksum':appChecksum,
					'state':state,
					'appType':appType,
					'displayOrder':displayOrder,
					'appMmemo':appMmemo
					},
				success : function(data) {
					if(data==true){
						showDialog3('更新成功');
						var i = 1;
						timer=setInterval(function() {
							if(i == 0) {
								clearInterval(timer);
								closeDiv3();
							} else {
							}
							i--;
						},1000);
						$('.rightWrap').find('iframe').get(0).src = ctx+"/app/menu_appReg_appReg.do";
					}else{
						showDialog3('更新失败');
					}
				}
			});
		}
}

//校验提示
function showDialog3(msg) {
	$("#msg2 ").html(msg);
	$("#msg2").show();
	setTimeout(function() {
		$('#msg2').hide();
	}, 1000);
}

