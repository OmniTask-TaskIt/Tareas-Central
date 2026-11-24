package com.taskit.mstareas.Application.Services;

import com.taskit.mstareas.Application.Ports.Inbound.DisputeTaskUseCase;
import com.taskit.mstareas.Application.Ports.Outbound.TaskEventPublisher;
import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskStatus;
import com.taskit.mstareas.Domain.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisputeTaskImpl implements DisputeTaskUseCase {

    private final TaskRepository taskRepository;
    private final TaskEventPublisher taskEventPublisher;

    @Override
    public Task disputeTask(String taskId, String reason) {
        if (taskId == null || reason == null || reason.isEmpty()) {
            throw new IllegalArgumentException("Task ID and reason are required");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));

        TaskStatus previousStatus = task.getTaskStatus();
        task.setTaskStatus(TaskStatus.DISPUTED);
        task.setDisputeReason(reason);

        Task savedTask = taskRepository.save(task);
        taskEventPublisher.publishTaskStatusChanged(savedTask, previousStatus);

        return savedTask;
    }
}