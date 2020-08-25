package usi.dbdp.portal.addressbook.dao.impl4oracle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import usi.common.annotation.OracleDb;
import usi.dbdp.portal.addressbook.dao.SearchDao;
import usi.dbdp.portal.app.util.OraclePasswdUtil;
import usi.dbdp.portal.dto.PtlAddressBookDto;
import usi.dbdp.portal.dto.PtlPabGroupAndPersonInfo;
import usi.dbdp.portal.entity.PtlAddressBook;
import usi.dbdp.portal.entity.PtlPabGroup;
import usi.dbdp.portal.util.JdbcDaoSupport4oracle;
import usi.dbdp.uic.dto.PageObj;

/**
 * 通讯录搜索增、删、改、查dao层
 * @author lxci
 * 创建时间：2015-03-25 上午 9:00:00
 */
@OracleDb
@Repository
public class SearchDaoImpl extends JdbcDaoSupport4oracle implements SearchDao{

	/**
	 * 搜索通讯录人员列表
	 * 
	 */
	public List<PtlAddressBook> queryPersonList(PageObj pageObj) {
		String infoSql = "SELECT ADDRESS_BOOK_ID "
                +OraclePasswdUtil.getDecryptStr("NAME")+" AS NAME ," 
                +" TITLE, "
                +" ADMINISTRATIVE_LEVEL ,"
                +" DEPARMENT, "
                +" EMAIL ,"
                +" GENDER ,"
                +" COMPANY ,"
                +OraclePasswdUtil.getDecryptStr("MOBILE_NO")+" AS MOBILE_NO ,"
                +OraclePasswdUtil.getDecryptStr("FIXED_LINE_TEL")+" AS FIXED_LINE_TEL ,"
                +" USER_CATA,"
                + " HEAD_IMG,"
                + " USER_ID "
		           +" FROM PTL_ADDRESS_BOOK";
		return this.queryByPage(infoSql, new RowMapper<PtlAddressBook>(){
			public PtlAddressBook mapRow(ResultSet rs, int rowNum)throws SQLException {
				PtlAddressBook pab = new PtlAddressBook();
				pab.setAddressBookId(rs.getInt("ADDRESS_BOOK_ID"));
				pab.setName(rs.getString("NAME"));
				pab.setTitle(rs.getString("TITLE"));
				pab.setAdministrativeLevel(rs.getString("ADMINISTRATIVE_LEVEL"));
				pab.setDeparment(rs.getString("DEPARMENT"));
				pab.setEmail(rs.getString("EMAIL"));
				pab.setGender(rs.getString("GENDER"));
				pab.setCompany(rs.getString("COMPANY"));
				pab.setMobileNo(rs.getString("MOBILE_NO"));
				pab.setFixedLineTel(rs.getString("FIXED_LINE_TEL"));
				pab.setUserCata(rs.getString("USER_CATA"));
				pab.setHeadImg(rs.getString("HEAD_IMG"));
				pab.setUserId(rs.getString("USER_ID"));
				return pab;
			}
			
		}, pageObj);
	}
	
	/**
	 * 查询通讯录人员具体信息
	 * @param flag=1根据user_id查询人员信息；2根据姓名模糊匹配；其他时根据通讯录主键查询人员信息
	 */
	public List<PtlAddressBookDto> queryPersonInfoByParam(Object param,int flag,PageObj pageObj){
		
		String infoSql ="";
		if(flag==1){
			infoSql="SELECT * FROM PTL_ADDRESS_BOOK A WHERE A.USER_ID =? ";
		}else if(flag==2){
			infoSql="SELECT ADDRESS_BOOK_ID, "+OraclePasswdUtil.getDecryptStr("NAME")+" AS NAME ,DEPARMENT FROM PTL_ADDRESS_BOOK A WHERE " +OraclePasswdUtil.getDecryptStr("NAME")+" like concat(?,'%')";
		}else{
			infoSql="SELECT * FROM PTL_ADDRESS_BOOK A WHERE A.ADDRESS_BOOK_ID=?";
		}
		
		return this.queryByPage(infoSql,new Object[]{param},new RowMapper<PtlAddressBookDto>(){
					public PtlAddressBookDto mapRow(ResultSet rs, int rowNum) throws SQLException {
						PtlAddressBookDto pab = new PtlAddressBookDto();
						pab.setAddressBookId(rs.getInt("ADDRESS_BOOK_ID"));
						pab.setName(rs.getString("NAME"));
						pab.setDeparment(rs.getString("DEPARMENT"));
						return pab;
					}
				},pageObj);
	}
/*public List<PtlAddressBookDto> queryPersonInfoByParam(Object param,int flag,PageObj pageObj){
		
		String infoSql ="";
		if(flag==1){
			infoSql="SELECT * FROM PTL_ADDRESS_BOOK A WHERE A.USER_ID =? ";
		}else if(flag==2){
			infoSql="SELECT ADDRESS_BOOK_ID,NAME,DEPARMENT FROM PTL_ADDRESS_BOOK A WHERE A.NAME like concat(?,'%')" ;
		}else{
			infoSql="SELECT * FROM PTL_ADDRESS_BOOK A WHERE A.ADDRESS_BOOK_ID=?";
		}
		
		return this.queryByPage(infoSql,new Object[]{param},new RowMapper<PtlAddressBookDto>(){
					public PtlAddressBookDto mapRow(ResultSet rs, int rowNum) throws SQLException {
						
						PtlAddressBookDto pab = new PtlAddressBookDto();
						pab.setAddressBookId(rs.getInt("ADDRESS_BOOK_ID"));
						pab.setName(rs.getString("NAME"));
						pab.setDeparment(rs.getString("DEPARMENT"));
						return pab;
					}
				},pageObj);
	}
	*/
	
