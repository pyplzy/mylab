<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
	<title>
		新闻阅读：
		<c:choose>
			<c:when test="${empty news.title }">无标题</c:when>
			<c:when test="${!empty news.title }">${news.title }</c:when>
		</c:choose>
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="renderer" content="webkit">	
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<link rel="stylesheet" type="text/css" href="${ctx}/res/css/reset.css" /><!--重置样式-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/pages.css" />
	<link type="text/css" rel="stylesheet" href="${ctx }/res/umeditor/themes/default/css/umeditor.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/ossPaginator.css" />
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
	<!-- 	整个外面的div宽度是980，在main w980里规定了 -->
		<div class="newsDetail clearfix">
	    	<div>
	    		<span>发布人：${news.userName }</span>
	    		<span>发布时间：<fmt:formatDate value='${news.releaseTime }' pattern='yyyy-MM-dd HH:mm:ss'/></span>
	    	</div>
	        <div class="reply">
	        	<a href="#rep" class="icon_rep">0</a>
	        	&nbsp;&nbsp;&nbsp;&nbsp;
	        </div>
	    </div>
	    <div class="cont">
	    	${news.content }
	    </div>
	    <div class="repWrap" id="rep">
	    	<div class="hd"><i class="hline"></i>评论列表<span id="total"></span></div>
	        <div class="cont">
	        	<div class="repnews" id="itemContainer" >
	            </div>
	        </div>
	    </div>
	    
		<div id='red' ></div>
	
		<div id="msgDialog" class="msgDialog11">
		</div>
		<div id="loadDialog" class="loadDialog">
			<b></b><span></span>
		</div>
	</div>
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.ossPaginator.js"></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/base.js"></script>
	
	<script type="text/javascript">
		var newsId = '${news.newsId }';
		var readCnt='${news.readCnt}';
		
		function queryReplys(page, rows, newsId) {
			$.ajax({
				type : 'post',
				url : 'menu_new_queryReplys.do',
				data : {
					'newsId' : newsId,
					'page' : page,
					'rows' : rows
				},
				success : function(data) {
					if (data.replys) {
						var str = "";
						if (data.pageObj) {
							$("#total").empty();
							$("#total").append(
									'参与<b class="red">' + readCnt
											+ '</b>人，跟帖<b class="red">'
											+ data.pageObj.total + '</b>条');
							$('.icon_rep').text(data.pageObj.total);
						}
						$('#itemContainer').empty();
						$.each(data.replys, function(i, v) {
							str += '<div class="items">'+
									  '<div class="author clearfix">'+
									      '<div class="left">' + 
									        v.userName + '<span>['+v.orgName + ']</span>：'+
										  '</div>'+
										  '<div class="right">' + v.replyTime + '发表</div>'+
									   '</div>' +
									   '<div class="detail">'+v.content + '</div>'+
								   '</div>';
						});
						$("#itemContainer").append(str);

						$('#red').ossPaginator({
							totalrecords : data.pageObj.total,
							recordsperpage : 10,
							length : 4,
							next : '下一页',
							prev : '上一页',
							first : '首页',
							last : '尾页',
							initval : page,// 初始化哪一页被选中
							controlsalways : true,
							onchange : function(newPage) {
								queryReplys(newPage, 10, newsId);
							}
						});
					}
				}
			});
		}
		
		queryReplys(1,10,newsId);
	</script>
</body>
</html>