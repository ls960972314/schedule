package com.schedule.domain.vo;

import java.io.Serializable;
import java.util.LinkedList;

import com.schedule.domain.Task;

/**
 * 任务组
 * @author 887961 
 * @Date 2016年11月2日 下午3:19:18
 */
public class GroupTask implements Serializable {

	private static final long serialVersionUID = 7924805557560427805L;

	private String groupName;
	
	private LinkedList<Task> taskList;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public LinkedList<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(LinkedList<Task> taskList) {
		this.taskList = taskList;
	}
	
	
}
