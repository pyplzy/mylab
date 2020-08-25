//查询人员：根据机构id和登录名
function queryUsersPOrgUser(){
	getUsers2POrgUser(1,5);
}
//根据角色ID查询出的未添加进该角色的员工,用于选择添加
function getUsers2POrgUser(page,rows){
	 showDialog('数据查询中。。。');
	var orgId2=$("#orgId2POrgUser").val();
	var userName=$("#treeuserId2POrgUser").val();
	$.ajax({
		type: 'POST',
		url: '../peopleOrgData/menu_toOrg_getUsersForNow.do',
		dataType : 'json',
		async: false,
		data:{
			'orgId':orgId2==''?orgId:orgId2,
			'userName':userName,
			'page':page,
			'rows':rows
		},
		success : function(data) {
			//隐藏提示框
			hideTip();
			$('#users').empty();
			var str="";
			if(data.peo.length>0) {
				$.each(data.peo,function(i,v) {
					str+=
				 	'<tr >'+
		            '<td width="30">'+v.userId+'</td>'+
		            '<td width="30">'+v.userName+'</td>'+
		            '<td width="50">'+v.orgName+'</td>'+
		            '<td width="50">'+v.manageOrgName+'</td>'+
		            '<td width="80" >';
		            if(v.isAdd==null || v.isAdd=='' ||v.isAdd=='null'){
		            	str+='<a href="javascript:void(0)" onclick="givePermission(this)" userId='+v.userId+' orgId='+v.orgId+' userName='+v.userName+' isAdd='+'"" ' +'  orgName='+v.orgName+' manageOrgName='+v.manageOrgName+'>添加管理机构</a>';
		            }else{
		            	str+='<a href="javascript:void(0)" onclick="givePermission(this)" userId='+v.userId+' orgId='+v.orgId+' userName='+v.userName+' isAdd='+v.isAdd+' orgName='+v.orgName+' manageOrgId='+v.manageOrgId+' manageOrgName='+v.manageOrgName+'>修改管理机构</a>';
		            	str+='&nbsp;&nbsp;&nbsp;&nbsp;';
		            	str+='<a href="javascript:void(0)" onclick="deleteItem(this)" userId='+v.userId+' orgId='+v.orgId+' userName='+v.userName+' isAdd='+v.isAdd+' orgName='+v.orgName+'+manageOrgId='+v.manageOrgId+'>删除管理机构</a>';
		            	
		            }
		            
		            str+='</td>'+
		            '<td name="userId" style="display:none;">'+v.id+'</td>'+
		            '<td name="orgId" style="display:none;">'+v.orgId+'</td>'+
		          '</tr>'
						;
				});
				$("#users").append(str.replace(/null/g,''));
				
				$('#red12POrgUser').ossPaginator({
					totalrecords: data.pageObj.total, 
					recordsperpage: 5, 
					length: 4, 
					next: '下一页', 
					prev: '上一页', 
					first: '首页', 
					last: '尾页', 
					initval: page,//初始化哪一页被选中
					controlsalways: true,
					onchange: function (newPage)
					{
						getUsers2POrgUser(newPage,5);
			    	}
				});
//				showDialog('查询成功！');
			}else{
				showDialog('未查到记录！');
				$('#users').empty();
				$('#red12POrgUser').empty();
			}	
		}
	});
}
$(document).ready(function(){
	//点击屏幕其他地方弹窗收起
		$(document).mouseup(function(e){
			if(e.which!=3){
			 	var _con = $("#treeWrapPOrgUser");   // 设置目标区域
				if(!_con.is(e.target) && _con.has(e.target).length === 0){ // Mark 1
			 	 	$("#treeWrapPOrgUser").slideUp();	
				 }
			}
		});
		$("#orgName2POrgUser").val(orgName);
		queryUsersPOrgUser();
});
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

var settingPOrgUser={
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
			url:"../peopleManage/menu_to_getOrgTree.do",
			autoParam:["orgId=orgId"]
		},
		callback:{
			onClick : onTreeClick
		}
};
//让操作人员可以通过选择机构树，查询该机构下的人员
function treePOrgUser(){
	var data={'orgId':orgId,'pOrgId':-1,'orgName':orgName,'isLeaf':0,'isParent':true,'open':false};
	$.fn.zTree.init($('#treeDemoWrapPOrgUser'),settingPOrgUser,data);
	$("#treeWrapPOrgUser").show();
}

//鼠标点击事件 
function onTreeClick(e,treeId,treeNode){
	$("#orgId2POrgUser").val(treeNode.orgId);
	$("#orgName2POrgUser").val(treeNode.orgName);
	$("#treeWrapPOrgUser").hide();
}
function showDialog(msg) {
	$('#msgDialog').html(msg).show();
	setTimeout(function() {
		$('#msgDialog').hide().html('');
	}, 1000);
}
function givePermission(obj){
	var userId1 = $(obj).attr('userId');
	var orgId1 = $(obj).attr('orgId');
	var orgName1 = $(obj).attr('orgName');
	var userName1=$(obj).attr('userName');
	var isAdd=$(obj).attr('isAdd');
	var manageOrgName=$(obj).attr('manageOrgName');
	//0代表添加，1代表修改
	var type=0;
	if(isAdd!='' && isAdd!=null && isAdd!='null'){
		type=1;
	}
	parent.userOrgDialog.showAddUserOrg(userId1,userName1,orgId1,orgName1,type,manageOrgName);	
}
function deleteItem(obj){
	var r=confirm("确定删除该用户所管辖区域？");
	var userId1 = $(obj).attr('userId');
	var manageOrgId1 = $(obj).attr('manageOrgId');
	if(r){
		$.ajax({
			async : true,
			cache : false,
			type : 'post',
			url : '../peopleOrgData/menu_toOrg_deleteUserOrg.do',
			data:{
				'userId':userId1,
				'orgId':manageOrgId1
			},
			success : function(msg) {
				if(msg=='success'){
					showDialog('删除成功！');
					queryUsersPOrgUser();
				}else{
					showDialog('删除失败！');
				}
				
			}
		});
	}

}
//提示框隐藏
function hideTip(){
	$('#msgDialog').hide().html('');
}