package usi.dbdp.portal.bulletin.service;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import usi.dbdp.portal.bulletin.dao.AttachmentDao;
import usi.dbdp.portal.bulletin.dao.BulletinDao;
import usi.dbdp.portal.bulletin.dao.VisitInfoDao;
import usi.dbdp.portal.dto.BulletinDto;
import usi.dbdp.portal.entity.PtlBulletin;
import usi.dbdp.portal.entity.PtlFile;
import usi.dbdp.portal.entity.PtlFileOpLog;
import usi.dbdp.portal.entity.VisitInfo;
import usi.dbdp.portal.util.CommonUtil;
import usi.dbdp.uic.dto.PageObj;


/**
 * 公告管理service
 * @author nie.zhengqian
 * 创建时间：2015年3月25日 下午1:40:55
 */
@Service
public class BulletinService {
	//日志
	private static Logger logger = Logger.getLogger(BulletinService.class);
	@Resource
	private BulletinDao bulletinDao;
	@Resource
	private AttachmentDao attachmentDao;
	@Resource
	private VisitInfoDao visitInfoDao;
	
	/**
	 * 保存公告
	 * @param ptlBulletin
	 * @return
	 */
	public long saveNotice(PtlBulletin ptlBulletin){
		String loseTime = ptlBulletin.getLoseTime();
		if("".equals(loseTime)) {
			ptlBulletin.setLoseTime(null);
		}else {
			ptlBulletin.setLoseDate(CommonUtil.parse(loseTime, "yyyy-MM-dd HH:mm:ss"));
		}
		if(ptlBulletin.getState() == 1) {
			//如果是发布，保存发布时间
			ptlBulletin.setReleaseTime(new Date());
		}
		return bulletinDao.insertNotice(ptlBulletin);
	}
	
	/**
	 * 修改公告
	 * @param ptlBulletin
	 * @return
	 */
	public int updateNotice(PtlBulletin ptlBulletin){
		String loseTime = ptlBulletin.getLoseTime();
		if("".equals(loseTime)) {
			ptlBulletin.setLoseTime(null);
		}else {
			ptlBulletin.setLoseDate(CommonUtil.parse(loseTime, "yyyy-MM-dd HH:mm:ss"));
		}
		if(ptlBulletin.getState() == 1) {
			//如果是发布，保存发布时间
			ptlBulletin.setReleaseTime(new Date());
		}
		return bulletinDao.updateNotice(ptlBulletin);
	}
	
	/**
	 * 查出所有的草稿
	 * @param createStaff
	 * @return
	 */
	public Map<String, Object> getDraftsByCreateStaff(Long createStaff, PageObj pageObj){
		List<BulletinDto> bulletin = bulletinDao.queryNoticeDraftsByCreateStaff(createStaff, pageObj);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", pageObj.getTotal());
		map.put("rows", bulletin);
		return map;
	}
	
	/**
	 * 查出公告
	 * @param createStaff
	 * @param offline
	 * @return
	 */
	public Map<String, Object> queryNoticePublishedByStaffId(long createStaff, String offline, PageObj pageObj){
		List<BulletinDto> bulletin = bulletinDao.queryNoticePublishedByStaffId(createStaff, offline, pageObj);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", pageObj.getTotal());
		map.put("rows", bulletin);
		return map;
	}
	
	/**
	 * 删除草稿
	 * @param bulletinIds
	 * @param authInfo
	 */
	public void batchDeleteNotice(final long[] bulletinIds, long staffId, String userNmae, long orgId){
		//先批量改公告表
		bulletinDao.batchDeleteNotice(bulletinIds);
		
		//得到所有的附件
		List<PtlFile> fileMetas = attachmentDao.queryUploadedFiles("notice", bulletinIds);
		int n = fileMetas.size();
		long[] fileIds = new long[n];
		PtlFileOpLog[] ptlFileOpLogs = new PtlFileOpLog[n];
		for(int i = 0; i < n; i++) {
			fileIds[i] = fileMetas.get(i).getFileId();
			PtlFileOpLog log = new PtlFileOpLog();
			log.setFileId(fileMetas.get(i).getFileId());
			log.setStaffId(staffId);
			log.setOperatorName(userNmae);
			log.setOrgId(orgId);
			log.setOpTime(new Date());
			log.setOpType(0);
			ptlFileOpLogs[i] = log;
			
			String filePath = fileMetas.get(i).getAbsolutepath();
			//是否删除成功（默认成功）
			boolean isSucceDel = true;
			if(filePath != null) {
				File file = new File(filePath);
				if (file.isFile() && file.exists()){
					isSucceDel = file.delete();
					if(!isSucceDel) {
						logger.info("删除文件失败。。。文件路径是："+filePath);
					}
				}
			}
		}
		if(fileIds.length > 0) {
			//修改附件表
			attachmentDao.delFileByIds(fileIds);
			//修改附件操作日志表
			attachmentDao.batchSavePtlFileOpLog(ptlFileOpLogs);
		}
	}
	
