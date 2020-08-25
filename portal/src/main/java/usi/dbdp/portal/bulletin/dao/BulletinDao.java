package usi.dbdp.portal.bulletin.dao;

import java.util.Date;
import java.util.List;

import usi.dbdp.portal.dto.BulletinDto;
import usi.dbdp.portal.entity.PtlBulletin;
import usi.dbdp.uic.dto.PageObj;

/**
 * @author zhqnie
 * @version 2015年9月7日 上午10:57:35
 * 说明
 */
public interface BulletinDao {

	/**
	 * 插入公告
	 * @param ptlBulletin
	 * @return 主键
	 */
	public long insertNotice(PtlBulletin ptlBulletin);

	/**
	 * 修改公告
	 * @param ptlBulletin
	 */
	public int updateNotice(PtlBulletin ptlBulletin);

	/**
	 * 查出草稿
	 * @param staffId
	 * @return
	 */
	public List<BulletinDto> queryNoticeDraftsByCreateStaff(
			Long createStaff, PageObj pageObj);

	/**
	 * 查出公告
	 * @param createStaff 创建人id
	 * @param isLine  是否下线
	 * @return
	 */
	public List<BulletinDto> queryNoticePublishedByStaffId(
			long createStaff, String offline, PageObj pageObj);

	/**
	 * 批量删除公告
	 * @param bulletinIds
	 * @return
	 */
	public void batchDeleteNotice(long[] bulletinIds);

	/**
	 * 批量修改公告至删除状态
	 * @param bulletinIds
	 * @return 
	 */
	public void batchUpdateToDeletedStatus(long[] bulletinIds);

	/**
	 * 批量修改公告至发布状态
	 * @param bulletinIds
	 * @return 
	 */
	public void batchUpdateToPublishedStatus(long[] bulletinIds);

	/**
	 * 批量修改公告至草稿状态
	 * @param bulletinIds
	 * @return 
	 */
	public void batchUpdateToDraftsStatus(long[] bulletinIds);

	/**
	 * 根据ID查询公告信息
	 * @param bulletinId
	 * @return
	 */
	public BulletinDto queryNoticeById(Long bulletinId);

	/**
	 * 更改上线状态和下线状态
	 * @param bulletinId 
	 * @param state
	 * @param loseDate
	 * @return
	 */
	public int updateStatus(Long bulletinId, int state, Date loseDate);

	/**
	 * 查出首页的公告
	 * @param createStaff
	 * @param date
	 * @return
	 */
	public List<BulletinDto> queryAllByCreateStaff(Long createStaff,
			Date date);

	/**
	 * 修改阅读次数
	 * @param bulletinId
	 * @return
	 */
	public int updateVisitNum(Long bulletinId);

	/**
	 * 修改置顶标识
	 * @param bulletinId
	 * @param stickUp
	 * @return
	 */
	public int updateStickById(long bulletinId, int stickUp);

	/**
	 * 修改回复标识
	 * @param bulletinId
	 * @param replyFlag
	 * @return
	 */
	public int updateReplyById(long bulletinId, int replyFlag);

	/**
	 * 分页查询公告
	 * @param createStaff
	 * @param date
	 * @param pageObj
	 * @return
	 */
	public List<BulletinDto> queryNoticeByPage(Date date,
			PageObj pageObj, Long province);

	/**
	 * 分页查出上线公告
	 * @return
	 */
	public List<BulletinDto> queryNoticePublishedByPage(PageObj pageObj,Long province);

}