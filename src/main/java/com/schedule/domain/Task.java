package com.schedule.domain;

import java.io.Serializable;

public class Task implements Serializable{

	private static final long serialVersionUID = 8066779905898235424L;

	private Long id;
	
	private String taskName;
	
	private String groupName;
	
	private Long nextTaskId;
	
	private String cronExpression;
	
	private Integer priority;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Long getNextTaskId() {
		return nextTaskId;
	}

	public void setNextTaskId(Long nextTaskId) {
		this.nextTaskId = nextTaskId;
	}

}
