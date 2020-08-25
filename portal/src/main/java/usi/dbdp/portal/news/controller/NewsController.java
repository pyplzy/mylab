package usi.dbdp.portal.news.controller;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;

import usi.dbdp.portal.dto.NewsDto;
import usi.dbdp.portal.dto.NewsReplyDto;
import usi.dbdp.portal.news.service.NewsReplyService;
import usi.dbdp.portal.news.service.NewsService;
import usi.dbdp.portal.util.CommonUtil;
import usi.dbdp.portal.util.JacksonUtil;
import usi.dbdp.uic.dto.PageObj;

@Controller
@RequestMapping("/news")
public class NewsController {
	@Resource
	private NewsService newsService;
	@Resource
	private NewsReplyService newsReplyService;
	
	/**
	 * 前往新建新闻
	 * @param model
	 * @param bulletinId
	 * @return
	 */
	@RequestMapping(value="/menu_new_new.do", method = RequestMethod.GET)
	public String newBulletin(Model model, Long newsId){
		NewsDto news = null;
		if(newsId != null) {
			news = newsService.queryNewsById(newsId);
			if(news == null) {
				news = new NewsDto();
				news.setNewsId(-1L);
				news.setReplyFlag(0);
			}
		} else {
			news = new NewsDto();
			news.setNewsId(-1L);
			news.setReplyFlag(0);
		}
		model.addAttribute("news", news);
		return "portal/news/new";
	}
	/**
	 * 浏览新闻（读者阅览）
	 * @return
	 */
	@RequestMapping(value = "/menu_new_browse.do", method = RequestMethod.GET)
	public String browse(HttpSession session, Model model, long newsId) {
		long staffId =  (Long)session.getAttribute("staffId");
		newsService.saveVisitLog(staffId, newsId);
		NewsDto news = newsService.queryNewsById(newsId);
		model.addAttribute("news", news);
		return "portal/news/browse";
	}
	
	
	/**
	 * 去草稿箱
	 * @return
	 */
	@RequestMapping(value="/menu_draftbox_draftbox.do", method = RequestMethod.GET)
	public String toDraftBox(){
		return "portal/news/draftBox";
	}
	
	/**
	 * 存草稿和发布
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/menu_new_postNews.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> postNews(HttpSession session,NewsDto news) {
		Map<String, Object> msgMap = new HashMap<String, Object>();
		try {
			String userId = (String) session.getAttribute("userId");
			String userName = (String)session.getAttribute("userName");
			Long orgId = Long.valueOf(session.getAttribute("orgId").toString());
			String orgName = (String)session.getAttribute("orgName");
			news.setUserId(userId);
			news.setUserName(userName);
			news.setOrgId(orgId);
			news.setOrgName(orgName);
			
			news.setTitle(StringEscapeUtils.escapeHtml4(news.getTitle()));
			news.setContent(Jsoup.clean(news.getContent(), Whitelist.basicWithImages().addAttributes("p","style","title","align")));

			if(news.getNewsId() == -1) {
				msgMap.put("newsId", newsService.saveNotice(news));
			} else {
				newsService.updateNews(news);
				msgMap.put("newsId", news.getNewsId());
			}
			msgMap.put("msgFlag", true);
		} catch (Exception e) {
			e.printStackTrace();
			msgMap.put("msgFlag", false);
		}
		return msgMap;
	}
	
	/**
	 * 分页加载草稿新闻
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/menu_new_getDrafts.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getDrafts(HttpSession session,PageObj pageObj) {
		String userId=(String) session.getAttribute("userId");
		return newsService.getDraftsByUserId(userId, pageObj );
	}
	
	/**
	 * 预览新闻
	 * @param notice
	 * @return
	 */
	@RequestMapping(value = "/menu_new_preview.do", method = RequestMethod.POST)
	public String preview(NewsDto news, Model model, HttpSession session) {
		String userName = (String) session.getAttribute("userName");
		if(!CommonUtil.hasValue(news.getTitle())) {
			news.setTitle("无标题");
		}
		news.setCreateTime(new Date());
		news.setUserName(userName);
		model.addAttribute("news", news);
		return "portal/news/preview";
	}
	
