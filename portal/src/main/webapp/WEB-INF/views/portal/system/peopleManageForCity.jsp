<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
<title>系统管理-机构人员管理</title>
	<%-- 通用文件，需要在本页面，设定ctx --%>
	<%@ include file="/WEB-INF/views/portal/common/resetandpageslibs.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx}/res/css/addmineEX.css" /> <!--附加样式-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/easydropdown.css" /><!--下拉菜单-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/plugins/zTree3.5.12/css/zTreeStyle/zTreeStyle.css" /><!--ztree核心样式-->
	<link rel="stylesheet" type="text/css" href="${ctx}/res/theme-${uiInfo.theme }/css/ossPaginator.css" /><!--分页样式-->
	
    <style type="text/css">
		.titleN22 {
		    background: #fafafa none repeat scroll 0 0;
		    border-bottom: 1px solid #fafafa;
		    font-size: 14px;
		    font-weight: bold;
		    height: 59px;
		    line-height: 39px;
		    padding-left: 12px;
		} 
		.jigouWrap {
		    background: #fff none repeat scroll 0 0;
		    border: 1px solid #ccc;
		    display: none;
		    height: 250px;
		    left: 116px;
		    overflow-y: auto;
		    position: absolute;
		    width: 216px;
		    z-index: 9999;
		}
    </style>
    <script type="text/javascript">
    	//当前登录人机构
    	var orgIdItem='${sessionScope.orgId}';
    	var ctx = '${ctx }';
    </script>
