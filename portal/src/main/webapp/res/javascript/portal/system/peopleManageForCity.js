var itemNum = 0;
var appNameValue = '';
var staffId = '';
var itemsRows = [];
var menuUrl = '';
var dataItem = null;
var globeOrgId=0;
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
var setting={
		data:{
			key: {
				name: 'orgName'
			},
			simpleData:{
				enable: true,
				idKey: 'orgId',
				pIdKey: 'pOrgId'
			}
		},
		async:{
			enable:true,
			type:"POST",
			url:"menu_to_getOrgTree.do",
			autoParam:["orgId=orgId"]
		},
		callback:{
			onClick : onTreeClick
//			onRightClick:onTreeRclick
		}
};

var setting2={
		data:{
			key: {
				name: 'orgName'
			},
			simpleData:{
				enable: true,
				idKey: 'orgId',
				pIdKey: 'pOrgId'
			}
		},
		async:{
			enable:true,
			type:"POST",
			url:ctx+"/peopleManage/menu_to_getOrgTree.do",
			autoParam:["orgId=orgId"]
		},
		callback:{
			onClick : onTreeClick2
		}
};

$(document).ready(function(){
	
	$('#userType').easyDropDown({
				cutOff: 4,
				onChange: function(selected){
//					console.info(selected);
				}
			});
	$.ajax({
		async: false,
		method:'POST',
		url:'menu_to_searchLoginOrg.do',
		data:{
			orgId:orgIdItem
		},
		success:function(orgMsg){
			if(orgMsg){
				dataItem=orgMsg;
			}
		}
	});
	var data={'orgId':dataItem.orgId,'pOrgId':dataItem.pOrgId,'orgName':dataItem.orgName,'orgIdSeq':dataItem.orgIdSeq,
				'orgNameSeq':dataItem.orgNameSeq,'orgMemo':dataItem.orgMemo,'orgGrade':dataItem.orgGrade,
				'displayOrder':dataItem.displayOrder,'isAdministrative':dataItem.isAdministrative,
				'isLeaf':dataItem.isLeaf,'isParent':true,'open':true};
	//环节库树初始化
	$.fn.zTree.init($('#treeDemo'),setting,data);
	//展开根节点
	menu_param.treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	
	//tab页切换
	eHover( ".tabItem>li", "hover" );
	tabClick( ".tabItem>li", ".tabPanel", "select" );
	
	$(".viewItem0").click(function(){
		$(this).parent().slideUp();
	});

	//点击屏幕其他地方弹窗收起
	$(document).mouseup(function(e){
		if(e.which!=3){
		 	var _con = $(".pop_view0");   // 设置目标区域
			if(!_con.is(e.target) && _con.has(e.target).length === 0){ // Mark 1
		 	 	$(".pop_view0").slideUp();	
			 }
		}
	});
	
	//点击屏幕其他地方弹窗收起
	$(document).mouseup(function(e){
		if(e.which!=3){
		 	var _con = $(".jigouWrap");   // 设置目标区域
			if(!_con.is(e.target) && _con.has(e.target).length === 0){ // Mark 1
		 	 	$(".jigouWrap").slideUp();	
			 }
		}
	});
	
});

//给角色添加成员，让操作人员可以通过选择机构树，查询该机构下的人员
function showOrgs(){
	var data={'orgId':dataItem.orgId,'pOrgId':dataItem.pOrgId,'orgName':dataItem.orgName,'orgIdSeq':dataItem.orgIdSeq,
			'orgNameSeq':dataItem.orgNameSeq,'orgMemo':dataItem.orgMemo,'orgGrade':dataItem.orgGrade,
			'displayOrder':dataItem.displayOrder,'isAdministrative':dataItem.isAdministrative,
			'isLeaf':dataItem.isLeaf,'isParent':true,'open':true};
	$.fn.zTree.init($('#treeDemo2'),setting2,data);
	$("#orgTree").show();
}
//鼠标点击事件 
function onTreeClick2(e,treeId,treeNode){
	var loginId = $("#loginId").val();
	var orgIdChangeSeq = treeNode.orgIdSeq;
	$.ajax({
		method:'POST',
		url:'menu_to_jugeOrgChange.do',
		data:{
			loginId:loginId,
			orgIdChangeSeq:orgIdChangeSeq
		},
		success:function(data){
			if(data=='no'){
				alert("机构信息不可跨省市修改！");
			}else{
				$("#orgId2").val('').val(treeNode.orgId);
				$("#orgName2").val('').val(treeNode.orgName);
				$(".jigouWrap").hide();
			}
		}
	});
	
}

