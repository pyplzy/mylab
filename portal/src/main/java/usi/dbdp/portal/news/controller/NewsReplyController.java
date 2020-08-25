package usi.dbdp.portal.news.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import usi.dbdp.portal.dto.NewsReplyDto;
import usi.dbdp.portal.news.service.NewsReplyService;


@Controller
@RequestMapping("/news")
public class NewsReplyController {
	
	@Resource
	private NewsReplyService newsReplyService;
	
	/**
	 * 发表回复
	 * @param reply
	 * @return
	 */
	@RequestMapping(value = "/publish.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> publish(HttpSession session,NewsReplyDto reply) {
		String userId=(String) session.getAttribute("userId");
		String userName=(String) session.getAttribute("userName");
		Long orgId=(Long) session.getAttribute("orgId");
		String orgName=(String) session.getAttribute("orgName");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			reply.setUserId(userId);
			reply.setUserName(userName);
			reply.setOrgId(orgId);
			reply.setOrgName(orgName);
			reply.setContent(Jsoup.clean(reply.getContent(), Whitelist.basicWithImages().addAttributes("p","style","title")));
			newsReplyService.saveNewsReply(reply);
			map.put("flag", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("flag", false);
		}
		return map;
	}
}
