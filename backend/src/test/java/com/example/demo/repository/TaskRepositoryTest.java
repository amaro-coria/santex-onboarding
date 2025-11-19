package com.example.demo.repository;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTest {

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
    }

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser1;
    private User testUser2;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        // Create test users
        testUser1 = userRepository.save(new User(
                "user1@example.com",
                "password123",
                "User",
                "One"
        ));

        testUser2 = userRepository.save(new User(
                "user2@example.com",
                "password123",
                "User",
                "Two"
        ));

        // Create test tasks
        task1 = new Task(
                "Task 1",
                "Description 1",
                TaskStatus.TODO,
                LocalDate.now().plusDays(5)
        );
        task1.setAssignedUser(testUser1);

        task2 = new Task(
                "Task 2",
                "Description 2",
                TaskStatus.IN_PROGRESS,
                LocalDate.now().minusDays(2)
        );
        task2.setAssignedUser(testUser1);

        task3 = new Task(
                "Task 3",
                "Description 3",
                TaskStatus.DONE,
                LocalDate.now().plusDays(10)
        );
        task3.setAssignedUser(testUser2);
    }

    @Test
    void shouldSaveTask() {
        // When
        Task savedTask = taskRepository.save(task1);

        // Then
        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo("Task 1");
        assertThat(savedTask.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(savedTask.getCreatedAt()).isNotNull();
        assertThat(savedTask.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldFindTasksByAssignedUserId() {
        // Given
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        // When
        List<Task> user1Tasks = taskRepository.findByAssignedUserId(testUser1.getId());

        // Then
        assertThat(user1Tasks).hasSize(2);
        assertThat(user1Tasks).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Task 1", "Task 2");
    }

    @Test
    void shouldFindTasksByStatus() {
        // Given
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        // When
        List<Task> todoTasks = taskRepository.findByStatus(TaskStatus.TODO);

        // Then
        assertThat(todoTasks).hasSize(1);
        assertThat(todoTasks.get(0).getTitle()).isEqualTo("Task 1");
    }

    @Test
    void shouldFindTasksByAssignedUserIdAndStatus() {
        // Given
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        // When
        List<Task> user1TodoTasks = taskRepository.findByAssignedUserIdAndStatus(
                testUser1.getId(),
                TaskStatus.TODO
        );

        // Then
        assertThat(user1TodoTasks).hasSize(1);
        assertThat(user1TodoTasks.get(0).getTitle()).isEqualTo("Task 1");
    }

    @Test
    void shouldFindTasksByDueDateBefore() {
        // Given
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        // When
        List<Task> pastDueTasks = taskRepository.findByDueDateBefore(LocalDate.now());

        // Then
        assertThat(pastDueTasks).hasSize(1);
        assertThat(pastDueTasks.get(0).getTitle()).isEqualTo("Task 2");
    }

    @Test
    void shouldFindOverdueTasks() {
        // Given
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        // When
        List<Task> overdueTasks = taskRepository.findByDueDateBeforeAndStatusNot(
                LocalDate.now(),
                TaskStatus.DONE
        );

        // Then
        assertThat(overdueTasks).hasSize(1);
        assertThat(overdueTasks.get(0).getTitle()).isEqualTo("Task 2");
        assertThat(overdueTasks.get(0).getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldUpdateTask() {
        // Given
        Task savedTask = taskRepository.save(task1);
        Long taskId = savedTask.getId();

        // When
        savedTask.setTitle("Updated Task");
        savedTask.setStatus(TaskStatus.IN_PROGRESS);
        taskRepository.save(savedTask);

        // Then
        Task updatedTask = taskRepository.findById(taskId).orElseThrow();
        assertThat(updatedTask.getTitle()).isEqualTo("Updated Task");
        assertThat(updatedTask.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldDeleteTask() {
        // Given
        Task savedTask = taskRepository.save(task1);
        Long taskId = savedTask.getId();

        // When
        taskRepository.deleteById(taskId);

        // Then
        assertThat(taskRepository.findById(taskId)).isEmpty();
    }

    @Test
    void shouldAllowTaskWithoutAssignedUser() {
        // Given
        Task unassignedTask = new Task(
                "Unassigned Task",
                "No user assigned",
                TaskStatus.TODO
        );

        // When
        Task savedTask = taskRepository.save(unassignedTask);

        // Then
        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getAssignedUser()).isNull();
    }
}
