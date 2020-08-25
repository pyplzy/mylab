<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
	<title>系统管理-角色管理</title>
	<%-- 通用文件，需要在本页面，设定ctx --%>
	<%@ include file="/WEB-INF/views/portal/common/resetandpageslibs.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx}/res/css/roleManage.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/easydropdown.css" /><!--下拉菜单-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/ossPaginator.css" /><!--分页样式-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/plugins/zTree3.5.12/css/zTreeStyle/zTreeStyle.css" /><!--ztree核心样式-->
	
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/base.js"></script>
	<script type="text/javascript" src="${ctx}/res/plugins/easydropdown/jquery.easydropdown.min.js"></script><!--下拉菜单-->
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.ossPaginator.js" ></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.placeholder.js" ></script>
	<script type="text/javascript" src="${ctx}/res/plugins/zTree3.5.12/js/jquery.ztree.core-3.5.min.js"></script><!--ztree核心控制js-->
	<script type="text/javascript" src="${ctx}/res/plugins/zTree3.5.12/js/jquery.ztree.excheck-3.5.min.js"></script>
	<script type="text/javascript" src="${ctx}/res/javascript/portal/system/optManage.js" ></script>
	<script type="text/javascript">
	var userId='${sessionScope.userId}';
	//应用code
	var appNameValue="";
	var ctx = '${ctx }';
	</script>
</head>
<body style="position:absolute;">
<input id="flag" type="hidden" value='${flag}' />
<input id="pageC" type="hidden" value=''>
<div class="wrap border clearfix">
	<div class="wholeArea topbtm">
    	<div class="titleN">操作管理</div>
        <div class="content">
        	<!--查询条件-->
        	<div class="queryWrap clearfix">
            	<dl>
                    <dt>应用</dt>
                    <dd>
                    	<select id="dropdownValue">
							<c:forEach items="${appItems}" var="appItem">
								<option value="${appItem.privilegeValue }">${appItem.privilegeMemo }</option>
		        			</c:forEach>
		                </select>
                    </dd>
                </dl>
                <a href="javascript:queryResources(1);" class="btn" ><span class="lf_btn"></span><span class="rf_btn">查询</span></a>
            </div>
            <!--查询条件end-->
            <div class="queryTable clearfix">
            	<!--左侧角色列表-->
            	<div class="roleList">
                	<div class="tabTitle clearfix">
                    	<span class="left">资源列表</span>
                        <div class="right">
                            <a href="####" class="btn btn_sp" onclick="parent.optDialog.showResourceDiv(0);"><span class="lf_btn"></span><span class="rf_btn">新增</span></a>
                            <a href="####" class="btn" onclick="showParentDialog();" ><span class="lf_btn"></span><span class="rf_btn">修改</span></a>
                            <a href="####" class="btn"  onclick="delResource();" ><span class="lf_btn"></span><span class="rf_btn">删除</span></a>
                        </div>
                    </div>
                    <div class="tableWrap">
                    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <th width="5"></th>
                            <th width="50">应用</th>
                            <th width="50">资源编码</th>
                            <th width="50">资源名称</th>
                            <th width="100">资源说明</th>
                          </tr>
                         </table> 
                        <table id='resourceTable' width="100%" border="0" cellspacing="0" cellpadding="0">  
                        </table>
                        <!--翻页-->
                        <div  id="red1" style="float:left;"></div>
                        <!--翻页end-->
                    </div>
                </div>
                <!--左侧角色列表end-->
            </div> 
            <div class="queryTable clearfix">
            	<!--左侧角色列表-->
            	<div class="roleList" style="height:330px;">
                	<div class="tabTitle clearfix">
                    	<span class="left">操作权限列表</span>
                        <div class="right">
                            <a href="####" class="btn btn_sp" onclick="showOptDiv();"><span class="lf_btn"></span><span class="rf_btn">新增</span></a>
                            <a href="####" class="btn" onclick="showOptDialog();" ><span class="lf_btn"></span><span class="rf_btn">修改</span></a>
                            <a href="####" class="btn"  onclick="delOpt();" ><span class="lf_btn"></span><span class="rf_btn">删除</span></a>
                        </div>
                    </div>
                    <div class="tableWrap">
                    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <th width="5"></th>
                            <th width="60">资源名称</th>
                            <th width="60">权限编码</th>
                            <th width="60">权限名称</th>
                          </tr>
                         </table> 
                        <table id='optTable' width="100%" border="0" cellspacing="0" cellpadding="0">  
                        </table>
                        <!--翻页-->
                        <div  id="red2" style="float:left;"></div>
                        <!--翻页end-->
                    </div>
                </div>
                <!--左侧角色列表end-->
            </div> 
        </div>
    </div>
</div>
<div class="msgTips" ></div>
</body>
</html>
