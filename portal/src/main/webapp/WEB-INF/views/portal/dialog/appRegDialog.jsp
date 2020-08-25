<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>应用管理弹出层</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<%-- 添加应用弹窗--%>
	<div class="mask" id="mask1"></div><%-- 遮罩--%>
 
 	<div class="popWrap popImg" id="popAdd">
		<div class="titleName clearfix">
			<span class="left">添加新应用</span><i class="icon_close right"
				onclick="closeDiv2();"></i>
		</div>
		<div class="cont clearfix">
			<form action="" name="appForm">
				<div class="appimg">
					<label>应用图标</label>
					<div class="icon_img">
						<img id="appImg" src="${ctx}/res/images/icon_app1.png" /> <input type='hidden' id='appImgPath' value='/res/images/icon_app1.png' />
					</div>
					<a href="####" class="btn left btn_again"><span class="lf_btn"></span><span class="rf_btn">选择图标</span></a>
				</div>
				<dl>
					<dt>
						<span style="color: red;">*</span>应用编码
					</dt>
					<dd>
						<input type="text" class="txtInput" id="appCode" value="" />
					</dd>
					<dd>
						<a href="javascript:;" class="btn" id="judge" onclick="judgeApp();"><span class="lf_btn"></span>
							<span class="rf_btn">检测</span></a>
					</dd>
				</dl>
				<div id="msg" class="msgDialog111"></div>
				<dl>
					<dt>应用名称</dt>
					<dd>
						<input type="text" class="txtInput" id="appName" value="" />
					</dd>
				</dl>
				<dl>
					<dt>入口URL</dt>
					<dd>
						<input type="text" class="txtInput" id="appUrl" value="" />
					</dd>
				</dl>
				<dl>
					<dt>代办通知URL</dt>
					<dd>
						<input type="text" class="txtInput" id="todoUrl" value="" />
					</dd>
				</dl>
				<dl>
					<dt>用户手册路径</dt>
					<dd>
						<input type="text" class="txtInput" id="userGuidePath" value="" />
					</dd>
				</dl>
				<dl>
					<dt>检验码</dt>
					<dd>
						<input type="text" class="txtInput" id="appChecksum" value="" disabled="disabled" />
					</dd>
					<dd>
						<a href="####" class="btn" onclick="app();"><span class="lf_btn"></span><span class="rf_btn">生成</span></a>
					</dd>
				</dl>
				<dl>
					<dt>状态</dt>
					<dd>
						<label><input type="radio" name="state" value="1" checked="checked" />上线</label> 
						<label><input type="radio" name="state" value="0" />下线</label>
					</dd>
				</dl>
				<dl>
					<dt>应用类型</dt>
					<dd>
						<label><input type="radio" name="appType" value="1" checked="checked" />内部应用</label> 
						<label><input type="radio" name="appType" value="2" />外部应用</label>
					</dd>
				</dl>
				<dl>
					<dt><span style="color: red;">*</span>显示顺序</dt>
					<dd><input type="text" class="txtInput" id="displayOrder" value="" /></dd>
				</dl>
				<dl>
					<dt>备注</dt>
					<dd><textarea class="textArea" id="appMmemo" style='height: 50px'></textarea></dd>
				</dl>
				<dl class="mt5">
					<dt>&nbsp;</dt>
					<dd>
						<a href="####" class="btn btn_sp" onclick="save()"><span
							class="lf_btn"></span><span class="rf_btn">保存</span></a> <a
							href="####" class="btn" onclick="document.appForm.reset(); "><span
							class="lf_btn"></span><span class="rf_btn">重置</span></a>
					</dd>
				</dl>
			</form>
		</div>
	</div>
	<!--添加应用弹窗end-->

	<!--重新选择图标弹窗-->
	<div class="mask1"></div>
	<!--遮罩层-->
	<div class="popWrap other" id="addIcon">
		<div class="titleName clearfix">
			<span class="left">选择应用图标</span><i class="icon_close1 right"></i>
		</div>
		<div class="cont">
			<ul class="clearfix" id="images">
				<li><div id='img1' value='/res/images/icon_app1.png' class="imgBox"><img src="${ctx}/res/images/icon_app1.png" />
					</div></li>
				<li><div id='img2' value='/res/images/icon_app2.png' class="imgBox"><img src="${ctx}/res/images/icon_app2.png" />
					</div></li>
				<li><div id='img3' value='/res/images/icon_app3.png' class="imgBox"><img src="${ctx}/res/images/icon_app3.png" />
					</div></li>
				<li><div id='img4' value='/res/images/icon_app4.png' class="imgBox"><img src="${ctx}/res/images/icon_app4.png" />
					</div></li>
				<li><div id='img5' value='/res/images/icon_app5.png' class="imgBox"><img src="${ctx}/res/images/icon_app5.png" />
					</div></li>
				<li><div id='img6' value='/res/images/icon_app6.png' class="imgBox"><img src="${ctx}/res/images/icon_app6.png" />
					</div></li>
				<li><div id='img7' value='/res/images/icon_app7.png' class="imgBox"><img src="${ctx}/res/images/icon_app7.png" />
					</div></li>
				<li><div id='img8' value='/res/images/icon_app8.png' class="imgBox"><img src="${ctx}/res/images/icon_app8.png" />
					</div></li>
				<li><div id='img9' value='/res/images/icon_app9.png' class="imgBox"><img src="${ctx}/res/images/icon_app9.png" />
					</div></li>
				<li><div id='img10' value='/res/images/icon_app10.png'class="imgBox"><img src="${ctx}/res/images/icon_app10.png" />
					</div></li>
			</ul>
			<div class="bottom clearfix">
				<a href="####" class="btn btn_sp" id="save1"><span class="lf_btn"></span><span class="rf_btn">确定</span></a> 
				<a href="javascript:;" class="btn" id="off1"><span class="lf_btn"></span><span class="rf_btn">重置</span></a>
			</div>
		</div>
	</div>
	<!--重新选择图标弹窗end-->

	<!--应用管理——修改应用弹窗-->
	<div class="mask" id="mask3"></div>
	<!--遮罩层-->
	<div class="popWrap popImg" id="popImg">
		<div class="titleName clearfix">
			<span class="left">应用编辑</span><i class="icon_close right"
				onclick="closeDiv3()"></i>
		</div>
		<div class="cont clearfix">
			<div class="appimg">
				<label>应用图标</label>
				<div class="icon_img"><img id="appImg2" src="" /></div>
				<a href="####" class="btn left btn_again"><span class="lf_btn"></span><span class="rf_btn">重新选择</span></a>
			</div>
			<dl>
				<dt>
					<span style="color: red;">*</span>应用编码
				</dt>
				<dd>
					<input type="text" class="txtInput" id="appCode2" value=""
						disabled="disabled" />
				</dd>
			</dl>
			<div id="msg2" class="msgDialog22"></div>
			<form action="" name="appForm2">
				<input type='hidden' id='appImgPath2' value='' />
				<dl>
					<dt>应用名称</dt>
					<dd>
						<input type="text" class="txtInput" id="appName2" value="" />
					</dd>
				</dl>
				<dl>
					<dt>入口URL</dt>
					<dd>
						<input type="text" class="txtInput" id="appUrl2" value="" />
					</dd>
				</dl>
				<dl>
					<dt>代办通知URL</dt>
					<dd>
						<input type="text" class="txtInput" id="todoUrl2" value="" />
					</dd>
				</dl>
				<dl>
					<dt>用户手册路径</dt>
					<dd>
						<input type="text" class="txtInput" id="userGuidePath2" value="" />
					</dd>
				</dl>
				<dl>
					<dt>检验码</dt>
					<dd>
						<input type="text" class="txtInput" id="appChecksum2" value=""
							disabled="disabled" />
					</dd>
				</dl>
				<dl>
					<dt>状态</dt>
					<dd>
						<label><input type="radio" name="state2" value="1" />上线</label> <label><input
							type="radio" name="state2" value="0" />下线</label>
					</dd>
				</dl>
				<dl>
					<dt>应用类型</dt>
					<dd>
						<label><input type="radio" name="appType2" value="1" />内部应用</label>
						<label><input type="radio" name="appType2" value="2" />外部应用</label>
					</dd>
				</dl>
				<dl>
					<dt>
						<span style="color: red;">*</span>显示顺序
					</dt>
					<dd>
						<input type="text" class="txtInput" id="displayOrder2" value="" />
					</dd>
				</dl>
				<dl>
					<dt>备注</dt>
					<dd>
						<textarea class="textArea" id="appMmemo2" style='height: 50px'></textarea>
					</dd>
				</dl>
				<dl class="mt5">
					<dt>&nbsp;</dt>
					<dd>
						<a href="####" class="btn btn_sp" onclick="updApp();"><span class="lf_btn"></span><span class="rf_btn">保存</span></a> 
						<a href="####" class="btn" onclick="document.appForm2.reset(); "><span class="lf_btn"></span><span class="rf_btn">重置</span></a>
					</dd>
				</dl>
			</form>
		</div>
	</div>
	<!--应用管理——点击图标弹窗end-->
<script type="text/javascript" src="${ctx}/res/javascript/portal/appManage/appReg.js" ></script>
</body>
</html>