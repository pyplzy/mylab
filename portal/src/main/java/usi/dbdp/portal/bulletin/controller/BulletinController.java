package usi.dbdp.portal.bulletin.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import usi.dbdp.portal.bulletin.service.BulletinReplyService;
import usi.dbdp.portal.bulletin.service.BulletinService;
import usi.dbdp.portal.dto.BulletinDto;
import usi.dbdp.portal.dto.BulletinReplyDto;
import usi.dbdp.portal.entity.PtlBulletin;
import usi.dbdp.portal.util.CommonUtil;
import usi.dbdp.uic.dto.PageObj;

/**
 * 
 * @author nie.zhengqian
 * 创建时间：2015年4月11日 上午9:33:41
 */
@Controller
@RequestMapping("/bulletin")
public class BulletinController {
	@Resource
	private BulletinService bulletinService;
	@Resource
	private BulletinReplyService bulletinReplyService;
	
	/**
	 * 前往新建公告
	 * @param model
	 * @param bulletinId
	 * @return
	 */
	@RequestMapping(value="/menu_newBulletin_newBulletin.do", method = RequestMethod.GET)
	public String newBulletin(Model model, Long bulletinId){
		BulletinDto bulletin = null;
		if(bulletinId != null) {
			bulletin = bulletinService.queryNoticeById(bulletinId);
			if(bulletin == null) {
				bulletin = new BulletinDto();
				bulletin.setBulletinId(-1);
			}
		} else {
			bulletin = new BulletinDto();
			bulletin.setBulletinId(-1);
		}
		model.addAttribute("bulletin", bulletin);
		return "portal/bulletin/newBulletin";
	}
	/**
	 * 去草稿箱
	 * @return
	 */
	@RequestMapping(value="/menu_draft_draftBox.do", method = RequestMethod.GET)
	public String toDraftBox(){
		return "portal/bulletin/draftBox";
	}
	/**
	 * 去我的公告
	 * @return
	 */
	@RequestMapping(value="/menu_myNotice_myNotice.do", method = RequestMethod.GET)
	public String toMyNotice(){
		return "portal/bulletin/myNotice";
	}
	
