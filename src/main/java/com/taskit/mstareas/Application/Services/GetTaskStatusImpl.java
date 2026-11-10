package com.taskit.mstareas.Application.Services;

import com.taskit.mstareas.Application.Ports.Inbound.GetTaskStatusUseCase;
import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Repository.TaskRepository;
import com.taskit.mstareas.Infrastucture.Inbound.DTO.TaskCompletionStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTaskStatusImpl implements GetTaskStatusUseCase {

    private final TaskRepository taskRepository;

    @Override
    public TaskCompletionStatusDTO getTaskStatus(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));

        return new TaskCompletionStatusDTO(
                task.getTaskId(),
                task.getTaskStatus(),
                task.isOwnerApproval(),
                task.isAssigneeApproval(),
                task.getTaskOwner(),
                task.getTaskAssignee()
        );
    }
}
