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
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/base.js" ></script>
	<!-- My97DatePicker -->
    <script type="text/javascript" src="${ctx }/res/plugins/My97DatePicker/WdatePicker.js"></script>
	<script>
			var ctx = '${ctx }';
			var staffId = '${sessionScope.staffId }';
	</script>
	<script type="text/javascript" src="${ctx}/res/javascript/portal/bulletin/myNotice.js" ></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.ossPaginator.js" ></script>
</head>
<body>
<div class="wrapOther">
	<div class="btnWrap">
    	<a href="javascript:void(0)" class="btn" id="removeBtn"><span class="lf_btn"></span><span class="rf_btn">删除</span></a>
    	<a href="javascript:void(0)" class="btn btn_look" id="lookBtn"><span class="lf_btn"></span><span class="rf_btn">查看<i></i></span></a>
        <div class="pop_view clearfix">
        	<div class="viewItem" id="lookAllBtn">全部</div>
        	<div class="viewItem" id="lookOnBtn">在线公告</div>
        	<div class="viewItem" id="lookOffBtn">下线公告</div>
        </div>
    </div>
    <div class="tnum" id="totalName"></div>
    
    <div class="tablebox">
    	<div class="table_head">
        	<div class="title title1"><input type="checkbox" class="checkbtn" id="choose"/></div>
            <div class="title titlek">&nbsp;</div>
            <div class="title title2">标题</div>
            <div class="title title3">&nbsp;</div>
            <div class="title title4">下线时间</div>
            <div class="title title5">浏览数</div>
            <div class="title title6">回复数</div>
            <div class="title title7">&nbsp;</div>
            <div class="title title8">&nbsp;</div>
        </div>
        <div class="items">
        </div>
        <!--翻页结束-->
		<div  id="red" style="float:left;"></div>
        <!--翻页end-->
    </div>
    <div class="h30 clearfix"></div>
    <div id="msgDialog" class="msgDialog">
		</div>
		<div id="loadDialog" class="loadDialog">
			<b></b><span></span>
	</div>
	<div id="publishBox" class="msgbox">
		<div class="msgbox-hd">
			<span class="msgbox-title"></span>
			<b class="msgbox-close" onclick="closeMsgbox();"></b>
		</div>
		<div class="msgbox-bd">
			<div class="msgbox-normal">
				<div class="msgbox-fm">
					<div class="label">输入下线时间</div>
					<div class="ipt">
						<input id="loseTime" class="ipt-input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d'});" class="Wdate" readonly="readonly" />
					</div>
				</div>
				<div></div>
			</div>
		</div>
		<div class="msgbox-ft">
			<div class="desc">
				默认不选，永不失效
			</div>
			<div class="menu" onclick="closeMsgbox();">取&nbsp;消</div>
			<div class="menu first" onclick="submitMsgbox();">确&nbsp;定</div>
		</div>
	</div>
	<div class="mask" id="maskItem"></div>
</div>
</body>
</html>