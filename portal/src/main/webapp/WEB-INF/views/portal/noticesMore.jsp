<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
<title>系统公告</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="renderer" content="webkit">	
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" type="text/css" href="${ctx}/res/css/reset.css" /><!--重置样式-->
<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/pages.css" /><!--单页面样式-->

<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/ossPaginator.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/notice.css" />
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
    <!--系统公告-->
    <div class="box">
        <div class="hdTitle"><span>系统公告</span></div>
        <div class="cont">
        	<ul class="newList" id="bulletins" >
            </ul>
            <!--翻页结束-->
             <div id="red" style="float:left;"></div>
    </div>
	<!--系统公告end-->
	</div>
</div>

<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js" ></script>
<script type="text/javascript" src="${ctx}/res/javascript/common/base.js" ></script>
<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.ossPaginator.js" ></script>
<script type="text/javascript" >
	function getNotices(page,rows){
		var str="";
		$.ajax({
			type: 'POST',
			url: 'menu_main_notices-more.do',
			dataType : 'json',
			async: false,
			data:{  
				'page':page,
				'rows':rows
			},
			success : function(data) {
				if(data.bulletinDtos) {
					if(data.pageObj){
						$(".tnum").empty();
						$(".tnum").append("共"+data.pageObj.total+"条记录 ");
					}
					$('#bulletins').empty();
					$.each(data.bulletinDtos,function(i,v) {
						str+=
							'<li>';
							if(v.stickUp==1){
								str+='<a target="_blank" href="${ctx }/bulletin/menu_notice_browseNotice.do?noticeId='+v.bulletinId+'" title="'+v.title+'">'+'<b title="置顶公告" class="stick"></b>'+v.title+'</a>';
							}else{
								str+='<a target="_blank" href="${ctx }/bulletin/menu_notice_browseNotice.do?noticeId='+v.bulletinId+'" title="'+v.title+'">'+v.title+'</a>';
							}
						str+='<div class="auther">'+v.createName+'</div>'+
		                    '<span >';
			            if(v.fileNum!='0'){
			            	str+='<span><i class="fujian" title="此公告包含附件"></i>'+v.releaseDate+'</span>';
			            }else{
			            	str+='<span>'+v.releaseDate+'</span>';
			            }
			          str+='</li>';
					});
					$("#bulletins").append(str);
					
					$('#red').ossPaginator({
						totalrecords: data.pageObj.total, 
						recordsperpage: 10, 
						length: 4, 
						next: '下一页', 
						prev: '上一页', 
						first: '首页', 
						last: '尾页', 
						initval: page,//初始化哪一页被选中
						controlsalways: true,
						onchange: function (newPage) {
							getNotices(newPage,10);
				    	}
					});
				}	
			}
		});
	}
	
	getNotices(1,10);
	</script>

</body>
</html>
