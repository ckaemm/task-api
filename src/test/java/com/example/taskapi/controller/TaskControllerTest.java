package com.example.taskapi.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("POST: bos title 400 dondurur")
	void createRejectsEmptyTitle() throws Exception {
		mockMvc.perform(post("/api/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"title": ""}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Validation failed"))
				.andExpect(jsonPath("$.fieldErrors.title").isArray())
				.andExpect(jsonPath("$.fieldErrors.title", containsInAnyOrder("must not be blank")));
	}

	@Test
	@DisplayName("POST: sadece whitespace iceren title 400 dondurur")
	void createRejectsWhitespaceTitle() throws Exception {
		mockMvc.perform(post("/api/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"title": "   "}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Validation failed"))
				.andExpect(jsonPath("$.fieldErrors.title").isArray())
				.andExpect(jsonPath("$.fieldErrors.title", containsInAnyOrder("must not be blank")));
	}

	@Test
	@DisplayName("POST: 201 karakterlik title 400 dondurur")
	void createRejectsTooLongTitle() throws Exception {
		String body = """
				{"title": "%s"}
				""".formatted("a".repeat(201));

		mockMvc.perform(post("/api/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Validation failed"))
				.andExpect(jsonPath("$.fieldErrors.title").isArray())
				.andExpect(jsonPath("$.fieldErrors.title",
						containsInAnyOrder("size must be between 0 and 200")));
	}

	@Test
	@DisplayName("POST: ayni alan birden fazla kurali cignerse tum mesajlar doner")
	void createReportsAllViolationsForSameField() throws Exception {
		// 201 karakterlik whitespace: hem @NotBlank hem @Size ihlal edilir.
		String body = """
				{"title": "%s"}
				""".formatted(" ".repeat(201));

		mockMvc.perform(post("/api/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Validation failed"))
				.andExpect(jsonPath("$.fieldErrors.title", hasSize(2)))
				.andExpect(jsonPath("$.fieldErrors.title", containsInAnyOrder(
						"must not be blank",
						"size must be between 0 and 200")));
	}

	@Test
	@DisplayName("POST: gecerli title 201 dondurur")
	void createAcceptsValidTitle() throws Exception {
		mockMvc.perform(post("/api/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"title": "Alisveris", "description": "Sut al", "completed": true}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.title").value("Alisveris"))
				.andExpect(jsonPath("$.description").value("Sut al"))
				.andExpect(jsonPath("$.completed").value(true))
				.andExpect(jsonPath("$.createdAt").exists());
	}

	@Test
	@DisplayName("POST: completed gonderilmezse false olur (completedOrDefault)")
	void createDefaultsCompletedToFalseWhenAbsent() throws Exception {
		mockMvc.perform(post("/api/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"title": "Kitap oku"}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value("Kitap oku"))
				.andExpect(jsonPath("$.completed").value(false));
	}

	@Test
	@DisplayName("GET: bulunamayan id icin 404 dondurur")
	void getByIdReturnsNotFoundForMissingTask() throws Exception {
		mockMvc.perform(get("/api/tasks/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value("Task not found"))
				.andExpect(jsonPath("$.message").value("Task not found: id=999"));
	}

	@Test
	@DisplayName("POST: 2001 karakterlik description 400 dondurur")
	void createRejectsTooLongDescription() throws Exception {
		String body = """
				{"title": "Gecerli baslik", "description": "%s"}
				""".formatted("a".repeat(2001));

		mockMvc.perform(post("/api/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Validation failed"))
				.andExpect(jsonPath("$.fieldErrors.description",
						containsInAnyOrder("size must be between 0 and 2000")));
	}

	@Test
	@DisplayName("PUT: bos title 400 dondurur")
	void updateRejectsEmptyTitle() throws Exception {
		long id = createTask("PUT bos title testi");

		mockMvc.perform(put("/api/tasks/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"title": ""}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Validation failed"))
				.andExpect(jsonPath("$.fieldErrors.title").isArray())
				.andExpect(jsonPath("$.fieldErrors.title", containsInAnyOrder("must not be blank")));
	}

	@Test
	@DisplayName("PUT: sadece whitespace iceren title 400 dondurur")
	void updateRejectsWhitespaceTitle() throws Exception {
		long id = createTask("PUT whitespace title testi");

		mockMvc.perform(put("/api/tasks/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"title": "   "}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Validation failed"))
				.andExpect(jsonPath("$.fieldErrors.title", containsInAnyOrder("must not be blank")));
	}

	@Test
	@DisplayName("PUT: 201 karakterlik title 400 dondurur")
	void updateRejectsTooLongTitle() throws Exception {
		long id = createTask("PUT uzun title testi");
		String body = """
				{"title": "%s"}
				""".formatted("a".repeat(201));

		mockMvc.perform(put("/api/tasks/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Validation failed"))
				.andExpect(jsonPath("$.fieldErrors.title",
						containsInAnyOrder("size must be between 0 and 200")));
	}

	@Test
	@DisplayName("PUT: 2001 karakterlik description 400 dondurur")
	void updateRejectsTooLongDescription() throws Exception {
		long id = createTask("PUT uzun description testi");
		String body = """
				{"title": "Gecerli baslik", "description": "%s"}
				""".formatted("a".repeat(2001));

		mockMvc.perform(put("/api/tasks/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Validation failed"))
				.andExpect(jsonPath("$.fieldErrors.description",
						containsInAnyOrder("size must be between 0 and 2000")));
	}

	@Test
	@DisplayName("DELETE: mevcut task silinince 204 dondurur")
	void deleteReturnsNoContent() throws Exception {
		long id = createTask("Silinecek gorev");

		mockMvc.perform(delete("/api/tasks/" + id))
				.andExpect(status().isNoContent())
				.andExpect(jsonPath("$").doesNotExist());

		// Gercekten silindigini dogrula: ayni id artik 404 olmali.
		mockMvc.perform(get("/api/tasks/" + id))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("GET: liste 200 ve olusturulan gorevi iceren bir dizi dondurur")
	void getAllReturnsList() throws Exception {
		// Diger testlerden bagimsiz kalmak icin listeyi kendi benzersiz basligimizla suzuyoruz.
		String uniqueTitle = "Liste testi gorevi " + System.nanoTime();
		createTask(uniqueTitle);

		mockMvc.perform(get("/api/tasks"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[?(@.title == '" + uniqueTitle + "')]", hasSize(1)));
	}

	/**
	 * Test verisi olusturur ve olusan kaydin id'sini dondurur.
	 * Her test kendi kaydiyla calissin diye ortak fixture kullanmiyoruz.
	 */
	private long createTask(String title) throws Exception {
		String response = mockMvc.perform(post("/api/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"title": "%s"}
						""".formatted(title)))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getContentAsString();

		return ((Number) JsonPath.read(response, "$.id")).longValue();
	}

}
