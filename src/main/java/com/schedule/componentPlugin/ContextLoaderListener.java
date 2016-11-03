package com.schedule.componentPlugin;

import javax.servlet.ServletContextEvent;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.schedule.common.util.ContextHelper;

/**
 * 继承ContextLoaderListener的容器监听器
 * @author 887961 
 * @Date 2016年11月2日 下午3:52:27
 */
public class ContextLoaderListener extends org.springframework.web.context.ContextLoaderListener {
	
	private static Logger log = LoggerFactory.getLogger(ContextLoaderListener.class);
	
	public void contextInitialized(ServletContextEvent event) {
		log.info("/************************schedule初始化开始*****************************/");
		super.contextInitialized(event);
		log.info("/************************schedule初始化结束*****************************/");
	}
	
	public void contextDestroyed(ServletContextEvent event) {
		log.info("/************************schedule销毁开始*****************************/");
		super.contextDestroyed(event);
		Scheduler scheduler = ContextHelper.getInstance().getBean("schedulerFactoryBean");
		try {
			scheduler.shutdown(true);
		} catch (SchedulerException e) {
			log.error("contextDestroyed SchedulerException", e);
		}
		log.info("/************************schedule销毁结束*****************************/");
	}

}
