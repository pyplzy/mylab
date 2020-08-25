<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
<title>我的公告</title>
	<%-- 通用文件，需要在本页面，设定ctx --%>
	<%@ include file="/WEB-INF/views/portal/common/resetandpageslibs.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx}/res/css/addmine.css" /><!--附加样式-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/ossPaginator.css" /><!--分页样式-->
</head>
<body>
<div style=" padding:0 0 0 10px; background:#fff;">
	<div style="padding:8px 20px;height:28px;background:#fafafa;">
		<dl>
			<dt>发布时间</dt>
			<dd><input type="text" id="releaseStartTime" class="txtInput" style="border:1px solid #d3d3d3;height:16px;width:145px;" onClick="WdatePicker({readOnly: true,maxDate:'#F{$dp.$D(\'releaseEndTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></dd>
			<dt>&nbsp;&nbsp;至</dt>
			<dd><input type="text" id="releaseEndTime" class="txtInput" style="border:1px solid #d3d3d3;height:16px;width:145px;" onClick="WdatePicker({readOnly: true,minDate:'#F{$dp.$D(\'releaseStartTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></dd>
			<dt>&nbsp;</dt>
			<dd><input class="txtInput" style="height:16px;width:190px;" type="text" id="newsName" placeholder="请输入新闻标题" onfocus="this.select()"/></dd>
			<dt>&nbsp;</dt>
			<dd><a href="javascript:void(0)" class="btn" id="qryBtn"><span class="lf_btn"></span><span class="rf_btn">查询</span></a></dd>
		</dl>
	</div>
    <div class="tnum" id="totalName"></div>
    
	<div class="tablebox">
		<div class="table_head">
	        <div class="title titlek" style="padding:0;">&nbsp;</div>
	        <div class="title title2">标题</div>
	        <div class="title title3">&nbsp;</div>
	        <div class="title title4">发布时间</div>
	        <div class="title title5">浏览数</div>
	        <div class="title title6">回复数</div>
	        <div class="title title71">发布人</div>
	       	<div class="title title72">&nbsp;</div>
	       	<div class="title title8">&nbsp;</div>
	    </div>
	    <div class="items"></div>
        
	    <!--翻页结束-->
		<div  id="red"></div>
	    <!--翻页end-->
    </div>
    <div class="clearfix"></div>
    <div id="msgDialog" class="msgDialog" style="top:8%;"></div>
	<div id="loadDialog" class="loadDialog">
		<b></b><span></span>
	</div>
</div>
<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.ossPaginator.js" ></script>
<!-- My97DatePicker -->
<script type="text/javascript" src="${ctx }/res/plugins/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	var staffId = '${sessionScope.staffId }';
</script>
<script type="text/javascript" src="${ctx}/res/javascript/common/base.js" ></script>
<script type="text/javascript" src="${ctx}/res/javascript/portal/news/manage.js" ></script>
</body>
</html>