</head>
<body style="position:absolute;">
<div class="wrap border clearfix">
	<div class="leftArea">
    	<div class="titleN">组织机构树</div>
        <div class="content" id="treeWrap">
        	<ul id="treeDemo" class="ztree"></ul>
        <div class="titleN22"></div>
        </div>
    </div>
    <div class="rightArea else">
    	<div class="titleN" id="titleName22"></div>
        <div class="content conte clearfix">
        	<div class="tab">
                <div class="tabHead">
                    <ul class="tabItem">
                        <li class="select">人员查询</li>
                        <li>机构信息</li>
                        <li>机构人员</li>
                    </ul>
                </div>
                <div class="tabPanel">
                    <!--人员查询tab中内容-->
                    <div class="tabCont">
                        <div class="queryWrap qelse clearfix">
                            <dl>
                                <dt>登录帐号</dt>
                                <dd><input type="text" id="baseId" class="txtInput" /></dd>
                            </dl>
                            <dl>
                                <dt>用户名</dt>
                                <dd><input type="text" id="baseName" class="txtInput" /></dd>
                            </dl>
                            <a href="javascript:void(0)" class="btn" onclick="searchUserMsgItem();"><span class="lf_btn"></span><span class="rf_btn">查询</span></a>
                        </div>
                        <div class="queryTable ptable">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <th width="60">登陆账号</th>
                                <th width="60">用户名称</th>
                                <th width="20">性别</th>
                                <th width="130">机构</th>
                                <th width="70">操作</th>
                              </tr>
                            </table>
                            <table width="100%" border="0" cellspacing="0" cellpadding="0" id="baseMsgTable">
                            </table>
                         <!--翻页结束-->
							<div  id="red"></div>
       					<!--翻页end-->
                        </div> 
                    </div>
                    <!--基本信息tab中内容end-->
                    
                    <!--机构信息tab中内容-->
                    <div class="tabCont poplecont">
                			<dl>
                                <dt><label><span style="color:red;">*</span>机构名称</label></dt>
                                <dd><input type="text" id="orgName" class="txtInput" /></dd>
                            </dl>
                            <dl>
                                <dt><label><span style="color:red;">*</span>机构ID序列</label></dt>
                                <dd><input disabled type="text" id="orgSeq" class="txtInput" /></dd>
                            </dl>
                            <dl>
                                <dt><label><span style="color:red;">*</span>机构名称序列</label></dt>
                                <dd><input disabled type="text" id="orgNameSeq" class="txtInput" /></dd>
                            </dl>
                            <dl>
                                <dt><label><span style="color:red;">*</span>父机构</label></dt>
                                <dd><input disabled type="text" id="pOrgName" class="txtInput" /></dd>
                            </dl>
                            <dl>
                                <dt><label><span style="color:red;">*</span>显示顺序</label></dt>
                                <dd><input type="text" id="displayOrder" class="txtInput" /></dd>
                            </dl>
                            <dl>
                               	<dt><label><span style="color:red;">*</span>是否叶子节点</label></dt>
                                <dd>
                                    <label><input type="radio" name="ynRadio" value="1" />是</label>
                                    <label><input type="radio" name="ynRadio" value="0" />否</label>
                                </dd>
                            </dl>
                            <dl>
                                <dt id="orgGradeDT"><label><span style="color:red;">*</span>机构级别</label></dt>
                                <dd id="orgGradeDD">
                                    <select class="dropdown" id="orgGrade" data-settings='{"cutOff": 5}'>
                                        <option value="1">集团公司</option>
                                        <option value="2">省公司</option>
                                        <option value="3">市公司</option>
                                        <option value="4">县公司</option>
                                        <option value="5">班组</option>
                                        <option value="0">未知</option>
                                    </select>
                                </dd>
                            </dl>
                            <dl>
                                <dt>备注</dt>
                                <dd><textarea class="textArea" id="orgMemo"></textarea></dd>
                            </dl>
                            
                    </div>
                    <!--机构信息tab中内容end-->
                    <!--机构人员tab中内容-->
                    <div class="tabCont" id="orgPersonal">
                        <a href="javascript:void(0)" class="btn btn_sp m10" onclick="addOrgPersonal();"><span class="lf_btn"></span><span class="rf_btn">新增</span></a>
                        <div class="queryTable ptable">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <th width="60">登陆账号</th>
                                <th width="60">用户名称</th>
                                <th width="20">性别</th>
                                <th width="130">机构</th>
                                <th width="70">操作</th>
                              </tr>
                            </table>
                            <table width="100%" border="0" cellspacing="0" cellpadding="0" id="orgPersonTable">
                            </table>
                            <!--翻页结束-->
								<div id="red3"></div>
							<!--翻页end-->
                        </div> 
                    </div>
                    <!--机构人员tab中内容end-->
                </div>
            </div>
        </div>
    </div>
</div>
	<div id="mm0" class="pop_view0">
		<div class="viewItem0" onclick="reload();">刷新</div>
		<div class="viewItem0" onclick="append()">增加子机构</div>
	</div>
	<div id="mm1" class="pop_view0">
		<div class="viewItem0" onclick="reload();">刷新</div>
		<div class="viewItem0" onclick="append()">增加子机构</div>
		<div class="viewItem0" onclick="removeit()">删除</div>
	</div>
	<div id="mm2" class="pop_view0">
		<div class="viewItem0" onclick="reload();">刷新</div>
		<div class="viewItem0" onclick="removeit()">删除</div>
	</div>
	<div id="deleteMenuId1" class="msgbox1"> 
		<div class="msgbox-hd1">
			<span class="msgbox-title1">提示</span>
			<b class="msgbox-close1" onclick="closeMsgbox();"></b>
		</div>
		<div class="msgbox-bd1">
			<div class="msgbox-normal1">	
				<div class="label" id="label22"></div>
			</div>
		</div>
		<div class="msgbox-ft1">
		</div>
	</div>
	<div id="msgDialog" class="msgDialog">
		</div>
		<div id="loadDialog" class="loadDialog">
			<b></b><span></span>
	</div>
	<div class="mask1" id="maskItem"></div>
	<div class="mask2" id="maskItem2"></div>
	<!--系统管理——人员机构管理——授予角色-->
