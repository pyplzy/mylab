//点击搜索按钮查询管理员 
function queryAdmins(){
	var value=$.trim($("#admin").val());
	//不输入点查询，placehold就是value的值，默认查全部
	if(value!='搜索管理员'){
		getAdmins(value);
	}else{
		getAdmins('');
	}
	
}

//获取管理员角色的人员
function getAdmins(value){
	//重新查询管理员角色时，清楚应用上的选中状态
	$("#apps li").each(function(n,v){
		$(v).find("div").first().removeClass().addClass("appBox");
	});
	
	var str="";
	$.ajax({
		type: 'POST',
		url: 'menu_toApp_getAdmins.do',
		async: false,
		data:{ 'value':value},
		success : function(data) {
			if(data) {
				$("#admins").empty();
				$.each(data,function(i,value) {
						str+=
						'<tr onclick="queryDataPri(this);">'+
							'<td width="35%">'+value.userId+'</td>'+
							'<td width="65%">'+value.userName+'</td>'+
	                    '</tr>';
				});
				if(str==""){
					showDialog2("未找到相关"+$('#admin').val()+"的记录");
					return;
				}
				$("#admins").append(str);
			}	
		}
	});
}

//查询应用级应用
function getNormalApps(){
	var str="";
	$.ajax({
		type: 'POST',
		url: 'menu_toApp_getNormalApps.do',
		async: false,
		success : function(data) {
			if(data) {
				$("#apps").empty();
				$.each(data,function(i,value) {
						str+=
							'<li>'+
								'<div class="appBox " value='+value.appCode+' value2='+value.appName+'>'+
			                    	'<div class="boxImg"><img src="'+ctx2+value.appImgPath+'" /></div>'+
			                        '<div class="boxTitle">'+value.appName+'</div>'+
			                    '</div>'+
			                '</li>';
				});
				$("#apps").append(str);
			}	
		}
	});
}

$(document).ready(function() {	
	
	$('#admin').keyup(function(event) {
		//回车
		if(event.keyCode == 13) {
			queryAdmins();
		}
	});
	
	getAdmins("");
	getNormalApps();
	
	// 图标选择 
	$(".appBox").click(function(){
		$(this).toggleClass("checkbox");
	});
	
	
});

//查询 数据权限
function queryDataPri(obj){
	//选中管理员时，添加样式
	$(obj).removeClass().addClass("activebg");
	$(obj).siblings().removeClass();
	
	$("#userid").val("");
	$("#userid").val($(obj).find("td").eq(0).text());
	$("#apps li").each(function(n,v){
		$(v).find("div").first().removeClass().addClass("appBox");
	});
	queryDataPri2($(obj).find("td").eq(0).text());
}

//保存应用权限 
function addDataPri(){
	var userId=$("#userid").val();
	if(userId==""){
		showDialog("请选择管理员");
		return;
	}
	var appCode="";
	var appName="";
	//userId:privilegeValue:privilegeMemo;userId:privilegeValue:privilegeMemo;userId:privilegeValue:privilegeMemo;
	str="";
	$("#apps li").each(function(n,v){
		if($(v).find("div").first().hasClass("checkbox")){
			appCode=$(v).find("div").first().attr("value");
			appName=$(v).find("div").first().attr("value2");
			str+=userId+":"+appCode+":"+appName+";";
		}
	});
	$.ajax({
		type: 'POST',
		url: 'menu_toApp_addDataPris.do',
		data:{'str':str,
				  'userId':userId	
				},
		success : function(data) {
			if(data==true){
				queryDataPri2(userId);
				showDialog("操作成功"); 
				var i = 1;
				var timer = null;
				timer=setInterval(function() {
					if(i == 0) {
						clearInterval(timer);
						queryDataPri2(userId);
					} else {
					}
					i--;
				},1000);
			}else{
				showDialog("操作失败"); 
				var i = 1;
				var timer = null;
				timer=setInterval(function() {
					if(i == 0) {
						clearInterval(timer);
						queryDataPri2(userId);
					} else {
					}
					i--;
				},1000);
			}
		}
	});
}

//查询 数据权限
function queryDataPri2(userId){
	$.ajax({
		type: 'POST',
		url: 'menu_toApp_getDataPris.do',
		data:{'userId':userId},
		success : function(data) {
			if(data) {
				$.each(data,function(i,value) {
					var vv=value.privilegeValue;
					$("#apps li").each(function(n,v){
						if($(v).find("div").first().attr("value")==vv){
							$(v).find("div").first().removeClass().addClass(" appBox checkbox  ");
						}
					});	
				});
			}	
		}
	});
}

//提示
function showDialog(msg) {
	$("#msg ").html(msg);
	$("#msg").show();
	setTimeout(function() {
		$('#msg').hide();
	}, 1000);
}

//提示
function showDialog2(msg) {
	$("#msg2 ").html(msg);
	$("#msg2").show();
	setTimeout(function() {
		$('#msg2').hide();
	}, 1000);
}
