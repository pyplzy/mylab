<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ page import="usi.dbdp.portal.util.*" %>
<!DOCTYPE HTML>
<%
	 response.setHeader("Pragma","No-cache");
	 response.setHeader("Cache-Control","No-cache");
	 response.setDateHeader("Expires", -1);
	 response.setHeader("Cache-Control", "No-store");
%>
<html>
	<head>
		<title>${uiInfo.title }</title>
		<%-- 通用文件，需要在本页面，设定ctx --%>
		<%@ include file="/WEB-INF/views/portal/common/commonlibs.jsp"%>
		<link rel="stylesheet" type="text/css" href="${ctx}/res/css/addmineEX.css" /><!--附加样式-->
		<link rel="stylesheet" type="text/css" href="${ctx}/res/css/roleManage.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/ossPaginator.css" /><!--分页样式-->
		<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/easydropdown.css" /><!--下拉菜单-->
		<link rel="stylesheet" type="text/css" href="${ctx}/res/plugins/zTree3.5.12/css/zTreeStyle/zTreeStyle.css" /><!--ztree核心样式-->
		<!-- File Upload -->
		<link rel="stylesheet" type="text/css" href="${ctx}/res/jQuery-File-Upload-9.9.3/css/jquery.fileupload.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/res/jQuery-File-Upload-9.9.3/css/jquery.fileupload-ui.css">
		<script type="text/javascript">
		var casUrl='<%=ConfigUtil.getValue("casUrl")%>';
		var omUrl='<%=ConfigUtil.getValue("omUrl")%>';
		var wfmUrl='<%=ConfigUtil.getValue("wfmUrl")%>';
		var amUrl='<%=ConfigUtil.getValue("amUrl")%>';
		</script>
 	</head>
<body>
<!--顶部-->
<div class="top"  >
	<div class="w980 clearfix">
    	<div class="left">${ sessionScope.userName }，${ sessionScope.orgName } </div>
        <div class="right">
        	<a href="javascript:void(0)"  onclick="editPwd.showDivKey()">修改密码</a>
        	<a href="javascript:void(0)" class="switch"></a>
        	<a href="javascript:void(0)" onclick="logout()" id = "logout_btn">安全退出</a>
        </div>
    </div>
</div>
<!--顶部end-->

<!--头部-->
<div class="header w980 clearfix">
	<div class="logo left">
    	<img src="${ctx}/res/theme-${uiInfo.theme }/images/logo.png" class="left" />
    </div>
    <%--通讯录搜索框 --%>    
    <div class="search right">
    	<input type="text" class="schInput" id="schcontent" value="通讯录搜索"/>
        <button class="schbtn" id="schbtn"><i></i></button>
    </div>
    <%-- 通讯录搜索结果--%>    
    <div class="searchcont schwidth hide">   
    	<%-- 点击个人之后的详情--%>      
        <div class="pdetail">
        	<div class="schhd">人员信息</div>
        	<div class="lfph">
            	<p class="photo"><img id="abHeadImg" src="" width="100" height="100" /></p>
                <p id="abName"></p>
                <p class="department" id="abCompany1"></p>
            </div>
        	<div class="infmdetail left">
            	<dl>
                	<dt>公司</dt>
                    <dd><input type="text" class="txtInput" id="abCompany2" readonly="readonly" /></dd>
                </dl>
            	<dl>
                	<dt>部门</dt>
                    <dd><input type="text" class="txtInput" id="abDeparment" readonly="readonly" /></dd>
                </dl>   
            	<dl>
                	<dt>性别</dt>
                    <dd><input type="text" class="txtInput" id="abGender" readonly="readonly" /></dd>
                </dl>                             
            	<dl>
                	<dt>手机</dt>
                    <dd><input type="text" class="txtInput" id="abMobileNo" readonly="readonly" /></dd>
                </dl>
            	<dl>
                	<dt>固话</dt>
                    <dd><input type="text" class="txtInput" id="abFixedLineTel" readonly="readonly" /></dd>
                </dl>
            	<dl>
                	<dt>邮箱</dt>
                    <dd><input type="text" class="txtInput" id="abEmail" readonly="readonly" /></dd>
                </dl>                        	
            	<dl>
                	<dt>职务</dt>
                    <dd><input type="text" class="txtInput" id="abTitle" readonly="readonly" /></dd>
                </dl>
            	<dl>
                	<dt>级别</dt>
                    <dd><input type="text" class="txtInput" id="abAdministrativeLevel" readonly="readonly" /></dd>
                </dl>
            	<dl>
                	<dt>类型</dt>
                    <dd><input type="text" class="txtInput" id="abUserCata" readonly="readonly" /></dd>
                </dl>
            </div>
        </div>
        
        <%-- 通讯录搜索结果--%>  
    	<div class="schresult schwidth">
        	<div class="schhd">搜索结果<span id="schcnt"></span></div>
            <div class="pcont schwidth" id="pcont">
            	<ul id="schresultul">
                </ul>
            </div>
        </div>
    </div>     
       
