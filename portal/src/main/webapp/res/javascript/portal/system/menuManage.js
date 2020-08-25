
var appNameTitle = null;
var appNameValue = null;
var menuUrl = null;

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
		edit: {
			enable: true
		},
		data : {
			simpleData : {
				enable : true, 
				idKey: "menuId",
				pIdKey: "pMenuId"
		},
		key: {
			name: 'menuName'
		}
	},
	callback :{
		onClick : onTreeClick,
		onRightClick:onTreeRclick
	}
};
		
$(document).ready(function(){
	var $selects = $('#dropdownValue');
	$selects.easyDropDown({
		cutOff: 6,
		onChange: function(selected){
			appNameTitle = selected.title;
			appNameValue = selected.value;
			//加载菜单树
			loadMenuTree();
			//清空表格
			menuFormReset();
			if(appNameValue==''){
				$("#titleName").html('');
			}else{
				$("#titleName").html('<span>'+appNameTitle+'</span>');
			}
		}
	});
	
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
});

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
		url : 'menu_mainPage_getAllMenu.do',
		data:{
			appCode:appNameValue
		},
		success : function(data) {
			var root = {'menuId':0, 'pMenuId':null, 'menuName':appNameTitle,'parentName':appNameTitle,'menuLevel':0,'isLeaf':0,'isParent':true,'open':true};
			data.push(root);
			menu_param.treeObj = $.fn.zTree.init($("#treeDemo"), setting, data);
		}
	});
}

//刷新
function reload(){
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var node = menu_param.currentNode;
	//更新当前节点
	treeObj.updateNode(node,false);
	//展开节点
	treeObj.expandNode(node,true);
	loadMenuForm();
}
//增加子菜单
function append(){
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var node = menu_param.currentNode;
	menuUrl = 'menu_mainPage_insertMenu.do';
	menu_param.opet = 1;
	//展开节点
	treeObj.expandNode(node,true);
	//清空表
	menuFormReset();
	$("#titleName").html('<span>'+appNameTitle+'</span>'+'-->'+'<span>'+node.menuName+'-->增加菜单'+'</span>');
	$("#pMenuName").val(node.menuName);
	$("#appName").val(appNameTitle);
//	$("input[name='ynRadio'][value=1]").prop("checked",true);
}
//删除
function removeit(){
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var node = menu_param.currentNode;
	if(node.children != null){
		showDialogForMenu('删除失败,该菜单下存在子菜单!');
	}else{
		var r=confirm("确定删除该菜单？");
		if(r){
			deleteMEnu();
		}
	}

}

