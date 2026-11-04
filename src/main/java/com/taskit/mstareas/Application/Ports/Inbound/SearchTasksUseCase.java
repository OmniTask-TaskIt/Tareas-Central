package com.taskit.mstareas.Application.Ports.Inbound;

import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskCategory;

import java.util.List;

public interface SearchTasksUseCase {
    List<Task> searchTasks(TaskCategory category, Long minPrice, Long maxPrice, String keyword);
}
