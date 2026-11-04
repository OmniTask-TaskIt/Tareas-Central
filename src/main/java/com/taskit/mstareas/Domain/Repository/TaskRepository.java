package com.taskit.mstareas.Domain.Repository;

import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskCategory;
import com.taskit.mstareas.Domain.Model.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(String taskId);

    void deleteById(String taskId);

    boolean existsById(String taskId);

    List<Task> findByFilters(TaskCategory category, Long minPrice, Long maxPrice, String keyword);

    List<Task> findByCategories(List<TaskCategory> categories);

    List<Task> findByOwner(String ownerId);

    List<Task> findByAssignee(String assigneeId);

    List<Task> findByAssigneeAndStatus(String assigneeId, TaskStatus status);
}