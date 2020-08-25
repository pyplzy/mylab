var timerArray = [];
$(function(){
	loadTimeList(1);//
	$("#sysTimeQry").on('click',function(){
		loadTimeList(1);//
	});
	parent.taskDialog.getjobs();
	parent.taskDialog.getnodes();
	//添加
	$("#taskAdd").on('click',function(){
		parent.taskDialog.showResourceDiv(null);
	});
	//修改
	$("#taskEdit").on('click',function(){
		var timerId = $("#uicTimesList .activebg").attr('timerId');
		if(timerId==undefined){
			showMsgTips("请选择一个定时器",1000);
			return;
		}
		var timerObj = getTimerObj(timerId);
		parent.taskDialog.showResourceDiv(timerObj);
	});
	//删除
	$("#taskDel").on('click',function(){
		var timerId = $("#uicTimesList .activebg").attr('timerId');
		if(timerId==undefined){
			showMsgTips("请选择一个定时器",1000);
			return;
		}else{
			var r = confirm('确定删除当前定时器任务吗？');
			if(r){
				$.ajax({
					url:"menu_index_deltask.do",
					type:"post",
					data:{
						timerId:timerId
					},
					success:function(data){
						if(data>0){
							alert('删除成功');
							loadTimeList(1);//
						}else{
							alert('系统异常,请稍后重试');
						}
					},
					error:function(data){
						alert("提示信息","操作异常，请联系系统管理员！");
					}
				}); 
			}
		}
	});
});

function getTimerObj(timerId){
	var tableObj = null;
	for(var i=0;i<timerArray.length;i++){
		if(timerId==timerArray[i].timerId){
			tableObj = timerArray[i];
			break;
		}
	}
	return tableObj;
}

//加载表格
function loadTimeList(page){
	var jobId=$("#sysJobList").val();
//	var nodeId=$("#sysNodeList").val();
	var timerName=$.trim($('#sysTimeName').val());
	var enAble=$("#sysEnable").val();
	$('#uicTimesList').empty();
	$.ajax({
		async: false,
		type:'post',
		url:'menu_index_getTaskbypage.do',
		data:{
			jobId:jobId,
//			nodeId:nodeId,
			timerName:timerName, 
			enAble:enAble,
			page : page,
			rows : 10
		},
		success:function(data){
			$("#uicTotalName").html("共"+data.total+"条记录");
			timerArray = data.rows;
			var timeItems = data.rows;
			$.each(timeItems, function(index, item) {
				var buttonKey = null;
				if(item.timerStatus==1){
					buttonKey = '<a href="javascript:void(0)"  class="onyes1 uica "></a>';  
				}else{
					buttonKey = '<a href="javascript:void(0)"  class="onyes1 offno1 uica"></a>';  
				}
				
				$("#uicTimesList").append('<div class="item" timerId="' + item.timerId + '" onclick="chkTimer(this);">'+ 
			            '	<div class="item_mask">'+
			            '   	<div class="cols titlek"></div>'+
			            '   	<div class="cols uicTimeTb" title="'+item.timerName+'">'+item.timerName+'</div>'+
			            '   	<div class="cols uicTimeTb" title="'+item.node+'">'+item.node+'</div>'+
			            '    	<div class="cols uicTimeTb" onclick="toggleStatus('+item.timerId+','+item.timerStatus+');">'+buttonKey+'</div>'+
			            '    	<div class="cols uicTimeTb" title="'+item.cronExpression+'">'+item.cronExpression+'</div>'+
			            '    	<div class="cols uicTimeTb" title="'+item.busiPara+'">'+item.busiPara+'</div>'+
			            '    	<div class="cols uicTimeTb" title="'+item.timerDesc+'">'+item.timerDesc+'</div>'+
			            '	</div>'+
			            '</div>');
			});
			$('#timeRed').ossPaginator({
				totalrecords: data.total, 
				recordsperpage: 10, 
				length: 6, 
				next: '下一页', 
				prev: '上一页', 
				first: '首页', 
				last: '尾页', 
				initval: page,//初始化哪一页被选中
				controlsalways: true,
				onchange: function (newPage)
				{
					loadTimeList(newPage);
		    	}
			});
			//重新调整iframe高度，防止分页不显示（防止第二个选项卡内容较多切换为第一个选项卡，翻页时内容较少，再点击第二个选项卡导致翻页消失）
			window.parent.changeHeight();
		}
	});
}

function toggleStatus(timerId,value){
	var row=getTimerObj(timerId);
	if(value==1){
		var r = confirm("确定关闭当前定时器任务吗？");
		if(r){
			$.ajax({
				url:"menu_index_stopTask.do",
				type:"post",
				data:{"taskString":JSON.stringify(row) },
				beforeSend:function(){
					$('#uicMsgTips').html('正在关闭定时器..........').show();
				},
				success:function(data){
					$('#uicMsgTips').hide().html('');
					loadTimeList(1);//
					alert(data);
				},
				error:function(data){
					alert("提示信息","操作异常，请联系系统管理员！");
				}
			});
		}
	}else if(value==0){
		var r = confirm("确定启动当前定时器任务吗？");
		if(r){
			if(row.isEnAble<2){
				alert("当前定时器未处于启用状态,请先启用定时器和任务");
				return false;
			}
			$.ajax({
				url:"menu_index_startTask.do",
				type:"post",
				data:{"taskString":JSON.stringify(row) },
				beforeSend:function(){
					$('#uicMsgTips').html('正在启动定时器..........').show();
				},
				success:function(data){
					$('#uicMsgTips').hide().html('');
					loadTimeList(1);//
					alert(data);
				},
				error:function(data){
					alert("提示信息","操作异常，请联系系统管理员！");
				}
			}); 
		}
	}
}

function chkTimer(obj){
	//选中样式
	$(obj).toggleClass('activebg');
	$(obj).siblings().removeClass('activebg');
}
