package usi.dbdp.portal.common.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import usi.dbdp.portal.bulletin.dao.AttachmentDao;
import usi.dbdp.portal.entity.FileMeta;
import usi.dbdp.portal.entity.PtlFile;
import usi.dbdp.portal.entity.PtlFileOpLog;
import usi.dbdp.portal.util.CommonUtil;
import usi.dbdp.portal.util.ConfigUtil;
import usi.dbdp.portal.util.SFTPUtils;



/**
 * 附件上传业务层
 * @author lxci
 * 创建时间：2014-4-11 下午1:23:30
 */
@Service
public class AttachmentService {
	
	//文件上传的路径
	private final String uploadFile =ConfigUtil.getValue("uploadFilePath");
	private final String uploadImage =ConfigUtil.getValue("staffImageImagePath");
	//危险文件
	private static final Map<String,String> DANGER_FILE_MAP = new HashMap<String,String>();
	//图片
	private static final Map<String,String> IMG_MAP = new HashMap<String,String>();
	//静态初始化
	static {
		DANGER_FILE_MAP.put(".exe", "1");
		DANGER_FILE_MAP.put(".bat", "1");
		DANGER_FILE_MAP.put(".sh", "1");
		DANGER_FILE_MAP.put(".js", "1");
		DANGER_FILE_MAP.put(".html", "1");
		DANGER_FILE_MAP.put(".htm", "1");
		
		IMG_MAP.put(".gif", "1");
		IMG_MAP.put(".png", "1");
		IMG_MAP.put(".jpg", "1");
		IMG_MAP.put(".jpeg", "1");
	}
	
	//操作附件表的dao
	@Resource
	private AttachmentDao attachmentDao;
	
