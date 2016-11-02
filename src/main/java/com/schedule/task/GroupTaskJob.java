package com.schedule.task;

import static org.quartz.JobKey.jobKey;

import java.util.LinkedList;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.Scheduler;

import com.schedule.common.util.ContextHelper;
import com.schedule.domain.Task;

@PersistJobDataAfterExecution // 将jobDataMap中的数据存储起来,多任务时可以传递参数
@DisallowConcurrentExecution  // 不允许任务并发执行
public class GroupTaskJob implements Job {
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			System.out.println("job:" + context.getJobDetail().getKey().toString() + "开始执行...");
			
			System.out.println("job:" + context.getJobDetail().getKey().toString() + "运行完毕执行...");
			// 在这里执行你的任务...
			
			// 判断组中是否有要继续执行的任务
			if (!context.getJobDetail().getJobDataMap().get("groupName").toString().equals("defaultGroup")) {
				int excuteTaskIndex = (int) context.getJobDetail().getJobDataMap().get("excuteTaskIndex");
				LinkedList<Task> list = (LinkedList<Task>) context.getJobDetail().getJobDataMap().get("taskList");
				Scheduler scheduler = ContextHelper.getInstance().getBean("schedulerFactoryBean");
				
				if (excuteTaskIndex < list.size()) {
					Task removeTask = list.get(excuteTaskIndex);
					excuteTaskIndex++;
					
					JobDetail jobDetail = scheduler.getJobDetail(new JobKey("task_" + removeTask.getId(), removeTask.getGroupName()));
					if (jobDetail == null) {
						jobDetail = JobBuilder.newJob(GroupTaskJob.class).withIdentity("task_" + removeTask.getId(), removeTask.getGroupName()).storeDurably().build();
					}
					jobDetail.getJobDataMap().put("excuteTaskIndex", excuteTaskIndex);
					jobDetail.getJobDataMap().put("groupName", removeTask.getGroupName());
					jobDetail.getJobDataMap().put("taskList", list);
					scheduler.addJob(jobDetail, true);
				    scheduler.triggerJob(jobKey("task_" + removeTask.getId(), removeTask.getGroupName()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
