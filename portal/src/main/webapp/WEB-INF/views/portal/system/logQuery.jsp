<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
<title>系统管理-审计日志查询</title>
	<%-- 通用文件，需要在本页面，设定ctx --%>
	<%@ include file="/WEB-INF/views/portal/common/resetandpageslibs.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx}/res/css/addmine.css" /><!--附加样式-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/easydropdown.css" /><!--下拉菜单-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/ossPaginator.css" /><!--分页样式-->
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="${ctx }/res/plugins/My97DatePicker/WdatePicker.js"></script><!--时间插件-->
    <script type="text/javascript" src="${ctx}/res/plugins/easydropdown/jquery.easydropdown.min.js"></script><!--下拉菜单-->    
    <script type="text/javascript" src="${ctx}/res/javascript/common/jquery.ossPaginator.js" ></script>
    <script type="text/javascript" src="${ctx }/res/javascript/portal/system/logQuery.js"></script>
    <style type="text/css">
	 .dropdown li {
	    height: 12px;
	}
    </style>
</head>
<body>
	<div class="wrap border clearfix">
		<div class="wholeArea">
			<div class="tab">
				<div class="tabHead">
					<ul class="tabItem">
						<li class="select" name="tabTitle">登录日志查询</li>
						<li name="tabTitle">信息变更日志查询</li>
					</ul>
				</div>
				<div class="tabPanel">
					<!--登录日志tab中内容-->
					<div class="tabCont">
						<div class="queryWrap clearfix">
							<dl>
								<dt>登录帐号</dt>
								<dd>
									<input type="text" class="txtInput" id="userId" />
								</dd>
							</dl>
							<dl>
								<dt>登录日期</dt>
								<dd>
									<input type="text" id="loginTime" class="txtInput" value="${today}" readonly="readonly"
										onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'${today}',isShowClear:false})" />
								</dd>
							</dl>
							<a href="##" class="btn" id="loginQryBtn"><span class="lf_btn"></span><span class="rf_btn">查询</span></a>
						</div>
						<div class="queryTable">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<th width="40">序号</th>
									<th width="70">登录帐号</th>
									<th width="70">用户姓名</th>
									<th width="100">机构</th>
									<th width="100">IP</th>
									<th width="140">登录时间</th>
									<th width="70">登录结果</th>
								</tr>
								<tbody id="loginQryTbl">
								</tbody>
							</table>
						</div>
						<%--登录日志分页 --%>
						<div id="loginQryPage" style="float:left;"></div>
					</div>

					<!--登录日志tab中内容end-->
					
					<!--信息变更日志查询tab中内容-->
					<div id="test" class="tabCont" style="z-index:10000;height:800px;">
						<div  class="queryWrapForObj clearfix" >
							<dl>
								<dt>操作对象</dt>
								<dd>
						                <select  class="dropdown" id="dropdownValueForLog">
						                	<option value="">----请选择----</option>
						                	<option value="菜单表">菜单</option>
						                	<option value="资源表">资源</option>
						                	<option value="操作权限表">操作</option>
						                	<option value="角色表">角色</option>
						                	<option value="机构表">机构</option>
						                	<option value="用户表">用户</option>       			
						                </select>
								</dd>
							</dl>						
							<dl>
								<dt>开始时间</dt>
								<dd>
									<input type="text" id="qryStartTime" class="txtInput" value="${todayStart}" readonly="readonly"
										onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'${todayStart}',isShowClear:false})" />
								</dd>
							</dl>
							<dl>
								<dt>结束时间</dt>
								<dd>
									<input type="text" id="qryEndTime" class="txtInput" value="${todayEnd}" readonly="readonly"
										onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'${todayEnd}',isShowClear:false})" />
								</dd>
							</dl>
							<a href="##" id="changeQryBtn" class="btn"><span class="lf_btn"></span><span class="rf_btn">查询</span></a>
						</div>
						<div class="queryTable">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<th width="130">操作时间</th>
									<th width="70">操作类型</th>
									<th width="80">操作对象</th>
									<th width="80">登录帐号</th>
									<th width="100">IP</th>
									<th width="240">操作内容</th>
								</tr>
								<tbody id="changeQryTbl">
								</tbody>								
							</table>
						</div>
						<%--信息变更日志分页 --%>
						<div id="changePage" style="float:left;"></div>
					</div>
					<!--信息变更日志查询tab中内容end-->
				</div>
			</div>
		</div>
	</div>
	<%--提示框 --%>
	<div id="msgDialog" class="msgDialogForLog">
	</div>	
</body>
</html>
                          