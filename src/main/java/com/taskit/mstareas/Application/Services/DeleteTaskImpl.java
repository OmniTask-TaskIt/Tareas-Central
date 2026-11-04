package com.taskit.mstareas.Application.Services;
 
import com.taskit.mstareas.Application.Ports.Inbound.DeleteTaskUseCase;
import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskStatus;
import com.taskit.mstareas.Domain.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
 
@Service
@RequiredArgsConstructor
public class DeleteTaskImpl implements DeleteTaskUseCase {
 
    private final TaskRepository taskRepository;
 
    @Override
    public void deleteTask(String taskId) {
        if (taskId == null || taskId.isEmpty()) {
            throw new IllegalArgumentException("Task ID cannot be null or empty");
        }
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
 
        if (task.getTaskStatus() != TaskStatus.ACTIVE) {
            throw new IllegalStateException("Task cannot be deleted because it is not in ACTIVE status");
        }
 
        taskRepository.deleteById(taskId);
    }
}
