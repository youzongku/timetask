package com.tomtop.timetask.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;

/**
 * Http请求的工具类
 * <p>
 *      note:使用HttpClinetPool,获取和关闭HttpClient
 *
 * </p>
 *
 *
 *
 * @author  ye_ziran
 * @since	2016-03-31
 */
public class HttpUtil {

	private static Logger log = Logger.getLogger(HttpUtil.class);
	
	public static final String TT_LTC = "TT_LTC";
	public static final String TT_STC = "TT_STC";


	//cookie失效时间，2小时
	//private static final int COOKIE_EXPIRE_TIME = 2 * 60 * 60;
	private static HttpClientPool pool = null;

	static {
		pool = HttpClientPool.instance();
	}


	/**
	 * 创建StringEntity
	 * @param params
	 * @return
	 *
	 */
	public static HttpEntity buildStringEntity(JsonNode params){
		return new StringEntity(params.toString(), ContentType.APPLICATION_JSON);
	}

	/**
	 * 创建StringEntity
	 * @param params
	 * @return
	 */
	public static HttpEntity buildStringEntity(Map<String, String> params){
		Set<String> keySet = params.keySet();
		ObjectNode on = Json.newObject();
		for(String key : keySet) {
			on.put(key, params.get(key));
		}
		return buildStringEntity(on);
	}

	/**
	 *
	 * 以post方式请求
	 *
	 * @param url
	 * @param reqEntity
	 * @return
	 * @throws IOException
	 *
	 * @author  ye_ziran
	 * @since	2016-03-25
	 */
	public static String post(String url, HttpEntity reqEntity){
		return post(url, reqEntity, null);
	}
	public static String post(String url, Map<String,String> params){
		return post(url, buildStringEntity(params), null);
	}
	public static String post(String url, JsonNode params){
		return post(url, buildStringEntity(params), null);
	}
	public static String post(String url, JsonNode params,Map<String,String> headers){
		return post(url, buildStringEntity(params), headers);
	}



	/**
	 *
	 * 以post方式请求
	 *
	 * @param url
	 * @param reqEntity
	 * @param header
	 * @return 请求接口后，返回的字符串
	 *
	 * @author  ye_ziran
	 * @since	2016-03-25
	 */
	public static String post(String url, HttpEntity reqEntity, Map<String,String> headers)  {
		String reponseStr = null;
		log.debug("post url = "+ url);

		CloseableHttpClient httpclient = pool.get();
		CloseableHttpResponse response = null;
		try {

			HttpUriRequest reqUri = RequestBuilder.post().setUri(new URI(url)).setEntity(reqEntity).build();
			if(null!=headers&&headers.size()>0){
				for(Map.Entry<String, String> entry:headers.entrySet()){
					reqUri.addHeader(entry.getKey(), entry.getValue());
				}
			}
			response = httpclient.execute(reqUri);

			if(response != null) {
				HttpEntity entity = response.getEntity();
				reponseStr = EntityUtils.toString(entity);
			}else{
				log.debug("请求超时，URL="+url);
			}

		} catch (URISyntaxException e) {
			log.error("无效的URI："+url ,e);
		} catch (ClientProtocolException e) {
			log.error("客户端协议错误："+url ,e);
		} catch (UnsupportedEncodingException e) {
			log.error("无效的编码格式,", e);
		} catch (IOException e) {
			log.error("httpclient.execute错误, errorMsg = ", e);
		} finally {
			pool.close(httpclient);
		}
		return reponseStr;
	}

	/**
	 * 以get方式请求
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		String reponseStr = null;
		log.info("get url = "+url);
		CloseableHttpClient httpclient = pool.get();
		try{

			HttpGet getMethod = new HttpGet(url);
			HttpResponse response = httpclient.execute(getMethod);
			HttpEntity entity = response.getEntity();
			reponseStr = EntityUtils.toString(entity);
		}catch(Exception e){
			log.error("HttpUtil.post error.", e);
		} finally {
			pool.close(httpclient);
		}
		return reponseStr;
	}
	/**
	 * http get方式请求
	 * @param url
	 * @param headers
	 * @return
	 */
	public static String get(String url,Map<String,String> headers){
		String reponseStr = null;
		log.info("get url = "+url);

		CloseableHttpClient httpclient = pool.get();
		try{

			HttpGet getMethod = new HttpGet(url);
			if(null!=headers&&headers.size()>0){
				for(Map.Entry<String, String> entry:headers.entrySet()){
					getMethod.addHeader(entry.getKey(), entry.getValue());
				}
			}
			HttpResponse response = httpclient.execute(getMethod);
			HttpEntity entity = response.getEntity();
			reponseStr = EntityUtils.toString(entity);
		}catch(Exception e){
			log.error("HttpUtil.post error.", e);
		} finally {
			pool.close(httpclient);
		}
		return reponseStr;
	}

}
