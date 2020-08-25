var userOrgDialog=(function(){
    
	var orgIdForUserOrg=null;
	var orgNameForUserOrg=null;
	var userNameForUserOrg=null;
	var userIdForUserOrg=null;
	var typeForUserOrg=null;
	var manageOrgNameForOrgUser=null;
	var settingForUserOrg=null;
	
	//添加管理区域弹窗弹窗
	var showAddUserOrg=function(userId1,userName1,orgId1,orgName1,type,manageOrgName) { 
	 	orgIdForUserOrg=null;
	    orgNameForUserOrg=null;
	    userNameForUserOrg=null;
	    userIdForUserOrg=null;
	    typeForUserOrg=null;
	    settingForUserOrg=null;
	 	orgIdForUserOrg=orgId1;
	  	userNameForUserOrg=userName1;
	 	userIdForUserOrg=userId1;
	 	orgNameForUserOrg=orgName1;
	 	typeForUserOrg=type;
	 	manageOrgNameForOrgUser=manageOrgName;

	 	//设置机构树的settings
	 	settingForUserOrg={
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
					onClick : onTreeClickForUserOrg
				}
		};
		//点击屏幕其他地方机构树弹窗收起
		$(document).mouseup(function(e){
			if(e.which!=3){
			 	var _con = $("#editUserOrgTreeWrap");   // 设置目标区域
				if(!_con.is(e.target) && _con.has(e.target).length === 0){ // Mark 1
			 	 	$("#editUserOrgTreeWrap").slideUp();	
				 }
			}
		});
	 	$('#editUserOrgUserId').val(userNameForUserOrg);
	 	if(type==1){
	 		$('#editUserOrgOrgName2').val(manageOrgNameForOrgUser);
	 		window.document .getElementById ('editUserOrgTitleSpan').innerHTML='修改管理区域' ;
	 	}else{
	 		window.document .getElementById ('editUserOrgTitleSpan').innerHTML='添加管理区域' ;
	 	}
	 	var bg = document.getElementById("userAddMask"); 
	 	var con = document.getElementById("addUserOrg"); 
	 	var w = document.body.scrollWidth; //网页正文全文宽 
	 	var h = document.body.scrollHeight; //网页正文全文高 
	 	bg.style.display = "block"; 
	 	bg.style.width = w + "px"; 
	 	bg.style.height = h + "px"; 
	 	con.style.display = "block"; 
	 }
	
	 // 获取树
	var treeForUserOrg=function(){
		var data={'orgId':orgIdForUserOrg,'pOrgId':-1,'orgName':orgNameForUserOrg,'isLeaf':0,'isParent':true,'open':false};
		$.fn.zTree.init($('#editUserOrgTreeDemo'),settingForUserOrg,data);
		$("#editUserOrgTreeWrap").show();
	}
	
	//鼠标点击事件 
	var onTreeClickForUserOrg=function(e,treeId,treeNode){
		$("#editUserOrgOrgId2").val(treeNode.orgId);
		$("#editUserOrgOrgName2").val(treeNode.orgName);
		$("#editUserOrgTreeWrap").hide();
	}
	
	/*关闭*/ 
	var closeAddUserOrg=function() { 
		orgIdForUserOrg=null;
	    orgNameForUserOrg=null;
	    userNameForUserOrg=null;
	    userIdForUserOrg=null;
	    typeForUserOrg=null;
	    settingForUserOrg=null;
	 	$('#editUserOrgUserId').val('');
	 	$('#editUserOrgOrgName2').val('');
	 	$('#editUserOrgOrgId2').val('');
	 	$("#editUserOrgTreeWrap").hide();
	 	var bg = document.getElementById("userAddMask"); 
		var con = document.getElementById("addUserOrg"); 
		bg.style.display = "none"; 
		con.style.display = "none"; 
	}
	//新增，修改提交
	var editUserOrg=function(){
		var orgIdForSub=$('#editUserOrgOrgId2').val();
		if(orgIdForSub=='' || null==orgIdForSub){
			showDialogForUserOrg2('请先选择机构！');
			return;
		}
		$.ajax( {
				type:'POST',
				url:ctx+"/peopleOrgData/menu_toOrg_editUserOrg.do",
				data:{
					'orgId':orgIdForSub,
					'userId':userIdForUserOrg,
					'type':typeForUserOrg
				},
				success:function(msg) {
					if(msg=='success') {
						showDialogForUserOrg('操作成功！');
						$('.rightWrap').find('iframe').get(0).contentWindow.queryUsersPOrgUser();
						
					} else {
						showDialogForUserOrg2('操作失败！');
					}
				}
			});
	}
	var showDialogForUserOrg=function(msg) {
		$('#msgDialogForUserOrg').html(msg).show();
		setTimeout(function() {
			$('#msgDialogForUserOrg').html(msg).hide();
			closeAddUserOrg();
		}, 2000);	
	}
	var showDialogForUserOrg2=function(msg) {
		$('#msgDialogForUserOrg').html(msg).show();
		setTimeout(function() {
			$('#msgDialogForUserOrg').html(msg).hide();
		}, 2000);	
	}

	return {
		showAddUserOrg:showAddUserOrg,
		closeAddUserOrg:closeAddUserOrg,
		treeForUserOrg:treeForUserOrg,
		editUserOrg:editUserOrg
	}

})();