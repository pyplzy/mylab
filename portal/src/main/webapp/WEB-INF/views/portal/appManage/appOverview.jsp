<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
<title>应用管理-应用概览</title>
<%-- 通用文件，需要在本页面，设定ctx --%>
<%@ include file="/WEB-INF/views/portal/common/resetandpageslibs.jsp"%>
<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${ctx}/res/javascript/common/base.js" ></script>
</head>
<body>
<div class="wrap">
	<ul class="appWrap clearfix">
		
		<c:forEach items="${apps }" var="temp" varStatus="status">	
	    	<li class="appItem1">
		    	<c:choose>
					<c:when test="${temp.state eq 0   }">
		           		<div class="appPanel nonline glan">
		           			<div class="appIcon"><img src="${ctx}${temp.appImgPath }" class="gray" /></div>
		           			<p class="appTips">${temp.appMmemo }</p>
	                		<p class="appTitle"><a href="${temp.userGuidePath }">用户手册</a></p>
	            			<p class="appName">${temp.appName }</p>
	            		</div>
					</c:when>
					<c:when test="${temp.state eq 1}">
						<div class="appPanel glan">
							<div class="appIcon"><img src="${ctx}${temp.appImgPath }" /></div>
		           			<p class="appTips">${temp.appMmemo }</p>
	                		<p class="appTitle"><a href="${temp.userGuidePath }">用户手册</a></p>
	            		</div>
					</c:when>
				</c:choose>
				<p class="appName">${temp.appName }</p>
	        </li>
	   	</c:forEach>  
    </ul>
</div>
</body>
</html>
