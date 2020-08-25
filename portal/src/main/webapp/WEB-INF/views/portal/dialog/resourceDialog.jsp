<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div class="popWrap other2" style="height:360px;" id="resource">
	<div class="titleName clearfix"><span class="left" id="resourceText">新建资源</span><i class="icon_close right"  id="optDialogResClose"></i></div>
    <div class="conto clearfix">
    	<input type="hidden" id="resourceId"/>
    	<dl>
        	<dt><span style="color:red;">*</span>所属应用</dt>
            <dd>
            	<div class="w300">
                    <select  class="dropdown" id="appForResource">
						<option value="-1" >---请选择---</option>
                    </select>
                </div>
            </dd>
        </dl>
        <dl>
        	<dt><span style="color:red;">*</span>资源编码</dt>
            <dd><input id="resourceCode"  type="text" class="txtInput"/></dd>
        </dl>
    	<dl>
        	<dt>资源名称</dt>
            <dd><input id="resourceName"  type="text" class="txtInput" /></dd>
        </dl>
        <dl id="tt"></dl>
    	<dl>
        	<dt>备注</dt>
            <dd><textarea id="resourceDesc" class="textArea"></textarea></dd>
        </dl>
    	<dl class="mt5">
        	<dt>&nbsp;</dt>
            <dd>
            	<a href="####" class="btn btn_sp" id="optDialogSaveRes" ><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
                <a href="####" class="btn" id="optDialogForResReset"><span class="lf_btn"></span><span class="rf_btn">重置</span></a>
            </dd>
        </dl>
    </div>
</div>
<div class="popWrap other2" style="height:200px;" id="optDialog">
	<div class="titleName clearfix"><span class="left" id="optText"></span><i class="icon_close right" id="optDialogOptClose"></i></div>
    <div class="conto clearfix">
    	<input type="hidden" id="optId"/>
    	<input type="hidden" id="resourceId2"/>
        <dl>
        	<dt><span style="color:red;">*</span>权限编码</dt>
            <dd><input id="optCode"  type="text" class="txtInput"/></dd>
        </dl>
    	<dl>
        	<dt>权限名称</dt>
            <dd><input id="optName"  type="text" class="txtInput" /></dd>
        </dl>
    	<dl class="mt5">
        	<dt>&nbsp;</dt>
            <dd>
            	<a href="####" class="btn btn_sp" id="optDialogOptSave"><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
                <a href="####" class="btn" id="optDialogOptReset"><span class="lf_btn"></span><span class="rf_btn">重置</span></a>
            </dd>
        </dl>
    </div>
</div>
<script type="text/javascript" src="${ctx}/res/javascript/portal/dialog/optManageDialog.js" ></script>
