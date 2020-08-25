<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
	<!--系统管理——个人信息修改——修改密码弹窗-->
	<div class="mask" id="userAddMask"></div><!--遮罩层-->
	<div class="popWrap other1" id="addUserOrg">
		<div class="titleName clearfix"><span class="left" id="editUserOrgTitleSpan"></span><i class="icon_close right" id="closeUserOrgDialog1" onclick="window.userOrgDialog.closeAddUserOrg();"></i></div>
	    <div id="msgDialogForUserOrg" class="msgDialogForUserOrg"></div>
	    <div class=" contAdd clearfix">
	    	<dl>
	        	<dt>管理区域</dt>
	            <dd>
	            	<input type="hidden"  id="editUserOrgOrgId2" />
				    	<div>
				            <input type="text" id="editUserOrgOrgName2" readonly="readonly"  value="" class="txtInput" onclick="window.userOrgDialog.treeForUserOrg();" />
				            <div class="orgDataWapAdd clearfix" id='editUserOrgTreeWrap' >
				            	<ul id="editUserOrgTreeDemo" class="ztree"></ul>
				            </div>
				        </div>
	            </dd>
	        </dl>
	    	<dl>
	        	<dt>用户名</dt>
	            <dd> <input disabled type="text"   id="editUserOrgUserId"  class="txtInput"  /></dd>
	        </dl>			 
	    	<dl class="mt5" style="padding-top: 20px;">
	        	<dt>&nbsp;</dt>
	            <dd>
	            	<a href="javascript:void(0)" class="btn btn_sp" id="saveAddUserOrg" onclick="window.userOrgDialog.editUserOrg();"><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
	                <a href="javascript:void(0)" class="btn"  id="closeUserOrgDialog2" onclick="window.userOrgDialog.closeAddUserOrg();"><span class="lf_btn"></span><span class="rf_btn">关闭</span></a>
	            </dd>
	        </dl>
	    </div>
	</div>
<script type="text/javascript" src="${ctx}/res/javascript/portal/system/editUserOrgManage.js" ></script>
	<script type="text/javascript">
	var ctx = '${ctx }';
	</script>
</html>