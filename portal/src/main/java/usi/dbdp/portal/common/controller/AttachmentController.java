package usi.dbdp.portal.common.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import usi.dbdp.portal.common.service.AttachmentService;
import usi.dbdp.portal.entity.FileMeta;
import usi.dbdp.portal.entity.PtlFile;
import usi.dbdp.portal.util.JacksonUtil;
import usi.dbdp.portal.util.SFTPUtils;


/**
 * 上传附件控制层（现在用sftp来做文件服务器）
 * @author lmwang
 * 创建时间：2014-4-10 下午5:27:03
 */
@Controller
@RequestMapping("/AttachmentController")
public class AttachmentController {
	
	@Resource
	private AttachmentService attachmentService;
	/**
	 * 如果get请求的参数onlyDownload=false转到允许上传并能删除附件的页面，如果onlyDownload=true转到只能下载附件的页面
	 * 上传删除 ../AttachmentController/main.do?groupCode=AAA&relationId=111&onlyDownload=false
	 * 仅下载	../AttachmentController/main.do?groupCode=AAA&relationId=111&onlyDownload=true
	 * @param model
	 * @param groupCode 附件所属分组
	 * @param relationId 关联主键（业务表的）
	 * @param onlyDownload 标识是否转到仅下载页面
	 * @return
	 */
	@RequestMapping(value = "/main.do", method = RequestMethod.GET)
	public String getMain(Model model,String groupCode,long relationId,String onlyDownload) {
		
		model.addAttribute("groupCode", groupCode);
		model.addAttribute("relationId", relationId);
		
		//根据groupCode和relationId查询附件信息
		model.addAttribute("files", attachmentService.getUploadedFiles(groupCode, relationId));
		
		//是上传附件
		if(null!=onlyDownload&&"false".equals(onlyDownload.trim())){
			
			return "system/attachmentUpload";
			
		//只下载附件，而没有上传	
		}else{
			
			return "system/attachmentDownload";
			
		}

	}
	
	/**
	 * 上传附件
	 * @param request
	 * @param response
	 * @param pw
	 * @param groupCode 附件所属分组
	 * @param relationId 关联主键（业务表的）
	 * 背景：使用@ResponseBody向页面返回json串，IE提示下载，而firefox，360等均能正常解析json文本
	 * 解决办法1：response.setContentType("text/html;charset=UTF-8");使用response.getWriter().write()写json字符串到页面（方法要抛出io异常 throws IOException）
	 * 解决办法2：response.setContentType("text/html;charset=UTF-8");使用PrintWriter.write()写json字符串到页面
	 * 备注：其中为什么用到response.setContentType("text/html;charset=UTF-8")？为了解决json字符串中有中文出现乱码的问题（特指IE，其他浏览器正常）；
	 * @throws Exception 
	 */
	@RequestMapping(value="/upload.do", method = RequestMethod.POST)
	public void upload(MultipartHttpServletRequest request, HttpServletResponse response,HttpSession session,PrintWriter pw,String groupCode,long relationId){
		
		LinkedList<FileMeta> files = null;
		try {
			files = attachmentService.upload(request,session,groupCode, relationId);
			//设置字符集防止文件名中文乱码
			response.setContentType("text/html;charset=UTF-8");
			pw.write(JacksonUtil.obj2json(files));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 上传头像
	 * @param request
	 * @param response
	 * @param pw
	 * @param groupCode 附件所属分组
	 * @param relationId 业务表主键（此处为通讯录表主键）
	 * @param userId 头像的登录账号
	 * 背景：使用@ResponseBody向页面返回json串，IE提示下载，而firefox，360等均能正常解析json文本
	 * 解决办法1：response.setContentType("text/html;charset=UTF-8");使用response.getWriter().write()写json字符串到页面（方法要抛出io异常 throws IOException）
	 * 解决办法2：response.setContentType("text/html;charset=UTF-8");使用PrintWriter.write()写json字符串到页面
	 * 备注：其中为什么用到response.setContentType("text/html;charset=UTF-8")？为了解决json字符串中有中文出现乱码的问题（特指IE，其他浏览器正常）；
	 * @throws Exception 
	 */
	@RequestMapping(value="/uploadHeadImg.do", method = RequestMethod.POST)
	public void uploadHeadImg(MultipartHttpServletRequest request, HttpServletResponse response,PrintWriter pw,String groupCode,long relationId,String userId){
		
		LinkedList<FileMeta> files = null;
		try {
			files = attachmentService.uploadHeadImg(request,groupCode,relationId,userId);
			//设置字符集防止文件名中文乱码
			response.setContentType("text/html;charset=UTF-8");
			pw.write(JacksonUtil.obj2json(files));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * 上传附件到sftp中，不插入数据库附件表
	 * @param request
	 * @param response
	 * @param pw
	 */
	@RequestMapping(value="/uploadSftp.do", method = RequestMethod.POST)
	public void uploadSftp(MultipartHttpServletRequest request,HttpServletResponse response,PrintWriter pw){
		try {
			LinkedList<FileMeta> files=attachmentService.uploadSftp(request);
			//设置字符集防止文件名中文乱码
			response.setContentType("text/html;charset=UTF-8");
			pw.write(JacksonUtil.obj2json(files));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据附件主键删除附件（逻辑删除表记录，物理删除文件）并记录附件操作日志
	 * @param request
	 * @param session
	 * @param fileId 附件主键
	 * @return
	 */
	@RequestMapping(value="/delFile.do", method = RequestMethod.POST)
	@ResponseBody
	public String delFile(HttpServletRequest request,HttpSession session,long fileId){
		return attachmentService.delFile(request, session, fileId);
	}
	
	/**
	 * 根据附件主键下载附件并记录附件操作日志
	 * @param session
	 * @param fileId 附件主键
	 * @param filePath 附件路径
	 */
	@RequestMapping(value = "/getFile.do", method = RequestMethod.GET)
	public void getFile(HttpServletRequest request,HttpServletResponse response,HttpSession session,long fileId){
		
		//二进制传输（这个设置和下面的设置，强制下载而不是执行，提供了安全性）
		response.setHeader("Content-Type", "application/octet-stream");
		PtlFile fileMeta = attachmentService.getFileMetaById(fileId);
		
		try{
			
			//设置下载时显示文件名（为防止文件名中文乱码，要转码）
			response.setHeader("Content-Disposition", "attachment; filename=" + toPathEncoding("GBK", fileMeta.getFileName()));
		    OutputStream out=response.getOutputStream();
//			FtpUtil.download(out, fileMeta.getAbsolutepath());
		    //因为防火墙问题改为sftp
		    SFTPUtils.download(out, fileMeta.getAbsolutepath());
		    out.close();	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//增加下载次数，记录附件操作日志
		attachmentService.getFileService(session, fileId);
	}
	
	/**
	 * 根据路径获取图片
	 * @param imgPath 图片路径
	 * @param response
	 */
	@RequestMapping(value = "/getImgByPath.do", method = RequestMethod.GET)
	public void getImgByPath(String imgPath,HttpServletResponse response) {
		response.setContentType("image/jpg");
		try {
			OutputStream out = response.getOutputStream();
		    SFTPUtils.download(out, imgPath);
		    out.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//编码字符串
	protected static final String DEFAULT_FILE_ENCODING = "ISO8859-1";

	//防止下载文件时，文明名中文乱码
	private String toPathEncoding(String origEncoding, String fileName)
			throws UnsupportedEncodingException {
		return new String(fileName.getBytes(origEncoding),
				DEFAULT_FILE_ENCODING);
	}
}
