package com.taskit.mstareas.Application.Services;

import com.taskit.mstareas.Application.Ports.Inbound.CompleteTaskUseCase;
import com.taskit.mstareas.Application.Ports.Outbound.TaskEventPublisher;
import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskStatus;
import com.taskit.mstareas.Domain.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompleteTaskImpl implements CompleteTaskUseCase {

    private final TaskRepository taskRepository;
    private final TaskEventPublisher taskEventPublisher;

    @Override
    public Task approveCompletion(String taskId, String userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));

        if (task.getTaskStatus() != TaskStatus.ASSIGNED && task.getTaskStatus() != TaskStatus.SEMI_COMPLETED) {
            throw new IllegalStateException("Task must be in ASSIGNED or SEMI_COMPLETED status to be completed");
        }

        boolean isOwner = userId.equals(task.getTaskOwner());
        boolean isAssignee = userId.equals(task.getTaskAssignee());

        if (!isOwner && !isAssignee) {
            throw new IllegalArgumentException("User is not authorized to approve completion for this task");
        }

        if (isOwner) {
            task.setOwnerApproval(true);
        }
        if (isAssignee) {
            task.setAssigneeApproval(true);
        }

        if (task.isOwnerApproval() && task.isAssigneeApproval()) {
            task.setTaskStatus(TaskStatus.COMPLETED);
            taskEventPublisher.deleteChat(taskId);
        } else if (task.isOwnerApproval() || task.isAssigneeApproval()) {
            task.setTaskStatus(TaskStatus.SEMI_COMPLETED);
        }

        return taskRepository.save(task);
    }
}
