package com.schedule.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.schedule.domain.GroupTask;
import com.schedule.domain.Task;
import com.schedule.service.TaskService;

@Service(value="taskService")
public class TaskServiceImpl implements TaskService {

	@Override
	public List<GroupTask> findTask() {
		List<GroupTask> groupList = new ArrayList<>();
		LinkedList<Task> list1 = new LinkedList<Task>();
		Task task1 = new Task();
		task1.setId(1l);
		task1.setGroupName("group1");
		task1.setCronExpression("0/10 * * * * ?");
		task1.setTaskName("第一个任务");
		task1.setPriority(3);
		list1.add(task1);
		Task task2 = new Task();
		task2.setId(2l);
		task2.setGroupName("group1");
		task2.setCronExpression("0/30 * * * * ?");
		task2.setTaskName("第二个任务");
		task2.setPriority(2);
		list1.add(task2);
		Task task3 = new Task();
		task3.setId(3l);
		task3.setGroupName("group1");
		task3.setCronExpression("0/30 * * * * ?");
		task3.setTaskName("第三个任务");
		task3.setPriority(1);
		list1.add(task3);
		GroupTask groupTask = new GroupTask();
		groupTask.setGroupName("group1");
		groupTask.setTaskList(list1);
		groupList.add(groupTask);
		
		LinkedList<Task> list2 = new LinkedList<Task>();
		Task task4 = new Task();
		task4.setId(4l);
		task4.setGroupName("defaultGroup");
		task4.setCronExpression("0/5 * * * * ?");
		task4.setTaskName("第四个任务");
		task4.setPriority(1);
		list2.add(task4);
		Task task5 = new Task();
		task5.setId(5l);
		task5.setGroupName("defaultGroup");
		task5.setCronExpression("0/5 * * * * ?");
		task5.setTaskName("第五个任务");
		task5.setPriority(2);
		list2.add(task5);
		GroupTask groupTask2 = new GroupTask();
		groupTask2.setGroupName("defaultGroup");
		groupTask2.setTaskList(list2);
		groupList.add(groupTask2);
		return groupList;
	}

}