	/**
	 * 上传附件并返回上传文件的信息
	 * @param request
	 * @param session
	 * @param groupCode 附件所属分组
	 * @param relationId 关联主键（业务表的）
	 * @return 上传附件的列表信息
	 * @throws Exception 抛出异常方便上传失败时的提示
	 */
	@Transactional(rollbackFor=Exception.class)
	public LinkedList<FileMeta> upload(MultipartHttpServletRequest request,HttpSession session,String groupCode,long relationId) throws Exception{
		
		//获取session里当前登陆人的信息
		long staffId = (Long) session.getAttribute("staffId");
		LinkedList<FileMeta> files = new LinkedList<FileMeta>();
		//1. 构造一个文件名迭代器
		 Iterator<String> itr =  request.getFileNames();
		 MultipartFile mpf = null;
		 
		 //上传时间
		 Date createTime = new Date();
		 //构造文件路径
		 //绝对路径
		 String fileAbsDir = getFilePath(groupCode,createTime);
		 //2. 迭代每一个文件
		 while(itr.hasNext()){
			 
			 //2.1 获取文件
			 mpf = request.getFile(itr.next());
			 //2.11附加非空判断：避免多个附件时，前台可能上传为空的情况
			 if(mpf.isEmpty()) {
				 continue;
			 }
			 //获取文件后缀名前面的点的位置
			 int i=mpf.getOriginalFilename().lastIndexOf(".");
			 //取文件后缀检验文件类型
			 String fileSuffix = mpf.getOriginalFilename().substring(i);
			 
			 //如果是危险文件后缀，不能给上传
			 if(DANGER_FILE_MAP.containsKey(fileSuffix)) {
				 //通过构造一个文件大小为-2的文件元数据对象返回，表示是危险文件不允许上传
				 FileMeta fileMeta = new FileMeta();
				 fileMeta.setFileSize("-2");
				 files.add(fileMeta);
				 break;
			 }
			 //2.2 如果文件多于10个，就pop出一个
			 if(files.size() >= 10)
				 files.pop();
			 
			 //上传文件大小限制为10M
			 if(mpf.getSize()<=10485760){
				 //2.3 创建文件元数据
				 FileMeta fileMeta = new FileMeta();
				 fileMeta.setFileName(mpf.getOriginalFilename());
				 fileMeta.setFileSize(Math.floor(mpf.getSize()/(double)1024)+" Kb");
				 fileMeta.setFileType(mpf.getContentType());
				 
				 //获取文件后缀名前面的点的位置
				 //为了防止文件覆盖，上传后使用的文件名格式为：时分秒毫秒+随机四位数+.文件后缀名。
				 String loadedFileName = new SimpleDateFormat("HHmmssSSS").format(createTime)+Math.round(Math.random()*8999+1000)+fileSuffix;
				 
				 //构造存放待保存的上传文件信息实体
				 PtlFile ptlFile = new PtlFile();
				 ptlFile.setFileName(mpf.getOriginalFilename());
				 //（为了开发时能测试，所以用的是/，因为ftp服务器是linux）
				 ptlFile.setAbsolutepath(fileAbsDir+"/"+loadedFileName);
				 ptlFile.setFileSize(mpf.getSize());
				 ptlFile.setFileType(mpf.getContentType());
				 ptlFile.setStaffId(staffId);
				 ptlFile.setFileTime(createTime);
				 ptlFile.setGroupCode(groupCode);
				 ptlFile.setRelationId(relationId);
				 ptlFile.setDownloadTimes(0);
				 ptlFile.setIsDel(0);
				 
				 //保存附件信息并返回附件主键
				 long fileId=attachmentDao.saveAttachment(ptlFile);
				 
				 try {
					 
					 //把附件主键放到文件元数据里
					 fileMeta.setFileId(fileId);
					 //2.4 文件元数据信息添加到list
					 files.add(fileMeta);
					// 拷贝文件到servlet上下文的upload目录下，File.separator系统相关路径分隔符
//					FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(fileAbsDir+File.separator+loadedFileName));
//					 FtpUtil.upload(mpf.getInputStream(), fileAbsDir, loadedFileName);
					 //因为防火墙问题，改用sftp
					 SFTPUtils.upload(mpf.getInputStream(), fileAbsDir, loadedFileName);
				} catch (Exception e) {
					//异常时把最后一个添加的元素删除
					files.removeLast();
					e.printStackTrace();
					throw new Exception("异常");
				}
			//上传文件大小超过限制	 
			 }else{
				 
				 //通过构造一个文件大小为-1的文件元数据对象返回
				 FileMeta fileMeta = new FileMeta();
				 fileMeta.setFileSize("-1");
				 files.add(fileMeta);
				 
			 }
		 }
		 
		 // 结果可能如下
		 // [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
		 //return files;
		 return files;
	}
	
