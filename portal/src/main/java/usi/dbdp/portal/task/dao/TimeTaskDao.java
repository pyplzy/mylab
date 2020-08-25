package usi.dbdp.portal.task.dao;

import java.util.List;

import usi.dbdp.portal.task.dto.SysJobDto;
import usi.dbdp.portal.task.dto.SysNodeDto;
import usi.dbdp.portal.task.dto.SysTimerDto;
import usi.dbdp.uic.dto.PageObj;

public interface TimeTaskDao {
	
	//查询所有的任务
	List<SysJobDto> sercAllJob();
	//新增一个任务
	long addNjob(SysJobDto job);
	//删除一个任务
	int deljobById(Integer jobId);
	//修改一个任务
	int updateJob(SysJobDto job);
	
	//查询所有的节点
	List<SysNodeDto> sercAllNode();
	//新增一个任务
	long addNode(SysNodeDto node);
	//删除一个任务
	int delNodeById(Integer nodeId);
	//修改一个任务
	int updateNode(SysNodeDto node);
	
	//分页查询定时任务
		List<SysTimerDto> searchByPage(SysTimerDto tt, PageObj pageObj);
	//条件查询所有定时任务
	List<SysTimerDto> search(SysTimerDto timer);
	//新增定时任务
	int addTask(SysTimerDto timer);
	//修改定时任务
	int updateTask(SysTimerDto timer);
	//删除
	int delTaskById(Integer timerId);
	
	//更新所有的定时任务状态
	public void updateJobTask(int status,String ip);
	//根据定时器Id查询数据 
	SysTimerDto queryJobTaskByJobId(SysTimerDto jobTaskDto, int shardingId);
	//更新指定的定时任务状态
	void updateJobTaskByJobId(SysTimerDto jobTaskDto, int i);
	
	//查询定时器表中原有的记录
	public SysTimerDto queryFormerTask(SysTimerDto jobTaskDto, Integer shardingId);
}
