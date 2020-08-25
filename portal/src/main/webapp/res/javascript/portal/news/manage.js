
var itemNum = 0;
var itemsRows = [];
var currBulletinId = null;
var operType = null;
var total = 0;

$(document).ready(function(){
	loadNotices(1,10);
	
	//查询按钮
	$('#qryBtn').click(function(){
		loadNotices(1,10);
	});
	
	//点击屏幕其他地方弹窗收起
	$(document).mouseup(function(e){
		var _con = $(".xiaxian,.pop_xia");// 设置目标区域
		if(!_con.is(e.target) && _con.has(e.target).length === 0){// Mark 1
			$(".pop_xia").hide();
		}
	});
})

//加载公告管理的数据
function loadNotices(page,rows) {
	var releaseStartTime = $.trim($("#releaseStartTime").val());
	var releaseEndTime = $.trim($("#releaseEndTime").val());
	var title = $.trim($('#newsName').val());
	openLoad('加载中...');
	$('.items').empty();
	$.ajax({
		type : 'POST',
		async: false,
		url : 'menu_new_getPublished.do?ss=' + new Date().getTime(),
		data : {
//			startTime : releaseStartTime,//用string接受的参数
//			endTime : releaseEndTime,
			stTime : releaseStartTime,//用date类型接受
			edTime : releaseEndTime,
			title : title,
			userId : -1,
			page : page,
			rows : rows
		},
		success : function(mapMsg) {
			total = mapMsg.total;
			$("#totalName").html("共"+total+"条记录");
			var newsItems = mapMsg.rows;
			itemNum = mapMsg.rows.length;
			appendItem(newsItems);//展示查询结果
			closeLoad();
			
			$(".xiaxian").click(function(){
				//当下拉选已经展示的时候再点击本身则隐藏下拉选
				if($(this).parent().find(".pop_xia").is(":hidden")){
					$(".pop_xia").hide();//保证页面上只有一个操作下拉选是展示的
					$(this).parent().find(".pop_xia").show();
				}else{
					$(this).parent().find(".pop_xia").hide();
				}
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
					loadNotices(newPage,10);
		    	}
			});
			//重新调整iframe高度，防止分页不显示（防止第二个选项卡内容较多切换为第一个选项卡，翻页时内容较少，再点击第二个选项卡导致翻页消失）
			window.parent.changeHeight();
		}
	});
		
}

//删除一行
function deleteItem(obj) {
	var newsId = $(obj).parents('.item').attr('newsId');
	if(window.confirm('是否确认删除？')){
		openLoad('删除中...');
		$.ajax({
			type: 'POST',
			url: 'menu_new_deleteNewsById.do',
			data: {
				newsId:newsId	
			},
			success : function(data) {
				if(data) {
					$('.item[newsId="' + newsId + '"]').remove();
					for(var i = 0; i < itemsRows.length; i++) {
						if(itemsRows[i].newsId==newsId) {
							itemsRows.splice(i, 1);
							break;
						}
					}
//					var totalNum = total-1;//单行删除
//					$("#totalName").html("").html("共"+totalNum+"条记录");
					closeLoad();
					showDialog('成功删除');
				}
				loadNotices(1,10);//删除成功后刷新页面
			}
		});
	}
}

//追加行
function appendItem(newsItems) {
	var html = '';
	$.each(newsItems, function(index, item) {
		var lineIcom = '<div class="cols colsd"></div>';
		var title = item.title;
		var newsTitle = (title == null || title == '')?'(无标题)':title;
		var replyIcon = item.replyFlag == 1?'<div class="cols cols3" title="点击关闭回复功能" onclick="closeReply(this,'+item.newsId+');"><i class="reply"></i></div>':'<div class="cols cols3" title="点击开启回复功能" onclick="openReply(this,'+item.newsId+');"><i class="unreply"></i></div>';
		var userName = (item.userName == null || item.userName == '')?'未知':item.userName;
		var oper =	'<div class="cols cols8">'+
		'   	<a href="javascript:void(0)" class="xiaxian"  >操作<i></i></a>'+
		'         <div class="pop_xia clearfix">'+
		'         <div class="viewItem" onclick="copy(this);">拷贝到草稿</div>'+
		'         <div class="viewItem" onclick="deleteItem(this);">删除</div>'+
		'      </div>'+
		'</div>';
		html += '<div class="item" newsId="' + item.newsId + '">'+ 
		'	<div class="item_mask">'+
//		'   	<div class="cols cols1"><input type="checkbox" onclick="checkBox(this, event);" class="checkbtn"  newsId="' + item.newsId + '"/></div>'+
		'    	'+lineIcom+
		'    	<div class="cols cols2" onclick="previewNotice(this);" title="'+newsTitle+'">'+newsTitle+'</div>'+
		'    	'+replyIcon+
		'    	<div class="cols cols4">'+item.releaseTime+'</div>'+
		'    	<div class="cols cols5">'+item.readCnt+'</div>'+
		'    	<div class="cols cols6">'+item.replyNum+'</div>'+
		'    	<div class="cols cols71">'+userName+'</div>'+
		'    	<div class="title title72">&nbsp;</div>'+
		'    	'+oper+
		'	</div>'+
		'</div>'
	    itemsRows.push(item);
	});
	$(".items").append(html);
			            
}

