
var itemNum = 0;
var itemsRows = [];
var currBulletinId = null;
var operType = null;
var total = 0;
var offLine = '';

$(document).ready(function(){
	loadNotices('',1,10);
	
	//删除按钮
	$('#removeBtn').click(function() {
//		$(".btn").removeClass("btn_sp"); 
//		$("#removeBtn").addClass("btn_sp");
		var itemIds = getSelectedItemIds();
//		console.info(itemIds);
		if(itemIds.length == 0) {
			showDialog('没有选择任何公告，请重新选择');
			return;
		}
		if(window.confirm('是否确认删除？')){
			openLoad('删除中...');
			$.ajax({
				type: 'POST',
				contentType: 'application/json;charset=UTF-8',
				url: 'menu_notice_removePublisheds.do',
				data: JSON.stringify(itemIds),
				success : function(data) {
					if(data) {
						$.each(itemIds, function(index, itemId) {
							deleteItem(itemId);
						});
						var totalNum = total-itemIds.length;
						$("#totalName").html("").html("共"+totalNum+"条记录");
//						$(".btn").removeClass("btn_sp"); 
						closeLoad();
						showDialog('成功删除');
					}
				}
			});
		}
	});
	
	//查看按钮
	$('#lookBtn').click(function() {
//		$(".btn").removeClass("btn_sp"); 
//		$("#lookBtn").addClass("btn_sp");
	});
	
	//查看全部公告按钮
	$('#lookAllBtn').click(function() {
		$("#choose").prop('checked',false);
		loadNotices('',1,10);
	});
	
	//查看在线公告按钮
	$('#lookOnBtn').click(function() {
		$("#choose").prop('checked',false);
		loadNotices('n',1,10);
	});
	
	//查看下线公告按钮
	$('#lookOffBtn').click(function() {
		$("#choose").prop('checked',false);
		loadNotices('y',1,10);
	});
	
	//全选按钮
	$("#choose").click(function(){
		if(document.getElementById("choose").checked){
			$('.item_mask').each(function(){
				$(".checkbtn").prop('checked',true);
			});
		}else {
			$('.item_mask').each(function(){
				$(".checkbtn").prop('checked',false);
			});
		}
	});
	
	//查看弹窗
	$(".btn_look").click(function(){
		$(this).parent().find(".pop_view").show();
	});
	$(".viewItem").click(function(){
		$(this).parent().hide();
	});
	

	//点击屏幕其他地方弹窗收起
	$(document).mouseup(function(e){
	  var _con = $(".btn_look,.xiaxian,.pop_xia,.pop_view");   // 设置目标区域
	  if(!_con.is(e.target) && _con.has(e.target).length === 0){ // Mark 1
		  $(".pop_xia,.pop_view").hide();	
	  }
	});
})

//加载我的公告数据
function loadNotices(offline,page,rows) {
	if(!offline) {
		offline = '';
	}
	openLoad('加载中...');
	$('.items').empty();
	$.ajax({
		type : 'POST',
		async: false,
		url : 'menu_myNotice_getPublished.do?ss=' + new Date().getTime(),
		data : {
			staffId : staffId,
			offline : offline,
			page : page,
			rows : rows
		},
		success : function(mapMsg) {
			total = mapMsg.total;
			$("#totalName").html("共"+total+"条记录");
			var noticeItems = mapMsg.rows;
			itemNum = mapMsg.rows.length;
			$.each(noticeItems, function(index, item) {
				appendItem(item);
			});
			closeLoad();
			
//			changeHeight();
			
			$(".xiaxian").click(function(){
				$(this).parent().find(".pop_xia").show();
			});
			
			$('#red').ossPaginator({
				totalrecords: mapMsg.total, 
				recordsperpage: 10, 
				length: 6, 
				next: '下一页', 
				prev: '上一页', 
				first: '首页', 
				last: '尾页', 
				initval: page,//初始化哪一页被选中
				controlsalways: true,
				onchange: function (newPage)
				{
					loadNotices(offline,newPage,10);
		    	}
			});
			window.parent.changeHeight();
		}
	});
		
}

