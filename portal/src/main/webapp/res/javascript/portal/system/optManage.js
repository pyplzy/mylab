//应用code
var appValueForResource = null;
var appTitleForResource = null;

$(document).ready(function(){
	//初始化下拉列表
	var $selects = $('#dropdownValue');
	$selects.easyDropDown({
		cutOff: 6,
		onChange: function(selected){
			appValueForResource = selected.value;
			appTitleForResource = selected.title;
			queryResources(1);
		}
	});
	appValueForResource=$("#dropdownValue").val();
	appTitleForResource=$("#dropdownValue option:selected").text();
	queryResources(1);
	
	window.parent.changeHeight();
});

//资源查询
function queryResources(page){
	$("#optTable").html('');
	var num = 0;
	$.ajax({
		async : true,
		cache : false,
		type : 'post',
		url : ctx+'/optManage/menu_toOpt_qryResources.do',
		data:{
			'appCode':appValueForResource,
			'rows':5,
			'page':page
		},
		success : function(data) {
			num = data.total;
			var str = '';
			$("#resourceTable").html('');
			$.each(data.rows,function(i,v){
				str+='<tr onclick="chk(this);" resourceId = '+v.resourceId+'>'+
		                '<td width="5"></td>'+
		                '<td width="50" title='+appTitleForResource+'>'+appTitleForResource+'</td>'+
		                '<td width="50" title='+v.resourceCode+'>'+v.resourceCode+'</td>'+
		                '<td width="50" title='+v.resourceName+'>'+v.resourceName+'</td>'+
		                '<td width="100" title='+v.resourceDesc+'>'+v.resourceDesc+'</td>'+
		              '</tr>';
				
			});
			$("#resourceTable").append(str.replace(/null/g,''));
			//$("#resourceTable").append(str);
			//角色列表分页
			$('#red1').ossPaginator({
				totalrecords: num, 
				recordsperpage: 5, 
				length: 4, 
				next: '下一页', 
				prev: '上一页', 
				first: '首页', 
				last: '尾页', 
				initval: page,//初始化哪一页被选中
//				display: 'single',
				controlsalways: true,
				onchange: function (newPage)
				{
					$("#pageC").val(newPage);
					queryResources(newPage);
		    	}
			});
		}
	});
	window.parent.changeHeight();
}

//删除
function delResource(){
	var resourceId = $("#resourceTable .activebg").attr('resourceId');
	if(resourceId==undefined){
		showMsgTips("请选择一个资源",1000);
		return;
	}
	var r=confirm("确定要删除资源？");
	if(r){
		$.ajax({
			async : true,
			cache : false,
			type : 'post',
			url : ctx+'/optManage/menu_toOpt_delResource.do',
			data:{
				'resourceId':resourceId
			},
			success : function(data) {
				if(data=='use'){
					showMsgTips("该资源已关联操作权限",3000);
				}
				if(data=='succ'){
					showMsgTips("资源删除成功",1000);
					queryResources(1);
				}
				if(data=='fail'){
					showMsgTips("资源删除失败",1000);
				}
			}
		});
	}
}

//修改弹筐
function showParentDialog(){
	var resourceId = $("#resourceTable .activebg").attr('resourceId');
	if(resourceId==undefined){
		showMsgTips("请选择一个资源",1000);
		return;
	}
	parent.optDialog.showResourceDiv(resourceId);
}

//加载权限table
function loadOptTable(page){
	var resourceId = $("#resourceTable .activebg").attr('resourceId');
//	if(resourceId==undefined){
//		showMsgTips("请选择一个资源");
//		return;
//	}
	var num = 0;
	$.ajax({
		async : true,
		cache : false,
		type : 'post',
		url : ctx+'/optManage/menu_toOpt_qryOpts.do',
		data:{
			'resourceId':resourceId,
			'rows':5,
			'page':page
		},
		success : function(data) {
			num = data.total;
			var str = '';
			$("#optTable").html('');
			$.each(data.rows,function(i,v){
				str+='<tr optId = '+v.privilegeId+' onclick="chk2(this);">'+
		                '<td width="5"></td>'+
		                '<td width="60" title='+v.resourceName+'>'+v.resourceName+'</td>'+
		                '<td width="60" title='+v.privilegeCode+'>'+v.privilegeCode+'</td>'+
		                '<td width="60" title='+v.privilegeName+'>'+v.privilegeName+'</td>'+
		              '</tr>';
			});
			$("#optTable").append(str);
			//角色列表分页
			$('#red2').ossPaginator({
				totalrecords: num, 
				recordsperpage: 5, 
				length: 4, 
				next: '下一页', 
				prev: '上一页', 
				first: '首页', 
				last: '尾页', 
				initval: page,//初始化哪一页被选中
//				display: 'single',
				controlsalways: true,
				onchange: function (newPage)
				{
					queryResources(newPage);
		    	}
			});
		}
	});
	window.parent.changeHeight();
}

//点击每行角色选中单选按钮
function chk(obj){
	//选中一行 
	$(obj).removeClass().addClass("activebg");
	$(obj).siblings().removeClass("activebg");
	
	loadOptTable(1);
}
//点击每行预置账号选中单选按钮
function chk2(obj){
	//选中一行 
	$(obj).removeClass().addClass("activebg");
	$(obj).siblings().removeClass("activebg");
}

//权限新增
function showOptDiv(){
	var resourceId = $("#resourceTable .activebg").attr('resourceId');
	if(resourceId==undefined){
		showMsgTips("请选择一个资源",1000);
		return;
	}
	var resourceName = $("#resourceTable .activebg td:eq(3)").html();
	parent.optDialog.showParentOptDiv(resourceName,resourceId,0);
}

//权限修改
function showOptDialog(){
	var resourceId = $("#resourceTable .activebg").attr('resourceId');
	if(resourceId==undefined){
		showMsgTips("请选择一个资源",1000);
		return;
	}
	$("#optTable .activebg").attr('optId');
	var tt = $("#optTable .activebg");
	if(tt.length==0||tt.length>1){
		showMsgTips("请选择一个操作权限",1000);
		return;
	}
	var optId = tt.attr('optId');
	var resourceName = $("#resourceTable .activebg td:eq(3)").html();
	parent.optDialog.showParentOptDiv(resourceName,resourceId,optId);
}

//删除
function delOpt(){
	var resourceId = $("#resourceTable .activebg").attr('resourceId');
	if(resourceId==undefined){
		showMsgTips("请选择一个资源",1000);
		return;
	}
	var tt = $("#optTable .activebg");
	if(tt.length==0){
		showMsgTips("请选择一个操作权限",1000);
		return;
	}
	var optId = tt.attr('optId');
	var r = confirm("确定要删除选中操作权限？");
	if(r){
		$.ajax({
			async : true,
			cache : false,
			type : 'post',
			url : ctx+'/optManage/menu_toOpt_delOpts.do',
			data:{
				'optId':optId
			},
			success : function(data) {
				if(data=='use'){
					showMsgTips("该操作权限已关联角色",3000);
				}else if(data=='succ'){
					showMsgTips("删除成功",1000);
					loadOptTable(1);
				}else{
					showMsgTips("删除失败",1000);
				}
			}
		});
	}
}