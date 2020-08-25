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
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/easydropdown.css" /><!--下拉菜单-->
	<style type="text/css">
	.queryWrapForObj dl dd .txtInput {
    	width: 139px;
	}
	.uicTimeTb {
    	width: 120px;
	}
	/* 授予按钮 */
	.onyes1{ display:block; width:55px; height:24px;margin-top: 9px; background:url(../res/images/icon_on_off.png) 0 0 no-repeat;}
	.offno1{ background-position:0 -24px;}
	</style>
</head>
<body>
<div style="padding:0 0 0 10px; background:#fff;">
	<div  class="queryWrapForObj clearfix" >
			<dl>
				<dt>任务</dt>
				<dd>
					<select class="dropdown" id="sysJobList">
						<option value="-1">---请选择---</option>
						<c:forEach items="${jobs}" var="appItem">
							<option value="${appItem.jobId }">${appItem.jobName }</option>
	        			</c:forEach>
					</select>
				</dd>
			</dl>
			<%-- <dl>
			<dt>节点</dt>
			<dd>
				<select class="dropdown" id="sysNodeList">
					<option value="">---请选择---</option>
					<c:forEach items="${nodes}" var="appItem">
							<option value="${appItem.appNodeId }">${appItem.ipAddress} : ${appItem.portValue }</option>
	        			</c:forEach>
				</select>
			</dd>
		</dl> --%>
		<dl>
			<dt>定时器</dt>
			<dd>
				<input type="text" class="txtInput" id="sysTimeName"/>
			</dd>
		</dl>
		<dl>
			<dt>是否启用</dt>
			<dd>
				<select class="dropdown" id="sysEnable">
					<option value="-1">---请选择---</option>
					<option value="1">启用</option>
					<option value="0">停用</option>
				</select>
			</dd>
		</dl>
		&nbsp;&nbsp;
		<a href="##" class="btn" id="sysTimeQry"><span class="lf_btn"></span><span class="rf_btn">查询</span></a>
		
	</div>
    <div class="btnWrap">
    	<a href="javascript:void(0)" id="taskAdd" class="btn btn_sp"><span class="lf_btn"></span><span class="rf_btn">添加</span></a>
    	<a href="javascript:void(0)" id="taskEdit" class="btn"><span class="lf_btn"></span><span class="rf_btn">修改</span></a>
    	<a href="javascript:void(0)" id="taskDel" class="btn"><span class="lf_btn"></span><span class="rf_btn">删除</span></a>
    </div>
    <div class="tnum" id="uicTotalName"></div>
	<div class="tablebox">
		<div class="table_head">
	        <div class="title titlek" style="padding:0;">&nbsp;</div>
	        <div class="title title2" style="width:120px;">定时器名称</div>
	        <div class="title title3" style="width:120px;">节点</div>
	        <div class="title title4" style="width:120px;">运行状态</div>
	        <div class="title title5" style="width:120px;">任务运行时间</div>
	        <div class="title title6" style="width:120px;">业务参数</div>
	        <div class="title title71" style="width:120px;">任务描述</div>
	       	<div class="title title8">&nbsp;</div>
	    </div>
	    <div class="items" id="uicTimesList"></div>
        
	    <!--翻页结束-->
		<div  id="timeRed"></div>
	    <!--翻页end-->
    </div>
    <div class="clearfix"></div>
    <div id="msgDialog" class="msgDialog" style="top:8%;"></div>
	<div id="loadDialog" class="loadDialog">
		<b></b><span></span>
	</div>
</div>
<div class="msgTips" id="uicMsgTips"></div>
<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.ossPaginator.js" ></script>
<!-- My97DatePicker -->
<script type="text/javascript" src="${ctx }/res/plugins/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctx}/res/plugins/easydropdown/jquery.easydropdown.min.js"></script><!--下拉菜单-->
<script type="text/javascript" src="${ctx}/res/javascript/common/base.js" ></script>
<script type="text/javascript" src="${ctx}/res/javascript/portal/system/task.js" ></script>
</body>
</html>