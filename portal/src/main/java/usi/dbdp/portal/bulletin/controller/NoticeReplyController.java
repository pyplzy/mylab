package usi.dbdp.portal.bulletin.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import usi.dbdp.portal.bulletin.service.BulletinReplyService;
import usi.dbdp.portal.dto.BulletinReplyDto;
import usi.dbdp.portal.util.CommonUtil;


@Controller
@RequestMapping("/noticeReply")
public class NoticeReplyController {
	
	@Resource
	private BulletinReplyService noticeReplyService;
	
	/**
	 * 发表回复
	 * @param reply
	 * @return
	 */
	@RequestMapping(value = "/publish.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> publish(BulletinReplyDto reply) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Date publishDate = new Date();
			reply.setReplyDate(publishDate);
			reply.setReplyContent(Jsoup.clean(reply.getReplyContent(), Whitelist.basicWithImages().addAttributes("p","style","title")));
			long replyId = noticeReplyService.saveBulletin(reply);
			map.put("flag", true);
			map.put("replyId", replyId);
			map.put("publishTime", CommonUtil.format(publishDate, "yyyy-MM-dd HH:mm:ss"));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("flag", false);
		}
		return map;
	}
}
