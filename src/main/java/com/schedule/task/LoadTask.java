package com.schedule.task;

import java.util.LinkedList;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.schedule.domain.GroupTask;
import com.schedule.domain.Task;
import com.schedule.service.TaskService;

public class LoadTask {
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	@Autowired
	private TaskService taskService;

	public void initTask() throws Exception {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		// 可执行的任务列表
		List<GroupTask> groupTaskList = taskService.findTask();
		for (GroupTask groupTask : groupTaskList) {
			if (groupTask.getGroupName().equals("defaultGroup")) {
				LinkedList<Task> list = groupTask.getTaskList();
				for (Task task : list) {
					JobDetail jobDetail = JobBuilder.newJob(GroupTaskJob.class)
							.withIdentity("task_" + task.getId(), task.getGroupName()).build();
					jobDetail.getJobDataMap().put("taskList", list);
					jobDetail.getJobDataMap().put("groupName", "defaultGroup");
					jobDetail.getJobDataMap().put("excuteTaskIndex", 0);
					
					CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
					CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("task_" + task.getId(), task.getGroupName())
							.withSchedule(scheduleBuilder).build();
					
					scheduler.scheduleJob(jobDetail, trigger);
				}
			} else {
				LinkedList<Task> list = groupTask.getTaskList();
				Task removeTask = list.removeFirst();
				JobDetail jobDetail = JobBuilder.newJob(GroupTaskJob.class)
						.withIdentity("task_" + removeTask.getId(), removeTask.getGroupName()).build();
				jobDetail.getJobDataMap().put("taskList", list);
				jobDetail.getJobDataMap().put("groupName", removeTask.getGroupName());
				jobDetail.getJobDataMap().put("excuteTaskIndex", 0);
				
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(removeTask.getCronExpression());
				CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("task_" + removeTask.getId(), removeTask.getGroupName())
						.withSchedule(scheduleBuilder).build();
				scheduler.scheduleJob(jobDetail, trigger);
			}
		}
	}
}
