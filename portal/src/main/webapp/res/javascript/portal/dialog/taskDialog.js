var taskDialog=(function(){
	var jobsList = [];
	var nodesList = [];
	var jobId = null;//任务id
	var uicNodeId = null;//节点id
	//定时器窗口关闭
	$('#taskDialogResClose, #taskFormDel').on('click',function(){
		$("#taskFrom").hide();
		$("#mask").hide();
	});
	//任务删除
	$("#uicJobRemove").on('click',function(){
		var r = confirm("确定删除此任务信息？");
		if(r){
			$.ajax({
				async:false,
				url:ctx+"/task/menu_index_deljob.do",
				type:'post',
				data:{jobId:jobId},
				success:function(data){
					if(data>0){
						alert("删除成功");
						$("#timeJob").hide();
						$(".jobmask").hide();
						$("#mask").show();
						getjobs();
					}else{
						alert('系统异常,请稍后重试');
					}
				}
			});
		}
	});
	//节点删除
	$("#uicNodeRemove").on('click',function(){
		var r = confirm("确定删除此节点信息？");
		if(r){
			$.ajax({
				async:false,
				url:ctx+"/task/menu_index_delnode.do",
				type:'post',
				data:{appNodeId:uicNodeId},
				success:function(data){
					if(data>0){
						alert("删除成功");
						$("#timeNodeDial").hide();
						$(".jobmask").hide();
						$("#mask").show();
						getnodes();
					}else{
						alert('系统异常,请稍后重试');
					}
				}
			});
		}
	});
	
	$("#timeTask").on('click',function(){
		showTimeTaskMsg();//任务添加和修改
	});
	$("#timeNode").on('click',function(){
		showTimeNodeMsg();//节点添加和修改
	});
	//关闭任务
	$("#closeTimeJob , #uicJobDel").on('click',function(){
		$("#timeJob").hide();
		$(".jobmask").hide();
		$("#mask").show();
	});
	//关闭节点
	$("#closetimeNode , #uicNodeDel ").on('click',function(){
		$("#timeNodeDial").hide();
		$(".jobmask").hide();
		$("#mask").show();
	});
	
	//任务添加和修改保存
	$("#uicJobSave").on('click',function(){
		var jobId = $("#jobId").val();
		var jobName = $("#jobName").val();
		var jobCd = $("#jobCd").val();
		var enAble = $("#jobEnable").val();
		var content = $("#jobContent").val();
		if(enAble!=0 && enAble!=1){
			showMsgTips("请选择是否启用任务", 1000);
			return false;
		}
		if(jobName=='' || jobCd==''){
			showMsgTips('信息不完整',1000);
			return false;
		}
		var url='';
		if(jobId>0){
			url=ctx+'/task/menu_index_modjob.do';
		}else{
			url=ctx+'/task/menu_index_addNjob.do';
		}
		//请求userId的个人信息       
		$.ajax({
			async:false,
			method:'POST',
			url:url,
			data:{
				jobId:jobId,
				jobName:jobName,
				jobCd:jobCd,
				enAble:enAble,
				content:content
			},
			success:function(data){
				if(data>0){
					alert("保存成功");
					$("#timeJob").hide();$(".jobmask").hide();$("#mask").show();
					if(jobId>0){//修改 easyDropDwon不初始化
						$('uciJobList').easyDropDown('select',jobId);
					}else{  //添加  easyDropDwon初始化
						getjobs();
					}
				}
			}
		});
	});
	
	$("#taskFromSave").on('click',function(){
		var jobId = $("#uciJobList").val();//任务id
		var uicNodeId = $("#uciNodeList").val();//任务id
		var enAble=$('#uicEnable').val();
		if(isNaN(jobId) || isNaN(uicNodeId) || jobId<0 || uicNodeId<0){
			alert('请选择关联的节点或任务');
			return false;
		}
		if(enAble!=0 && enAble!=1){
			alert('请选择是否启用该定时任务');
			return false;
		}
		var timerName=$.trim($('#uicTimeName').val());
		var className=$.trim($('#uicClassName').val());
		var excuteMethod=$.trim($('#excuteMethod').val());
		var cronExpression=$.trim($('#cronExpression').val());
		var busiPara=$.trim($('#uicBusiPara').val());
		if(!(timerName && className && excuteMethod && cronExpression && busiPara)){
			alert('请将信息填写完整');
			return false;
		}
		var timerId=$('#uicTimeId').val();
		var timerDesc=$.trim($('#uicTimerDesc').val());
		var url='';
		if(timerId>0){
			url=ctx+'/task/menu_index_modtask.do';
		}else {
			url=ctx+'/task/menu_index_addTask.do';
		}
		$.ajax({
			url:url,
			dataType:'text',
			type:'post',
			data:{
				timerId:timerId,
				jobId:jobId,
				nodeId:uicNodeId, 
				timerName:timerName,
				className:className,
				excuteMethod:excuteMethod,
				cronExpression:cronExpression,
				busiPara:busiPara,
				enAble:enAble,
				timerDesc:timerDesc,
				timerStatus:0
			},
			success:function(data){
				if(data>0){
					alert('保存成功');
					$("#mask").hide();
					$("#taskFrom").hide();
					$('.rightWrap').find('iframe').get(0).contentWindow.loadTimeList(1);
				}else{
					alert('系统异常，请稍后重试');
				}
			}
		});
	});
	
	//任务添加和修改保存
	$("#uicNodeSave").on('click',function(){
		var appnodeid = $('#uicNodeId').val();
		var ip = $('#uicIpAddress').val();
		var port = $('#uicPortValue').val();
		var nodename = $('#uicNodeName').val();
		var flag = $('#uicNodeType').val();
		var content =  $.trim($('#uicNodeContent').val());
		if(ip!='localhost' && !/(\d+)\.(\d+)\.(\d+)\.(\d+)/g.test(ip)){
			alert('请输入正确的IP');
			return false;
		}
		if(!/^\d+$/.test(port)){
			alert('端口号不正确');
			return false;
		}
		if(flag!=1){
			alert('请选择节点类型');
			return false;
		}
		if(nodename==''){
			alert('信息不完整');
			return false;
		}
		var url='';
		if(appnodeid>0){
			url= ctx+'/task/menu_index_modnode.do';
		}else{
			url= ctx+'/task/menu_index_addNode.do';
		}
		//请求userId的个人信息       
		$.ajax({
			async:false,
			method:'POST',
			url:url,
			data:{
				appNodeId:appnodeid,
				nodeName:nodename,
				nodeType:flag,
				ipAddress:ip,
				portValue:port,
				content:content
			},
			success:function(data){
				if(data>0){
					alert("保存成功");
					$("#timeNodeDial").hide();$(".jobmask").hide();$("#mask").show();
					if(appnodeid>0){//修改 easyDropDwon不初始化
						$('uciNodeList').easyDropDown('select',appnodeid);
					}else{  //添加  easyDropDwon初始化
						getnodes();
					}
				}
			}
		});
	});
	
	//任务下拉框赋值
	var getjobs = function(){
		$.ajax({
			async:false,
			url:ctx+"/task/menu_index_getJobs.do",
			type:'post',
			success:function(data){
				jobsList = data;
				var str="";
				$.each(data,function(i,v){
					str+='<option value="'+v.jobId+'">'+v.jobName+'</option>';
				});
				$("#uciJobList").easyDropDown('destroy');
				$("#uciJobList").empty().append('<option value="-1" >---请选择---</option>'+str);
                $("#uciJobList").easyDropDown({
            		onChange: function(selected){
            			jobId = selected.value;
            			if(selected.value>0){
            				$("#timeTask .rf_btn").html('').html('修改');
            			}else{
            				$("#timeTask .rf_btn").html('').html('添加');
            			}
            		}
                });
			}
		});
	};
	
	//获得所有的节点，给下拉选赋值
	var getnodes = function(){
		$.ajax({
			async:false,
			url:ctx+"/task/menu_index_getNodes.do",
			type:'post',
			success:function(data){
				nodesList = data;
				var str="";
				$.each(data,function(i,v){
					str+='<option value="'+v.appNodeId+'">'+v.ipAddress+':'+v.portValue+'</option>';
				});
				$("#uciNodeList").easyDropDown('destroy');
				$("#uciNodeList").empty().append('<option value="-1" >---请选择---</option>'+str);
                $("#uciNodeList").easyDropDown({
            		onChange: function(selected){
            			uicNodeId = selected.value;
            			if(selected.value>0){
            				$("#timeNode .rf_btn").html('').html('修改');
            			}else{
            				$("#timeNode .rf_btn").html('').html('添加');
            			}
            		}
                });
			}
		});
	};
	
	//任务添加和修改
	var showTimeTaskMsg = function(){
		$("#mask").hide();
		$(".jobmask").height($(document).height());  
		$(".jobmask").width($(document).width()); 
		$(".jobmask").show();
		$("#timeJob").show();
		if(jobId>0){
			$.each(jobsList,function(index,item){
				if(item['jobId']==jobId){
					$('#jobId').val(item['jobId']);
					$('#jobName').val(item['jobName']);
					$('#jobCd').val(item['jobCd']);
					$('#jobEnable').easyDropDown('select',item['enAble']);
					$('#jobContent').val(item['content']);
					return false;
				}
			});
			$("#taskDialText").html('').html('修改任务');
			$("#uicJobRemove").show();
			$('#jobName').attr("disabled",true);
		}else{
			$('#jobId').val(0);
			$('#jobName').val('');
			$('#jobCd').val('');
			$('#jobEnable').easyDropDown('select','-1');
			$('#jobContent').val('');
			$("#taskDialText").html('').html('新建任务');
			$("#uicJobRemove").hide();
			$('#jobName').attr("disabled",false);
		}
	};
	//节点添加和修改
	var showTimeNodeMsg = function(){
		$("#mask").hide();
		$(".jobmask").height($(document).height());  
		$(".jobmask").width($(document).width()); 
		$(".jobmask").show();
		$("#timeNodeDial").show();
		if(uicNodeId>0){
			$.each(nodesList,function(index,item){
				if(item['appNodeId']==uicNodeId){
					$('#uicNodeId').val(item['appNodeId']);
					$('#uicIpAddress').val(item['ipAddress']);
					$('#uicPortValue').val(item['portValue']);
					$('#uicNodeName').val(item['nodeName']);
					$('#uicNodeType').easyDropDown('select',item['nodeType']);
					$('#uicNodeContent').val(item['content']);
					return false;
				}
			});
			$("#nodeDialText").html('').html('修改节点');
			$("#uicNodeRemove").show();
//			$('#jobName').attr("disabled",true);
		}else{
			$('#uicNodeId').val(0);
			$('#uicIpAddress').val('');
			$('#uicPortValue').val('');
			$('#uicNodeName').val('');
			$('#uicNodeType').easyDropDown('select','-1');
			$('#uicNodeContent').val('');
			$("#nodeDialText").html('').html('新建节点');
			$("#uicNodeRemove").hide();
//			$('#jobName').attr("disabled",false);
		}
	};
	
	//系统管理/操作管理/新建和修改资源弹窗
	var showResourceDiv=function(timerObj) {
		$("#mask").height($(document).height());  
		$("#mask").width($(document).width()); 
		$("#mask").show();
		$("#taskFrom").show();
		
		if(timerObj==null){  //新增  取消赋值
			$("#uicTimeText").html('').html('添加定时器');
			$("#uicTimeId").val(0);
			$("#uciJobList").easyDropDown('select','-1');
			$("#uciNodeList").easyDropDown('select','-1');
			$("#uicTimeName").val('');
			$("#uicClassName").val('');
			$("#excuteMethod").val('');
			$("#cronExpression").val('');
			$("#uicBusiPara").val('');
			$("#uicEnable").easyDropDown('select','-1');
			$("#uicTimerDesc").val('');
			$("#timeTask").show();
			$("#uciJobList").easyDropDown('enable');
		}else{   //timeId>0 修改  赋值 
			$("#uicTimeText").html('').html('修改定时器');
			$("#uicTimeId").val(timerObj.timerId);
			$("#uciJobList").easyDropDown('select',timerObj.jobId+'');
			$("#uciNodeList").easyDropDown('select',timerObj.nodeId+'');
			$("#uicTimeName").val(timerObj.timerName);
			$("#uicClassName").val(timerObj.className);
			$("#excuteMethod").val(timerObj.excuteMethod);
			$("#cronExpression").val(timerObj.cronExpression);
			$("#uicBusiPara").val(timerObj.busiPara);
			$("#uicEnable").easyDropDown('select',timerObj.enAble+'');
			$("#uicTimerDesc").val(timerObj.timerDesc);
			$("#timeTask").hide();
			$("#uciJobList").easyDropDown('disable');
		}
	}; 
	
	return{
		showResourceDiv:showResourceDiv,
		getjobs:getjobs,
		getnodes:getnodes
	};
})();
