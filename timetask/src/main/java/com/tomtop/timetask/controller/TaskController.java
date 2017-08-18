package com.tomtop.timetask.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.tomtop.timetask.service.ITaskService;
import com.tomtop.timetask.utils.JsonCaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 
 * TODO 
 * 1. 新增任务 已完成
 * 2. 暂停任务 
 * 3. 删除任务 
 * 4. 全部暂停
 * 5. 全部启动
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/task")
public class TaskController {

	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	private ITaskService taskService;

	/**
	 * 新增任务
	 * 
	 * { "url": "http://new2.b2b.com/member/custaccount?email=15012902046",
	 * "taskName": "query customer service", "createUser": "superadmin", "cron":
	 * "0/1 * * * * ?", "groupName": "member", "triggerName": "Once a minute" }
	 * { "url": "http://new1.b2b.com/sales/pushSalesToB2C", "taskName": "push
	 * sales order to b2c", "createUser": "superadmin", "cron": "0 *\/1 * * *
	 * ?", "groupName": "sales", "triggerName": "Ten minutes" }
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/add")
	public @ResponseBody Map<String, Object> createTask(HttpServletRequest request) {
		Map<String, Object> result = Maps.newHashMap();
		JsonNode params = JsonCaseUtil.parse(request);
		logger.info("新增任务参数:{}", params);
		if (JsonCaseUtil.checkParam(params, "url", "taskName", "createUser", "cron", "groupName", "triggerName")) {
			return taskService.create(params.toString());
		}
		result.put("suc", false);
		result.put("msg", "参数错误");
		return result;
	}

	/**
	 * 2. 暂停任务
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/pause")
	public @ResponseBody Map<String, Object> pauseTask(HttpServletRequest request) {
		Map<String, Object> result = Maps.newHashMap();
		JsonNode params = JsonCaseUtil.parse(request);
		logger.info("暂停任务参数:{}", params);
		if (JsonCaseUtil.checkParam(params, "taskName", "groupName")) {
			return taskService.pause(params.toString());
		}
		return result;
	}

	/**
	 * 3. 删除任务
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/deleteTask")
	public @ResponseBody Map<String, Object> deleteTask(HttpServletRequest request) {
		Map<String, Object> result = Maps.newHashMap();
		JsonNode params = JsonCaseUtil.parse(request);
		logger.info("删除任务参数:{}", params);
		if (JsonCaseUtil.checkParam(params, "taskName", "groupName")) {
			return taskService.deleteTask(params.toString());
		}
		return result;
	}

	/**
	 * 4. 全部暂停
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/pauseAllTask")
	public @ResponseBody Map<String, Object> pauseAllTask() {
		return taskService.pauseAllTask();
	}
	
	/**
	 *  5.全部启动
	 *
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/startAllTask")
	public @ResponseBody Map<String, Object> startAllTask() {
		return taskService.startAllTask();
	}

	/**
	 * 更新任务
	 * @author youz
	 * @date 2017/8/18
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/updateTask")
	public @ResponseBody Map<String, Object> updateTask(HttpServletRequest request) {
		Map<String, Object> result = Maps.newHashMap();
		JsonNode params = JsonCaseUtil.parse(request);
		logger.info("更新任务参数:{}", params);
		if (JsonCaseUtil.checkParam(params, "taskName", "groupName")) {
			return taskService.updateTask(params.toString());
		}
		return result;
	}

	@RequestMapping(value = "/demo1")
	@ResponseBody
	public Map<String, Object> demo1() {
		Map<String, Object> resultMap = Maps.newHashMap();
		String resultStr = "demo1";
		resultMap.put("result", true);
		resultMap.put("message", resultStr);
		logger.info(">>>>>[" + resultStr + "]<<<<<");
		return resultMap;
	}

	@RequestMapping(value = "/demo2")
	@ResponseBody
	public Map<String, Object> demo2() {
		Map<String, Object> resultMap = Maps.newHashMap();
		String resultStr = "demo2";
		resultMap.put("result", true);
		resultMap.put("message", resultStr);
		logger.info(">>>>>>[" + resultStr + "]<<<<<<");
		return resultMap;
	}

	@RequestMapping(value = "/demo3")
	@ResponseBody
	public Map<String, Object> demo3() {
		Map<String, Object> resultMap = Maps.newHashMap();
		String resultStr = "demo3";
		resultMap.put("result", true);
		resultMap.put("message", resultStr);
		logger.info(">>>>>>>[" + resultStr + "]<<<<<<<");
		return resultMap;
	}
}