//拷贝到草稿箱
function copy(obj) {
	openLoad('处理中...');
	var newsId = $(obj).parents('.item').attr('newsId');
	$.ajax({
		type: 'POST',
		url: 'menu_new_copyNewsToDraft.do',
		data: {
			newsId:newsId
		},
		success : function(data) {
			if(data) {
				closeLoad();
				showDialog('成功拷贝至草稿箱');
			}
		}
	});
}

//取单行数据
function getItem(newsId) {
	for(var i = 0, n = itemsRows.length; i < n; i++) {
		if(itemsRows[i].newsId == newsId) {
			return itemsRows[i];
		}
	}
}

//刷新一行
function refreshItem(newsId, newItem){
	var item = getItem(newsId);
	for(var s in newItem) {
		item[s] = newItem[s];
	}
	var lineIcom = '<div class="cols colsd"></div>';
	var title = item.title;
	var newsTitle = (title == null || title == '')?'(无标题)':title;
	var replyIcon = item.replyFlag == 1?'<div class="cols cols3" title="点击关闭回复功能" onclick="closeReply(this,'+item.newsId+');"><i class="reply"></i></div>':'<div class="cols cols3" title="点击开启回复功能" onclick="openReply(this,'+item.newsId+');"><i class="unreply"></i></div>';
	var releaseTime = (item.releaseTime==null || item.releaseTime == '')?'':item.releaseTime;
	var userName = (item.userName == null || item.userName == '')?'未知':item.userName;
	//在线的公告拥有的操作
	var	oper =	'<div class="cols cols8">'+
             	'   	<a href="javascript:void(0)" class="xiaxian"  >操作<i></i></a>'+
               	'         <div class="pop_xia clearfix">'+
        		'         <div class="viewItem" onclick="copy(this);">拷贝到草稿</div>'+
        		'         <div class="viewItem" onclick="deleteItem(this);">删除</div>'+
                '      </div>'+
                '</div>';
	$('.item[newsId="' + item.newsId +'"]').empty()
				.append('<div class="item" newsId="' + item.newsId + '">'+ 
			            '	<div class="item_mask">'+
//			            '   	<div class="cols cols1"><input type="checkbox" onclick="checkBox(this, event);" class="checkbtn"  noticeId="' + item.newsId + '"/></div>'+
			            '    	'+lineIcom+
			            '    	<div class="cols cols2" onclick="previewNotice(this);" title="'+newsTitle+'">'+newsTitle+'</div>'+
			            '    	'+replyIcon+
			            '    	<div class="cols cols4">'+releaseTime+'</div>'+
			            '    	<div class="cols cols5">'+item.readCnt+'</div>'+
			            '    	<div class="cols cols6">'+item.replyNum+'</div>'+
			            '    	<div class="cols cols71">'+userName+'</div>'+
			            '    	<div class="title title72">&nbsp;</div>'+
			            '    	'+oper+
			            '	</div>'+
			            '</div>');
        $('.item[newsId="' + item.newsId +'"] .xiaxian').click(function(){
			$(this).parent().find(".pop_xia").show();
		});
}

//公告预览
function previewNotice(obj) {
	var newsId = $(obj).parents('.item').attr('newsId');
	window.open("menu_new_newsDetail.do?newsId=" + newsId);
}

//关闭回复
function closeReply(obj, newsId){
	var $obj = $(obj);
	openLoad('处理中...');
	$.ajax({
		type: 'GET',
		url: 'menu_new_closeReply.do?ss=' + new Date().getTime(),
		data: {
			newsId:newsId
		},
		success : function(data) {
			if(data) {
				refreshItem(newsId, {
					replyFlag:0
				});
				closeLoad();
				showDialog('回复功能已被关闭');
			}
		}
	});
}

//开启回复
function openReply(obj, newsId){
	var $obj = $(obj);
	openLoad('处理中...');
	$.ajax({
		type: 'GET',
		url: 'menu_new_openReply.do?ss=' + new Date().getTime(),
		data: {
			newsId:newsId
		},
		success : function(data) {
			if(data) {
				refreshItem(newsId, {
					replyFlag:1
				});
				closeLoad();
				showDialog('回复功能已被开启');
			}
		}
	});
}

//展示开启、关闭回复提示
function showDialog(msg) {
	$('#msgDialog').html(msg).show();
	setTimeout(function() {
		$('#msgDialog').hide().html('');
	}, 3000);
}

//加载中
function openLoad(msg) {
	$('#loadDialog').show().find('span').html(msg);
}

//关闭加载中
function closeLoad() {
	$('#loadDialog').hide().find('span').html('');
}
