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
	<script type="text/javascript" src="${ctx}/res/javascript/portal/system/roleManage.js" ></script>
	<script type="text/javascript" src="${ctx}/res/plugins/zTree3.5.12/js/jquery.ztree.core-3.5.min.js"></script><!--ztree核心控制js-->
	<script type="text/javascript" src="${ctx}/res/plugins/zTree3.5.12/js/jquery.ztree.excheck-3.5.min.js"></script>
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
    	<div class="titleN">角色维护</div>
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
                <dl>
                    <dt>省份</dt>
                    <dd>
                    	<!-- <input id='roleName'  type="text" class="txtInput"  placeholder="  " /> -->
                    	<select  id="province" >
                            <option value="-1">---请选择---</option>
                        </select>
                    </dd>
                </dl>
                <a href="javascript:queryRole(1);" class="btn" ><span class="lf_btn"></span><span class="rf_btn">查询</span></a>
            </div>
            <!--查询条件end-->
            <div class="queryTable clearfix">
            	<!--左侧角色列表-->
            	<div class="roleList">
                	<div class="tabTitle clearfix">
                    	<span class="left">角色列表</span>
                        <div class="right">
                            <a href="####" class="btn btn_sp" onclick="showAddOrEditDialog(1);"><span class="lf_btn"></span><span class="rf_btn">新增</span></a>
                            <a href="####" class="btn" onclick="showAddOrEditDialog(2);" ><span class="lf_btn"></span><span class="rf_btn">修改</span></a>
                            <a href="####" class="btn"  onclick="delConfirm();" ><span class="lf_btn"></span><span class="rf_btn">删除</span></a>
                        </div>
                    </div>
                    <div class="tableWrap">
                    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <th width="5"></th>
                            <th width="55">角色编码</th>
                            <th width="65">角色名称</th>
                            <th width="30">省份</th>
                            <th width="100">角色说明</th>
                          </tr>
                         </table> 
                        <table id="roles"  width="100%" border="0" cellspacing="0" cellpadding="0">  
                        </table>
                        <!--翻页-->
                                <div  id="red1" style="float:left;"></div>
                        <!--翻页end-->
                    </div>
                </div>
                <!--左侧角色列表end-->
                <!--右侧角色权限-->
                <div class="rolePerm">
                	<div class="tabTitle">角色权限</div>
                    <div class="tableWrap">
                    	<div class="tab">
                            <div class="tabHead">
                                <ul class="tabItem">
                                    <li id="tab1" class="select">菜单权限配置</li>
                                    <li id="tab3">操作权限列表</li>
                                    <li id="tab2">角色成员列表</li>
                                </ul>
                            </div>
                            <div class="tabPanel">
                            	<!--菜单权限配置tab中内容-->
                                <div class="tabCont" id="first" >
                                    <div class="roleTree" id="treeWrap">
                                        <ul id="treeDemo" class="ztree"></ul>
                                    </div>
                                    <div class="opt_btn">
                                        <a href="####" class="btn btn_sp" onclick="saveMenu();"><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
                                        <a href="####" class="btn" onclick="clearMenu();"><span class="lf_btn"></span><span class="rf_btn">重置</span></a>
                                    </div>
                                </div>
                                <!--菜单权限配置tab中内容end-->
                                <!--操作权限配置-->
                                <div class="tabCont" id="third" style="height:400px;">
                                    <div class="queryWrap clearfix">
                                        <dl>
                                            <dt>资源</dt>
                                            <dd><select  class="dropdown" id="resourceId">
						                            <option value="-1">---请选择---</option>
						                        </select>
					                        </dd>
                                        </dl>
                                        <dl>
                                            <dt>操作权限</dt>
                                            <dd><input type="text" class="txtInput" id="optName"/></dd>
                                        </dl>
                                        <a href="####" class="btn ml20" onclick="queryOpt(1);" ><span class="lf_btn"></span><span class="rf_btn">查询</span></a>
                                    </div>
                                	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                                      <tr>
                                        <th width="70">资源名称</th>
                                        <th width="70">操作权限编码</th>
                                        <th width="70">操作权限名称</th>
                                        <th width="100">操作</th>
                                      </tr>
                                      </table>
                                    <table id="optsTable" width="100%" border="0" cellspacing="0" cellpadding="0">
                                    </table>
                                    <!--翻页-->
                                            <div  id="red3" style="float:left;width:600px;" ></div>
                                    <!--翻页end-->
                                    
                                </div>
                                <!--角色成员列表tab中内容-->
                                <div class="tabCont" id="second" >
                                	<div class="queryWrap clearfix">
                                        <dl>
                                            <dt>登录帐号</dt>
                                            <dd><input type="text" class="txtInput" id="userId" /></dd>
                                        </dl>
                                        <dl>
                                            <dt>用户姓名</dt>
                                            <dd><input type="text" class="txtInput"  id="userName" /></dd>
                                        </dl>
                                        <a href="####" class="btn" onclick="queryBy();" ><span class="lf_btn"></span><span class="rf_btn">查询</span></a>
                                    </div>
                                	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                                      <tr>
                                        <th width="15"><input type="checkBox" onclick="allC();" id="all1" /></th>
                                        <th width="70">登录帐号</th>
                                        <th width="70">用户姓名</th>
                                        <th width="100">机构</th>
                                      </tr>
                                      </table>
                                    <table  id="users"  width="100%" border="0" cellspacing="0" cellpadding="0">
                                    </table>
                                    <!--翻页-->
                                            <div  id="red2" style="float:left;width:600px;" ></div>
                                    <!--翻页end-->
                                    <div class="opt_btn">
                                        <a href="####" class="btn btn_sp" onclick="addUsers();"><span class="lf_btn"></span><span class="rf_btn">新增</span></a>
                                        <a href="####" class="btn"  onclick="delConfirm2();" ><span class="lf_btn"></span><span class="rf_btn">删除</span></a>
                                    </div>
                                </div>
                                <!--角色成员列表tab中内容end-->
                                
                            </div>
                    	</div>
                    </div>
                </div>
 				<!--右侧角色权限end-->
            </div> 
        </div>

    </div>
</div>
<div id="msg4" ></div>
<div class="msgTips" ></div>
</body>
</html>
