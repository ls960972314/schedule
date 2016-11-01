package com.schedule.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.schedule.domain.Task;

public class QuartzTaskFactory implements Job {
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		try {
			System.out.println("任务运行...");
			Task task = (Task) context.getMergedJobDataMap().get("scheduleJob");
			System.out.println("任务名称: [" + task.getTaskName() + "]");
			// 在这里执行你的任务...
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
