package usi.dbdp.portal.task.controller;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import usi.dbdp.portal.task.dto.SysJobDto;
import usi.dbdp.portal.task.dto.SysNodeDto;
import usi.dbdp.portal.task.dto.SysTimerDto;
import usi.dbdp.portal.task.service.QuartzService;
import usi.dbdp.portal.task.service.TimeTaskService;
import usi.dbdp.uic.dto.PageObj;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/task")
public class TimeTaskController {
	private static final Logger LOGGER = LogManager.getLogger(TimeTaskController.class);
	
	@Resource
	private TimeTaskService taskService;
	@Resource
	private QuartzService quartzService;
	
	/** 跳转到定时器菜单*/
 	@RequestMapping(value="/menu_index_index.do", method = RequestMethod.GET)
	public String index(HttpSession session,Model model){
 		model.addAttribute("jobs", taskService.getJobs());
 		model.addAttribute("nodes", taskService.getNodes());
		return "portal/system/task";
	}
 	
 	/**查询所有任务job*/
 	@RequestMapping(value = "/menu_index_getJobs.do")
 	@ResponseBody
 	public List<SysJobDto> getJobs(HttpSession session){
 		return taskService.getJobs();
 	}
 	/**查询所有节点node*/
 	@RequestMapping(value = "/menu_index_getNodes.do")
 	@ResponseBody
 	public List<SysNodeDto> getNodes(HttpSession session){
 		return  taskService.getNodes();
 	}
 	/**条件查询定时任务task*/
 	@RequestMapping(value = "/getTask.do")
 	@ResponseBody
 	public List<SysTimerDto> getNodes(HttpSession session,SysTimerDto tt){
 		return taskService.getTasks(tt);
 	}
 	/**分页、条件查询定时任务task*/
 	@RequestMapping(value = "/menu_index_getTaskbypage.do")
 	@ResponseBody
 	public Map<String,Object> getNodesByPage(HttpSession session,SysTimerDto tt,PageObj pageObj){
 		return taskService.getTasksByPage(tt,pageObj);
 	}
 	/**新增任务*/
 	@RequestMapping(value = "/menu_index_addNjob.do")
 	@ResponseBody
 	public long addNjob(SysJobDto job){
 		try{
 			return taskService.addJob(job);
 		}catch(Exception e){
 			e.printStackTrace();
 			return 0;
 		}
 	}
 	/**新增节点*/
 	@RequestMapping(value = "/menu_index_addNode.do")
 	@ResponseBody
 	public long addNode(SysNodeDto node){
 		try{
 			return taskService.addNode(node);
 		}catch(Exception e){
 			e.printStackTrace();
 			return 0;
 		}
 	}
 	/**新增定时器*/
 	@RequestMapping(value = "/menu_index_addTask.do")
 	@ResponseBody
 	public long addTask(HttpServletRequest request,HttpSession session,SysTimerDto t){
 		try{
 			String userId = (String) request.getSession().getAttribute("userId");
//			SaveSysLogDto saveSysLogDto = new SaveSysLogDto();
//			saveSysLogDto.setUserId(userId);
//			saveSysLogDto.setOptObj("sys_timer");
//			saveSysLogDto.setOptType("insert");
//			InetAddress ia = null;
//			ia = ia.getLocalHost();
//			String localIp=ia.getHostAddress();//获取操作电脑的的ip地址
//			LOGGER.info("本机ip地址为【{}】",localIp);
//			saveSysLogDto.setOptIp(localIp);
//			saveSysLogDto.setOptContent("sys_timer:"+JacksonUtil.obj2json(t));
//			saveSysLogService.insertSysLog(saveSysLogDto);
 			return taskService.addTask(t);
 		}catch(Exception e){
 			e.printStackTrace();
 			return 0;
 		}
 	}
 	/**修改任务*/
 	@RequestMapping(value = "/menu_index_modjob.do")
 	@ResponseBody
 	public int modNjob(SysJobDto job){
 		try{
 			return taskService.modJob(job);
 		}catch(Exception e){
 			e.printStackTrace();
 			return 0;
 		}
 	}
 	/**修改节点*/
 	@RequestMapping(value = "/menu_index_modnode.do")
 	@ResponseBody
 	public int modNjob(SysNodeDto node){
 		try{
 			return taskService.modNode(node);
 		}catch(Exception e){
 			e.printStackTrace();
 			return 0;
 		}
 	}
 	
 	/**修改定时器*/
 	@RequestMapping(value = "/menu_index_modtask.do")
 	@ResponseBody
 	public int modNjob(HttpServletRequest request,HttpSession session,SysTimerDto t){
 		try{
 			String userId = (String) request.getSession().getAttribute("userId");
//			SaveSysLogDto saveSysLogDto = new SaveSysLogDto();
//			saveSysLogDto.setUserId(userId);
//			saveSysLogDto.setOptObj("sys_timer");
//			saveSysLogDto.setOptType("update");
//			InetAddress ia = null;
//			ia = ia.getLocalHost();
//			String localIp=ia.getHostAddress();//获取操作电脑的的ip地址
//			LOGGER.info("本机ip地址为【{}】",localIp);
//			saveSysLogDto.setOptIp(localIp);
//			SysTimerDto result = taskService.queryFormerTimer(t, shardingId);
//			saveSysLogDto.setOptContent("sys_timer:原记录="+JacksonUtil.obj2json(result)+"新记录="+JacksonUtil.obj2json(t));
//			saveSysLogService.insertSysLog(saveSysLogDto);
 			return  taskService.modTask(t);
 		}catch(Exception e){
 			e.printStackTrace();
 			return 0;
 		}
 	}
 	
