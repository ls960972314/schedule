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
 
package org.quartz.examples.example8;

import static org.quartz.DateBuilder.dateOf;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.examples.example2.SimpleJob;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.AnnualCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 展示如何使用calendar在执行调度时排除一个时间段
 */
public class CalendarExample {

  public void run() throws Exception {
    final Logger log = LoggerFactory.getLogger(CalendarExample.class);

    log.info("------- Initializing ----------------------");

    // First we must get a reference to a scheduler
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    log.info("------- Initialization Complete -----------");

    log.info("------- Scheduling Jobs -------------------");

    // 向调度中增加排除的假期时间
    AnnualCalendar holidays = new AnnualCalendar();

    // fourth of July (July 4)
    Calendar fourthOfJuly = new GregorianCalendar(2005, 6, 4);
    holidays.setDayExcluded(fourthOfJuly, true);
    // halloween (Oct 31)
    Calendar halloween = new GregorianCalendar(2005, 9, 31);
    holidays.setDayExcluded(halloween, true);
    // christmas (Dec 25)
    Calendar christmas = new GregorianCalendar(2005, 11, 25);
    holidays.setDayExcluded(christmas, true);

    // calendar和schedule关联
    sched.addCalendar("holidays", holidays, false, false);

    // 在万圣节开始执行调度,每小时跑一次
    Date runDate = dateOf(0, 0, 10, 31, 10);
    JobDetail job = newJob(SimpleJob.class).withIdentity("job1", "group1").build();
    SimpleTrigger trigger = newTrigger().withIdentity("trigger1", "group1").startAt(runDate)
        .withSchedule(simpleSchedule().withIntervalInHours(1).repeatForever()).modifiedByCalendar("holidays").build();
    Date firstRunTime = sched.scheduleJob(job, trigger);

    // 打印出来的将要在圣诞节第二天执行(圣诞节被排除了)
    log.info(job.getKey() + " will run at: " + firstRunTime + " and repeat: " + trigger.getRepeatCount()
             + " times, every " + trigger.getRepeatInterval() / 1000 + " seconds");

    log.info("------- Starting Scheduler ----------------");
    sched.start();
    log.info("------- Waiting 30 seconds... --------------");
    try {
      // wait 30 seconds to show jobs
      Thread.sleep(30L * 1000L);
      // executing...
    } catch (Exception e) {
      //
    }

    // shut down the scheduler
    log.info("------- Shutting Down ---------------------");
    sched.shutdown(true);
    log.info("------- Shutdown Complete -----------------");

    SchedulerMetaData metaData = sched.getMetaData();
    log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");

  }

  public static void main(String[] args) throws Exception {

    CalendarExample example = new CalendarExample();
    example.run();
  }

}
