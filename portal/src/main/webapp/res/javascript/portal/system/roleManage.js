//应用code
var appNameValue = null;
var appNameTitle = null;
//省份
var provinceId = null;
var provinceName = null;

//服务于check()方法
var rows1=0;
var setting = {
	edit : {
		enable : true
	},
	data : {
		simpleData : {
			enable : true,
			idKey : "menuId",
			pIdKey : "pMenuId"
		},
		key : {
			name : 'menuName'
		}
	},
	check : {
		enable : true,
		chkStyle : "checkbox",
		chkboxType : {
			"Y" : "ps",
			"N" : "ps"
		}
	}
};
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
$(document).ready(function(){
	//初始化下拉列表
	var $selects = $('#dropdownValue');
	$selects.easyDropDown({
		cutOff: 6,
		onChange: function(selected){
			if(selected.value=='portal'){
				$("#province").easyDropDown('destroy');
				$("#province").empty().append('<option value="100000" >系统级别</option>');
	            $("#province").easyDropDown({});
			}else{
				$.ajax({
					async : false,
					cache : true,
					type : 'post',
					url : ctx+'/roleManage/menu_roleMain_queryProvinces.do',
					success : function(data) {
						if(data){
							var str2="";
							$.each(data,function(i,v){
								str2+='<option value="'+v.orgId+'">'+v.orgName+'</option>';
							});
							$("#province").easyDropDown('destroy');
							$("#province").empty().append(str2);
			                $("#province").easyDropDown({
			             		cutOff: 6,
			             		onChange: function(selected){
			            			provinceId= selected.value;
			            			provinceName= selected.title;
			            			queryRole(1);
			            			loadMenuTree();
			            		}
			             	});
						}
					}
				});
			}
			appNameValue = selected.value;
			appNameTitle=selected.title;
			queryRole(1);
			loadMenuTree();
			//加载资源下拉框
			loadResourcesList(appNameValue);
			$("#optsTable").empty();
		}
	});
	//选择省份
	$('#province').easyDropDown({});
	$('#dropdownValue').easyDropDown('select',0);
});

//加载资源下拉框
function loadResourcesList(appCode){
	$.ajax({
		async : false,
		cache : true,
		type : 'post',
		url : ctx+'/optManage/menu_toOpt_queryResources.do',
		data :{appCode:appCode},
		success : function(data) {
			if(data){
				var str2 = '';
				$.each(data,function(i,v){
					str2+='<option value="'+v.resourceId+'">'+v.resourceName+'</option>';
				});
				str2 = '<option value="-1">---请选择---</option>'+str2;
				$("#resourceId").easyDropDown('destroy');
				$("#resourceId").empty().append(str2);
                $("#resourceId").easyDropDown({
             		cutOff: 6,
             		onChange: function(selected){
             			queryOpt(1);
            		}
             	});
			}
		}
	});
}

//查询角色列表
function queryRole(page){
//	var roleName=$("#roleName").val();
	var province = $("#province").val();
	if(page==""){
		page=1;
	}
	$.ajax({
		async : false,
		cache : true,
		type : 'post',
		dataType : 'json',
		url : 'menu_roleMain_getRoles.do',
		data:{
			'userId':userId,
			'appCode':appNameValue,
			'province':province,
			'rows':5,
			'page':page
		},
		success : function(data) {
			if($("#first").is(':visible')){
//				loadMenuTree();   重复加载菜单 注释掉
			}else{
				$("#users").empty();
				$("#red2").empty();
			}
			//角色列表拼接字段
			var str="";
			if(data){
				$("#roles").html('');
				$.each(data.roles,function(i,v){
					str+=
						'<tr onclick="chk(this);" roleId='+v.roleId+'>'+
			                '<td width="5" ></td>'+
			                '<td width="55" title='+v.roleCode+'>'+v.roleCode+'</td>'+
			                '<td width="65" title='+v.roleName+'>'+v.roleName+'</td>'+
			                '<td width="30" title='+v.orgName+'>'+v.orgName+'</td>'+
			                '<td width="100" title='+v.roleMemo+'>'+v.roleMemo+'</td>'+
			              '</tr>';
				});
				$("#roles").append(str);
				//角色列表分页
				$('#red1').ossPaginator({
					totalrecords: data.pageObj.total, 
					recordsperpage: 5, 
					length: 4, 
					next: '下一页', 
					prev: '上一页', 
					first: '首页', 
					last: '尾页', 
					initval: page,//初始化哪一页被选中
//					display: 'single',
					controlsalways: true,
					onchange: function (newPage)
					{
						$("#pageC").val(newPage);
						queryRole(newPage);
			    	}
				});
			}
		}
	});
	window.parent.changeHeight();
}

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
			
			if($("#first").is(':visible')){
				getMyMenu();
			}else if($("#second").is(':visible')){
				queryBy();
			}else if($("#third").is(':visible')){
				queryOpt(1);
			}
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
	
});

