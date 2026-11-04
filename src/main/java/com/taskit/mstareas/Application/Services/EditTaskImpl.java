package com.taskit.mstareas.Application.Services;
 
import com.taskit.mstareas.Application.Ports.Inbound.EditTaskUseCase;
import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskCategory;
import com.taskit.mstareas.Domain.Model.TaskStatus;
import com.taskit.mstareas.Domain.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
 
@Service
@RequiredArgsConstructor
public class EditTaskImpl implements EditTaskUseCase {

    private final TaskRepository taskRepository;

    @Override
    public Task editTask(String taskId, String taskName, String taskDescription, long price, TaskCategory category) {
        if (taskId == null || taskName == null || taskDescription == null || price <= 0) {
            throw new IllegalArgumentException("Invalid task data for editing");
        }
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));

        if (task.getTaskStatus() != TaskStatus.ACTIVE) {
            throw new IllegalStateException("Task cannot be edited because it is not in ACTIVE status");
        }

        task.setTaskName(taskName);
        task.setTaskDescription(taskDescription);
        task.setPrice(price);
        task.setCategory(category);

        return taskRepository.save(task);
    }
}
