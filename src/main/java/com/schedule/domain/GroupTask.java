package com.schedule.domain;

import java.io.Serializable;
import java.util.LinkedList;

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
