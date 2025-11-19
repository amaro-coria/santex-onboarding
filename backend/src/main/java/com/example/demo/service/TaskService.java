package com.example.demo.service;

import com.example.demo.dto.TaskRequest;
import com.example.demo.dto.TaskResponse;
import com.example.demo.model.TaskStatus;

import java.util.List;

/**
 * Service interface for Task entity operations.
 * Defines business logic methods for task management.
 */
public interface TaskService {

    /**
     * Create a new task.
     *
     * @param request the task creation request
     * @return the created task response
     * @throws com.example.demo.exception.ResourceNotFoundException if assigned user not found
     */
    TaskResponse createTask(TaskRequest request);

    /**
     * Get a task by its ID.
     *
     * @param id the task ID
     * @return the task response
     * @throws com.example.demo.exception.ResourceNotFoundException if task not found
     */
    TaskResponse getTaskById(Long id);

    /**
     * Get all tasks in the system.
     *
     * @return list of all tasks
     */
    List<TaskResponse> getAllTasks();

    /**
     * Get all tasks assigned to a specific user.
     *
     * @param userId the user ID
     * @return list of tasks assigned to the user
     * @throws com.example.demo.exception.ResourceNotFoundException if user not found
     */
    List<TaskResponse> getTasksByUserId(Long userId);

    /**
     * Get all tasks with a specific status.
     *
     * @param status the task status
     * @return list of tasks with the given status
     */
    List<TaskResponse> getTasksByStatus(TaskStatus status);

    /**
     * Get all tasks for a specific user with a specific status.
     *
     * @param userId the user ID
     * @param status the task status
     * @return list of matching tasks
     * @throws com.example.demo.exception.ResourceNotFoundException if user not found
     */
    List<TaskResponse> getTasksByUserIdAndStatus(Long userId, TaskStatus status);

    /**
     * Update an existing task.
     *
     * @param id the task ID
     * @param request the update request
     * @return the updated task response
     * @throws com.example.demo.exception.ResourceNotFoundException if task or assigned user not found
     */
    TaskResponse updateTask(Long id, TaskRequest request);

    /**
     * Update only the status of a task.
     *
     * @param id the task ID
     * @param status the new status
     * @return the updated task response
     * @throws com.example.demo.exception.ResourceNotFoundException if task not found
     */
    TaskResponse updateTaskStatus(Long id, TaskStatus status);

    /**
     * Delete a task by ID.
     *
     * @param id the task ID
     * @throws com.example.demo.exception.ResourceNotFoundException if task not found
     */
    void deleteTask(Long id);

    /**
     * Get all overdue tasks (tasks with due date in the past and not completed).
     *
     * @return list of overdue tasks
     */
    List<TaskResponse> getOverdueTasks();
}
