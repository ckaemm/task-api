package com.example.taskapi.controller;

import com.example.taskapi.dto.TaskRequest;
import com.example.taskapi.dto.TaskResponse;
import com.example.taskapi.mapper.TaskMapper;
import com.example.taskapi.model.Task;
import com.example.taskapi.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Tasks", description = "Gorev kayitlari uzerinde CRUD islemleri")
public class TaskController {

	private final TaskService taskService;

	private final TaskMapper taskMapper;

	public TaskController(TaskService taskService, TaskMapper taskMapper) {
		this.taskService = taskService;
		this.taskMapper = taskMapper;
	}

	@Operation(summary = "Tum gorevleri listeler")
	@ApiResponse(responseCode = "200", description = "Gorev listesi dondu (liste bos olabilir)")
	@GetMapping
	public ResponseEntity<List<TaskResponse>> getAllTasks() {
		List<TaskResponse> body = taskService.getAllTasks()
				.stream()
				.map(taskMapper::toResponse)
				.toList();
		return ResponseEntity.ok(body);
	}

	@Operation(summary = "Verilen id'ye sahip gorevi dondurur")
	@ApiResponse(responseCode = "200", description = "Gorev bulundu")
	@ApiResponse(responseCode = "404", description = "Verilen id ile gorev bulunamadi")
	@GetMapping("/{id}")
	public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
		Task task = taskService.getTaskById(id);
		return ResponseEntity.ok(taskMapper.toResponse(task));
	}

	@Operation(summary = "Yeni bir gorev olusturur")
	@ApiResponse(responseCode = "201", description = "Gorev olusturuldu")
	@ApiResponse(responseCode = "400", description = "Istek govdesi validation kurallarini saglamiyor")
	@PostMapping
	public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
		Task entity = taskMapper.toEntity(request);
		Task created = taskService.createTask(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(taskMapper.toResponse(created));
	}

	@Operation(summary = "Verilen id'ye sahip gorevi gunceller")
	@ApiResponse(responseCode = "200", description = "Gorev guncellendi")
	@ApiResponse(responseCode = "400", description = "Istek govdesi validation kurallarini saglamiyor")
	@ApiResponse(responseCode = "404", description = "Verilen id ile gorev bulunamadi")
	@PutMapping("/{id}")
	public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
		Task updated = taskService.updateTask(
				id,
				request.title(),
				request.description(),
				request.completedOrDefault());
		return ResponseEntity.ok(taskMapper.toResponse(updated));
	}

	@Operation(summary = "Verilen id'ye sahip gorevi siler")
	@ApiResponse(responseCode = "204", description = "Gorev silindi, govde dondurulmez")
	@ApiResponse(responseCode = "404", description = "Verilen id ile gorev bulunamadi")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
		taskService.deleteTask(id);
		return ResponseEntity.noContent().build();
	}

}
