/**
 * 该函数用于等待页面中所有涉及到页面高度的ajax请求完成后调整页面的高度
 */
var ajaxcount = 3;
function whenloaded(){
	ajaxcount --;
	if(ajaxcount < 0) return;
	if(ajaxcount == 0) {
		parent.changeHeight();
	}
}

$(function() {
	//查我的应用
	$.ajax({
		type: 'post',
		url: 'menu_main_app-list.do',
		data:{rows:12,page:1},
		success : function(data) {
			if(data) {
				var str2 = '';
				//app计数
				$.each(data,function(i,v) {
					str2+='<li>'+
                	'<a href="'+v.appUrl+'" target="_blank">'+
                    	'<div class="app_icon">'+
                            '<i style="background-image:url('+ctx+v.appImgPath+') ;background-repeat:no-repeat"></i>'+
                        '</div>'+
                        '<div class="app_name">'+v.appName+'</div>'+
                    '</a>'+
                   '</li>';
				});
				//如果我的应用不足5个，用虚线框占位（有for循环其实都不需要if语句）
				var appCount = data.length;
				
				if(appCount <= 5){
					for(var i=0;i<5-appCount;i++){
						str2+='<li><a href="####" class="app_icon_k"></a></li>';
					}
				} else if(appCount <= 10){
					for(var i=0;i<10-appCount;i++){
						str2+='<li><a href="####" class="app_icon_k"></a></li>';
					}
				}
				$("#apps").append(str2);
			}
			whenloaded();
		},
		error: function(){whenloaded();}
	});
/*$(function() {
	//查我的应用
	$.ajax({
		type: 'post',
		url: 'menu_main_app-list.do',
		data:{rows:12,page:1},
		success : function(data) {
			if(data) {
				alert(data.length);
				var str2 = '';
				//app计数
				$.each(data,function(i,v) {
					str2+='<li>'+
                	'<a href="'+v.appUrl+'" target="_blank">'+
                    	'<div class="app_icon">'+
                            '<i style="background-image:url('+ctx+v.appImgPath+') ;background-repeat:no-repeat"></i>'+
                        '</div>'+
                        '<div class="app_name">'+v.appName+'</div>'+
                    '</a>'+
                   '</li>';
				});
				//如果我的应用不足5个，用虚线框占位（有for循环其实都不需要if语句）
				var appCount = data.length;
				
				if(appCount <= 5){
					for(var i=0;i<5-appCount;i++){
						str2+='<li><a href="####" class="app_icon_k"></a></li>';
					}
				} else if(appCount <= 10){
					for(var i=0;i<10-appCount;i++){
						str2+='<li><a href="####" class="app_icon_k"></a></li>';
					}
				}
				$("#apps").append(str2);
			}
			whenloaded();
		},
		error: function(){whenloaded();}
	});*/
	//给天气头上的刷新按钮绑定点击事件
/*	$('#wRefresh').on('click',function(){
		qryWeather();
	});*/
	$('#tRefresh').on('click',function(){
		qryTodo();
	});
	
	//查天气
//qryWeather();
	//查待办
	qryTodo();
});

function qryTodo(){
	//我的公告
	$.ajax({
		type: 'post',
		url: 'menu_main_todo-list.do',
		success : function(data) {
			if(data) {
				var todohtml = '';
				for(var i=0;i<data.length;i++){
					var sysinfo = data[i];
					todohtml += '<div class="clearfix">'+
									 '<div class="wrapsys">'+
						                '<img src="'+ctx+sysinfo.icon+'">'+
						                '<span class="appname">'+sysinfo.sysname+'</span>'+
						        	 '</div>'+
						        	 '<div class="wrapModule">'+
						        	 	'<ul class="clearfix">';
					for(var j=0;j<sysinfo.todo.length;j++){
						
						var todoinfo = sysinfo.todo[j];
						todohtml += '<li>'+
										'<a class="module" href="'+sysinfo.appurl+todoinfo.url+'" target="_blank">'+todoinfo.module+'</a><span class="num">'+sysinfo.count+'</span>'+
									'</li>';
					}
					todohtml += '</ul></div></div>';
				}
				$(".tbox .cont").html(todohtml);
			}
			whenloaded();
		},
		error: function(){whenloaded();}
	});
}

//查天气
/*function qryWeather(){
	//显示loading
	$('#wLoading').removeClass("hide");
	//隐藏刷新
	$('#wRefresh').hide();
	$.ajax({
		type: 'POST',
		url: 'menu_main_weather.do',
		async: true,
		dataType:"json",
		success : function(data) {
			//显示刷新
			$('#wRefresh').show('slow');
			//隐藏loading
			$('#wLoading').addClass("hide");
			if(data){
				if(!data.status){
					$('#wCity').text(data.city);//城市
					$('#wDay').text(data.day);//日期
					$('#wWeek').text(data.week);//周几
					$('#wLunar').text(data.lunar);//农历
					
					$('#wImg').attr("src","../res/images/weather/"+data.weather.imageCode+".png");//天气图标
					$('#wTemp').text(data.weather.temperature);//温度
					$('#wInfo').text(data.weather.imageExplain);//多云？
					var pmValue = data.PM25.AQI_num;
					$('#wPm').html(pmValue);//PM2.5
					$('#wLevel').html(data.PM25.AQI_level).show();//良?
					if(pmValue>=0&&pmValue<=50){
						$('#wLevel').removeClass().addClass("level1");
					}else if(pmValue>50&&pmValue<=100){
						$('#wLevel').removeClass().addClass("level2");
					}else if(pmValue>100&&pmValue<=150){
						$('#wLevel').removeClass().addClass("level3");
					}else if(pmValue>150&&pmValue<=200){
						$('#wLevel').removeClass().addClass("level4");
					}else if(pmValue>200&&pmValue<=300){
						$('#wLevel').removeClass().addClass("level5");
					}else {
						$('#wLevel').removeClass().addClass("level6");
					}
					$('#wPmDetail').text("PM2.5浓度："+data.PM25.concentration+"；"+data.PM25.other);//具体信息
	//				$('#wRank').text(data.PM25.other);//其他信息
					$('#wRemind').html(data.prompt);//温馨提示
	//				$('#wRemind').html(data.prompt+"<br/>"+data.other.substring(5));//温馨提示
				}else{//天气接口调用失败时
					$('#wLevel').hide();
					$('#wImg').attr("src","../res/images/weather/sad-face.png");//哭脸图标
					$('#wRemind').html("暂时无法提供天气服务");//温馨提示
				}
			}
			whenloaded();
		},
		error:function(err){
			//显示刷新
			$('#wRefresh').show('slow');
			whenloaded();
		}
	});	
	
}*/