//右击节点事件处理
function onTreeRclick(e,treeId,treeNode){
//	/***重置表单和临时参数***/
	menu_param.reset();
//	/***清除其他节点的选中状态***/
	menu_param.treeObj.cancelSelectedNode();
	menu_param.treeObj.selectNode(treeNode);
	menu_param.currentNode = treeNode;
	/***显示菜单***/
	clearMM();
	if(treeNode.pMenuId == null){
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

//ztree左键点击节点事件处理
function onTreeClick(e,treeId,treeNode){
	menu_param.currentNode = treeNode;
	loadMenuForm();
}

function loadMenuForm(){
	var node = menu_param.currentNode;
	//根节点是虚结点，不能保存
	if(node.menuId == 0){
		node.parentName = '';
	}else{
		node.parentName = node.getParentNode().menuName;
	}
	menuUrl = 'menu_mainPage_updateMenu.do';
	menu_param.opet = 0 ;     
	menuFormReset();
	$("#titleName").html('<span>'+appNameTitle+'</span>'+'-->'+'<span>'+node.menuName+'</span>');
	$("#menuName").val(node.menuName);
	$("#pMenuName").val(node.parentName);
	$("#displayOrder").val(node.displayOrder);
	$("#appName").val(appNameTitle);
	if(node.isLeaf==1){
		document.getElementById("menuAction").disabled=false;  
		$("input[name='ynRadio'][value=1]").prop("checked",true);
	}
	if(node.isLeaf==0){
		//不可编辑状态
		document.getElementById("menuAction").disabled=true;  
		$("input[name='ynRadio'][value=0]").prop("checked",true);
	}
	
	$("#menuAction").val(node.menuAction);
	$("#menuMemo").val(node.menuMemo);
	if(node.menuId == 0){
		menuFormReset();
		$("#titleName").html('<span>'+appNameTitle+'</span>');
	}
}

function menuFormReset(){
	$("#menuName").val('');
	$("#pMenuName").val('');
	$("#displayOrder").val('');
	if(appNameValue==''){
		$("#appName").val('');
	}else{
		$("#appName").val(appNameTitle);
	}
	var ynRadio = document.getElementsByName("ynRadio");
	for(var i=0; i<ynRadio.length; i++){
		ynRadio[i].checked = false;
	}
	$("#menuAction").val('');
	$("#menuMemo").val('');
	$("#titleName").html('');
}

//关闭删除菜单对话框并刷新树刷新表
function closeMsgbox2() {
	loadMenuTree();
	menuFormReset();
}

//删除子菜单保存
function deleteMEnu(){
		$.ajax({
			async : true,
			cache : false,
			method:'POST',
			url : 'menu_mainPage_deleteLeafMenu.do',
			data:{
				appCode:appNameValue,
				menuId:menu_param.currentNode.menuId
			},
			success : function(data) {
				if(data){
					closeMsgbox2();
					showDialogForMenu('删除成功!');
				}else{
					showDialogForMenu('删除失败!');
				}
			}
		})
}

//是否叶子节点切换
function checkAction(){
var chkObjs = document.getElementsByName("ynRadio");
    for(var i=0;i<chkObjs.length;i++){
        if(chkObjs[i].checked){
        	var ret =chkObjs[i].value;
        	if(ret == '0'){
        		document.getElementById('menuAction').disabled = true; 
        		document.getElementById('menuAction').value='';
        	}else{
        		document.getElementById('menuAction').disabled = false;
        	}
            break;
        }
    }
}

function saveMenuEdit(){
	var node = menu_param.currentNode;
	var isLeaf = $('input:radio[name=ynRadio]:checked').val();
	if(node==null){
		showDialogForMenu('请选择操作菜单!');
		return;
	}
//	console.info(menu_param.opet);
	if(node.menuId==0 && menu_param.opet==0){
		showDialogForMenu('根节点不能进行操作!');
		return;
	}
	if($.trim($("#menuName").val())==''){
		showDialogForMenu('菜单名称不能为空!');
		return;
	}
	if(isLeaf == 1){
		var menuUrlAc = $.trim($("#menuAction").val());
		if(menuUrlAc==''){
			showDialogForMenu('菜单动作不能为空!');
			return;
		}
		
		if(menuUrlAc.indexOf("/") == -1){
			showDialogForMenu('菜单动作要以"/"开头!');
			return;
		}
	}
	if($.trim($("#displayOrder").val())==''){
		showDialogForMenu('显示顺序为空!');
		return;
	}
	var menuId = null;  //菜单id
	var menuLevel = null;  //菜单级别
	var pMenuId = null;  //父菜单名称
	//修改
	if(menu_param.opet == 0){
		menuId = node.menuId;
		menuLevel = node.menuLevel;
		pMenuId = node.pMenuId;
	}
	//新增
	if(menu_param.opet == 1){
		menuId = -2;
		menuLevel = node.menuLevel + 1;
		pMenuId = node.menuId;
	}
	$.ajax({
		method:'POST',
		url:menuUrl,
		data:{
			menuId:menuId,
			appCode:appNameValue,
			menuName:$.trim($("#menuName").val()),
			menuLevel:menuLevel,
			menuAction:$.trim($("#menuAction").val()),
			pMenuId:pMenuId,
			displayOrder:$.trim($("#displayOrder").val()),
			delFlag:0,
			menuMemo:$.trim($("#menuMemo").val()),
			menuSeq:node.menuSeq,
			isLeaf:isLeaf
		},
		success:function(data){
			if(data){
				closeMsgbox2();
				showDialogForMenu('保存成功!');
			}else{
				showDialogForMenu('保存失败!');
			}
		}
	})
}

function resetMenuEdit(){
	if(menu_param.opet == 1){
		$("#menuName").val('');
		$("#displayOrder").val('');
		var ynRadio = document.getElementsByName("ynRadio");
		for(var i=0; i<ynRadio.length; i++){
			ynRadio[i].checked = false;
		}
		$("#menuAction").val('');
		$("#menuMemo").val('');
	}
}

function clearMM(){
	var a1 = document.getElementById("mm0");
	var a2 = document.getElementById("mm1");
	var a3 = document.getElementById("mm2");
	a1.style.display = "none"; 
	a2.style.display = "none"; 
	a3.style.display = "none"; 
}

function showDialogForMenu(msg) {
	$('#msgDialogForMenu').html(msg).show();
	setTimeout(function() {
		$('#msgDialogForMenu').hide().html('');
	}, 1000);
}




