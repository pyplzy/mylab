package usi.dbdp.uic.base.service;

import java.util.List;
import java.util.Map;

import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.entity.Org;

/**
 * 机构服务接口
 * @author lmwang
 * 创建时间：2015-2-6 上午10:46:23
 */
public interface OrgService {
	
	/**
	 * 增加一个机构
	 * @param org 机构对象
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean addOrg(Org org);
	
	/**
	 * 更新一个机构
	 * @param org 机构对象
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean updateOrg(Org org);
	
	/**
	 * 查询某机构信息
	 * @param orgId 机构id
	 * @return 机构对象 Org
	 */
	public Org queryOrgById(long orgId);
	
	/**
	 * 删除机构（逻辑删除，设置机构状态为失效） 是否需要级联删除用户？？？
	 * @param orgId 机构id
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean deleteOrgById(long orgId);
	
	/**
	 * 获取机构的直接子机构
	 * @param orgId 机构id
	 * @return 直接子机构列表
	 */
	public List<Org> getDirectSubOrgsById(long orgId);
	
	/**
	 * 获取机构下用户列表（分页）
	 * @param orgId 机构id
	 * @param pageObj 分页对象
	 * @return {"pageObj":pageObj , "users" :users } :pageObj是 分页对象 users是用户列表 
	 */
	public Map<String , Object> getUsersByOrgIdWithPage(long orgId, String userId, String userName,PageObj pageObj);
	/**
	 * 获取机构下用户列表（分页）
	 * @param orgId 机构id
	 * @param pageObj 分页对象
	 * @return {"pageObj":pageObj , "users" :users } :pageObj是 分页对象 users是用户列表 
	 */
	public Map<String , Object> getAllUsersByOrgIdWithPage(long orgId, String userId, String userName,PageObj pageObj);
	
	/**
	 * 查询用户所在机构信息
	 * @param loginId 登录账号
	 * @return
	 */
	public Org getOrgByLoginId(String loginId);
	
	/**
	 * 查询用户所在机构及其子机构
	 * @param userId 登录帐号
	 * @return 机构列表
	 */
	public List<Org> getAllOrgsByUserId(String userId);
	/**
	 * 更新子机构序列
	 * @param orgIdSeq 机构id序列
	 * @param newOrgSeqName 新的机构系列
	 * @param oldOrgSeqname 旧的机构序列
	 */
	public void updateChildOrgSeq(String orgIdSeq,String newOrgNameSeq,String oldOrgNameSeq);
}
