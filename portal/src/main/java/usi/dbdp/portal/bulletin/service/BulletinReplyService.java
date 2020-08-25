package usi.dbdp.portal.bulletin.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import usi.dbdp.portal.bulletin.dao.BulletinReplyDao;
import usi.dbdp.portal.dto.BulletinReplyDto;
import usi.dbdp.uic.dto.PageObj;

/**
 * 公告回复service
 * @author nie.zhengqian
 * 创建时间：2015年3月26日 上午11:00:03
 */
@Service
public class BulletinReplyService {
	@Resource
	private BulletinReplyDao bulletinReplyDao;
	
	/**
	 * 保存回复
	 * @param bulletinReplyDto
	 * @return
	 */
	public long saveBulletin(BulletinReplyDto bulletinReplyDto) {
		
		return bulletinReplyDao.insertReply(bulletinReplyDto);
	}
	
	/**
	 * 查出某个公告的回复
	 * @param bulletinId
	 * @return
	 */
	public List<BulletinReplyDto> getReplysByNoticeId(long bulletinId) {
		
		return bulletinReplyDao.queryReplysByBulletinId(bulletinId);
	}
	
	/**
	 * 分页查出某个公告的回复
	 * @param noticeId
	 * @return
	 */
	public List<BulletinReplyDto> queryReplysByBulletinIdWithPage(long bulletinId , PageObj pageObj){
		return bulletinReplyDao.queryReplysByBulletinIdWithPage(bulletinId, pageObj);
	}
}
