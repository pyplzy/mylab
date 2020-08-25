<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
<title>个人信息维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
    	<div class="titleN">
    		个人信息维护
    		<c:if test="${ptlAddressBook.addressBookId==null }" >
            	<span style="color:#ff6000;text-align:center;">(<b>在通讯录中没有关联信息</b>)</span>
            </c:if>
    	</div>
        <div class="content">
        	<div class="wraperson clearfix">
            	<div class="left">
	               <dl>
	                	<dt><span style="color:red;">*</span>姓名</dt>
	                    <dd>
	                    	<input type="text" class="txtInput" id="abName" value="${ptlAddressBook.name }"/>
	                    	<%--隐藏主键、头像路径、userId待用 --%>
	                    	<input type="hidden" id="abAddressBookId" value="${ptlAddressBook.addressBookId }">
	                    	<input type="hidden" id="imgPath" value="${ptlAddressBook.headImg }">
	                    	<input type="hidden" id="userId" value="${ptlAddressBook.userId }">
	                    </dd>
	                </dl>
	            	<dl>
	                	<dt>公司</dt>
	                    <dd><input type="text" class="txtInput" id="abCompany" value="${ptlAddressBook.company }"/></dd>
	                </dl>                
	            	<dl>
	                	<dt>部门</dt>
	                    <dd><input type="text" class="txtInput" id="abDeparment" value="${ptlAddressBook.deparment }" /></dd>
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
	                    <dd><input type="text" class="txtInput" id="abMobileNo" value="${ptlAddressBook.mobileNo }" /></dd>
	                </dl>
	            	<dl>
	                	<dt>固话</dt>
	                    <dd><input type="text" class="txtInput" id="abFixedLineTel" value="${ptlAddressBook.fixedLineTel }" /></dd>
	                </dl>                               
	            	<dl>
	                	<dt>邮箱</dt>
	                    <dd><input type="text" class="txtInput" id="abEmail" value="${ptlAddressBook.email }" /></dd>
	                </dl>
	                <dl>
	                	<dt>职务</dt>
	                    <dd><input type="text" class="txtInput" id="abTitle" value="${ptlAddressBook.title }"/></dd>
	                </dl>
	            	<dl>
	                	<dt>级别</dt>
	                    <dd><input type="text" class="txtInput" id="abAdministrativeLevel"  value="${ptlAddressBook.administrativeLevel }"/></dd>
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
	                        <c:choose>
	                        	<c:when test="${ptlAddressBook.addressBookId>0}">
		                            <a href="####" class="btn btn_sp" id="btnSave"><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
		                            <a href="####" class="btn" id="btnReset"><span class="lf_btn"></span><span  class="rf_btn">重置</span></a>
	                            </c:when>
	                         </c:choose>
                        </dd>
                    </dl>
                </div>
                
                <div class="right">
                    <img id="abHeadImg" src="${ctx}/res/images/photo1.png" width="100" height="100" />
             	    <c:choose>
                  		<c:when test="${ptlAddressBook.addressBookId>0}">
							<a href="####" class="btn fileinput-button">
								<span class="lf_btn" id="lChangeHeadBtn"></span><span class="rf_btn" id="rChangeHeadBtn">上传头像</span>
								<input id="fileupload" type="file" name="files[]" data-url="../AttachmentController/uploadHeadImg.do" accept="image/png,image/gif,image/jpg,image/jpeg"/>
							</a>
                      	</c:when>
                   </c:choose>
                    <div class="tips">文件格式GIF、JPG、JPEG、PNG文件大小80K以内，建议尺寸100PX*100PX<span style="color:red;">（上传之后，记得保存）</span></div>
                </div>
                
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
<script type="text/javascript" src="${ctx}/res/javascript/common/base.js" ></script>
<script>
//全局变量，重置使用，初始化时使用
var g_ptlAddressBook = ${ptlAddressBookJson};
</script>
<script type="text/javascript" src="${ctx }/res/javascript/portal/ab/selfABinfoMaintain.js"></script>
<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/jquery.fileupload.js"></script>

</body>
</html>