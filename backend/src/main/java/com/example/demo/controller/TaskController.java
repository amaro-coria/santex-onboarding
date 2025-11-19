package com.example.demo.controller;

import com.example.demo.dto.TaskRequest;
import com.example.demo.dto.TaskResponse;
import com.example.demo.model.TaskStatus;
import com.example.demo.service.TaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Task management.
 * Handles HTTP requests for task CRUD operations.
 */
@RestController
@RequestMapping("/api/tasks")
@Validated
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Create a new task.
     * POST /api/tasks
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        logger.info("REST request to create task with title: {}", request.getTitle());
        TaskResponse response = taskService.createTask(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get a task by ID.
     * GET /api/tasks/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        logger.debug("REST request to get task by ID: {}", id);
        TaskResponse response = taskService.getTaskById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all tasks, optionally filtered by user ID and/or status.
     * GET /api/tasks
     * GET /api/tasks?userId=1
     * GET /api/tasks?status=TODO
     * GET /api/tasks?userId=1&status=TODO
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) TaskStatus status) {

        logger.debug("REST request to get tasks with filters - userId: {}, status: {}", userId, status);

        List<TaskResponse> tasks;

        if (userId != null && status != null) {
            tasks = taskService.getTasksByUserIdAndStatus(userId, status);
        } else if (userId != null) {
            tasks = taskService.getTasksByUserId(userId);
        } else if (status != null) {
            tasks = taskService.getTasksByStatus(status);
        } else {
            tasks = taskService.getAllTasks();
        }

        return ResponseEntity.ok(tasks);
    }

    /**
     * Get overdue tasks.
     * GET /api/tasks/overdue
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<TaskResponse>> getOverdueTasks() {
        logger.debug("REST request to get overdue tasks");
        List<TaskResponse> tasks = taskService.getOverdueTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * Update an existing task.
     * PUT /api/tasks/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {
        logger.info("REST request to update task with ID: {}", id);
        TaskResponse response = taskService.updateTask(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Update only the status of a task.
     * PATCH /api/tasks/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable Long id,
            @RequestParam TaskStatus status) {
        logger.info("REST request to update task status for ID: {} to {}", id, status);
        TaskResponse response = taskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a task.
     * DELETE /api/tasks/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        logger.info("REST request to delete task with ID: {}", id);
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