	/**
	 * 存草稿和发布
	 * @param bulletin
	 * @return
	 */
	@RequestMapping(value = "/menu_newBulletin_postNotice.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> postBulletin(PtlBulletin bulletin) {
		Map<String, Object> msgMap = new HashMap<String, Object>();
		try {
			bulletin.setTitle(StringEscapeUtils.escapeHtml4(bulletin.getTitle()));
			bulletin.setContent(Jsoup.clean(bulletin.getContent(), Whitelist.basicWithImages().addAttributes("p","style","title","align")));
			if(bulletin.getBulletinId() == -1) {
				msgMap.put("bulletinId", bulletinService.saveNotice(bulletin));
			} else {
				bulletinService.updateNotice(bulletin);
				msgMap.put("bulletinId", bulletin.getBulletinId());
			}
			msgMap.put("msgFlag", true);
		} catch (Exception e) {
			e.printStackTrace();
			msgMap.put("msgFlag", false);
		}
		return msgMap;
	}
	/**
	 * 查出所有的草稿
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/menu_draft_getDrafts.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getDrafts(long createStaff, PageObj pageObj) {
		return bulletinService.getDraftsByCreateStaff(createStaff, pageObj );
	}
	
	/**
	 * 预览公告
	 * @param notice
	 * @return
	 */
	@RequestMapping(value = "/menu_myNotice_previewNotice.do")
	public String previewNotice(BulletinDto bulletin, Model model, HttpSession session) {
		String userName = (String) session.getAttribute("userName");
		if(!CommonUtil.hasValue(bulletin.getTitle())) {
			bulletin.setTitle("无标题");
		}
		if(bulletin.getBulletinId() != -1) {
			//查找附件
			bulletin.setFiles(bulletinService.queryNoticeById(bulletin.getBulletinId()).getFiles());
		}
		bulletin.setCreateDate(CommonUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		bulletin.setCreateName(userName);
		model.addAttribute("bulletin", bulletin);
		return "portal/bulletin/noticePreview";
	}
	
	/**
	 * 删除草稿
	 * @param request
	 * @param session
	 * @param noticeIds
	 * @return
	 */
	@RequestMapping(value = "/menu_draft_removeDrafts.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean removeDrafts(HttpServletRequest request, HttpSession session, @RequestBody long[] noticeIds) {
		try {
			Long staffId = (Long)session.getAttribute("staffId");
			String userName = (String)session.getAttribute("userName");
			Long orgId = (Long)session.getAttribute("orgId");
			bulletinService.batchDeleteNotice(noticeIds, staffId,userName,orgId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 批量修改公告至发布状态
	 * @param bulletinIds
	 * @return 
	 */
	@RequestMapping(value = "/menu_myNotice_publishNoticeItems.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean publishNoticeItems(@RequestBody long[] noticeIds) {
		try {
			bulletinService.batchUpdateToPublishedStatus(noticeIds);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 查出所有的已发布公告
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/menu_myNotice_getPublished.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getPublished(long staffId, String offline ,PageObj pageObj) {
		
		return bulletinService.queryNoticePublishedByStaffId(staffId, offline, pageObj);
	}
	/**
	 * 去我的公告
	 * @return
	 */
	@RequestMapping(value="/menu_notice_noticeManage.do", method = RequestMethod.GET)
	public String toNoticeManage(){
		return "portal/bulletin/noticeManage";
	}

	/**
	 * 浏览公告（读者阅览）
	 * @return
	 */
	@RequestMapping(value = "/menu_notice_browseNotice.do", method = RequestMethod.GET)
	public String browseNotice(HttpSession session, Model model, long noticeId) {
		long staffId =  (Long)session.getAttribute("staffId");
		bulletinService.saveVisitLog(staffId, noticeId);
		BulletinDto notice = bulletinService.queryNoticeById(noticeId);
		PageObj pageObj=new PageObj(10, 1);
		List<BulletinReplyDto> replys = bulletinReplyService.queryReplysByBulletinIdWithPage(noticeId,pageObj);
		model.addAttribute("notice", notice);
		model.addAttribute("replys", replys);
		model.addAttribute("total", pageObj.getTotal());
		model.addAttribute("fileSize", notice.getFiles().size());
		return "portal/bulletin/notice-browse";
	}
	
	/**
	 * 分页查出某个公告的回复
	 * @param noticeId
	 * @return
	 */
	@RequestMapping(value = "/menu_notice_queryReplys.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String , Object > queryReplysByBulletinIdWithPage(long bulletinId , PageObj pageObj){
		Map<String , Object > map =new HashMap<String , Object >();
		if(pageObj.getRows()==0){
			pageObj.setRows(10);
		}
		if(pageObj.getPage()==0){			
			pageObj.setPage(1);
		}
		map.put("replys",bulletinReplyService.queryReplysByBulletinIdWithPage(bulletinId,pageObj));
		map.put("pageObj",pageObj);
		return map ;
	}
	
	/**
	 * 关闭回复
	 * @param bulletinId
	 * @return
	 */
	@RequestMapping(value = "/menu_notice_closeReply.do", method = RequestMethod.GET)
	@ResponseBody
	public boolean closeReply(long bulletinId) {
		try {
			bulletinService.closeReply(bulletinId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * 开启回复
	 * @param bulletinId
	 * @return
	 */
	@RequestMapping(value = "/menu_notice_openReply.do", method = RequestMethod.GET)
	@ResponseBody
	public boolean openReply(long bulletinId) {
		try {
			bulletinService.openReply(bulletinId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 拷贝公告到草稿箱
	 * @param request
	 * @param session
	 * @param bulletinIds
	 * @return
	 */
	@RequestMapping(value = "/menu_notice_copyToDraft.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean copyToDraft(long bulletinId) {
		try {
			bulletinService.copyToDraft(bulletinId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 手动下线公告
	 * @param request
	 * @param session
	 * @param bulletinIds
	 * @return
	 */
	@RequestMapping(value = "/menu_notice_downlineNotice.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> downlineNotice(long bulletinId) {
		Map<String, Object> msgMap = new HashMap<String, Object>();
		try {
			Date loseDate = bulletinService.downlineNotice(bulletinId);
			String loseTime = CommonUtil.format(loseDate, "yyyy-MM-dd HH:mm:ss");
			msgMap.put("msgFlag", true);
			msgMap.put("loseTime", loseTime);
		} catch (Exception e) {
			e.printStackTrace();
			msgMap.put("msgFlag", false);
		}
		return msgMap;
	}
	
	/**
	 * 重新发布公告
	 * @param request
	 * @param session
	 * @param bulletinId
	 * @return
	 */
	@RequestMapping(value = "/menu_notice_publishNotice.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean publishNotice(long bulletinId, String loseTime) {
		try {
			bulletinService.publishNotice(bulletinId, loseTime);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 修改下线时间
	 * @param request
	 * @param session
	 * @param bulletinId
	 * @return
	 */
	@RequestMapping(value = "/menu_notice_updateLoseDate.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean updateLoseDate(long bulletinId, String loseTime) {
		try {
			bulletinService.updateLoseDate(bulletinId, loseTime);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 批量删除公告
	 * @param request
	 * @param session
	 * @param bulletinIds
	 * @return
	 */
	@RequestMapping(value = "/menu_notice_removePublisheds.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean removePublisheds(@RequestBody long[] bulletinIds) {
		try {
			bulletinService.batchDeleteNotice(bulletinIds);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 批量将公共修改为删除状态
	 * @param request
	 * @param session
	 * @param bulletinIds
	 * @return
	 */
	@RequestMapping(value = "/menu_notice_deletePublished.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean batchUpdateToDeletedStatus(@RequestBody long[] bulletinIds){
		try {
			bulletinService.batchUpdateToDeletedStatus(bulletinIds);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 置顶公告
	 * @param bulletinId
	 * @return
	 */
	@RequestMapping(value = "/menu_notice_stickNotice.do", method = RequestMethod.GET)
	@ResponseBody
	public boolean stickNotice(long bulletinId) {
		try {
			bulletinService.stickNotice(bulletinId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 取消置顶
	 * @param bulletinId
	 * @return
	 */
	@RequestMapping(value = "/menu_notice_unStickNotice.do", method = RequestMethod.GET)
	@ResponseBody
	public boolean unStickNotice(long bulletinId) {
		try {
			bulletinService.unStickNotice(bulletinId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
