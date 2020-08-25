<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
<title>公告管理-草稿箱</title>
	<%-- 通用文件，需要在本页面，设定ctx --%>
	<%@ include file="/WEB-INF/views/portal/common/resetandpageslibs.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx}/res/css/addmine.css" /><!--附加样式-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/ossPaginator.css" /><!--分页样式-->
 	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/base.js" ></script>
	<script>
			var ctx = '${ctx }';
			var staffId = '${sessionScope.staffId }';
	</script>
	<script type="text/javascript" src="${ctx}/res/javascript/portal/bulletin/draftBox.js" ></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.ossPaginator.js" ></script>
</head>
<body>
<div class="wrapOther">
	<div class="btnWrap">
    	<div class="choose" ><input type="checkbox" id="choose" /></div>
    	<a href="javascript:void(0)" class="btn" id="removeBtn"><span class="lf_btn"></span><span class="rf_btn">删除</span></a>
    	<a href="javascript:void(0)" class="btn btn_sp" id="publishBtn"><span class="lf_btn"></span><span class="rf_btn">发布</span></a>
    	<a href="javascript:void(0)" class="btn" id="reloadBtn"><span class="lf_btn"></span><span class="rf_btn">刷新</span></a>
    </div>
    <div class="tnum" id="totalName"></div>
    <div class="tablebox">
    	<table class="items" width="100%" border="0" cellspacing="0" cellpadding="0">
        </table>
    </div>
   	<!--翻页结束-->
		<div  id="red" style="float:left;"></div>
   	<!--翻页end-->
    <div class="h30 clearfix"></div>
    <div id="msgDialog" class="msgDialog">
	</div>
	<div id="loadDialog" class="loadDialog">
		<b></b><span></span>
	</div>
   
</div>
</body>
</html>