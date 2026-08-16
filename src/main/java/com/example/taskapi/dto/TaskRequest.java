package com.example.taskapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequest(

		@NotBlank
		@Size(max = 200)
		String title,

		@Size(max = 2000)
		String description,

		Boolean completed) {

	/**
	 * Istemci "completed" alanini gondermediginde (CREATE durumu) null gelir.
	 * Varsayilan degeri burada, tek ve gorunur bir yerde uyguluyoruz.
	 */
	public boolean completedOrDefault() {
		return completed != null && completed;
	}

}
