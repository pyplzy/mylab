package usi.dbdp.portal.news.dao.impl4mysql;

import org.springframework.stereotype.Repository;

import usi.common.annotation.MysqlDb;
import usi.dbdp.portal.news.dao.NewsVisitInfoDao;
import usi.dbdp.portal.entity.VisitInfo;
import usi.dbdp.portal.util.JdbcDaoSupport4mysql;

/**
 * 阅读记录表Dao
 * @author nie.zhengqian
 * 创建时间：2015年3月25日 下午4:59:41
 */
@MysqlDb
@Repository
public class NewsVisitInfoDaoImpl extends JdbcDaoSupport4mysql implements NewsVisitInfoDao{
	
	/**
	 * 增加阅读次数
	 * @param bulletinId
	 * @param createStaff
	 * @return
	 */
	public int updateVisitNum(long newsId, long createStaff){
		String sql = "UPDATE ptl_news_read SET READ_CNT = READ_CNT+1 "
				+ "WHERE NEWS_ID = ? AND READER = ?";
		return this.getJdbcTemplate().update(sql, new Object[]{newsId, createStaff});
		
	}
	
	/**
	 * 新增记录
	 * @param visitInfo
	 * @return
	 */
	public int insertVisitInfo(VisitInfo visitInfo){
		String sql = " INSERT INTO ptl_news_read (NEWS_ID, READER, READ_CNT) "
				+ " VALUES (?, ?, 1)";
		return this.getJdbcTemplate().update(sql, new Object[]{visitInfo.getBulletinId(),visitInfo.getReader()});
	}
}
