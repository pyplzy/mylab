package usi.dbdp.portal.bulletin.dao;

import java.util.List;

import usi.dbdp.portal.dto.BulletinReplyDto;
import usi.dbdp.uic.dto.PageObj;

/**
 * @author zhqnie
 * @version 2015年9月7日 上午10:57:52
 * 说明
 */
public interface BulletinReplyDao {

	/**
	 * 插入一条公告回复
	 * @return 
	 */
	public long insertReply(BulletinReplyDto reply);

	/**
	 * 查出某个公告的回复
	 * @param noticeId
	 * @return
	 */
	public List<BulletinReplyDto> queryReplysByBulletinId(
			long bulletinId);

	/**
	 * 分页查出某个公告的回复
	 * @param noticeId
	 * @return
	 */
	public List<BulletinReplyDto> queryReplysByBulletinIdWithPage(
			long bulletinId, PageObj pageObj);

}