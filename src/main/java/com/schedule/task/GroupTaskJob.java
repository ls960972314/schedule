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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.schedule.common.Constants;
import com.schedule.common.util.ContextHelper;
import com.schedule.domain.Task;
import com.schedule.interfaces.TaskExcute;

/**
 * 根据组划分的job
 * 在defaultGroup组中的任务都并发执行
 * 在其他组中的任务按照顺序执行(触发时间为第一个任务的cron表达式时间)
 * @author 887961 
 * @Date 2016年11月2日 下午3:05:04
 */
@PersistJobDataAfterExecution // 将jobDataMap中的数据存储起来,多任务时可以传递参数
@DisallowConcurrentExecution  // 不允许任务并发执行
public class GroupTaskJob implements Job {
	
	private static Logger log = LoggerFactory.getLogger(GroupTaskJob.class);
	
	private TaskExcute sqlTaskExcute;
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			sqlTaskExcute = ContextHelper.getInstance().getBean("sqlTaskExcute");
			
			log.info(String.format("job:%s开始执行", context.getJobDetail().getKey().toString()));
			// 在这里执行你的任务...
			sqlTaskExcute.excuteSql();
			log.info(String.format("job:%s执行完毕", context.getJobDetail().getKey().toString()));
			// 判断组中是否有要继续执行的任务
			if (!context.getJobDetail().getJobDataMap().get(Constants.groupName).toString().equals(Constants.conCurrentGroupName)) {
				int excuteTaskIndex = (int) context.getJobDetail().getJobDataMap().get(Constants.excuteTaskIndex);
				LinkedList<Task> list = (LinkedList<Task>) context.getJobDetail().getJobDataMap().get(Constants.taskList);
				Scheduler scheduler = ContextHelper.getInstance().getBean("schedulerFactoryBean");
				
				if (excuteTaskIndex < list.size()) {
					Task removeTask = list.get(excuteTaskIndex);
					excuteTaskIndex++;
					
					JobDetail jobDetail = scheduler.getJobDetail(new JobKey(Constants.taskPre + removeTask.getId(), removeTask.getGroupName()));
					if (jobDetail == null) {
						jobDetail = JobBuilder.newJob(GroupTaskJob.class).withIdentity(Constants.taskPre + removeTask.getId(), removeTask.getGroupName()).storeDurably().build();
					}
					jobDetail.getJobDataMap().put(Constants.excuteTaskIndex, excuteTaskIndex);
					jobDetail.getJobDataMap().put(Constants.groupName, removeTask.getGroupName());
					jobDetail.getJobDataMap().put(Constants.taskList, list);
					scheduler.addJob(jobDetail, true);
				    scheduler.triggerJob(jobKey(Constants.taskPre + removeTask.getId(), removeTask.getGroupName()));
				}
			}
		} catch (Exception e) {
			// 如果出异常,接触所有trigger与该job的联系,使该job不再执行
			log.info("GroupTaskJob Exception");
            JobExecutionException e2 = new JobExecutionException(e);
            e2.setUnscheduleAllTriggers(true);
            throw e2;
		}
	}
}
