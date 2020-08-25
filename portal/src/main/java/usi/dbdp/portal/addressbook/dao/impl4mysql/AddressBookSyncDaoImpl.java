package usi.dbdp.portal.addressbook.dao.impl4mysql;

import org.springframework.stereotype.Repository;

import usi.common.annotation.MysqlDb;
import usi.dbdp.portal.addressbook.dao.AddressBookSyncDao;
import usi.dbdp.portal.util.JdbcDaoSupport4mysql;
import usi.dbdp.uic.entity.User;

/**
 * 机构人员同步到通讯录的dao
 * @author lmwang
 * 创建时间：2015-5-7 下午12:50:09
 */
@MysqlDb
@Repository
public class AddressBookSyncDaoImpl extends JdbcDaoSupport4mysql implements AddressBookSyncDao{

	/**
	 * 插入到通讯录
	 * @param user 用户对象
	 * @return 插入记录个数
	 */
	public int insert2AddressBook(User user) {
		String sql = "insert into ptl_address_book (name,gender,mobile_no,user_id) values(?,?,?,?)";
		//机构人员存的是012，通讯存的是未知 男 女（很牛的三元表达式吧）
		String genderStr = user.getGender()==0?"未知":(user.getGender()==1?"男":"女");
		return this.getJdbcTemplate().update(sql, new Object[] {user.getUserName(),genderStr,user.getMobileNo(),user.getUserId()});
	}
	/**
	 * 更新到通讯录
	 * @param user 用户对象
	 * @return 更新记录个数
	 */
	public int updateAddressBookByUserId(User user) {
		String sql = "update ptl_address_book set name=?,gender=?,mobile_no=?,user_cata='系统用户' where user_id=?";
		//机构人员存的是012，通讯存的是未知 男 女（很牛的三元表达式吧）
		String genderStr = user.getGender()==0?"未知":(user.getGender()==1?"男":"女");
		return this.getJdbcTemplate().update(sql, new Object[] {user.getUserName(),genderStr,user.getMobileNo(),user.getUserId()});
	}
	
	/**
	 * 更新通讯录公司和部门
	 * @param company 公司（集团或者是省）
	 * @param department 部门（机构名称序列去掉省以后的，或者是空）
	 * @param userId 登录账号
	 * @return
	 */
	public int updateABcompanyAndDepartment(String company,String department,String userId) {
		String sql = "update ptl_address_book set company=?,deparment=? where user_id=?";
		return this.getJdbcTemplate().update(sql, new Object[] {company,department,userId});
	}
}
