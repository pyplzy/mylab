package usi.dbdp.portal.news.dao.impl4oracle;

import org.springframework.stereotype.Repository;

import usi.common.annotation.OracleDb;
import usi.dbdp.portal.entity.VisitInfo;
import usi.dbdp.portal.news.dao.NewsVisitInfoDao;
import usi.dbdp.portal.util.JdbcDaoSupport4oracle;

/**
 * 阅读记录表Dao
 * @author nie.zhengqian
 * 创建时间：2015年3月25日 下午4:59:41
 */
@OracleDb
@Repository
public class NewsVisitInfoDaoImpl extends JdbcDaoSupport4oracle implements NewsVisitInfoDao{
	
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
		String sql = " INSERT INTO ptl_news_read (READ_ID,NEWS_ID, READER, READ_CNT) "
				+ " VALUES (PTL_NEWS_READ_SEQ.NEXTVAL,?, ?, 1)";
		return this.getJdbcTemplate().update(sql, new Object[]{visitInfo.getBulletinId(),visitInfo.getReader()});
	}
}