	/**
	 * 删除草稿
	 * @param request
	 * @param session
	 * @param noticeIds
	 * @return
	 */
	@RequestMapping(value = "/menu_new_removeDrafts.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean removeDrafts(@RequestBody long[] noticeIds) {
		try {
			newsService.batchDeleteNotice(noticeIds);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 批量修改新闻至发布状态
	 * @param bulletinIds
	 * @return 
	 */
	@RequestMapping(value = "/menu_new_publishItems.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean publishItems(@RequestBody long[] noticeIds) {
		try {
			newsService.batchUpdateToPublishedStatus(noticeIds);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 根据条件查出所有的已发布新闻
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/menu_new_getPublished.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getPublished(NewsDto newsDto, PageObj pageObj) {
		return newsService.queryNewsPublishedByUserId(newsDto, pageObj);
	}
	
	/**
	 * 去新闻管理页面
	 * @return
	 */
	@RequestMapping(value="/menu_manage_manage.do", method = RequestMethod.GET)
	public String toNoticeManage(){
		return "portal/news/manage";
	}
	
	
	/**
	 * 从新闻管理页面查看新闻详情
	 * @param session
	 * @param model
	 * @param newsId
	 * @return
	 */
	@RequestMapping(value = "/menu_new_newsDetail.do", method = RequestMethod.GET)
	public String newsDetail(HttpSession session, Model model, long newsId) {
		long staffId =  (Long)session.getAttribute("staffId");
		newsService.saveVisitLog(staffId, newsId);
		NewsDto news = newsService.queryNewsById(newsId);
		PageObj pageObj=new PageObj(10, 1);
		List<NewsReplyDto> replys = newsReplyService.queryReplysByNewsIdWithPage(newsId,pageObj);
		model.addAttribute("news", news);
		model.addAttribute("replys", replys);
		model.addAttribute("total", pageObj.getTotal());
		return "portal/news/newsDetail";
	}
	
	/**
	 * 分页查出某个新闻的回复
	 * @param noticeId
	 * @return
	 */
	@RequestMapping(value = "/menu_new_queryReplys.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String , Object > queryReplysByNewsIdWithPage(long newsId , PageObj pageObj){
		Map<String , Object > map =new HashMap<String , Object >();
		if(pageObj.getRows()==0){
			pageObj.setRows(10);
		}
		if(pageObj.getPage()==0){			
			pageObj.setPage(1);
		}
		map.put("replys",newsReplyService.queryReplysByNewsIdWithPage(newsId,pageObj));
		map.put("pageObj",pageObj);
		return map ;
	}
	
	/**
	 * 关闭回复
	 * @param newsId
	 * @return
	 */
	@RequestMapping(value = "/menu_new_closeReply.do", method = RequestMethod.GET)
	@ResponseBody
	public boolean closeReply(long newsId) {
		try {
			newsService.closeReply(newsId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 开启回复
	 * @param newsId
	 * @return
	 */
	@RequestMapping(value = "/menu_new_openReply.do", method = RequestMethod.GET)
	@ResponseBody
	public boolean openReply(long newsId) {
		try {
			newsService.openReply(newsId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 拷贝新闻到草稿箱
	 * @param request
	 * @param session
	 * @param newsId
	 * @return
	 */
	@RequestMapping(value = "/menu_new_copyNewsToDraft.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean copyToDraft(long newsId) {
		try {
			newsService.copyToDraft(newsId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 重新发布公告
	 * @param request
	 * @param session
	 * @param bulletinId
	 * @return
	 */
	@RequestMapping(value = "/rePublish.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean rePublish(long bulletinId, String loseTime) {
		try {
			newsService.publishNotice(bulletinId, loseTime);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 批量删除新闻
	 * @param request
	 * @param session
	 * @param newsIds
	 * @return
	 */
	@RequestMapping(value = "/removePublisheds.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean removePublisheds(@RequestBody long[] newsIds) {
		try {
			newsService.batchDeleteNews(newsIds);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping(value="/menu_new_upload.do", method = RequestMethod.POST)
	public void upload(MultipartHttpServletRequest request, HttpServletResponse response,HttpSession session,PrintWriter pw) throws JsonProcessingException{
		try {
			long staffId = (Long) session.getAttribute("staffId");
			
			//只支持单个文件上传
			MultipartFile multipartFile = request.getFile("upfile");
			Map<String,String> fileMeta = newsService.uploadFile(staffId,multipartFile);
			//设置字符集防止文件名中文乱码
			response.setContentType("text/html;charset=UTF-8");
			pw.write(JacksonUtil.obj2json(fileMeta));
		} catch (Exception e) {
			e.printStackTrace();
			Map<String,String> fileMeta = new HashMap<String,String>();
			fileMeta.put("state", "系统异常");
			fileMeta.put("url", "");
			pw.write(JacksonUtil.obj2json(fileMeta));
		}
	}
	
	/**
	 * 根据id删除该新闻
	 * @param newsId
	 * @return
	 */
	@RequestMapping(value = "/menu_new_deleteNewsById.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean removeNews(long newsId){
		try {
			newsService.deleteNewsById(newsId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
