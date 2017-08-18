package com.tomtop.timetask.utils;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import play.libs.Json;

public class JsonCaseUtil {
	
	private static ObjectMapper map = new ObjectMapper();
	/**
	 * 获取node的内容
	 * @param node
	 * @param key
	 * @return 没有此key，返回空字符串
	 */
	public static String getStringValue(JsonNode node, String key) {
		return isNotJsonEmpty(node.get(key))  ? (node.get(key).asText() != "null" ? node.get(key).asText() : "") : "";
	}

	/**
	 * 获取node的内容
	 * @param node
	 * @param key
	 * @return 没有此key，返回0
	 */
	public static Integer getIntegerValue(JsonNode node, String key) {
		return isNotJsonEmpty(node.get(key))  ? node.get(key).asInt() : 0;
	}

	/**
	 * 获取值
	 * @param money
	 * @return money为null，返回0
	 */
	public static Double getValue(Double money) {
		return money != null ? money : 0;
	}

	/**
	 * 获取node的内容
	 * @param node
	 * @param key
	 * @return 没有此key，返回0
	 */
	public static Double getDoubleValue(JsonNode node, String key) {
		return isNotJsonEmpty(node.get(key)) ? node.get(key).asDouble() : 0;
	}

	/**
	 * 获取node的内容
	 * @param json
	 * @return json为空，返回null
	 */
	public static Date jsonToDate(JsonNode json) {
		return isNotJsonEmpty(json)? new Date(json.asLong()) : null;
	}


	public static boolean isJsonEmpty(JsonNode json) {
		return null ==json || "null".equals(json.asText()) || "".equals(json.toString());
	}
	
	public static boolean isNotJsonEmpty(JsonNode json) {
		return !isJsonEmpty(json);
	}

	public static Boolean jsonToBoolean(JsonNode json){
		return isJsonEmpty(json)?null:json.asBoolean();
	}
	public static String jsonToString(JsonNode json) {
		return isJsonEmpty(json) ? null : json.asText();
	}

	public static Integer jsonToInteger(JsonNode json) {
		return isJsonEmpty(json) ? null: json.asInt() ;
	}

	public static Double jsonToDouble(JsonNode json) {
		return isJsonEmpty(json) ? null:json.asDouble() ;
	}
	
	public static  Short JsonToShort(JsonNode json){
		return isJsonEmpty(json) ? null:Short.valueOf(json.asText()) ;
	}
	
	/**
	 * 校验参数完整性方法
	 * 
	 * @author zbc
	 * @since 2017年2月20日 上午9:43:45
	 */
	public static boolean checkParam(JsonNode node, String... fields) {
		if (node != null) {
			for (String p : fields) {
				if (isJsonEmpty(node.get(p))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public static <T> T jsonToBean(String jsonStr,Class<T> t){
		try {
			return map.readValue(jsonStr, t);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T jsonToBean(String jsonStr,TypeReference<T> t){
		try {
			return map.readValue(jsonStr, t);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static JsonNode parse(HttpServletRequest request) {
		try {
			return Json.parse(request.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}	
	}
}
