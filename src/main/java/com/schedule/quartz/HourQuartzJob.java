package com.schedule.quartz;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 调度任务
 * @author 887961 
 * @Date 2016年10月27日 下午6:30:15
 */
public class HourQuartzJob {
	
	private final Log log = LogFactory.getLog(HourQuartzJob.class);
	/* job */
	public void work() {
		log.info("job begin");
		
		log.info("job end");
	}
	
}
