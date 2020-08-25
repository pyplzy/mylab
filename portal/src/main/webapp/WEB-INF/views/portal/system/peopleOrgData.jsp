<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
	<title>系统管理-机构数据权限</title>
	<%-- 通用文件，需要在本页面，设定ctx --%>
	<%@ include file="/WEB-INF/views/portal/common/resetandpageslibs.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx}/res/css/roleManage.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/easydropdown.css" /><!--下拉菜单-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/ossPaginator.css" /><!--分页样式-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/main.css" /><!--分页样式-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/plugins/zTree3.5.12/css/zTreeStyle/zTreeStyle.css" /><!--ztree核心样式-->
	
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/base.js"></script>
<!-- 	<script type="text/javascript" src="${ctx}/res/plugins/easydropdown/jquery.easydropdown.min.js"></script>下拉菜单-->
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.ossPaginator.js" ></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.placeholder.js" ></script>
	<script type="text/javascript" src="${ctx}/res/javascript/portal/system/peopleOrgData.js" ></script>
	<script type="text/javascript" src="${ctx}/res/plugins/zTree3.5.12/js/jquery.ztree.core-3.5.min.js"></script><!--ztree核心控制js-->
	<script type="text/javascript" src="${ctx}/res/plugins/zTree3.5.12/js/jquery.ztree.excheck-3.5.min.js"></script>
	<script type="text/javascript">
	var userId='${sessionScope.userId}';
	//应用code
	var appNameValue="";
	var orgId='${sessionScope.orgId}';
	var orgName='${sessionScope.orgName}';
	var ctx = '${ctx }';
	</script>
</head>
<body style="position:absolute;">
<div class="wrap border clearfix" >
	<div class="wholeArea topbtm">
    	<div class="titleN">机构数据权限</div>
        <div class="content">
        	<!--查询条件-->
        	<div class="queryWrapForUserOrg clearfix">
            	<dl>
                    <dt>机构</dt>
                    <dd>
				    	<input type="hidden"  id="orgId2POrgUser" />
				    	<div class="baseItem one">
				            <input type="text" id="orgName2POrgUser" readonly="readonly"  value="" class="txtInput" id="tree"  onclick="treePOrgUser();"/>
				            <div class="orgDataWap clearfix" id='treeWrapPOrgUser'>
				            	<ul id="treeDemoWrapPOrgUser" class="ztree"></ul>
				            </div>
				        </div>
                    </dd>
                </dl>
                <dl>
                    <dt>用户姓名</dt>
                    <dd>
                    	<input type="text"   id="treeuserId2POrgUser" value="" class="txtInput" />
                    </dd>
                </dl>
                <a href="javascript:queryUsersPOrgUser();" class="btn" ><span class="lf_btn"></span><span class="rf_btn">查询</span></a>
            </div>
            <!--查询条件end-->
            <div class="queryTable clearfix">
            	<div class="roleList">
                	<div class="tabTitle clearfix">
                    	<span class="left">人员列表</span>
                    	<span  class="right"></span>
                    </div>
                    <div class="tableWrap">
                    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <th width="30">登录帐号</th>
                            <th width="30">用户姓名</th>
                            <th width="50">用户所在机构</th>
                            <th width="50">用户管理机构</th>
                            <th width="80">操作</th>
                            <th name="id" style="display:none">用户标识</th>
                            <th name="orgId" style="display:none">机构标识</th>
                          </tr>
                         </table> 
                        <table id='users' width="100%" border="0" cellspacing="0" cellpadding="0">  
                        </table>
                        <!--翻页-->
                                <div  id="red12POrgUser" style="float:left;"></div>
                        <!--翻页end-->
                    </div>
                </div>
            </div> 
        </div>

    </div>
</div>
<div class="h30 clearfix"></div>
<div id="msgDialog" class="msgDialogForLog">
</div>
</body>
</html>
