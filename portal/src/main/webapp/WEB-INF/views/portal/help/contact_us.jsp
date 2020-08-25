<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
	<title>联系我们</title>
	<%@ include file="/WEB-INF/views/portal/common/resetandpageslibs.jsp"%>
	<!-- File Upload -->
    <link rel="stylesheet" type="text/css" href="${ctx}/res/jQuery-File-Upload-9.9.3/css/jquery.fileupload.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/res/jQuery-File-Upload-9.9.3/css/jquery.fileupload-ui.css">
	
	<style type="text/css">
		.contact_us {
		    padding: 10px;
		}
		.contact_us dl {
		    line-height: 32px;
		}
		.contact_us dl.half {
		    width: 50%;
		}
		.contact_us a {
		    color: #515151;
		    text-decoration: none;
		}
		.new .textArea{
			width: 640px;
		}
		.pl94 {
		    padding-left: 94px;
		}
	</style>
</head>
<body>
    <div class="wrap border clearfix">
   	    <div class="wholeArea">
	    	<div class="titleN">客户服务</div>
	        <div class="contb contact_us clearfix">
                <dl class="half">
                	<dt>维护电话：</dt>
                    <dd>${contactUsInfo.tel }</dd>
                </dl>
            	<dl class="half">
                	<dt>维护邮箱：</dt>
                    <dd><a href="mailto:${contactUsInfo.mail }"> ${contactUsInfo.mail }</a></dd>
                </dl>                
            	<dl>
                	<dt>公司信息：</dt>
                    <dd>${contactUsInfo.coInfo }</dd>
                </dl>
	        </div>
	    </div>
		<div class="wholeArea">
	    	<div class="titleN">问题反馈</div>
	        <div class="contb p20 clearfix">
	        	<div class="new clearfix">
			    	<dl>
			        	<dt><span style="color:red;">*</span>标题</dt>
			            <dd><input class="txtInput" type="text" id="fb_title"></dd>
			        </dl>
			    	<dl>
			        	<dt><span style="color:red;">*</span>描述</dt>
			            <dd><textarea class="textArea" id="fb_info"></textarea></dd>
			        </dl>
			        <div class="addbox clearfix">
				        <div class="tianj clearfix fb_menu pl94 fileinput-button">
				       		 添加附件(最大<span style="color:red;font-weight:bold;">10M</span>)
				       		<input id="fileupload" name="files[]" data-url="menu_feed_upload.do" type="file">
				        </div>
	        	        <div class="fileArea clearfix" id="fileAreaItems">
	       				 </div>
			        </div>
			        <dl>
                		<dt>&nbsp;</dt>
	                    <dd>
	                    	<a href="####" class="btn btn_sp" id="btnSave"><span class="lf_btn"></span><span class="rf_btn">提交</span></a>
	                    </dd>
	                </dl>
			    </div>
	        </div>
	    </div>
	</div>
	<div id="msgDialog" class="msgDialog"></div>
	<div id="loadDialog" class="loadDialog"><b></b><span></span></div>
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/vendor/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/jquery.iframe-transport.js"></script>
	<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/jquery.fileupload.js"></script>
	<script type="text/javascript">
		var forbidden = ['.exe', '.bat','.sh','.js','.html','.htm'];	//禁止上传的类型
	    $('#fileupload').fileupload({
			dataType: 'json',
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
	    		var suffix = fileName.substring(lastDotIndex).toLowerCase();
	    		if(forbidden.indexOf(suffix) >= 0){
		    		alert('上传文件类型属于服务器禁止上传类别，请选择其他类型文件！');
	                return;
	    		}
	    		//直接上传
	    		data.submit();
		    },
		    submit:function(e, data) {
		    	openLoad('上传中...');
		    },
		    done: function (e, data) {
		    	closeLoad();
		    	var file = data.result;
		    	if(file.fileId){
			    	var fileItem = '<div class="fileItem" data-fileid='+file.fileId+'>'+
										'<b class="fileIcon"></b>'+
					                	'<div class="fileName" title="' + file.fileName + '">'+file.fileName+'</div>'+
					                	'<div class="fileDel" onclick="removeFile(this, ' + file.fileId + ');">删除</div>'+
					                '</div>';
	           		$("#fileAreaItems").append(fileItem);
		    	} else {
		    		alert('上传失败！');
		    	}
	        }
		 });
	    
	    //保存反馈信息
	    $('#btnSave').on('click',function(){
    		var feedbackTitle = $.trim($('#fb_title').val());
    		var feedbackInfo = $.trim($('#fb_info').val());
    		if(feedbackTitle == '' || feedbackInfo == ''){
    			alert("请完整填写标题与反馈内容！")
    			return;
    		}
    		if(feedbackTitle.length > 128){
    			alert("标题过长！")
    			return;
    		}
			if(feedbackInfo.length > 512){
    			alert("反馈内容过长！")
    			return;
    		}
    		var fileIds = [];
    		$('.fileItem').each(function(index, domEle){
    			var fileId = $(this).data('fileid');
    			fileIds.push(fileId);
    		});
    		$.ajax({
    			url: 'menu_feed_saveFeedback.do',
		        type : 'post',
		        data:{
		        	feedbackTitle: feedbackTitle,
		        	feedbackInfo: feedbackInfo,
		        	fileIds: fileIds
		        },
    	        success: function(data) {
    	        	if(data == 'succ') {
    	        		$('#fb_title').val('');
    	        		$('#fb_info').val('');
    	        		$('#fileAreaItems').empty();
    	        		$(".shide").click();
    	        		alert('保存成功');
    	        	} else {
    	        		alert('保存失败');
    	        	}
    	        }
    		});
	    });
	    
	  	//删除附件
	    function removeFile(srcElement, fileId) {
	    	if(window.confirm('是否确认删除？')){
	    		openLoad('删除中...');
	    		$.ajax({
	    			type : 'post',
	    			url :  '${ctx}/AttachmentController/delFile.do',
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
	    function openLoad(msg) {
	    	$('#loadDialog').show().find('span').html(msg);
	    }
	    function closeLoad() {
	    	$('#loadDialog').hide().find('span').html('');
	    }
	    function showDialog(msg) {
	    	$('#msgDialog').html(msg).show();
	    	setTimeout(function() {
	    		$('#msgDialog').hide().html('');
	    	}, 3000);
	    }
	</script>
</body>
</html>
                          