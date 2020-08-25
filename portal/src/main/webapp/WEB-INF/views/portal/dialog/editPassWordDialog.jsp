<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
	<!--系统管理——个人信息修改——修改密码弹窗-->
	<div class="mask" id="mask"></div><!--遮罩层-->
	<div class="popWrap other1" id="popAddKey">
		<div class="titleName clearfix"><span class="left">修改密码</span><i class="icon_close right" id="closePasDialog" ></i></div>
	    <div class="cont clearfix">
	    	<dl>
	        	<dt>旧密码</dt>
	            <dd><input type=password autocomplete="off" class="txtInput" id="keyOld1"  style="ime-mode:disabled;"/></dd><%--ime-mode:disabled;只能输入英文，仅IE火狐有效--%>
	        </dl>
	    	<dl>
	        	<dt>新密码</dt>
	            <dd><input type="password" autocomplete="off" class="txtInput" id="keyNew1"  style="ime-mode:disabled;"/></dd><%--ime-mode:disabled;只能输入英文，仅IE火狐有效--%>
	        </dl>
	        <dl>
	        	<dt>&nbsp;</dt>
	            <dd><div class="pwd_normal" id="pwdHeight"></div></dd>
	        </dl>
	    	<dl>
	        	<dt>再次输入</dt>
	            <dd><input type="password" autocomplete="off" class="txtInput" id="keyAgain1"  style="ime-mode:disabled;"/></dd><%--ime-mode:disabled;只能输入英文，仅IE火狐有效--%>
	        </dl>
	<!--       	<input type="hidden" id="keyOld" />	
		        <input type="hidden" id="keyNew" />
				<input type="hidden" id="keyAgain" />
	 -->	 			 
				<div id="warn_area" class="pwd_warn">
					<span id="pwd_error" style="display:inline-block;">
	           			<i class="pwd_error"></i>
	           		</span>
	           		<span id="pwd_right" style="display:inline-block;">
	           			<i class="pwd_right"></i>
	           		</span>
	          		<span id="warn_tip" class="red"></span>
				</div>	
	    	<dl class="mt5" style="padding-top: 20px;">
	        	<dt>&nbsp;</dt>
	            <dd>
	            	<a href="javascript:void(0)" class="btn btn_sp" id="submitKey" ><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
	                <a href="javascript:void(0)" class="btn" id="resetKey" ><span class="lf_btn"></span><span class="rf_btn">重置</span></a>
	            </dd>
	        </dl>
	    </div>
	</div>
<!--系统管理——个人信息修改——修改密码弹窗end-->
<script type="text/javascript" src="${ctx}/res/javascript/portal/system/passWordEdit.js" ></script>
</html>