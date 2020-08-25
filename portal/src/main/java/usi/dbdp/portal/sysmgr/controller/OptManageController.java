package usi.dbdp.portal.sysmgr.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import usi.dbdp.portal.dto.OptDto;
import usi.dbdp.portal.dto.ResourceDto;
import usi.dbdp.portal.entity.ChangeLog;
import usi.dbdp.portal.sysmgr.service.LogService;
import usi.dbdp.portal.sysmgr.service.OptManageService;
import usi.dbdp.portal.util.JacksonUtil;
import usi.dbdp.uic.base.service.DataPriService;
import usi.dbdp.uic.dto.PageObj;

/**
 * @author zhqnie
 * @version 2016年12月28日 下午3:07:57
 * 说明  操作权限管理
 */
@Controller
@RequestMapping("/optManage")
public class OptManageController {
	@Resource
	private DataPriService dataPriService;
	@Resource
	private OptManageService optManageService;
	@Resource
	private LogService logService;
	/**
	 * 前往菜单管理页面
	 * @return
	 */
	@RequestMapping(value="/menu_toOpt_toOpt.do", method = RequestMethod.GET)
	public String toMainPageMenu(HttpSession session,Model model){
		String userId = (String)session.getAttribute("userId");
		model.addAttribute("appItems", dataPriService.getDataPrisByUserIdAndPrivilegeType(userId, 1));
		return "portal/system/optManage";
	}
	
	/**
	 * 判断resourceCode是否可用
	 * @param appCode 应用code
	 * @param roleCode 角色code
	 * @return 
	 */
	@RequestMapping(value="/menu_toOpt_queryResource.do", method = RequestMethod.POST)
	@ResponseBody
	public String queryResource(String appCode, String  resourceCode) {
		return optManageService.queryResource(appCode, resourceCode)>0?"use":"yes";
	}
	/**
	 * 删除资源 by resourceId
	 * @param resourceId 
	 * @return 
	 */
	@RequestMapping(value="/menu_toOpt_delResource.do", method = RequestMethod.POST)
	@ResponseBody
	public String delResource(long resourceId,HttpSession session) {
		try {
			if(optManageService.qryOptRes(resourceId)){
				return "use";
			}
			//记录日志
			ChangeLog changeLog = new ChangeLog();
			changeLog.setUserId((String) session.getAttribute("userId"));
			changeLog.setOptObj("资源表");
			changeLog.setOptType(LogService.OPT_DELETE);
			changeLog.setOptIp((String) session.getAttribute("ip"));
			changeLog.setOptContent("uic_resource:update uic_resource set del_flag = 1 where RESOURCE_ID = ? :"+resourceId);
			logService.saveChangeLogInfo(changeLog);
			return optManageService.delResource(resourceId)>0?"succ":"fail";
		} catch (Exception e) {
			return "fail";
		}
		
	}
	/**
	 * 查询资源
	 * @param resourceId 
	 * @return 
	 */
	@RequestMapping(value="/menu_toOpt_queryReById.do", method = RequestMethod.POST)
	@ResponseBody
	public ResourceDto queryReById(long resourceId) {
		return optManageService.queryReById(resourceId);
	}
	/**
	 * 根据appcode查询资源列表
	 * @param resourceId 
	 * @return 
	 */
	@RequestMapping(value="/menu_toOpt_queryResources.do", method = RequestMethod.POST)
	@ResponseBody
	public List<ResourceDto> queryResources(String appCode) {
		return optManageService.queryResources(appCode);
	}
	/**
	 * 查询权限 by resourceId  optId
	 * @param resourceId 
	 * @return 
	 */
	@RequestMapping(value="/menu_toOpt_queryOptById.do", method = RequestMethod.POST)
	@ResponseBody
	public OptDto queryOptById(long resourceId,long optId) {
		return optManageService.queryOptById(resourceId,optId);
	}
	
