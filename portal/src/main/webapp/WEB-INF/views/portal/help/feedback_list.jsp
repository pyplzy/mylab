<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
	<title>问题反馈查询</title>
	<%@ include file="/WEB-INF/views/portal/common/resetandpageslibs.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx}/res/css/addmine.css" /><!--附加样式-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/ossPaginator.css" /><!--分页样式-->
</head>
<body>
	<div class="wrap border clearfix">
		<div class="wholeArea">
			<div class="queryWrap clearfix">
				<dl>
					<dt>标题</dt>
					<dd>
						<input type="text" class="txtInput" id="feedbackTitle" />
					</dd>
				</dl>
				<dl>
					<dt>反馈人姓名</dt>
					<dd>
						<input type="text" class="txtInput" id="userName" />
					</dd>
				</dl>
				<dl>
					<dt>起始时间</dt>
					<dd>
						<input type="text" id="startTime" class="txtInput" readonly="readonly" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false})" />
					</dd>
				</dl>
				<dl>
					<dt>结束时间</dt>
					<dd>
						<input type="text" id="endTime" class="txtInput" readonly="readonly" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false})" />
					</dd>
				</dl>
				<a href="##" class="btn" id="QryBtn" style="float: left;"><span class="lf_btn"></span><span class="rf_btn">查询</span></a>
			</div>
			<div class="queryTable">
				<table style="width:100%;">
					<thead>
						<tr>
							<th width="20">序号</th>
							<th width="80">标题</th>
							<th width="160">描述</th>
							<th width="50">反馈人</th>
							<th width="80">反馈时间</th>
							<th width="30">状态</th>
						</tr>
					</thead>
					<tbody id="QryResult">
					</tbody>
				</table>
			</div>
			<%--登录日志分页 --%>
			<div id="QryPage" style="float:left;"></div>
		</div>
	</div>
	<%--提示框 --%>
	<div id="msgDialog" class="msgDialog"></div>
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/util.js"></script>
    <script type="text/javascript" src="${ctx }/res/plugins/My97DatePicker/WdatePicker.js"></script><!--时间插件-->
    <script type="text/javascript" src="${ctx}/res/javascript/common/jquery.ossPaginator.js" ></script>
    <script type="text/javascript">
	   $('#startTime').val(new Date().format("yyyy-MM-dd") + ' 00:00:00');
	   $('#endTime').val(new Date().format("yyyy-MM-dd") + ' 23:59:59');
		//登录日志查询按钮绑定click事件
		$('#QryBtn').on('click',function(){
			//默认查第一页10行数据
			qry(1,10);
		});
		$('#QryResult').on('click','.fb_detail',function(){
			var feedbackId = $(this).data('id');
			parent.help.showDialog(feedbackId);
		});
	
		function qry(page,rows){
			var startTime = new Date($('#startTime').val().replace(/-/g, '/'));
			var endTime = new Date($('#endTime').val().replace(/-/g, '/'));
			if(startTime.getFullYear() != endTime.getFullYear() || startTime.getMonth() != endTime.getMonth()){
			  alert('查询时间不能跨月!');
			  return;
			}
			showTip('查询中，请稍候...！');
			$.ajax({
				type: 'post',
				url: 'menu_feed_getFeedbackList.do',
				async: false,//因为要实时改变分页数据，只能同步请求
				dataType:"json",
				data:{
					'page':page,
					'rows':rows,
					'feedbackTitle':$.trim($('#feedbackTitle').val()),
					'userName':$.trim($('#userName').val()),
					'startTime':$('#startTime').val(),
					'endTime':$('#endTime').val()
				},
				success : function(data) {
					//隐藏提示
					hideTip();
					if(data.list) {
						$('#QryResult').empty();
						var str = '';
						$.each(data.list,function(i,v) {
							str+='<tr><td>'+(i+1)+'</td>';
							str+='<td><a href="##" class="fb_detail" data-id='+v.feedbackId+' title="'+v.feedbackTitle+'">'+v.feedbackTitle+'</a></td>';
							str+='<td>'+v.feedbackInfo+'</td>';
							str+='<td>'+v.userName+'</td>';
							str+='<td>'+v.feedbackDate+'</td>';
							if(v.state==0){
								str+='<td><span class="success">新建</span></td>';
							}else{
								str+='<td><span class="closed">关闭</span></td>';
							}
							str+='</tr>';
						});
						
						$("#QryResult").append(str);
						//总记录数>0
						if( data.pageObj.total>0){
							$('#QryPage').ossPaginator({
								totalrecords: data.pageObj.total, 
								recordsperpage: 10, 
								length: 6, 
								next: '下一页', 
								prev: '上一页', 
								first: '首页', 
								last: '尾页', 
								initval: page,//初始化哪一页被选中
								controlsalways: true,
								onchange: function (newPage){
									qry(newPage,10);
						    	}
							});
						}else{
							showTip('未查询到相关记录！',3000);
							$('#QryPage').empty();
						}
					}
					//重新调整iframe高度，防止分页不显示
					window.parent.changeHeight();
				}
			});
		}
		//提示框显示
		function showTip(msg,timeout) {
			$('#msgDialog').html(msg).show();
			//如果提供了timeout，那么设置timeout毫秒后自动隐藏
			if(timeout){
				setTimeout(function() {
					$('#msgDialog').hide().html('');
				}, timeout);
			}
		}
		//提示框隐藏
		function hideTip(){
			$('#msgDialog').hide().html('');
		}
		
		qry(1,10);
    </script>
</body>
</html>
                          