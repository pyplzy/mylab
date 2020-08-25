<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
   	<style type="text/css">
	.w416{
	    width:416px;
	}
	.w360{
	    width:348px;
	}
	.w316{
	    width:316px;
	}
	.task400{
		width:400px;
	}
	.jobmask{
		background: #000 none repeat scroll 0 0;
	    display: none;
	    left: 0;
	    opacity: 0.3;
	    overflow: hidden;
	    position: absolute;
	    top: 0;
	    z-index: 10;
	}
	.jobzindex{
		z-index: 11;
	}
	.hide{
		display: none;
	}
    </style>
<div class="popWrap" style="height:560px;width:600px;" id="taskFrom">
	<div class="titleName clearfix"><span class="left" id="uicTimeText">新建资源</span><i class="icon_close right"  id="taskDialogResClose"></i></div>
    <div class="conto clearfix">
    	<input type="hidden" id="uicTimeId"/>
    	<dl>
        	<dt><span style="color:red;">*</span>任务</dt>
            <dd>
            	<div class="w360">
                    <select  class="dropdown" id="uciJobList">
						<option value="-1" >---请选择---</option>
                    </select>
                </div>
            </dd>
            &nbsp;&nbsp;
            <a href="####" class="btn" id="timeTask"><span class="lf_btn"></span><span class="rf_btn">添加</span></a>
        </dl>
        <dl>
        	<dt><span style="color:red;">*</span>节点</dt>
            <dd>
            	<div class="w360">
                    <select  class="dropdown" id="uciNodeList">
						<option value="-1" >---请选择---</option>
                    </select>
                </div>
            </dd>
            &nbsp;&nbsp;
            <a href="####" class="btn" id="timeNode"><span class="lf_btn"></span><span class="rf_btn">添加</span></a>
        </dl>
        <dl>
        	<dt><span style="color:red;">*</span>定时器名称</dt>
            <dd><input type="text" class="txtInput" id="uicTimeName" style="width:400px;"/></dd>
        </dl>
        <dl>
        	<dt><span style="color:red;">*</span>执行类全名</dt>
            <dd><input type="text" class="txtInput" id="uicClassName" style="width:400px;" placeholder="请实现接口：usi.dbdp.sys.task.service.jobService.ExecuteTask"/></dd>
        </dl>
        <dl>
        	<dt><span style="color:red;">*</span>执行方法名</dt>
            <dd><input type="text" class="txtInput" id="excuteMethod" style="width:400px;" placeholder="为接口的实现方法：runTask" /></dd>
        </dl>
        <dl>
        	<dt><span style="color:red;">*</span>运行时间</dt>
            <dd><input type="text"  class="txtInput" id="cronExpression" style="width:400px;"placeholder="0/5 * * * * ?(quartz的corn表达式)" /></dd>
        </dl>
        <dl>
        	<dt><span style="color:red;">*</span>业务参数</dt>
            <dd><textarea  class="textArea task400" id="uicBusiPara" placeholder="定时器中需要使用到的参数，建议按照json格式输入" ></textarea></dd>
        </dl>
         <dl>
        	<dt><span style="color:red;">*</span>启用状态</dt>
            <dd>
            	<div class="w416">
                    <select  class="dropdown" id="uicEnable">
						<option value="-1" >---请选择---</option>
						<option value="1" >启用</option>
						<option value="0" >停用</option>
                    </select>
                </div>
            </dd>
        </dl>
    	<dl>
        	<dt>任务描述</dt>
            <dd><textarea class="textArea task400" id="uicTimerDesc"></textarea></dd>
        </dl>
    	<dl class="mt5">
        	<dt>&nbsp;</dt>
            <dd>
            	<a href="####" class="btn btn_sp" id="taskFromSave"><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
                <a href="####" class="btn" id="taskFormDel"><span class="lf_btn"></span><span class="rf_btn">取消</span></a>
            </dd>
        </dl>
    </div>
</div>
<div class="popWrap jobzindex" id="timeJob" style="height:290px;width:520px; margin-left: -239px;margin-top: -160px;" >
	<div class="titleName clearfix"><span class="left" id="taskDialText">新建资源</span><i class="icon_close right" id="closeTimeJob"></i></div>
	<div class="conto clearfix">
		<input type="hidden" id="jobId"/>
		<dl>
        	<dt><span style="color:red;">*</span>任务名称</dt>
            <dd><input type="text" id="jobName" class="txtInput"/></dd>
        </dl>
        <dl>
        	<dt><span style="color:red;">*</span>任务编码</dt>
            <dd><input type="text" id="jobCd" class="txtInput"/></dd>
        </dl>
		<dl>
        	<dt><span style="color:red;">*</span>启用状态</dt>
            <dd>
            	<div class="w316">
                    <select  class="dropdown" id="jobEnable">
						<option value="-1" >---请选择---</option>
						<option value="1" >启用</option>
						<option value="0" >停用</option>
                    </select>
                </div>
            </dd>
        </dl>
		<dl>
        	<dt>说明</dt>
            <dd><textarea class="textArea" id="jobContent"></textarea></dd>
        </dl>
        <dl class="mt5">
        	<dt>&nbsp;</dt>
            <dd>
            	<a href="####" class="btn btn_sp" id="uicJobSave"><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
                <a href="####" class="btn" id="uicJobRemove"><span class="lf_btn"></span><span class="rf_btn">删除</span></a>
                <a href="####" class="btn" id="uicJobDel"><span class="lf_btn"></span><span class="rf_btn">取消</span></a>
            </dd>
        </dl>
	</div>
</div>
<div class="popWrap jobzindex" id="timeNodeDial" style="height:325px;width:520px; margin-left: -239px;margin-top: -160px;" >
	<div class="titleName clearfix"><span class="left" id="nodeDialText">新建资源</span><i class="icon_close right" id="closetimeNode"></i></div>
	<div class="conto clearfix">
		<input type="hidden" id="uicNodeId"/>
		<dl>
        	<dt><span style="color:red;">*</span>节点ip</dt>
            <dd><input type="text" id="uicIpAddress" class="txtInput"/></dd>
        </dl>
        <dl>
        	<dt><span style="color:red;">*</span>节点端口</dt>
            <dd><input type="text" id="uicPortValue" class="txtInput"/></dd>
        </dl>
        <dl>
        	<dt><span style="color:red;">*</span>节点名称</dt>
            <dd><input type="text" id="uicNodeName" class="txtInput"/></dd>
        </dl>
		<dl>
        	<dt><span style="color:red;">*</span>节点类型</dt>
            <dd>
            	<div class="w316">
                    <select  class="dropdown" id="uicNodeType">
						<option value="-1" >---请选择---</option>
						<option value="1" >应用节点</option>
                    </select>
                </div>
            </dd>
        </dl>
		<dl>
        	<dt>说明</dt>
            <dd><textarea class="textArea" id="uicNodeContent"></textarea></dd>
        </dl>
        <dl class="mt5">
        	<dt>&nbsp;</dt>
            <dd>
            	<a href="####" class="btn btn_sp" id="uicNodeSave"><span class="lf_btn"></span><span class="rf_btn">保存</span></a>
                <a href="####" class="btn" id="uicNodeRemove"><span class="lf_btn"></span><span class="rf_btn">删除</span></a>
                <a href="####" class="btn" id="uicNodeDel"><span class="lf_btn"></span><span class="rf_btn">取消</span></a>
            </dd>
        </dl>
	</div>
</div>
<div class="jobmask"></div><!--遮罩层-->
<script type="text/javascript" src="${ctx}/res/javascript/portal/dialog/taskDialog.js" ></script>