	/**
	 * 根据主键查询通讯录具体信息
	 * @param addressBookId 主键
	 * @return 
	 */
	public PtlAddressBook queryPersonDetailInfoById(int addressBookId){
		
		String infoSql = "SELECT ADDRESS_BOOK_ID, "
                +OraclePasswdUtil.getDecryptStr("NAME")+" AS NAME ," 
                +" TITLE, "
                +" ADMINISTRATIVE_LEVEL ,"
                +" DEPARMENT, "
                +" EMAIL ,"
                +" GENDER ,"
                +" COMPANY ,"
                +OraclePasswdUtil.getDecryptStr("MOBILE_NO")+" AS MOBILE_NO ,"
                +OraclePasswdUtil.getDecryptStr("FIXED_LINE_TEL")+" AS FIXED_LINE_TEL ,"
                +" USER_CATA,"
                + " HEAD_IMG,"
                + " USER_ID "
		        +" FROM PTL_ADDRESS_BOOK A WHERE A.ADDRESS_BOOK_ID=?"; 
		List<PtlAddressBook> tmpPtlAddressBookLst =  this.getJdbcTemplate().query(infoSql,new Object[]{addressBookId},new RowMapper<PtlAddressBook>(){
					public PtlAddressBook mapRow(ResultSet rs, int rowNum) throws SQLException {
						PtlAddressBook pab = new PtlAddressBook();
						pab.setAddressBookId(rs.getInt("ADDRESS_BOOK_ID"));
						pab.setName(rs.getString("NAME"));
						pab.setTitle(rs.getString("TITLE"));
						pab.setAdministrativeLevel(rs.getString("ADMINISTRATIVE_LEVEL"));
						pab.setDeparment(rs.getString("DEPARMENT"));
						pab.setEmail(rs.getString("EMAIL"));
						pab.setGender(rs.getString("GENDER"));
						pab.setCompany(rs.getString("COMPANY"));
						pab.setMobileNo(rs.getString("MOBILE_NO"));
						pab.setFixedLineTel(rs.getString("FIXED_LINE_TEL"));
						pab.setUserCata(rs.getString("USER_CATA"));
						pab.setHeadImg(rs.getString("HEAD_IMG"));
						pab.setUserId(rs.getString("USER_ID"));
						return pab;
					}
				});
		return tmpPtlAddressBookLst.size()>0?tmpPtlAddressBookLst.get(0):null;
	}	
	
