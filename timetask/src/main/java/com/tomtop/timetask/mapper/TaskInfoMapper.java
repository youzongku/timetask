package com.tomtop.timetask.mapper;

import com.tomtop.timetask.config.InsertUseGeneratedKeysMapper;
import com.tomtop.timetask.config.SqlMapper;
import com.tomtop.timetask.entity.TaskInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 *
 */
public interface TaskInfoMapper  extends SqlMapper<TaskInfo> , InsertUseGeneratedKeysMapper<TaskInfo>{

	/**
	 * 查询所有启用状态的任务
	 * @return
	 */
	List<TaskInfo> getTaskInfos();

	/**
	 * 根据任务名称和分组名称查询任务信息
	 * @author youz
	 * @date 2017/8/16
	 * @param taskName 任务名称
	 * @param groupName 分组名称
	 * @return
	 */
	TaskInfo selectByTaskNameAndGroupName(@Param("taskName") String taskName, @Param("groupName") String groupName);

	/**
	 * 根据任务名称和分组名称更新任务信息
	 * @author youz
	 * @date 2017/8/15
	 * @param taskInfo
	 * @return
	 */
	int updateByTaskNameAndGroupName(TaskInfo taskInfo);

	/**
	 * 更新所有任务状态
	 * @author youz
	 * @date 2017/8/16
	 * @param status 任务状态
	 */
	int updateAllTaskStatus(@Param("status") Integer status);

	/**
	 * 根据任务名称和分组名称删除任务信息
	 * @author youz
	 * @date 2017/8/16
	 * @param taskName 任务名称
	 * @param groupName 分组名称
	 * @return
	 */
	int deleteByTaskNameAndGroupName(@Param("taskName") String taskName, @Param("groupName") String groupName);
}