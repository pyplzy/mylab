<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
	<head>
		<title>
			公告阅读：
			<c:choose>
				<c:when test="${empty notice.title }">无标题</c:when>
				<c:when test="${!empty notice.title }">${notice.title }</c:when>
			</c:choose>
		</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="renderer" content="webkit">	
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<link rel="stylesheet" type="text/css" href="${ctx}/res/css/reset.css" /><!--重置样式-->
		<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/pages.css" /><!--单页面样式-->
		<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/notice.css" /><!--单页面样式-->

		<link type="text/css" rel="stylesheet" href="${ctx }/res/umeditor/themes/default/css/umeditor.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/ossPaginator.css" /><!--分页样式-->
		<!-- umeditor -->
		<link href="${ctx }/res/umeditor/themes/default/css/umeditor.css" type="text/css" rel="stylesheet">
	</head>
<body>
	<!--头部-->
	<div class="header w980 clearfix">
		<div class="logo left">
	    	<img src="${ctx }/res/theme-${uiInfo.theme }/images/logo.png" class="left" />
	    </div> 
	</div>
	<div class="single clearfix"></div>
	<!--头部end-->
	<div class="main w980 clearfix"> 
		<div class="noticeTitle">
			<c:choose>
				<c:when test="${empty notice.title }">
					无标题
				</c:when>
				<c:when test="${!empty notice.title }">
					${notice.title }
				</c:when>
			</c:choose>
		</div>
		<div class="noticeDetail clearfix">
	    	<div class="dtleft"><span>发布人：${notice.createName }</span><span>发布时间：${notice.releaseDate }</span></div>
	        <div class="reply">
	        	<a href="#rep" class="icon_rep">${total}</a>&nbsp;&nbsp;&nbsp;&nbsp;|
	        	<a href="#att" class="icon_bb">${fileSize }</a>
	        </div>
	    </div>
	    <div class="noticeCont">
	    	${notice.content }
	    </div>
	<c:if test="${fileSize>0 }">
	    <div class="attach" id="att">
	    	<div class="hd"><i class="att"></i>附件<span>（${fileSize }个）</span></div>
	        <div class="cont">
	        	<div class="listWrap">
	        		<c:forEach items="${notice.files }" var="file">
		            	<div class="items">${file.fileName } <span>(${file.fileSize }K)</span> <a href="${ctx }/AttachmentController/getFile.do?fileId=${file.fileId }">下载</a></div>
					</c:forEach>
	            </div>
	        </div>
	    </div>
	</c:if>
	    <div class="repWrap" id="rep">
	    	<div class="hd"><i class="hline"></i>评论列表<span id="total">已阅<b class="red">${notice.readCnt }</b>人，评论<b class="red">${total}</b>条</span></div>
	        <div class="cont">
	        	<div class="repnews" id="itemContainer" >
	<c:forEach items="${replys }" var="reply" varStatus="status">
	            	<div class="items">
	                	<div class="author clearfix">
	                    	<div class="left">${reply.userName } <span>[${reply.orgName }部门]</span>：</div>
	                        <div class="right">${reply.replyTime } 发表</div>
	                    </div>
	                    <div class="detail">${reply.replyContent }
						</div>
	                </div>
	</c:forEach>
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
	     <a id="publishBtn"   class="btn btn_sp right"><span class="lf_btn"></span><span class="rf_btn">立即发表</span></a>
	     
		<div id="msgDialog" class="msgDialog11">
		</div>
		<div id="loadDialog" class="loadDialog">
			<b></b><span></span>
		</div>
	</div>
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.ossPaginator.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctx}/res/umeditor/umeditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctx}/res/umeditor/umeditor.min.js"></script>
    <script type="text/javascript" src="${ctx}/res/umeditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript" src="${ctx}/res/javascript/common/base.js"></script>
	<script type="text/javascript">
   	var noticeId = '${notice.bulletinId }';
   	var staffId = '${sessionScope.staffId }';
   	var replyFlag = '${notice.replyFlag }';
   	var total=${total};
   	var readCnt="${notice.readCnt}";
	function showDialog(msg) {
		$('#msgDialog').show().html(msg);
		setTimeout(function() {
			$('#msgDialog').hide().html('');
		}, 3000);
	}
	
	$('#red').ossPaginator({
		totalrecords : total,
		recordsperpage : 10,
		length : 4,
		next : '下一页',
		prev : '上一页',
		first : '首页',
		last : '尾页',
		initval : 1,// 初始化哪一页被选中
		controlsalways : true,
		onchange : function(newPage) {
			queryReplys(newPage, 10, noticeId);
		}
	});

	var um = UM.getEditor('myEditor', {
			readonly : replyFlag == 0,
			autoHeightEnabled : false,
			pasteImageEnabled: false,
			dropFileEnabled: false,
			initialFrameWidth : '',
			initialFrameHeight : 250,
			// autoHeightEnabled:false,
			toolbar : [
					'undo redo | emotion horizontal forecolor backcolor | bold italic underline strikethrough | superscript subscript | removeformat |',
					'insertorderedlist insertunorderedlist | selectall cleardoc paragraph | fontfamily fontsize',
					'| justifyleft justifycenter justifyright justifyjustify |' ]
		});

	// 发表按钮
	$('#publishBtn').click(function() {
		if (replyFlag == 0) {
			showDialog('回复功能被关闭！');
			return;
		}
		if (!um.hasContents()) {
			showDialog('回复内容不能为空！');
			return;
		}
		if (um.getContent().length > 500) {
			showDialog('回复内容过长！');
			return;
		}
		var reply = {
			bulletinId : noticeId,
			staffId : staffId,
			replyContent : um.getContent(),
			// 不被js过滤器转义<
			// >等特殊字符（即使过滤器不开，对功能没有影响，另外只要检查到这个参数，就不转义，而不管这个参数值是什么）
			inXssWhiteListQ : "true"
		};
		$.ajax({
			type : 'POST',
			url : '${ctx }/noticeReply/publish.do',
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

	function queryReplys(page, rows, bulletinId) {
		var str = "";
		$.ajax({
			type : 'POST',
			url : 'menu_notice_queryReplys.do',
			async : false,
			data : {
				'bulletinId' : noticeId,
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
					}
					$('#itemContainer').empty();
					$.each(data.replys, function(i, v) {
						str += '<div class="items">'
								+ '<div class="author clearfix">'
								+ '<div class="left">' + v.userName + '<span>['
								+ v.orgName + '部门]</span>：</div>'
								+ '<div class="right">' + v.replyTime + '发表</div>'
								+ '</div>' + '<div class="detail">'
								+ v.replyContent + '</div>' + '</div>';
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
							queryReplys(newPage, 10, noticeId);
						}
					});
				}
			}
		});
	}
	</script>
</body>
</html>