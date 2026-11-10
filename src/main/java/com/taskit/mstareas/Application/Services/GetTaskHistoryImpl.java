package com.taskit.mstareas.Application.Services;

import com.taskit.mstareas.Application.Ports.Inbound.GetTaskHistoryUseCase;
import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskStatus;
import com.taskit.mstareas.Domain.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTaskHistoryImpl implements GetTaskHistoryUseCase {

    private final TaskRepository taskRepository;

    @Override
    public List<Task> getTasksCreatedByUser(String userId) {
        return taskRepository.findByOwner(userId);
    }

    @Override
    public List<Task> getTasksAssignedToUser(String userId) {
        return taskRepository.findByAssignee(userId);
    }

    @Override
    public List<Task> getTasksCompletedByUser(String userId) {
        List<Task> completed = taskRepository.findByAssigneeAndStatus(userId, TaskStatus.COMPLETED);
        List<Task> semiCompleted = taskRepository.findByAssigneeAndStatus(userId, TaskStatus.SEMI_COMPLETED);
        completed.addAll(semiCompleted);
        return completed;
    }
}
