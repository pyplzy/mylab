package usi.dbdp.portal.addressbook.dao.impl4mysql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import usi.common.annotation.MysqlDb;
import usi.dbdp.portal.addressbook.dao.AddressBookManagerDao;
import usi.dbdp.portal.entity.PtlAddressBook;
import usi.dbdp.portal.util.JdbcDaoSupport4mysql;


/**
 * 通讯录管理增、删、改、查dao层
 * @author lxci
 * 创建时间：2015-03-26 下午 3:00:00
 */
@MysqlDb
@Repository
public class AddressBookManagerDaoImpl extends JdbcDaoSupport4mysql implements AddressBookManagerDao{
	
	/**
	 * 删除通讯录人员
	 */
	public int deletePersonFromAddressBook(int addressBookId){
		String deleteSql="DELETE FROM PTL_ADDRESS_BOOK WHERE ADDRESS_BOOK_ID = ?";
		return this.getJdbcTemplate().update(deleteSql, new Object[]{addressBookId});
	}
	
	/**
	 * 更新通讯录人员信息
	 * @param ptlAddressBook 通讯录实体
	 * @return
	 */
	public int updatePersonInAddressBook(PtlAddressBook ptlAddressBook) {
		String sql = "update ptl_address_book set name=?,title=?,administrative_level=?,deparment=?,email=?,gender=?,\n" +
						"company=?,mobile_no=?,fixed_line_tel=?,user_cata=?,head_img=?,user_id=?\n" + 
						"where address_book_id=?";
		return this.getJdbcTemplate().update(sql, new Object[] {ptlAddressBook.getName(),ptlAddressBook.getTitle(),ptlAddressBook.getAdministrativeLevel(),
				ptlAddressBook.getDeparment(),ptlAddressBook.getEmail(),ptlAddressBook.getGender(),ptlAddressBook.getCompany(),
				ptlAddressBook.getMobileNo(),ptlAddressBook.getFixedLineTel(),ptlAddressBook.getUserCata(),ptlAddressBook.getHeadImg(),
				ptlAddressBook.getUserId(),ptlAddressBook.getAddressBookId()});
	}
	
	/**
	 * 新增通讯录记录
	 * @param ptlAddressBook 通讯录实体
	 * @return
	 */
	public int addAddressBookRecord(PtlAddressBook ptlAddressBook) {
		String sql = "insert into ptl_address_book(name,title,administrative_level,deparment,email,gender,company,mobile_no,fixed_line_tel,user_cata,head_img) "+
				"values(?,?,?,?,?,?,?,?,?,?,?)";
		return this.getJdbcTemplate().update(sql, new Object[] {ptlAddressBook.getName(),ptlAddressBook.getTitle(),ptlAddressBook.getAdministrativeLevel(),
				ptlAddressBook.getDeparment(),ptlAddressBook.getEmail(),ptlAddressBook.getGender(),ptlAddressBook.getCompany(),
				ptlAddressBook.getMobileNo(),ptlAddressBook.getFixedLineTel(),ptlAddressBook.getUserCata(),ptlAddressBook.getHeadImg()});
		
	}
	
	/**
	 * 批量添加新的通讯录联系人
	 * 
	 */
	public int[] batchAddPersonInfos(final List<PtlAddressBook> ptladdressbooks){
		
		String addSql="insert into ptl_address_book(name,title,administrative_level,deparment,email,gender,company,mobile_no,fixed_line_tel,user_cata,head_img,user_id) values(?,?,?,?,?,?,?,?,?,?,?,?)";
		return super.getJdbcTemplate().batchUpdate(addSql, new BatchPreparedStatementSetter(){

			public void setValues(PreparedStatement ps, int i)throws SQLException {
				PtlAddressBook ptladdressbook = ptladdressbooks.get(i);
				 ps.setString(1,ptladdressbook.getName());
				 ps.setString(2,ptladdressbook.getTitle());
				 ps.setString(3,ptladdressbook.getAdministrativeLevel());
				 ps.setString(4,ptladdressbook.getDeparment());
				 ps.setString(5,ptladdressbook.getEmail());
				 ps.setString(6,ptladdressbook.getGender());
				 ps.setString(7,ptladdressbook.getCompany());
				 ps.setString(8,ptladdressbook.getMobileNo());
				 ps.setString(9,ptladdressbook.getFixedLineTel());
				 ps.setString(10,ptladdressbook.getUserCata());
				 ps.setString(11,ptladdressbook.getHeadImg());
				 if("".equals(ptladdressbook.getUserId())) {
					 ps.setNull(12, Types.VARCHAR);
				 }else {
					 ps.setString(12,ptladdressbook.getUserId());
				 }
				 
			}

			public int getBatchSize() {
				return ptladdressbooks.size();
			}
			
		} );
	}
	
}
