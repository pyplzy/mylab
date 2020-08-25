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
    	var userId='${sessionScope.userId}';
    	var ctx = '${ctx }';
    	var optList = ${optList};
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
                                <dt>用户姓名</dt>
                                <dd><input type="text" id="baseName" class="txtInput" /></dd>
                            </dl>
                            <a href="javascript:void(0)" class="btn" onclick="searchUserMsgItem();"><span class="lf_btn"></span><span class="rf_btn">查询</span></a>
                        </div>
                        <div class="queryTable ptable">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <th width="60">登录帐号</th>
                                <th width="60">用户姓名</th>
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
                                <dt><label><span style="color:red;">*</span>机构编码</label></dt>
                                <dd><input type="text" id="orgCode" class="txtInput" onblur="checkOrgCode();" /></dd>
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
                                        <option value="1">总部</option>
                                        <option value="2">省</option>
                                        <option value="3">市</option>
                                        <option value="4">区县</option>
                                        <option value="5">乡镇</option>
                                        <option value="0">其他部门</option>
                                    </select>
                                </dd>
                            </dl>
                            <dl>
                                <dt><label><span style="color:red;">*</span>行政级别</label></dt>
                                <dd>
                                    <select class="dropdown" id="administrativeGrade" data-settings='{"cutOff": 5}'>
                                        <option value="1">国级</option>
                                        <option value="2">部级</option>
                                        <option value="3">厅局级</option>
                                        <option value="4">处级</option>
                                        <option value="5">科级</option>
                                        <option value="0">未知</option>
                                    </select>
                                </dd>
                            </dl>
                            <dl>
                                <dt>备注</dt>
                                <dd><textarea class="textArea" id="orgMemo"></textarea></dd>
                            </dl>
                            <dl class="mt20" id="orgEditButton">
                                <dt>&nbsp;</dt>
                                <dd>
                                    <a href="javascript:void(0)" class="btn btn_sp" onclick="saveOrgBaseMsg();"><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
                                    <a href="javascript:void(0)" class="btn" onclick="orgFormReset();"><span class="lf_btn"></span><span class="rf_btn">重置</span></a>
                                </dd>
                            </dl>
                    </div>
                    <!--机构信息tab中内容end-->
                    <!--机构人员tab中内容-->
                    <div class="tabCont" id="orgPersonal">
                        <a href="javascript:void(0)" class="btn btn_sp m10" onclick="addOrgPersonal();"><span class="lf_btn"></span><span class="rf_btn">新增</span></a>
                        <div class="queryTable ptable">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <th width="60">登录帐号</th>
                                <th width="60">用户姓名</th>
                                <th width="30">性别</th>
                                <th width="130">机构</th>
                                <th width="60">类别</th>
                                <th width="110">操作</th>
                              </tr>
                            </table>
                            <table width="100%"  border="0" cellspacing="0" cellpadding="0" id="orgPersonTable">
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
		<c:choose>
			<c:when test="${optList.org_add== '1' }">
				<div class="viewItem0" onclick="append()">增加子机构</div>
			</c:when>
		</c:choose>
	</div>
	<div id="mm1" class="pop_view0">
		<div class="viewItem0" onclick="reload();">刷新</div>
		<c:choose>
			<c:when test="${optList.org_add== '1' }">
				<div class="viewItem0" onclick="append()">增加子机构</div>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${optList.org_delete== '1' }">
				<div class="viewItem0" onclick="removeit()">删除</div>
			</c:when>
		</c:choose>
	</div>
	<div id="mm2" class="pop_view0">
		<div class="viewItem0" onclick="reload();">刷新</div>
		<c:choose>
			<c:when test="${optList.org_delete== '1' }">
				<div class="viewItem0" onclick="removeit()">删除</div>
			</c:when>
		</c:choose>
	</div>
	<div id="msgDialog" class="msgDialog"></div>
	<div id="loadDialog" class="loadDialog">
		<b></b><span></span>
	</div>

<div class="msgTips" ></div>
<script type="text/javascript" src="${ctx}/res/javascript/common/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${ctx}/res/plugins/easydropdown/jquery.easydropdown.min.js"></script><!--下拉菜单-->
<script type="text/javascript" src="${ctx}/res/plugins/zTree3.5.12/js/jquery.ztree.core-3.5.min.js"></script><!--ztree核心控制js-->
<script type="text/javascript" src="${ctx}/res/javascript/common/jquery.ossPaginator.js" ></script>
<script type="text/javascript" src="${ctx }/res/javascript/portal/system/peopleManage.js"></script>
</body>
</html>