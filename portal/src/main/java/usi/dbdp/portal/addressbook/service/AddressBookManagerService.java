package usi.dbdp.portal.addressbook.service;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import usi.dbdp.portal.addressbook.dao.AddressBookManagerDao;
import usi.dbdp.portal.entity.PtlAddressBook;
import usi.dbdp.uic.util.CommonUtil;

/**
 * 通讯录Service
 * @author lxci
 * 创建时间：2015-3-27 上午9:00:00
 */

@Service
public class AddressBookManagerService {
	
	@Resource
	private AddressBookManagerDao  addressBookManagerDao;
	
	/**
	 * 删除通讯录记录
	 * @param addressBookId 通讯录主键
	 * @return
	 */
	@Transactional
	public int deletePersonFromAddressBook(int addressBookId){
		return addressBookManagerDao.deletePersonFromAddressBook(addressBookId);
	}
	
	/**
	 * 更新通讯录人员信息
	 * @param ptlAddressBook 通讯录实体
	 * @return
	 */
	@Transactional
	public int updatePersonInAddressBook(PtlAddressBook ptlAddressBook) {
		return addressBookManagerDao.updatePersonInAddressBook(ptlAddressBook);
	}
	/**
	 * 新增通讯录记录
	 * @param ptlAddressBook 通讯录实体
	 * @return
	 */
	@Transactional
	public int addAddressBookRecord(PtlAddressBook ptlAddressBook) {
		return addressBookManagerDao.addAddressBookRecord(ptlAddressBook);
	}
	
	/**
	 * 从excel批量导入添加联系人
	 * @param inputStream
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public int batchImportPtlAddressBook(InputStream inputStream) {
		List<PtlAddressBook> ptlAddressBooks = new LinkedList<PtlAddressBook>();
		int[] returnArray = null;
		try {
			// 创建对Excel工作簿文件的引用
			Workbook workbook=WorkbookFactory.create(inputStream);
			// 在Excel文档中，第一张工作表的缺省索引是0
			Sheet sheet=workbook.getSheetAt(0);
			// 获取到Excel文件中的所有行数
			int rows = sheet.getPhysicalNumberOfRows();
			// 遍历行（第一行是标题）
			for (int i = 1; i < rows; i++) {
				Row row = sheet.getRow(i);
				if (row != null) {
					String s1= row.getCell(0).toString();
					if(CommonUtil.hasValue(s1)){//姓名为空 数据无效
						PtlAddressBook ptladdressbook = new PtlAddressBook();
						ptladdressbook.setName(s1);
						ptladdressbook.setTitle(row.getCell(1).toString());
						ptladdressbook.setAdministrativeLevel(row.getCell(2).toString());
						ptladdressbook.setDeparment(row.getCell(3).toString());
						ptladdressbook.setEmail(row.getCell(4).toString());
						ptladdressbook.setGender(row.getCell(5).toString());
						ptladdressbook.setCompany(row.getCell(6).toString());
						ptladdressbook.setMobileNo(row.getCell(7).toString());
						ptladdressbook.setFixedLineTel(row.getCell(8).toString());
						ptladdressbook.setUserCata(row.getCell(9).toString());
						ptladdressbook.setHeadImg("");
						ptladdressbook.setUserId(row.getCell(10).toString());
						
						ptlAddressBooks.add(ptladdressbook);
					}
				}
			}
			returnArray = addressBookManagerDao.batchAddPersonInfos(ptlAddressBooks);
		} catch (Exception e1) {
			e1.printStackTrace();
			return -2;
		}
		return null==returnArray?0:returnArray.length;
	}

}
