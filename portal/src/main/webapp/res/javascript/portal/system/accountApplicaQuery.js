$(function(){ 
	//信息变更日志查询按钮绑定click事件
	$('#changeQryBtn').click(function(){
		//默认查第一页10行数据
		qryAccCheck(1,10);
	});	
});//文档初始化结束



//信息查询
function qryAccCheck(page,rows){
	var optBeginTime = $('#qryStartTime').val();
	var optEndTime = $('#qryEndTime').val();

	if(optBeginTime>optEndTime&&optEndTime!=null&&optEndTime!=""){
		showTip('开始时间不能大于结束时间！',3000);
        return;
	}

	showTip('查询中，请稍候...！');
	//拼接查询结果
	var str="";
	$.ajax({
		type: 'POST',
		url: 'accountAppliQuery.do',
		async: false,//因为要实时改变分页数据，只能同步请求
		dataType:"json",//返回数据类型为json时（controller使用@ResponseBody）必须设置返回的数据类型为json
		data:{  
			'page':page,
			'rows':rows,
			'optBeginTime':optBeginTime,
			'optEndTime':optEndTime,
			'mobilePhone':$.trim($('#contactNumber').val()),
			'userName':$('#applicationMan').val(),
			'deptName':$('#applicationCompany').val()
		},
		success : function(data) {
			//隐藏提示
			hideTip();
			if(data.accountRegisters) {
				$('#accountApplication').empty();
				$.each(data.accountRegisters,function(i,v) {
					var statestr = '';
					if(v.state == 0){
						statestr = '未审核';
					} else if(v.state == 1){
						statestr = '不通过';
					} else if(v.state == 2){
						statestr = '通过';
					}
					str+='<tr><td>'+(i+1)+'</td>';
					str+='<td>'+v.userName+'</td>';
					str+='<td>'+v.loginId+'</td>';
					str+='<td>'+v.mobilePhone+'</td>';
					str+='<td>'+v.certNbr+'</td>';
					str+='<td>'+v.deptName+'</td>';
					str+='<td>'+v.createTime+'</td>';
					str+='<td>'+statestr+'</td>';
					str+='<td>'+v.auditorUserId+'</td>';
					str+='<td>'+v.comment+'</td>';
					str+='</tr>';
				});
				
				$("#accountApplication").append(str);
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
							qryLoginLog(newPage,10);
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