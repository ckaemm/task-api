package com.example.taskapi.controller;

import com.example.taskapi.dto.TaskRequest;
import com.example.taskapi.dto.TaskResponse;
import com.example.taskapi.mapper.TaskMapper;
import com.example.taskapi.model.Task;
import com.example.taskapi.service.TaskService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	private final TaskService taskService;

	private final TaskMapper taskMapper;

	public TaskController(TaskService taskService, TaskMapper taskMapper) {
		this.taskService = taskService;
		this.taskMapper = taskMapper;
	}

	@GetMapping
	public ResponseEntity<List<TaskResponse>> getAllTasks() {
		List<TaskResponse> body = taskService.getAllTasks()
				.stream()
				.map(taskMapper::toResponse)
				.toList();
		return ResponseEntity.ok(body);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
		Task task = taskService.getTaskById(id);
		return ResponseEntity.ok(taskMapper.toResponse(task));
	}

	@PostMapping
	public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
		Task entity = taskMapper.toEntity(request);
		Task created = taskService.createTask(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(taskMapper.toResponse(created));
	}

	@PutMapping("/{id}")
	public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest request) {
		Task updated = taskService.updateTask(
				id,
				request.title(),
				request.description(),
				request.completedOrDefault());
		return ResponseEntity.ok(taskMapper.toResponse(updated));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
		taskService.deleteTask(id);
		return ResponseEntity.noContent().build();
	}

}