//加载菜单树
function loadMenuTree(){
	if(appNameValue==''){
		$("#treeDemo").html('');
		return;
	}
	$.ajax({
		async : true,
		cache : false,
		type : 'post',
		dataType : 'json',
		url : ctx+'/menuManage/menu_mainPage_getAllMenu.do',
		data:{
			appCode:appNameValue
		},
		success : function(data) {
			var root = {'menuId':0, 'pMenuId':null, 'menuName':appNameTitle,'parentName':appNameTitle,'menuLevel':0,'isLeaf':0,'isParent':true,'open':true};
			data.push(root);
			menu_param.treeObj = $.fn.zTree.init($("#treeDemo"), setting, data);
			
			//重新调整iframe高度，防止分页不显示（防止第二个选项卡内容较多切换为第一个选项卡，翻页时内容较少，再点击第二个选项卡导致翻页消失）
			window.parent.changeHeight();
		}
	});
}

function showAddOrEditDialog(type){
	if(type==1){
		parent.roleDialog.showDiv1();
	}
	if(type==2){
		var roleId = $("#roles .activebg").attr('roleId');
		if(roleId == undefined){
			showMsgTips("请选择一个角色",1000);
			return;
		}
		parent.roleDialog.showDiv2(roleId);
	}
}

//删除角色
function delConfirm(){
	var roleId = $("#roles .activebg").attr('roleId');
	if(roleId == undefined){
		showMsgTips("请选择一个角色",1000);
		return;
	}
	var r = confirm("确定要删除该角色吗?");
	if(r){
		$.ajax({
			async : true,
			cache : false,
			type : 'post',
			url : ctx+'/roleManage/menu_roleMain_delRole.do',
			data:{
				'appCode':appNameValue,
				'roleId':roleId
			},
			success : function(data) {
				if(data=="succ"){
					showMsgTips('删除角色成功',1000);
					queryRole(1);
				}else{
					showMsgTips('删除角色失败',1000);
				}
			}
		});
	}
}

function queryOpt (page){
	var roleId = $("#roles .activebg").attr('roleId');
	if(roleId == undefined){
		showMsgTips("请选择一个角色",1000);
		return;
	}
	$.ajax({
		type: 'POST',
		url: 'menu_roleMain_qryOptsByRoleId.do',
		dataType : 'json',
		data:{  
			'page':page,
			'rows':5,
			'resourceId':$("#resourceId").val(),
			'roleId':roleId,
			'appCode':appNameValue,
			'optName':$.trim($("#optName").val())
			
		},
		success : function(data) {
			if(data){
				var str = '';
				$.each(data.optList,function(i,v) {
					var buttonKey = '';
					if(v.isGranted==1){
						buttonKey = '<td width="100" isGranted="'+v.isGranted+'"><a href="javascript:void(0)" onclick="changeLine(this, event);" class="onyes1"></a></td>';  
					}else{
						buttonKey = '<td width="100" isGranted="'+v.isGranted+'"><a href="javascript:void(0)" onclick="changeLine(this, event);" class="onyes1 offno1"></a></td>';  
					}
					str+="<tr optId="+v.privilegeId+">"+
	                    "<td width='70'>"+v.resourceName+"</td>"+
	                    "<td width='70'>"+v.privilegeCode+"</td>"+
	                    "<td width='70' title='"+v.privilegeName+"'>"+v.privilegeName+"</td>"+
	                    buttonKey+
	                    "</tr>"
						;
				});
				$("#optsTable").empty().append(str);
				
				$('#red3').ossPaginator({
					totalrecords: data.pageObj.total, 
					recordsperpage: 5, 
					length: 4, 
					next: '下一页', 
					prev: '上一页', 
					first: '首页', 
					last: '尾页', 
					initval: page,//初始化哪一页被选中
					controlsalways: true,
					onchange: function (newPage){
						queryOpt(newPage);
			    	}
				});
			}
		}
	});
}

