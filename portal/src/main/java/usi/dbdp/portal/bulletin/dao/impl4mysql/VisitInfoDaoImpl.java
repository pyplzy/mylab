package usi.dbdp.portal.bulletin.dao.impl4mysql;

import org.springframework.stereotype.Repository;

import usi.common.annotation.MysqlDb;
import usi.dbdp.portal.bulletin.dao.VisitInfoDao;
import usi.dbdp.portal.entity.VisitInfo;
import usi.dbdp.portal.util.JdbcDaoSupport4mysql;

/**
 * 阅读记录表Dao
 * @author nie.zhengqian
 * 创建时间：2015年3月25日 下午4:59:41
 */
@MysqlDb
@Repository
public class VisitInfoDaoImpl extends JdbcDaoSupport4mysql implements VisitInfoDao{
	
	/**
	 * 增加阅读次数
	 * @param bulletinId
	 * @param createStaff
	 * @return
	 */
	public int updateVisitNum(long bulletinId, long createStaff){
		String sql = "UPDATE PTL_BULLETIN_READ SET READ_CNT = READ_CNT+1 "
				+ "WHERE BULLETIN_ID = ? AND READER = ?";
		return this.getJdbcTemplate().update(sql, new Object[]{bulletinId, createStaff});
		
	}
	
	/**
	 * 新增记录
	 * @param visitInfo
	 * @return
	 */
	public int insertVisitInfo(VisitInfo visitInfo){
		String sql = " INSERT INTO PTL_BULLETIN_READ (BULLETIN_ID, READER, READ_CNT) "
				+ " VALUES (?, ?, 1)";
		return this.getJdbcTemplate().update(sql, new Object[]{visitInfo.getBulletinId(),visitInfo.getReader()});
	}
}