//删除一行
function deleteItem(bulletinId) {
	$('.item[bulletinId="' + bulletinId + '"]').remove();
	$("#choose").prop('checked',false);
	for(var i = 0; i < itemsRows.length; i++) {
		if(itemsRows[i].bulletinId==bulletinId) {
			itemsRows.splice(i, 1);
			break;
		}
	}
}

//追加一行
function appendItem(item) {
	offLine = item.offline == 'y';
	var lineIcom = offLine?'<div class="cols colsd" title="此公告已下线"><i class="status offline"></i></div>':'<div class="cols colsd" title="此公告已上线"><i class="status online"></i></div>';
	var title = item.title;
	var noticeTitle = (title == null || title == '')?'(无标题)':title;
	var replyIcon = item.replyFlag == 1?'<div class="cols cols3" title="点击关闭回复功能" onclick="closeReply(this,'+item.bulletinId+');"><i class="reply"></i></div>':'<div class="cols cols3" title="点击开启回复功能" onclick="openReply(this,'+item.bulletinId+');"><i class="unreply"></i></div>';
	var loseTime = (item.loseTime==null || item.loseTime == '')?'永不失效':item.loseTime;
	var fileFlag = item.fileNum > 0?'<div class="cols cols7" title="此公告包含附件"><i class="file"></i></div>':'<div class="cols cols7"></div>';
	var oper = '';
	if(offLine) {
		oper =	'<div class="cols cols8">'+
             	'   	<a href="javascript:void(0)" class="xiaxian">操作<i></i></a>'+
               	'         	<div class="pop_xia clearfix">'+
                '           <div class="viewItem" onclick="publish(this);">再次发布</div>'+
                '           <div class="viewItem" onclick="copy(this);">拷贝到草稿</div>'+
                '      </div>'+
                '</div>';
	} else {
		oper =	'<div class="cols cols8">'+
             	'   	<a href="javascript:void(0)" class="xiaxian"  >操作<i></i></a>'+
               	'         <div class="pop_xia clearfix">'+
              	'          	<div class="viewItem" onclick="downline(this);">下线</div>'+
                '           <div class="viewItem" onclick="updateLoseDate(this);">修改下线时间</div>'+
                '           <div class="viewItem" onclick="copy(this);">拷贝到草稿</div>'+
                '      </div>'+
                '</div>';
	}
	$(".items").append('<div class="item" bulletinId="' + item.bulletinId + '">'+ 
			            '	<div class="item_mask">'+
			            '   	<div class="cols cols1"><input type="checkbox" onclick="checkBox(this, event);" class="checkbtn"  noticeId="' + item.bulletinId + '"/></div>'+
			            '    	'+lineIcom+
			            '    	<div class="cols cols2" onclick="previewNotice(this);" title="'+noticeTitle+'">'+noticeTitle+'</div>'+
			            '    	'+replyIcon+
			            '    	<div class="cols cols4">'+loseTime+'</div>'+
			            '    	<div class="cols cols5">'+item.readCnt+'</div>'+
			            '    	<div class="cols cols6">'+item.replyNum+'</div>'+
			            '    	'+fileFlag+
			            '    	'+oper+
			            '	</div>'+
			            '</div>');
			            
    itemsRows.push(item);
			            
}

//点击操作
//function operateItem(obj){
//var $obj = $(obj);
//	$(".xiaxian").click(function(){
//		console.info("aqqq");
//		$(this).parent().find(".pop_xia").slideToggle();
//	});
//}

//再次发布
function publish(obj) {
	currBulletinId = $(obj).parents('.item').attr('bulletinId');
	operType = 0;
	showMsgbox('再&nbsp;次&nbsp;发&nbsp;布');
}

