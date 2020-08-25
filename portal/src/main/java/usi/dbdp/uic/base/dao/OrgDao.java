package usi.dbdp.uic.base.dao;

import java.util.List;

import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.dto.UserInfo;
import usi.dbdp.uic.entity.Org;

/**
 * @author zhqnie
 * @version 2015年9月6日 上午10:32:53
 * 说明
 */
public interface OrgDao {

	/**
	 * 增加一个机构(返回主键id)
	 * @param org
	 * @return
	 */
	public long saveSubOrg(Org org);

	/**
	 * 更新机构 id_seq和name_seq
	 * @param seq
	 * @param orgId
	 */
	public int saveOrgSeq(String orgIdSeq, String orgNameSeq,
			long orgId);

	/**
	 * 更新机构
	 * @param org
	 * @return
	 */
	public int updateOrg(Org org);

	/**
	 * 查询某机构信息
	 * @param orgId
	 * @return
	 */
	public List<Org> queryOrgById(long orgId);

	/**
	 * 删除机构（逻辑删除，设置机构状态为失效） 是否需要级联删除用户？？？
	 * @param orgId
	 * @return
	 */
	public int deleteOrgById(Long orgId);

	/**
	 * 获取机构的直接子机构
	 * @param orgId
	 * @return
	 */
	public List<Org> getDirectSubOrgsById(long orgId);
	
	/**
	 * 获取当前当前机构ID的机构ID序列
	 * @param orgId
	 * @return orgIdSeq 
	 */
	public List<Org> getOrgIdSeqByOrgId(long orgId);

	/**
	 * 获取当前登录人机构下用户列表（分页）
	 * @param orgId
	 * @param pageObj
	 * @return
	 */
	public List<UserInfo> getAllUsersByOrgIdWithPage(String orgIdSeq,
			String userId, String userName, PageObj pageObj);

	/**
	 * 获取机构下用户列表（分页）
	 * @param orgId
	 * @param pageObj
	 * @return
	 */
	public List<UserInfo> getUsersByOrgIdWithPage(long orgId,
			String userId, String userName, PageObj pageObj);


	/**
	 * 根据orgIdSeq返回所有父机构（包括自己）
	 * @param orgIdSeq 机构id序列
	 * @return 返回所有父机构（包括自己）
	 */
	public List<Org> getParentOrgsByOrgIdSeq(String orgIdSeq);

	
	/**
	 * 查询用户所在机构信息
	 * @param loginId 登录账号
	 * @return
	 */
	public Org getOrgByLoginId(String loginId);

	public void updateOrgIdSeq(long orgId, String newOrgIdSeq);
	
	
	/**   
	* @Title: getAllOrg    
	* @Description: 获取所有的机构数据
	* @return
	* @author 
	* @Date   2017年2月8日   
	*/
	public List<Org> getAllOrg();

	public List<Org> getAllOrgsByUserId(String userId);
	/**
	 * 更新子机构序列
	 * @param orgIdSeq 机构id序列
	 * @param newOrgSeqName 新的机构系列
	 * @param oldOrgSeqname 旧的机构序列
	 */
	public void updateChildOrgSeq(String orgIdSeq,String newOrgNameSeq,String oldOrgNameSeq);
}