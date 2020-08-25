<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<style>
.bottom{ margin-top:0; text-align:left;}
</style>
<!--系统管理——角色管理——新建角色弹窗-->
<div class="popWrap other2" style="height:360px;" id="addRole">
	<div class="titleName clearfix"><span class="left">新建角色</span><i class="icon_close right" id="roleDialogCloseAddRole"></i></div>
    <div class="conto clearfix">
    	<dl>
        	<dt><span style="color:red;">*</span>所属应用</dt>
            <dd>
            	<div class="w300">
                    <select  class="dropdown" id="dropdownid">
						<option value="" >---请选择---</option>
                    </select>
                </div>
            </dd>
        </dl>
        <dl>
        	<dt><span style="color:red;">*</span>省份</dt>
            <dd>
            	<div class="w300">
                    <select class="dropdown" id="province2" >
						<option value="" >---请选择---</option>
                    </select>
                </div>
            </dd>
        </dl>
        <dl>
        	<dt><span style="color:red;">*</span>角色编码</dt>
            <dd><input id="roleCode"  type="text" class="txtInput"/></dd>
        </dl>
         <div id="msg3" ></div>
    	<dl>
        	<dt>角色名称</dt>
            <dd><input id="roleName"  type="text" class="txtInput" /></dd>
        </dl>
<!--         <dl> -->
<!--         	<dt>状态</dt> -->
<!--         	<dd> -->
<!--         	<label><input type="radio" name="roleState" value="1"  checked="checked" />启用</label> -->
<!--             <label><input type="radio" name="roleState" value="0" />停用</label> -->
<!--             </dd> -->
<!--         </dl> -->
        <dl id="roleTT">
       		<dt>角色类型</dt>
	        	<dd>
	        	<label><input type="radio" name="roleType" value="1" checked="checked" />应用级</label>
	            <label><input type="radio" name="roleType" value="0" />系统级</label>
            </dd>	
        </dl>
    	<dl>
        	<dt>备注</dt>
            <dd><textarea id="roleMemo" class="textArea"></textarea></dd>
        </dl>
    	<dl class="mt5">
        	<dt>&nbsp;</dt>
            <dd>
            	<a href="####" class="btn btn_sp" id="roleDialogSaveRole" onclick="saveRole()"><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
                <a href="####" class="btn"  id="roleDialogResetDiv"><span class="lf_btn"></span><span class="rf_btn">重置</span></a>
            </dd>
        </dl>
    </div>
</div>
<!--系统管理——个人信息修改——新建角色弹窗end-->

<!--系统管理——角色管理——修改角色弹窗-->
<div class="popWrap other2" style="height:360px;" id="updRole">
	<div class="titleName clearfix"><span class="left">修改角色</span><i class="icon_close right" id="roleDialogCloseUpdRole"></i></div>
    <div class="cont clearfix">
    	<input id="roleId"  type="hidden"   />
    	<input id="appCode3"  type="hidden" class="txtInput"  disabled='disabled' />
    	<dl>
        	<dt>所属应用</dt>
            <dd><input id="appName3"  type="text" class="txtInput"  disabled='disabled' /></dd>
        </dl>
        <dl>
        	<dt>省份</dt>
            <dd><input id="province3"  type="text" class="txtInput"  disabled='disabled' /></dd>
        </dl>
        <dl>
        	<dt>角色编码</dt>
            <dd><input id="roleCode2"  type="text" class="txtInput" disabled='disabled' /></dd>
        </dl>
         
    	<dl>
        	<dt>角色名称</dt>
            <dd><input id="roleName2"  type="text" class="txtInput" /></dd>
        </dl>
<!--         <dl> -->
<!--         	<dt>状态</dt> -->
<!--         	<dd> -->
<!--         	<label><input type="radio" name="roleState" value="1"  checked="checked" />启用</label> -->
<!--             <label><input type="radio" name="roleState" value="0" />停用</label> -->
<!--             </dd> -->
<!--         </dl> -->
        <dl id="tt2"></dl>
    	<dl>
        	<dt>备注</dt>
            <dd><textarea id="roleMemo2" class="textArea"></textarea></dd>
        </dl>
    	<dl class="mt5">
        	<dt>&nbsp;</dt>
            <dd>
            	<a href="####" class="btn btn_sp" id="roleDialogUpdRoleSave"><span class="lf_btn"></span><span class="rf_btn">修改</span></a>
                <a href="####" class="btn" id="roleDialogResetDiv2"><span class="lf_btn"></span><span class="rf_btn">重置</span></a>
            </dd>
        </dl>
    </div>
</div>
<!--系统管理——角色管理——修改角色弹窗end-->

<%--系统管理——角色管理管理——添加成员弹窗--%>
<div class="popWrap h550" id="poeple">
<div id="msgDialog" class="msgDialog"></div>
<div id="msg66" ></div>
	<div class="titleName clearfix"><i class="icon_close right" id="roleDialogClosePeople"></i></div>
	<div class="titleName clearfix" style="background:#F2F2F2" ><span  class="left" id="titleAdd"></span></div>
    <div class="contpage cont1 clearfix">
    	<input type="hidden"  id="orgId2" />
    	<div class="baseItem one">
            <label>机构</label>
            <input type="text" id="orgName2" readonly="readonly"  value="" class="txtInput"/>
            <div class="jigouWrap clearfix" id="treeWrap12">
            	<ul id="treeDemo12" class="ztree"></ul>
            </div>
        </div>
    	<div class="baseItem one">
            <label>登录帐号</label>
            <input type="text"   id="userId2" value="" class="txtInput" />
        </div>
        <a href="####" class="btn ml20" id="roleDialogPeopleQuery"><span class="lf_btn"></span><span class="rf_btn">查询</span></a>
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="poptable">
          <tr>
            <th width="15"><input type="checkbox" id="all2"  /></th>
            <th width="60">登录帐号</th>
            <th width="70">用户姓名</th>
            <th width="200">机构</th>
          </tr>
        </table>  
        <table id="usersRoleDialog" width="100%" border="0" cellspacing="0" cellpadding="0" class="poptable">
        </table>
        <!--翻页-->
        <div  id="red2" style="float:left;" ></div>
        <!--翻页end-->
        <div class="popbtm">
            <a href="####" class="btn btn_sp" id="roleDialogPeopleSaveUsers"><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
            <a href="####" class="btn" id="roleDialogPeopleCloseDiv"><span class="lf_btn"></span><span class="rf_btn">关闭</span></a>
        </div>

    </div>
</div>
<%--系统管理—角色管理管理——添加成员弹窗end--%>
<script type="text/javascript" src="${ctx }/res/javascript/portal/system/roleManageForIndex.js"></script>