	/**
	 * 批量删除公告
	 * @param bulletinIds
	 * @return
	 */
	public void batchDeleteNotice(final long[] bulletinIds){
		bulletinDao.batchDeleteNotice(bulletinIds);
	}
	
	/**
	 * 批量修改公告至发布状态
	 * @param bulletinIds
	 * @return 
	 */
	public void batchUpdateToPublishedStatus(final long[] bulletinIds){
		bulletinDao.batchUpdateToPublishedStatus(bulletinIds);
	}
	
	/**
	 * 根据ID查询公告信息
	 * @param bulletinId
	 * @return
	 */
	public BulletinDto queryNoticeById(Long bulletinId){
		BulletinDto bulletinDto = bulletinDao.queryNoticeById(bulletinId);
		//加附件
		if(bulletinDto != null) {
			bulletinDto.setFiles(attachmentDao.getUploadedFiles("notice", bulletinId));
		}
		return bulletinDto;
	}
	
	/**
	 * 拷贝到草稿箱
	 * @param bulletinId
	 */
	public void copyToDraft(long bulletinId){
		BulletinDto bulletinDto = bulletinDao.queryNoticeById(bulletinId);
		bulletinDto.setState(0);
		bulletinDao.insertNotice(bulletinDto);
		
	}
	
	/**
	 * 手动下线公告
	 * @param bulletinId
	 */
	public Date downlineNotice(long bulletinId) {
		Date loseDate = new Date();
		bulletinDao.updateStatus(bulletinId, 2, loseDate);
		return loseDate;
	}
	
	/**
	 * 重新发布
	 * @param bulletinId
	 */
	public void publishNotice(long bulletinId, String loseTime) {
		Date  loseDate= null;
		if(CommonUtil.hasValue(loseTime)) {
			loseDate = CommonUtil.parse(loseTime, "yyyy-MM-dd HH:mm:ss");
		}
		bulletinDao.updateStatus(bulletinId, 1, loseDate);
	}
	
	/**
	 * 修改下线时间
	 * @param bulletinId
	 */
	public void updateLoseDate(long bulletinId, String loseTime) {
		Date  loseDate = null;
		if(CommonUtil.hasValue(loseTime)) {
			loseDate = CommonUtil.parse(loseTime, "yyyy-MM-dd HH:mm:ss");
		}
		bulletinDao.updateStatus(bulletinId, 1, loseDate);
	}
	
	/**
	 * 查出某个人的公告
	 * @param createStaff
	 * @return
	 */
	public List<BulletinDto> getAllByStaffId(long createStaff) {
		return bulletinDao.queryAllByCreateStaff(createStaff, new Date());
	}
	
	/**
	 * 记录阅读记录
	 * @param staffId
	 * @param noticeId
	 */
	public void saveVisitLog(long createStaff, long bulletinId) {
		//修改公告表的阅读次数字段
		bulletinDao.updateVisitNum(bulletinId);
		//修改记录表
		int n = visitInfoDao.updateVisitNum(bulletinId, createStaff);
		//如果修改记录为0，添加记录
		if(n < 1) {
			VisitInfo visitInfo = new VisitInfo();
			visitInfo.setBulletinId(bulletinId);
			visitInfo.setReader(createStaff);
			visitInfoDao.insertVisitInfo(visitInfo);
		}
	}
	/**
	 * 置顶公告
	 * @param bulletinId
	 */
	public void stickNotice(long bulletinId) {
		bulletinDao.updateStickById(bulletinId, 1);
	}
	/**
	 * 取消置顶公告
	 * @param bulletinId
	 */
	public void unStickNotice(long bulletinId) {
		bulletinDao.updateStickById(bulletinId, 0);
	}

	/**
	 * 开启回复功能
	 * @param bulletinId
	 */
	public void openReply(long bulletinId) {
		bulletinDao.updateReplyById(bulletinId, 1);
	}
	
	/**
	 * 关闭回复功能
	 * @param bulletinId
	 */
	public void closeReply(long bulletinId) {
		bulletinDao.updateReplyById(bulletinId, 0);
	}
	/**
	 * 分页查询公告
	 * @param createStaff
	 * @param date
	 * @param pageObj
	 * @return
	 */
	public List<BulletinDto> queryNoticeByPage(PageObj pageObj,Long province){
		return bulletinDao.queryNoticeByPage( new Date(), pageObj,province);
	}
	/**
	 * 分页查出上线公告
	 * @return
	 */
	public List<BulletinDto> queryNoticePublishedByPage(PageObj pageObj,Long province){
		return bulletinDao.queryNoticePublishedByPage(pageObj,province);
	}
	
	/**
	 * 批量修改公告至删除状态
	 * @param bulletinIds
	 * @return 
	 */
	public void batchUpdateToDeletedStatus(long[] bulletinIds){
		bulletinDao.batchUpdateToDeletedStatus(bulletinIds);
	}
}