<div class="maskme" id="mask6"></div><!--遮罩层-->
<div class="popWrapme1 editme1" id="popyurole">
	<div class="titleName1 clearfix" ><i class="icon_close right" onclick="closeDiv6()"></i></div>
	<div class="titleName1 clearfix" style="background:#F2F2F2"><span class="left"></span></div>
    <div class="cont clearfix">
    	<div class="queryWrapme1 clearfix">
            <dl>
                <dt>应用</dt>
                <dd>
                	<select class="dropdown" id="rolePermission">
                		<option value=""></option>
                    </select>
                </dd>
            </dl>
            <dl>
                <dt>省份</dt>
                <dd>
               		<select class="dropdown" id="province" >
                           <option value=""></option>
                    </select>
                </dd>
            </dl>
            <a href="javascript:void(0)" class="btn" style="padding-left:60px" onclick="searchRoleMsg();"><span class="lf_btn"></span><span class="rf_btn">查询</span></a>
        </div>
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="poptable">
          <tr>
            <th width="70">角色编码</th>
            <th width="70">角色名称</th>
            <th width="50">省份</th>
            <th width="100">角色说明</th>
            <th width="100">是否授予</th>
          </tr>
        </table>
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="poptable" id="roleTable">
        </table>
        <!--翻页结束-->
		<div  id="red2" style="float:left"></div>
        <!--翻页end-->
    </div>
</div>
<!--系统管理——人员机构管理——修改账号基本信息-->
<div class="popWrapme2 editme2" style="height:380px;" id="popedzh">
	<div class="titleName2 clearfix"><i class="icon_close right" onclick="closeUserDial()"></i></div>
	<div class="titleName2 clearfix" style="background:#F2F2F2"><span class="left"></span></div>
    <div class="cont1 clearfix">
    	<div class="baseNew">
        	<div class="hdnew1">基本信息</div>
            <div class="basecont1 clearfix">
            	<div class="baseItem1">
                	<label>登录帐号</label>
                    <input type="text" id="loginId" disabled class="txtInput" />
                </div>
            	<div class="baseItem1">
                	<label>用户姓名</label>
                    <input type="text" id="userName" class="txtInput" />
                </div>
            	<div class="baseItem1">
                	<label>用户性别</label>
                    <div class="mod1 w2161">
                        <select class="dropdown" id="genderValue" >
                            <option value="1">男</option>
                            <option value="2">女</option>
                            <option value="0">未知</option>
                            <option>&nbsp;</option>
                            <option>&nbsp;</option>
                        </select>
                    </div>
                </div>
            	<div class="baseItem1">
                	<label>密码有效日期</label>
                    <input type="text" value="90天" disabled class="txtInput w1801" />
                    <a href="javascript:void(0)" onclick="resetUserPassWord();">重置密码</a>
                </div>
                <div class="baseItem1">
                	<label>所属机构</label>
                	<input type="hidden"  id="orgId2" />
                    <input type="text" class="txtInput" id="orgName2" readonly="readonly" onclick="showOrgs();"/>
	                    <div class="jigouWrap clearfix" id='orgTree'>
	            			<ul id="treeDemo2" class="ztree"></ul>
	            		</div>
                </div>
            </div>
        </div>
    	<div class="otherNew">
        	<div class="hdnew1">其他信息</div>
            <div class="basecont1">
            	<div class="baseItem1">
                	<label>手机号码</label>
                    <input type="text" id="mobileNo" class="txtInput" />
                </div>
            	<div class="baseItem1">
                	<label>用户状态</label>
                    <div class="mod1 w2161">
                        <select class="dropdown" id="userState">
                            <option value="0">停用</option>
                            <option value="1">启用</option>
                            <option>&nbsp;</option>
                            <option>&nbsp;</option>
                        </select>
                    </div>
                </div>
            	<div class="baseItem1">
                	<label>用户类型</label>
                    <div class="mod1 w2161">
                        <select  class="dropdown" id="userType">
                            <option value="1">普通用户</option>
                            <option value="2">测试用户</option>
                            <option value="3">运维用户</option>
                            <option>&nbsp;</option>
                            <option>&nbsp;</option>
                        </select>
                    </div>
                </div>
            	<div class="baseItem1">
                	<label>创建时间</label>
                    <input type="text" id="createTime" disabled class="txtInput littleItem" />
                </div>
                <div class="popbtm1">
                    <a href="javascript:void(0)" class="btn btn_sp" onclick="updateBaseMsg();"><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
                    <a href="javascript:void(0)" class="btn" onclick="resetBaseMsg1();"><span class="lf_btn"></span><span class="rf_btn">重置</span></a>
                </div>
            </div>
        </div>
    </div>
