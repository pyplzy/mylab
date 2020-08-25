package usi.dbdp.portal.help.controller;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;

import usi.dbdp.portal.dto.ContactUsInfo;
import usi.dbdp.portal.dto.FeedbackDto;
import usi.dbdp.portal.entity.FileMeta;
import usi.dbdp.portal.help.service.HelpService;
import usi.dbdp.portal.util.JacksonUtil;
import usi.dbdp.uic.dto.PageObj;

@Controller
@RequestMapping("/help")
public class HelpController {

	@Resource
	private HelpService helpService;

	/**
	 * 联系我们页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/menu_contact_contactUs.do", method = RequestMethod.GET)
	public String contactUsPage(Model model) {
		//查询联系我们信息
		ContactUsInfo contactUsInfo = helpService.getContactUsInfo();
		model.addAttribute("contactUsInfo", contactUsInfo);
		return "portal/help/contact_us";
	}

	/**
	 * 问题反馈查询页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/menu_feed_feedbackList.do", method = RequestMethod.GET)
	public String feedbackListPage(Model model) {
		return "portal/help/feedback_list";
	}

	/**
	 * 保存问题反馈
	 * @param feedbackDto
	 * @return
	 */
	@RequestMapping(value = "/menu_feed_saveFeedback.do", method = RequestMethod.POST)
	@ResponseBody
	public String saveFeedback(HttpSession session,FeedbackDto feedbackDto,@RequestParam(value="fileIds[]",required=false) Long[] fileIds) {
		String result = "fail";
		try {
			String userId = (String) session.getAttribute("userId");
			String userName = (String)session.getAttribute("userName");
			Long orgId = Long.valueOf(session.getAttribute("orgId").toString());
			String orgName = (String)session.getAttribute("orgName");
			feedbackDto.setOrgId(orgId);
			feedbackDto.setOrgName(orgName);
			feedbackDto.setUserId(userId);
			feedbackDto.setUserName(userName);
			helpService.saveFeedback(feedbackDto,fileIds);
			result = "succ";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 查询信息变更日志
	 * @param changeLogInfo  信息变更日志dto
	 * @param pageObj  分页对象
	 * @return
	 */
	@RequestMapping(value = "/menu_feed_getFeedbackList.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getFeedbackList(FeedbackDto feedbackDto, PageObj pageObj) {
		return helpService.getFeedbackList(feedbackDto, pageObj);
	}
	
	/**
	 * 查看反馈信息，用于处理
	 * @param feedbackId
	 * @return
	 */
	@RequestMapping(value = "/menu_feed_getFeedbackDetail.do", method = RequestMethod.POST)
	@ResponseBody
	public FeedbackDto getFeedbackDetail(Long feedbackId) {
		return helpService.getFeedbackDetail(feedbackId);
	}
	
	/**
	 * 保存处理信息
	 * @param feedbackDto
	 * @return
	 */
	@RequestMapping(value = "/menu_feed_saveHandle.do", method = RequestMethod.POST)
	@ResponseBody
	public String saveHandle(FeedbackDto feedbackDto) {
		String result = "fail";
		try {
			helpService.saveHandle(feedbackDto);
			result = "succ";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 上传问题反馈附件
	 * @param request
	 * @param response
	 * @param session
	 * @param pw
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value="/menu_feed_upload.do", method = RequestMethod.POST)
	public void upload(MultipartHttpServletRequest request, HttpServletResponse response,HttpSession session,PrintWriter pw) throws JsonProcessingException{
		try {
			long staffId = (Long) session.getAttribute("staffId");
			List<MultipartFile> fileList = request.getFiles("files[]");
			FileMeta file = helpService.uploadFile(staffId,fileList);
			//设置字符集防止文件名中文乱码
			response.setContentType("text/html;charset=UTF-8");
			pw.write(JacksonUtil.obj2json(file));
		} catch (Exception e) {
			e.printStackTrace();
			pw.write(JacksonUtil.obj2json(new FileMeta()));
		}
	}
}
