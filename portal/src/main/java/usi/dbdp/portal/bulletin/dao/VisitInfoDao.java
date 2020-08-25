package usi.dbdp.portal.bulletin.dao;

import usi.dbdp.portal.entity.VisitInfo;

/**
 * @author zhqnie
 * @version 2015年9月7日 上午10:58:19
 * 说明
 */
public interface VisitInfoDao {

	/**
	 * 增加阅读次数
	 * @param bulletinId
	 * @param createStaff
	 * @return
	 */
	public int updateVisitNum(long bulletinId, long createStaff);

	/**
	 * 新增记录
	 * @param visitInfo
	 * @return
	 */
	public int insertVisitInfo(VisitInfo visitInfo);

}