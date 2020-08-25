package usi.dbdp.portal.task.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import usi.dbdp.portal.task.dao.UserOrgIncDao;
import usi.dbdp.portal.task.dto.SysTimerDto;
import usi.dbdp.portal.util.CommonUtil;
import usi.dbdp.portal.util.SFTPUtils;
import usi.dbdp.uic.a2a.dao.AccountingDao;
import usi.dbdp.uic.base.dao.OrgDao;
import usi.dbdp.uic.entity.Org;
import usi.dbdp.uic.entity.UicOrgInc;
import usi.dbdp.uic.entity.UicUserInc;
import usi.dbdp.uic.entity.User;

/**
 * ClassName: UicTaskService 
 * @Description: 用户中心定时任务
 * @author johnDong
 * @date 2017年2月8日
 */
@Service("uicTaskService")
public class UicTaskService {

	@Autowired
	public OrgDao orgDao ; 
	@Autowired
	public AccountingDao accountingDao ; 
	@Autowired
	public UserOrgIncDao userOrgIncDao ;
	private static final Logger LOGGER = LogManager.getLogger(UicTaskService.class);
	private static final String SEG_CHAR= ",";//分隔符
	private static final String DIR = "file";
	

	/**   
	* @Title: syncFullData    
	* @Description: 同步全量数据
	* @author johnDong
	* @Date   2017年2月8日   
	*/
	public void syncFullData(SysTimerDto sysTimerDto){
		long startTime = System.currentTimeMillis();
		//机构信息
		Date date =  new Date();
		String fileDir = DIR+"/"+CommonUtil.format(date, "yyyyMM")+"/"+CommonUtil.format(date, "yyyyMMdd");
		List<Org>  orgs = orgDao.getAllOrg();
		if(orgs!=null&&!orgs.isEmpty()){
			String orgfileName = "UIC_ORG_FULL"+"_"+CommonUtil.format(date, "yyyyMMdd")+".txt"; 
        	StringBuffer sbr = new StringBuffer();
	        for(Org org :orgs){
	           sbr = sbr.append(org.getOrgId()).append(SEG_CHAR)
	            			  .append(org.getOrgCode()).append(SEG_CHAR)
	            			  .append(org.getOrgName()).append(SEG_CHAR)
	            			  .append(org.getpOrgId()).append(SEG_CHAR)
	            			  .append(org.getOrgGrade()).append(SEG_CHAR)
	            			  .append(org.getAdministrativeGrade()).append(SEG_CHAR)
	            			  .append(org.getOrgCodeSeq()).append(SEG_CHAR)
	            			  .append(org.getOrgIdSeq()).append(SEG_CHAR)
	            			  .append(org.getOrgNameSeq()).append(SEG_CHAR)
	            			  .append(org.getDisplayOrder()).append(SEG_CHAR)
	            			  .append(org.getIsLeaf()).append(SEG_CHAR)
	            			  .append(org.getDelFlag()).append(SEG_CHAR)
	            			  .append(org.getOrgMemo()).append("\n");
	         }
        	InputStream   fis = null ;
        	try {
        	fis  = new ByteArrayInputStream(sbr.toString().getBytes("UTF-8"));
    		SFTPUtils.upload(fis,fileDir , orgfileName);
        	} catch (UnsupportedEncodingException e) {
			 e.printStackTrace();
        	}
        }
		long endOrgTime = System.currentTimeMillis();
	    LOGGER.info("同步全量数据（机构）结束，耗时："+(endOrgTime-startTime)+"ms");
        //用户信息
	    long startUserTime = System.currentTimeMillis();
        List<User>  users =accountingDao.getAllUser();
        if(users!=null&&!users.isEmpty()){
        	String userfileName = "UIC_USER_FULL"+"_"+CommonUtil.format(date, "yyyyMMdd")+".txt"; 
        	StringBuffer sbr = new StringBuffer();
        	for(User user: users){
        		sbr.append(user.getId()).append(SEG_CHAR)
        		.append(user.getUserId()).append(SEG_CHAR)
        		.append(user.getUserName()).append(SEG_CHAR)
        		.append(user.getLoginId()).append(SEG_CHAR)
        		.append(user.getGender()).append(SEG_CHAR)
        		.append(user.getMobileNo()).append(SEG_CHAR)
        		.append(user.getUserType()).append(SEG_CHAR)
        		.append(user.getDelFlag()).append(SEG_CHAR)
        		.append(user.getPassword()).append(SEG_CHAR)
        		.append(user.getDuration()).append(SEG_CHAR)
        		.append(CommonUtil.format(user.getPwdLastModTime(), "yyyyMMddHHmmss")).append(SEG_CHAR)
        		.append(CommonUtil.format(user.getLstErrPwdTime(), "yyyyMMddHHmmss")).append(SEG_CHAR)
        		.append(user.getPwdErrCnt()).append(SEG_CHAR)
        		.append(CommonUtil.format(user.getCreateTime(), "yyyyMMddHHmmss")).append("\n");
        		
        	}
        	InputStream   fis = null ;
        	try {
        	fis  = new ByteArrayInputStream(sbr.toString().getBytes("UTF-8"));
    		SFTPUtils.upload(fis, fileDir, userfileName);
        	} catch (UnsupportedEncodingException e) {
			 e.printStackTrace();
        	}
        	}
        long endUserTime = System.currentTimeMillis();
	    LOGGER.info("同步全量数据（人员）结束，耗时："+(endUserTime-startUserTime)+"ms");
        //通知接口
        long endTime = System.currentTimeMillis();
        LOGGER.info("同步全量数据结束，总耗时："+(endTime-startTime)+"ms");
    }	
	
	
	/**   
	* @Title: syncIncData    
	* @Description: 同步增量数据
	* @author johnDong
	* @Date   2017年2月8日   
	*/
	public void syncIncData(SysTimerDto sysTimerDto){
		long startTime = System.currentTimeMillis();
		//机构增量
		Date date = new Date();
		String dateStr= CommonUtil.format(date, "yyyy-MM-dd HH:mm:ss");
		List<UicOrgInc>  orgIncs = userOrgIncDao.getUicOrgIncByDate(dateStr);
		if(orgIncs!=null&&!orgIncs.isEmpty()){
			String orgfileName = "UIC_ORG_INC"+"_"+CommonUtil.format(date, "yyyyMMddHHmm")+".txt"; 
        	StringBuffer sbr = new StringBuffer();
	        for(UicOrgInc orgInc :orgIncs){
	           sbr = sbr.append(orgInc.getOrgId()).append(SEG_CHAR)
	            			  .append(orgInc.getOrgCode()).append(SEG_CHAR)
	            			  .append(orgInc.getOrgName()).append(SEG_CHAR)
	            			  .append(orgInc.getpOrgId()).append(SEG_CHAR)
	            			  .append(orgInc.getOrgGrade()).append(SEG_CHAR)
	            			  .append(orgInc.getAdministrativeGrade()).append(SEG_CHAR)
	            			  .append(orgInc.getOrgCodeSeq()).append(SEG_CHAR)
	            			  .append(orgInc.getOrgIdSeq()).append(SEG_CHAR)
	            			  .append(orgInc.getOrgNameSeq()).append(SEG_CHAR)
	            			  .append(orgInc.getDisplayOrder()).append(SEG_CHAR)
	            			  .append(orgInc.getIsLeaf()).append(SEG_CHAR)
	            			  .append(orgInc.getDelFlag()).append(SEG_CHAR)
	            			  .append(orgInc.getOrgMemo()).append(SEG_CHAR)
	            			  .append(orgInc.getIncType()).append(SEG_CHAR)
	            			  .append(CommonUtil.format(orgInc.getOccurTime(), "yyyyMMddHHmmss")).append("\n");
	         }
        	InputStream   fis = null ;
        	try {
        	fis  = new ByteArrayInputStream(sbr.toString().getBytes("UTF-8"));
    		if(SFTPUtils.upload(fis, DIR+"/"+CommonUtil.format(date, "yyyyMM")
    				+"/"+CommonUtil.format(date, "yyyyMMdd"), orgfileName)){
    			userOrgIncDao.deleteUicOrgIncByDate(dateStr);
    		}
        	} catch (UnsupportedEncodingException e) {
			 e.printStackTrace();
        	}
        }
		//人员增量
		List<UicUserInc>  userIncs = userOrgIncDao.getUicUserIncByDate(dateStr);
		if(userIncs!=null&&!userIncs.isEmpty()){
			String userfileName = "UIC_USER_INC"+"_"+CommonUtil.format(date, "yyyyMMddHHmm")+".txt"; 
        	StringBuffer sbr = new StringBuffer();
	        for(UicUserInc userInc :userIncs){
	        	sbr.append(userInc.getId()).append(SEG_CHAR)
        		.append(userInc.getUserId()).append(SEG_CHAR)
        		.append(userInc.getUserName()).append(SEG_CHAR)
        		.append(userInc.getLoginId()).append(SEG_CHAR)
        		.append(userInc.getGender()).append(SEG_CHAR)
        		.append(userInc.getMobileNo()).append(SEG_CHAR)
        		.append(userInc.getUserType()).append(SEG_CHAR)
        		.append(userInc.getDelFlag()).append(SEG_CHAR)
        		.append(userInc.getPassword()).append(SEG_CHAR)
        		.append(userInc.getDuration()).append(SEG_CHAR)
        		.append(CommonUtil.format(userInc.getPwdLastModTime(), "yyyyMMddHHmmss")).append(SEG_CHAR)
        		.append(CommonUtil.format(userInc.getLstErrPwdTime(), "yyyyMMddHHmmss")).append(SEG_CHAR)
        		.append(userInc.getPwdErrCnt()).append(SEG_CHAR)
        		.append(CommonUtil.format(userInc.getCreateTime(), "yyyyMMddHHmmss")).append(SEG_CHAR)
        		.append(userInc.getIncType()).append(SEG_CHAR)
  			  	.append(CommonUtil.format(userInc.getOccurTime(), "yyyyMMddHHmmss")).append("\n");
	         }
        	InputStream   fis = null ;
        	try {
        	fis  = new ByteArrayInputStream(sbr.toString().getBytes("UTF-8"));
    		if(SFTPUtils.upload(fis, DIR+"/"+CommonUtil.format(date, "yyyyMM")+"/"+CommonUtil.format(date, "yyyyMMdd"), userfileName)){
    			userOrgIncDao.deleteUicUserIncByDate(dateStr);
    		}
        	} catch (UnsupportedEncodingException e) {
			 e.printStackTrace();
        	}
        }
		//通知接口
		 long endTime = System.currentTimeMillis();
	     LOGGER.info("同步增量数据结束，总耗时："+(endTime-startTime)+"ms");
	}
	
}