//是否授权切换
function changeLine(obj, e){
	$(obj).toggleClass("offno1");
	var roleId = $("#roles .activebg").attr('roleId');
	if(roleId == undefined){
		showMsgTips("请选择一个角色",1000);
		return;
	}
	var isGranted =$(obj).parents('td').attr("isGranted");
	var optId =$(obj).parents('tr').attr("optId");
	var backset = $(obj).hasClass("offno1");
	var type = 0;  //0不操作 1取消2添加
	if(isGranted==1 && backset==true){
		type = 1;
	}
	if(isGranted==0 && backset==false){
		type = 2;
	}
	$.ajax({
		method:'POST',
		url:'menu_roleMain_delOrGiveRoleOpt.do',
		data:{
			optId:optId,
			roleId:roleId,
			type:type
		},
		success:function(data){
			if(data=="succ"){
				queryOpt(1);
				showMsgTips('修改权限操作成功',1000);
			}else{
				showMsgTips('修改权限操作失败',1000);
			}
		}
	});
}

//点击单选按钮时，根据选项卡的显示状态判断查询内容
function judgeTab(){
	if($("#first").is(':visible')){
		getMyMenu();
	}else if($("#second").is(':visible')){
		queryBy();
	}else if($("#third").is(':visible')){
		queryOpt(1);
	}
}

//点击每行角色选中单选按钮
function chk(obj){
	//选中管理员时，添加样式
	$(obj).removeClass().addClass("activebg");
	$(obj).siblings().removeClass();
	$(obj).find(":input").prop("checked",true);
	judgeTab();
}

//获取某角色的菜单
function getMyMenu(){
	var roleId = $("#roles .activebg").attr('roleId');
	if(roleId == undefined){
		showMsgTips("请选择一个角色",1000);
		return;
	}
	$.ajax({
		async : true,
		cache : false,
		type : 'post',
		dataType : 'json',
		url : 'menu_roleMain_getMenus.do',
		data:{
			'appCode':appNameValue,
			'roleId':roleId
		},
		success : function(data) {
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			treeObj.checkAllNodes(false);
			if(data){
				for(var i = 0, n = data.length; i < n; i++) {
					var menuId=data[i].menuId;
					var node = treeObj.getNodeByParam("menuId", menuId, null);
					treeObj.checkNode(node, true, false);
				}
			}
		}
	});
}

//清除选中的菜单
function clearMenu(){
	getMyMenu();
}

//保存某角色的菜单
function saveMenu(){
	var roleId = $("#roles .activebg").attr('roleId');
	if(roleId == undefined){
		showMsgTips("请选择一个角色",1000);
		return;
	}
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var nodes = treeObj.getCheckedNodes(true);
	var str=roleId+";";
	for (var i=0, l=nodes.length; i < l; i++) {
		str+=nodes[i].menuId+":";
	}
	$.ajax({
		async : true,
		cache : false,
		type : 'post',
		url : 'menu_roleMain_saveMenus.do',
		data:{
			'str':str
		},
		success : function(data) {
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			treeObj.checkAllNodes(false);
			if(data==true){
				getMyMenu();
				showMsgTips('保存菜单成功',1000);
			}else{
				showMsgTips('保存菜单失败',1000);
			}
		}
	});
}

//点按钮查询角色下的成员
function queryBy(){
	queryUser(1,5);
}

