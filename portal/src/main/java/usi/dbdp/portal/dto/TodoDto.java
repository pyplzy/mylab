package usi.dbdp.portal.dto;

/**
 * 我的待办
 * @author zhang.dechang
 * 2017年1月11日 下午2:24:16
 */
public class TodoDto{
	
	private String module; 		//模块
	
	private Integer count;		//待办数
	
	private String url;			//待办任务处理页面url

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public TodoDto(String module, Integer count, String url) {
		super();
		this.module = module;
		this.count = count;
		this.url = url;
	}
	
}