</div>
<%--新增人员弹窗 --%>
<div class="popWrapme2 editme2" id="addNewOrgPerson">
	<div class="titleName3 clearfix"><i class="icon_close right" onclick="closeUserDialOrg()"></i></div>
	<div class="titleName3 clearfix" style="background:#F2F2F2"><span class="left"></span></div>
    <div class="cont1 clearfix">
    	<div class="baseNew">
        	<div class="hdnew1">基本信息</div>
            <div class="basecont1 clearfix">
            	<div class="baseItem1">
                	<label><span style="color:red;">*</span>登录号</label>
                    <input type="text" id="loginId2" class="txtInput" style="width:146px;"/>
                    <a href="javascript:void(0)" onclick="checkLoginIdInUse();">检测可用</a>
                </div>
            	<div class="baseItem1">
                	<label><span style="color:red;">*</span>用户姓名</label>
                    <input type="text" id="userName2" class="txtInput" />
                </div>
            	<div class="baseItem1">
                	<label>用户性别</label>
                    <div class="mod1 w2161">
                        <select class="dropdown" id="genderValue2" >
                            <option value="1">男</option>
                            <option value="2">女</option>
                            <option value="0">未知</option>
                            <option>&nbsp;</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
    	<div class="otherNew">
        	<div class="hdnew1">其他信息</div>
            <div class="basecont1">
            	<div class="baseItem1">
                	<label><span style="color:red;">*</span>手机号码</label>
                    <input type="text" id="mobileNo2" class="txtInput" />
                </div>
            	<div class="baseItem1">
                	<label>用户状态</label>
                    <div class="mod1 w2161">
                        <select class="dropdown" id="userState2">
                            <option value="1">启用</option>
                            <option value="0">停用</option>
                            <option>&nbsp;</option>
                            <option>&nbsp;</option>
                        </select>
                    </div>
                </div>
            	<div class="baseItem1">
                	<label>用户类型</label>
                    <div class="mod1 w2161">
                        <select  class="dropdown" id="userType2">
                            <option value="1">普通用户</option>
                            <option value="2">测试用户</option>
                            <option value="3">运维用户</option>
                            <option>&nbsp;</option>
                            <option>&nbsp;</option>
                        </select>
                    </div>
                </div>
                <div class="baseItem1">
                	<label>密码有效期</label>
                    <input type="text" value="90天" disabled class="txtInput w1801" />
                </div>
                <div class="popbtm1">
                    <a href="javascript:void(0)" class="btn btn_sp" onclick="isnertUserMsg();"><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
                    <a href="javascript:void(0)" class="btn" onclick="resetBaseMsg2();"><span class="lf_btn"></span><span class="rf_btn">重置</span></a>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/res/plugins/easydropdown/jquery.easydropdown.min.js"></script><!--下拉菜单-->
	<script type="text/javascript" src="${ctx}/res/plugins/zTree3.5.12/js/jquery.ztree.core-3.5.min.js"></script><!--ztree核心控制js-->
   	<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.ossPaginator.js" ></script>
    <script type="text/javascript" src="${ctx }/res/javascript/portal/system/peopleManageForCity.js"></script>
</body>
</html>