//刷新
function reload(){
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var node = menu_param.currentNode;
	//更新当前节点
	treeObj.reAsyncChildNodes(node, "refresh", false);
	//展开节点
	treeObj.expandNode(node,true);
	//加载机构信息form
//	loadMenuForm();
}
//增加子机构
function append(){
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var node = menu_param.currentNode;
	menuUrl = 'menu_to_insertOrg.do';
	menu_param.opet = 1;
	//展开节点
	treeObj.expandNode(node,true);
	$(".tabHead ul li").each(function(i){
		$(this).removeClass("select");
	});
	$(".tabHead ul li:eq(1)").addClass("select");
	//进机构人员tab页
	$(".tabPanel").each(function(i){ 
        $(this).children().first().show();
    });
	var p = $(".tabHead");
	var n = p.next("div").children();
	n.hide();
	n.eq(1).show();
	//清空机构信息form
	orgFormReset();
	$("#orgSeq").val(node.orgIdSeq);
	$("#orgNameSeq").val(node.orgNameSeq);
	$("#pOrgName").val(node.orgName);
	$("#orgGrade").easyDropDown('select',node.orgGrade-1);
}
//删除
function removeit(){
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var node = menu_param.currentNode;
	//展开节点
	treeObj.expandNode(node,true);
	//更新当前节点
	treeObj.updateNode(node,false);
	
	if(node.isAdministrative==1){
		$('#deleteMenuId1').show();
		$('#label22').html('').html('行政机构不予删除!');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox();">确&nbsp;定</div>');
		$('#maskItem').show();
		return;
	}
	$.ajax({
		async : true,
		cache : false,
		method:'POST',
		url:'menu_to_jugeCandel.do',
		data:{
			userId:'',
			userName:'',
			orgId:node.orgId,
			rows:10,
			page:1
		},
		success:function(data){
			if(data.hasChildOrg=='yes'){
				$('#deleteMenuId1').show();
				$('#label22').html('').html('删除失败,该机构下存在子机构!');
				$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox();">确&nbsp;定</div>');
				$('#maskItem').show();
			}else if(data.users.length>0){
				$('#deleteMenuId1').show();
				$('#label22').html('').html('删除失败,该机构下存在用户!');
				$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox();">确&nbsp;定</div>');
				$('#maskItem').show();
			}else{
				$('#deleteMenuId1').show();
				$('#label22').html('').html('确定要删除该机构吗?');
				$('.msgbox-ft1').html('').html('<div class="menu1" onclick="deleteMEnu();">确&nbsp;定</div>');
				$('#maskItem').show();
			}
		}
	});
	
}
//删除子机构 确认
function deleteMEnu(){
		$.ajax({
			async : true,
			cache : false,
			method:'POST',
			url : 'menu_to_deleteOrgMenu.do',
			data:{
				orgId:menu_param.currentNode.orgId
			},
			success : function(data) {
				if(data==true){
					$('#deleteMenuId1').hide();
					$('#label22').html('').html('删除成功!');
					$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox10();">确&nbsp;定</div>');
					$('#deleteMenuId1').show();
				}else{
					$('#deleteMenuId1').hide();
					$('#label22').html('').html('删除失败!');
					$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox();">确&nbsp;定</div>');
					$('#deleteMenuId1').show();
				}
			}
		});
}
//机构信息保存按钮
function saveOrgBaseMsg(){
	var node = menu_param.currentNode;
	var isLeaf = $('input:radio[name=ynRadio]:checked').val();
	if(node==null){
		$('#label22').html('').html('请选择操作机构.');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		$('#maskItem').show();
		return;
	}
	if(node.isAdministrative==1 && menu_param.opet==0){
		$('#label22').html('').html('行政机构不予修改!');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		$('#maskItem').show(); 
		return;
	}
	if($.trim($("#orgName").val())==''){
		$('#label22').html('').html('机构名称不能为空!');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		$('#maskItem').show();
		return;
	}
	if($.trim($("#displayOrder").val())==''){
		$('#label22').html('').html('显示顺序不能为空!');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		$('#maskItem').show();
		return;
	}
	if(!(isLeaf==0 || isLeaf==1 )){
		$('#label22').html('').html('请选择是否叶子节点!');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		$('#maskItem').show();
		return;
	}
	//新增时机构机构级别不能高于其父机构
	var orgId = null;  //机构id
	var orgSeq = null;  //机构ID序列
	var orgNameSeq = null;  //机构名称序列
	var pOrgId = null;  //父机构ID
	//修改
	if(menu_param.opet == 0){
		orgId = node.orgId;
		pOrgId = node.pOrgId;
	}
	//新增
	if(menu_param.opet == 1){
		//新增时 树选中的机构自动变为父机构
		pOrgId = node.orgId;
		orgId = -2;
		
		//新增时机构机构级别不能高于其父机构
		var orgGradeOld = node.orgGrade;
		var orgGradeNew = $("#orgGrade").val();
		if(orgGradeNew<orgGradeOld){
			$('#label22').html('').html('新增机构级别不能高于其父机构!');
			$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox();">确&nbsp;定</div>');
			$('#deleteMenuId1').show();
			$('#maskItem').show();
			return ;
		}
		
	}
	openLoad('保存中...');
	$.ajax({
		method:'POST',
		url:menuUrl,
		data:{
			orgId:orgId,
			orgName:$.trim($("#orgName").val()),
			orgIdSeq:$.trim($("#orgSeq").val()),
			orgNameSeq:$.trim($("#orgNameSeq").val()),
			pOrgId:pOrgId,
			isLeaf:isLeaf,
			displayOrder:$.trim($("#displayOrder").val()),
			orgGrade:$("#orgGrade").val(),
			orgMemo:$.trim($("#orgMemo").val()),
			state:1
		},
		success:function(data){
			if(data==true){
				$('#label22').html('').html('保存成功!');
				$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox9();">确&nbsp;定</div>');
				$('#deleteMenuId1').show();
				$('#maskItem').show();
				closeLoad();
			}else{
				$('#label22').html('').html('保存失败!');
				$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox();">确&nbsp;定</div>');
				$('#deleteMenuId1').show();
				$('#maskItem').show();
			}
		}
	});
}

