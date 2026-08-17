package com.example.taskapi.dto;

/**
 * Hata yanitlarinin govdesini OpenAPI semasi olarak temsil eder.
 *
 * <p>Bu kayit runtime'da serilestirilmez; govdeyi hala
 * {@code GlobalExceptionHandler} icindeki Map uretiyor. Amaci yalnizca
 * Swagger dokumantasyonuna dogru alan listesini bildirmek.
 */
public record ErrorResponse(String error, String message) {
}
