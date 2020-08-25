package usi.dbdp.portal.news.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import usi.dbdp.portal.news.dao.NewsVisitInfoDao;
import usi.dbdp.portal.dto.BulletinDto;
import usi.dbdp.portal.dto.NewsDto;
import usi.dbdp.portal.entity.VisitInfo;
import usi.dbdp.portal.news.dao.NewsDao;
import usi.dbdp.portal.util.CommonUtil;
import usi.dbdp.portal.util.ConfigUtil;
import usi.dbdp.portal.util.SFTPUtils;
import usi.dbdp.uic.dto.PageObj;


/**
 * 公告管理service
 * @author nie.zhengqian
 * 创建时间：2015年3月25日 下午1:40:55
 */
@Service
public class NewsService {
	//日志
	@Resource
	private NewsDao newsDao;
	@Resource
	private NewsVisitInfoDao visitInfoDao;
	
	private final String uploadPath = ConfigUtil.getValue("uploadPath.news");
	private static final List<String> ALLOWEXT = new ArrayList<String>();
	static {
		ALLOWEXT.add(".jpg");
		ALLOWEXT.add(".jpeg");
		ALLOWEXT.add(".bmp");
		ALLOWEXT.add(".png");
	}
	
	/**
	 * 保存公告
	 * @param ptlBulletin
	 * @return
	 */
	public long saveNotice(NewsDto news){
		if(news.getState() == 1) {
			//如果是发布，保存发布时间
			news.setReleaseTime(new Date());
		}
		return newsDao.insertNews(news);
	}
	
	/**
	 * 修改公告
	 * @param ptlBulletin
	 * @return
	 */
	public int updateNews(NewsDto news){
		if(news.getState() == 1) {
			//如果是发布，保存发布时间
			news.setReleaseTime(new Date());
		}
		return newsDao.updateNews(news);
	}
	
	/**
	 * 分页查询新闻草稿
	 * @param createStaff
	 * @return
	 */
	public Map<String, Object> getDraftsByUserId(String userId, PageObj pageObj){
		List<NewsDto> newsList = newsDao.queryDraftsByUserId(userId, pageObj);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", pageObj.getTotal());
		map.put("rows", newsList);
		return map;
	}
	
	/**
	 * 查出新闻
	 * @param newsDto
	 * @return
	 */
	public Map<String, Object> queryNewsPublishedByUserId(NewsDto newsDto, PageObj pageObj){
		try{
			List<NewsDto> news = newsDao.queryPublishedByUserId(newsDto, pageObj);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", pageObj.getTotal());
			map.put("rows", news);
			return map;
		}catch (Exception e){
			e.printStackTrace();
			return Collections.emptyMap();
		}
	}
	
	/**
	 * 删除草稿
	 * @param bulletinIds
	 * @param authInfo
	 */
	public void batchDeleteNotice(final long[] newsIds){
		newsDao.batchDeleteNews(newsIds);
	}
	
	/**
	 * 批量删除公告
	 * @param newsIds
	 * @return
	 */
	public void batchDeleteNews(final long[] newsIds){
//		newsDao.batchDeleteNews(newsIds);//物理删除
		newsDao.batchUpdateToDeletedStatus(newsIds);//逻辑删除
	}
	
	/**
	 * 根据id删除新闻
	 * @param newsId
	 */
	public void deleteNewsById(long newsId){
//		newsDao.deleteNewsById(newsId);//物理删除
		newsDao.updateDelStatusById(newsId);
	}
	
	/**
	 * 批量修改公告至发布状态
	 * @param bulletinIds
	 * @return 
	 */
	public void batchUpdateToPublishedStatus(final long[] newsIds){
		newsDao.batchUpdateToPublishedStatus(newsIds);
	}
	
	/**
	 * 根据ID查询公告信息
	 * @param bulletinId
	 * @return
	 */
	public NewsDto queryNewsById(Long newsId){
		NewsDto news = newsDao.queryNewsById(newsId);
		return news;
	}
	
	/**
	 * 拷贝到草稿箱
	 * @param newsId
	 */
	public void copyToDraft(Long newsId){
		NewsDto news = newsDao.queryNewsById(newsId);
		news.setState(0);
		newsDao.insertNews(news);
	}
	
	/**
	 * 手动下线公告
	 * @param bulletinId
	 */
	public Date downlineNotice(long bulletinId) {
		Date loseDate = new Date();
		newsDao.updateStatus(bulletinId, 2, loseDate);
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
		newsDao.updateStatus(bulletinId, 1, loseDate);
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
		newsDao.updateStatus(bulletinId, 1, loseDate);
	}
	