</div>
<!--头部end-->

<!--导航菜单-->
<div class="nav">
    <div class="navs w980 clearfix">
        <ul class="menu">
        	<c:forEach items="${menus.authMenus }" var="menu">
        		<li class="menuParent" onclick="tabClick(this);" menuId="${menu.menuId }" id="${menu.menuId }_tab" title="${menu.menuName }" >
        			<a class="menuParentA" href="javascript:;"> ${menu.menuName }</a>
        		</li>
        	</c:forEach>
        </ul>
    </div>
</div>
<!--导航菜单end-->
<div class="main w980 clearfix">
		
	<div class="leftWrap">
       	<ul class="lf_menu">
       	</ul>              
    </div>
    <div class="rightWrap">
        <iframe name="mainFrame" class="if_main" frameborder="0" width="100%" height="100%" scrolling="no" marginheight="0" marginwidth="0" ></iframe>
    </div>
    
</div>
<!--底部footer-->
<div class="footer">
    	<p>${uiInfo.Copyright }</p>
</div>
<!--底部footer end-->

<%--通讯录——新增联系人--%>
<div class="mask" id="mask9"></div><!--遮罩层-->
<div class="popWrap book" id="searchnew">
	<div class="titleName clearfix"><span class="left">新增联系人</span><i class="icon_close right" onclick="closeDiv9()"></i></div>
    <div class="conto clearfix">
    	<div class="wraperson clearfix">
            <div class="left w380">
                  <dl>
                	<dt><span style="color:red;">*</span>姓名</dt>
                    <dd>
                    	<input type="text" class="txtInput" id="newabName" />
                    	<%--头像路径 --%>
                    	<input type="hidden" id="newimgPath">
                    </dd>
                </dl>
            	<dl>
                	<dt>公司</dt>
                    <dd><input type="text" class="txtInput" id="newabCompany" /></dd>
                </dl>                
            	<dl>
                	<dt>部门</dt>
                    <dd><input type="text" class="txtInput" id="newabDeparment" /></dd>
                </dl>
            	<dl>
                	<dt>性别</dt>
                    <dd class="w316" id="dropdownGender">
                        <select class="dropdown" id="newabGender">
                        	<option value="未知">未知</option>
                            <option value="男">男</option>
                            <option value="女">女</option>
                        </select>  
                    </dd>
                </dl> 
            	<dl>
                	<dt>手机</dt>
                    <dd><input type="text" class="txtInput" id="newabMobileNo" /></dd>
                </dl>
            	<dl>
                	<dt>固话</dt>
                    <dd><input type="text" class="txtInput" id="newabFixedLineTel" /></dd>
                </dl>                               
            	<dl>
                	<dt>邮箱</dt>
                    <dd><input type="text" class="txtInput" id="newabEmail" /></dd>
                </dl>
                <dl>
                	<dt>职务</dt>
                    <dd><input type="text" class="txtInput" id="newabTitle" /></dd>
                </dl>
            	<dl>
                	<dt>级别</dt>
                    <dd><input type="text" class="txtInput" id="newabAdministrativeLevel" /></dd>
                </dl>                
            	<dl>
                	<dt>类型</dt>
                    <dd>
                    	<div class="w316" id="dropdownUserCata">
	                        <select  class="dropdown" id="newabUserCata">
	                            <option value="系统用户">系统用户</option>
	                            <option value="非系统用户">非系统用户</option>
	                        </select>
                        </div>
                    </dd>
                </dl>
                
                <dl>
                    <dt>&nbsp;</dt>
                    <dd>
                    	<a href="####" class="btn btn_sp" id="btnSave"><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
                        <a href="####" class="btn" id="btnReset"><span class="lf_btn"></span><span class="rf_btn">重置</span></a>
                    </dd>
                </dl>
            </div>
            
            <div class="right">
                <img id="newabHeadImg" src="" width="100" height="100" />
				<a href="####" class="btn fileinput-button">
					<span class="lf_btn" id="lChangeHeadBtn"></span><span class="rf_btn" id="rChangeHeadBtn">上传头像</span>
					<input id="fileupload" type="file" name="files[]" data-url="AttachmentController/uploadHeadImg.do" accept="image/png,image/gif,image/jpg,image/jpeg"/>
				</a>                
                <div class="tips">文件格式GIF、JPG、JPEG、PNG文件大小80K以内，建议尺寸100PX*100PX</div>
            </div>
            
        </div>
    </div>