//ztree左键点击节点事件处理
function onTreeClick(e,treeId,treeNode){
	menu_param.currentNode = treeNode;
	menu_param.opet = 0 ;
	var node = menu_param.currentNode;
	$("#titleName22").html('').html('<span style="color:red;">'+node.orgName+'</span>'+'-->机构详情');
	//进机构人员tab									
	$(".tabHead ul li").each(function(i){
		$(this).removeClass("select");
	});
	$(".tabHead ul li:eq(1)").addClass("select");
	//进机构人员tab页
	showTab( ".tabItem>li", ".tabPanel", "select" );
	//加载机构信息
	loadOrgBaseMsg();
	loadOrgPersonTable(1);
	//清空人员信息查询表
	$("#baseMsgTable").empty();
	$("#red").hide();
}
//加载机构信息
function loadOrgBaseMsg(){
	menuUrl = 'menu_to_updateOrg.do';
	var node = menu_param.currentNode;
	orgFormReset();
	var pNode = null;
	if(node.orgId == orgIdItem){
		$("#pOrgName").val('');
	}else{
		pNode = node.getParentNode();
		$("#pOrgName").val(pNode.orgName);
	}
	//清空机构信息form
	$("#orgName").val(node.orgName);
	$("#orgSeq").val(node.orgIdSeq);
	$("#orgNameSeq").val(node.orgNameSeq);
	$("#displayOrder").val(node.displayOrder);
	$("#orgMemo").val(node.orgMemo);
	if(node.orgGrade==0){
		$("#orgGrade").easyDropDown('select',5);
	}else{
		$("#orgGrade").easyDropDown('select',node.orgGrade-1);
	}
	if(node.isLeaf==1){
		$("input[name='ynRadio'][value=1]").prop("checked",true);
	}else{
		$("input[name='ynRadio'][value=0]").prop("checked",true);
	}
}

//加载机构人员数据
function loadOrgPersonTable(page){
	var node = menu_param.currentNode;
	$.ajax({
		method:'POST',
		url:'menu_to_getUserList.do',
		data:{
			userId:'',
			userName:'',
			orgId:node.orgId,
			rows:10,
			page:page
		},
		success:function(data){
			if(data){
				var total = data.pageObj.total;
				var usersItems = data.users; 
				$("#orgPersonTable").empty();
				$.each(usersItems,function(index, item){
					var gend='';
					if(item.gender==1){gend = '男';}
					if(item.gender==2){gend = '女';}
					if(item.gender==0){gend = '未知';}
					$("#orgPersonTable").append(
							'<tr staffId="'+item.id+'" userId="' + item.userId +'" loginId="' + item.loginId + '">'+
							'	<td width="60"><a href="javascript:void(0)" onclick="showBaseMsg(this)">'+item.loginId +'</a></td>'+	
							'	<td width="60">'+item.userName+'</td>'+	
							'	<td width="20">'+gend+'</td>'+	
							'	<td width="130" title="'+item.orgNameSeq+'">'+item.orgNameSeq+'</td>'+	
							'	<td width="70"><a href="javascript:void(0)" onclick="openRolePermission(this)">授予角色</a>'+'&nbsp;'+'<a href="javascript:void(0)" onclick="delectItem(this);">删除</a></td>'+	
							'</tr>'
					);
				});
				$('#red3').ossPaginator({
						totalrecords: total, 
						recordsperpage:10, 
						length: 6, 
						next: '下一页', 
						prev: '上一页', 
						first: '首页', 
						last: '尾页', 
						initval: page,//初始化哪一页被选中
						display: 'single',
						controlsalways: true,
						onchange: function (newPage)
						{
							loadOrgPersonTable(newPage);
				    	}
					});
				//重新调整iframe高度，防止分页不显示（防止第二个选项卡内容较多切换为第一个选项卡，翻页时内容较少，再点击第二个选项卡导致翻页消失）
				window.parent.changeHeight();
			}
				
		}
	});
}

