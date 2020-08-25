<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="renderer" content="webkit">	
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>预览：
		<c:choose>
			<c:when test="${empty news.title }">无标题</c:when>
			<c:when test="${!empty news.title }">${news.title }</c:when>
		</c:choose>
	</title>
	<link rel="stylesheet" type="text/css" href="${ctx}/res/css/reset.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/news.css" />
</head>
<body>
	<div class="main w980 clearfix"> 
		<div class="newsTitle">
			<c:choose>
				<c:when test="${empty news.title }">无标题</c:when>
				<c:when test="${!empty news.title }">${news.title }</c:when>
			</c:choose>
		</div>
		<div class="newsDetail clearfix">
   			<span>发布人：${news.userName }</span>
   			<span>发布时间：<fmt:formatDate value="${news.createTime}" pattern="yyyy/MM/dd HH:mm:ss" /></span>
    	</div>
    	<div class="cont">
    		${news.content }
   	 	</div>
	</div>
</body>
</html>