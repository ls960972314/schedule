/* 
 * All content copyright Terracotta, Inc., unless otherwise indicated. All rights reserved. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */
 
package org.quartz.examples.example3;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 使用Cron Triggers展示所有的scheduler的基本功能
 */
public class CronTriggerExample {

  public void run() throws Exception {
    Logger log = LoggerFactory.getLogger(CronTriggerExample.class);

    log.info("------- Initializing -------------------");

    // First we must get a reference to a scheduler
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();
    log.info("------- Initialization Complete --------");
    log.info("------- Scheduling Jobs ----------------");

    // job 可以在sched.start()调用前被设定好
    
    // job1 将每20秒执行一次
    JobDetail job = newJob(SimpleJob.class).withIdentity("job1", "group1").build();
    CronTrigger trigger = newTrigger().withIdentity("trigger1", "group1").
    		withSchedule(cronSchedule("0/20 * * * * ?")).build();
    Date ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
             + trigger.getCronExpression());

    // job2 每隔2分钟执行一次,在每分钟的第15秒开始执行
    job = newJob(SimpleJob.class).withIdentity("job2", "group1").build();
    trigger = newTrigger().withIdentity("trigger2", "group1").
    		withSchedule(cronSchedule("15 0/2 * * * ?")).build();
    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
             + trigger.getCronExpression());

    // job3 每隔2分钟执行一次,从上午8点到下午5点
    job = newJob(SimpleJob.class).withIdentity("job3", "group1").build();
    trigger = newTrigger().withIdentity("trigger3", "group1").withSchedule(cronSchedule("0 0/2 8-17 * * ?")).build();
    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
             + trigger.getCronExpression());

    // job4 每隔3分钟执行一次,从下午5点到下午11点
    job = newJob(SimpleJob.class).withIdentity("job4", "group1").build();
    trigger = newTrigger().withIdentity("trigger4", "group1").withSchedule(cronSchedule("0 0/3 17-23 * * ?")).build();
    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
             + trigger.getCronExpression());

    // job5 在每个月1号到15号的上午10点执行
    job = newJob(SimpleJob.class).withIdentity("job5", "group1").build();
    trigger = newTrigger().withIdentity("trigger5", "group1").withSchedule(cronSchedule("0 0 10am 1,15 * ?")).build();
    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
             + trigger.getCronExpression());

    // job6 在每个周一到周五每隔30秒执行一次
    job = newJob(SimpleJob.class).withIdentity("job6", "group1").build();
    trigger = newTrigger().withIdentity("trigger6", "group1").withSchedule(cronSchedule("0,30 * * ? * MON-FRI"))
        .build();
    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
             + trigger.getCronExpression());

    // job7 在每个周六周末每隔30秒执行一次
    job = newJob(SimpleJob.class).withIdentity("job7", "group1").build();
    trigger = newTrigger().withIdentity("trigger7", "group1").withSchedule(cronSchedule("0,30 * * ? * SAT,SUN"))
        .build();
    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
             + trigger.getCronExpression());
    
    log.info("------- Starting Scheduler ----------------");
    // 所有的job都加入了scheduler,直到scheduler执行start方法才会被执行
    sched.start();
    log.info("------- Started Scheduler -----------------");
    
    log.info("------- Waiting five minutes... ------------");
    try {
      // wait five minutes to show jobs
      Thread.sleep(300L * 1000L);
      // executing...
    } catch (Exception e) {
      //
    }

    log.info("------- Shutting Down ---------------------");
    sched.shutdown(true);
    log.info("------- Shutdown Complete -----------------");

    SchedulerMetaData metaData = sched.getMetaData();
    log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
  }

  public static void main(String[] args) throws Exception {
    CronTriggerExample example = new CronTriggerExample();
    example.run();
  }

}