//加载人员查询信息
function loadBaseMsgTable(page){
//	var node = menu_param.currentNode;
//	var userId = $.trim($("#baseId").val());
//	var userName = $.trim($("#baseName").val());
	openLoad('加载中...');
	$("#red").show();
	$.ajax({
		method:'POST',
		url:'menu_to_getAllUserList.do',
		data:{
			userId:$.trim($("#baseId").val()),
			userName:$.trim($("#baseName").val()),
			orgId:orgIdItem,
			rows:10,
			page:page
		},
		success:function(data){
			if(data){
				var total = data.pageObj.total;
				var usersItems = data.users; 
				$("#baseMsgTable").empty();
				$.each(usersItems,function(index, item){
					var gend='';
					if(item.gender==1){gend = '男';}
					if(item.gender==2){gend = '女';}
					if(item.gender==0){gend = '未知';}
					$("#baseMsgTable").append(
							'<tr staffId="'+item.id+'" userId="' + item.userId +'" loginId="' + item.loginId + '">'+
							'	<td width="60"><a href="javascript:void(0)" onclick="showBaseMsg(this)">'+item.loginId +'</a></td>'+	
							'	<td width="60">'+item.userName+'</td>'+	
							'	<td width="20">'+gend+'</td>'+	
							'	<td width="130" title="'+item.orgNameSeq+'">'+item.orgNameSeq+'</td>'+	
							'	<td width="70"><a href="javascript:void(0)" onclick="openRolePermission(this)">授予角色</a>'+'&nbsp;'+'<a href="javascript:void(0)" onclick="delectItem(this);">删除</a></td>'+	
							'</tr>'
					);
				});
				$('#red').ossPaginator({
						totalrecords: total, 
						recordsperpage:10, 
						length: 6, 
						next: '下一页', 
						prev: '上一页', 
						first: '首页', 
						last: '尾页', 
						initval: page,//初始化哪一页被选中
						display: 'single',
						controlsalways: true,
						onchange: function (newPage)
						{
							loadBaseMsgTable(newPage);
				    	}
					});
				closeLoad();	
				//重新调整iframe高度，防止分页不显示（防止第二个选项卡内容较多切换为第一个选项卡，翻页时内容较少，再点击第二个选项卡导致翻页消失）
				window.parent.changeHeight();
			}
				
		}
	});
}

//机构人员删除
function delectItem(obj){
	var userId = $(obj).parents("tr").attr("userId");
	if(window.confirm('是否确认删除？')){
			$.ajax({
				method: 'POST',
				url: 'menu_to_removeOrgUser.do',
				data:{
					userId:userId
				},
				success : function(data) {
					if(data==true) {
						$(obj).parents('tr').remove();
						showDialog('成功删除改机构人员.');
					}
				}
			});
		}
}

//基本信息tab页 搜索
function searchUserMsgItem(){
	
	loadBaseMsgTable(1);
	//清空搜索框
//	$("#baseId").val('');
//	$("#baseName").val('');
}

//右击节点事件处理
function onTreeRclick(e,treeId,treeNode){
//	/***重置表单和临时参数***/
	menu_param.reset();
//	/***清除其他节点的选中状态***/
	menu_param.treeObj.cancelSelectedNode();
	menu_param.treeObj.selectNode(treeNode);
	menu_param.currentNode = treeNode;
	clearMM();
	if(treeNode.pOrgId == null){
		var bg = document.getElementById("mm0");
 		bg.style.display = "block";
 		bg.style.left = e.clientX +"px";
 		bg.style.top = e.clientY +"px";
 
	}else if(treeNode.isParent == true){
		var bg = document.getElementById("mm1");
		bg.style.display = "block"; 
 		bg.style.left = e.clientX +"px";
 		bg.style.top = e.clientY +"px";
	}else{
		var bg = document.getElementById("mm2");
		bg.style.display = "block";
 		bg.style.left = e.clientX +"px";
 		bg.style.top = e.clientY +"px";
	}

}
//机构人员新增弹框
function addOrgPersonal(){
	 var node = menu_param.currentNode;
	 if(node==null){
		 $('#label22').html('').html('请选择机构!');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		$('#maskItem').show();
		return;
	 }
 	$(".titleName3 .left").html('').html('<span style="color:red;">'+node.orgName+'</span>'+"-->新增人员");
	var wg = document.getElementById("mask6"); 
	var w = document.body.scrollWidth; //网页正文全文宽 
	var h = document.body.scrollHeight; //网页正文全文高 
	wg.style.display = "block"; 
	wg.style.width = w + "px"; 
	wg.style.height = h + "px"; 
	var con = document.getElementById("addNewOrgPerson");
	con.style.display = "block";

}
//授予角色弹窗
function openRolePermission(obj) { 
	var wg = document.getElementById("mask6"); 
	var w = document.body.scrollWidth; //网页正文全文宽 
	var h = document.body.scrollHeight; //网页正文全文高 
	wg.style.display = "block"; 
	wg.style.width = w + "px"; 
	wg.style.height = h + "px"; 
	var con = document.getElementById("popyurole");
	con.style.display = "block";
	//先清空
	$("#rolePermission").empty();
	$("#province").empty();
	var userId = $(obj).parents('tr').attr('userId');
	$(".titleName1 .left").html('').html('<span style="color:red;">'+userId+'</span>'+"角色维护");
	staffId = $(obj).parents('tr').attr('staffId');
	loadPermissionManage();
	loadProvinces();
	loadRoleTable('',1);
};
//加载省份
function loadProvinces(){
	$.ajax({
		async : false,
		cache : false,
		method:'POST',
		url:ctx+'/roleManage/menu_roleMain_queryProvinces.do',
		success:function(data){
			if(data){
				$.each(data,function(i,item){
					$("#province").append('<option value="'+item.orgId+'">'+item.orgName+'</option>');
				});
				$("#province").easyDropDown('destroy');
	            $("#province").easyDropDown({
					cutOff: 6,
					onChange: function(selected){
						globeOrgId = selected.value;
						//加载roleTable表数据
						loadRoleTable(appNameValue,1);
					}
				});
			}
		}
	});
	globeOrgId = $("#province").val();
}
//加载应用下拉框
function loadPermissionManage(){
	$.ajax({
		async : false,
		cache : false,
		method:'POST',
		url:'menu_to_getAppCodes.do',
		success:function(data){
			var flag = false;
			var option = '';
			$.each(data,function(i,item){
				if(item.appCode == 'portal'){
					flag = true;
					return false;
				}
			});
			$.each(data,function(i,item){
				option += '<option value="'+item.appCode+'">'+item.appName+'</option>';
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
					appNameValue = selected.value;
					//加载roleTable表数据
					loadRoleTable(appNameValue,1);
				}
			});
		}
	});
}
//加载roleTable表数据
function loadRoleTable(appNameValue,page){
	if(appNameValue==''){
		appNameValue = 'portal';
	}
	var orgId = $("#province").val();
	openLoad('加载中...');
	//清空表格
	$("#roleTable").empty();
	$.ajax({
		method:'POST',
		url:'menu_to_getAllRoleByApp.do',
		data:{
			id:staffId,
			appCode:appNameValue,
			orgId:orgId,
			page:page,
			rows:6
		},
		success:function(data){
			var total = data.pageObj.total;
			var rowsItems = data.roleGrantInfos;
			itemNum = rowsItems.length;
			if(rowsItems){
				$.each(rowsItems,function(i,item){
					//加载一行表格
					appenRoleTable(item);
				});
				$('#red2').ossPaginator({
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
						loadRoleTable(appNameValue,newPage);
			    	}
				});
				closeLoad();
//				$(".onyes1").click(function(){
//					$(this).toggleClass("offno1");
//				});
			}else{
				$('#label22').html('').html('该应用下未分配角色!');
				$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox3();">确&nbsp;定</div>');
				$('#deleteMenuId1').show();
			}
		}
	});
}

