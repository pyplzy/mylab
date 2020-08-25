package usi.dbdp.portal.task.dao.impl4mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import usi.common.annotation.MysqlDb;
import usi.dbdp.portal.task.dao.TimeTaskDao;
import usi.dbdp.portal.task.dto.SysJobDto;
import usi.dbdp.portal.task.dto.SysNodeDto;
import usi.dbdp.portal.task.dto.SysTimerDto;
import usi.dbdp.portal.util.JdbcDaoSupport4mysql;
import usi.dbdp.uic.dto.PageObj;
import usi.dbdp.uic.util.CommonUtil;
@Repository
@MysqlDb
public class TimeTaskDaoImpl extends JdbcDaoSupport4mysql implements TimeTaskDao {

	@Override
	public List<SysJobDto> sercAllJob() {//查询所有
		String sql="SELECT JOB_ID,JOB_CD,JOB_NAME,CONTENT,ENABLE FROM UIC_JOB WHERE DEL_FLAG=0  ";
		return this.getJdbcTemplate().query(sql, new RowMapper<SysJobDto>(){
			@Override
			public SysJobDto mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				SysJobDto j=new SysJobDto();
				j.setJobId(rs.getInt("JOB_ID"));
				j.setJobCd(rs.getString("JOB_CD"));
				j.setJobName(rs.getString("JOB_NAME"));
				j.setContent(rs.getString("CONTENT"));
				j.setEnAble(rs.getInt("ENABLE"));
				return j;
			}
		});
	}

	@Override
	public long addNjob(final SysJobDto job) {//新增
		KeyHolder key=new GeneratedKeyHolder();
		this.getJdbcTemplate().update(new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				 String sql="INSERT INTO UIC_JOB VALUES(NULL,?,?,?,?,?)";
				 PreparedStatement ps = con.prepareStatement(sql,new String[]{"JOB_ID"}); 
				 ps.setString(1, job.getJobCd());
				 ps.setString(2, job.getJobName());
				 ps.setString(3, job.getContent());
				 ps.setInt(4, job.getEnAble());
				 ps.setInt(5, 0);
				return ps;
			}}, key);
		return key.getKey().longValue();
	}

	@Override
	public int deljobById(Integer jobId) {//逻辑删除
		String sql="UPDATE UIC_JOB SET DEL_FLAG=1 WHERE JOB_ID=?";
		return this.getJdbcTemplate().update(sql, jobId);
	}

	@Override
	public int updateJob(SysJobDto job) {//修改数据
		String sql="UPDATE UIC_JOB SET JOB_NAME=?,JOB_CD=?,ENABLE=?,CONTENT=? WHERE JOB_ID=?";
		Object[] args=new Object[]{job.getJobName(),job.getJobCd(),job.getEnAble(),job.getContent(),job.getJobId()};
		return this.getJdbcTemplate().update(sql, args);
	}

	@Override
	public List<SysNodeDto> sercAllNode() {//查询所有Node
		String sql="SELECT APP_NODE_ID,NODE_NAME,NODE_TYPE,IP_ADDRESS,PORT_VALUE,CONTENT,CONCAT(IP_ADDRESS,':',PORT_VALUE) IPPORT FROM UIC_NODE ";
		return this.getJdbcTemplate().query(sql, new RowMapper<SysNodeDto>(){
			@Override
			public SysNodeDto mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				SysNodeDto n=new SysNodeDto();
				n.setAppNodeId(rs.getInt("APP_NODE_ID"));
				n.setNodeName(rs.getString("NODE_NAME"));
				n.setNodeType(rs.getInt("NODE_TYPE"));
				n.setIpAddress(rs.getString("IP_ADDRESS"));
				n.setPortValue(rs.getInt("PORT_VALUE"));
				n.setContent(rs.getString("CONTENT"));
				n.setIpport(rs.getString("IPPORT"));
				return n;
			}
		});
	}

	@Override
	public long addNode(final SysNodeDto node) {//新增Node
		KeyHolder key=new GeneratedKeyHolder();
		this.getJdbcTemplate().update(new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				String sql="INSERT INTO UIC_NODE VALUES(NULL,?,?,?,?,?)";
				 PreparedStatement ps = con.prepareStatement(sql,new String[]{"APP_NODE_ID"});
				 ps.setString(1, node.getNodeName());
				 ps.setInt(2, node.getNodeType());
				 ps.setString(3, node.getIpAddress());
				 ps.setInt(4, node.getPortValue());
				 ps.setString(5, node.getContent());
				return ps;
			}},key);
		return key.getKey().longValue();
	}

	@Override
	public int delNodeById(Integer nodeId) {//物理删除
		String sql="DELETE FROM UIC_NODE WHERE APP_NODE_ID=?";
		return this.getJdbcTemplate().update(sql, nodeId);
	}

	@Override
	public int updateNode(SysNodeDto node) {//修改Node
		String sql="UPDATE UIC_NODE SET IP_ADDRESS=?,PORT_VALUE=?,NODE_NAME=?,NODE_TYPE=?,CONTENT=? "
				+ " WHERE APP_NODE_ID=? ";
		Object[] args=new Object[]{node.getIpAddress(),node.getPortValue(),node.getNodeName(),node.getNodeType(),node.getContent(),node.getAppNodeId()};
		return this.getJdbcTemplate().update(sql,args);
	}
	
	@Override//条件查询：不分页
	public List<SysTimerDto> search(SysTimerDto timer) {
		TaskRowMapper mapper=new TaskRowMapper();
		String sql="SELECT t1.TIMER_ID,t1.TIMER_NAME,t1.JOB_ID,t1.NODE_ID,t1.CLASS_NAME,t1.EXCUTE_METHOD,t1.TIMER_STATUS,"
				+ " t1.CRON_EXPRESSION,t1.TIMER_DESC,t1.BUSI_PARA,t1.ENABLE,CONCAT(t2.IP_ADDRESS,':',t2.PORT_VALUE) NODE,t1.ENABLE+t3.ENABLE ISENABLE "
				+ " FROM UIC_TIMER t1,UIC_NODE t2,UIC_JOB t3 "
				+ " WHERE t1.DEL_FLAG=0 AND t1.JOB_ID=t3.JOB_ID AND t1.NODE_ID=t2.APP_NODE_ID  ";
		if(timer.getJobId()!=null && timer.getJobId()>-1){//jobId
			sql+=" AND t3.JOB_ID="+timer.getJobId();
		}
		if(timer.getNodeId()!=null && timer.getNodeId()>-1){//nodeId
			sql+=" AND t2.APP_NODE_ID="+timer.getNodeId();
		}
		if(CommonUtil.hasValue(timer.getTimerName())){//timeName
			sql+=" AND t1.TIMER_NAME LIKE '%"+timer.getTimerName()+"%' ";
		}
		if( timer.getEnAble()!=null && timer.getEnAble()>-1){//是否启用：定时器和任务都为启用，则为启用，否则为停用状态
			if(timer.getEnAble()==0){
				sql+=" AND( t1.ENABLE=0 OR t3.ENABLE=0) ";
			}else if(timer.getEnAble()==1){
				sql+=" AND t1.ENABLE=1 AND t3.ENABLE=1 ";
			}
		}
		return this.getJdbcTemplate().query(sql, mapper);
	}

	@Override//新增
	public int addTask(SysTimerDto t) {
		String sql="INSERT INTO UIC_TIMER VALUES(NULL,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP,?,0)";
		Object[] args=new Object[]{t.getTimerName(),t.getJobId(),t.getNodeId(),t.getClassName(),t.getExcuteMethod(),0,
				t.getCronExpression(),t.getTimerDesc(),t.getBusiPara(),t.getEnAble()};
		return this.getJdbcTemplate().update(sql, args);
	}

	@Override//修改
	public int updateTask(SysTimerDto t) {
		String sql="UPDATE UIC_TIMER SET JOB_ID=?,NODE_ID=?,TIMER_NAME=?,CLASS_NAME=?,EXCUTE_METHOD=?,CRON_EXPRESSION=?,"
				+ "BUSI_PARA=?,ENABLE=?,TIMER_DESC=? WHERE TIMER_ID=?";
		Object[] args=new Object[]{t.getJobId(),t.getNodeId(),t.getTimerName(),t.getClassName(),t.getExcuteMethod(),t.getCronExpression(),
				t.getBusiPara(),t.getEnAble(),t.getTimerDesc(),t.getTimerId()};
		return this.getJdbcTemplate().update(sql, args);
	}

	@Override//逻辑删除
	public int delTaskById(Integer timerId) {
		String sql="UPDATE UIC_TIMER SET DEL_FLAG=1 WHERE TIMER_ID=?";
		return this.getJdbcTemplate().update(sql, timerId);
	}

	@Override
	public SysTimerDto queryJobTaskByJobId(SysTimerDto jobTaskDto,
			int shardingId) {
		TaskRowMapper mapper=new TaskRowMapper();
		String sql="SELECT t1.TIMER_ID,t1.TIMER_NAME,t1.JOB_ID,t1.NODE_ID,t1.CLASS_NAME,t1.EXCUTE_METHOD,t1.TIMER_STATUS,"
				+ "t1.CRON_EXPRESSION,t1.TIMER_DESC,t1.BUSI_PARA,t1.ENABLE,CONCAT(t2.IP_ADDRESS,':',t2.PORT_VALUE) NODE,t1.ENABLE+t3.ENABLE ISENABLE "
				+ " FROM UIC_TIMER t1,UIC_NODE t2,UIC_JOB t3 "
				+ " WHERE t1.DEL_FLAG=0 AND t1.JOB_ID=t3.JOB_ID AND t1.NODE_ID=t2.APP_NODE_ID "
				+ "AND  AND t1.TIMER_ID=?";
		return this.getJdbcTemplate().query(sql, new Object[]{jobTaskDto.getTimerId()}, mapper).get(0);
	}
