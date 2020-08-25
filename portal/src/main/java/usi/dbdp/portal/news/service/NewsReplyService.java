package usi.dbdp.portal.news.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import usi.dbdp.portal.dto.BulletinReplyDto;
import usi.dbdp.portal.dto.NewsReplyDto;
import usi.dbdp.portal.news.dao.NewsReplyDao;
import usi.dbdp.uic.dto.PageObj;

/**
 * 公告回复service
 * @author nie.zhengqian
 * 创建时间：2015年3月26日 上午11:00:03
 */
@Service
public class NewsReplyService {
	@Resource
	private NewsReplyDao newsReplyDao;
	
	/**
	 * 保存回复
	 * @param reply
	 * @return
	 */
	public long saveNewsReply(NewsReplyDto reply) {
		
		return newsReplyDao.insertReply(reply);
	}
	
	/**
	 * 查出某个公告的回复
	 * @param bulletinId
	 * @return
	 */
	public List<BulletinReplyDto> getReplysByNoticeId(long bulletinId) {
		
		return newsReplyDao.queryReplysByBulletinId(bulletinId);
	}
	
	/**
	 * 分页查出某个新闻的回复
	 * @param newsId
	 * @return
	 */
	public List<NewsReplyDto> queryReplysByNewsIdWithPage(long bulletinId , PageObj pageObj){
		return newsReplyDao.queryReplysByNewsIdWithPage(bulletinId, pageObj);
	}
	
}
