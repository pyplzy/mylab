<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
<title>账号申请审核</title>
	<%-- 通用文件，需要在本页面，设定ctx --%>
	<%@ include file="/WEB-INF/views/portal/common/resetandpageslibs.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx}/res/css/addmine.css" /><!--附加样式-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/easydropdown.css" /><!--下拉菜单-->
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="${ctx }/res/plugins/My97DatePicker/WdatePicker.js"></script><!--时间插件-->
	<script type="text/javascript" src="${ctx}/res/javascript/common/base.js" ></script>
	    <script type="text/javascript" src="${ctx}/res/javascript/common/jquery.ossPaginator.js" ></script>
	<script type="text/javascript" src="${ctx}/res/plugins/easydropdown/jquery.easydropdown.min.js"></script><!--下拉菜单-->
	<script type="text/javascript">
		var genderValue = ${user.gender };
	</script>
    <script type="text/javascript" src="${ctx }/res/javascript/portal/system/accountApplicaCheck.js"></script>
	<style type="text/css">
		.queryWrapForObj dl dt {
		   text-align:center;
		   width:111px;
		} 
		.queryWrapForObj dl {
		   width:255px;
		}
		.queryWrapForObj dl dd{
		   width:auto;
		}
		.queryWrapForObj dl dd .txtInput{
		   width:119px;
		}
		.queryWrapForObj{
		  text-align:center;
		}
    </style>
</head>
<body>
<%--loginId是否可用，默认0未检测，1可用，2不可用 --%>
<input type="hidden" id="isValidLoginId" value="0">

<div class="wrap border clearfix">
	<!--审核查询查询tab中内容-->
	<div id="test" class="tabCont" style="z-index:10000;height:800px;">
		<div  class="queryWrapForObj clearfix" >
			<dl>
				<dt>申请时间(起)</dt>
				<dd>
					<input type="text" id="qryStartTime" class="txtInput"  readonly="readonly"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false})" />
				</dd>
			</dl>
			<dl>
				<dt>申请时间(止)</dt>
				<dd>
					<input type="text" id="qryEndTime" class="txtInput"  readonly="readonly"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false})" />
				</dd>
			</dl>
			<dl>
				<dt>联系电话</dt>
				<dd><input type="text" class="txtInput" id="contactNumber" />
				</dd>
			</dl>
			<dl>
				<dt>申请人</dt>
				<dd><input type="text" class="txtInput" id="applicationMan" />
				</dd>
			</dl>
			<dl style="width:530px">
				<dt>申请单位</dt>
				<dd><input type="text" style="width:380px" class="txtInput" id="applicationCompany" />
				</dd>
			</dl>
			<a href="##" id="changeQryBtn" style="margin-top: 10px;" class="btn"><span class="lf_btn"></span>
			<span class="rf_btn">查询</span></a>
		</div>
		<div class="queryTable">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th width="30">操作</th>
					<th width="45">申请人</th>
					<th width="70">账号</th>
					<th width="75">联系电话</th>
					<th width="100">证件号码</th>
					<th width="100">单位</th>
					<th width="120">申请时间</th>
					<th width="70">状态</th>
					<th name="registerId" style="display:none">申请单位ID</th>
				</tr>
				<tbody id="accountApplication">
				</tbody>								
			</table>
		</div>
		<%--信息变更日志分页 --%>
		<div id="changePage" style="float:left;"></div>
	</div>
		<%--提示框 --%>
	<div id="msgDialog" class="msgDialogForLog">
	</div>
</div>
</body>
</html>