	/**
	 * 上传头像并返回上传文件的信息
	 * @param request
	 * @param groupCode 附件所属分组
	 * @param relationId 业务表主键（此处为通讯录表主键）
	 * @param userId 头像属主登录账号（可能为空，如果为空，就使用relationId）
	 * @return 上传附件的列表信息
	 * @throws Exception 抛出异常方便上传失败时的提示
	 */
	public LinkedList<FileMeta> uploadHeadImg(MultipartHttpServletRequest request,String groupCode,long relationId,String userId) throws Exception{
		
		LinkedList<FileMeta> files = new LinkedList<FileMeta>();
		 //1. 构造一个文件名迭代器
		 Iterator<String> itr =  request.getFileNames();
		 MultipartFile mpf = null;
		 
		 //上传时间
		 Date createTime = new Date();
		 //构造文件路径
		 //绝对路径
		 String fileAbsDir = getFilePath(groupCode,createTime);
		 //2. 迭代每一个文件
		 while(itr.hasNext()){
			 
			 //2.1 获取文件
			 mpf = request.getFile(itr.next());
			 //2.11附加非空判断：避免多个附件时，前台可能上传为空的情况
			 if(mpf.isEmpty()) {
				 //通过构造一个文件大小为-1的文件元数据对象返回
				 FileMeta fileMeta = new FileMeta();
				 fileMeta.setFileSize("-1");
				 files.add(fileMeta);
				 break;
			 }
			 //获取文件后缀名前面的点的位置
			 int i=mpf.getOriginalFilename().lastIndexOf(".");
			 //取文件后缀检验文件类型
			 String fileSuffix = mpf.getOriginalFilename().substring(i);
			 
			 //检验是否是图片文件
			 if(IMG_MAP.containsKey(fileSuffix)) {
				 //上传文件大小限制为10M
				 if(mpf.getSize()<=10485760){
					 //2.3 创建文件元数据
					 FileMeta fileMeta = new FileMeta();
					 fileMeta.setFileSize(Math.floor(mpf.getSize()/(double)1024)+" Kb");
					 fileMeta.setFileType(mpf.getContentType());
					 String loadedFileName ="";
					 //如果是新建联系人的话
					 if("".equals(userId)&&relationId==0) {
						  loadedFileName = new SimpleDateFormat("ddHHmmssSSS").format(createTime)+Math.round(Math.random()*8999+1000)+fileSuffix;
					 }else {
						//登录账号或者业务表主键+文件后缀
						 loadedFileName = ("".equals(userId)?relationId:userId)+fileSuffix;
					 }
					 //将上传后的路径加文件名返回。用来刷新头像
					 fileMeta.setFileName(fileAbsDir+"/"+loadedFileName);
					 files.add(fileMeta);
					//因为防火墙问题，改用sftp
					SFTPUtils.upload(mpf.getInputStream(), fileAbsDir, loadedFileName);
					//只有一个文件
					if(!"".equals(fileMeta.getFileName())) {
						break;
					}
				//上传文件大小超过限制	 
				 }else{
					 //通过构造一个文件大小为-1的文件元数据对象返回
					 FileMeta fileMeta = new FileMeta();
					 fileMeta.setFileSize("-1");
					 files.add(fileMeta);
					 break;
				 }
			 //非图片
			 }else {
				 //通过构造一个文件大小为-2的文件元数据对象返回，表示是非图片文件不允许上传
				 FileMeta fileMeta = new FileMeta();
				 fileMeta.setFileSize("-2");
				 files.add(fileMeta);
				 break;
			 }
		 }
		 return files;
	}	
	
	/**
     * 
     * @param request
     * @param imgPath
     * @param loadedFileName
     * @return
    * @throws IOException 
     */
    @Transactional(rollbackFor=Exception.class)
	public LinkedList<FileMeta> uploadSftp(MultipartHttpServletRequest request) throws IOException{
   	 LinkedList<FileMeta> files = new LinkedList<FileMeta>();
		//1. 构造一个文件名迭代器
		 Iterator<String> itr =  request.getFileNames();
		 MultipartFile mpf = null;
		 //2. 迭代每一个文件
		 while(itr.hasNext()){
			 //2.1 获取文件
			 mpf = request.getFile(itr.next());
			 Date createTime = new Date();
			 String imgPath=getFilePath("image",createTime);
			//获取文件后缀名前面的点的位置
			 int i=mpf.getOriginalFilename().lastIndexOf(".");
			 //为了防止文件覆盖，上传后使用的文件名格式为：时分秒毫秒+随机四位数+.文件后缀名。
			 String loadedFileName = new SimpleDateFormat("HHmmssSSS").format(createTime)+Math.round(Math.random()*8999+1000)+mpf.getOriginalFilename().substring(i);
			 SFTPUtils.upload(mpf.getInputStream(), imgPath, loadedFileName);
			//2.3 创建文件元数据
			 FileMeta fileMeta = new FileMeta();
			 fileMeta.setFilePath(imgPath+"/"+loadedFileName);
			 files.add(fileMeta);
		 }
		 return files;
	}
    
