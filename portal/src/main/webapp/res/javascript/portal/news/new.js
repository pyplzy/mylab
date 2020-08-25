
$(function(){
	var um = UM.getEditor('myEditor', {
		imageUrl:'menu_new_upload.do',
		imagePath:'',
//		dropFileEnabled:false,	//禁用拖拽(直接拖到编辑框中)
    	initialFrameWidth:'',
    	initialFrameHeight:330,
    	autoHeightEnabled:false,
    	toolbar:[
    	            'undo redo | emotion horizontal forecolor backcolor | bold italic underline strikethrough | superscript subscript | removeformat |',
    	            'insertorderedlist insertunorderedlist | selectall cleardoc paragraph | fontfamily fontsize' ,
    	            '| justifyleft justifycenter justifyright justifyjustify |',
    	            'link unlink | image | fullscreen'
    	        ]
    });
    
    //文件上传
    $('#fileupload').fileupload({
		dataType: 'json',
		dropZone: null,	//禁用拖拽
	    add: function (e, data) {
	    	var size = data.files[0].size;
	    	if (size > 812 * 1024) { // 512kb
	    		showDialog('您上传的文件超过大小限制');
                return;
            }
    		//文件名
    		var fileName = data.files[0].name;
    		//点的位置
    		var lastDotIndex = fileName.lastIndexOf(".");
    		//后缀名
    		var suffix = fileName.substring(lastDotIndex).toLowerCase();
    		var whiteList = ['.png','.jpg','.jpeg','.bmp'];
    		if(whiteList.indexOf(suffix) < 0){
    			showDialog('请上传正确格式的图片！');
                return;
    		}
    		//直接上传
    		data.submit();
	    },
	    submit:function(e, data) {
	    	openLoad('上传中...');
	    	$('#fileupload').attr('disabled',true);
	    	
	    },
	    done: function (e, data) {
	    	$('#fileupload').attr('disabled',false);
	    	
	    	closeLoad();
	    	if(data.result.url){
	    		$('#coverWrap').find('.fileinput-button').hide();
	    		//$('#coverWrap').append('<div class="imgWrap"><img src="'+data.result.url+'"><a class="close"></a></div>');
	    		$('#coverWrap').append('<div class="imgWrap"><img src="'+ctx+'/AttachmentController/getImgByPath.do?imgPath='+data.result.url+'"><a class="close"></a></div>');
	    		
	    	} else {
	    		if(data.result.state){
	    			showDialog(data.result.state);
	    		} else {
	    			alert("系统异常！");
	    		}
	    	}
        }
	 });
    
    //发布
	$('#publishBtn').click(function() {
		var title = $.trim($('#title').val());
		var digest = $.trim($('#digest').val());
		if(title == ''){
			showDialog('没有标题');
			return; 
		}
		if(title.length > 127){
			showDialog('标题过长');
			return; 
		}
		if(digest.length > 25){
			showDialog('摘要过长,最多25个字符');
			return; 
		}

		var coverPhoto = $('#coverWrap img').attr('src');
		if(coverPhoto == undefined || coverPhoto == ''){
			showDialog('没有封面');
			return; 
		}
		showDialog('发布中...');
		
		var content = um.getContent();
		var replyFlag=null;
		if(document.getElementById("replyBtn").checked){
			replyFlag= 1;
		}else{
			replyFlag= 0;
		}
		
		$.ajax({
			url: 'menu_new_postNews.do',
	        type : 'post',
	        data:{
	        	newsId: newsId,
	        	title: title,
	        	digest: digest,
	        	coverPhoto: coverPhoto,
				content: um.getContent(),
				state:1,
				replyFlag: replyFlag,
				//不被js过滤器转义< >等特殊字符（即使过滤器不开，对功能没有影响，另外只要检查到这个参数，就不转义，而不管这个参数值是什么）
				inXssWhiteListQ:"true"
	        },
	        success: function(msgMap) {
	        	closeLoad();
	        	var href = $('#flag').val()=='new' ? window.location.href : 'menu_draftbox_draftbox.do';
	        	if(msgMap.msgFlag) {
	        		newsId = msgMap.newsId;
	        		showDialog('已成功发布');
	        		setTimeout(function() {
	        			window.location.href = href;
	        		}, 1000);
	        	}
	        }
		});
	});
	
	// 预览
	$('#previewBtn').click(function() {
		var title = $('#title').val();
		$('#previewForm').find('input[name="newsId"]').val(newsId);
		$('#previewForm').find('input[name="title"]').val(title);
		$('#previewForm').submit();
	});
	
	 // 存草稿
	 $('#saveBtn').click(function(){
		var title = $.trim($('#title').val());
		var digest = $.trim($('#digest').val());
		if(title == ''){
			showDialog('没有标题');
			return; 
		}
		if(title.length > 127){
			showDialog('标题过长');
			return; 
		}
		if(digest.length > 25){
			showDialog('摘要过长，最多25个字符');
			return; 
		}
		var coverPhoto = $('#coverWrap img').attr('src');
		if(coverPhoto == undefined){
			coverPhoto = '';
		}
		openLoad('保存中...');
	 	var replyFlag=null;
		if(document.getElementById("replyBtn").checked){
			replyFlag = 1;
		}else{
			replyFlag = 0;
		}
		$.ajax({
			url: 'menu_new_postNews.do',
			type : 'post',
			data:{
				newsId: newsId,
				title: title,
				digest: digest,
				coverPhoto: coverPhoto,
				content: um.getContent(),
				state:0,
				replyFlag: replyFlag,
				//不被js过滤器转义< >等特殊字符（即使过滤器不开，对功能没有影响，另外只要检查到这个参数，就不转义，而不管这个参数值是什么）
				inXssWhiteListQ:"true"
			},
			success: function(msgMap) {
				closeLoad();
				if(msgMap.msgFlag) {
					newsId = msgMap.newsId;
					showDialog('已成功保存到草稿箱');
		    	} else {
		    		showDialog('保存到草稿箱失败');
		    	}
			}
		});
	 });
	 
	 // 重写
	$('#rewriteBtn').click(function() {
		openLoad('加载中...');
		setTimeout(function() {
			window.location.href = window.location.href;
		}, 100);
	});
	
	 //返回
	$('#returnBtn').click(function() {
		window.location.href = 'menu_draftbox_draftbox.do';
	});
	
	//删除封面图片
	$('#coverWrap').on('click','.close',function(){
		$('#coverWrap').find('.imgWrap').remove();
		$('#coverWrap').find('.add_file_div').show();
	});
});

function showDialog(msg) {
	$('#msgDialog').html(msg).show();
	setTimeout(function() {
		$('#msgDialog').hide().html('');
	}, 2000);
}

function openLoad(msg) {
	$('#loadDialog').show().find('span').html(msg);
}

function closeLoad() {
	$('#loadDialog').hide().find('span').html('');
}