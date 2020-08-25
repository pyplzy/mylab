<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
	<title>公告预览：
		<c:choose>
			<c:when test="${empty bulletin.title }">无标题</c:when>
			<c:when test="${!empty bulletin.title }">${bulletin.title }</c:when>
		</c:choose>
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="renderer" content="webkit">	
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<link rel="stylesheet" type="text/css" href="${ctx}/res/css/reset.css" /><!--重置样式-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/pages.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/notice.css" />
</head>
<body>
	<!--头部-->
	<div class="header w980 clearfix">
		<div class="logo left">
	    	<img src="${ctx }/res/theme-${uiInfo.theme }/images/logo.png" class="left" />
	    </div> 
	</div>
	<div class="single clearfix"></div>
	<!--头部end-->
	
	<div class="main w980 clearfix"> 
		<div class="noticeTitle">
			<c:choose>
				<c:when test="${empty bulletin.title }">无标题</c:when>
				<c:when test="${!empty bulletin.title }">${bulletin.title }</c:when>
			</c:choose>
		</div>
		<div class="noticeDetail clearfix">
    		<div class="dtleft"><span>发布人：${bulletin.createName }</span><span>发布时间：${bulletin.createDate }</span></div>
        	<div class="reply">
        		<a href="#rep" class="icon_rep">${total}</a>&nbsp;&nbsp;&nbsp;&nbsp;|
        		<a href="#att" class="icon_bb">${fileSize }</a>
       		</div>
    	</div>
    	<div class="noticeCont">
    		${bulletin.content }
   	 	</div>
   	 	<div class="attach"  id="att">
	    	<div class="hd"><i class="att"></i>附件<span>（${fileSize }个）</span></div>
	        <div class="cont">
	        	<div class="listWrap">
	        		<c:forEach items="${bulletin.files }" var="file">
		            	<div class="items">${file.fileName } <span>(${file.fileSize }K)</span> <a href="${ctx }/AttachmentController/getFile.do?fileId=${file.fileId }">下载</a></div>
					</c:forEach>
	            </div>
	        </div>
    	</div>
	</div>
</body>
</html>