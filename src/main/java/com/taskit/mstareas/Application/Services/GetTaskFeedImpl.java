package com.taskit.mstareas.Application.Services;

import com.taskit.mstareas.Application.Ports.Inbound.GetTaskFeedUseCase;
import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskCategory;
import com.taskit.mstareas.Domain.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTaskFeedImpl implements GetTaskFeedUseCase {

    private final TaskRepository taskRepository;

    @Override
    public List<Task> getTaskFeed(List<TaskCategory> userCategories) {
        if (userCategories == null || userCategories.isEmpty()) {
            return List.of();
        }
        return taskRepository.findByCategories(userCategories);
    }
}
