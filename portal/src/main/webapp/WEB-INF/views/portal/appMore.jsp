<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
<title>应用概览</title>
<%-- 通用文件，需要在本页面，设定ctx --%>
<%@ include file="/WEB-INF/views/portal/common/commonlibs.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/res/css/addmineEX.css" /> <!--附加样式-->
</head>
<body>
	<!--头部-->
	<div class="header w980 clearfix">
		<div class="logo left">
			<img src="${ctx}/res/theme-${uiInfo.theme }/images/logo.png" class="left" />
		</div> 
	</div>
	<div class="single clearfix"></div>
	<!--头部end-->

	<!--主体内容-->
	<div class="main w980 clearfix">
		<div class="hdTitle"><span>应用概览</span></div> 
		<ul class="appWrap clearfix" id="apps">
		</ul>
	</div>
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/base.js" ></script>
	<script type="text/javascript">
	$(function() {
		//查更多应用
		$.ajax({
			type: 'post',
			url: 'menu_main_appMore.do',
			async: false,
			success : function(data) {
				if(data) {
					var str2="";
					//app计数
					var appCount=0;
					$.each(data,function(i,v) {
						appCount++;
						str2+='<li class="appItem1"><div class="appPanel glan"><a href="'+v.appUrl+'" target="_blank"><div class="appIcon"><img src="${ctx}'+v.appImgPath+'" /></div></a>';
					/* 	if(v.appMmemo==''){//应用备注为空，人性化提示
							str2+='<p class="appTips">管理员很懒，什么描述都没写</p>';
						}else{
							str2+='<p class="appTips">'+v.appMmemo+'</p>';
						} */
						if(v.userGuidePath==''){//用户手册为空，人性化提示
							str2+='<p class="appTitle"><a href="####">暂无用户手册</a></p></div>';
						}else{
							str2+='<p class="appTitle"><a href="'+v.userGuidePath +'">用户手册</a></p></div>';
						}
			            str2+=  '<p class="appName">'+v.appName +'</p></li>';
					});
					//如果我的应用不足10个，用虚线框占位（有for循环其实都不需要if语句）
					if(appCount<10){
						for(var i=0;i<10-appCount;i++){
							str2+='<li class="appItem1"><div class="appPanel glan bddash bg "></div></li>';
						}
					}
					$("#apps").append(str2);
				}	
			}
		});
	});
	</script>
</body>
</html>