	/**
	 * 查出某个人的公告
	 * @param createStaff
	 * @return
	 */
	public List<BulletinDto> getAllByStaffId(long createStaff) {
		return newsDao.queryAllByCreateStaff(createStaff, new Date());
	}
	
	/**
	 * 记录阅读记录
	 * @param staffId
	 * @param noticeId
	 */
	@Transactional
	public void saveVisitLog(long createStaff, long newsId) {
		//修改新闻表的阅读次数字段
		newsDao.updateVisitNum(newsId);
		//修改记录表
		int n = visitInfoDao.updateVisitNum(newsId, createStaff);
		//如果修改记录为0，添加记录
		if(n < 1) {
			VisitInfo visitInfo = new VisitInfo();
			visitInfo.setBulletinId(newsId);
			visitInfo.setReader(createStaff);
			visitInfoDao.insertVisitInfo(visitInfo);
		}
	}
	/**
	 * 置顶公告
	 * @param bulletinId
	 */
	public void stickNotice(long bulletinId) {
		newsDao.updateStickById(bulletinId, 1);
	}
	/**
	 * 取消置顶公告
	 * @param bulletinId
	 */
	public void unStickNotice(long bulletinId) {
		newsDao.updateStickById(bulletinId, 0);
	}

	/**
	 * 开启回复功能
	 * @param bulletinId
	 */
	public void openReply(long newsId) {
		newsDao.updateReplyById(newsId, 1);
	}
	
	/**
	 * 关闭回复功能
	 * @param bulletinId
	 */
	public void closeReply(long newsId) {
		newsDao.updateReplyById(newsId, 0);
	}
	/**
	 * 分页查询公告
	 * @param createStaff
	 * @param date
	 * @param pageObj
	 * @return
	 */
	public List<BulletinDto> queryNoticeByPage(PageObj pageObj,Long province){
		return newsDao.queryNoticeByPage( new Date(), pageObj,province);
	}
	/**
	 * 分页查出上线公告
	 * @return
	 */
	public List<BulletinDto> queryNoticePublishedByPage(PageObj pageObj){
		return newsDao.queryNoticePublishedByPage(pageObj);
	}
	
	/**
	 * 上传新闻图片(封面图片和正文图片，只能接受png jpg bmp 大小不能超过1M)
	 * @param staffId
	 * @param fileList
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> uploadFile(long staffId,MultipartFile multipartFile) throws Exception {
		Map<String,String> fileMeta = new HashMap<String,String>();
		if(multipartFile.getSize() > 1048576){
			fileMeta.put("state", "文件太大,不能超过1M");
			fileMeta.put("url", "");
			return fileMeta;
		}
		
		String originalFilename = multipartFile.getOriginalFilename();
		int i = originalFilename.lastIndexOf(".");
		String ext = originalFilename.substring(i).toLowerCase();	//文件扩展名
		
		if(!ALLOWEXT.contains(ext)){
			fileMeta.put("state", "只能上传jpg jpeg png bmp格式图片");
			fileMeta.put("url", "");
			return fileMeta;
		}
		
		Date createTime = new Date();
		
		//重命名附件(上传人的staffId+日期)
		String newFileName = staffId + "_" + CommonUtil.format(createTime, "yyyyMMddHHmmssSSS")+ext;
		
		//绝对路径(如portal/feedback/2016/12),是文件在服务器上的路径
		String fileAbsDir = getFilePath(createTime);
		
		//上传到文件服务器
		boolean flag = SFTPUtils.upload(multipartFile.getInputStream(), fileAbsDir, newFileName);

		if (flag) {
			String filePath = fileAbsDir + newFileName;
			
			// 保存附件信息并返回附件主键
			fileMeta.put("url",filePath);
			fileMeta.put("state", "SUCCESS");
		} else {
			fileMeta.put("url", "");
			fileMeta.put("state", "上传失败");
		}

		return fileMeta;
	}
	
	/**
	 * 根据upload和groupCode和日期生成文件目录
	 * @param groupCode 所属分组
	 * @param date 当前日期
	 * @return
	 */
     private String getFilePath(Date date) {
		String timeFolderName = CommonUtil.format(date, "yyyyMM");
		//文件路径 portal/groupCode/年月/
		String filePath = uploadPath + timeFolderName + "/";
		return filePath;
	}
    
     /**
      * 系统首页的新闻
      * @param province
      * @return
      */
 	public List<NewsDto> getNewsForPortal(Long province) {
		return newsDao.getNewsForPortal(province);
	}
	
}
