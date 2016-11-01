package com.schedule.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.schedule.domain.Task;
import com.schedule.service.TaskService;

@Service(value="taskService")
public class TaskServiceImpl implements TaskService {

	@Override
	public List<Task> findTask() {
		List<Task> list = new ArrayList<Task>();
		Task task1 = new Task();
		task1.setId(1l);
		task1.setCronExpression("0/5 * * * * ?");
		task1.setTaskName("第一个任务");
		list.add(task1);
		Task task2 = new Task();
		task2.setId(2l);
		task2.setCronExpression("0/5 * * * * ?");
		task2.setTaskName("第二个任务");
		list.add(task2);
		Task task3 = new Task();
		task3.setId(3l);
		task3.setCronExpression("0/5 * * * * ?");
		task3.setTaskName("第三个任务");
		list.add(task3);
		return list;
	}

}
