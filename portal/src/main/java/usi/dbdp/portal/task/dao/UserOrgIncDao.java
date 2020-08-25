package usi.dbdp.portal.task.dao;

 
import java.util.List;

import usi.dbdp.uic.entity.Org;
import usi.dbdp.uic.entity.UicOrgInc;
import usi.dbdp.uic.entity.UicUserInc;
import usi.dbdp.uic.entity.User;

public interface UserOrgIncDao {
	/**
	 * 根据工号获取用户信息(只查询用户表，不关联其他表，不考虑是否逻辑删除)
	 * @param id 用户主键
	 * @return 用户信息模型
	 */
	public User getUserByUserIdForInc(Long id);
	/**
	 * 人员变更信息插入用户信息增量表
 	* @param User
 	* @param incType
 	* @return boolean
	 */
	public boolean insertIncUser(User user,String incType);
	/**
	 * 机构变更信息插入机构信息增量表
 	* @param org
 	* @param incType
 	* @return void
	 */
	public boolean insertIncOrg(Org org, String incType);
	
	/**   
	* @Title: getAllUicOrgInc    
	* @Description: 获取小于等于指定时间的机构增量信息
	* @return
	* @author johnDong
	* @Date   2017年2月9日   
	*/
	public List<UicOrgInc> getUicOrgIncByDate(String date);
	
	/**   
	* @Title: deleteUicOrgIncByDate    
	* @Description: 删除小于等于指定时间的机构增量数据
	* @param date
	* @return
	* @author johnDong
	* @Date   2017年2月9日   
	*/
	public int deleteUicOrgIncByDate(String date);
	
	
	/**   
	* @Title: getAllUicOrgInc    
	* @Description: 获取小于等于指定时间的人员增量信息
	* @return
	* @author johnDong
	* @Date   2017年2月9日   
	*/
	public List<UicUserInc> getUicUserIncByDate(String date);
	
	/**   
	* @Title: deleteUicOrgIncByDate    
	* @Description: 删除小于等于指定时间的人员增量数据
	* @param date
	* @return
	* @author johnDong
	* @Date   2017年2月9日   
	*/
	public int deleteUicUserIncByDate(String date);
	
}
