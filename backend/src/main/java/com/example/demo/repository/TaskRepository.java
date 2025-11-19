package com.example.demo.repository;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Find all tasks assigned to a specific user.
     *
     * @param userId the ID of the user
     * @return list of tasks assigned to the user
     */
    List<Task> findByAssignedUserId(Long userId);

    /**
     * Find all tasks with a specific status.
     *
     * @param status the task status to filter by
     * @return list of tasks with the given status
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Find all tasks assigned to a specific user with a specific status.
     *
     * @param userId the ID of the user
     * @param status the task status to filter by
     * @return list of tasks matching the criteria
     */
    List<Task> findByAssignedUserIdAndStatus(Long userId, TaskStatus status);

    /**
     * Find all tasks with a due date before the specified date.
     *
     * @param date the date to compare against
     * @return list of tasks due before the given date
     */
    List<Task> findByDueDateBefore(LocalDate date);

    /**
     * Find all overdue tasks that are not completed.
     *
     * @param currentDate the current date
     * @param status the status to exclude (DONE)
     * @return list of overdue tasks
     */
    List<Task> findByDueDateBeforeAndStatusNot(LocalDate currentDate, TaskStatus status);
}
