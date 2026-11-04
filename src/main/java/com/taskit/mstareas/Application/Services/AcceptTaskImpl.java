package com.taskit.mstareas.Application.Services;

import com.taskit.mstareas.Application.Ports.Inbound.AcceptTaskUseCase;
import com.taskit.mstareas.Application.Ports.Outbound.TaskEventPublisher;
import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskStatus;
import com.taskit.mstareas.Domain.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcceptTaskImpl implements AcceptTaskUseCase {

    private final TaskRepository taskRepository;
    private final TaskEventPublisher taskEventPublisher;

    @Override
    public Task acceptTask(String taskId, String taskAssignee) {
        if (taskId == null || taskAssignee == null) {
            throw new IllegalArgumentException("Task ID and Assignee cannot be null");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));

        task.setTaskAssignee(taskAssignee);
        task.setTaskStatus(TaskStatus.ASSIGNED);
        Task savedTask = taskRepository.save(task);

        taskEventPublisher.createChat(
                savedTask.getTaskId(),
                savedTask.getTaskName(),
                savedTask.getTaskOwner(),
                savedTask.getTaskAssignee()
        );

        taskEventPublisher.notifyTaskAccepted(
                savedTask.getTaskId(),
                savedTask.getTaskOwner(),
                savedTask.getTaskAssignee(),
                savedTask.getPrice()
        );
        return savedTask;
    }
}
