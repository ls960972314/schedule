package com.schedule.task;

import java.util.Collection;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

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
		Collection<Task> taskList = taskService.findTask();
		for (Task task : taskList) {
			// 任务名称和任务组设置规则：
			// 名称：task_1 ..
			// 组 ：group_1 ..
			TriggerKey triggerKey = TriggerKey.triggerKey("task_" + task.getId(), "group_" + task.getId());
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			// 不存在,创建一个
			if (null == trigger) {
				JobDetail jobDetail = JobBuilder.newJob(QuartzTaskFactory.class)
						.withIdentity("task_" + task.getId(), "group_" + task.getId()).build();
				jobDetail.getJobDataMap().put("scheduleJob", task);
				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
				// 按新的表达式构建一个新的trigger
				trigger = TriggerBuilder.newTrigger().withIdentity("task_" + task.getId(), "group_" + task.getId())
						.withSchedule(scheduleBuilder).build();
				scheduler.scheduleJob(jobDetail, trigger);
			} else {
				// trigger已存在,则更新相应的定时设置
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
				// 按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
			}
		}
	}
}
