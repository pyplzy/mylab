package usi.dbdp.portal.todo.service;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Description 实际调用接口的类
 * @author zhang.dechang
 * @date 2015年2月28日 上午9:30:04
 */
public class TodoIntfCallable implements Callable<String> {
	
	private final Logger logger = LogManager.getLogger(getClass());

	private String url;
	private CountDownLatch doneSignal;
	
	public TodoIntfCallable(String url, CountDownLatch doneSignal) {
		super();
		this.url = url;
		this.doneSignal = doneSignal;
	}

	@Override
	public String call() throws Exception {
		
		String result = "";
		HttpClient client = new DefaultHttpClient();
		//设置连接超时
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		//设置返回超时时间
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
		
		HttpResponse response = null;
		try {
			logger.debug("调用接口");
			
			String input = "{\"userId\":\"admin\",\"page\":\"1\",\"row\":\"5\"}";
			//String p="{\"pageObj\":\"6\"}";
			//post方式(默认方式)
			HttpPost post = new HttpPost(url);
			System.out.println("url的值为："+url);
			StringEntity entity = new StringEntity(input, "UTF-8");
			post.setEntity(entity);
			response = client.execute(post);
			result = EntityUtils.toString(response.getEntity(),"UTF-8");
		} catch (ParseException | IOException e ) {
			e.printStackTrace();
		} finally {
			if(response != null){
				//关闭流
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//关闭连接
			client.getConnectionManager().shutdown();
			this.doneSignal.countDown();
		}
		return result;
	}
	
}
