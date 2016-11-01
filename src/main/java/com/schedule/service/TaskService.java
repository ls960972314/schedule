package com.schedule.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.schedule.domain.Task;

public interface TaskService {

	public List<Task> findTask();
}
