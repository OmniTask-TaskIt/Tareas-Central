package com.taskit.mstareas.Infrastucture.Inbound;
 
import com.taskit.mstareas.Application.Ports.Inbound.*;
import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskCategory;
import com.taskit.mstareas.Infrastucture.Inbound.DTO.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.Map;
 
@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
@Tag(name = "Task Controller", description = "Endpoints para la gestión de tareas en TaskIt")
public class TaskController {
 
    private final CreateTaskUserCase createTaskUserCase;
    private final EditTaskUseCase editTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;
    private final AcceptTaskUseCase acceptTaskUseCase;
    private final ReportTaskUseCase reportTaskUseCase;
    private final DisputeTaskUseCase disputeTaskUseCase;
    private final SearchTasksUseCase searchTasksUseCase;
    private final GetTaskFeedUseCase getTaskFeedUseCase;
    private final GetTaskHistoryUseCase getTaskHistoryUseCase;
    private final CompleteTaskUseCase completeTaskUseCase;
 
    @GetMapping("/health")
    @Operation(summary = "Verificar salud del servicio")
    public Map<String, String> health() {
        return Map.of("status", "UP", "message", "Mstareas is running");
    }
 
    @PostMapping("/create")
    @Operation(summary = "Crear una nueva tarea")
    public ResponseEntity<Task> createTask(@RequestBody CreateTaskRequestDTO request) {
 
        Task task = createTaskUserCase.createTask(
                request.taskOwner(),
                request.taskName(),
                request.taskDescription(),
                request.price(),
                request.category()
        );
 
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }
 
    @PutMapping("/{taskId}")
    @Operation(summary = "Editar una tarea existente", description = "Solo se puede editar si la tarea está en estado ACTIVE")
    public ResponseEntity<Task> editTask(@PathVariable String taskId, @RequestBody EditTaskRequestDTO request) {
        Task task = editTaskUseCase.editTask(
                taskId,
                request.taskName(),
                request.taskDescription(),
                request.price(),
                request.category()
        );
        return ResponseEntity.ok(task);
    }
 
    @DeleteMapping("/{taskId}")
    @Operation(summary = "Eliminar una tarea", description = "Solo se puede eliminar si la tarea está en estado ACTIVE")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId) {
        deleteTaskUseCase.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
 
    @PatchMapping("/{taskId}/accept")
    @Operation(summary = "Aceptar una tarea", description = "Asigna un trabajador a la tarea y cambia su estado a ASSIGNED")
    public ResponseEntity<Task> acceptTask(@PathVariable String taskId, @RequestBody AcceptTaskRequestDTO request) {
        Task task = acceptTaskUseCase.acceptTask(taskId, request.taskAssignee());
        return ResponseEntity.ok(task);
    }
 
    @PatchMapping("/{taskId}/report")
    @Operation(summary = "Reportar una tarea")
    public ResponseEntity<Task> reportTask(@PathVariable String taskId, @RequestBody ReportTaskRequestDTO request) {
        Task task = reportTaskUseCase.reportTask(taskId, request.reason());
        return ResponseEntity.ok(task);
    }
 
    @PatchMapping("/{taskId}/dispute")
    @Operation(summary = "Disputar una tarea")
    public ResponseEntity<Task> disputeTask(@PathVariable String taskId, @RequestBody DisputeTaskRequestDTO request) {
        Task task = disputeTaskUseCase.disputeTask(taskId, request.reason());
        return ResponseEntity.ok(task);
    }
 
    @PatchMapping("/{taskId}/complete")
    @Operation(summary = "Aprobar finalización de tarea", description = "Requiere aprobación del dueño y del asignado para marcar como COMPLETED")
    public ResponseEntity<Task> completeTask(@PathVariable String taskId, @RequestBody CompleteTaskRequestDTO request) {
        Task task = completeTaskUseCase.approveCompletion(taskId, request.userId());
        return ResponseEntity.ok(task);
    }
 
    @GetMapping("/search")
    @Operation(summary = "Buscar y filtrar tareas")
    public ResponseEntity<List<Task>> searchTasks(
            @RequestParam(required = false) TaskCategory category,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) String keyword) {
        List<Task> tasks = searchTasksUseCase.searchTasks(category, minPrice, maxPrice, keyword);
        return ResponseEntity.ok(tasks);
    }
 
    @GetMapping("/feed")
    @Operation(summary = "Obtener feed personalizado de tareas")
    public ResponseEntity<List<Task>> getTaskFeed(@RequestParam List<TaskCategory> categories) {
        List<Task> tasks = getTaskFeedUseCase.getTaskFeed(categories);
        return ResponseEntity.ok(tasks);
    }
 
    @GetMapping("/history/created/{userId}")
    @Operation(summary = "Historial de tareas creadas por un usuario")
    public ResponseEntity<List<Task>> getTasksCreatedByUser(@PathVariable String userId) {
        List<Task> tasks = getTaskHistoryUseCase.getTasksCreatedByUser(userId);
        return ResponseEntity.ok(tasks);
    }
 
    @GetMapping("/history/assigned/{userId}")
    @Operation(summary = "Historial de tareas asignadas a un usuario")
    public ResponseEntity<List<Task>> getTasksAssignedToUser(@PathVariable String userId) {
        List<Task> tasks = getTaskHistoryUseCase.getTasksAssignedToUser(userId);
        return ResponseEntity.ok(tasks);
    }
 
    @GetMapping("/history/completed/{userId}")
    @Operation(summary = "Historial de tareas completadas por un usuario")
    public ResponseEntity<List<Task>> getTasksCompletedByUser(@PathVariable String userId) {
        List<Task> tasks = getTaskHistoryUseCase.getTasksCompletedByUser(userId);
        return ResponseEntity.ok(tasks);
    }
}
