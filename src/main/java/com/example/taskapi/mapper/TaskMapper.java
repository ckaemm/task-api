package com.example.taskapi.mapper;

import com.example.taskapi.dto.TaskRequest;
import com.example.taskapi.dto.TaskResponse;
import com.example.taskapi.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

	public TaskResponse toResponse(Task task) {
		return new TaskResponse(
				task.getId(),
				task.getTitle(),
				task.getDescription(),
				task.isCompleted(),
				task.getCreatedAt());
	}

	public Task toEntity(TaskRequest request) {
		Task task = new Task(request.title(), request.description());
		task.setCompleted(request.completedOrDefault());
		return task;
	}

}
