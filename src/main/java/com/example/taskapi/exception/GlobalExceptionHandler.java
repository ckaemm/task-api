package com.example.taskapi.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(TaskNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleTaskNotFound(TaskNotFoundException ex) {
		Map<String, String> body = Map.of(
				"error", "Task not found",
				"message", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationFailure(MethodArgumentNotValidException ex) {
		// Ayni alan birden fazla kurali birden cignenebilir, bu yuzden alan basina liste tutuyoruz.
		Map<String, List<String>> fieldErrors = new TreeMap<>();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			fieldErrors.computeIfAbsent(fieldError.getField(), field -> new ArrayList<>())
					.add(fieldError.getDefaultMessage());
		}
		// Validator ihlalleri belirsiz sirada dondurur; yanit stabil kalsin diye siraliyoruz.
		fieldErrors.values().forEach(Collections::sort);

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("error", "Validation failed");
		body.put("fieldErrors", fieldErrors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

}
