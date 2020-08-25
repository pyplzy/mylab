package usi.dbdp.uic.base.service.impl;

//import java.sql.ResultSet;
//import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

//import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import usi.dbdp.portal.task.service.UserOrgIncService;
import usi.dbdp.uic.base.dao.OrgDao;
//import usi.dbdp.uic.base.dao.impl4mysql.OrgDaoImpl.OrgRowMapper;
import usi.dbdp.uic.base.service.OrgService;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.entity.Org;

/**
 * 机构服务
 * @author nie.zhengqian
 * 创建时间：2015年3月3日 下午6:25:26
 */
@Service
public class OrgServiceImpl implements OrgService{
	@Resource
	private OrgDao orgDao;
	@Resource
	UserOrgIncService userOrgIncService;
	@Resource
	private OrgService orgService;
	/**
	 * 增加一个机构
	 * @param org 机构对象
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean addOrg(Org org) {
		//新增机构
		//1.机构id序列  主键生成之后  原orgIdSeq+新orgId+'.'
		//2.机构编码序列  原orgCodeSeq + 填写的 orgCode + '.'
		//3.机构名称序列  原orgNameSeq + 填写的 orgName + '.'
		org.setOrgCodeSeq(org.getOrgCodeSeq()+org.getOrgCode()+".");
		org.setOrgNameSeq(org.getOrgNameSeq()+"->"+org.getOrgName());
		long orgId = orgDao.saveSubOrg(org);
		String newOrgIdSeq = org.getOrgIdSeq() + orgId + ".";
		orgDao.updateOrgIdSeq(orgId,newOrgIdSeq);
		//机构数据新增，插入机构增量表
		Org orgForInc=orgService.queryOrgById(orgId);
		userOrgIncService.insertIncOrg(orgForInc, "A");
		return true;
	}
	/**
	 * 更新一个机构
	 * @param org 机构对象
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean updateOrg(Org org) {
		return (orgDao.updateOrg(org)>0);
	}
	/**
	 * 查询某机构信息
	 * @param orgId 机构id
	 * @return 机构对象 Org
	 */
	@Override
	@Transactional(readOnly = true)
	public Org queryOrgById(long orgId) {
		List<Org> org = orgDao.queryOrgById(orgId);
		if(org.size()>0){
			return org.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 删除机构（逻辑删除，设置机构状态为失效） 是否需要级联删除用户？？？
	 * @param orgId 机构id
	 * @return true表示操作成功，false表示操作失败
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean deleteOrgById(long orgId) {
		return (orgDao.deleteOrgById(orgId)>0);
	}
	/**
	 * 获取机构的直接子机构
	 * @param orgId 机构id
	 * @return 直接子机构列表
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Org> getDirectSubOrgsById(long orgId) {
		return orgDao.getDirectSubOrgsById(orgId);
	}
	/**
	 * 获取机构下用户列表（分页）
	 * @param orgId 机构id
	 * @param pageObj 分页对象
	 * @return {"pageObj":pageObj , "users" :users } :pageObj是 分页对象 users是机构下人员列表（不包含子机构人员）
	 */
	@Override
	@Transactional(readOnly = true)
	public Map<String , Object> getUsersByOrgIdWithPage(long orgId, String userId, String userName,PageObj pageObj) {
		Map<String , Object> map=new HashMap<String , Object>();
		map.put("pageObj", pageObj);
		map.put("users", orgDao.getUsersByOrgIdWithPage(orgId,userId, userName, pageObj));
		return map;
	}

	
	/**
	 * 获取机构下用户列表（分页）
	 * @param orgId 机构id
	 * @param pageObj 分页对象
	 * @return {"pageObj":pageObj , "users" :users } :pageObj是 分页对象 users是机构下人员列表（不包含子机构人员）
	 */
	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getAllUsersByOrgIdWithPage(long orgId,
			String userId, String userName, PageObj pageObj) {
		List<Org>list=orgDao.getOrgIdSeqByOrgId(orgId);
		String orgIdSeq="";
		if(list.size()>0){
		orgIdSeq=list.get(0).getOrgIdSeq()+"%";
		}
		Map<String , Object> map=new HashMap<String , Object>();
		map.put("pageObj", pageObj);
		map.put("users", orgDao.getAllUsersByOrgIdWithPage(orgIdSeq,userId, userName, pageObj));
		return map;
	}
	
	/**
	 * 查询用户所在机构信息
	 * @param loginId 登录账号
	 * @return
	 */
	public Org getOrgByLoginId(String loginId){
		return orgDao.getOrgByLoginId(loginId);
	}
	
	/**
	 * @author liu.tan
	 * 查询用户所在机构及其子机构
	 * @param userId 登录帐号
	 * @return 机构列表
	 */
	public List<Org> getAllOrgsByUserId(String userId){
		return orgDao.getAllOrgsByUserId(userId);
	}
	/**
	 * 更新子机构序列
	 * @param orgIdSeq 机构id序列
	 * @param newOrgSeqName 新的机构系列
	 * @param oldOrgSeqname 旧的机构序列
	 */
	@Override
	public void updateChildOrgSeq(String orgIdSeq,String newOrgNameSeq,String oldOrgNameSeq) {
		orgDao.updateChildOrgSeq(orgIdSeq, newOrgNameSeq, oldOrgNameSeq);
	}
}
