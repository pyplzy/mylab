
var peopleMangeDialog = (function(){
	var curentOrgNode = null;
	var staffIdValue = null;
	var rootOrg = null;
	//清空新增机构人员表
	var resetBaseMsg2 = function(){
		$("#userId2").val('');
		$("#loginId2").val('');
		$("#userName2").val('');
		$("#genderValue2").easyDropDown('select',0);
		$("#mobileNo2").val('');
		$("#userType2").easyDropDown('select',0);
	};
	
	//登录号是否可用
	var checkLoginIdCanUseAndReturn = function (loginId){
		var flag = true;
		$.ajax({
			async : false,
			cache : false,
			method:'POST',
			url:ctx+'/personMsg/menu_toPage_checkLoginIdCanUse.do',
			data:{
				loginId:loginId
			},
			success:function(data){
				if(data=='success'){
				}else{
					flag = false;
				}
			}
		});
		return flag;
	};
	//机构人员新增保存操作
	function isnertUserMsg(){
		var node = curentOrgNode;
		if($.trim($("#loginId2").val())==''){
			showMsgTips('登录号不能为空.',1000);
			return;
		}
		//登录号是否存在
		if(!checkLoginIdCanUseAndReturn($.trim($("#loginId2").val()))){
			showMsgTips('登录号已存在!',1000);
			return;
		}
		
		var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
		if(reg.test($.trim($("#loginId2").val()))){
			showMsgTips('登录号不能包含汉字!',1000);
			return;
		}   
		
		if($.trim($("#userName2").val())==''){
			showMsgTips('用户姓名不能为空!',1000);
			return;
		}
		//手机
		var mobileNo3 = $.trim($("#mobileNo2").val());
		if(mobileNo3==''||mobileNo3.length!=11||!isint(mobileNo3)){
			showMsgTips('请输入正确的手机号!',1000);
			return;
		}
		$.ajax({
			method:'POST',
			url:ctx+'/peopleManage/menu_to_isnertUserMsg.do',
			data:{
				orgId:node.orgId,
				loginId:$.trim($("#loginId2").val()),
				userId:$.trim($("#loginId2").val()),
				userName:$.trim($("#userName2").val()),
				gender:$("#genderValue2").val(),
				mobileNo:$.trim($("#mobileNo2").val()),
				userType:$("#userType2").val()
			},
			success:function(data){
				if(data==true){
					showMsgTips('新增成功!',1000);
					var i = 1;
					setInterval(function() {
						if(i == 0) {
							closeMak();
							$("#addNewOrgPerson").hide();
						} else {
						}
						i--;
					},1000);
					$('.rightWrap').find('iframe').get(0).contentWindow.loadOrgPersonTable(1);
				}else{
					showMsgTips('新增失败!',1000);
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
	    		alert("系统异常,请查看后台异常日志！");
	        }
		});
	}
	
	//检测账loginId（登录号）是否可用
	var checkLoginIdInUse = function(){
		var loginId = $.trim($("#loginId2").val());
		if(loginId==''){
			showMsgTips('登录号不能为空.',1000);
			return;
		}
		var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
		if(reg.test($.trim($("#loginId2").val()))){
			showMsgTips('登录号不能包含汉字!',1000);
			$('#maskItem2').show();
			return;
		}
		$.ajax({
			method:'POST',
			url:ctx+'/personMsg/menu_toPage_checkLoginIdCanUse.do',
			data:{
				loginId:loginId
			},
			success:function(data){
				if(data=='success'){
					showMsgTips('登录号可用.',1000);
				}else{
					showMsgTips('登录号已存在.',1000);
				}
			}
		});
	}
	
	var saveUsers2people = function(){
		var staffId = $("#users2people .activebg").attr('staffId');
		if(staffId==undefined){
			showMsgTips('请选择要添加的人员',1000);
			return;
		}
		var orgId2 = curentOrgNode.orgId;
		var newOrgId = $("#users2people .activebg").attr('orgId');
		if(orgId2==newOrgId){
			showMsgTips('该人员已行政归属'+curentOrgNode.orgName,2000);
			return;
		}
		$.ajax({
			async: false,
			method:'POST',
			url:ctx+'/peopleManage/menu_to_addUserOrgType2.do',  //添加业务归属人员
			data:{
				orgId:orgId2,
				staffId:staffId
			},
			success:function(data){
				if(data=='succ'){
					showMsgTips('添加业务归属人员成功',1000);
					var i = 1;
					setInterval(function() {
						if(i == 0) {
							closeMak();
							$("#addNewOrgPerson").hide();
						} else {
						}
						i--;
					},1000);
					$('.rightWrap').find('iframe').get(0).contentWindow.loadOrgPersonTable(1);
				}else if(data=='use'){
					showMsgTips('该人员已归属'+curentOrgNode.orgName,2000);
				}else{
					showMsgTips('添加业务归属人员失败',1000);
				}
			}
		});
	};
	
	var openAddOrgPersonDialog = function(node){
		curentOrgNode = node;
		$.ajax({
			async: false,
			method:'POST',
			url:ctx+'/peopleManage/menu_to_searchRootOrg.do',
			data:{
				orgId:orgId,
				userId:userId
			},
			success:function(orgMsg){
				if(orgMsg){
					rootOrg=orgMsg;
				}
			}
		});
		$(".titleName3 .left").html('').html('<span style="color:red;">' + node.orgName + '</span>' + "-->新增人员");
		openMask();
		$("#addNewOrgPerson").show();
		$('#users2people').empty();
		$('#userId2people').val('');
		$('#orgName2people').val('');
		queryUsers(1,5);
		//tab页切换
		$(".tabHead ul li").each(function(i){
			$(this).removeClass("select");
		});
		$(".tabHead ul li:eq(0)").addClass("select");
		eHover( ".tabItem>li", "hover" );
		tabClick( ".tabItem>li", ".tabPanel", "select" );
	};
	
	//查询人员
	var queryUsers =function (page,rows){
		var orgId2=$("#orgId2people").val();
		var userId2=$("#userId2people").val();
		$('#users2people').empty();
		$.ajax({
			async: false,
			type: 'POST',
			url: ctx+'/peopleManage/menu_to_getUsersByOrgId.do',
			dataType : 'json',
			data:{
				'orgId':orgId2,
				'userId':userId2,
				'adminId':userId,
				'adminOrgId':rootOrg.orgId,
				'page':page,
				'rows':rows
			},
			success : function(data) {
				if(data.userInfos.length>0) {
					rows2=data.userInfos.length;
					var str="";
					$.each(data.userInfos,function(i,v) {
						str+=
					 ' <tr staffId="'+v.id+'" orgId="'+v.orgId+'">'+
			            '<td width="5"></th>'+
			            '<td width="60">'+v.loginId+'</th>'+
			            '<td width="70">'+v.userName+'</th>'+
			            '<td width="200" title="'+v.orgNameSeq+'">'+v.orgNameSeq+'</th>'+
			          '</tr>';
					});
					$("#users2people").append(str);
					$('#red2user').ossPaginator({
						totalrecords: data.pageObj.total, 
						recordsperpage: 5, 
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
							queryUsers(newPage,5);
				    	}
					});
				}else{
					showMsgTips('未查询到相关成员',1000);
				}	
			}
		});
		$("#users2people tr").on('click',function(){
			chk2(this);
		});
	};
	
	var chk2 = function(obj){
		$(obj).removeClass().addClass("activebg");
		$(obj).siblings().removeClass();
	};
	//是否授权切换
	var changeLine = function(obj){
		$(obj).toggleClass("offno1");
		var isGranted =$(obj).parents('td').attr("isGranted");
		var roleId = $(obj).parents('tr').attr("roleId");
		var backset = $(obj).hasClass("offno1");
		//取消授予角色
		if(isGranted==1 && backset==true){
			$.ajax({
				method:'POST',
				url:ctx+'/peopleManage/menu_to_delPermission.do',
				data:{
					roleId:roleId,
					id:staffIdValue
				},
				success:function(data){
					if(data==true){
						showMsgTips("已取消授予角色", 1000);
						loadRoleTable(appCodeId,1);
					}else{
						showMsgTips("取消授予角色失败", 1000);
					}
				}
			});
		}
		//授予角色
		if(isGranted==0 && backset==false){
			$.ajax({
				method:'POST',
				url:ctx+'/peopleManage/menu_to_givePermission.do',
				data:{
					roleId:roleId,
					id:staffIdValue
				},
				success:function(data){
					if(data==true){
						//changeType授予角色 1
						showMsgTips("已授予角色", 1000);
						loadRoleTable(appCodeId,1);
					}else{
						showMsgTips("授予角色失败", 1000);
					}
				}
			});
		}
	};
	
	//加载roleTable表数据
	var loadRoleTable = function(appCodeId,page){
		if(appCodeId==''){
			appCodeId = 'portal';
		}
		var orgId = $("#province2people").val();
		$.ajax({
			async: false,
			method:'POST',
			url:ctx+'/peopleManage/menu_to_getAllRoleByApp.do',
			data:{
				id:staffIdValue,
				appCode:appCodeId,
				orgId:orgId,
				page:page,
				rows:6
			},
			success:function(data){
				var total = data.pageObj.total;
				var rowsItems = data.roleGrantInfos;
				itemNum = rowsItems.length;
				if(rowsItems){
					var str = '';
					$.each(rowsItems,function(i,item){
						//授权按钮
						var buttonKey = null;
						if(item.isGranted==1){
							buttonKey = '<td width="100" isGranted="'+item.isGranted+'"><a href="javascript:void(0)"  class="onyes1"></a></td>';  
						}else{
							buttonKey = '<td width="100" isGranted="'+item.isGranted+'"><a href="javascript:void(0)"  class="onyes1 offno1"></a></td>';  
						}
						str += '<tr class="item_mask1" roleId='+item.roleId+'>'+
						 '<td width="70" title='+item.roleCode+'>'+item.roleCode+'</td>'+   
				         '<td width="70" title='+item.roleName+'>'+item.roleName+'</td>'+ 
				         '<td width="50" title='+item.orgName+'>'+item.orgName+'</td>'+ 
				         '<td width="100" title='+item.roleDesc+'>'+item.roleDesc+'</td>'+   
				         	buttonKey+   
						 '</tr>';
					});
					$("#roleTable2").empty().append(str);
					
					$('#red2people').ossPaginator({
						totalrecords: total, 
						recordsperpage:6, 
						length: 6, 
						next: '下一页', 
						prev: '上一页', 
						first: '首页', 
						last: '尾页', 
						initval: page,//初始化哪一页被选中
						controlsalways: true,
						display: 'single',
						onchange: function (newPage)
						{
							loadRoleTable(appCodeId,newPage);
				    	}
					});
					
				}else{
					$('#label22').html('').html('该应用下未分配角色!');
					$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox3();">确&nbsp;定</div>');
					$('#deleteMenuId1').show();
				}
			}
		});
		$("#roleTable2 tr a").on('click',function(){
			changeLine(this);
		});
	};
	
	var appCodeId = null;
	var provinceId = null;
	//加载应用下拉框
	var loadPermissionManage = function(){
		$.ajax({
			async : false,
			method:'POST',
			url:ctx+'/roleManage/menu_roleMain_queryDataPri.do',
			success:function(data){
				var flag = false;
				var option = '';
				$.each(data,function(i,item){
					if(item.privilegeValue == 'portal'){
						flag = true;
						return false;
					}
				});
				$.each(data,function(i,v){
					option += '<option value="'+v.privilegeValue+'">'+v.privilegeMemo+'</option>';
				});
				$("#rolePermission").easyDropDown('destroy');
				if(flag){
					$("#rolePermission").append(option);
				}else{
					$("#rolePermission").append('<option value="portal">门户</option>'+option);
				}
	            $("#rolePermission").easyDropDown({
					cutOff: 6,
					onChange: function(selected){
						appCodeId = selected.value;
						if(selected.value=='portal'){
							$("#province2people").easyDropDown('destroy');
							$("#province2people").empty().append('<option value="1" >系统级别</option>');
				            $("#province2people").easyDropDown({});
						}else{
							$.ajax({
								async : false,
								type : 'post',
								url : ctx+'/roleManage/menu_roleMain_queryProvinces.do',
								success : function(data) {
									if(data){
										var str2="";
										$.each(data,function(i,v){
											str2+='<option value="'+v.orgId+'">'+v.orgName+'</option>';
										});
										$("#province2people").easyDropDown('destroy');
										$("#province2people").empty().append(str2);
						                $("#province2people").easyDropDown({
						             		cutOff: 6,
						             		onChange: function(selected){
						            			provinceId= selected.value;
						            			loadRoleTable(appCodeId,1);
						            		}
						             	});
									}
								}
							});
						}
						loadRoleTable(appCodeId,1);
					}
				});
			}
		});
	};
	
	var openRolePermissionDialog = function(userId,staffId){
		staffIdValue = staffId;
		openMask();
		$("#popyurole").show();
		//先清空
		$("#rolePermission").empty();
		$("#province2people").empty();
		$(".titleName1 .left").html('').html('<span style="color:red;">'+userId+'</span>'+"角色维护");
		loadPermissionManage();
		$("#rolePermission").easyDropDown('select',0);
	};
	
	/**临时参数**/
	var menu_param = {
		treeObj: null		//ztree
	};
	//鼠标点击事件 
	var onTreeClick = function(e,treeId,treeNode){
		console.info(111);
		var loginId = $("#loginId").val();
		var orgIdChangeSeq = treeNode.orgIdSeq;
		$.ajax({
			method:'POST',
			url:ctx+'/peopleManage/menu_to_jugeOrgChange.do',
			data:{
				loginId:loginId,
				orgIdChangeSeq:orgIdChangeSeq
			},
			success:function(data){
				if(data=='no'){
					alert("机构信息不可跨省市修改！");
				}else{
					$("#popedzh input[name=orgId]").val(treeNode.orgId);
					$('#popedzh input[name=orgName]').val(treeNode.orgName);
					$('#popedzh .jigouWrap2').hide();
				}
			}
		});
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
			url : ctx + "/peopleManage/menu_to_getOrgTree.do",
			autoParam : [ "orgId=orgId" ]
		},
		callback : {
			onClick : onTreeClick
		}
	};
	var setting2 = {
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
				url : ctx + "/peopleManage/menu_to_getOrgTree.do",
				autoParam : [ "orgId=orgId" ]
			},
			callback : {
				onClick : onTreeClick2people
			}
		};
	
	var showOrgs = function(){
		var dataItem = null;
		$.ajax({
			async: false,
			method:'POST',
			url:ctx+'/peopleManage/menu_to_searchRootOrg.do',
			data:{
				orgId:orgId,
				userId:userId
			},
			success:function(orgMsg){
				if(orgMsg){
					dataItem=orgMsg;
				}
			}
		});
		var data={'orgId':dataItem.orgId,'pOrgId':dataItem.pOrgId,'orgName':dataItem.orgName,'orgIdSeq':dataItem.orgIdSeq,
					'orgNameSeq':dataItem.orgNameSeq,'orgMemo':dataItem.orgMemo,'orgGrade':dataItem.orgGrade,
					'displayOrder':dataItem.displayOrder,'administrativeGrade':dataItem.administrativeGrade,
					'orgCode':dataItem.orgCode,'orgCodeSeq':dataItem.orgCodeSeq,
					'isLeaf':dataItem.isLeaf,'isParent':true,'open':true};
		//环节库树初始化
		menu_param.treeObj = $.fn.zTree.init($('#popedzh .ztree'),setting,data);
		$('#popedzh .jigouWrap2').show();
	};
	
	var resetUserPassWord = function(){
		var password = $.trim($("#loginId").val())+(new Date).getFullYear();
	    var value = prompt('输入重置密码：',password);  
	    if(value == ''){
	    	alert('密码输入为空！');
	    }else if(value == null){
	    }else{
	    	var loginId = $.trim($("#loginId").val());
	    	var newPassword = md5(loginId+''+value);
			$.ajax({
				method:'POST',
				url:ctx+'/peopleManage/menu_to_updatePassWord.do',
				data:{
					loginId:loginId,
					newPassword:newPassword
				},
				success:function(data){
					if(data==true){
						showMsgTips("重置成功!", 1000);
					}else{
						showMsgTips("重置失败!", 1000);
					}
				}
			});
	    }
	};
	
	var userObj = null;
	//打开修改信息弹出框
	var openUserDialog = function(userId){
		openMask();
		$("#popedzh").show();
		//请求userId的个人信息       
		$.ajax({
			method:'POST',
			url:ctx+'/peopleManage/menu_to_serchUserMsg.do',
			data:{userId:userId},
			success:function(data){
				if(data){
					//清空个人信息
					resetBaseMsg();
					//加载个人信息
					loadBaseMsg(data);
					userObj = data;
					$(".titleName2 .left").html('').html('<span style="color:red;">'+data.loginId+'</span>'+"修改信息"); 
				}else{
					//信息为空
					showMsgTips("个人信息为空!", 1000);
					return;
				}
			}
		});
	};
	
	var updateBaseMsg = function(){
		if($.trim($("#loginId").val())==''){
			showMsgTips("登录号为空!", 1000);
			return;
		}
		if($.trim($("#userName").val())==''){
			showMsgTips("用户姓名为空!", 1000);
			return;
		}
		//手机
		var mobileNo3 = $.trim($("#mobileNo").val());
		if(mobileNo3==''||mobileNo3.length!=11||!isint(mobileNo3)){
			showMsgTips("请输入正确的手机号!", 1000);
			return;
		}
		$.ajax({
			method:'POST',
			url:ctx+'/peopleManage/menu_to_updateBaseMsg.do',
			data:{
				loginId:$.trim($("#loginId").val()),
				userName:$.trim($("#userName").val()),
				gender:$("#genderValue").val(),
				mobileNo:$.trim($("#mobileNo").val()),
				userType:$("#userType").val(),
				createDate:$("#createTime").val(),
				orgId:$("#popedzh input[name=orgId]").val()
			},
			success:function(data){
				if(data){
					showMsgTips("保存成功!", 1000);
					var i = 1;
					timer=setInterval(function() {
						if(i == 0) {
							clearInterval(timer);
							closeMak();
							$("#popedzh").hide();
						} else {
						}
						i--;
					},1000);
					$('.rightWrap').find('iframe').get(0).contentWindow.searchUserMsgItem();
				}else{
					showMsgTips("保存失败!", 1000);
				}
			}
		});
	};
	
	var loadBaseMsg = function(data){
		$("#loginId").val(data.loginId);
		$("#userId").val(data.userId);
		$("#userName").val(data.userName);
		$("#popedzh input[name=orgId]").val(data.orgId);
		$('#popedzh input[name=orgName]').val(data.orgName);
		$("#genderValue").easyDropDown('select',data.gender+'');
		$("#mobileNo").val(data.mobileNo);
		$("#userType").easyDropDown('select',data.userType+'');
		$("#createTime").val(data.createDate);
	};
	//清空个人信息表
	var resetBaseMsg = function(){
		$("#userId").val('');
		$("#loginId").val('');
		$("#userName").val('');
		$("#genderValue").easyDropDown('select',0);
		$("#mobileNo").val('');
		$("#userType").easyDropDown('select',0);
		$("#createTime").val('');
	};
	
	var openMask = function(){
		$("#mask").height($(document).height());  
		$("#mask").width($(document).width()); 
		$("#mask").show();
	};
	var closeMak = function(){
		$("#mask").hide();
	};
	
	//鼠标点击事件 
	function onTreeClick2people(e,treeId,treeNode){
		$("#orgId2people").val(treeNode.orgId);
		$("#orgName2people").val(treeNode.orgName);
		$("#treeWrap2people").slideUp();
	}
	//关闭修改用户基本信息框
	$('#popedzh').find('.icon_close').on('click',function(){
		closeMak();
		$("#popedzh").hide();
	});
	//关闭授予角色基本信息框
	$('#popyurole').find('.icon_close').on('click',function(){
		closeMak();
		$("#popyurole").hide();
	});
	$('#addNewOrgPerson').find('.icon_close').on('click',function(){
		closeMak();
		$("#addNewOrgPerson").hide();
	});
	$('#popyurole').find('.btn').on('click',function(){
		loadRoleTable(appCodeId,1);
	});
	
	$('#popedzh .baseItem1 a').on('click',resetUserPassWord);
	$('#popedzh input[name=orgName]').on('click',showOrgs);
	
	$('#popedzh .popbtm1 a:eq(1)').on('click',function(){
		loadBaseMsg(userObj);
	});
	$('#popedzh .popbtm1 a:eq(0)').on('click',function(){
		updateBaseMsg();
	});
	$('#addNewOrgPerson .ml20').on('click',function(){
		queryUsers(1,5);
	});
	$('#addNewOrgPerson .baseItem1 a').on('click',function(){
		checkLoginIdInUse();
	});
	
	//添加行政归属人员保存
	$('#addNewOrgPerson .popbtm a:eq(0)').on('click',function(){
		isnertUserMsg();
	});
	//添加行政归属人员重置
	$('#addNewOrgPerson .popbtm a:eq(1)').on('click',function(){
		resetBaseMsg2();
	});
	//添加业务归属人员关闭
	$('#addNewOrgPerson .popbtm1 a:eq(1)').on('click',function(){
		closeMak();
		$("#addNewOrgPerson").hide();
	});
	//业务归属人员保存
	$('#addNewOrgPerson .popbtm1 a:eq(0)').on('click',function(){
		saveUsers2people();
	});
	
	$('#orgName2people').on('click',function(){
		var data={'orgId':rootOrg.orgId,'pOrgId':-1,'orgName':rootOrg.orgName,'isLeaf':0,'isParent':true,'open':true};
		$.fn.zTree.init($('#treeDemo2people'),setting2,data);
		$("#treeWrap2people").slideDown();
	});
	
	//点击屏幕其他地方弹窗收起
	$(document).mouseup(function(e){
		if(e.which!=3){
		 	var _con = $("#popedzh .jigouWrap2"); // 设置目标区域
			if (!_con.is(e.target) && _con.has(e.target).length === 0) { // Mark
																			// 1
				$("#popedzh .jigouWrap2").slideUp();
			}
			var _con2 = $("#addNewOrgPerson .jigouWrap"); // 设置目标区域
			if (!_con2.is(e.target) && _con2.has(e.target).length === 0) { // Mark
																			// 1
				$("#addNewOrgPerson .jigouWrap").slideUp();
			}
		}
	});
	
	/*--------------选项卡--------------*/
	var eHover = function ( elem, cla ){
		$(document).on("mouseover", elem, function(){
			$(this).addClass( cla );
		});
		$(document).on("mouseout", elem, function(){
			$(this).removeClass( cla );
		});
	};
	//选项卡点击
	var tabClick = function( tabItem, content, cla ){
		$(content).each(function(i){ //遍历选项卡内容 显示第一个
	        $(this).children().first().show();
	        $(this).children().first().siblings().hide();
	    });
		$(document).on("click", tabItem, function(){
			var p = $(this).parent().parent();
			var n = p.next("div").children();
			p.children("ul").children("li").removeClass( cla );
			$(this).addClass( cla );
			n.hide();
			n.eq( $(this).index() ).show();
		});
	};
	
	return {
		openUserDialog : openUserDialog,
		openRolePermissionDialog : openRolePermissionDialog,
		openAddOrgPersonDialog : openAddOrgPersonDialog
	};
})();