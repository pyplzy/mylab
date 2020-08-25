
var groupCode = 'notice';

$(document).ready(function(){
	//添加附件按钮
//	$('#uploadBtn').hover(function() {
//		$(this).addClass('fb_menu_hover').find('span').css('color', '#FFFFFF');
//	}, function() {
//		$(this).removeClass('fb_menu_hover').find('span').css('color', 'red');
//	});
	
	var um = UM.getEditor('myEditor', {
    	initialFrameWidth:'',
    	initialFrameHeight:330,
    	autoHeightEnabled:false,
    	pasteImageEnabled:false,
		dropFileEnabled:false,
    	toolbar:[
    	            'undo redo | emotion horizontal forecolor backcolor | bold italic underline strikethrough | superscript subscript | removeformat |',
    	            'insertorderedlist insertunorderedlist | selectall cleardoc paragraph | fontfamily fontsize' ,
    	            '| justifyleft justifycenter justifyright justifyjustify |',
    	            'link unlink fullscreen'
    	        ]
    });
    
//    $(".fileArea").empty();
    //文件上传
    $('#fileupload').fileupload({
		dataType: 'json',
//		autoUpload: false,
	    add: function (e, data) {
	    	var size = data.files[0].size;
	    	if (size > 10 * 1024 * 1024) { // 10mb
	    		showDialog('您上传的文件超过大小限制');
                return;
            }
    		//文件名
    		var fileName = data.files[0].name;
    		//点的位置
    		var lastDotIndex = fileName.lastIndexOf(".");
    		//后缀名
    		var suffix = fileName.substring(lastDotIndex);
    		if(suffix=='.exe'||suffix=='.bat'||suffix=='.sh'||suffix=='.js'||suffix=='.html'||suffix=='.htm'){
	    		showTipDiag('上传文件类型属于服务器禁止上传类别，请选择其他类型文件！');
                return;
    		}
            var replyFlag=null;
			if(document.getElementById("replyBtn").checked){
				replyFlag = 1;
			}else{
				replyFlag = 0;
			}
			var title = $('#title').val();
	    	if(relationId == -1) {
	    		//先存草稿，再上传
	    		$.ajax({
	    			url: 'menu_newBulletin_postNotice.do',
			        type : 'POST',
			        data:{
			        	bulletinId: relationId,
			        	title: title == ''? '无标题':title,
			        	loseTime:$('#loseTime').val(),
						content: um.getContent(),
						state:0,
						createStaff: staffId,
						replyFlag: replyFlag,
						//不被js过滤器转义< >等特殊字符（即使过滤器不开，对功能没有影响，另外只要检查到这个参数，就不转义，而不管这个参数值是什么）
						inXssWhiteListQ:"true"
			        },
	    	        success: function(msgMap) {
	    	        	if(msgMap.msgFlag) {
	    	        		relationId = msgMap.bulletinId;
//	    	        		$('#previewForm').form('load', msgMap);
//	    	        		alert(relationId);
	    	        		showDialog('公告已成功保存到草稿箱');
	    	        		//上传
	    	        		data.formData =  [
	    		      		    {name:'groupCode',value:groupCode},
	    		    		    {name:'relationId',value:relationId}
	    		    		];
	    		    		data.submit();
	    	        	}
	    	        }
	    		});
	    	} else {
	    		//直接上传
	    		data.formData =  [
	      		    {name:'groupCode',value:groupCode},
	    		    {name:'relationId',value:relationId}
	    		];
	    		data.submit();
	    	}
	    },
	    submit:function(e, data) {
	    	openLoad('上传中...');
	    },
	    done: function (e, data) {
	    	closeLoad();
            $.each(data.result, function (index, file) {
            	if(file.fileSize=="-1"){
            		showDialog('您上传的文件超过大小限制');
            	}else{
            		$("#fileAreaItems").append('<div class="fileItem">'+
		            							'<b class="fileIcon"></b>'+
							                    '<div class="fileName" title="' + file.fileName + '">'+file.fileName+'</div>'+
							                    '<div class="fileSize">'+file.fileSize + '<span class="fileMsg">上传完成</span></div>'+
							                    '<div class="fileDel" onclick="removeFile(this, ' + file.fileId + ');" >删除</div>'+
							                    '</div>')
					                    
            	}
            });
        }
	 });
    
    //发布
	$('#publishBtn').click(function() {
//		$(".btn").removeClass("btn_sp"); 
//		$("#publishBtn").addClass("btn_sp");
		showDialog('发布中...');
		if($('#title').val()==''){
			showDialog('没有公告标题');
			return; 
		}
		var content = um.getContent();
		var replyFlag=null;
		if(document.getElementById("replyBtn").checked){
			replyFlag= 1;
		}else{
			replyFlag= 0;
		}
		$.ajax({
			url: 'menu_newBulletin_postNotice.do',
	        type : 'POST',
	        data:{
	        	bulletinId: relationId,
	        	title: $('#title').val(),
	        	loseTime:$('#loseTime').val(),
				content: um.getContent(),
				state:1,
				createStaff: staffId,
				replyFlag: replyFlag,
				//不被js过滤器转义< >等特殊字符（即使过滤器不开，对功能没有影响，另外只要检查到这个参数，就不转义，而不管这个参数值是什么）
				inXssWhiteListQ:"true"
	        },
	        success: function(msgMap) {
	        	closeLoad();
	        	var href = $('#flag').val()=='new' ? window.location.href : ctx+'/bulletin/menu_draft_draftBox.do';
	        	if(msgMap.msgFlag) {
	        		relationId = msgMap.bulletinId;
	        		showDialog('公告已成功发布');
	        		setTimeout(function() {
	        			window.location.href = href;
	        		}, 1000);
	        	}
	        }
		});
	});
	
	// 预览
	$('#previewBtn').click(function() {
//		$(".btn").removeClass("btn_sp"); 
//		$("#previewBtn").addClass("btn_sp"); 
		var title = $('#title').val();
		$('#previewForm').find('input[name="bulletinId"]').val(relationId);
		$('#previewForm').find('input[name="title"]').val(title);
		$('#previewForm').submit();
	});
	
	// 存草稿
	 $('#saveBtn').click(function(){
//	 	$(".btn").removeClass("btn_sp"); 
//		$("#saveBtn").addClass("btn_sp");
		if($('#title').val()==''){
			showDialog('没有公告标题');
			return; 
		}
		openLoad('保存中...');
	 	var replyFlag=null;
		if(document.getElementById("replyBtn").checked){
			replyFlag = 1;
		}else{
			replyFlag = 0;
		}
		 $.ajax({
			url: 'menu_newBulletin_postNotice.do',
	        type : 'POST',
	        data:{
	        	bulletinId: relationId,
	        	title: $('#title').val(),
	        	loseTime:$('#loseTime').val(),
				content: um.getContent(),
				state:0,
				createStaff: staffId,
				replyFlag: replyFlag,
				//不被js过滤器转义< >等特殊字符（即使过滤器不开，对功能没有影响，另外只要检查到这个参数，就不转义，而不管这个参数值是什么）
				inXssWhiteListQ:"true"
	        },
	        success: function(msgMap) {
	        	closeLoad();
	        	if(msgMap.msgFlag) {
	        		relationId = msgMap.bulletinId;
	        		showDialog('公告已成功保存到草稿箱');
	        	}
	        }
		});
	 });
	 
	 // 重写
	$('#rewriteBtn').click(function() {
//		$(".btn").removeClass("btn_sp"); 
//		$("#saveBtn").addClass("btn_sp");
		openLoad('加载中...');
		setTimeout(function() {
			window.location.href = window.location.href;
		}, 100);
	});
	
	 //返回
	$('#returnBtn').click(function() {
		window.location.href = ctx + '/bulletin/menu_draft_draftBox.do';
	});
	
	
})

//删除附件
function removeFile(srcElement, fileId) {
	if(window.confirm('是否确认删除？')){
		openLoad('删除中...');
		$.ajax({
			type : 'POST',
			url : ctx + '/AttachmentController/delFile.do',
			data : {
				fileId : fileId
			},
			success : function(data) {
				closeLoad();
				if (data == "success") {
					showDialog('文件删除成功');
					$(srcElement).parent('.fileItem').remove();
				} else {
					showDialog('文件删除失败');
				}
			}
		});
	}
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

