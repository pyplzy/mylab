<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
<title>通讯录管理</title>
<%-- 通用文件，需要在本页面，设定ctx --%>
<%@ include file="/WEB-INF/views/portal/common/resetandpageslibs.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/easydropdown.css" /><!--下拉菜单-->
<link rel="stylesheet" type="text/css" href="${ctx}/res/css/addmineEX.css" /> <!--确认框，弹出框-->
<!-- File Upload -->
<link rel="stylesheet" type="text/css" href="${ctx}/res/jQuery-File-Upload-9.9.3/css/jquery.fileupload.css">
<link rel="stylesheet" type="text/css" href="${ctx}/res/jQuery-File-Upload-9.9.3/css/jquery.fileupload-ui.css">

</head>
<body style="position:absolute;">
<div class="wrap border clearfix">
	<div class="wholeArea">
    	<div class="titleN"><a href="####" class="shide right"><span>显示新增</span><i class="updown"></i></a>新增联系人</div>
        <div class="contb p20 hide clearfix">
        	<div class="method">
        		<p>方法一：点<span>击新增联系人</span>按钮，每次添加单个联系人</p>
    			<a href="####" class="btn" id="newABrec"><span class="lf_btn"></span><span class="rf_btn">新增单个联系人</span></a>
            </div>
            <div class="method mt10 clearfix">
                <p>方法二：选择<span>模板文件</span>，然后点击<span>增量导入</span>按钮，每次添加单个或多个联系人</p>
                <div class="left ">
                	<input type="text" class="txtInput w350 left" id="selectedFile" readonly="readonly"/>&nbsp;&nbsp;
                </div>                
                <div class="left  fileinput-button">
                	<a href="####" class="btn btn_sp"><span class="lf_btn"></span><span class="rf_btn">选择文件</span></a>
    				<input id="fileuploadExl" type="file" name="files[]" data-url="menu_ab_impAddressBookFromExcel.do"/>
                </div>
                <div class="left">
                	<a href="####" class="btn left" id="btnStartImp"><span class="lf_btn"></span><span class="rf_btn">增量导入</span></a>
                </div>
            </div>
            <div class=" method">
            	<p>没有模板，<a href="${ctx}/res/addressbookTemplate.xlsx" target="_blank">点击这里下载</a></p>
            </div>
        </div>
    </div>
    
    <div class="wholeArea">
    	<div class="titleN">管理联系人</div>
        <div class="contb clearfix">
        	<%--搜索 --%>
        	<div class="schleft">
        		<%--搜索文本框及搜索按钮 --%>
                <div class="schWrap">
                    <input type="text" placeholder="输入联系人" class="txtInput" id="schcontent" /><button class="btnSearch" id="schbtn"><i></i></button>
                </div>
                <%--搜索结果 --%>
                <div class="schres">
                	<ul id="schresultul">
                    </ul>
                </div>
            </div>
            <%--搜索结果展示 --%>
            <div class="schright">
            	<dl>
                	<dt>&nbsp;</dt>
                    <dd>
                    	<img id="abHeadImg" src="${ctx}/res/images/photo1.png" width="100" height="100" />
    					<a href="####" class="btn fileinput-button">
    						<span class="lf_btn" id="lChangeHeadBtn"></span><span class="rf_btn" id="rChangeHeadBtn">上传头像</span>
    						<input id="fileupload" type="file" name="files[]" data-url="../AttachmentController/uploadHeadImg.do" accept="image/png,image/gif,image/jpg,image/jpeg"/>
    					</a>
                	</dd>
                </dl>
                <dl>
                	<dt><span style="color:red;">*</span>姓名</dt>
                    <dd>
                    	<input type="text" class="txtInput" id="abName" />
                    	<%--隐藏主键、头像路径、userId待用 --%>
                    	<input type="hidden" id="abAddressBookId">
                    	<input type="hidden" id="imgPath">
                    	<input type="hidden" id="userId">
                    </dd>
                </dl>
            	<dl>
                	<dt>公司</dt>
                    <dd><input type="text" class="txtInput" id="abCompany" /></dd>
                </dl>                
            	<dl>
                	<dt>部门</dt>
                    <dd><input type="text" class="txtInput" id="abDeparment" /></dd>
                </dl>
            	<dl>
                	<dt>性别</dt>
                    <dd class="wdd">
                        <select class="dropdown" id="abGender">
                        	<option value="未知">未知</option>
                            <option value="男">男</option>
                            <option value="女">女</option>
                        </select>
                    </dd>
                </dl> 
            	<dl>
                	<dt>手机</dt>
                    <dd><input type="text" class="txtInput" id="abMobileNo" /></dd>
                </dl>
            	<dl>
                	<dt>固话</dt>
                    <dd><input type="text" class="txtInput" id="abFixedLineTel" /></dd>
                </dl>                               
            	<dl>
                	<dt>邮箱</dt>
                    <dd><input type="text" class="txtInput" id="abEmail" /></dd>
                </dl>
                <dl>
                	<dt>职务</dt>
                    <dd><input type="text" class="txtInput" id="abTitle" /></dd>
                </dl>
            	<dl>
                	<dt>级别</dt>
                    <dd><input type="text" class="txtInput" id="abAdministrativeLevel" /></dd>
                </dl>                
            	<dl>
                	<dt>类型</dt>
                    <dd class="wdd">
                        <select  class="dropdown" id="abUserCata">
                            <option value="系统用户">系统用户</option>
                            <option value="非系统用户">非系统用户</option>
                        </select>
                    </dd>
                </dl>
            	<dl>
                	<dt>&nbsp;</dt>
                    <dd>
                    	<a href="####" class="btn btn_sp" id="btnSave"><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
                        <a href="####" class="btn"  id="btnReset"><span class="lf_btn"></span><span class="rf_btn">重置</span></a>
                        <a href="####" class="btn" id="btnDelete"><span class="lf_btn"></span><span class="rf_btn">删除</span></a>
                    </dd>
                </dl>
            </div>
        </div>
    </div>
    
</div>
<%--确认框和提示框 --%>
<div id="tipBox" class="msgbox1">
	<div class="msgbox-hd1">
		<span class="msgbox-title1">提示</span> <b class="msgbox-close1" onclick="closeMsgbox();"></b>
	</div>
	<div class="msgbox-bd1">
		<div class="msgbox-normal1">
			<div class="label" id="label22"></div>
		</div>
	</div>
	<div class="msgbox-ft1"></div>
</div>

<%-- js放在底部提升页面加载速度--%>
<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${ctx}/res/plugins/easydropdown/jquery.easydropdown.min.js"></script><!--下拉菜单-->
<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.placeholder.js" ></script>
<script type="text/javascript" src="${ctx}/res/javascript/common/base.js" ></script>
<script type="text/javascript" src="${ctx }/res/javascript/portal/ab/abManager.js"></script>
<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/jquery.fileupload.js"></script>

</body>
</html>