package com.example.taskapi.dto;

import java.util.List;
import java.util.Map;

/**
 * Validation hatasi yanitlarinin govdesini OpenAPI semasi olarak temsil eder.
 *
 * <p>{@code fieldErrors} alan adindan mesaj listesine eslesir; ayni alan
 * birden fazla kurali cignedigi icin deger bir listedir.
 *
 * <p>{@link ErrorResponse} gibi bu kayit da runtime'da serilestirilmez;
 * yalnizca dokumantasyon icindir.
 */
public record ValidationErrorResponse(String error, Map<String, List<String>> fieldErrors) {
}
