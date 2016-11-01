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
 
package org.quartz.examples.example5;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

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
 * 调度(scheduleJob)或恢复调度(resumeTrigger,resumeJob)后不同的misfire对应的处理规则 

CronTrigger 

withMisfireHandlingInstructionDoNothing
——不触发立即执行
——等待下次Cron触发频率到达时刻开始按照Cron频率依次执行

withMisfireHandlingInstructionIgnoreMisfires
——以错过的第一个频率时间立刻开始执行
——重做错过的所有频率周期后
——当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行

withMisfireHandlingInstructionFireAndProceed
——以当前时间为触发频率立刻触发一次执行
——然后按照Cron频率依次执行


SimpleTrigger 

withMisfireHandlingInstructionFireNow
——以当前时间为触发频率立即触发执行
——执行至FinalTIme的剩余周期次数
——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值

withMisfireHandlingInstructionIgnoreMisfires
——以错过的第一个频率时间立刻开始执行
——重做错过的所有频率周期
——当下一次触发频率发生时间大于当前时间以后，按照Interval的依次执行剩下的频率
——共执行RepeatCount+1次

withMisfireHandlingInstructionNextWithExistingCount
——不触发立即执行
——等待下次触发频率周期时刻，执行至FinalTime的剩余周期次数
——以startTime为基准计算周期频率，并得到FinalTime
——即使中间出现pause，resume以后保持FinalTime时间不变


withMisfireHandlingInstructionNowWithExistingCount
——以当前时间为触发频率立即触发执行
——执行至FinalTIme的剩余周期次数
——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值

withMisfireHandlingInstructionNextWithRemainingCount
——不触发立即执行
——等待下次触发频率周期时刻，执行至FinalTime的剩余周期次数
——以startTime为基准计算周期频率，并得到FinalTime
——即使中间出现pause，resume以后保持FinalTime时间不变

withMisfireHandlingInstructionNowWithRemainingCount
——以当前时间为触发频率立即触发执行
——执行至FinalTIme的剩余周期次数
——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值

MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT
——此指令导致trigger忘记原始设置的starttime和repeat-count
——触发器的repeat-count将被设置为剩余的次数
——这样会导致后面无法获得原始设定的starttime和repeat-count值
 */
public class MisfireExample {

  public void run() throws Exception {
    Logger log = LoggerFactory.getLogger(MisfireExample.class);

    log.info("------- Initializing -------------------");

    // First we must get a reference to a scheduler
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    log.info("------- Initialization Complete -----------");
    log.info("------- Scheduling Jobs -----------");
    // jobs can be scheduled before start() has been called
    // get a "nice round" time a few seconds in the future...
    Date startTime = nextGivenSecondDate(null, 15);

    // statefulJob1 will run every three seconds
    // (but it will delay for ten seconds)
    JobDetail job = newJob(StatefulDumbJob.class).withIdentity("statefulJob1", "group1")
        .usingJobData(StatefulDumbJob.EXECUTION_DELAY, 10000L).build();
    SimpleTrigger trigger = newTrigger().withIdentity("trigger1", "group1").startAt(startTime)
        .withSchedule(simpleSchedule().withIntervalInSeconds(3).repeatForever()).build();
    Date ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
             + trigger.getRepeatInterval() / 1000 + " seconds");

    // statefulJob2 will run every three seconds
    // (but it will delay for ten seconds - and therefore purposely misfire after a few iterations)
    job = newJob(StatefulDumbJob.class).withIdentity("statefulJob2", "group1")
        .usingJobData(StatefulDumbJob.EXECUTION_DELAY, 10000L).build();
    trigger = newTrigger().withIdentity("trigger2", "group1").startAt(startTime)
        .withSchedule(simpleSchedule().withIntervalInSeconds(3).repeatForever()
                          .withMisfireHandlingInstructionNowWithExistingCount()) // set misfire instructions
        .build();
    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
             + trigger.getRepeatInterval() / 1000 + " seconds");

    log.info("------- Starting Scheduler ----------------");
    // jobs don't start firing until start() has been called...
    sched.start();
    log.info("------- Started Scheduler -----------------");

    try {
      // sleep for ten minutes for triggers to file....
      Thread.sleep(600L * 1000L);
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

    MisfireExample example = new MisfireExample();
    example.run();
  }

}
