package com.taskit.mstareas.Application.Ports.Inbound;

import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskCategory;

import java.util.List;

public interface GetTaskFeedUseCase {
    List<Task> getTaskFeed(List<TaskCategory> userCategories);
}
