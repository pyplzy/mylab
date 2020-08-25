<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
<title>系统管理-个人信息修改</title>
	<%-- 通用文件，需要在本页面，设定ctx --%>
	<%@ include file="/WEB-INF/views/portal/common/resetandpageslibs.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx}/res/css/addmine.css" /><!--附加样式-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/easydropdown.css" /><!--下拉菜单-->
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/res/javascript/common/base.js" ></script>
	<script type="text/javascript" src="${ctx}/res/plugins/easydropdown/jquery.easydropdown.min.js"></script><!--下拉菜单-->
	<script type="text/javascript">
		var genderValue = ${user.gender };
	</script>
    <script type="text/javascript" src="${ctx }/res/javascript/portal/system/personalData.js"></script>
</head>
<body>
<%--loginId是否可用，默认0未检测，1可用，2不可用 --%>
<input type="hidden" id="isValidLoginId" value="0">

<div class="wrap border clearfix">
	<div class="wholeArea">
    	<div class="titleN">个人信息修改</div>
        <div class="content clearfix">
        	<dl>
            	<dt>工号</dt>
                <dd><input disabled type="text" value="${user.userId }" class="txtInput" /></dd>
            </dl>        
        	<dl>
            	<dt>登录帐号</dt>
                <dd>
                	<input type="text" id="loginId" value="${loginId}" class="txtInput" style="width:330px;"/>
                	<input type="hidden" id="originalLoginId" value="${loginId}" />
                	<a href="javascript:void(0)" onclick="checkLoginIdInUse();">检测</a>
                </dd>
            </dl>
        	<dl>
            	<dt>用户姓名</dt>
                <dd><input type="text" id="userName" value="${user.userName }" class="txtInput" /></dd>
            </dl>
        	<dl>
            	<dt>性别</dt>
                <dd>
                    <select class="dropdown" id="gender" name="gender">
                        <option value="1">男</option>
                        <option value="2">女</option>
                        <option value="0">其他</option>
                    </select>
                </dd>
            </dl>
        	<dl>
            	<dt>手机号码</dt>
                <dd><input type="text" class="txtInput" id="phoneNum" value="${user.mobileNo }"/></dd>
            </dl>
        	<dl>
            	<dt>&nbsp;</dt>
                <dd><a href="javascript:void(0)" class="pswd" onclick="parent.editPwd.showDivKey()">修改密码</a></dd>
            </dl>
        	<dl class="mt20">
            	<dt>&nbsp;</dt>
                <dd>
                    <a href="javascript:void(0)" class="btn btn_sp" onclick="submitUserMsg();"><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
                    <a href="javascript:void(0)" class="btn" onclick="resetUserMsg();"><span class="lf_btn"></span><span class="rf_btn">重置</span></a>
                </dd>
            </dl>
        </div>
        
        <div class="h30 clearfix"></div>
        <div id="msgDialog" class="msgDialog">
		</div>
    </div>
</div>
</body>
</html>