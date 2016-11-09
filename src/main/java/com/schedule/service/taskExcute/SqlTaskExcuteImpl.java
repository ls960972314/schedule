package com.schedule.service.taskExcute;

import org.springframework.stereotype.Service;

import com.schedule.interfaces.TaskExcute;

/**
 * 执行SQL的任务实现
 * @author 887961 
 * @Date 2016年11月9日 下午6:31:36
 */
@Service(value="sqlTaskExcute")
public class SqlTaskExcuteImpl implements TaskExcute {

	@Override
	public void excuteSql() {
		try {
			// wait 2 seconds to show job
			Thread.sleep(2l * 1000L);
			// executing...
		} catch (Exception e) {
			//
		}
	}

}