function appenRoleTable(item){
	//授权按钮
	var buttonKey = null;
	if(item.isGranted==1){
		buttonKey = '<td width="100" isGranted="'+item.isGranted+'"><a href="javascript:void(0)" onclick="changeLine(this, event);" class="onyes1"></a></td>';  
	}else{
		buttonKey = '<td width="100" isGranted="'+item.isGranted+'"><a href="javascript:void(0)" onclick="changeLine(this, event);" class="onyes1 offno1"></a></td>';  
	}
		 
	$("#roleTable").append(
		 '<tr class="item_mask1" onclick="clickOnetr(this, event);" roleId='+item.roleId+'>'+
		 '<td width="70" title='+item.roleCode+'>'+item.roleCode+'</td>'+   
         '<td width="70" title='+item.roleName+'>'+item.roleName+'</td>'+ 
         '<td width="50" title='+item.orgName+'>'+item.orgName+'</td>'+ 
         '<td width="100" title='+item.roleDesc+'>'+item.roleDesc+'</td>'+   
         	buttonKey+   
		 '</tr>');
	//刷新一行用
	itemsRows.push(item);
}
//取当行数据
function getRoleIdItem(roleId){
	for(var i = 0, n = itemsRows.length; i < n; i++) {
		if(itemsRows[i].roleId == roleId) {
			return itemsRows[i];
		}
	}
}
//刷新一行
function refreshItem(roleId, changeType){
	var item = getRoleIdItem(roleId);
	var buttonKey = null;
	if(changeType==1){
		buttonKey = '<td width="100" isGranted="'+changeType+'"><a href="javascript:void(0)" onclick="changeLine(this, event);" class="onyes1"></a></td>';  
	}else{
		buttonKey = '<td width="100" isGranted="'+changeType+'"><a href="javascript:void(0)" onclick="changeLine(this, event);" class="onyes1 offno1"></a></td>';  
	}
	$('tr[roleId="' + item.roleId +'"]').empty().append(
         '<td width="70" title='+item.roleCode+'>'+item.roleCode+'</td>'+   
         '<td width="70" title='+item.roleName+'>'+item.roleName+'</td>'+ 
         '<td width="50" title='+item.orgName+'>'+item.orgName+'</td>'+ 
         '<td width="100" title='+item.roleDesc+'>'+item.roleDesc+'</td>'+   
         	buttonKey);  
}

//是否授权切换
function changeLine(obj, e){
	$(obj).toggleClass("offno1");
	//阻止冒泡事件  
	stopBubble(e);
	var isGranted =$(obj).parents('td').attr("isGranted");
	var roleId = $(obj).parents('tr').attr("roleId");
	var backset = $(obj).hasClass("offno1");
	//取消授予角色
	if(isGranted==1 && backset==true){
		$.ajax({
			method:'POST',
			url:'menu_to_delPermission.do',
			data:{
				roleId:roleId,
				id:staffId
			},
			success:function(data){
				if(data==true){
					//changeType消授予角色  0
					$('#label22').html('').html('已取消授予角色.');
					$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
					$('#deleteMenuId1').show();
					$('#maskItem2').show();
					
					refreshItem(roleId,0);
				}else{
					$('#label22').html('').html('取消授予角色失败.');
					$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
					$('#deleteMenuId1').show();
					$('#maskItem2').show();
				}
			}
		});
	}
	//授予角色
	if(isGranted==0 && backset==false){
		$.ajax({
			method:'POST',
			url:'menu_to_givePermission.do',
			data:{
				roleId:roleId,
				id:staffId
			},
			success:function(data){
				if(data==true){
					//changeType授予角色 1
					$('#label22').html('').html('已授予角色');
					$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
					$('#deleteMenuId1').show();
					$('#maskItem2').show();
					refreshItem(roleId,1);
				}else{
					$('#label22').html('').html('授予角色失败');
					$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
					$('#deleteMenuId1').show();
					$('#maskItem2').show();
				}
			}
		});
	}
}

