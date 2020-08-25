<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
	<title>新建新闻</title>
	<%-- 通用文件，需要在本页面，设定ctx --%>
	<%@ include file="/WEB-INF/views/portal/common/resetandpageslibs.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx}/res/css/addmine.css" /><!--附加样式-->
   	<!-- umeditor -->
	<link type="text/css" rel="stylesheet" href="${ctx }/res/umeditor/themes/default/css/umeditor.css">
    
    <!-- File Upload -->
    <link rel="stylesheet" type="text/css" href="${ctx}/res/jQuery-File-Upload-9.9.3/css/jquery.fileupload.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/res/jQuery-File-Upload-9.9.3/css/jquery.fileupload-ui.css">
	<script type="text/javascript">
		var ctx='${ctx}';
	</script>
</head>
<body>
<div class="wrap">
	<div class="btnWrap">
    	<a href="javascript:void(0)" id="publishBtn" class="btn btn_sp"><span class="lf_btn"></span><span class="rf_btn">发布</span></a>
    	<a href="javascript:void(0)" id="previewBtn" class="btn"><span class="lf_btn"></span><span class="rf_btn">预览</span></a>
    	<a href="javascript:void(0)" id="saveBtn" class="btn"><span class="lf_btn"></span><span class="rf_btn">存草稿</span></a>
    	
    	<c:if test="${empty param.newsId }">
			<a href="javascript:void(0)" id="rewriteBtn" class="btn"><span class="lf_btn"></span><span class="rf_btn">重写</span></a>
			<input id="flag" type="hidden" value="new" />
		</c:if>
		<c:if test="${!empty param.newsId }">
			<a href="javascript:void(0)" id="returnBtn" class="btn"><span class="lf_btn"></span><span class="rf_btn">返回</span></a>
			<input id="flag" type="hidden" value="edit" />
		</c:if>
    </div>
    <div class="new clearfix">
    	<dl>
        	<dt>标题</dt>
            <dd><input type="text" class="txtInput" id="title" value="${news.title }"/></dd>
        </dl>
    	<dl>
        	<dt>摘要</dt>
            <dd><input type="text" class="txtInput" id="digest" value="${news.digest }"/></dd>
        </dl>
    	<dl>
        	<dt>封面图片</dt>
            <dd style="width: 400px; margin-top: 5px;" id="coverWrap">
              	<c:if test="${!empty news.coverPhoto}">
            		<div class="imgWrap"><img src="${news.coverPhoto}"><a class="close"></a></div>
	            	<div class="add_file_div fileinput-button hide">
						<input type="file" id="fileupload" name="upfile" data-url="menu_new_upload.do" />
					</div>
            	</c:if>
              	<c:if test="${empty news.coverPhoto}">
	            	<div class="add_file_div fileinput-button">
	            		<span>文件格式png jpg,文件大小500kB以内，建议尺寸400*270</span>
						<input type="file" id="fileupload" name="upfile" data-url="menu_new_upload.do" />
					</div>
            	</c:if>
            </dd>
            <dd style="line-height: 28px;width: 250px;text-align: right;">
            	<c:if test="${news.replyFlag >0}">
            		<label><input type="checkbox" id="replyBtn" checked="checked"/>开启回复</label>
            	</c:if>
            	<c:if test="${news.replyFlag <=0}">
            		<label><input type="checkbox" id="replyBtn"/>开启回复</label>
            	</c:if>
            </dd>
        </dl>
    </div>
   	<div class="fillWrap">
   		<form id="previewForm" target="_blank" action="menu_new_preview.do" method="post">
			<input name="newsId" type="hidden" />
			<input name="title" type="hidden" />
			<%-- 不被js过滤器转义< >等特殊字符（即使过滤器不开，对功能没有影响，另外只要检查到这个参数，就不转义，而不管这个参数值是什么） --%>
			<input name="inXssWhiteListQ" value="true" type="hidden" />
			<script name="content" type="text/plain" id="myEditor" style="width:100%">${news.content }</script>
		</form>
    </div>
</div>

<div id="msgDialog" class="msgDialog"></div>
<div id="loadDialog" class="loadDialog"><b></b><span></span></div>

<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${ctx}/res/javascript/common/base.js" ></script>
<!-- umeditor -->
<script type="text/javascript" charset="utf-8" src="${ctx }/res/umeditor/umeditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${ctx }/res/umeditor/umeditor.min.js"></script>
<script type="text/javascript" src="${ctx }/res/umeditor/lang/zh-cn/zh-cn.js"></script>

<!-- File Upload -->
<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/jquery.fileupload.js"></script>

<script type="text/javascript">
	var newsId = '${news.newsId }';
</script>
<script type="text/javascript" src="${ctx }/res/javascript/portal/news/new.js"></script>
</body>
</html>