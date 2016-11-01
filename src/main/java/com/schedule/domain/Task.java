package com.schedule.domain;

import java.io.Serializable;

public class Task implements Serializable{

	private static final long serialVersionUID = 8066779905898235424L;

	private Long id;
	
	private String taskName;
	
	private String cronExpression;

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

}
