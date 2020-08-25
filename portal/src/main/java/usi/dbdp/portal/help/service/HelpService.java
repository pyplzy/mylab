package usi.dbdp.portal.help.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import usi.dbdp.portal.bulletin.dao.AttachmentDao;
import usi.dbdp.portal.dto.ContactUsInfo;
import usi.dbdp.portal.dto.FeedbackDto;
import usi.dbdp.portal.entity.FileMeta;
import usi.dbdp.portal.entity.PtlFile;
import usi.dbdp.portal.help.dao.HelpDao;
import usi.dbdp.portal.util.CommonUtil;
import usi.dbdp.portal.util.ConfigUtil;
import usi.dbdp.portal.util.SFTPUtils;
import usi.dbdp.uic.dto.PageObj;

/**
 * 联系我们和问题反馈
 * @author zhang.dechang
 * 2016年12月29日 上午11:31:18
 */
@Service
public class HelpService{
	
	@Resource
	private HelpDao helpDao;
	@Resource
	private AttachmentDao attachmentDao;
	
	private final String uploadPath = ConfigUtil.getValue("uploadPath.feedback");
	private final String fileserver = ConfigUtil.getValue("fileserver.prefix");
	private final String groupcode = "feedback";
	
	/**
	 * 查询联系我们信息
	 * @return
	 */
	public ContactUsInfo getContactUsInfo(){
		return helpDao.getContactUsInfo();
	}

	/**
	 * 保存问题反馈信息
	 * @param feedbackDto
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveFeedback(FeedbackDto feedbackDto,Long[] fileIds){
		Long feedbackId = helpDao.saveFeedback(feedbackDto);
		if(fileIds!=null && fileIds.length>0){
			helpDao.updateFeedbackFile(feedbackId,fileIds);
		}
	}

	/**
	 * 查询问题反馈列表
	 * @param feedbackDto
	 * @param pageObj
	 * @return
	 */
	public Map<String,Object> getFeedbackList(FeedbackDto feedbackDto,PageObj pageObj){
		Map<String,Object> map = new HashMap<String,Object>();
		List<FeedbackDto> result = helpDao.getFeedbackList(feedbackDto, pageObj);
		map.put("list", result);
		map.put("pageObj",pageObj);
		return map;
	}
	
	/**
	 * 查询反馈信息，用于处理
	 * @param feedbackId
	 * @return
	 */
	public FeedbackDto getFeedbackDetail(Long feedbackId){
		FeedbackDto feedbackDto = helpDao.getFeedbackDetail(feedbackId);
		List<PtlFile> files = helpDao.getFeedbackFiles(groupcode, feedbackId);
		for(PtlFile file : files){
			file.setAbsolutepath(fileserver + file.getAbsolutepath());
		}
		feedbackDto.setFiles(files);
		return feedbackDto;
	}

	/**
	 * 保存处理信息
	 * @param feedbackDto
	 */
	public void saveHandle(FeedbackDto feedbackDto) {
		helpDao.saveHandleInfo(feedbackDto);
	}
	
	/**
	 * 上传附件
	 * @param staffId
	 * @param fileList
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public FileMeta uploadFile(long staffId,List<MultipartFile> fileList) throws Exception {
		FileMeta fileMeta = new FileMeta();
		Date createTime = new Date();
		MultipartFile multipartFile = fileList.get(0);
		
		String originalFilename = multipartFile.getOriginalFilename();
		int i = originalFilename.lastIndexOf(".");
		String ext = originalFilename.substring(i);	//文件扩展名
		
		//重命名附件(上传人的staffId+日期)
		String newFileName = staffId + "_" + CommonUtil.format(createTime, "yyyyMMddHHmmssSSS")+ext;
		
		 //绝对路径(如portal/feedback/2016/12),是文件在服务器上的路径
		 String fileAbsDir = getFilePath(createTime);
		
		//上传到文件服务器
		boolean flag = SFTPUtils.upload(multipartFile.getInputStream(), fileAbsDir, newFileName);

		if (flag) {
			String filePath = fileserver + fileAbsDir + newFileName;
			
			// 构造存放待保存的上传文件信息实体(没有relationId)
			PtlFile ptlFile = new PtlFile();
			ptlFile.setFileName(multipartFile.getOriginalFilename());
			ptlFile.setAbsolutepath(fileAbsDir+newFileName);
			ptlFile.setFileSize(multipartFile.getSize());
			ptlFile.setFileType(multipartFile.getContentType());
			ptlFile.setStaffId(staffId);
			ptlFile.setFileTime(createTime);
			ptlFile.setGroupCode(groupcode);
			ptlFile.setDownloadTimes(0);
			ptlFile.setIsDel(0);
	
			// 保存附件信息并返回附件主键
			long fileId = attachmentDao.saveAttachment(ptlFile);
			fileMeta.setFileName(multipartFile.getOriginalFilename());
			fileMeta.setFileId(fileId);
			fileMeta.setFilePath(filePath);
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
	
}
