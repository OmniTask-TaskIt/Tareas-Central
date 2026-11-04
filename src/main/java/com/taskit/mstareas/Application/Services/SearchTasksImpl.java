package com.taskit.mstareas.Application.Services;

import com.taskit.mstareas.Application.Ports.Inbound.SearchTasksUseCase;
import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskCategory;
import com.taskit.mstareas.Domain.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchTasksImpl implements SearchTasksUseCase {

    private final TaskRepository taskRepository;

    @Override
    public List<Task> searchTasks(TaskCategory category, Long minPrice, Long maxPrice, String keyword) {
        return taskRepository.findByFilters(category, minPrice, maxPrice, keyword);
    }
}
