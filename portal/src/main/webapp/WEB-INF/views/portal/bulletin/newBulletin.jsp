<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
<title>公告管理-新建公告</title>
	<%-- 通用文件，需要在本页面，设定ctx --%>
	<%@ include file="/WEB-INF/views/portal/common/resetandpageslibs.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx}/res/css/addmine.css" /><!--附加样式-->
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/base.js" ></script>
	<script type="text/javascript" src="${ctx}/res/plugins/My97DatePicker/WdatePicker.js" ></script><!--时间插件-->
	<!-- umeditor -->
	<link type="text/css" rel="stylesheet" href="${ctx }/res/umeditor/themes/default/css/umeditor.css">
    <script type="text/javascript" charset="utf-8" src="${ctx }/res/umeditor/umeditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctx }/res/umeditor/umeditor.min.js"></script>
    <script type="text/javascript" src="${ctx }/res/umeditor/lang/zh-cn/zh-cn.js"></script>
    <!-- File Upload -->
    <link rel="stylesheet" type="text/css" href="${ctx}/res/jQuery-File-Upload-9.9.3/css/jquery.fileupload.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/res/jQuery-File-Upload-9.9.3/css/jquery.fileupload-ui.css">
    <script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/vendor/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/jquery.iframe-transport.js"></script>
	<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/jquery.fileupload.js"></script>
 
    <script type="text/javascript">
	    	var ctx = '${ctx }';
	    	var staffId = '${sessionScope.staffId }';
	    	var relationId = ${bulletin.bulletinId };
    </script>
    <script type="text/javascript" src="${ctx }/res/javascript/portal/bulletin/newBulletin.js"></script>
</head>
<body>
<div class="wrap">
	<div class="btnWrap">
    	<a href="javascript:void(0)" id="publishBtn" class="btn btn_sp"><span class="lf_btn"></span><span class="rf_btn">发布</span></a>
    	<a href="javascript:void(0)" id="previewBtn" class="btn"><span class="lf_btn"></span><span class="rf_btn">预览</span></a>
    	<a href="javascript:void(0)" id="saveBtn" class="btn"><span class="lf_btn"></span><span class="rf_btn">存草稿</span></a>
    	
    	<c:if test="${empty param.bulletinId }">
					<a href="javascript:void(0)" id="rewriteBtn" class="btn"><span class="lf_btn"></span><span class="rf_btn">重写</span></a>
					<input id="flag" type="hidden" value="new" />
				</c:if>
				<c:if test="${!empty param.bulletinId }">
					<a href="javascript:void(0)" id="returnBtn" class="btn"><span class="lf_btn"></span><span class="rf_btn">返回</span></a>
					<input id="flag" type="hidden" value="edit" />
		</c:if>
    </div>
    <div class="new clearfix">
    	<dl>
        	<dt>发件人</dt>
            <dd><input disabled type="text"  class="txtInput" value="<c:if test="${empty param.relationId }">${sessionScope.userName }
						</c:if>
						<c:if test="${!empty param.relationId }">${bulletin.createName }
						</c:if>"/>
			</dd>
            	
        </dl>
    	<dl>
        	<dt>公告标题</dt>
            <dd><input type="text" class="txtInput" id="title" value="${bulletin.title }"/></dd>
        </dl>
        <div class="addbox clearfix">
	        <div class="tianj clearfix fb_menu pl94 fileinput-button" >
	       		 添加附件(最大<span style="color:red;font-weight:bold;">10M</span>)
	       		<input id="fileupload" type="file" name="files[]" data-url="${ctx }/AttachmentController/upload.do" />
	        </div>
	        <div class="fileArea clearfix" id="fileAreaItems">
	        	<c:forEach items="${bulletin.files }" var="file">
					<div class="fileItem">
    					<b class="fileIcon"></b>
                    	<div class="fileName" title="${file.fileName } ">${file.fileName }</div>
                    	<div class="fileSize">${file.fileSize }<span class="fileMsg">上传完成</span></div>
                    	<div class="fileDel" onclick="removeFile(this,'${file.fileId }' );" >删除</div>
                    </div>
				</c:forEach>
	        </div>
        </div>
    	<dl>
        	<dt>下线时间</dt>
            <dd><input type="text" id="loseTime"  class="txtInput w170" value="${bulletin.loseTime }" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d'})"/></dd>
            <dd>
            	<c:if test="${bulletin.replyFlag >0}">
            		<label><input type="checkbox" id="replyBtn" checked="checked"/>开启回复</label>
            	</c:if>
            	<c:if test="${bulletin.replyFlag <=0}">
            		<label><input type="checkbox" id="replyBtn"/>开启回复</label>
            	</c:if>
            
            </dd>
        </dl>
    </div>
   	<div class="fillWrap">
   		<form id="previewForm" target="_blank" action="menu_myNotice_previewNotice.do" method="post">
				<input name="bulletinId" type="hidden" />
				<input name="title" type="hidden" />
				<%-- 不被js过滤器转义< >等特殊字符（即使过滤器不开，对功能没有影响，另外只要检查到这个参数，就不转义，而不管这个参数值是什么） --%>
				<input name="inXssWhiteListQ" value="true" type="hidden" />
				<script name="content" type="text/plain" id="myEditor" style="width:100%">${bulletin.content }</script>
		</form>
    </div> 
</div>

	<div id="msgDialog" class="msgDialog">
	</div>
	<div id="loadDialog" class="loadDialog">
		<b></b><span></span>
	</div>
</body>
</html>