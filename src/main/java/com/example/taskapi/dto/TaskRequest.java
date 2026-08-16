package com.example.taskapi.dto;

public record TaskRequest(
		String title,
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
