

var itemNum = 0;

$(document).ready(function(){
	loadDrafts(1,10);
	
	//发布按钮
	$('#publishBtn').click(function() {
//		$(".btn").removeClass("btn_sp"); 
//		$("#publishBtn").addClass("btn_sp");
		var itemIds = getSelectedItemIds();
		if(itemIds.length == 0) {
			showDialog('没有选择任何草稿，请重新选择');
			return;
		}
		openLoad('发布中...');
		$.ajax({
			type: 'POST',
			contentType: 'application/json;charset=UTF-8',
			url: 'menu_myNotice_publishNoticeItems.do',
			data: JSON.stringify(itemIds),
			success : function(data) {
				if(data) {
					$('.itemli .checkedItem:checked').parents('.itemli').remove();
					closeLoad();
					showDialog('成功发布');
				}
			}
		});
	});
	
	//删除按钮
	$('#removeBtn').click(function() {
//		$(".btn").removeClass("btn_sp"); 
//		$("#removeBtn").addClass("btn_sp");
		var itemIds = getSelectedItemIds();
		if(itemIds.length == 0) {
			showDialog('没有选择任何草稿，请重新选择');
			return;
		}
		if(window.confirm('是否确认删除？')){
			openLoad('删除中...');
			$.ajax({
				type: 'POST',
				contentType: 'application/json;charset=UTF-8',
				url: 'menu_draft_removeDrafts.do',
				data: JSON.stringify(itemIds),
				success : function(data) {
					if(data) {
						$('.itemli .checkedItem:checked').parents('.itemli').remove();
						closeLoad();
						showDialog('成功删除草稿');
					}
				}
			});
		}
	});
	
	//刷新按钮
	$('#reloadBtn').click(function() {
//		$(".btn").removeClass("btn_sp"); 
//		$("#reloadBtn").addClass("btn_sp");
		$("#choose").attr('checked',false);
		loadDrafts(1,10);
	});
	
	//全选按钮
	$("#choose").click(function(){
		if(document.getElementById("choose").checked){
			$('.itemli').each(function(){
				$(".checkedItem").prop('checked',true);
			});
		}else {
			$('.itemli').each(function(){
				$(".checkedItem").prop('checked',false);
			});
		}
	});
})

//加载数据
function loadDrafts(page,rowSize){
	openLoad('加载中...');
	$('.items').empty();
	$.ajax({
		type : 'POST',
		url : 'menu_draft_getDrafts.do?ss=' + new Date().getTime(),
		data : {
			createStaff : staffId,
			page : page,
			rows : rowSize
		},
		success : function(mapMsg) {
			var total = mapMsg.total;
			$("#totalName").html("共"+total+"条记录");
			itemNum = mapMsg.rows.length;
			var noticeItems = mapMsg.rows;
			$.each(noticeItems, function(index, noticeItem) {
				var tr3 = null;
				var title = noticeItem.title;
				var noticeTitle = (title == null || title == '')?'(无标题)':title;
//				var fileFlag = noticeItem.fileNum > 0?'<b title="此公告包含附件"></b>':'';
				if(noticeItem.fileNum > 0){
					tr3 = '<td width="15"><i class="file"></i><b title="此公告包含附件"></b></td>';
				}else{
					tr3 = '<td>&nbsp;</td>';
				}
				$('.items').append('<tr class="itemli">'+
       				 			'<td width="15" onclick="checkBox(this, event);"><input class="checkedItem" type="checkbox" noticeId="' + noticeItem.bulletinId + '"/></td>'+
        						'<td width="580" onclick="showEditView('+noticeItem.bulletinId+');" title="'+noticeTitle+'">'+noticeTitle+'</td>'+
        						tr3+
        						'<td width="150" class="grey">'+noticeItem.createDate+'</td>'+
      							'</tr>');
				});
				closeLoad();
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
						loadDrafts(newPage,10);
			    	}
				});
			
		}
	});

}

//查找checbox选中项
function getSelectedItemIds() {
	var itemIds = [];
	$('.itemli .checkedItem:checked').each(function(i){
		itemIds.push($(this).attr("noticeId"));
	});
	console.info(itemIds);
//	$('.itemli').each(function() {
//		if($(this).find(".checkedItem").is(":checked")){
//			console.info($(this));
//		}
	return itemIds;
}

//单选chenck
function checkBox(srcElement, e) {
	if(document.getElementById("choose").checked){
		$("#choose").prop('checked',false);
	}
	var flag = 0;
	$('.itemli .checkedItem:checked').each(function(i){
		flag++;
	});
	if(flag == itemNum){
		$("#choose").prop('checked',true);
	}
}

//点击标题重新编辑
function showEditView(noticeId) {
	window.location.href = ctx + '/bulletin/menu_newBulletin_newBulletin.do?bulletinId=' + noticeId;
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