//选中加背景色
function clickOnetr(obj, e){
	$(obj).toggleClass("backset");
}

//用户授权查询
function searchRoleMsg(){
	loadRoleTable(appNameValue,1);
}


/*关闭*/ 
function closeDiv6() { 
	var bg = document.getElementById("mask6"); 
	var con = document.getElementById("popyurole"); 
	bg.style.display = "none"; 
	con.style.display = "none"; 
};

//个人基本信息弹框
function showBaseMsg(obj){
	var userId = $(obj).parents('tr').attr('userId');//工号
	var loginId = $(obj).parents('tr').attr('loginId');//登录号
	$(".titleName2 .left").html('').html('<span style="color:red;">'+loginId+'</span>'+"修改信息");   
	//请求userId的个人信息       
	$.ajax({
		method:'POST',
		url:'menu_to_serchUserMsg.do',
		data:{userId:userId},
		success:function(data){
			if(data){
				openUserDial();
				//清空个人信息
				resetBaseMsg();
				//加载个人信息
				$("#loginId").val(loginId);
				$("#userId").val(userId);
				$("#userName").val(data.userName);
				$("#orgId2").val('').val(data.orgId);
				$("#orgName2").val('').val(data.orgName);
				var indexGender = 0;
				if(data.gender==1){indexGender=0;}
				if(data.gender==2){indexGender=1;}
				if(data.gender==0){indexGender=2;}
				$("#genderValue").easyDropDown('select',indexGender);
				$("#mobileNo").val(data.mobileNo);
				$("#userState").easyDropDown('select',data.state);
				var indexType = 1;
				if(data.userType==1){indexType=0;}
				if(data.userType==2){indexType=1;}
				if(data.userType==3){indexType=2;}
				$("#userType").easyDropDown('select',indexType);
				$("#createTime").val(data.createDate);
			}else{
				//信息为空
				$('#label22').html('').html('个人信息为空!');
				$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox();">确&nbsp;定</div>');
				$('#deleteMenuId1').show();
				$('#maskItem').show();
				return;
			}
		}
	});
}

