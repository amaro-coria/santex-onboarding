package com.example.demo.service.impl;

import com.example.demo.dto.TaskRequest;
import com.example.demo.dto.TaskResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.model.User;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of TaskService interface.
 * Handles business logic for task management operations.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskResponse createTask(TaskRequest request) {
        logger.info("Creating new task with title: {}", request.getTitle());

        // Convert DTO to entity
        Task task = taskMapper.toEntity(request);

        // Assign user if userId is provided
        if (request.getAssignedUserId() != null) {
            User user = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getAssignedUserId()));
            taskMapper.setAssignedUser(task, user);
            logger.debug("Assigned task to user ID: {}", user.getId());
        }

        // Save to database
        Task savedTask = taskRepository.save(task);
        logger.info("Successfully created task with ID: {}", savedTask.getId());

        return taskMapper.toResponse(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        logger.debug("Fetching task with ID: {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        return taskMapper.toResponse(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        logger.debug("Fetching all tasks");

        List<Task> tasks = taskRepository.findAll();
        logger.debug("Found {} tasks", tasks.size());

        return tasks.stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByUserId(Long userId) {
        logger.debug("Fetching tasks for user ID: {}", userId);

        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        List<Task> tasks = taskRepository.findByAssignedUserId(userId);
        logger.debug("Found {} tasks for user ID: {}", tasks.size(), userId);

        return tasks.stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByStatus(TaskStatus status) {
        logger.debug("Fetching tasks with status: {}", status);

        List<Task> tasks = taskRepository.findByStatus(status);
        logger.debug("Found {} tasks with status: {}", tasks.size(), status);

        return tasks.stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByUserIdAndStatus(Long userId, TaskStatus status) {
        logger.debug("Fetching tasks for user ID: {} with status: {}", userId, status);

        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        List<Task> tasks = taskRepository.findByAssignedUserIdAndStatus(userId, status);
        logger.debug("Found {} tasks for user ID: {} with status: {}", tasks.size(), userId, status);

        return tasks.stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponse updateTask(Long id, TaskRequest request) {
        logger.info("Updating task with ID: {}", id);

        // Find existing task
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        // Update entity from request
        taskMapper.updateEntityFromRequest(existingTask, request);

        // Update assigned user if userId is provided
        if (request.getAssignedUserId() != null) {
            User user = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getAssignedUserId()));
            taskMapper.setAssignedUser(existingTask, user);
            logger.debug("Updated task assignment to user ID: {}", user.getId());
        } else {
            // If userId is null, remove assignment
            taskMapper.setAssignedUser(existingTask, null);
            logger.debug("Removed task assignment");
        }

        // Save updated entity
        Task updatedTask = taskRepository.save(existingTask);
        logger.info("Successfully updated task with ID: {}", id);

        return taskMapper.toResponse(updatedTask);
    }

    @Override
    public TaskResponse updateTaskStatus(Long id, TaskStatus status) {
        logger.info("Updating status for task ID: {} to {}", id, status);

        // Find existing task
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        // Update only the status
        existingTask.setStatus(status);

        // Save updated entity
        Task updatedTask = taskRepository.save(existingTask);
        logger.info("Successfully updated task status for ID: {}", id);

        return taskMapper.toResponse(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        logger.info("Deleting task with ID: {}", id);

        // Verify task exists
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task", "id", id);
        }

        // Delete task
        taskRepository.deleteById(id);
        logger.info("Successfully deleted task with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getOverdueTasks() {
        logger.debug("Fetching overdue tasks");

        LocalDate today = LocalDate.now();
        List<Task> overdueTasks = taskRepository.findByDueDateBeforeAndStatusNot(today, TaskStatus.DONE);
        logger.debug("Found {} overdue tasks", overdueTasks.size());

        return overdueTasks.stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }
}
