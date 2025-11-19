package com.example.demo.service;

import com.example.demo.dto.TaskRequest;
import com.example.demo.dto.TaskResponse;
import com.example.demo.dto.UserResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.model.User;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task testTask;
    private User testUser;
    private TaskRequest taskRequest;
    private TaskResponse taskResponse;

    @BeforeEach
    void setUp() {
        testUser = new User("user@example.com", "password", "Test", "User");
        testUser.setId(1L);

        testTask = new Task("Test Task", "Description", TaskStatus.TODO);
        testTask.setId(1L);
        testTask.setAssignedUser(testUser);
        testTask.setDueDate(LocalDate.now().plusDays(5));
        testTask.setCreatedAt(LocalDateTime.now());
        testTask.setUpdatedAt(LocalDateTime.now());

        taskRequest = new TaskRequest(
                "Test Task",
                "Description",
                TaskStatus.TODO,
                1L,
                LocalDate.now().plusDays(5)
        );

        UserResponse userResponse = new UserResponse(1L, "user@example.com", "Test", "User", LocalDateTime.now(), LocalDateTime.now());

        taskResponse = new TaskResponse(
                1L,
                "Test Task",
                "Description",
                TaskStatus.TODO,
                userResponse,
                LocalDate.now().plusDays(5),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void shouldCreateTaskSuccessfully() {
        // Given
        when(taskMapper.toEntity(any(TaskRequest.class))).thenReturn(testTask);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
        when(taskMapper.toResponse(any(Task.class))).thenReturn(taskResponse);
        doNothing().when(taskMapper).setAssignedUser(any(Task.class), any(User.class));

        // When
        TaskResponse result = taskService.createTask(taskRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Task");

        verify(taskMapper).toEntity(taskRequest);
        verify(userRepository).findById(1L);
        verify(taskMapper).setAssignedUser(testTask, testUser);
        verify(taskRepository).save(testTask);
        verify(taskMapper).toResponse(testTask);
    }

    @Test
    void shouldCreateTaskWithoutAssignedUser() {
        // Given
        TaskRequest requestWithoutUser = new TaskRequest("Test Task", "Description", TaskStatus.TODO, null, LocalDate.now());
        when(taskMapper.toEntity(any(TaskRequest.class))).thenReturn(testTask);
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
        when(taskMapper.toResponse(any(Task.class))).thenReturn(taskResponse);

        // When
        TaskResponse result = taskService.createTask(requestWithoutUser);

        // Then
        assertThat(result).isNotNull();

        verify(taskMapper).toEntity(requestWithoutUser);
        verify(userRepository, never()).findById(any());
        verify(taskRepository).save(testTask);
    }

    @Test
    void shouldThrowExceptionWhenCreatingTaskWithNonExistentUser() {
        // Given
        when(taskMapper.toEntity(any(TaskRequest.class))).thenReturn(testTask);
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        TaskRequest requestWithInvalidUser = new TaskRequest("Test", "Desc", TaskStatus.TODO, 999L, null);

        // When/Then
        assertThatThrownBy(() -> taskService.createTask(requestWithInvalidUser))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id");

        verify(userRepository).findById(999L);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void shouldGetTaskByIdSuccessfully() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskMapper.toResponse(any(Task.class))).thenReturn(taskResponse);

        // When
        TaskResponse result = taskService.getTaskById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(taskRepository).findById(1L);
        verify(taskMapper).toResponse(testTask);
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFoundById() {
        // Given
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> taskService.getTaskById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found with id");

        verify(taskRepository).findById(999L);
    }

    @Test
    void shouldGetAllTasksSuccessfully() {
        // Given
        Task task2 = new Task("Task 2", "Description 2", TaskStatus.IN_PROGRESS);
        task2.setId(2L);

        List<Task> tasks = Arrays.asList(testTask, task2);
        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.toResponse(any(Task.class))).thenReturn(taskResponse);

        // When
        List<TaskResponse> result = taskService.getAllTasks();

        // Then
        assertThat(result).hasSize(2);

        verify(taskRepository).findAll();
        verify(taskMapper, times(2)).toResponse(any(Task.class));
    }

    @Test
    void shouldGetTasksByUserIdSuccessfully() {
        // Given
        List<Task> userTasks = Arrays.asList(testTask);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.findByAssignedUserId(1L)).thenReturn(userTasks);
        when(taskMapper.toResponse(any(Task.class))).thenReturn(taskResponse);

        // When
        List<TaskResponse> result = taskService.getTasksByUserId(1L);

        // Then
        assertThat(result).hasSize(1);

        verify(userRepository).existsById(1L);
        verify(taskRepository).findByAssignedUserId(1L);
        verify(taskMapper).toResponse(testTask);
    }

    @Test
    void shouldThrowExceptionWhenGettingTasksByNonExistentUser() {
        // Given
        when(userRepository.existsById(999L)).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> taskService.getTasksByUserId(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id");

        verify(userRepository).existsById(999L);
        verify(taskRepository, never()).findByAssignedUserId(any());
    }

    @Test
    void shouldGetTasksByStatusSuccessfully() {
        // Given
        List<Task> todoTasks = Arrays.asList(testTask);
        when(taskRepository.findByStatus(TaskStatus.TODO)).thenReturn(todoTasks);
        when(taskMapper.toResponse(any(Task.class))).thenReturn(taskResponse);

        // When
        List<TaskResponse> result = taskService.getTasksByStatus(TaskStatus.TODO);

        // Then
        assertThat(result).hasSize(1);

        verify(taskRepository).findByStatus(TaskStatus.TODO);
        verify(taskMapper).toResponse(testTask);
    }

    @Test
    void shouldGetTasksByUserIdAndStatusSuccessfully() {
        // Given
        List<Task> tasks = Arrays.asList(testTask);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.findByAssignedUserIdAndStatus(1L, TaskStatus.TODO)).thenReturn(tasks);
        when(taskMapper.toResponse(any(Task.class))).thenReturn(taskResponse);

        // When
        List<TaskResponse> result = taskService.getTasksByUserIdAndStatus(1L, TaskStatus.TODO);

        // Then
        assertThat(result).hasSize(1);

        verify(userRepository).existsById(1L);
        verify(taskRepository).findByAssignedUserIdAndStatus(1L, TaskStatus.TODO);
        verify(taskMapper).toResponse(testTask);
    }

    @Test
    void shouldUpdateTaskSuccessfully() {
        // Given
        TaskRequest updateRequest = new TaskRequest("Updated Task", "New Description", TaskStatus.IN_PROGRESS, 1L, LocalDate.now());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
        when(taskMapper.toResponse(any(Task.class))).thenReturn(taskResponse);
        doNothing().when(taskMapper).updateEntityFromRequest(any(Task.class), any(TaskRequest.class));
        doNothing().when(taskMapper).setAssignedUser(any(Task.class), any(User.class));

        // When
        TaskResponse result = taskService.updateTask(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();

        verify(taskRepository).findById(1L);
        verify(taskMapper).updateEntityFromRequest(testTask, updateRequest);
        verify(userRepository).findById(1L);
        verify(taskMapper).setAssignedUser(testTask, testUser);
        verify(taskRepository).save(testTask);
    }

    @Test
    void shouldUpdateTaskStatusSuccessfully() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
        when(taskMapper.toResponse(any(Task.class))).thenReturn(taskResponse);

        // When
        TaskResponse result = taskService.updateTaskStatus(1L, TaskStatus.DONE);

        // Then
        assertThat(result).isNotNull();

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(testTask);
        verify(taskMapper).toResponse(testTask);
    }

    @Test
    void shouldDeleteTaskSuccessfully() {
        // Given
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        // When
        taskService.deleteTask(1L);

        // Then
        verify(taskRepository).existsById(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTask() {
        // Given
        when(taskRepository.existsById(999L)).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> taskService.deleteTask(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found with id");

        verify(taskRepository).existsById(999L);
        verify(taskRepository, never()).deleteById(any());
    }

    @Test
    void shouldGetOverdueTasksSuccessfully() {
        // Given
        Task overdueTask = new Task("Overdue Task", "Description", TaskStatus.IN_PROGRESS);
        overdueTask.setDueDate(LocalDate.now().minusDays(2));

        List<Task> overdueTasks = Arrays.asList(overdueTask);
        when(taskRepository.findByDueDateBeforeAndStatusNot(any(LocalDate.class), eq(TaskStatus.DONE)))
                .thenReturn(overdueTasks);
        when(taskMapper.toResponse(any(Task.class))).thenReturn(taskResponse);

        // When
        List<TaskResponse> result = taskService.getOverdueTasks();

        // Then
        assertThat(result).hasSize(1);

        verify(taskRepository).findByDueDateBeforeAndStatusNot(any(LocalDate.class), eq(TaskStatus.DONE));
        verify(taskMapper).toResponse(overdueTask);
    }
}
