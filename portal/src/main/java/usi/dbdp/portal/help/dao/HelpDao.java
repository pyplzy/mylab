package usi.dbdp.portal.help.dao;

import java.util.List;

import usi.dbdp.portal.dto.ContactUsInfo;
import usi.dbdp.portal.dto.FeedbackDto;
import usi.dbdp.portal.entity.PtlFile;
import usi.dbdp.uic.dto.PageObj;

public interface HelpDao {

	/**
	 * 查询联系我们信息
	 * @return
	 */
	public ContactUsInfo getContactUsInfo();

	/**
	 * 保存问题反馈信息
	 * @param feedbackDto
	 * @return
	 */
	public Long saveFeedback(FeedbackDto feedbackDto);

	/**
	 * 查询问题反馈列表
	 * @param feedbackDto
	 * @param pageObj
	 * @return
	 */
	public List<FeedbackDto> getFeedbackList(FeedbackDto feedbackDto,PageObj pageObj);
	
	/**
	 * 查询反馈内容
	 * @param feedbackId
	 * @return
	 */
	public FeedbackDto getFeedbackDetail(Long feedbackId);

	/**
	 * 关联附件与反馈
	 * @param feedbackId
	 * @param fileIds
	 */
	public void updateFeedbackFile(Long feedbackId, Long[] fileIds);
	
	/**
	 * 查询反馈的附件
	 * @param groupCode
	 * @param feedbackId
	 * @return
	 */
	public List<PtlFile> getFeedbackFiles(String groupCode,Long feedbackId);

	/**
	 * 保存处理信息
	 * @param feedbackDto
	 */
	public void saveHandleInfo(FeedbackDto feedbackDto);
	
}