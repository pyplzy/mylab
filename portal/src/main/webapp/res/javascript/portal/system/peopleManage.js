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
			onClick : onTreeClick,
			onRightClick:onTreeRclick
		}
};


$(document).ready(function(){
	$("#orgEditButton").hide();
	
	$('#userType').easyDropDown({cutOff: 4});
	$.ajax({
		async: false,
		method:'POST',
		url:'menu_to_searchRootOrg.do',
		data:{
			orgId:orgIdItem,
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
	
	//添加事件  机构存在子机构，不可修改为叶子节点！
/*	$('input:radio[name=ynRadio]').click(function(){
		var node = menu_param.currentNode;
		if(menuUrl=='menu_to_updateOrg.do'&&node!=null){
			if(node.isLeaf==0){
				$.ajax({
					method:'POST',
					url:'menu_to_getOrgTree.do',
					data:{
						orgId:node.orgId
					},
					success:function(data){
						if(data.length>0){
							alert("该机构存在子机构，不可修改为叶子节点！");
							$("input[name='ynRadio'][value=0]").prop("checked",true);
						}
					}
				});
			}
		}
	});
	
	//添加事件  机构级别不允许修改
	$("#orgGradeDD").click(function(){
		var node = menu_param.currentNode;
		//修改且机构id不为空
		if(menuUrl=='menu_to_updateOrg.do'&&node!=null){
			alert("机构级别不可修改！");
			$("#orgGradeDT").click();
		}
	});
	*/
	
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
	
	if(optList.org_add==1){
		$("#orgEditButton").show();
	}else{
		$("#orgEditButton").hide();
	}
	window.parent.changeHeight();
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
		showMsgTips('行政机构不予删除!',1000);
		return;
	}
	$.ajax({
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
				showMsgTips('删除失败,该机构下存在子机构!',1000);
			}else if(data.users.length>0){
				showMsgTips('删除失败,该机构下存在用户!',1000);
			}else{
				var r = confirm('确定要删除该机构吗?');
				if(r){
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
								showMsgTips('删除成功!',1000);
								var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
								var node = menu_param.currentNode;
								var pNode = node.getParentNode();
								treeObj.reAsyncChildNodes(pNode, "refresh", false);
							}else{
								showMsgTips('删除失败!',1000);
							}
						}
					});
				}
			}
		}
	});
	
}
//机构信息保存按钮
function saveOrgBaseMsg(){
	var node = menu_param.currentNode;
	var isLeaf = $('input:radio[name=ynRadio]:checked').val();
	if(node==null){
		showMsgTips("请选择操作机构", 1000);
		return;
	}
	if(node.orgGrade<5 && menu_param.opet==0){
		showMsgTips("主机构数据不予修改!", 1000);
		return;
	}
	if($.trim($("#orgName").val())==''){
		showMsgTips("机构名称不能为空!", 1000);
		$("#orgName").focus();
		return;
	}
	if($.trim($("#orgCode").val())==''){
		showMsgTips("机构编码不能为空!", 1000);
		$("#displayOrder").focus();
		return;
	}
	if($.trim($("#displayOrder").val())==''){
		showMsgTips("显示顺序不能为空!", 1000);
		$("#displayOrder").focus();
		return;
	}
	if(!(isLeaf==0 || isLeaf==1 )){
		showMsgTips("请选择是否叶子节点!", 1000);
		return;
	}
	
	//机构级别不能高于其父机构
	if($("#orgGrade").val()<node.orgGrade){
		showMsgTips("机构级别不能高于其父机构!", 1000);
		return;
	}
	//行政级别不能高于其父机构
	if($("#administrativeGrade").val()<node.administrativeGrade){
		showMsgTips("行政级别不能高于其父机构!", 1000);
		return;
	}
	
	//新增时机构机构级别不能高于其父机构
	var orgId = null;  //机构id
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
			orgCode:$.trim($("#orgCode").val()),
			orgCodeSeq:node.orgCodeSeq,
			isLeaf:isLeaf,
			displayOrder:$.trim($("#displayOrder").val()),
			orgGrade:$("#orgGrade").val(),
			administrativeGrade:$("#administrativeGrade").val(),
			orgMemo:$.trim($("#orgMemo").val())
		},
		success:function(data){
			if(data==true){
				showMsgTips("保存成功!", 1000);
				var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
				var node = menu_param.currentNode;
				var pNode = node.getParentNode();
				//展开节点
				treeObj.reAsyncChildNodes(pNode, "refresh", false);
				menu_param.treeObj.cancelSelectedNode();
				menu_param.treeObj.selectNode(node,true);
				closeLoad();
			}else{
				showMsgTips("保存失败!", 1000);
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
	if(optList.org_edit==1){
		$("#orgEditButton").show();
	}else{
		$("#orgEditButton").hide();
	}
	window.parent.changeHeight();
	
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
		pNode!=null?$("#pOrgName").val(pNode.orgName):'';
	}
	//清空机构信息form
	$("#orgName").val(node.orgName);
	$("#orgSeq").val(node.orgIdSeq);
	$("#orgNameSeq").val(node.orgNameSeq);
	$("#displayOrder").val(node.displayOrder);
	$("#orgMemo").val(node.orgMemo);
	$("#orgCode").val(node.orgCode);
	$("#orgCode").attr("disabled",true);
	$("#orgGrade").easyDropDown('select',node.orgGrade+'');
	if(node.isLeaf==1){
		$("input[name='ynRadio'][value=1]").prop("checked",true);
	}else{
		$("input[name='ynRadio'][value=0]").prop("checked",true);
	}
	$("#administrativeGrade").easyDropDown('select',node.administrativeGrade+'');
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
					var status=item.status==0?"锁定":"解锁";
					if(item.gender==1){gend = '男';}
					if(item.gender==2){gend = '女';}
					if(item.gender==0){gend = '未知';}
					var type = item.type==2?"（业务归属）":"";
					var key = item.keyFlag==0? "非KEY用户":"有KEY用户";
					$("#orgPersonTable").append(
							'<tr staffId="'+item.id+'" userId="' + item.userId +'" orgId="' + item.orgId + '" type="' + item.type + '" >'+
							'	<td width="60"><a href="javascript:void(0)" onclick="showBaseMsg(this)">'+item.loginId +'</a></td>'+	
							'	<td width="60">'+item.userName+'</td>'+	
							'	<td width="20">'+gend+'</td>'+	
							'	<td width="130" title="'+item.orgNameSeq+'">'+item.orgNameSeq+type+'</td>'+
							'	<td width="60">'+key+'</td>'+
							'	<td width="130"><a href="javascript:void(0)" onclick="openRolePermission(this)">授予角色</a>'+'&nbsp;'+'<a href="javascript:void(0)" onclick="delectItem(this);">删除</a>'+'&nbsp;'+
							'<a href="javascript:void(0)" onclick="updateStatus(this);" id="'+item.status+'">'+status+'</a></td>'+
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
					var status=item.status==0?"锁定":"解锁";
					if(item.gender==1){gend = '男';}
					if(item.gender==2){gend = '女';}
					if(item.gender==0){gend = '未知';}
					var type = item.type==2?"（业务归属）":"";
					$("#baseMsgTable").append(
							'<tr staffId="'+item.id+'" userId="' + item.userId +'" orgId="' + item.orgId + '" type="' + item.type + '" >'+
							'	<td width="60"><a href="javascript:void(0)" onclick="showBaseMsg(this)">'+item.loginId +'</a></td>'+	
							'	<td width="60">'+item.userName+'</td>'+	
							'	<td width="20">'+gend+'</td>'+	
							'	<td width="130" title="'+item.orgNameSeq+'">'+item.orgNameSeq+type+'</td>'+	
							'	<td width="110"><a href="javascript:void(0)" onclick="openRolePermission(this)">授予角色</a>'+'&nbsp;'+
							'<a href="javascript:void(0)" onclick="delectItem(this);">删除</a>'+'&nbsp;'+
							'<a href="javascript:void(0)" onclick="updateStatus(this);" id="'+item.status+'">'+status+'</a></td>'+
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
	var id = $(obj).parents("tr").attr("staffId");
	var userId = $(obj).parents("tr").attr("userId");
	var type = $(obj).parents("tr").attr("type");
	var orgId = $(obj).parents("tr").attr("orgId");
	if(window.confirm('是否确认删除？')){
			$.ajax({
				method: 'POST',
				url: 'menu_to_removeOrgUser.do',
				data:{
					id:id,
					userId:userId,
					orgId:orgId,
					type:type
				},
				success : function(data) {
					if(data==true) {
						$(obj).parents('tr').remove();
						showMsgTips('成功删除改机构人员',1000);
					}
				}
			});
		}
}

//机构人员锁定
function updateStatus(obj){
	var loginId= $(obj).parents("tr").find("td").eq(0).text();
	var updateStatus=$(obj).attr("id")=="0"?"2":"0";
	if(window.confirm('是否确认'+$(obj).text()+'？')){
		$.ajax({
			method:'POST',
			url:'menu_to_updateUserStatus.do',
			data:{
				loginId:loginId,
				status:updateStatus
			},
			success:function(data){
				if(data==true){
					showMsgTips('成功'+$(obj).text()+'机构人员',1000);
					/*if($(obj).attr("id")=="0"){
						$(obj).text("解锁");
						$(obj).attr("id","2");
					}else{
						$(obj).text("锁定");
						$(obj).attr("id","0");
					}*/
					loadOrgPersonTable(1);
					loadBaseMsgTable(1);
				}
			}
		})
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
function addOrgPersonal() {
	var node = menu_param.currentNode;
	if (node == null) {
		showMsgTips('请选择机构!', 1000);
		return;
	}
	parent.peopleMangeDialog.openAddOrgPersonDialog(node);
}
//授予角色弹窗
function openRolePermission(obj) { 
	var userId = $(obj).parents('tr').attr('userId');//工号
	var staffId = $(obj).parents('tr').attr('staffId');//工号
	parent.peopleMangeDialog.openRolePermissionDialog(userId,staffId);
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

//选中加背景色
function clickOnetr(obj, e){
	$(obj).toggleClass("backset");
}

//用户授权查询
function searchRoleMsg(){
	loadRoleTable(appNameValue,1);
}

//个人基本信息弹框
function showBaseMsg(obj){
	var userId = $(obj).parents('tr').attr('userId');//工号
	parent.peopleMangeDialog.openUserDialog(userId);
}

//清空机构信息表
function orgFormReset(){
	$("#orgName").val('');
	$("#orgSeq").val('');
	$("#orgCode").val('');
	$("#orgCode").attr("disabled",false);
	$("#orgNameSeq").val('');
	$("#pOrgName").val('');
	$("#orgMemo").val('');
	$("#displayOrder").val('');
	var ynRadio = document.getElementsByName("ynRadio");
	for(var i=0; i<ynRadio.length; i++){
		ynRadio[i].checked = false;
	}
	$("#orgGrade").easyDropDown('select', 0);
	$("#administrativeGrade").easyDropDown('select', 0);
}

//检查orgCode是否重复
function checkOrgCode(){
	var orgCode = $.trim($("#orgCode").val());
	if(orgCode==null||orgCode==''){
		showMsgTips("机构编码不可用",1000);
		$("#orgCode").focus();
		return;
	}
	$.ajax({
		type:'post',
		url:'menu_to_checkOrgCode.do',
		date:{
			orgCode:orgCode
		},
		success:function(data){
			if(data=='use'){
				showMsgTips("机构编码不可用",1000);
				$("#orgCode").focus();
			}
		},
		errer:function(){
			showMsgTips("机构编码不可用",1000);
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


function openLoad(msg) {
	$('#loadDialog').show().find('span').html(msg);
}

function closeLoad() {
	$('#loadDialog').hide().find('span').html('');
}
//校验提示
function showMsgTips(msg,time) {
	$(".msgTips").html(msg);
	$(".msgTips").show();
	setTimeout(function() {
		$('.msgTips').hide();
	}, time);
}