//修改过定时器的状态
	@Override
	public void updateJobTaskByJobId(SysTimerDto jobTaskDto, int status) {
		String sql = "UPDATE UIC_TIMER SET TIMER_STATUS=? WHERE TIMER_ID=?  ";
		this.getJdbcTemplate().update(sql, new Object[]{status,jobTaskDto.getTimerId()});
	}

	@Override
	public void updateJobTask(int status, String ip) {
		String updateJobTaskStatus = "";
		if(ip!=null){
			updateJobTaskStatus = "UPDATE UIC_TIMER t SET  t.TIMER_STATUS=? where t.node_id in (select a.APP_NODE_ID from uic_node a where a.IP_ADDRESS = ? )";
			this.getJdbcTemplate().update(updateJobTaskStatus, new Object[]{status,ip});		
		}else{
			updateJobTaskStatus = "UPDATE UIC_TIMER t SET  t.TIMER_STATUS=? ";
			this.getJdbcTemplate().update(updateJobTaskStatus, new Object[]{status});		
		}
	}

	@Override//分页查询
	public List<SysTimerDto> searchByPage(SysTimerDto timer, PageObj pageObj) {
		TaskRowMapper mapper=new TaskRowMapper();
		String sql="SELECT t1.TIMER_ID,t1.TIMER_NAME,t1.JOB_ID,t1.NODE_ID,t1.CLASS_NAME,t1.EXCUTE_METHOD,t1.TIMER_STATUS,"
				+ "t1.CRON_EXPRESSION,t1.TIMER_DESC,t1.BUSI_PARA,t1.ENABLE,CONCAT(t2.IP_ADDRESS,':',t2.PORT_VALUE) NODE,t1.ENABLE+t3.ENABLE ISENABLE "
				+ " FROM UIC_TIMER t1,UIC_NODE t2,UIC_JOB t3 "
				+ " WHERE t1.DEL_FLAG=0 AND t1.JOB_ID=t3.JOB_ID AND t1.NODE_ID=t2.APP_NODE_ID  ";
		if(timer.getJobId()!=null && timer.getJobId()>-1){//jobId
			sql+=" AND t3.JOB_ID="+timer.getJobId();
		}
		if(timer.getNodeId()!=null && timer.getNodeId()>-1){//nodeId
			sql+=" AND t2.APP_NODE_ID="+timer.getNodeId();
		}
		if(CommonUtil.hasValue(timer.getTimerName())){//timeName
			sql+=" AND t1.TIMER_NAME LIKE '%"+timer.getTimerName()+"%' ";
		}
		if( timer.getEnAble()!=null && timer.getEnAble()>-1){//是否启用：定时器和任务都为启用，则为启用，否则为停用状态
			if(timer.getEnAble()==0){
				sql+=" AND( t1.ENABLE=0 OR t3.ENABLE=0) ";
			}else if(timer.getEnAble()==1){
				sql+=" AND t1.ENABLE=1 AND t3.ENABLE=1 ";
			}
		}
		return this.queryByPage(sql,mapper, pageObj);
	}
	private class TaskRowMapper implements RowMapper<SysTimerDto>{
		@Override
		public SysTimerDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			SysTimerDto t=new SysTimerDto();
			t.setTimerId(rs.getInt("TIMER_ID"));
			t.setTimerName(rs.getString("TIMER_NAME"));
			t.setJobId(rs.getInt("JOB_ID"));
			t.setNodeId(rs.getInt("NODE_ID"));
			t.setClassName(rs.getString("CLASS_NAME"));
			t.setBusiPara(rs.getString("BUSI_PARA"));
			t.setExcuteMethod(rs.getString("EXCUTE_METHOD"));
			t.setTimerStatus(rs.getInt("TIMER_STATUS"));
			t.setCronExpression(rs.getString("CRON_EXPRESSION"));
			t.setTimerDesc(rs.getString("TIMER_DESC"));
			t.setBusiPara(rs.getString("BUSI_PARA"));
			t.setEnAble(rs.getInt("ENABLE"));
			t.setNode(rs.getString("NODE"));
			t.setIsEnAble(rs.getInt("ISENABLE"));
			return t;
		}
		
	}
	
	//查询定时器表中原有的记录
	@Override
	public SysTimerDto queryFormerTask(SysTimerDto jobTaskDto,Integer shardingId) {
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "SELECT TIMER_ID,TIMER_NAME,JOB_ID,NODE_ID,CLASS_NAME,EXCUTE_METHOD,CRON_EXPRESSION,TIMER_DESC,BUSI_PARA,CREATE_TIME FROM UIC_TIMER WHERE TIMER_ID=? ";
		return this.getJdbcTemplate().queryForObject(sql, new Object[]{jobTaskDto.getTimerId()}, new RowMapper<SysTimerDto>(){
			@Override
			public SysTimerDto mapRow(ResultSet rs, int rowNum)throws SQLException {
				SysTimerDto sysTimerDto = new SysTimerDto();
				sysTimerDto.setTimerId(rs.getInt("TIMER_ID"));
				sysTimerDto.setTimerName(rs.getString("TIMER_NAME"));
				sysTimerDto.setJobId(rs.getInt("JOB_ID"));
				sysTimerDto.setNodeId(rs.getInt("NODE_ID"));
				sysTimerDto.setClassName(rs.getString("CLASS_NAME"));
				sysTimerDto.setExcuteMethod(rs.getString("EXCUTE_METHOD"));
				sysTimerDto.setCronExpression(rs.getString("CRON_EXPRESSION"));
				sysTimerDto.setTimerDesc(rs.getString("TIMER_DESC"));
				sysTimerDto.setBusiPara(rs.getString("BUSI_PARA"));
				try {
					sysTimerDto.setCreateTime(new Timestamp(format.parse(rs.getString("CREATE_TIME")).getTime()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return sysTimerDto;
			}
		});
	}
}
