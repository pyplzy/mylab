<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
<title>系统管理-应用权限管理</title>
	<%-- 通用文件，需要在本页面，设定ctx --%>
	<%@ include file="/WEB-INF/views/portal/common/resetandpageslibs.jsp"%>
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/base.js" ></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.placeholder.js" ></script>
	<script type="text/javascript" src="${ctx}/res/javascript/portal/system/appPermission.js" ></script>
	<script type="text/javascript">
	var ctx2="${ctx}";
	</script>
</head>

<body style="position:absolute">
<div class="wrap border clearfix">
	<div class="leftArea">
    	<div class="titleN">应用</div>
        <div class="content pd0">
        	<div class="schWrap">
            	<input  type="text" placeholder="搜索管理员"   class="txtInput" id="admin" /><button id='searchButton' onclick="queryAdmins();" class="btnSearch"><i></i></button>
            </div>
            <div class="queryTable colstwo">
            	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <th width="35%">登录帐号</th>
                    <th width="65%">用户姓名</th>
                  </tr>
                 </table> 
				<table width="100%" border="0" cellspacing="0" cellpadding="0" id="admins" >
                </table>

            </div>
        </div>
    </div>
    <div class="rightArea">
        <div class="content1 apli clearfix">
        	<ul id="apps" >
            </ul>
            <input type="hidden" id='userid' value=""></input>
            <div class="btm_btn clearfix">
            	<a href="####" class="btn btn_sp" onclick="addDataPri();" ><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
            </div>
        </div>
    </div>
</div>

<div id="msg" class="msg00"></div>

<div id="msg2" class="msg22"></div>
</body>
</html>
