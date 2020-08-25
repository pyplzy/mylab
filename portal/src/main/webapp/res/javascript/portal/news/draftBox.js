

var itemNum = 0;

$(document).ready(function(){
	loadDrafts(1,10);
	
	//发布按钮
	$('#publishBtn').click(function() {
		var itemIds = getSelectedItemIds();
		var checklen=itemIds.length;
		if(checklen == 0) {
			showDialog('没有选择任何草稿，请重新选择');
			return;
		}
		openLoad('发布中...');
		$.ajax({
			type: 'post',
			contentType: 'application/json;charset=UTF-8',
			url: 'menu_new_publishItems.do',
			data: JSON.stringify(itemIds),
			success : function(data) {
				if(data) {
					$('.checkedItem:checked').parents('.itemli').remove();
					itemNum-=checklen;
					closeLoad();
					showDialog('成功发布');
				}
			}
		});
	});
	
	//删除按钮
	$('#removeBtn').click(function() {
		var itemIds = getSelectedItemIds();
		var checklen=itemIds.length;
		if(checklen == 0) {
			showDialog('没有选择任何草稿，请重新选择');
			return;
		}
		if(window.confirm('是否确认删除？')){
			openLoad('删除中...');
			$.ajax({
				type: 'POST',
				contentType: 'application/json;charset=UTF-8',
				url: 'menu_new_removeDrafts.do',
				data: JSON.stringify(itemIds),
				success : function(data) {
					if(data) {
						$('.checkedItem:checked').parents('.itemli').remove();
						itemNum-=checklen;
						closeLoad();
						showDialog('成功删除草稿');
					}
				}
			});
		}
	});
	
	//刷新按钮
	$('#reloadBtn').click(function() {
		$("#choose").attr('checked',false);
		loadDrafts(1,10);
	});
	
	//全选按钮
	$("#choose").click(function(){
		if(document.getElementById("choose").checked){
			$(".checkedItem").prop('checked',true);
		}else {
			$(".checkedItem").prop('checked',false);
		}
	});
})

//加载数据
function loadDrafts(page,rowSize){
	openLoad('加载中...');
	$('.items').empty();
	$.ajax({
		type : 'POST',
		url : 'menu_new_getDrafts.do',
		data : {
			page : page,
			rows : rowSize
		},
		success : function(mapMsg) {
			var total = mapMsg.total;
			$("#totalName").html("共"+total+"条记录");
			itemNum = mapMsg.rows.length;
			var items = mapMsg.rows;
			var array=[];
			$.each(items, function(index, item) {
				var title = (item.title == null || item.title =='') ? '(无标题)' : item.title;
				array.push('<tr class="itemli">'+
       				 			'<td width="15" onclick="checkBox(this, event);"><input class="checkedItem" type="checkbox" newsId="' + item.newsId + '"/></td>'+
        						'<td width="580" onclick="showEditView('+item.newsId+');" title="'+title+'">'+title+'</td>'+
        						'<td width="150" class="grey">'+item.createTime+'</td>'+
      							'</tr>');
				});
				$('.items').append(array.join(''));
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
					onchange: function (newPage) {
						loadDrafts(newPage,10);
			    	}
				});
		}
	});
}

//查找checbox选中项
function getSelectedItemIds() {
	var itemIds = [];
	$('.checkedItem:checked').each(function(i){
		itemIds.push($(this).attr("newsId"));
	});
	return itemIds;
}

//单选chenck
function checkBox(srcElement, e) {
	if(document.getElementById("choose").checked){
		$("#choose").prop('checked',false);
	}
	var flag = 0;
	$('.checkedItem:checked').each(function(i){
		flag++;
	});
	if(flag == itemNum){
		$("#choose").prop('checked',true);
	}
}

//点击标题重新编辑
function showEditView(newsId) {
	window.location.href = 'menu_new_new.do?newsId=' + newsId;
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