//拷贝到草稿箱
function copy(obj) {
	openLoad('处理中...');
	var bulletinId = $(obj).parents('.item').attr('bulletinId');
	$.ajax({
		type: 'POST',
		url: 'menu_notice_copyToDraft.do',
		data: {
			bulletinId:bulletinId
		},
		success : function(data) {
			if(data) {
				closeLoad();
				showDialog('成功拷贝至草稿箱');
			}
		}
	});
}

//下线
function downline(obj) {
	openLoad('处理中...');
	var bulletinId = $(obj).parents('.item').attr('bulletinId');
	$.ajax({
		type: 'POST',
		url: 'menu_notice_downlineNotice.do',
		data: {
			bulletinId:bulletinId
		},
		success : function(dataMsg) {
			if(dataMsg.msgFlag) {
				refreshItem(bulletinId, {
					offline:'y',
					loseTime:dataMsg.loseTime
				});
				closeLoad();
				showDialog('成功下线');
			}
		}
	});
}

//修改下线时间
function updateLoseDate(obj) {
	currBulletinId = $(obj).parents('.item').attr('bulletinId');
	operType = 1;
	showMsgbox('下&nbsp;线&nbsp;时&nbsp;间&nbsp;设&nbsp;置');
}

//下线时间表单提交
function submitMsgbox() {
	openLoad('处理中...');
	var url = operType == 0?'menu_notice_publishNotice.do':'menu_notice_updateLoseDate.do';
	var loseTime = $('#loseTime').val();
	var msg = operType == 0?'成功发布':'成功修改';
	$.ajax({
		type: 'POST',
		url: url,
		data: {
			bulletinId:currBulletinId,
			loseTime:loseTime
		},
		success : function(data) {
			if(data) {
				refreshItem(currBulletinId, {
					offline:'n',
					loseTime:loseTime
				});
				closeLoad();
				showDialog(msg);
				closeMsgbox();
			}
		}
	});
}

//弹出选择下线时间对话框
function showMsgbox(title) {	
	$('#publishBox').show().find('.msgbox-title').html(title);
	$('#maskItem').show();
}

//关闭选择下线时间对话框
function closeMsgbox() {
	$('#loseTime').val('');
	$('#publishBox').hide().find('.msgbox-title').html('');
	$('#maskItem').hide();
}

//取单行数据
function getItem(bulletinId) {
	for(var i = 0, n = itemsRows.length; i < n; i++) {
		if(itemsRows[i].bulletinId == bulletinId) {
			return itemsRows[i];
		}
	}
}

