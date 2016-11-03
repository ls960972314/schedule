package com.schedule.componentPlugin;

import java.util.LinkedList;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.schedule.common.Constants;
import com.schedule.domain.Task;
import com.schedule.domain.vo.GroupTask;
import com.schedule.service.TaskService;
import com.schedule.task.GroupTaskJob;

/**
 * 容器启动时加载initTask函数初始化任务
 * @author 887961 
 * @Date 2016年11月2日 下午3:11:37
 */
public class LoadTask {
	
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Autowired
	private TaskService taskService;

	/**
	 * 容器初始化函数
	 * @throws Exception
	 */
	public void initTask() throws Exception {
		groupTaskJobExcute();
	}

	/**
	 * 根据组划分的job 初始化
	 * @throws SchedulerException
	 */
	private void groupTaskJobExcute() throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		// 可执行的任务列表
		List<GroupTask> groupTaskList = taskService.findTask();
		for (GroupTask groupTask : groupTaskList) {
			if (groupTask.getGroupName().equals(Constants.conCurrentGroupName)) {
				LinkedList<Task> list = groupTask.getTaskList();
				for (Task task : list) {
					JobDetail jobDetail = JobBuilder.newJob(GroupTaskJob.class)
							.withIdentity(Constants.taskPre + task.getId(), task.getGroupName()).build();
					jobDetail.getJobDataMap().put(Constants.taskList, list);
					jobDetail.getJobDataMap().put(Constants.groupName, Constants.conCurrentGroupName);
					jobDetail.getJobDataMap().put(Constants.excuteTaskIndex, 0);

					CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
					CronTrigger trigger = TriggerBuilder.newTrigger()
							.withIdentity(Constants.taskPre + task.getId(), task.getGroupName()).withSchedule(scheduleBuilder)
							.build();

					scheduler.scheduleJob(jobDetail, trigger);
				}
			} else {
				LinkedList<Task> list = groupTask.getTaskList();
				Task removeTask = list.removeFirst();
				
				JobDetail jobDetail = JobBuilder.newJob(GroupTaskJob.class)
						.withIdentity(Constants.taskPre + removeTask.getId(), removeTask.getGroupName()).build();
				jobDetail.getJobDataMap().put(Constants.taskList, list);
				jobDetail.getJobDataMap().put(Constants.groupName, removeTask.getGroupName());
				jobDetail.getJobDataMap().put(Constants.excuteTaskIndex, 0);

				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(removeTask.getCronExpression());
				CronTrigger trigger = TriggerBuilder.newTrigger()
						.withIdentity(Constants.taskPre + removeTask.getId(), removeTask.getGroupName())
						.withSchedule(scheduleBuilder).build();
				scheduler.scheduleJob(jobDetail, trigger);
			}
		}
	}
	
}
