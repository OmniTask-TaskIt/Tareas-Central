package com.taskit.mstareas.Application.Ports.Inbound;

import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskCategory;

public interface EditTaskUseCase {
    Task editTask(String taskId, String taskName, String taskDescription, long price, TaskCategory category);
}
