package usi.dbdp.portal.addressbook.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import usi.dbdp.portal.addressbook.service.AddressBookManagerService;
import usi.dbdp.portal.addressbook.service.SearchService;
import usi.dbdp.portal.entity.FileMeta;
import usi.dbdp.portal.entity.PtlAddressBook;
import usi.dbdp.portal.util.JacksonUtil;
import usi.dbdp.portal.util.SFTPUtils;
import usi.dbdp.uic.dto.PageObj;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 通讯录
 * @author lmwang
 * 创建时间：2015-5-8 上午10:52:37
 */
@Controller
@RequestMapping("/ab")
public class AddressbookController {

	@Resource
	private SearchService searchService;
	@Resource
	private AddressBookManagerService addressBookManagerService;
	
	/**
	 * 通讯录管理菜单
	 * @return
	 */
	@RequestMapping(value = "/menu_ab_abManager.do", method = RequestMethod.GET)
	public String abManager() {
		return "portal/ab/abManager";
	}
	
	/**
	 * 个人信息维护菜单
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/menu_self_selfABinfoMaintain.do", method = RequestMethod.GET)
	public String selfABinfoMaintain(Model model,HttpSession session) {
		//从session获取登录账号
		String userId = (String)session.getAttribute("userId");
		PtlAddressBook ptlAddressBook =searchService.getDetailUserId(userId);
		//转换为json字符串
		String tmpStr="";
		try {
			tmpStr = JacksonUtil.obj2json(ptlAddressBook);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		model.addAttribute("ptlAddressBookJson",tmpStr );
		model.addAttribute("ptlAddressBook",ptlAddressBook );
		return "portal/ab/selfABinfoMaintain";
	}
	/**
	 * 搜索通讯录，默认按名称
	 * @param searchValue 搜索的值
	 * @param pageObj
	 * @return
	 */
	@RequestMapping(value = "/menu_ab_doSearch.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String , Object> searchAddressBoos(String searchValue,PageObj pageObj){
		return searchService.queryPersonInfoByParamService(searchValue, 2, pageObj);
	}
	
	/**
	 * 根据主键获取通讯录详细信息
	 * @param addressBookId 通讯录主键
	 * @return
	 */
	@RequestMapping(value = "/menu_ab_getABdetail.do", method = RequestMethod.POST)
	@ResponseBody
	public PtlAddressBook getDetailAddressBookById(int addressBookId) {
		return searchService.getDetailAddressBookById(addressBookId);
	}
	
	/**
	 * 显示头像
	 * @param imgPath 头像路径
	 * @param response
	 */
	@RequestMapping(value = "/menu_ab_getHeadImg.do", method = RequestMethod.GET)
	public void getHeadImg(String imgPath,HttpServletResponse response) {
		response.setContentType("image/jpg");
		try {
			OutputStream out=response.getOutputStream();
		    SFTPUtils.download(out, imgPath);
		    out.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据主键删除通讯录记录
	 * @param addressBookId 通讯录记录主键
	 */
	@RequestMapping(value = "/menu_ab_delABrec.do", method = RequestMethod.POST)
	public void deleteABrecord(int addressBookId,PrintWriter pw) {
		if(addressBookManagerService.deletePersonFromAddressBook(addressBookId)==1) {
			pw.write("success"); 
		}else {
			pw.write("fail"); 
		}
	}
	/**
	 * 更新通讯录人员信息
	 * @param ptlAddressBook 通讯录实体
	 */
	@RequestMapping(value = "/menu_ab_updateABrec.do", method = RequestMethod.POST)
	public void updateABrec(PtlAddressBook ptlAddressBook,PrintWriter pw) {
		if(addressBookManagerService.updatePersonInAddressBook(ptlAddressBook)==1) {
			pw.write("success"); 
		}else {
			pw.write("fail"); 
		}
	}
	/**
	 * 新增通讯录记录
	 * @param ptlAddressBook 通讯录实体
	 */
	@RequestMapping(value = "/menu_ab_addABrec.do", method = RequestMethod.POST)
	public void addABrec(PtlAddressBook ptlAddressBook,PrintWriter pw) {
		if(addressBookManagerService.addAddressBookRecord(ptlAddressBook)==1) {
			pw.write("success"); 
		}else {
			pw.write("fail"); 
		}
	}
	/**
	 * excel导入通讯录信息
	 * @param request
	 * @param pw
	 */
	@RequestMapping(value = "/menu_ab_impAddressBookFromExcel.do", method = RequestMethod.POST)
	public void impAddressBookFromExcel(MultipartHttpServletRequest request,PrintWriter pw) {
		LinkedList<FileMeta> files = new LinkedList<FileMeta>();
		FileMeta fileMeta = new FileMeta();
		//默认导入失败
		int impNum =-2;
		MultipartFile file=request.getFile("files[]");//单文件上传
		if(file.isEmpty()){
			impNum=0;//空文件
		}else  if(file.getSize()>10485760){	 //上传文件大小限制为10M
			impNum= -1;
		 }else{
			 try {
				 impNum = addressBookManagerService.batchImportPtlAddressBook(file.getInputStream());
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
		 }
		fileMeta.setFileSize(String.valueOf(impNum));
		files.add(fileMeta);
		try {
			pw.write(JacksonUtil.obj2json(files));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
}
