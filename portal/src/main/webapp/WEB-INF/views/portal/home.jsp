<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
	<title>首页</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="renderer" content="webkit">	
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<link rel="stylesheet" type="text/css" href="${ctx}/res/css/reset.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/res/unslider/css/unslider.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/res/unslider/css/unslider-dots.css">
	 <link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/home.css" /> 
	
</head>
<body>
	<!--主体内容-->
	<div class="main clearfix">
		<div class="l_boxWrap clearfix">
	    	<!--我的应用-->
	    	<div class="abox box">
	        	<div class="title clearfix"><span class="left">我的应用</span><a href="${ctx}/homePage/menu_main_appMore.do" target="_blank" class="right">更多</a></div>
	            <div class="cont">
	            	<ul id="apps" class="appList clearfix">
	                </ul>
	            </div>
	        </div>
	        <!--我的应用end-->
	        
	    	<!--我的待办-->
	    	<div class="tbox box">
	        	<div class="title clearfix"><span class="left">我的待办</span><a href="##" class="right" id="tRefresh"><i class="refresh"></i>刷新</a></div>
				<div class="cont">
		<%-- 		<div class="clearfix">
									 <div class="wrapsys">
						                <img src="${ctx}/res/images/icon_app1.png">
						                <span class="appname">测试</span>
						        	 </div>
						        	 <div class="wrapModule">
						        	 	<ul class="clearfix"><li>
										<a class="module" href="' '" target="_blank">待办事项测试</a><span class="num">70</span>
									</li>
 </ul></div></div>  --%>
	            </div>
	        </div>
	        <!--我的待办end-->
		</div>
		<div class="r_boxWrap clearfix">
	        <!--新闻-->
	        <div class="nbox box">
	        	<div class="title clearfix">
	        		<span class="left">新闻</span>
	        	<!-- 	<a href="to-news-more.do" target="_blank" class="right">更多</a> -->
	        	</div>
	        	<div class="cont" style="width: 500px; height: 240px" >
			    	<div class="news-slider">
			    		<ul>
				        <c:forEach items="${newsList }" var="news">
			        		<li data-newsid="${news.newsId }">
			        			<img src="${news.coverPhoto }" alt="${news.title }"  style="cursor:pointer" >
			        			<span>${news.digest }</span>
			        		</li>
			        	</c:forEach>
			    		</ul>
			        </div>
	        	</div>
	        </div>
	        <!--新闻end-->
	        
		    <!--系统公告-->
		    <div class="bbox box">
		        <div class="title clearfix"><span class="left">公告</span><a href="menu_main_to-notices-more.do" target="_blank" class="right">更多</a></div>
		        <div class="cont">
		        	<ul id="bulletins" class="newList">
					<c:forEach items="${noticeList }" var="notice" varStatus="status">
						<li <c:if test="${status.index%2 != 0 }">class="odd"</c:if>>
							<a target="_blank" href="${ctx }/bulletin/menu_notice_browseNotice.do?noticeId=${notice.bulletinId }" title="${notice.title }">
							<c:if test="${notice.stickUp==1 }"><b title="置顶公告" class="stick"></b></c:if>${notice.title }
							</a>
							<span>${notice.releaseDate}</span>
						</li>
					</c:forEach>
		            </ul>
		        </div>
		    </div>
			<!--系统公告end-->
			
			<!--天气状况-->
	    <%-- 	<div class="wbox box">
	        	<div class="title clearfix"><span class="left">天气</span><a href="##" class="right" id="wRefresh"><i class="refresh"></i>刷新</a></div>
	            <div class="cont">
	            	<div class="loading" id="wLoading"><img src="${ctx}/res/images/weather/tacticat-loading.gif" /></div><!--加载画面-->
	            	<div class="reg_time">
	                	<a href="####" class="region" id="wCity"></a>
	                    <div class="date">
	                    	<span class="day" id="wDay"></span>
	                        <span class="week" id="wWeek"></span>
	                        <span class="lunar" id="wLunar"></span>
	                    </div>
	                </div>
	                <div class="weather clearfix">
	                	<a href="####" class="left">
	                    	<p class="weathimg"><img id="wImg" src="" width="70" height="59" /></p>
	                        <p id="wTemp"></p>
	                        <p id="wInfo"></p>
	                        <p>&nbsp;</p>
	                    </a>
	                    <div class="right">
	                    	<div class="lwrap"><div class="lnum" id="wPm"></div><div id="wLevel"></div></div>
	                        <p id="wPmDetail"></p>
	                    </div>
	                </div>
	                <div class="remind" id="wRemind"></div>
	            </div>
	        </div>
	        <!--天气状况end--> --%>
		</div>
	</div>

	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/res/unslider/js/unslider-min.js"></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/base.js"></script>
	<script type="text/javascript">
		var ctx = '${ctx }';
		$('.news-slider').unslider({
			autoplay: true,
			delay: 6000,
			speed: 300,
			infinite: true,
			arrows: false
		});
		$('.news-slider li').on('click',function(){
			var newsId = $(this).data('newsid');
			window.open('${ctx}/news/menu_new_browse.do?newsId='+newsId);
		});
	</script>
	<script type="text/javascript" src="${ctx}/res/javascript/portal/home.js" ></script>
</body>
</html>
