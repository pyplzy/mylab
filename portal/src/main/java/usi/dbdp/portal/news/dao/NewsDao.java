package usi.dbdp.portal.news.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import usi.dbdp.portal.dto.BulletinDto;
import usi.dbdp.portal.dto.NewsDto;
import usi.dbdp.uic.dto.PageObj;

/**
 * @author zhqnie
 * @version 2015年9月7日 上午10:57:35 说明
 */
public interface NewsDao {

	/**
	 * 插入公告
	 * @param ptlBulletin
	 * @return 主键
	 */
	public long insertNews(NewsDto news);

	/**
	 * 修改公告
	 * @param ptlBulletin
	 */
	public int updateNews(NewsDto news);

	/**
	 * 查出草稿
	 * @param staffId
	 * @return
	 */
	public List<NewsDto> queryDraftsByUserId(String userId, PageObj pageObj);

	/**
	 * 查出新闻
	 * @param newsDto 创建人id
	 * @return
	 * @throws ParseException 
	 */
	public List<NewsDto> queryPublishedByUserId(NewsDto newsDto, PageObj pageObj) throws ParseException;

	/**
	 * 批量删除新闻
	 * @param newsIds
	 * @return
	 */
	public void batchDeleteNews(long[] newsIds);

	/**
	 * 批量修改新闻至删除状态
	 * @param newsIds
	 * @return
	 */
	public void batchUpdateToDeletedStatus(long[] newsIds);

	//根据id删除该新闻
	public void deleteNewsById(long newsId);
	
	//根据id修改该新闻为删除状态
	public void updateDelStatusById(long newsId);
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
	 * 根据ID查询新闻信息
	 * @param newsId
	 * @return
	 */
	public NewsDto queryNewsById(Long newsId);

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
	public List<BulletinDto> queryAllByCreateStaff(Long createStaff, Date date);

	/**
	 * 修改阅读次数
	 * @param bulletinId
	 * @return
	 */
	public int updateVisitNum(Long newsId);

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
	public int updateReplyById(long newsId, int replyFlag);

	/**
	 * 分页查询公告
	 * @param createStaff
	 * @param date
	 * @param pageObj
	 * @return
	 */
	public List<BulletinDto> queryNoticeByPage(Date date, PageObj pageObj, Long province);

	/**
	 * 分页查出上线公告
	 * @return
	 */
	public List<BulletinDto> queryNoticePublishedByPage(PageObj pageObj);

	/**
	 * 查询新闻封面
	 * @param groupcode
	 * @param newsId
	 * @return
	 */
	public String getCoverPhotoUrl(String groupcode,Long newsId);

	/**
	 * 系统首页的新闻
	 * @param province
	 * @return
	 */
	public List<NewsDto> getNewsForPortal(Long province);

}