	/**
	 * 删除文件（逻辑删除表记录，物理删除文件）
	 * @param request
	 * @param session
	 * @param fileId 关联主键（业务表的）
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public String delFile(HttpServletRequest request,HttpSession session,long fileId){
		
		//获取session里当前登陆人的信息
		long staffId = (Long) session.getAttribute("staffId");
		String userName = (String) session.getAttribute("userName");
		Long orgId = (Long) session.getAttribute("orgId");
		String orgName = (String) session.getAttribute("orgName");
		//获取文件位置
		String filePath = attachmentDao.getAbsolutepathById(fileId);
		//是否删除成功（默认成功）
		boolean isSucceDel = true;
		//删除文件
		if(null!=filePath){
//			isSucceDel = FtpUtil.deleteFile(filePath);
			//因为防火墙问题改为sftp
			isSucceDel = SFTPUtils.deleteFile(filePath);
		}
		if(isSucceDel) {
			PtlFileOpLog ptlFileOpLog = new PtlFileOpLog();
			ptlFileOpLog.setFileId(fileId);
			ptlFileOpLog.setStaffId(staffId);
			ptlFileOpLog.setOperatorName(userName);
			ptlFileOpLog.setOrgId(orgId);
			ptlFileOpLog.setOrgName(orgName);
			ptlFileOpLog.setOpTime(new Date());
			//操作类型(0删除1下载)
			ptlFileOpLog.setOpType(0);
			//更新附件表标识位，标识为已删除并记录附件操作日志
			if(attachmentDao.delFileByID(fileId)==1&&attachmentDao.savePtlFileOpLog(ptlFileOpLog)==1){
				return "success";
			}else {
				return "fail";
			}
		}else {
			return "fail";
		}
		
	}
	
	/**
	 * 根据所属分组和关联主键取附件列表
	 * @param groupCode 附件所属分组
	 * @param relationId 关联主键（业务表的）
	 * @return 没有返回null，有的话返回附件元数据列表
	 */
	@Transactional(readOnly = true)
	public List<PtlFile> getUploadedFiles(String groupCode,long relationId){
		
		List<PtlFile> lstFiles = attachmentDao.getUploadedFiles(groupCode, relationId);
		if(lstFiles.size()>0){
			return lstFiles;
		}
		return null;
	}
	
	/**
	 * 记录操作日志，增加下载次数
	 * @param session
	 * @param fileId 附件主键
	 */
	@Transactional(rollbackFor=Exception.class)
	public void getFileService(HttpSession session,long fileId){
		
		//获取session里当前登陆人的信息
		Long staffId = (Long) session.getAttribute("staffId");
		String userName = (String) session.getAttribute("userName");
		Long orgId = (Long) session.getAttribute("orgId");
		String orgName = (String) session.getAttribute("orgName");
		
		PtlFileOpLog ptlFileOpLog = new PtlFileOpLog();
		ptlFileOpLog.setFileId(fileId);
		ptlFileOpLog.setStaffId(staffId);
		ptlFileOpLog.setOperatorName(userName);
		ptlFileOpLog.setOrgId(orgId);
		ptlFileOpLog.setOrgName(orgName);
		ptlFileOpLog.setOpTime(new Date());
		//操作类型(0删除1下载)
		ptlFileOpLog.setOpType(1);
		attachmentDao.savePtlFileOpLog(ptlFileOpLog);
		attachmentDao.incDownloadTimesById(fileId);
		
	}
	
	/**
	 * 根据主键查询附件的路径文件名
	 * @param fileId 附件主键
	 * @return 文件名
	 */
	@Transactional(readOnly = true)
	public PtlFile getFileMetaById(long fileId){
		return attachmentDao.getPtlFileById(fileId);
	}
	
	/**
	 * 根据upload和groupCode和日期生成文件目录
	 * @param groupCode 所属分组
	 * @param date 当前日期
	 * @return
	 */
     public String getFilePath(String groupCode, Date date) {
		String filePath="";
		String timeFolderName = CommonUtil.format(date, "yyyyMM");
		
		if("headImage".equals(groupCode)){
			 filePath = this.uploadImage;
		}else{
			 //特殊要求不再加groupCode和day（为了开发时能测试，所以用的是/，因为ftp服务器是linux）
			 filePath = this.uploadFile + "/" + groupCode +"/"+timeFolderName;
		}
		return filePath;
	}

}
