package com.example.demo.mapper;

import com.example.demo.dto.TaskRequest;
import com.example.demo.dto.TaskResponse;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import org.springframework.stereotype.Component;

/**
 * Mapper utility for converting between Task entity and DTOs.
 */
@Component
public class TaskMapper {

    private final UserMapper userMapper;

    public TaskMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * Convert Task entity to TaskResponse DTO.
     *
     * @param task the task entity
     * @return the task response DTO
     */
    public TaskResponse toResponse(Task task) {
        if (task == null) {
            return null;
        }

        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setDueDate(task.getDueDate());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());

        // Map assigned user if present
        if (task.getAssignedUser() != null) {
            response.setAssignedUser(userMapper.toResponse(task.getAssignedUser()));
        }

        return response;
    }

    /**
     * Convert TaskRequest DTO to Task entity.
     *
     * @param request the task request DTO
     * @return the task entity
     */
    public Task toEntity(TaskRequest request) {
        if (request == null) {
            return null;
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());

        return task;
    }

    /**
     * Update existing Task entity with data from TaskRequest DTO.
     *
     * @param task the existing task entity
     * @param request the update request DTO
     */
    public void updateEntityFromRequest(Task task, TaskRequest request) {
        if (task == null || request == null) {
            return;
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());
    }

    /**
     * Set the assigned user for a task.
     *
     * @param task the task entity
     * @param user the user to assign
     */
    public void setAssignedUser(Task task, User user) {
        if (task == null) {
            return;
        }
        task.setAssignedUser(user);
    }
}