//刷新一行
function refreshItem(bulletinId, newItem){
	var item = getItem(bulletinId);
	for(var s in newItem) {
		item[s] = newItem[s];
	}
	var offLine = item.offline == 'y';
	var lineIcom = offLine?'<div class="cols colsd" title="此公告已下线"><i class="status offline"></i></div>':'<div class="cols colsd" title="此公告已上线"><i class="status online"></i></div>';
	var title = item.title;
	var noticeTitle = (title == null || title == '')?'(无标题)':title;
	var replyIcon = item.replyFlag == 1?'<div class="cols cols3" title="点击关闭回复功能" onclick="closeReply(this,'+item.bulletinId+');"><i class="reply"></i></div>':'<div class="cols cols3" title="点击开启回复功能" onclick="openReply(this,'+item.bulletinId+');"><i class="unreply"></i></div>';
	var loseTime = (item.loseTime==null || item.loseTime == '')?'永不失效':item.loseTime;
	var fileFlag = item.fileNum > 0?'<div class="cols cols7" title="此公告包含附件"><i class="file"></i></div>':'<div class="cols cols7"></div>';
	var oper = '';
	if(offLine) {
		oper =	'<div class="cols cols8">'+
             	'   	<a href="javascript:void(0)" class="xiaxian">操作<i></i></a>'+
               	'         	<div class="pop_xia clearfix">'+
                '           <div class="viewItem" onclick="publish(this);">再次发布</div>'+
                '           <div class="viewItem" onclick="copy(this);">拷贝到草稿</div>'+
                '      </div>'+
                '</div>';
	} else {
		oper =	'<div class="cols cols8">'+
             	'   	<a href="javascript:void(0)" class="xiaxian"  >操作<i></i></a>'+
               	'         <div class="pop_xia clearfix">'+
              	'          	<div class="viewItem" onclick="downline(this);">下线</div>'+
                '           <div class="viewItem" onclick="updateLoseDate(this);">修改下线时间</div>'+
                '           <div class="viewItem" onclick="copy(this);">拷贝到草稿</div>'+
                '      </div>'+
                '</div>';
	}
	$('.item[bulletinId="' + item.bulletinId +'"]').empty()
				.append('<div class="item_mask">'+
			            '   <div class="cols cols1"><input type="checkbox" onclick="checkBox(this, event);" class="checkbtn"  noticeId="' + item.bulletinId + '"/></div>'+
			            '    '+lineIcom+
			            '    <div class="cols cols2" onclick="previewNotice(this);" title="'+noticeTitle+'">'+noticeTitle+'</div>'+
			            '    '+replyIcon+
			            '    <div class="cols cols4">'+loseTime+'</div>'+
			            '    <div class="cols cols5">'+item.readCnt+'</div>'+
			            '    <div class="cols cols6">'+item.replyNum+'</div>'+
			            '    '+fileFlag+
			            '    '+oper+
			            '</div>');
			            
    $('.item[bulletinId="' + item.bulletinId +'"] .xiaxian').click(function(){
		$(this).parent().find(".pop_xia").show();
	});
	
}

//公告预览
function previewNotice(obj) {
	var bulletinId = $(obj).parents('.item').attr('bulletinId');
	window.open("menu_notice_browseNotice.do?noticeId=" + bulletinId);
}

//关闭回复
function closeReply(obj, bulletinId){
	var $obj = $(obj);
	openLoad('处理中...');
	$.ajax({
		type: 'GET',
//		async:false,
		url: 'menu_notice_closeReply.do?ss=' + new Date().getTime(),
		data: {
			bulletinId:bulletinId
		},
		success : function(data) {
			if(data) {
				refreshItem(bulletinId, {
					replyFlag:0
				});
				closeLoad();
				showDialog('回复功能已被关闭');
			}
		}
	});
}

//开启回复
function openReply(obj, bulletinId){
	var $obj = $(obj);
	openLoad('处理中...');
	$.ajax({
		type: 'GET',
		url: 'menu_notice_openReply.do?ss=' + new Date().getTime(),
		data: {
			bulletinId:bulletinId
		},
		success : function(data) {
			if(data) {
				refreshItem(bulletinId, {
					replyFlag:1
				});
				closeLoad();
				showDialog('回复功能已被开启');
			}
		}
	});
}

//单选chenckbox
function checkBox(srcElement, e) {
	if(document.getElementById("choose").checked){
		$("#choose").prop('checked',false);
	}
	var flag = 0;
	$('.tablebox .item .checkbtn:checked').each(function(i){
		flag++;
	});
	if(flag == itemNum){
		$("#choose").prop('checked',true);
	}
}

//查找checbox选中项
function getSelectedItemIds() {
	var itemIds = [];
	$('.item .checkbtn:checked').each(function(i){
		itemIds.push($(this).parents('.item').attr('bulletinId'));
	});
	return itemIds;
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
//对比得到最大高度并赋给各包块
function changeHeight(){
	r_h = iframeObj.contents().find("body").height();
	//alert(r_h);
	// 菜单页面显示高度不低于500
//	if(r_h < 500){
//		r_h = 600;
//	}
	$(".rightWrap").height(r_h);
};