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

package org.quartz.examples.example1;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * This Example will demonstrate how to start and shutdown the Quartz scheduler
 * and how to schedule a job to run in Quartz. 如何start和shutdown一个quartz调度
 * 如何执行一个job
 * 
 * @author Bill Kratzer
 */
public class SimpleExample {

	/**
	 * 实验日志:
	 * 2016-10-28 16:56:01,851 [main] [ - ]  INFO  org.quartz.examples.example1.SimpleExample - ------- Initializing ----------------------
2016-10-28 16:56:01,918 [main] [ - ]  INFO  org.quartz.impl.StdSchedulerFactory - Using default implementation for ThreadExecutor
2016-10-28 16:56:01,923 [main] [ - ]  INFO  org.quartz.simpl.SimpleThreadPool - Job execution threads will use class loader of thread: main
2016-10-28 16:56:01,949 [main] [ - ]  INFO  org.quartz.core.SchedulerSignalerImpl - Initialized Scheduler Signaller of type: class org.quartz.core.SchedulerSignalerImpl
2016-10-28 16:56:01,952 [main] [ - ]  INFO  org.quartz.core.QuartzScheduler - Quartz Scheduler v.2.2.1 created.
2016-10-28 16:56:01,953 [main] [ - ]  INFO  org.quartz.simpl.RAMJobStore - RAMJobStore initialized.
2016-10-28 16:56:01,955 [main] [ - ]  INFO  org.quartz.core.QuartzScheduler - Scheduler meta-data: Quartz Scheduler (v2.2.1) 'DefaultQuartzScheduler' with instanceId 'NON_CLUSTERED'
  Scheduler class: 'org.quartz.core.QuartzScheduler' - running locally.
  NOT STARTED.
  Currently in standby mode.
  Number of jobs executed: 0
  Using thread pool 'org.quartz.simpl.SimpleThreadPool' - with 10 threads.
  Using job-store 'org.quartz.simpl.RAMJobStore' - which does not support persistence. and is not clustered.

2016-10-28 16:56:01,955 [main] [ - ]  INFO  org.quartz.impl.StdSchedulerFactory - Quartz scheduler 'DefaultQuartzScheduler' initialized from default resource file in Quartz package: 'quartz.properties'
2016-10-28 16:56:01,955 [main] [ - ]  INFO  org.quartz.impl.StdSchedulerFactory - Quartz scheduler version: 2.2.1
2016-10-28 16:56:01,955 [main] [ - ]  INFO  org.quartz.examples.example1.SimpleExample - ------- Initialization Complete -----------
2016-10-28 16:56:01,957 [main] [ - ]  INFO  org.quartz.examples.example1.SimpleExample - ------- Scheduling Job  -------------------
2016-10-28 16:56:01,975 [main] [ - ]  INFO  org.quartz.examples.example1.SimpleExample - group1.job1 will run at: Fri Oct 28 16:57:00 CST 2016
2016-10-28 16:56:01,975 [main] [ - ]  INFO  org.quartz.core.QuartzScheduler - Scheduler DefaultQuartzScheduler_$_NON_CLUSTERED started.
2016-10-28 16:56:01,975 [main] [ - ]  INFO  org.quartz.examples.example1.SimpleExample - ------- Started Scheduler -----------------
2016-10-28 16:56:01,976 [main] [ - ]  INFO  org.quartz.examples.example1.SimpleExample - ------- Waiting 65 seconds... -------------
2016-10-28 16:57:00,009 [DefaultQuartzScheduler_Worker-1] [ - ]  INFO  org.quartz.examples.example1.HelloJob - Hello World! - Fri Oct 28 16:57:00 CST 2016
2016-10-28 16:57:06,973 [main] [ - ]  INFO  org.quartz.examples.example1.SimpleExample - ------- Shutting Down ---------------------
2016-10-28 16:57:06,973 [main] [ - ]  INFO  org.quartz.core.QuartzScheduler - Scheduler DefaultQuartzScheduler_$_NON_CLUSTERED shutting down.
2016-10-28 16:57:06,973 [main] [ - ]  INFO  org.quartz.core.QuartzScheduler - Scheduler DefaultQuartzScheduler_$_NON_CLUSTERED paused.
2016-10-28 16:57:07,448 [main] [ - ]  INFO  org.quartz.core.QuartzScheduler - Scheduler DefaultQuartzScheduler_$_NON_CLUSTERED shutdown complete.
2016-10-28 16:57:07,448 [main] [ - ]  INFO  org.quartz.examples.example1.SimpleExample - ------- Shutdown Complete -----------------

	 * @throws Exception
	 */
	public void run() throws Exception {
		Logger log = LoggerFactory.getLogger(SimpleExample.class);

		log.info("------- Initializing ----------------------");

		// First we must get a reference to a scheduler
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		log.info("------- Initialization Complete -----------");

		// computer a time that is on the next round minute
		Date runTime = evenMinuteDate(new Date());

		log.info("------- Scheduling Job  -------------------");

		// define the job and tie it to our HelloJob class
		JobDetail job = newJob(HelloJob.class).withIdentity("job1", "group1").build();

		// Trigger the job to run on the next round minute
		Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startAt(runTime).build();

		// Tell quartz to schedule the job using our trigger
		sched.scheduleJob(job, trigger);
		log.info(job.getKey() + " will run at: " + runTime);

		// Start up the scheduler (nothing can actually run until the
		// scheduler has been started)
		sched.start();

		log.info("------- Started Scheduler -----------------");

		// wait long enough so that the scheduler as an opportunity to
		// run the job!
		log.info("------- Waiting 65 seconds... -------------");
		try {
			// wait 65 seconds to show job
			Thread.sleep(65L * 1000L);
			// executing...
		} catch (Exception e) {
			//
		}

		// shut down the scheduler
		log.info("------- Shutting Down ---------------------");
		sched.shutdown(true);
		log.info("------- Shutdown Complete -----------------");
	}

	public static void main(String[] args) throws Exception {

		SimpleExample example = new SimpleExample();
		example.run();

	}

}