</div>
<%--通讯录——新增联系人end--%>

<div class="msgTips" ></div>
<div id="confirm" class="msgbox1"> 
	<div class="msgbox-hd1">
		<span class="msgbox-title1">提示</span>
		<b class="msgbox-close1" onclick="closeMsgbox();"></b>
	</div>
	<div class="msgbox-bd1">
		<div class="msgbox-normal1">	
			<div class="label" id="label33"></div>
		</div>
	</div>
	<div class="msgbox-ft1">
	</div>
</div>
<div style="with:0px;htight:0px;" id="logoutIframe" >
	
</div>
<%-- js放在底部加快页面渲染速度--%>
<%-- <script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${ctx}/res/javascript/common/base.js" ></script>
<script type="text/javascript" src="${ctx}/res/javascript/common/md5.js" ></script>
<script type="text/javascript" src="${ctx}/res/plugins/easydropdown/jquery.easydropdown.min.js"></script><!--下拉菜单-->
<script type="text/javascript" src="${ctx }/res/javascript/portal/system/passWordEdit.js"></script>
<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.ossPaginator.js" ></script>
<script type="text/javascript" src="${ctx}/res/plugins/zTree3.5.12/js/jquery.ztree.core-3.5.min.js"></script><!--ztree核心控制js-->
<script type="text/javascript" src="${ctx}/res/plugins/zTree3.5.12/js/jquery.ztree.excheck-3.5.min.js"></script>

<script type="text/javascript">
	//应用上下文
	var ctx = '${ctx }';
	//无意义变量（也可能有意义，后期可以用存储解决）
	var authInfo = ${menus};
	//安全退出
	function logout(){
		if(confirm("确定要安全退出吗？")){
			
			//销毁session
// 			$.ajax({
// 				type: 'POST',
// 				url: 'logout',
// 				async: true,//异步，不用等返回
// 				dataType:"text"//返回数据类型为json时（controller使用@ResponseBody）必须设置返回的数据类型为json			
// 			});//ajax结束
			
// 			window.top.location="http://192.168.80.175:10010/cas/logout";
			var selfHref = window.location.href;
			var tmpArr = new Array();
			tmpArr = selfHref.split("/");
			var appUrl = "";
			for(var i=0;i<3;i++){			
				appUrl = appUrl+tmpArr[i]+"/";
			}
			appUrl = 'http://192.168.80.175:17777/'+"cas/logout";
			window.top.location='${ctx}/logout?casUrl='+appUrl;
//			window.top.location="http://192.168.80.175:16666/cas/logout";
		}
	}
	var userId='${sessionScope.userId}';
	var loginId='${sessionScope.loginId}';
	var orgId='${sessionScope.orgId}';
	var orgName='${sessionScope.orgName}';
