package usi.dbdp.portal.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

/**
 * HTTP工具类
 * @author jianjian.li
 * @2013-11-19
 */
public class HttpclientUtil {
	
	/**
	 * 获取一个httpclient客户端
	 * @return
	 */
	public static HttpClient getHttpClient() {
		PoolingClientConnectionManager pccm = new PoolingClientConnectionManager();  
		pccm.setDefaultMaxPerRoute(20); //每个主机的最大并行链接数   
		pccm.setMaxTotal(100);          //客户端总并行链接最大数  
		HttpClient httpClient = new DefaultHttpClient(pccm); 
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);//设置连接超时（毫秒）
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);//设置返回超时时间（毫秒）
		return httpClient;
	}
		
	/**
	 * 发送POST请求
	 * @param url 请求的URL
	 * @param content 请求的参数
	 * @return
	 */
	public static String sendRequestByPost(String url,String content){
	    HttpClient httpClient = getHttpClient(); 
		String result = null;
		HttpPost httpPost = new HttpPost(url);//构造请求
		httpPost.setHeader("SOAPAction", "");//非必要
		
		try {
			//内容实体
			httpPost.setEntity(new StringEntity(content, "UTF-8"));
			//发送
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			//返回结果转换成字符串
			result = EntityUtils.toString(entity,"UTF-8");
			EntityUtils.consume(entity);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}
	
	/**
	 * 发送POST请求（可设置超时时间）
	 * @param url 请求的URL
	 * @param content 请求的参数
	 * @param timeout 超时时间（毫秒）
	 * @return
	 */
	public static String sendRequestByPost(String url,String content,int timeout){
	    HttpClient httpClient = getHttpClient(); 
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);//设置连接超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);//设置返回超时时间
		String result = null;
		HttpPost httpPost = new HttpPost(url);//构造请求
		httpPost.setHeader("SOAPAction", "");//非必要
		
		try {
			//内容实体
			httpPost.setEntity(new StringEntity(content, "UTF-8"));
			//发送
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			//返回结果转换成字符串
			result = EntityUtils.toString(entity,"UTF-8");
			EntityUtils.consume(entity);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}	
	
	/**
	 * 发送GET请求
	 * @param url 请求的URL
	 * @return
	 */
	public static String sendRequestByGet(String url){
		
	    HttpClient httpClient = getHttpClient(); 
		String result = null;
		HttpGet httpGet = new HttpGet(url);//构造请求
		
		try {
			//发送
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			//返回结果转换成字符串
			result = EntityUtils.toString(entity,"UTF-8");
			EntityUtils.consume(entity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}
	
	/**
	 * 发送GET请求（可设置超时时间）
	 * @param url 请求的URL
	 * @param timeout 超时时间（毫秒）
	 * @return
	 */
	public static String sendRequestByGet(String url,int timeout){
	    HttpClient httpClient = getHttpClient(); 
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);//设置连接超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);//设置返回超时时间
		String result = null;
		HttpGet httpGet = new HttpGet(url);//构造请求
		
		try {
			//发送
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			//返回结果转换成字符串
			result = EntityUtils.toString(entity,"UTF-8");
			EntityUtils.consume(entity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}	
	
}
