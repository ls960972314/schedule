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
 
package org.quartz.examples.example2;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 使用Simple Triggers展示所有的scheduler的基本功能
 */
public class SimpleTriggerExample {

  public void run() throws Exception {
    Logger log = LoggerFactory.getLogger(SimpleTriggerExample.class);

    log.info("------- Initializing -------------------");

    // First we must get a reference to a scheduler
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    log.info("------- Initialization Complete --------");

    log.info("------- Scheduling Jobs ----------------");

    //job1 跑一次
    Date startTime = DateBuilder.nextGivenSecondDate(null, 15);
    JobDetail job = newJob(SimpleJob.class).withIdentity("job1", "group1").build();
    SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity("trigger1", "group1").startAt(startTime).build();
    Date ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
             + trigger.getRepeatInterval() / 1000 + " seconds");

    // job2 跑一次
    job = newJob(SimpleJob.class).withIdentity("job2", "group1").build();
    trigger = (SimpleTrigger) newTrigger().withIdentity("trigger2", "group1").startAt(startTime).build();
    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
             + trigger.getRepeatInterval() / 1000 + " seconds");

    // job3 跑11次,第一次跑+重复10次,每10秒重复一次
    job = newJob(SimpleJob.class).withIdentity("job3", "group1").build();
    trigger = newTrigger().withIdentity("trigger3", "group1").startAt(startTime)
        .withSchedule(simpleSchedule().withIntervalInSeconds(10).withRepeatCount(10)).build();
    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
             + trigger.getRepeatInterval() / 1000 + " seconds");

    // 同样的job3会执行 3次,第一次跑+重复2次,每10秒重复一次
    trigger = newTrigger().withIdentity("trigger3", "group2").startAt(startTime)
        .withSchedule(simpleSchedule().withIntervalInSeconds(10).withRepeatCount(2)).forJob(job).build();
    ft = sched.scheduleJob(trigger);
    log.info(job.getKey() + " will [also] run at: " + ft + " and repeat: " + trigger.getRepeatCount()
             + " times, every " + trigger.getRepeatInterval() / 1000 + " seconds");
    
    // job4 跑11次,第一次跑+重复5次,每10秒重复一次
    job = newJob(SimpleJob.class).withIdentity("job4", "group1").build();
    trigger = newTrigger().withIdentity("trigger4", "group1").startAt(startTime)
        .withSchedule(simpleSchedule().withIntervalInSeconds(10).withRepeatCount(5)).build();
    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
             + trigger.getRepeatInterval() / 1000 + " seconds");

    // job5 跑一次,5分钟之后
    job = newJob(SimpleJob.class).withIdentity("job5", "group1").build();
    trigger = (SimpleTrigger) newTrigger().withIdentity("trigger5", "group1")
        .startAt(futureDate(5, IntervalUnit.MINUTE)).build();
    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
             + trigger.getRepeatInterval() / 1000 + " seconds");
    
    // job6 将永久的跑下去,每40秒一次
    job = newJob(SimpleJob.class).withIdentity("job6", "group1").build();
    trigger = newTrigger().withIdentity("trigger6", "group1").startAt(startTime)
        .withSchedule(simpleSchedule().withIntervalInSeconds(40).repeatForever()).build();
    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
             + trigger.getRepeatInterval() / 1000 + " seconds");
    log.info("------- Starting Scheduler ----------------");
    
    // 所有的job都加入了scheduler,直到scheduler执行start方法才会被执行
    sched.start();
    log.info("------- Started Scheduler -----------------");
    
    // job 也可以在scheduled执行start方法后加入调度任务
    
    // job7 跑21次,第一次跑+重复20次,每5分钟跑一次
    job = newJob(SimpleJob.class).withIdentity("job7", "group1").build();
    trigger = newTrigger().withIdentity("trigger7", "group1").startAt(startTime)
        .withSchedule(simpleSchedule().withIntervalInMinutes(5).withRepeatCount(20)).build();
    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
             + trigger.getRepeatInterval() / 1000 + " seconds");
    
    // job也可以直接执行(而不是等待一个触发器)
    job = newJob(SimpleJob.class).withIdentity("job8", "group1").storeDurably().build();
    sched.addJob(job, true);
    log.info("'Manually' triggering job8...");
    sched.triggerJob(jobKey("job8", "group1"));
    
    
    log.info("------- Waiting 30 seconds... --------------");
    try {
        // wait 33 seconds to show jobs
        Thread.sleep(30L * 1000L);
        // executing...
      } catch (Exception e) {
        //
      }
    // job也可以重新设置
    // job7 将马上执行一次,然后重复10次,每秒执行1次
    log.info("------- Rescheduling... --------------------");
    trigger = newTrigger().withIdentity("trigger7", "group1").startAt(startTime)
        .withSchedule(simpleSchedule().withIntervalInSeconds(1).withRepeatCount(10)).build();

    ft = sched.rescheduleJob(trigger.getKey(), trigger);
    log.info("job7 rescheduled to run at: " + ft);
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
    
    // 展示一些有关刚刚执行的调度的统计值
    SchedulerMetaData metaData = sched.getMetaData();
    log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
	
  }

  public static void main(String[] args) throws Exception {

    SimpleTriggerExample example = new SimpleTriggerExample();
    example.run();

  }

}
