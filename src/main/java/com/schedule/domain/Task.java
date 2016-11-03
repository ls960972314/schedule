package com.schedule.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务
 * @author 887961 
 * @Date 2016年11月2日 下午3:19:30
 */
public class Task implements Serializable{

	private static final long serialVersionUID = 8066779905898235424L;

	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 任务名
	 */
	private String taskName;
	/**
	 * 组名
	 */
	private String groupName;
	/**
	 * 任务cron表达式
	 */
	private String cronExpression;
	/**
	 * 任务顺序
	 */
	private Integer priority;
	/**
	 * 任务类型
	 * 1 执行SQL
	 */
	private Integer taskType;
	/**
	 * 任务执行的数据
	 */
	private String data;
	/**
	 * 任务状态
	 * Y 可用 N 不可用
	 */
	private String status;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	
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


}
