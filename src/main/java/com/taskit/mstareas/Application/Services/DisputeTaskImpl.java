package com.taskit.mstareas.Application.Services;

import com.taskit.mstareas.Application.Ports.Inbound.DisputeTaskUseCase;
import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskStatus;
import com.taskit.mstareas.Domain.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisputeTaskImpl implements DisputeTaskUseCase {

    private final TaskRepository taskRepository;

    @Override
    public Task disputeTask(String taskId, String reason) {
        if (taskId == null || reason == null || reason.isEmpty()) {
            throw new IllegalArgumentException("Task ID and reason are required");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));

        task.setTaskStatus(TaskStatus.DISPUTED);
        task.setDisputeReason(reason);

        return taskRepository.save(task);
    }
}