</script>
<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/jquery.fileupload.js"></script>
<script type="text/javascript" src="${ctx}/res/javascript/portal/appManage/appReg.js" ></script>
<script type="text/javascript" src="${ctx }/res/javascript/portal/index.js"></script>
<%@ include file="./dialog/helpDialog.jsp"%>
修改密码弹框
<%@ include file="./dialog/editPassWordDialog.jsp"%>
应用注册弹窗
<%@ include file="./dialog/appRegDialog.jsp"%>
操作管理弹窗
<%@ include file="./dialog/resourceDialog.jsp"%>
审核弹框
<%@ include file="./dialog/checkResult.jsp"%>
添加管理机构弹框
<%@ include file="./dialog/editUserOrgManageDialog.jsp"%>

角色管理弹框
<%@ include file="./dialog/roleManageDialog.jsp"%>
机构人员管理弹框
<%@ include file="./dialog/peopleManageDialog.jsp"%>
机构人员管理弹框
<%@ include file="./dialog/taskDialog.jsp"%> --%>
<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${ctx}/res/javascript/common/base.js" ></script>
<script type="text/javascript" src="${ctx}/res/javascript/common/md5.js" ></script>
<script type="text/javascript" src="${ctx}/res/plugins/easydropdown/jquery.easydropdown.min.js"></script><!--下拉菜单-->
<script type="text/javascript" src="${ctx }/res/javascript/portal/system/passWordEdit.js"></script>
<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.ossPaginator.js" ></script>
<script type="text/javascript" src="${ctx}/res/plugins/zTree3.5.12/js/jquery.ztree.core-3.5.min.js"></script><!--ztree核心控制js-->
<script type="text/javascript" src="${ctx}/res/plugins/zTree3.5.12/js/jquery.ztree.excheck-3.5.min.js"></script>
<script type="text/javascript" src="${ctx}/res/plugins/layer/layer.js"></script>

<script type="text/javascript">
	//应用上下文
	var ctx = '${ctx }';
	//无意义变量（也可能有意义，后期可以用存储解决）
	var authInfo = ${menus};
	var userId='${sessionScope.userId}';
	var loginId='${sessionScope.loginId}';
	var orgId='${sessionScope.orgId}';
	var orgName='${sessionScope.orgName}';
	//Secure Exit
	function logout(){
		//销毁session
		$.ajax({
			type: 'POST',
			url: 'logout',
			async: true,//异步，不用等返回
			dataType:"text"//返回数据类型为jsonHour（controller使用@ResponseBody）必须设置返回的数据类型为json			
		});
		addAppIframe("http://"+omUrl+"/cn-om");
		addAppIframe("http://"+wfmUrl+"/wfm");
		addAppIframe("http://"+amUrl+"/inas-web");
		layer.confirm('确定要退出吗?',{title:'Tip',
			  btn: ['确定','取消'] //按钮
			}, function(){
				
				window.top.location ="http://"+casUrl+"/cas/logout?service="+window.location.href;
			});
	}
	function addAppIframe(url){
		$("#logoutIframe").append('<iframe style="width:0px;height:0px;" src="'+url+'/logoutView"></iframe>')
	}
</script>
<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="${ctx}/res/jQuery-File-Upload-9.9.3/js/jquery.fileupload.js"></script>
<script type="text/javascript" src="${ctx}/res/javascript/portal/appManage/appReg.js" ></script>
<script type="text/javascript" src="${ctx }/res/javascript/portal/index.js"></script>
<%@ include file="./dialog/helpDialog.jsp"%>
<%-- Change Password弹框--%>
<%@ include file="./dialog/editPassWordDialog.jsp"%>
<%-- 应用注册弹窗--%>
<%@ include file="./dialog/appRegDialog.jsp"%>
<%-- 操作管理弹窗--%>
<%@ include file="./dialog/resourceDialog.jsp"%>
<%-- 添加管理机构弹框--%>
<%@ include file="./dialog/editUserOrgManageDialog.jsp"%>
<%-- 角色管理弹框--%>
<%@ include file="./dialog/roleManageDialog.jsp"%>
<%-- 机构人员管理弹框--%>
<%@ include file="./dialog/peopleManageDialog.jsp"%>
<%-- 机构人员管理弹框--%>
<%@ include file="./dialog/taskDialog.jsp"%>
</body>
</html>
