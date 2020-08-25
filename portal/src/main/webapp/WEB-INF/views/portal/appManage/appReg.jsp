<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>
<head>
<title>应用管理-应用注册</title>
<%-- 通用文件，需要在本页面，设定ctx --%>
<%@ include file="/WEB-INF/views/portal/common/commonlibs.jsp"%>
<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${ctx}/res/javascript/common/base.js"></script>
<script type="text/javascript" src="${ctx}/res/javascript/portal/appManage/appReg.js"></script>
<script type="text/javascript">
	var ctx='${ctx}';
</script>
</head>
<body>
	<div class="wrap">
		<ul class="appWrap clearfix">
			<c:forEach items="${apps }" var="temp" varStatus="status">
				<c:choose>
					<%-- 启用 --%>
					<c:when test="${temp.state == 1}">
						<li class="appItem">
							<div class="appPanel  " v='${temp.appCode }'>
								<div class="appIcon hand">
									<img onclick="queryApp(this);" src="${ctx}${temp.appImgPath }" /><span class="icon_lamp"></span>
								</div>
								<p class="appTitle">${temp.appName }</p>
								<p class="appTips">${temp.appMmemo }</p>
							</div>
						</li>
					</c:when>
					<%-- 停用 --%>
					<c:when test="${temp.state == 0   }">
						<li class="appItem">
							<div class="appPanel nonline " v='${temp.appCode }'>
								<div class="appIcon hand ">
									<img onclick="queryApp(this);" src="${ctx}${temp.appImgPath }" class="gray" /><span class="icon_lamp dark "></span>
								</div>
								<p class="appTitle">${temp.appName }</p>
								<p class="appTips">${temp.appMmemo }</p>
							</div>
						</li>
					</c:when>
				</c:choose>
			</c:forEach>

			<li class="appItem">
				<div class="appPanel addApp" onclick="parent.window.showDiv()">
					<div class="appIcon">
						<i class="icon_add"></i>
					</div>
				</div>
			</li>
		</ul>
	</div>

</body>
</html>