package com.example.demo.controller;

import com.example.demo.dto.TaskRequest;
import com.example.demo.dto.UserRequest;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for TaskController.
 * Uses Testcontainers for real database testing.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TaskControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "false");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    private Long createTestUser() throws Exception {
        UserRequest userRequest = new UserRequest("test@example.com", "password123", "Test", "User");
        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    void shouldCreateTaskSuccessfully() throws Exception {
        Long userId = createTestUser();
        TaskRequest request = new TaskRequest(
                "Test Task",
                "Test Description",
                TaskStatus.TODO,
                userId,
                LocalDate.now().plusDays(5)
        );

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.assignedUser.id").value(userId))
                .andExpect(jsonPath("$.dueDate").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    void shouldCreateTaskWithoutAssignedUser() throws Exception {
        TaskRequest request = new TaskRequest(
                "Unassigned Task",
                "No user assigned",
                TaskStatus.TODO,
                null,
                LocalDate.now().plusDays(5)
        );

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Unassigned Task"))
                .andExpect(jsonPath("$.assignedUser").doesNotExist());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingTaskWithInvalidData() throws Exception {
        TaskRequest request = new TaskRequest(
                "AB", // Too short
                "Description",
                TaskStatus.TODO,
                null,
                null
        );

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }

    @Test
    void shouldGetTaskByIdSuccessfully() throws Exception {
        Long userId = createTestUser();
        TaskRequest request = new TaskRequest("Test Task", "Description", TaskStatus.TODO, userId, null);

        String createResponse = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        Long taskId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void shouldReturnNotFoundWhenGettingNonExistentTask() throws Exception {
        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value(containsString("Task not found")));
    }

    @Test
    void shouldGetAllTasksSuccessfully() throws Exception {
        Long userId = createTestUser();
        TaskRequest task1 = new TaskRequest("Task 1", "Description 1", TaskStatus.TODO, userId, null);
        TaskRequest task2 = new TaskRequest("Task 2", "Description 2", TaskStatus.IN_PROGRESS, userId, null);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task1)));

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task2)));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder("Task 1", "Task 2")));
    }

    @Test
    void shouldFilterTasksByUserId() throws Exception {
        Long user1Id = createTestUser();

        // Create second user
        UserRequest user2Request = new UserRequest("user2@example.com", "password123", "User", "Two");
        String user2Response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2Request)))
                .andReturn().getResponse().getContentAsString();
        Long user2Id = objectMapper.readTree(user2Response).get("id").asLong();

        // Create tasks for different users
        TaskRequest task1 = new TaskRequest("Task 1", "Desc", TaskStatus.TODO, user1Id, null);
        TaskRequest task2 = new TaskRequest("Task 2", "Desc", TaskStatus.TODO, user2Id, null);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task1)));

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task2)));

        // Filter by user1
        mockMvc.perform(get("/api/tasks?userId=" + user1Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Task 1"));
    }

    @Test
    void shouldFilterTasksByStatus() throws Exception {
        Long userId = createTestUser();
        TaskRequest task1 = new TaskRequest("Task 1", "Desc", TaskStatus.TODO, userId, null);
        TaskRequest task2 = new TaskRequest("Task 2", "Desc", TaskStatus.IN_PROGRESS, userId, null);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task1)));

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task2)));

        mockMvc.perform(get("/api/tasks?status=TODO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status").value("TODO"));
    }

    @Test
    void shouldGetOverdueTasks() throws Exception {
        Long userId = createTestUser();
        TaskRequest overdueTask = new TaskRequest(
                "Overdue Task",
                "Description",
                TaskStatus.TODO,
                userId,
                LocalDate.now().minusDays(2)
        );

        TaskRequest futureTask = new TaskRequest(
                "Future Task",
                "Description",
                TaskStatus.TODO,
                userId,
                LocalDate.now().plusDays(5)
        );

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(overdueTask)));

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(futureTask)));

        mockMvc.perform(get("/api/tasks/overdue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Overdue Task"));
    }

    @Test
    void shouldUpdateTaskSuccessfully() throws Exception {
        Long userId = createTestUser();
        TaskRequest createRequest = new TaskRequest("Original Task", "Description", TaskStatus.TODO, userId, null);

        String createResponse = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();

        Long taskId = objectMapper.readTree(createResponse).get("id").asLong();

        TaskRequest updateRequest = new TaskRequest(
                "Updated Task",
                "Updated Description",
                TaskStatus.IN_PROGRESS,
                userId,
                LocalDate.now().plusDays(10)
        );

        mockMvc.perform(put("/api/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void shouldUpdateTaskStatusSuccessfully() throws Exception {
        Long userId = createTestUser();
        TaskRequest request = new TaskRequest("Test Task", "Description", TaskStatus.TODO, userId, null);

        String createResponse = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        Long taskId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(patch("/api/tasks/" + taskId + "/status?status=DONE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"))
                .andExpect(jsonPath("$.title").value("Test Task")); // Title should remain unchanged
    }

    @Test
    void shouldDeleteTaskSuccessfully() throws Exception {
        Long userId = createTestUser();
        TaskRequest request = new TaskRequest("Test Task", "Description", TaskStatus.TODO, userId, null);

        String createResponse = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        Long taskId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(delete("/api/tasks/" + taskId))
                .andExpect(status().isNoContent());

        // Verify task is deleted
        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isNotFound());
    }
}
