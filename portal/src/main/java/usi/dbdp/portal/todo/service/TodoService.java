package usi.dbdp.portal.todo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import usi.dbdp.portal.dto.TodoDto;
import usi.dbdp.portal.util.CommonUtil;
import usi.dbdp.portal.util.JacksonUtil;
import usi.dbdp.uic.entity.AppRegister;

/**
 * 调用待办接口的服务
 * 
 * @author zhang.dechang 2017年2月17日 上午9:20:10
 */
@Service
public class TodoService {

	private final Logger logger = LogManager.getLogger(getClass());

	// 线程池
	/*
	 * @Resource ThreadPoolTaskExecutor threadPoolTaskExecutor;
	 * 
	 * @SuppressWarnings("unchecked") public List<Map<String,Object>>
	 * invokeInterface(List<AppRegister> myapps) throws InterruptedException {
	 * List<Map<String,Object>> todoList = new ArrayList<Map<String,Object>>();
	 * 
	 * int urlCount = 0; //需要调的接口数 for (AppRegister app : myapps) {
	 * //只保留配置了待办接口url的应用 if(CommonUtil.hasValue(app.getTodoUrl())){ urlCount++;
	 * Map<String,Object> sysinfo = new HashMap<String,Object>();
	 * sysinfo.put("sysname", app.getAppName()); sysinfo.put("icon",
	 * app.getAppImgPath()); sysinfo.put("intfurl", app.getTodoUrl());
	 * todoList.add(sysinfo); } }
	 * 
	 * if(urlCount > 0){
	 * 
	 * 异步调接口，等全部接口完成进行下一步处理(解析报文)
	 * 
	 * CountDownLatch doneSignal = new CountDownLatch(urlCount); for
	 * (Map<String,Object> app : todoList) { TodoIntfCallable call = new
	 * TodoIntfCallable((String) app.get("intfurl"), doneSignal); Future<String>
	 * future = threadPoolTaskExecutor.submit(call); app.put("future", future);
	 * } doneSignal.await();
	 * 
	 * 
	 * 解析接口返回报文
	 * 
	 * Iterator<Map<String,Object>> iterator = todoList.iterator();
	 * while(iterator.hasNext()){ Map<String,Object> app = iterator.next();
	 * Future<String> future = (Future<String>) app.get("future"); String
	 * intfurl = (String) app.get("intfurl"); try { String result =
	 * future.get(); logger.info("请求url【{}】,返回报文【{}】",intfurl,result);
	 * 
	 * JSONObject root = JSON.parseObject(result); JSONArray modules =
	 * root.getJSONArray("todoList");
	 * 
	 * List<TodoDto> todos = new ArrayList<TodoDto>(); for(Object o : modules){
	 * JSONObject info = (JSONObject)o;
	 * 
	 * String module = info.getString("moduleName"); int count =
	 * info.getInteger("toDoNbr"); String url = info.getString("toDoHyperlink");
	 * TodoDto todoDto = new TodoDto(module, count, url); todos.add(todoDto); }
	 * app.put("todo", todos); app.remove("intfurl"); app.remove("future"); }
	 * catch (Exception e) { iterator.remove(); e.printStackTrace(); } } }
	 * 
	 * return todoList; }
	 * 
	 * }
	 */
	@Resource
	ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> invokeInterface(List<AppRegister> myapps) throws InterruptedException {
		List<Map<String, Object>> todoList = new ArrayList<Map<String, Object>>();

		int urlCount = 0; // 需要调的接口数
		for (AppRegister app : myapps) { // 只保留配置了待办接口url的应用
			if (CommonUtil.hasValue(app.getTodoUrl())) {
				urlCount++;
				Map<String, Object> sysinfo = new HashMap<String, Object>();
				sysinfo.put("sysname", app.getAppName());
				sysinfo.put("icon", app.getAppImgPath());
				sysinfo.put("intfurl", app.getTodoUrl());
				sysinfo.put("appurl", app.getAppUrl());
				todoList.add(sysinfo);
			}
		}

		if (urlCount > 0) {
			/*
			 * 异步调接口，等全部接口完成进行下一步处理(解析报文)
			 */
			CountDownLatch doneSignal = new CountDownLatch(urlCount);
			for (Map<String, Object> app : todoList) {
				
				TodoIntfCallable call = new TodoIntfCallable((String) app.get("intfurl"), doneSignal);
				Future<String> future = threadPoolTaskExecutor.submit(call);
				app.put("future", future);
				
			}
			doneSignal.await();
		
			/*
			 * 解析接口返回报文
			 */
			Iterator<Map<String, Object>> iterator = todoList.iterator();
			while (iterator.hasNext()) {
				Map<String, Object> app = iterator.next();
				Future<String> future = (Future<String>) app.get("future");
				System.out.println("future的值是" + future);
				String intfurl = (String) app.get("intfurl"); 
				try {
					String result = future.get();
					logger.info("请求url【{}】,返回报文【{}】", intfurl, result);
					if(!CommonUtil.hasValue(result)){
						continue;
					}
				
					JSONObject jsonObj=JSONObject.parseObject(result);
					String ddd=jsonObj.getString("count");
					String dffx=jsonObj.getString("list");
					System.out.println(ddd);
					System.out.println(dffx);
					List<TodoDto> todos=JacksonUtil.json2list(dffx,TodoDto.class);
					System.out.println("todos的值为："+todos);
					app.put("count",ddd);
					app.put("todo", todos);
					app.remove("intfurl");
					app.remove("future");
				} catch (Exception e) {
					iterator.remove();
					e.printStackTrace();
				}
			}
		}

		return todoList;
	}

}

