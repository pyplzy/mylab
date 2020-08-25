$(function(){ 

	/*--------------选项卡--------------*/
	function eHover( elem, cla ){
		$(document).on("mouseover", elem, function(){
			$(this).addClass( cla );
		});
		$(document).on("mouseout", elem, function(){
			$(this).removeClass( cla );
		});
	}
	
	//选项卡点击
	function tabClick( tabItem, content, cla ){
		$(content).each(function(i){ //遍历选项卡内容 显示第一个
            $(this).children().first().show();
        });
		$(document).on("click", tabItem, function(){
			var p = $(this).parent().parent();
			var n = p.next("div").children();
			p.children("ul").children("li").removeClass( cla );
			$(this).addClass( cla );
			n.hide();
			n.eq( $(this).index() ).show();
			if( $(this).attr("post")!=null ){ //post属性存在时
				if( !n.eq( $(this).index() ).children().is("iframe") ){ //包块没有iframe 创建iframe
					n.eq( $(this).index() ).empty().html('<iframe frameborder="0" width="100%" height="100%" scrolling="auto" marginheight="0" marginwidth="0" src="'+$(this).attr("post")+'"></iframe>');
				}
			}
			//重新调整iframe高度，防止分页不显示（防止第二个选项卡内容较多切换为第一个选项卡，翻页时内容较少，再点击第二个选项卡导致翻页消失）
			window.parent.changeHeight();
		});

	}
	
	eHover( ".tabItem>li", "hover" );
	tabClick( ".tabItem>li", ".tabPanel", "select" );
	
	//登录日志查询按钮绑定click事件
	$('#loginQryBtn').click(function(){
		//默认查第一页10行数据
		qryLoginLog(1,10);
	});
	
	//信息变更日志查询按钮绑定click事件
	$('#changeQryBtn').click(function(){
		//默认查第一页10行数据
		qryChgLog(1,10);
	});	
});//文档初始化结束

//查询登录日志
function qryLoginLog(page,rows){
	showTip('查询中，请稍候...！');
	//拼接查询结果
	var str="";
	$.ajax({
		type: 'POST',
		url: 'menu_to_getLoginLogs.do',
		async: false,//因为要实时改变分页数据，只能同步请求
		dataType:"json",//返回数据类型为json时（controller使用@ResponseBody）必须设置返回的数据类型为json
		data:{  
			'page':page,
			'rows':rows,
			'userId':$.trim($('#userId').val()),
			'loginTime':$('#loginTime').val()
		},
		success : function(data) {
			//隐藏提示
			hideTip();
			if(data.loginLogs) {
				$('#loginQryTbl').empty();
				$.each(data.loginLogs,function(i,v) {
					str+='<tr><td>'+(i+1)+'</td>';
					str+='<td>'+v.userId+'</td>';
					str+='<td>'+v.userName+'</td>';
					str+='<td>'+v.orgName+'</td>';
					str+='<td>'+v.loginIp+'</td>';
					str+='<td>'+v.loginTime+'</td>';
					if(v.isSuccess==1){
						str+='<td><span class="success">成功</span></td>';
					}else{
						str+='<td><span class="fail">失败</span></td>';
					}
					str+='</tr>';
				});
				
				$("#loginQryTbl").append(str);
				//总记录数>0
				if( data.pageObj.total>0){
					$('#loginQryPage').ossPaginator({
						totalrecords: data.pageObj.total, 
						recordsperpage: 10, 
						length: 6, 
						next: '下一页', 
						prev: '上一页', 
						first: '首页', 
						last: '尾页', 
						initval: page,//初始化哪一页被选中
						controlsalways: true,
						onchange: function (newPage){
							qryLoginLog(newPage,10);
				    	}
					});//初始化分页结束
				//没有记录，提示信息且不显示分页
				}else{
					showTip('未查询到相关记录！',3000);
					$('#loginQryPage').empty();
				}
				
			}	
			//重新调整iframe高度，防止分页不显示
			window.parent.changeHeight();
		}//success回调函数结束
	});//ajax结束
}

//查询信息变更日志
function qryChgLog(page,rows){
	var optBeginTime = $('#qryStartTime').val();
	var optEndTime = $('#qryEndTime').val();
	var optObj=$('#dropdownValueForLog').val();
	
	if(optBeginTime>optEndTime){
		showTip('开始时间不能大于结束时间！',3000);
        return;
	}
	if(optBeginTime.substring(0,7)!=optEndTime.substring(0,7)){
		showTip('查询时间不能跨月！',3000);
        return;
	}
	//显示提示
	showTip('查询中，请稍候...！');
	//拼接查询结果
	var str="";
	$.ajax({
		type: 'POST',
		url: 'menu_to_getChgLogs.do',
		async: false,//因为要实时改变分页数据，只能同步请求
		dataType:"json",//返回数据类型为json时（controller使用@ResponseBody）必须设置返回的数据类型为json
		data:{  
			'page':page,
			'rows':rows,
			'optBeginTime':optBeginTime,
			'optEndTime':optEndTime,
			'optObj':optObj
		},
		success : function(data) {
			//隐藏提示
			hideTip();
			if(data.changeLogs) {
				$('#changeQryTbl').empty();
				$.each(data.changeLogs,function(i,v) {
					str+='<tr title=\''+v.optContent+'\'><td>'+v.optTime+'</td>';
					str+='<td>'+v.optType+'</td>';		
					str+='<td>'+v.optObj+'</td>';
					str+='<td>'+v.userId+'</td>';
					str+='<td>'+v.optIp+'</td>';
					str+='<td>'+v.optContent+'</td>';
					str+='</tr>';
				});
				
				$("#changeQryTbl").append(str);
				//总记录数>0
				if( data.pageObj.total>0){
					$('#changePage').ossPaginator({
						totalrecords: data.pageObj.total, 
						recordsperpage: 10, 
						length: 6, 
						next: '下一页', 
						prev: '上一页', 
						first: '首页', 
						last: '尾页', 
						initval: page,//初始化哪一页被选中
						controlsalways: true,
						onchange: function (newPage){
							qryChgLog(newPage,10);
				    	}
					});//初始化分页结束
				//没有记录，提示信息且不显示分页
				}else{
					showTip('未查询到相关记录！',3000);
					$('#changePage').empty();
				}
				
			}	
			//重新调整iframe高度，防止分页不显示
			window.parent.changeHeight();
		}//success回调函数结束
	});//ajax结束
}
//提示框显示
function showTip(msg,timeout) {
	$('#msgDialog').html(msg).show();
	//如果提供了timeout，那么设置timeout毫秒后自动隐藏
	if(timeout){
		setTimeout(function() {
			$('#msgDialog').hide().html('');
		}, timeout);
	}
}
//提示框隐藏
function hideTip(){
	$('#msgDialog').hide().html('');
}