package com.taskit.mstareas.Application.Ports.Outbound;

import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskStatus;

public interface TaskEventPublisher {
    void createChat(String taskId, String taskName, String taskOwner, String taskAssignee);

    void deleteChat(String taskId);

    void notifyTaskAccepted(String taskId, String taskOwner, String taskAssignee, long amount);

    void notifyTaskCreatedForAita(Task task);

    void publishTaskStatusChanged(Task task, TaskStatus previousStatus);
}