//查询角色下的成员
function queryUser(page,rows){
	var roleId = $("#roles .activebg").attr('roleId');
	if(roleId == undefined){
		showMsgTips("请选择一个角色",1000);
		return;
	}
	var userId=$("#userId").val();
	var userName=$("#userName").val();
	var str="";
	$.ajax({
		type: 'POST',
		url: 'menu_roleMain_getUsers.do',
		dataType : 'json',
		async: false,
		data:{  
			'page':page,
			'rows':rows,
			'appCode':appNameValue,
			'roleId':roleId,
			'userId':userId,
			'userName':userName
		},
		success : function(data) {
			if(data){
				$('#users').empty();
				rows1=data.userInfo4Sessions.length;
				$.each(data.userInfo4Sessions,function(i,v) {
					str+=
					"<tr onclick='check(this);'>"+
                    "<td width='15'><input type='checkBox' onclick='chk2(this,event);'  name='userRadio'  value="+v.id+"  /></td>"+
                    "<td width='70'>"+v.userId+"</td>"+
                    "<td width='70'>"+v.userName+"</td>"+
                    "<td width='100' title='"+v.orgNameSeq+"'>"+v.orgNameSeq+"</td>"+
                    "</tr>"
					;
				});
				$("#users").append(str);
				$('#red2').ossPaginator({
					totalrecords: data.pageObj.total, 
					recordsperpage: 5, 
					length: 4, 
					next: '下一页', 
					prev: '上一页', 
					first: '首页', 
					last: '尾页', 
					initval: page,//初始化哪一页被选中
					controlsalways: true,
					onchange: function (newPage){
						 queryUser(newPage,5);
			    	}
				});
			}
		}
	});
}

//角色添加人员
function addUsers(){
	var roleId = $("#roles .activebg").attr('roleId');
	if(roleId == undefined){
		showMsgTips("请选择一个角色",1000);
		return;
	}
	var name = $("#roles .activebg td:eq(2)").text();
	parent.roleDialog.showDiv8(roleId,name);
}

//删除角色下人员的提示
function delConfirm2(){
	var roleId = $("#roles .activebg").attr('roleId');
	if(roleId == undefined){
		showMsgTips("请选择一个角色",1000);
		return;
	}
	var str ='';
	$("#users").find('input[name="userRadio"]:checked').each(function(){
		str+=$(this).val()+":";
	});
	if(str.length==0){
		showMsgTips("请至少选择一个成员",1000);
		return;
	}
	str=roleId+";"+str;
	var r = confirm("确定要删除吗?");
	if(r){
		$.ajax({
			async : true,
			cache : false,
			type : 'post',
			dataType : 'json',
			url : ctx+'/roleManage/menu_roleMain_delUsers2.do',
			data:{
				'str':str
			},
			success : function(data) {
				if(data==true){
					$("#all1").prop("checked",false);
					queryUser(1,5);
					showMsgTips('删除成员成功',1000);
				}else{
					showMsgTips('删除成员失败',1000);
				}
			}
		});
	}
}

//点击该按钮全选，或取消全选
function allC(){
	if($("#all1").prop("checked")){
		$("input[name='userRadio']").prop('checked',true);
	}else {
		$("input[name='userRadio']").prop('checked',false);
	}
}

//点击checkBox时触发
function check(obj){
	if($(obj).find(":input").prop("checked")){
		$(obj).find(":input").prop("checked",false);
	}else{
		$(obj).find(":input").prop("checked",true);
	}
	if($("#all1").prop("checked")){
		$("#all1").prop('checked',false);
	}
	var num=$("input[name='userRadio']:checked").length;
	if(rows1==num){
		$("#all1").prop('checked',true);
	}
}
//点击checkBox时触发
function chk2(obj,e){
	if($("#all1").prop("checked")){
		$("#all1").prop('checked',false);
	}
	var num=$("input[name='userRadio']:checked").length;
	if(rows1==num){
		$("#all1").prop('checked',true);
	}
	//阻止冒泡事件  
	stopBubble(e);
}
function stopBubble(e) {  
    if (e && e.stopPropagation) {//非IE  
        e.stopPropagation();  
    }  
    else {//IE  
        window.event.cancelBubble = true;  
    }  
}