	/**
	 * 根据userId查询通讯录具体信息
	 * @param userId 登录账号
	 * @return 
	 */
	public PtlAddressBook queryPersonDetailInfoByUserId(String userId){
		
		String infoSql = "SELECT ADDRESS_BOOK_ID, "
                +OraclePasswdUtil.getDecryptStr("NAME")+" AS NAME ," 
                +" TITLE, "
                +" ADMINISTRATIVE_LEVEL ,"
                +" DEPARMENT, "
                +" EMAIL ,"
                +" GENDER ,"
                +" COMPANY ,"
                +OraclePasswdUtil.getDecryptStr("MOBILE_NO")+" AS MOBILE_NO ,"
                +OraclePasswdUtil.getDecryptStr("FIXED_LINE_TEL")+" AS FIXED_LINE_TEL ,"
                +" USER_CATA,"
                + " HEAD_IMG,"
                + " USER_ID "
		        +" FROM PTL_ADDRESS_BOOK A WHERE A.USER_ID=?";
		List<PtlAddressBook> tmpPtlAddressBookLst =  this.getJdbcTemplate().query(infoSql,new Object[]{userId},new RowMapper<PtlAddressBook>(){
					public PtlAddressBook mapRow(ResultSet rs, int rowNum) throws SQLException {
						PtlAddressBook pab = new PtlAddressBook();
						pab.setAddressBookId(rs.getInt("ADDRESS_BOOK_ID"));
						pab.setName(rs.getString("NAME"));
						pab.setTitle(rs.getString("TITLE"));
						pab.setAdministrativeLevel(rs.getString("ADMINISTRATIVE_LEVEL"));
						pab.setDeparment(rs.getString("DEPARMENT"));
						pab.setEmail(rs.getString("EMAIL"));
						pab.setGender(rs.getString("GENDER"));
						pab.setCompany(rs.getString("COMPANY"));
						pab.setMobileNo(rs.getString("MOBILE_NO"));
						pab.setFixedLineTel(rs.getString("FIXED_LINE_TEL"));
						pab.setUserCata(rs.getString("USER_CATA"));
						pab.setHeadImg(rs.getString("HEAD_IMG"));
						pab.setUserId(rs.getString("USER_ID"));
						return pab;
					}
				});
		return tmpPtlAddressBookLst.size()>0?tmpPtlAddressBookLst.get(0):null;
	}		
	
	/**
	 * 根据个人的userId查询个人通讯录的分组及人员信息
	 */
	
	public List<PtlPabGroupAndPersonInfo> queryGroupAndPersonInfo(String userId,final PageObj pageObj){
		
		String groupAndInfoSql="SELECT * FROM PTL_P_ADDRESS_BOOK A WHERE A.USER_ID=?";
		return this.getJdbcTemplate().query(groupAndInfoSql, new Object[]{userId}, new RowMapper<PtlPabGroupAndPersonInfo>(){

			public PtlPabGroupAndPersonInfo mapRow(ResultSet rs, int rowNum)throws SQLException {
				PtlPabGroupAndPersonInfo ppgnpi = new PtlPabGroupAndPersonInfo();
				//太多的话最多查100个
				PageObj subPageObj = new PageObj();
				subPageObj.setPage(1);
				subPageObj.setRows(100);
				List<PtlAddressBookDto> ptladdressbook = queryPersonInfoByParam(rs.getInt("ADDRESS_BOOK_ID"),2,subPageObj);
				ppgnpi.setGroupId(rs.getInt("GROUP_ID"));
				ppgnpi.setGroupName(rs.getString("GROUP_NAME"));
				ppgnpi.setAddressBookId(rs.getInt("ADDRESS_BOOK_ID"));
				ppgnpi.setPtladdressbook(ptladdressbook);
				return ppgnpi;
			}
		});
	}

	/**
	 * 查询个人通讯录联系人组名
	 */
	public List<PtlPabGroup> queryPabGroup(String userId){
		String groupSql="SELECT * FROM PTL_PAB_GROUP A WHERE A.USER_ID =?";
	 return this.getJdbcTemplate().query(groupSql, new Object[]{userId}, new RowMapper<PtlPabGroup>(){

			public PtlPabGroup mapRow(ResultSet rs, int rowNum)throws SQLException {
				PtlPabGroup ppg = new PtlPabGroup();
				ppg.setGroupId(rs.getInt("GROUP_ID"));
				ppg.setGroupName(rs.getString("GROUP_NAME"));
				ppg.setUserId(rs.getString("USER_ID"));
				return ppg;
			}
		});
	}
	
	/**
	 * 创建个人通讯录联系人组（暂未使用）
	 */
	public int addPabGroup(String groupName,String userId){
		String addgroupSql="INSERT INTO PTL_PAB_GROUP(GROUP_NAME,USER_ID) VALUES(?,?)";
	    return this.getJdbcTemplate().update(addgroupSql, new Object[]{groupName,userId});
	}
	
	/**
	 * 将人员添加到相应的通讯录联系人组（暂未使用）
	 *
	 */
	
	public int addPersonToGroup(String userId, int addressBookId, int groupId){
		 String addSql="INSERT INTO PTL_P_ADDRESS_BOOK(USER_ID,ADDRESS_BOOK_ID,GROUP_ID) VALUES(?,?,?)";
		return this.getJdbcTemplate().update(addSql, new Object[]{userId,addressBookId,groupId});
	}
	
}
