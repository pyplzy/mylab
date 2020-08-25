<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
	<title>
		<c:if test="${empty news.title }">无标题</c:if>
		<c:if test="${!empty news.title }">新闻阅读：${news.title }</c:if>
	</title>
	<%-- 通用文件，需要在本页面，设定ctx --%>
	<%@ include file="/WEB-INF/views/portal/common/commonlibs.jsp"%>
	<link type="text/css" rel="stylesheet" href="${ctx }/res/umeditor/themes/default/css/umeditor.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/ossPaginator.css" />
	<!-- umeditor -->
	<link href="${ctx }/res/umeditor/themes/default/css/umeditor.css" type="text/css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/news.css" />
	<style type="text/css">
	/*  .cont {position:absolute;}  */
 /* 	.cont .imgs{position:absolute;
                bottom:0px;
                right:0px;
}  */
	
	</style>
</head>
	
<body>
<!--头部-->
	<div class="header w980 clearfix">
		<div class="logo left">
	    	<img src="${ctx }/res/theme-${uiInfo.theme }/images/logo.png" class="left" />
	    </div> 
	</div>
	<div class="single clearfix"></div>


	<div class="main w980 clearfix">
		<div class="newsTitle">
			<c:if test="${empty news.title }">无标题</c:if>
			<c:if test="${!empty news.title }">${news.title }</c:if>
		</div>
		<div class="newsDetail clearfix">
   			<span>发布人：${news.userName }</span>
   			<span>发布时间：<fmt:formatDate value="${news.createTime}" pattern="yyyy/MM/dd HH:mm:ss" /></span>
    	</div>
    	<div class="cont">
    		${news.content }
    		<%-- ${news.coverPhoto }  --%>
    		<img alt="${news.title }" src="${news.coverPhoto }" style="width: 400px; margin-top: 5px;" class="imgs"> 
   	 <%-- 	<img alt="${news.title }" src="${news.coverPhoto }" style="width: 400px; margin-top: 5px;" position:relative> --%>
   	 	</div>
		   
		<c:if test="${news.replyFlag==1 }">
	    <div class="repWrap" id="rep">
	    	<div class="hd"><i class="hline"></i>评论列表<span id="total"></span></div>
	        <div class="cont">
	        	<div class="repnews" id="itemContainer" >
	            </div>
	        </div>
	    </div>
	    
		<div id='red' ></div>
	
		<div class="repWrap">
	    	<div class="hd bdbnone"><i class="hline"></i>我要评论</div>
	        <div class="cont bordernone">
	        	<script id="myEditor" class="write" type="text/plain"></script>
	        </div>
	    </div>
     	<a id="publishBtn" class="btn btn_sp right"><span class="lf_btn"></span><span class="rf_btn">立即发表</span></a>
     		
		<div id="msgDialog" class="msgDialog"></div>
		
		<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
	    <script type="text/javascript" charset="utf-8" src="${ctx}/res/umeditor/umeditor.config.js"></script>
	    <script type="text/javascript" charset="utf-8" src="${ctx}/res/umeditor/umeditor.min.js"></script>
	    <script type="text/javascript" src="${ctx}/res/umeditor/lang/zh-cn/zh-cn.js"></script>
		<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.ossPaginator.js"></script>
	    <script type="text/javascript" src="${ctx}/res/javascript/common/base.js"></script>
		<script type="text/javascript">
			var replyFlag='${news.replyFlag}';
			var newsId= '${news.newsId }';
			var readCnt='${news.readCnt}';
			function showDialog(msg) {
				$('#msgDialog').show().html(msg);
				setTimeout(function() {
					$('#msgDialog').hide().html('');
				}, 3000);
			}
			
			queryReplys(1,10,newsId);
			
			var um = UM.getEditor('myEditor',{
								readonly : replyFlag == 0,
								autoHeightEnabled : false,
								pasteImageEnabled:false,
								dropFileEnabled:false,
								initialFrameWidth : '',
								initialFrameHeight : 250,
								toolbar : [
										'undo redo | emotion horizontal forecolor backcolor | bold italic underline strikethrough | superscript subscript | removeformat |',
										'insertorderedlist insertunorderedlist | selectall cleardoc paragraph | fontfamily fontsize',
										'| justifyleft justifycenter justifyright justifyjustify |' ]
			});

			// 发表按钮
			$('#publishBtn').click(function() {
				if (!um.hasContents()) {
					showDialog('回复内容不能为空！');
					return;
				}
				if (um.getContent().length > 500) {
					showDialog('回复内容过长！');
					return;
				}
				var reply = {
					newsId : newsId,
					content : um.getContent(),
					// 不被js过滤器转义<
					// >等特殊字符（即使过滤器不开，对功能没有影响，另外只要检查到这个参数，就不转义，而不管这个参数值是什么）
					inXssWhiteListQ : "true"
				};
				$.ajax({
					type : 'POST',
					url :  '${ctx}/news/publish.do',
					data : reply,
					success : function(msgMap) {
						if (msgMap.flag) {
							showDialog('回复发表成功 ');
							setTimeout(function() {
								window.location.reload(true);
							}, 1000);
						} else {
							showDialog('回复发表失败 ');
						}
					}
				});
			});

			function queryReplys(page, rows, newsId) {
				var str = "";
				$.ajax({
					type : 'POST',
					url : 'menu_new_queryReplys.do',
					async : false,
					data : {
						'newsId' : newsId,
						'page' : page,
						'rows' : rows
					},
					success : function(data) {
						if (data.replys) {
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
								str += '<div class="items">'
										+ '<div class="author clearfix">'
										+ '<div class="left">' + v.userName + '<span>['
										+ v.orgName + ']</span>：</div>'
										+ '<div class="right">' + v.replyTime + '发表</div>'
										+ '</div>' + '<div class="detail">'
										+ v.content + '</div>' + '</div>';
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
		</script>
		</c:if>
	</div>
</body>
</html>