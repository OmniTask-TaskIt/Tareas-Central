package com.taskit.mstareas.Application.Ports.Inbound;

import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskCategory;

public interface CreateTaskUserCase {
    Task createTask(String taskOwner, String taskName, String taskDescription, long price, TaskCategory category);
}
