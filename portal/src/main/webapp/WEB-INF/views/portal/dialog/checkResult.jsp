<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
	<!--系统管理——个人信息修改——修改密码弹窗-->
	<div class="mask" id="checkUserAddMask"></div><!--遮罩层-->
	<div class="popWrap other1" id="checkAddUserOrg">
		<div class="titleName clearfix"><span class="left" id="checkInfoTitleSpan"></span><i class="icon_close right" id="closeUserOrgDialog1" onclick="window.userCheckInfo.closeCheckResult();"></i></div>
	    <div id="checkMmsgDialogForUserOrg" class="msgDialogForUserOrg"></div>
	    <div class=" contAdd clearfix" id="checkWrap">
	    	<dl>
	        	<dt>通过</dt>
	            <dd><input type="radio" name="checkIfTongguo" id="tongGuo" value="shi" /></dd>
	        </dl>
	        <dl>
	        	<dt>不通过</dt>
	            <dd> <input type="radio" name="checkIfTongguo" id="buTongGuo" value="fou"/></dd>
	        </dl>
	        <dl>
	        	<dt>审核意见</dt>
	            <dd> <textarea rows="3" cols="50" name="checkOpinion" id="checkOpinion"></textarea>
	        </dl>
	        			 
	    	<dl class="mt5" style="padding-top: 20px;">
	        	<dt>&nbsp;</dt>
	            <dd>
	            	<a href="javascript:void(0)" class="btn btn_sp" id="saveCheckInfo" onclick="window.userCheckInfo.checkResult();"><span class="lf_btn"></span><span class="rf_btn">确定</span></a>
	                <a href="javascript:void(0)" class="btn"  id="closeCheckInfo" onclick="window.userCheckInfo.closeCheckResult();"><span class="lf_btn"></span><span class="rf_btn">取消</span></a>
	            </dd>
	        </dl>
	    </div>
	</div>
<script type="text/javascript" src="${ctx}/res/javascript/portal/system/checkResult.js" ></script>
	<script type="text/javascript">
	var ctx = '${ctx }';
	</script>
</html>