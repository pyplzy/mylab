<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
<title>系统管理-菜单管理</title>
	<%-- 通用文件，需要在本页面，设定ctx --%>
	<%@ include file="/WEB-INF/views/portal/common/resetandpageslibs.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx}/res/css/addmineEX.css" /><!--附加样式-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/easydropdown.css" /><!--下拉菜单-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/plugins/zTree3.5.12/css/zTreeStyle/zTreeStyle.css" /><!--ztree核心样式-->
</head>
<body>
<div class="wrap border clearfix textPosition">
	<div class="leftArea">
    	<div class="titleN">
        	<span>应用</span>
        	<div class="selectWrap w175">
                <select id="dropdownValue">
                	<option value="">---请选择---</option>
					<c:forEach items="${appItems}" var="appItem">
						<option value="${appItem.privilegeValue }">${appItem.privilegeMemo }</option>
        			</c:forEach>
                </select>
            </div>
        </div>
        <div class="content" id="treeWrap">
        	<ul id="treeDemo" class="ztree"></ul>
        </div>
    </div>
    <div class="rightArea">
    	<div class="titleN" id="titleName"></div>
        <div class="content clearfix">
        	<form id="memuForm" method="post" action="inse">
	        	<dl>
	            	<dt><label><span style="color:red;">*</span>菜单名称</label></dt>
	                <dd><input type="text" id="menuName" class="txtInput" /></dd>
	            </dl>
	        	<dl>
	            	<dt><label><span style="color:red;">*</span>所属应用</label></dt>
	                <dd><input disabled type="text" id="appName" class="txtInput" /></dd>
	            </dl>
	        	<dl>
	            	<dt><label><span style="color:red;">*</span>父菜单名称</label></dt>
	                <dd><input disabled type="text" id="pMenuName" class="txtInput" /></dd>
	            </dl>
	        	<dl>
	            	<dt>显示顺序</dt>
	                <dd><input type="text" id="displayOrder" class="txtInput" /></dd>
	            </dl>
	        	<dl>
	            	<dt><label><span style="color:red;">*</span>是否叶子节点</label></dt>
	                <dd>
	                	<label><input type="radio" name="ynRadio" onclick="checkAction();" value="1" >是</label>
	                    <label><input type="radio" name="ynRadio" onclick="checkAction();" value="0" >否</label>
	                </dd>
	            </dl>
	        	<dl>
	            	<dt>菜单动作</dt>
	                <dd><textarea class="textArea" name="menuAction" id="menuAction"></textarea></dd>
	            </dl>
	        	<dl>
	            	<dt>菜单备注</dt>
	                <dd><textarea class="textArea" id="menuMemo"></textarea></dd>
	            </dl>
	        	<dl class="mt20">
	            	<dt>&nbsp;</dt>
	                <dd>
	                    <a href="javascript:void(0)" class="btn btn_sp" onclick="saveMenuEdit();"><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
	                    <a href="javascript:void(0)" class="btn" onclick="resetMenuEdit();"><span class="lf_btn"></span><span class="rf_btn">重置</span></a>
	                </dd>
	            </dl>
            </form>
        </div>
    </div>
	<div id="mm0" class="pop_view0">
		<div class="viewItem0" onclick="reload();">刷新</div>
		<div class="viewItem0" onclick="append()">增加子菜单</div>
	</div>
	<div id="mm1" class="pop_view0">
		<div class="viewItem0" onclick="reload();">刷新</div>
		<div class="viewItem0" onclick="append()">增加子菜单</div>
		<div class="viewItem0" onclick="removeit()">删除</div>
	</div>
	<div id="mm2" class="pop_view0">
		<div class="viewItem0" onclick="reload();">刷新</div>
		<div class="viewItem0" onclick="removeit()">删除</div>
	</div>
	<div id="msgDialogForMenu" class="msgDialog">
</div>
	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/res/plugins/easydropdown/jquery.easydropdown.min.js"></script><!--下拉菜单-->
	<script type="text/javascript" src="${ctx}/res/plugins/zTree3.5.12/js/jquery.ztree.core-3.5.min.js"></script><!--ztree核心控制js-->
    <script type="text/javascript" src="${ctx }/res/javascript/portal/system/menuManage.js"></script>
</body>
</html>