	/**
	 * 新增和修改资源
	 * @param resourceId 
	 * @return 
	 */
	@RequestMapping(value="/menu_toOpt_addAndEditResource.do", method = RequestMethod.POST)
	@ResponseBody
	public String addAndEditResource(ResourceDto resourceDto,HttpSession session) {
		String flag = "fail";
		try {
			ChangeLog changeLog = new ChangeLog();
			//新增和修改
			if(resourceDto.getResourceId()>0){
				optManageService.editResource(resourceDto);  //修改
				changeLog.setOptType(LogService.OPT_UPDATE);
				ResourceDto oldResource = optManageService.queryOldResource(resourceDto.getResourceId());
				changeLog.setOptContent("uic_resource:原记录="+JacksonUtil.obj2json(oldResource)+",新记录="+JacksonUtil.obj2json(resourceDto));

			}else{
				optManageService.addResource(resourceDto);  //增加
				changeLog.setOptType(LogService.OPT_INSERT);
				changeLog.setOptContent("uic_resource:"+JacksonUtil.obj2json(resourceDto));
			}
			//记录日志
			changeLog.setUserId((String) session.getAttribute("userId"));
			changeLog.setOptObj("资源表");
			changeLog.setOptIp((String) session.getAttribute("ip"));
			logService.saveChangeLogInfo(changeLog);
			flag = "succ";
		} catch (Exception e) {
			e.printStackTrace();
		}
		//记录日志
		return flag;
	}
	
	@RequestMapping(value="/menu_toOpt_qryResources.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String , Object> qryResources(String appCode,PageObj pageObj) {
		return optManageService.qryResources(appCode,pageObj);
	}
	
	@RequestMapping(value="/menu_toOpt_qryOpts.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String , Object> qryOpts(long resourceId,PageObj pageObj) {
		return optManageService.qryOpts(resourceId,pageObj);
	}
	
	/**
	 * 删除optId
	 * @param optId
	 * @return
	 */
	@RequestMapping(value="/menu_toOpt_delOpts.do", method = RequestMethod.POST)
	@ResponseBody
	public String delOpts(long optId,HttpSession session) {
		try {
			if(optManageService.checkOpt(optId)){
				return "use";
			}
			optManageService.delOpts(optId);
			//记录日志
			ChangeLog changeLog = new ChangeLog();
			changeLog.setUserId((String) session.getAttribute("userId"));
			changeLog.setOptObj("操作权限表");
			changeLog.setOptType(LogService.OPT_DELETE);
			changeLog.setOptIp((String) session.getAttribute("ip"));
			changeLog.setOptContent("uic_opt:update uic_opt set del_flag = 1 where PRIVILEGE_ID = ? :"+optId);
			logService.saveChangeLogInfo(changeLog);
			return "succ";
		} catch (Exception e) {
			return "fail";
		}
	}
	
	/**
	 * 新增和修改权限
	 * @param resourceId 
	 * @return 
	 */
	@RequestMapping(value="/menu_toOpt_addAndEditOpt.do", method = RequestMethod.POST)
	@ResponseBody
	public String addAndEditOpt(OptDto optDto,HttpSession session) {
		String flag = "fail";
		try {
			ChangeLog changeLog = new ChangeLog();
			if(optManageService.jugdeCode(optDto)){
				flag = "use";
			}else{
				//新增和修改
				if(optDto.getPrivilegeId()>0){
					optManageService.editOpt(optDto);  //修改
					changeLog.setOptType(LogService.OPT_UPDATE);
					OptDto oldOpt = optManageService.queryOldOpt(optDto.getPrivilegeId());
					changeLog.setOptContent("uic_opt:原记录="+JacksonUtil.obj2json(oldOpt)+",新记录="+JacksonUtil.obj2json(optDto));
				}else{
					optManageService.addOpt(optDto);  //增加
					
					changeLog.setOptType(LogService.OPT_INSERT);
					changeLog.setOptContent("uic_opt:"+JacksonUtil.obj2json(optDto));
				}
				//记录日志
				changeLog.setUserId((String) session.getAttribute("userId"));
				changeLog.setOptObj("操作权限表");
				changeLog.setOptIp((String) session.getAttribute("ip"));
				logService.saveChangeLogInfo(changeLog);
				flag = "succ";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