//工号是否可用
function checkUserIdPermission(userId){
	var flag = true;
	$.ajax({
		async : false,
		cache : false,
		method:'POST',
		url:'menu_to_checkUserIdInUse.do',
		data:{
			userId:userId
		},
		success:function(data){
			flag = data;
		}
	});
	return flag;
}
//登录号是否可用
function checkLoginIdCanUseAndReturn(loginId){
	var flag = true;
	$.ajax({
		async : false,
		cache : false,
		method:'POST',
		url:'../personMsg/menu_toPage_checkLoginIdCanUse.do',
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
}

//机构人员新增保存操作
function isnertUserMsg(){
	 var node = menu_param.currentNode;
//	 if(node==null){
//		 $('#label22').html('').html('请选择机构!');
//		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
//		$('#deleteMenuId1').show();
//		$('#maskItem2').show();
//		return;
//	 }
	if($.trim($("#loginId2").val())==''){
		$('#label22').html('').html('登录号不能为空!');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		$('#maskItem2').show();
		return;
	}
	//登录号是否存在
	if(!checkLoginIdCanUseAndReturn($.trim($("#loginId2").val()))){
		$('#label22').html('').html('登录号已存在!');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		$('#maskItem2').show();
		return;
	}
	
	var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
	if(reg.test($.trim($("#loginId2").val()))){
		$('#label22').html('').html('登录号不能包含汉字!');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		$('#maskItem2').show();
		return;
	}   
	
//	if($.trim($("#userId2").val())==''){
//		$('#label22').html('').html('工号不能为空!');
//		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
//		$('#deleteMenuId1').show();
//		$('#maskItem2').show();
//		return;
//	}
//	//工号是否存在
//	if(!checkUserIdPermission($.trim($("#userId2").val()))){
//		$('#label22').html('').html('工号已存在!');
//		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
//		$('#deleteMenuId1').show();
//		$('#maskItem2').show();
//		return;
//	}
	
	if($.trim($("#userName2").val())==''){
		$('#label22').html('').html('用户姓名不能为空!');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		$('#maskItem2').show();
		return;
	}
	//手机
	var mobileNo3 = $.trim($("#mobileNo2").val());
	if(mobileNo3==''||mobileNo3.length!=11||!isint(mobileNo3)){
		$('#label22').html('').html('请输入正确的手机号!');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		$('#maskItem2').show();
		return;
	}
	openLoad('保存中...');
	$.ajax({
		method:'POST',
		url:'menu_to_isnertUserMsg.do',
		data:{
			orgId:node.orgId,
			loginId:$.trim($("#loginId2").val()),
			userId:$.trim($("#loginId2").val()),
			userName:$.trim($("#userName2").val()),
			gender:$("#genderValue2").val(),
			mobileNo:$.trim($("#mobileNo2").val()),
			state:$("#userState2").val(),
			userType:$("#userType2").val()
		},
		success:function(data){
			if(data==true){
				$('#label22').html('').html('新增成功!');
				$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox8();">确&nbsp;定</div>');
				$('#deleteMenuId1').show();
				$('#maskItem2').show();
				closeLoad();
			}else{
				$('#label22').html('').html('新增失败!');
				$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
				$('#deleteMenuId1').show();
				$('#maskItem2').show();
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			closeLoad();
    		alert("系统异常,请查看后台异常日志！");
        }
	});
}
function updateBaseMsg(){
	if($.trim($("#loginId").val())==''){
		$('#label22').html('').html('登录账号为空!');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox3();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		return;
	}
	if($.trim($("#userName").val())==''){
		$('#label22').html('').html('用户姓名为空!');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox3();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		return;
	}
	//手机
	var mobileNo3 = $.trim($("#mobileNo").val());
	if(mobileNo3==''||mobileNo3.length!=11||!isint(mobileNo3)){
		$('#label22').html('').html('请输入正确的手机号!');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox3();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		return;
	}
//	if($.trim($("#mobileNo").val())==''){
//		$('#label22').html('').html('手机号为空!');
//		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox3();">确&nbsp;定</div>');
//		$('#deleteMenuId1').show();
//		return;
//	}
	$.ajax({
		method:'POST',
		url:'menu_to_updateBaseMsg.do',
		data:{
			loginId:$.trim($("#loginId").val()),
			userName:$.trim($("#userName").val()),
			gender:$("#genderValue").val(),
			mobileNo:$.trim($("#mobileNo").val()),
			state:$("#userState").val(),
			userType:$("#userType").val(),
			createDate:$("#createTime").val(),
			orgId:$("#orgId2").val()
		},
		success:function(data){
			if(data){
				$('#label22').html('').html('保存成功!');
				$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox14();">确&nbsp;定</div>');
				$('#deleteMenuId1').show();
			}else{
				$('#label22').html('').html('保存失败!');
				$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox3();">确&nbsp;定</div>');
				$('#deleteMenuId1').show();
			}
		}
	});
}

//清空机构信息表
function orgFormReset(){
	$("#orgName").val('');
	$("#orgSeq").val('');
	$("#orgNameSeq").val('');
	$("#pOrgName").val('');
	$("#orgMemo").val('');
	$("#displayOrder").val('');
	var ynRadio = document.getElementsByName("ynRadio");
	for(var i=0; i<ynRadio.length; i++){
		ynRadio[i].checked = false;
	}
	$("#orgGrade").easyDropDown('select', 0);
}

//清空新增机构人员表
function resetBaseMsg2(){
	$("#userId2").val('');
	$("#loginId2").val('');
	$("#userName2").val('');
	$("#genderValue2").easyDropDown('select',0);
	$("#mobileNo2").val('');
	$("#userState2").easyDropDown('select',0);
	$("#userType2").easyDropDown('select',0);
}
//信息修改框重置
function resetBaseMsg1(){
	$("#userName").val('');
	$("#genderValue").easyDropDown('select',0);
	$("#mobileNo").val('');
	$("#userState").easyDropDown('select',0);
	$("#userType").easyDropDown('select',0);
	$("#userName2").val('');
}

function resetUserPassWord(){
	var loginId = $.trim($("#loginId").val());
	if(loginId==''){
		$('#label22').html('').html('重置失败!');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox3();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		return;
	}else{
		$('#label22').html('').html('确定要重置密码?');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox5();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
	}
}
//检测账userId（工号）是否可用
function checkUserIdInUse(){
	var userId2 = $.trim($("#userId2").val());
	if(userId2==''){
		$('#label22').html('').html('工号不能为空.');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		$('#maskItem2').show();
		return;
	}
	$.ajax({
		method:'POST',
		url:'menu_to_checkUserIdInUse.do',
		data:{
			userId:userId2
		},
		success:function(data){
			if(data==true){
				$('#label22').html('').html('工号可用.');
				$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
				$('#deleteMenuId1').show();
				$('#maskItem2').show();
			}else{
				$('#label22').html('').html('工号已存在.');
				$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
				$('#deleteMenuId1').show();
				$('#maskItem2').show();
			}
		}
	});
}

//检测账loginId（登录号）是否可用
function checkLoginIdInUse(){
	var loginId = $.trim($("#loginId2").val());
	if(loginId==''){
		$('#label22').html('').html('登录账号不能为空.');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		$('#maskItem2').show();
		return;
	}
	var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
	if(reg.test($.trim($("#loginId2").val()))){
		$('#label22').html('').html('登录号不能包含汉字!');
		$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
		$('#deleteMenuId1').show();
		$('#maskItem2').show();
		return;
	}
	
	$.ajax({
		method:'POST',
		url:'../personMsg/menu_toPage_checkLoginIdCanUse.do',
		data:{
			loginId:loginId
		},
		success:function(data){
			if(data=='success'){
				$('#label22').html('').html('登录账号可用.');
				$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
				$('#deleteMenuId1').show();
				$('#maskItem2').show();
			}else{
				$('#label22').html('').html('登录账号已存在.');
				$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox7();">确&nbsp;定</div>');
				$('#deleteMenuId1').show();
				$('#maskItem2').show();
			}
		}
	});
}
//确定重置密码
function closeMsgbox5(){
	var loginId = $.trim($("#loginId").val());
	$.ajax({
		method:'POST',
		url:'menu_to_updatePassWord.do',
		data:{
			loginId:loginId
		},
		success:function(data){
			if(data==true){
				$('#label22').html('').html('重置成功!');
				$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox3();">确&nbsp;定</div>');
				$('#deleteMenuId1').show();
			}else{
				$('#label22').html('').html('重置失败!');
				$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox3();">确&nbsp;定</div>');
				$('#deleteMenuId1').show();
			}
		}
	});
}
//打开修改信息弹出框
function openUserDial(){
	var wg = document.getElementById("mask6"); 
	var w = document.body.scrollWidth; //网页正文全文宽 
	var h = document.body.scrollHeight; //网页正文全文高 
	wg.style.display = "block"; 
	wg.style.width = w + "px"; 
	wg.style.height = h + "px"; 
	var con = document.getElementById("popedzh");
	con.style.display = "block";
}
//关闭修改信息弹出框
function closeUserDialOrg(){
	var bg = document.getElementById("mask6"); 
	var con = document.getElementById("addNewOrgPerson"); 
	bg.style.display = "none"; 
	con.style.display = "none"; 
	resetBaseMsg2();
}
//关闭修改信息弹出框
function closeUserDial(){
	var bg = document.getElementById("mask6"); 
	var con = document.getElementById("popedzh"); 
	bg.style.display = "none"; 
	con.style.display = "none"; 
}


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

//显示tab页
function showTab(tabItem, content, cla ){
	$(content).each(function(i){ //遍历选项卡内容 显示第一个
        $(this).children().first().show();
    });
	var p = $(".tabHead");
	var n = p.next("div").children();
	n.hide();
	n.eq(1).show();
	window.parent.changeHeight();
}

function clearMM(){
	$('#mm0').hide();
	$('#mm1').hide();
	$('#mm2').hide();
}

//关闭对话框
function closeMsgbox() {
	$('#deleteMenuId1').hide();
	$('#maskItem').hide();
}
//关闭对话框
function closeMsgbox9() {
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var node = menu_param.currentNode;
	var pNode = node.getParentNode();
	$('#deleteMenuId1').hide();
	$('#maskItem').hide();
	//展开节点
//	treeObj.expandNode(node,true);
	treeObj.reAsyncChildNodes(pNode, "refresh", false);
	menu_param.treeObj.cancelSelectedNode();
	menu_param.treeObj.selectNode(node,true);
}
//删除确认
function closeMsgbox10() {
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var node = menu_param.currentNode;
	var pNode = node.getParentNode();
	treeObj.reAsyncChildNodes(pNode, "refresh", false);
	$('#deleteMenuId1').hide();
	$('#maskItem').hide();
}
//关闭对话框
function closeMsgbox3() {
	$('#deleteMenuId1').hide();
}
function closeMsgbox7() {
	$('#deleteMenuId1').hide();
	$('#maskItem2').hide();
}
//关闭对话框
function closeMsgbox4() {
	$('#deleteMenuId1').hide();
	$('#popedzh').hide();
	$('#mask6').hide();
}
//关闭对话框
function closeMsgbox14() {
	$('#deleteMenuId1').hide();
	$('#popedzh').hide();
	$('#mask6').hide();
	if(menu_param.currentNode==null){
		loadBaseMsgTable(1);
	}else{
		loadOrgPersonTable(1);
	}
}
//关闭对话框
function closeMsgbox8() {
	$('#deleteMenuId1').hide();
	$('#addNewOrgPerson').hide();
	$('#maskItem2').hide();
	$('#mask6').hide();
	closeUserDialOrg();
	loadOrgPersonTable(1);
	
}

//判断输入是否是一个整数
function isint(str){    
	//var result=str.match(/^(-|\+)?\d+$/);
	var result=str.match( /^\+?[1-9][0-9]*$/);
	if(result==null){
		return false;
	}else{
		return true;
	}
}
 function stopBubble(e) {  
     if (e && e.stopPropagation) {//非IE  
         e.stopPropagation();  
     }  
     else {//IE  
         window.event.cancelBubble = true;  
     }  
 }
function showDialog(msg) {
	$('#msgDialog').html(msg).show();
	setTimeout(function() {
		$('#msgDialog').hide().html('');
	}, 3000);
}

function openLoad(msg) {
	$('#loadDialog').show().find('span').html(msg);
}

function closeLoad() {
	$('#loadDialog').hide().find('span').html('');
}
