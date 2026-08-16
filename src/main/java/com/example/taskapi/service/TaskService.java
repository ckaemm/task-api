package com.example.taskapi.service;

import com.example.taskapi.model.Task;
import com.example.taskapi.repository.TaskRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

	private final TaskRepository taskRepository;

	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Transactional(readOnly = true)
	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Task getTaskById(Long id) {
		return findTaskOrThrow(id);
	}

	@Transactional
	public Task createTask(String title, String description) {
		return taskRepository.save(new Task(title, description));
	}

	@Transactional
	public Task updateTask(Long id, String title, String description, boolean completed) {
		Task task = findTaskOrThrow(id);
		task.setTitle(title);
		task.setDescription(description);
		task.setCompleted(completed);
		return taskRepository.save(task);
	}

	@Transactional
	public void deleteTask(Long id) {
		Task task = findTaskOrThrow(id);
		taskRepository.delete(task);
	}

	// Kayit bulunamadiginda tek karar noktasi.
	// TaskNotFoundException eklenince sadece bu satir degisecek.
	private Task findTaskOrThrow(Long id) {
		return taskRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Task bulunamadi: id=" + id));
	}

}
