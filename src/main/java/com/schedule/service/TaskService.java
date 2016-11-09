package com.schedule.service;

import java.util.List;

import com.schedule.domain.vo.GroupTask;

/**
 * 任务Service
 * @author 887961 
 * @Date 2016年11月9日 下午6:31:55
 */
public interface TaskService {

	public List<GroupTask> findTask();
}
