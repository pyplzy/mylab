<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="${ctx}/res/css/addmineEX.css" /> <!--附加样式-->
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

.jigouWrap2 {
	background: #fff none repeat scroll 0 0;
	border: 1px solid #ccc;
	display: none;
	height: 250px;
	left: 116px;
	top: 220px;
	overflow-y: auto;
	position: absolute;
	width: 216px;
	z-index: 9999;
}
.mlt2people{
	left: 76px;
	top: 118px;
}
.bottom{ margin-top:0; text-align:left;}
</style>
<div class="popWrapme1 editme1" id="popyurole">
	<div class="titleName1 clearfix" ><i class="icon_close right" ></i></div>
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
               		<select class="dropdown" id="province2people" >
                           <option value="-1"></option>
                    </select>
                </dd>
            </dl>
            <a href="javascript:void(0)" class="btn" style="padding-left:60px" ><span class="lf_btn"></span><span class="rf_btn">查询</span></a>
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
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="poptable" id="roleTable2">
        </table>
        <!--翻页结束-->
		<div  id="red2people" style="float:left"></div>
        <!--翻页end-->
    </div>
</div>
<!--系统管理——人员机构管理——修改基本信息-->
<div class="popWrapme2 editme2" style="height:400px;" id="popedzh">
	<div class="titleName2 clearfix"><i class="icon_close right" ></i></div>
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
                        </select>
                    </div>
                </div>
            	<div class="baseItem1">
                	<label>密码有效日期</label>
                    <input type="text" value="90天" disabled class="txtInput w1801" />
                    <a href="javascript:void(0)">重置密码</a>
                </div>
                <div class="baseItem1">
                	<label>所属机构</label>
                	<input type="hidden"  name="orgId" />
                    <input type="text" class="txtInput" readonly="readonly" name="orgName"/>
                    <div class="jigouWrap2 clearfix">
            			<ul class="ztree"></ul>
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
                	<label>用户类型</label>
                    <div class="mod1 w2161">
                        <select  class="dropdown" id="userType">
                            <option value="1">普通用户</option>
                            <option value="2">测试用户</option>
                            <option value="3">运维用户</option>
                        </select>
                    </div>
                </div>
            	<div class="baseItem1">
                	<label>创建时间</label>
                    <input type="text" id="createTime" disabled class="txtInput littleItem" />
                </div>
                <div class="popbtm1">
                    <a href="javascript:void(0)" class="btn btn_sp" ><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
                    <a href="javascript:void(0)" class="btn" ><span class="lf_btn"></span><span class="rf_btn">重置</span></a>
                </div>
            </div>
        </div>
    </div>
</div>
<%--新增人员弹窗 --%>
<div class="popWrapme2 editme2" id="addNewOrgPerson" style="height:420px;">
	<div class="titleName3 clearfix"><i class="icon_close right"></i></div>
	<div class="tab">
         <div class="tabHead">
             <ul class="tabItem">
                 <li class="select">新增行政归属人员</li>
                 <li>新增业务归属人员</li>
             </ul>
         </div>
		<div class="tabPanel">
			<div class="tabCont">
				<div class="cont1 clearfix">
					<div class="baseNew">
						<div class="hdnew1">基本信息</div>
						<div class="basecont1 clearfix">
							<div class="baseItem1">
								<label><span style="color: red;">*</span>登录帐号</label> <input
									type="text" id="loginId2" class="txtInput"
									style="width: 146px;" /> <a href="javascript:void(0)"
									>检测可用</a>
							</div>
							<div class="baseItem1">
								<label><span style="color: red;">*</span>用户姓名</label> <input
									type="text" id="userName2" class="txtInput" />
							</div>
							<div class="baseItem1">
								<label>用户性别</label>
								<div class="mod1 w2161">
									<select class="dropdown" id="genderValue2">
										<option value="1">男</option>
										<option value="2">女</option>
										<option value="0">未知</option>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="otherNew">
						<div class="hdnew1">其他信息</div>
						<div class="basecont1">
							<div class="baseItem1">
								<label><span style="color: red;">*</span>手机号码</label> <input
									type="text" id="mobileNo2" class="txtInput" />
							</div>
							<div class="baseItem1">
								<label>用户类型</label>
								<div class="mod1 w2161">
									<select class="dropdown" id="userType2">
										<option value="1">普通用户</option>
										<option value="2">测试用户</option>
										<option value="3">运维用户</option>
									</select>
								</div>
							</div>
							<div class="baseItem1">
								<label>密码有效期</label> <input type="text" value="90天" disabled
									class="txtInput w1801" />
							</div>
							<div class="popbtm">
								<a href="javascript:void(0)" class="btn btn_sp"
									><span class="lf_btn"></span><span
									class="rf_btn">保存</span></a> <a href="javascript:void(0)"
									class="btn"><span class="lf_btn"></span><span
									class="rf_btn">重置</span></a>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="tabCont">
				<div class="contpage cont1 clearfix">
					<input type="hidden" id="orgId2people" />
					<div class="baseItem one">
						<label>机构</label> <input type="text" id="orgName2people" readonly="readonly" value="" class="txtInput" />
						<div class="jigouWrap clearfix mlt2people" id="treeWrap2people">
							<ul id="treeDemo2people" class="ztree"></ul>
						</div>
					</div>
					<div class="baseItem one">
						<label>登录帐号</label> <input type="text" id="userId2people" value=""
							class="txtInput" />
					</div>
					<a href="####" class="btn ml20" ><span class="lf_btn"></span><span class="rf_btn">查询</span></a>
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="poptable">
						<tr>
							<th width="5"></th>
							<th width="60">登录帐号</th>
							<th width="70">用户姓名</th>
							<th width="200">机构</th>
						</tr>
					</table>
					<table id="users2people" width="100%" border="0" cellspacing="0" cellpadding="0" class="poptable">
					</table>
					<!--翻页-->
					<div id="red2user" style="float: left;"></div>
					<!--翻页end-->
					<div class="popbtm1">
						<a href="####" class="btn btn_sp" ><span class="lf_btn"></span><span class="rf_btn">保存</span></a> 
						<a href="####" class="btn" ><span class="lf_btn"></span><span class="rf_btn">关闭</span></a>
					</div>
				</div>
			</div>
		</div>
	</div>
   
</div>
<script type="text/javascript" src="${ctx }/res/javascript/portal/dialog/peopleManageDialog.js"></script>