package com.taskit.mstareas.Application.Ports.Inbound;

import com.taskit.mstareas.Domain.Model.Task;
import java.util.List;

public interface GetTaskHistoryUseCase {
    List<Task> getTasksCreatedByUser(String userId);
    List<Task> getTasksAssignedToUser(String userId);
    List<Task> getTasksCompletedByUser(String userId);
}