	/**删除任务*/
	@RequestMapping(value = "/menu_index_deljob.do")
	@ResponseBody
	public int deljob(Integer jobId){
		try{
			return taskService.delJob(jobId);
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
 	
	/**删除节点*/
	@RequestMapping(value = "/menu_index_delnode.do")
	@ResponseBody
	public int delnode(Integer appNodeId){
		try{
			return taskService.delNode(appNodeId);
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	/**删除定时器*/
	@RequestMapping(value = "/menu_index_deltask.do")
	@ResponseBody
	public int delTask(HttpServletRequest request,HttpSession session,Integer timerId){
		try{
			String userId = (String) request.getSession().getAttribute("userId");
//			SaveSysLogDto saveSysLogDto = new SaveSysLogDto();
//			saveSysLogDto.setUserId(userId);
//			saveSysLogDto.setOptObj("sys_timer");
//			saveSysLogDto.setOptType("delete");
//			InetAddress ia = null;
//			ia = ia.getLocalHost();
//			String localIp=ia.getHostAddress();//获取操作电脑的的ip地址
//			LOGGER.info("本机ip地址为【{}】",localIp);
//			saveSysLogDto.setOptIp(localIp);
//			saveSysLogDto.setOptContent("sys_timer:UPDATE SYS_TIMER SET DEL_FLAG=1 WHERE TIMER_ID="+timerId);
//			saveSysLogService.insertSysLog(saveSysLogDto);
			return taskService.delTask(timerId);
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	/** 停止单个定时任务 */
	@RequestMapping(value="/menu_index_stopTask.do",method=RequestMethod.POST,produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String stopSingleTask(String taskString,HttpServletRequest req) throws Exception{
		try {
			String uri=req.getRequestURI();
			SysTimerDto task = JSONObject.parseObject(taskString, SysTimerDto.class);
			return taskService.stopSingleTask(task,uri.substring(0, uri.lastIndexOf("/")));
		} catch (Exception e) {
			e.printStackTrace();
			return  "<span style=\"color:red;\">" + e.getMessage() + "</span>";
		}
	}
	
	/** 停止所有定时任务 */
	@RequestMapping(value="/stopAllTask.do",method=RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String stopAllTasks(String taskStrings,HttpServletRequest req) throws Exception{
		String uri=req.getRequestURI();
		List<SysTimerDto> tasks = JSONObject.parseArray(taskStrings,SysTimerDto.class);
		return taskService.stopAllTasks(tasks,uri.substring(0, uri.lastIndexOf("/")));
	}
	
	/** 单个启动定时任务 */
	@RequestMapping(value="/menu_index_startTask.do",method=RequestMethod.POST,produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String startJobController(String taskString,HttpServletRequest req) throws Exception{
		try {
			String uri=req.getRequestURI();
			SysTimerDto task =JSONObject.parseObject(taskString, SysTimerDto.class);
			return  taskService.startSingleTask(task,uri.substring(0, uri.lastIndexOf("/")));
		} catch (Exception e) {
			return  "<span style=\"color:red;\">" +  e.getMessage() + "</span>";
		}
	}
	
	/** 启动所定时任务 */
	@RequestMapping(value="/startAllTask.do",method=RequestMethod.POST,produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String startAllTasks(String taskStrings,HttpServletRequest req) throws Exception{
		String uri=req.getRequestURI();
		List<SysTimerDto> tasks = JSONObject.parseArray(taskStrings,SysTimerDto.class);
		return taskService.startAllTasks(tasks,uri.substring(0, uri.lastIndexOf("/")));
	}
	
	/** 远程启动本地定时任务 */
	@RequestMapping(value="/startTask.refresh",method=RequestMethod.POST ,produces="text/json;charset=UTF-8")
	public void startLocalJobController(HttpServletRequest request,String taskString,PrintWriter writer) throws Exception{
		SysTimerDto task = JSONObject.parseObject(taskString, SysTimerDto.class);
	    Map<String,Object> map =quartzService.startJob(task);
	    if((Boolean) map.get("flag")){
	    	writer.write("启动成功！");
	    }else{
	    	writer.write((String)map.get("failCause"));
	    }
	}
	
	/** 远程停止本地定时任务 */
	@RequestMapping(value="/stopTask.refresh",method=RequestMethod.POST,produces="text/json;charset=UTF-8")
	public void stopLocalJobController(String taskString,PrintWriter writer) throws Exception{
		SysTimerDto task = JSONObject.parseObject(taskString, SysTimerDto.class);
	    Map<String,Object> map =quartzService.stopJob(task);    
	    if((Boolean) map.get("flag")){
	    	writer.write("停止成功！");
	    }else{
	    	writer.write((String)map.get("failCause")); 
	    }
	}
	
}
