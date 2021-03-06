package usi.dbdp.portal.bulletin.dao.impl4oracle;

import org.springframework.stereotype.Repository;

import usi.common.annotation.OracleDb;
import usi.dbdp.portal.bulletin.dao.VisitInfoDao;
import usi.dbdp.portal.entity.VisitInfo;
import usi.dbdp.portal.util.JdbcDaoSupport4oracle;

/**
 * 阅读记录表Dao
 * @author nie.zhengqian
 * 创建时间：2015年3月25日 下午4:59:41
 */
@OracleDb
@Repository
public class VisitInfoDaoImpl extends JdbcDaoSupport4oracle implements VisitInfoDao{
	
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
		String sql = " INSERT INTO PTL_BULLETIN_READ (READ_ID,BULLETIN_ID, READER, READ_CNT) "
				+ " VALUES (ptl_bulletin_read_seq.nextval,?, ?, 1)";
		return this.getJdbcTemplate().update(sql, new Object[]{visitInfo.getBulletinId(),visitInfo.getReader()});
	}
}
