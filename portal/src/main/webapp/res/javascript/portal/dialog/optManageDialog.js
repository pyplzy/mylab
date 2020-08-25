var optDialog=(function(){
	var resourceObj = null;
	var optObj = null;
	$('#optDialogResClose').on('click',function(){
		closeResourceDiv();
	});
	$('#optDialogSaveRes').on('click',function(){
		saveResource();
	});
	$('#optDialogForResReset').on('click',function(){
		resetResource();
	});
	$("#resourceCode").blur(function(){
		judgeResource();
	});
	$('#optDialogOptClose').on('click',function(){
		closeOptDiv();
	});
	$('#optDialogOptSave').on('click',function(){
		saveOpt();
	});
	$('#optDialogOptReset').on('click',function(){
		resetOpt();
	});
	
	//系统管理/操作管理/新建和修改资源弹窗
	var showResourceDiv=function(resourceId) {
		resourceObj = null;
		$("#mask").height($(document).height());  
		$("#mask").width($(document).width()); 
		$("#mask").show();
		$("#resource").show();
		if(resourceId==0){   //新建
			$("#resourceText").html('').html('新建资源');
			$("#resourceId").val(0);
			loadAppForResource();
		}else{
			$("#resourceText").html('').html('修改资源');
			$("#resourceId").val(resourceId);
			loadAppForResource();
			//查询资源
			$.ajax({
				async : false,
				cache : true,
				type : 'post',
				url : 'optManage/menu_toOpt_queryReById.do',
				data:{
					'resourceId':resourceId
				},
				success : function(data) {
					resourceObj = data;
					$("#appForResource").easyDropDown('select',data.appCode);
					$("#resourceCode").val(data.resourceCode);
					$("#resourceName").val(data.resourceName);
					$("#resourceDesc").val(data.resourceDesc);
					$('#resourceCode').prop('disabled',"disabled");
				}
			});
		}
	}; 
	
	var resetResourceForm=function(){
		//清除内容
		$("#appForResource").easyDropDown('select','-1');
		$("#resourceCode").val("");
		$("#resourceName").val("");
		$("#resourceDesc").val("");
		$('#resourceCode').removeAttr("disabled");
	};
	//加载应用
	var loadAppForResource=function(){
		$.ajax({
			async : false,
			cache : true,
			type : 'post',
			url : 'roleManage/menu_roleMain_queryDataPri.do',
			data:{
				'userId':userId
			},
			success : function(data) {
				if(data){
					var str='<option value="-1">---请选择---</option>';
					$.each(data,function(i,v){
						str+='<option value="'+v.privilegeValue+'">'+v.privilegeMemo+'</option>';
					});
					$("#appForResource").easyDropDown('destroy');
					$("#appForResource").empty().append(str);
	                $("#appForResource").easyDropDown({});
				}
			}
		});
	};
	
	//校验提示
	var showResourceDialog=function(msg) {
		$(".msgTips").html(msg);
		$(".msgTips").show();
		setTimeout(function() {
			$('.msgTips').hide();
		}, 1000);
	};
	//检测资源编码是否有效
	var judgeResource=function(){
		var appCode=$("#appForResource").val();
		if(appCode!="-1" ){
			var resourceCode=$("#resourceCode").val();
			if($.trim(resourceCode)==""){
				showMsgTips('资源编码不可用',1000);
				$("#resourceCode").focus(); 
				return;
			}
			$.ajax({
				async : false,
				type : 'post',
				url : ctx+'/optManage/menu_toOpt_queryResource.do',
				data:{
					'appCode':appCode,
					'resourceCode':resourceCode
				},
				success : function(data){
					if(data == 'use'){
						showMsgTips('资源编码不可用',1000);
						$("#resourceCode").focus(); 
					}
					if(data == 'yes'){
						showMsgTips('资源编码可用',1000);
					}
				}
			});
		}
	};
	
	/*关闭*/ 
	var closeResourceDiv=function() { 
		$("#appForResource").empty().append('<option value="-1">---请选择---</option>');
		$("#mask").hide();
		$("#resource").hide();
		resetResourceForm();
	};
	/*关闭*/ 
	var closeOptDiv=function closeOptDiv() { 
		$("#mask").hide();
		$("#optDialog").hide();
		$("#resourceId2").val("");
		$("#optId").val("");
		$("#optCode").val("");
		$("#optName").val("");
	};
	
	//添加资源的重置按钮
	var resetResource=function (){
		if(resourceObj==null){
			//清除内容
			$("#appForResource").easyDropDown('select','-1');
			$("#resourceCode").val("");
			$("#resourceName").val("");
			$("#resourceDesc").val("");
		}else{
			$("#appForResource").easyDropDown('select',resourceObj.appCode);
			$("#resourceCode").val(resourceObj.resourceCode);
			$("#resourceName").val(resourceObj.resourceName);
			$("#resourceDesc").val(resourceObj.resourceDesc);
		}
	};
	//保存
	var saveResource=function(){
		var resourceId = $("#resourceId").val();
		var appCode = $("#appForResource").val();
		var resourceCode = $.trim($("#resourceCode").val());
		var resourceName = $.trim($("#resourceName").val());
		var resourceDesc = $.trim($("#resourceDesc").val());
		if(appCode=="-1" ){
			showMsgTips("请选择资源所属应用",1000);
			$("#appForResource").focus(); 
			return;
		}
		if(resourceCode==""){
			showResourceDialog('资源编码不可为空');
			$("#resourceCode").focus(); 
			return;
		}
		$.ajax({
			type : 'post',
			url : ctx+'/optManage/menu_toOpt_addAndEditResource.do',
			data:{
				'resourceId':resourceId,
				'appCode':appCode,
				'resourceCode':resourceCode,
				'resourceName':resourceName,
				'resourceDesc':resourceDesc
			},
			success : function(data) {
				if(data == 'succ'){
					showReturnMsg("保存成功");
					$('.rightWrap').find('iframe').get(0).contentWindow.queryResources(1);
				}else{
					showResourceDialog('保存失败');
				}
			}
		});
	};
	
	var showReturnMsg=function(msg){
		$(".msgTips").html(msg);
		$(".msgTips").show();
		setTimeout(function() {
			$('.msgTips').hide();
			closeResourceDiv();
		}, 1000);
	};
	
	var showReturnMsg2=function(msg){
		$(".msgTips").html(msg);
		$(".msgTips").show();
		setTimeout(function() {
			$('.msgTips').hide();
			$("#optCode").val('');
			$("#optName").val('');
			$("#mask").hide();
			$("#optDialog").hide();
		}, 1000);
	};
	
	var showParentOptDiv=function(resourceName,resourceId,optId) { 
		optObj = null;
		$("#mask").height($(document).height());  
		$("#mask").width($(document).width()); 
		$("#mask").show();
		$("#optDialog").show();
		if(optId==0){   //新建
			$("#optText").html('').html('<span style="color:red;">'+resourceName+'</span>'+'新建操作权限');
			$("#optId").val(0);
			$("#resourceId2").val(resourceId);
			$("#optCode").val("");
			$("#optName").val("");
		}else{
			$("#optText").html('').html('<span style="color:red;">'+resourceName+'</span>'+'修改操作权限');
			$("#optId").val(optId);
			$("#resourceId2").val(resourceId);
			//查询权限
			$.ajax({
				async : false,
				type : 'post',
				url : ctx+'/optManage/menu_toOpt_queryOptById.do',
				data:{
					'resourceId':resourceId,
					'optId':optId
				},
				success :function(data){
					optObj = data;
					$("#optCode").val(data.privilegeCode);
					$("#optName").val(data.privilegeName);
				}
			});
		}
	};
	
	//权限保存
	var saveOpt=function(){
		var resourceId = $("#resourceId2").val();
		var optId = $("#optId").val();
		var optCode = $.trim($("#optCode").val());
		var optName = $.trim($("#optName").val());
		if(optCode==""){
			showResourceDialog('权限编码不可为空');
			$("#optCode").focus(); 
			return;
		}
		//查询权限
		$.ajax({
			async : false,
			type : 'post',
			url : ctx+'/optManage/menu_toOpt_addAndEditOpt.do',
			data:{
				'resourceId':resourceId,
				'privilegeCode':optCode,
				'privilegeName':optName,
				'privilegeId':optId
			},
			success : function(data) {
				if(data=="use"){
					showResourceDialog('权限编码已存在');
				}
				if(data=="succ"){
					showReturnMsg2("保存成功");
					$('.rightWrap').find('iframe').get(0).contentWindow.loadOptTable(1);
				}
				if(data=="fail"){
					showReturnMsg2("保存失败");
				}
			}
		});
	};
	
	var resetOpt=function(){
		if(optObj==null){
			$("#optCode").val("");
			$("#optName").val("");
		}else{
			$("#optCode").val(optObj.privilegeCode);
			$("#optName").val(optObj.privilegeName);
		}
	};
	return{
		showResourceDiv:showResourceDiv,
		showParentOptDiv:showParentOptDiv
	};
})();
