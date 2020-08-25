var roleDialog=(function(){
	var globalRoleType="";
	var globalRoleName="";
	var globalRoleMemo="";
	var globalProvince="";
	var timer = null;
	var roleIdForDialog = null;
	var rolenameDialog = null;
	//服务于check()方法
	var rows2=0;
	/**临时参数**/
	var menu_param = {
		treeObj: null,		//ztree
		currentNode: null,	//当前编辑节点
		opet: null,			//操作类型 0修改1新增
		nodeList: [],		//当前搜索到的节点
		reset: function(){
			this.currentNode = null;
			this.opet = null;
		}
	};
	var setting = {
			data : {
				key : {
					name : 'orgName'
				},
				simpleData : {
					enable : true,
					idKey : 'orgId',
					pIdKey : 'pOrgId'
				}
			},
			async : {
				enable : true,
				type : "POST",
				url : "./peopleManage/menu_to_getOrgTree.do",
				autoParam : [ "orgId=orgId" ]
			},
			callback : {
				onClick :function(e,treeId,treeNode){
						$("#orgId2").val(treeNode.orgId);
						$("#orgName2").val(treeNode.orgName);
						$("#treeWrap12").slideUp();
					}
			}
		};
	//添加角色事件
	$('#roleDialogCloseAddRole').on('click',function(){
		closeDiv1();
	});
	$("#roleCode").blur(function(){
 		judgeRole();
	});
	$('#roleDialogSaveRole').on('click',function(){
		saveRole();
	});
	$('#roleDialogResetDiv').on('click',function(){
		resetDiv();
	});
	//更新角色事件
	$('#roleDialogCloseUpdRole').on('click',function(){
		closeDiv4();
	});
	$('#roleDialogUpdRoleSave').on('click',function(){
		updRole();
	});
	$('#roleDialogResetDiv2').on('click',function(){
		resetDiv2();
	});
	//添加人事件
	$('#roleDialogClosePeople').on('click',function(){
		closeDiv8();
	});
	$('#orgName2').on('click',function(){
		tree();
	});
	$('#roleDialogPeopleQuery').on('click',function(){
		queryUsers();
	});
	$('#all2').on('click',function(){
		allC2();
	});
	$('#roleDialogPeopleSaveUsers').on('click',function(){
		saveUsers2();
	});
	$('#roleDialogPeopleCloseDiv').on('click',function(){
		closeDiv8();
	});
	
	//系统管理/角色管理/新建角色弹窗
	var showDiv1=function() { 
		var flag=$(window.frames["mainFrame"].document).contents().find("#flag").val();
		if( flag == 'true' ){
			$('#roleTT').show();		
		}else{
			$('#roleTT').hide();
		}
		$("#mask").height($(document).height());  
		$("#mask").width($(document).width()); 
		$("#mask").show();
		$("#addRole").show();
		loadApps();
	}; 
	//加载应用
	var loadApps=function(){
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
					var str="";
					$.each(data,function(i,v){
						str+='<option value="'+v.privilegeValue+'">'+v.privilegeMemo+'</option>';
					});
					$("#dropdownid").easyDropDown('destroy');
					$("#dropdownid").append(str);
	                $("#dropdownid").easyDropDown({
	            		onChange: function(selected){
	            			if(selected.value=='portal'){
	            				loadProvinces2();
	            			}else{
	            				loadProvinces();
	            			}
	            		}
	            	
	                });
				}
			}
		});
	}
	//加载省份
	var loadProvinces2=function(){
		$("#province2").easyDropDown('destroy');
		$("#province2").empty().append('<option value="" >---请选择---</option><option value="1" >系统级别</option>');
	    $("#province2").easyDropDown({});
	}
	//加载省份
	var loadProvinces=function(){
		$.ajax({
			async : false,
			cache : true,
			type : 'post',
			url : 'roleManage/menu_roleMain_queryProvinces.do',
			success : function(data) {
				if(data){
					var str2="";
					$.each(data,function(i,v){
						str2+='<option value="'+v.orgId+'">'+v.orgName+'</option>';
					});
					str2 = '<option value="" >---请选择---</option>'+str2;
					$("#province2").easyDropDown('destroy');
					$("#province2").empty().append(str2);
	                $("#province2").easyDropDown({
	             		cutOff: 6
	             	});
				}
			}
		});
	}
	
	/*关闭*/ 
	var closeDiv1=function(){ 
		clearInterval(timer);
		//清除内容
		$("#dropdownid").empty().append('<option value="">---请选择---</option>');
		$("#dropdownid").val("");
		$("#province2").easyDropDown('destroy');
		$("#province2").empty().append('<option value="" >---请选择---</option>');
	    $("#province2").easyDropDown({});
	    
		$("#province2").val("");
		$("#roleCode").val("");
		$("#roleName").val("");
		$("#roleMemo").val("");
		$("#mask").hide();
		$("#addRole").hide();
	};
	
	//添加角色的重置按钮
	var resetDiv=function(){
		//清除内容
		var flag=$(window.frames["mainFrame"].document).contents().find("#flag").val();
		if(flag=='true'){
			$('#tt').show();
		}else{
			$('#tt').hide();
		}
		/*$("#dropdownid").easyDropDown('destroy');
		$("#dropdownid").easyDropDown({
			cutOff: 3
		});
		$("#dropdownid").empty();
		$("#dropdownid").easyDropDown('destroy');
		$("#dropdownid").easyDropDown({
			cutOff: 3
		});*/
		$("#dropdownid").easyDropDown('select', 0);
		$("#province2").empty();
		$("#province2").append(
		'<option value="">---请选择---</option>');

		$("#province2").easyDropDown('destroy');
		var $selects = $('#province2');
		$("#province2").easyDropDown({
		cutOff : 6
		});
		$("#dropdownid").val("");
		$("#roleCode").val("");
		$("#roleName").val("");
		$("#roleMemo").val("");
	}
	
	//判断roleCode是否可用
	var judgeRole=function(){
 		var appCode=$("#dropdownid").val();
		if(appCode != ""){
			var roleCode=$("#roleCode").val();
			if($.trim(roleCode)==""){
				showMsgTips('角色编码为空',1000);
				$("#roleCode").focus(); 
				return;
			}
			$.ajax({
				async : true,
				cache : false,
				type : 'post',
				dataType : 'json',
				url : 'roleManage/menu_roleMain_queryRole.do',
				data:{
					'appCode':appCode,
					'roleCode':roleCode
				},
				success : function(data) {
					if(data){
						showMsgTips('角色编码不可用',1000);
						$("#roleCode").focus(); 
						return;
					}else{
						showMsgTips('角色编码可用',1000);
						return;
					}
				}
			});
		}
	}
	
	//保存角色
	var saveRole=function(){
		var appCode=$("#dropdownid").val();
		var province=$("#province2").val();
		if(appCode==""||appCode=="---请选择---"){
			showMsgTips('请选择角色所属应用',1000);
			$("#dropdownid").focus(); 
			return;
		}
		var roleCode=$("#roleCode").val();
		if($.trim(roleCode)==""){
			showMsgTips('角色编码不可用',1000);
			$("#roleCode").focus(); 
			return;
		}
		$.ajax({
			async : true,
			cache : false,
			type : 'post',
			dataType : 'json',
			url : 'roleManage/menu_roleMain_queryRole.do',
			data:{
				'appCode':appCode,
				'roleCode':roleCode
			},
			success : function(data) {
				if(data){
					showMsgTips('角色编码不可用',1000);
					$("#roleCode").focus(); 
					return;
				}else{
					//showDialog4('角色编码可用');
				}
			}
		});
		var roleName=$("#roleName").val();
		var roleType;
		if($("#roleTT").children().length>0){
			roleType=$("input[name='roleType']:checked").val();
		}else{
			roleType=1;
		}
		var roleMemo=$("#roleMemo").val();
		$.ajax({
			async : true,
			cache : false,
			type : 'post',
			dataType : 'json',
			url : 'roleManage/menu_roleMain_addRole.do',
			data:{
				'appCode':appCode,
				'roleCode':roleCode,
				'roleName':roleName,
				'roleType':roleType,
				'roleMemo':roleMemo,
				'province':province
			},
			success : function(data) {
				if(data==true){
					showMsgTips('添加角色成功',1000);
					var i = 1;
					timer=setInterval(function() {
						if(i == 0) {
							clearInterval(timer);
							closeDiv1();
						} else {
						}
						i--;
					},1000);
					var pageC=$(window.frames["mainFrame"].document).contents().find("#pageC").val();
					$('.rightWrap').find('iframe').get(0).contentWindow.queryRole(pageC);
				}else{
					showMsgTips('添加角色失败',1000);
				}
			},
			error : function(textStatus, errorThrown) {
				showMsgTips('添加角色失败',1000);
			}
		});
	}
	
	//系统管理/角色管理/修改角色弹窗
	var showDiv2=function(roleId) { 
		var appCode=$(window.frames["mainFrame"].document).contents().find("#dropdownValue").val();
		$.ajax({
			async : true,
			cache : false,
			type : 'post',
			dataType : 'json',
			url : 'roleManage/menu_roleMain_queryRoleById.do',
			data:{
				'appCode':appCode,
				'roleId':roleId
			},
			success : function(data) {
				if(data){
					globalRoleType=data.roleType;
					globalRoleName=data.roleName;
					globalRoleMemo=data.roleMemo;
					globalProvince=data.orgName;
					//赋值
					var flag=$(window.frames["mainFrame"].document).contents().find("#flag").val();
					if(flag=='true'){
						if(data.roleType==1){
							$('#tt2').empty();
							$('#tt2').append(
									"<dt>角色类型</dt>"+
						        	"<dd>"+
						        	'<label><input type="radio" name="roleType" value="1" checked="checked" />应用级</label>'+
						            '<label><input type="radio" name="roleType" value="0" />系统级</label>'+
						            "</dd>"		
							);
						}else{
							$('#tt2').empty();
							$('#tt2').append(
									"<dt>角色类型</dt>"+
						        	"<dd>"+
						        	'<label><input type="radio" name="roleType" value="1"  />应用级</label>'+
						            '<label><input type="radio" name="roleType" value="0" checked="checked" />系统级</label>'+
						            "</dd>"		
							);
						}
					}else{
						$('#tt2').empty();
					}
					$("#appCode3").val('').val(data.appCode);
					$("#appName3").val('').val(data.appName);
					$("#roleCode2").val('').val(data.roleCode);
					$("#roleName2").val('').val(data.roleName);
					$("#province3").val('').val(data.orgName);
					$("#roleMemo2").val('').val(data.roleMemo);
					$("#roleId").val(data.roleId);
					//显示弹窗
					$("#mask").height($(document).height());  
					$("#mask").width($(document).width()); 
					$("#mask").show();
					$("#updRole").show();
				}
			}
		});
	}; 
	
	//修改角色的重置按钮
	var resetDiv2=function(){
		//清除内容
		var flag=$(window.frames["mainFrame"].document).contents().find("#flag").val();
		if(flag=='true'){
			if(globalRoleType==1){
				$('#tt2').empty();
				$('#tt2').append(
						"<dt>角色类型</dt>"+
			        	"<dd>"+
			        	'<label><input type="radio" name="roleType" value="1" checked="checked" />应用级</label>'+
			            '<label><input type="radio" name="roleType" value="0" />系统级</label>'+
			            "</dd>"		
				);
			}else{
				$('#tt2').empty();
				$('#tt2').append(
						"<dt>角色类型</dt>"+
			        	"<dd>"+
			        	'<label><input type="radio" name="roleType" value="1"  />应用级</label>'+
			            '<label><input type="radio" name="roleType" value="0" checked="checked" />系统级</label>'+
			            "</dd>"		
				);
			}
		}else{
			$('#tt2').empty();
		}
		$("#roleName2").val(globalRoleName);
		$("#roleMemo2").val(globalRoleMemo);
	}
	
	//修改角色
	var updRole=function(){
		var roleId=$("#roleId").val();
		var appCode=$("#appCode3").val();
		var roleCode=$("#roleCode2").val();
		var roleName=$("#roleName2").val();
		var roleType;
		if($("#tt2").children().length>0){
			roleType=$("input[name='roleType']:checked").val();
		}else{
			roleType=1;
		}
		var roleMemo=$("#roleMemo2").val();
		
		$.ajax({
			async : true,
			cache : false,
			type : 'post',
			dataType : 'json',
			url : ctx+'/roleManage/menu_roleMain_updRole.do',
			data:{
				'appCode':appCode,
				'roleId':roleId,
				'roleCode':roleCode,
				'roleName':roleName,
				'roleType':roleType,
				'roleMemo':roleMemo
			},
			success : function(data) {
				if(data==true){
					showMsgTips('修改角色成功',1000);
					var i = 1;
					timer=setInterval(function() {
						if(i == 0) {
							clearInterval(timer);
							closeDiv4();
						} else {
						}
							i--;
					},1000);
					$('.rightWrap').find('iframe').get(0).contentWindow.queryRole(1);
				}else{
					showMsgTips('修改角色失败',1000);
				}
			}
		});
	}
	
	/*关闭修改角色弹窗*/ 
	var closeDiv4=function() { 
		//清除内容
		$("#appCode3").val("");
		$("#roleCode2").val("");
		$("#roleName2").val("");
		$("#roleMemo2").val("");
		
		$("#mask").hide();
		$("#updRole").hide();
	};
	
	//根据角色ID查询出的未添加进该角色的员工,用于选择添加
	var getUsers2=function(page,rows){
		//点击屏幕其他地方弹窗收起
		$(document).mouseup(function(e){
			if(e.which!=3){
			 	var _con = $("#treeWrap12");   // 设置目标区域
				if(!_con.is(e.target) && _con.has(e.target).length === 0){ // Mark 1
			 	 	$("#treeWrap12").slideUp();	
				 }
			}
		});
		
		var orgId2=$("#orgId2").val();
		var userId2=$("#userId2").val();
		$.ajax({
			type: 'POST',
			url: ctx+'/roleManage/menu_roleMain_getUsers2.do',
			dataType : 'json',
			async: false,
			data:{
				'orgId':orgId2,
				'userId':userId2,
				'roleId':roleIdForDialog,
				'adminId':userId,
				'adminOrgId':orgId,
				'page':page,
				'rows':rows
			},
			success : function(data) {
				$('#usersRoleDialog').empty();
				var str="";				
				if(data.userInfos.length>0) {
					$("#titleAdd").html("<span style='color:red;'>"+rolenameDialog+"</span>-->添加成员");
					rows2=data.userInfos.length;
					$.each(data.userInfos,function(i,v) {
						str+=
					 ' <tr onclick="parent.roleDialog.check2(this);" >'+
			            '<td width="15"><input type="checkBox" name="userRadio"  value="'+v.id+'" onclick="parent.roleDialog.check3(this,event)"  /></th>'+
			            '<td width="60">'+v.userId+'</th>'+
			            '<td width="70">'+v.userName+'</th>'+
			            '<td width="200" title="'+v.orgNameSeq+'">'+v.orgNameSeq+'</th>'+
			          '</tr>';
					});
					$("#usersRoleDialog").append(str);
					$('#red2').ossPaginator({
						totalrecords: data.pageObj.total, 
						recordsperpage: 8, 
						length: 4, 
						next: '下一页', 
						prev: '上一页', 
						first: '首页', 
						last: '尾页', 
						initval: page,//初始化哪一页被选中
						display: 'single',
						controlsalways: true,
						onchange: function (newPage)
						{
							getUsers2(newPage,8);
				    	}
					});
					showDiv9();
				}else{
					showMsgTips('未查询到相关成员',1000);
				}	
			}
		});
	}
	
	//系统管理添加成员弹窗
	var showDiv8=function(roleId,name) { 
		roleIdForDialog = roleId;
		rolenameDialog = name;
		getUsers2(1,8);
	}; 
	
	var showDiv9=function(){
		$("#mask").height($(document).height());  
		$("#mask").width($(document).width()); 
		$("#mask").show();
		$("#poeple").show();
	}
	//批量添加成员
	var saveUsers2=function(){
		var roleId = roleIdForDialog;
		if(roleId==null){
			showMsgTipsForRoleDialog("请选择一个角色",1000);
			return;
		}
		//roleId;id1:id2.......
		var str ='';
		$('input[name="userRadio"]:checked').each(function(){
			str+=$(this).val()+":";
		});
		if(str.length==0){
			showMsgTipsForRoleDialog("请至少选择一个成员",1000);
			return;
		}
		str=roleId+";"+str;
		$.ajax({
			type: 'POST',
			url: 'roleManage/menu_roleMain_saveUsers2.do',
			dataType : 'json',
			async: false,
			data:{
				'str':str
			},
			success : function(data) {
				if(data==true){
					showMsgTipsForRoleDialog('添加成员成功',1000);
					var i = 1;
					timer=setInterval(function() {
						if(i == 0) {
							clearInterval(timer);
							closeDiv8();
						} else {
						}
						i--;
					},1000);
	
					$(window.frames["mainFrame"].document).contents().find("#tab2").trigger("click");
					$("#all2").prop("checked",false);
				}else{
					showMsgTipsForRoleDialog('添加成员失败',1000);
					
				}
			}
		});
	}
	
	/*关闭*/ 
	var closeDiv8=function() { 
		clearInterval(timer);
		$("#orgId2").val("");
		$("#orgName2").val("");
		$("#userId2").val("");
		roleIdForDialog = null;
		rolenameDialog = null;
		$("#mask").hide();
		$("#poeple").hide();
	};
	
	//点击该按钮全选，或取消全选
	var allC2=function (){
		if($("#all2").prop("checked")){
			$("input[name='userRadio']").prop('checked',true);
		}else {
			$("input[name='userRadio']").prop('checked',false);
		}
	}
	
	//点击tr时触发
	var check2=function (obj){
		if($(obj).find(":input").prop("checked")){
			$(obj).find(":input").prop("checked",false);
		}else{
			$(obj).find(":input").prop("checked",true);
		}
		
		if($("#all2").prop("checked")){
			$("#all2").prop('checked',false);
		}
		var num=$("input[name='userRadio']:checked").length;
		if(rows2==num){
			$("#all2").prop('checked',true);
		}
	}
	
	//点击checkBox时触发
	var check3=function(obj,e){
		if($("#all2").prop("checked")){
			$("#all2").prop('checked',false);
		}
		var num=$("input[name='userRadio']:checked").length;
		if(rows2==num){
			$("#all2").prop('checked',true);
		}
		//阻止冒泡事件  
		stopBubble(e);
	}
	var stopBubble=function(e) {  
	    if (e && e.stopPropagation) {//非IE  
	        e.stopPropagation();  
	    }  
	    else {//IE  
	        window.event.cancelBubble = true;  
	    }  
	}
	
	//鼠标点击事件 
//	var onTreeClick=function(e,treeId,treeNode){
//		$("#orgId2").val(treeNode.orgId);
//		$("#orgName2").val(treeNode.orgName);
//		$("#treeWrap12").slideUp();
//	}
	
	//给角色添加成员，让操作人员可以通过选择机构树，查询该机构下的人员
	var tree=function(){
		var data={'orgId':orgId,'pOrgId':-1,'orgName':orgName,'isLeaf':0,'isParent':true,'open':true};
		$.fn.zTree.init($('#treeDemo12'),setting,data);
		$("#treeWrap12").slideDown();
	}
	
	//查询人员：根据机构id和登录帐号
	var queryUsers=function(){
		getUsers2(1,10);
	}
	//校验提示
	var showMsgTipsForRoleDialog=function(msg,time){
		$("#msgDialog").html(msg);
		$("#msgDialog").show();
		setTimeout(function() {
			$('#msgDialog').hide();
		}, time);
	}
	return{
		showDiv1:showDiv1,
		showDiv2:showDiv2,
		showDiv8:showDiv8,
		check2:check2,
		check3:check3
	}
})();
