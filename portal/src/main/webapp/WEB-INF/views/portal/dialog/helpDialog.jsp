<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="popWrap" id="help">
	<div class="titleName clearfix"><span class="left">反馈处理</span><i class="icon_close right"></i></div>
    <div class="conto clearfix">
    	<dl>
        	<dt>反馈信息</dt>
            <dd><textarea name="feedbackInfo" class="textArea" readonly="readonly"></textarea></dd>
        </dl>
    	<dl class="item_wrap">
        	<dt>附件</dt>
            <dd></dd>
        </dl>
    	<dl>
        	<dt><span style="color:red;">*</span>问题定性</dt>
            <dd>
            	<div class="w300">
                    <select class="dropdown" name="questionType">
						<option value="1">软件BUG</option>
						<option value="2">功能需求</option>
						<option value="3">优化建议</option>
                    </select>
                </div>
            </dd>
        </dl>
        <dl>
        	<dt><span style="color:red;">*</span>回复</dt>
            <dd><textarea name="replyContent" class="textArea"></textarea></dd>
        </dl>
    	<dl class="mt5">
        	<dt>&nbsp;</dt>
            <dd>
            	<a href="####" class="btn btn_sp"><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
            </dd>
        </dl>
    </div>
</div>
<script type="text/javascript">
	var help = (function(){
		$('#help').find('.icon_close').on('click',function(){closeAndclear()});
		$('#help').find('a.btn_sp').on('click',function(){save()});
		
		var feedback_id = null;
		
		var showDialog = function(feedbackId){
			feedback_id = feedbackId;
			$("#mask").height($(window).height()).width($(window).width()).show();
			$("#help").show();
			$.ajax({
				type: 'post',
				url: '${ctx}/help/menu_feed_getFeedbackDetail.do',
				dataType:"json",
				data:{
					'feedbackId':feedbackId
				},
				success : function(data) {
					$('#help').find('textarea[name="feedbackInfo"]').val(data.feedbackInfo);
					$('#help').find('textarea[name="replyContent"]').val(data.replyContent);
					$('#help').find('select').easyDropDown('select',data.questionType? data.questionType.toString() : 0);
					if(data.state == 1){
						$('#help').find('.btn_wrap').hide();
					} else {
						$('#help').find('.btn_wrap').show();
					}
					if(data.files.length>0){
						var filestr = '';
						for(var i=0;i<data.files.length;i++){
							filestr += '<a href="'+data.files[i].absolutepath+'" target="_blank">'+data.files[i].fileName+'</a>'
						}
						$('#help').find('.item_wrap dd').html(filestr).parent().show();
					} else {
						$('#help').find('.item_wrap').hide();
					}
				}
			});
		}
		var closeAndclear = function(){
			$('#help').find('textarea[name="feedbackInfo"]').val('');
			$('#help').find('textarea[name="replyContent"]').val('');
			$('#help').find('select').easyDropDown('select',0);
			$('#help').find('.item_wrap dd').empty();
			$('#help').find('.btn_wrap').hide();
			$("#mask").hide();
			$("#help").hide();
			feedback_id = null;
		}
		var save = function(){
			var questionType = $('#help').find('select').val();
			var replyContent = $.trim($('#help').find('textarea[name="replyContent"]').val());
			if(replyContent == ''){
				alert('请填写问题回复');
				return;
			}
			if(replyContent.length > 512){
				alert('问题回复中最多填写512个字');
				return;
			}
			$.ajax({
				type: 'post',
				url: '${ctx}/help/menu_feed_saveHandle.do',
				dataType:"text",
				data:{
					'feedbackId':feedback_id,
					'questionType':questionType,
					'replyContent':replyContent
				},
				success : function(data) {
					alert('保存成功');
					closeAndclear();
					window.frames['mainFrame'].qry(1,10);	//刷新页面
				},
				error: function(){
					alert('保存失败');
				}
			});
		}
		return {
			showDialog : showDialog
		};
	})();
</script>
</html>