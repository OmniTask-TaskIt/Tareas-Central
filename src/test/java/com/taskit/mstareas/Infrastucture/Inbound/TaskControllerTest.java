package com.taskit.mstareas.Infrastucture.Inbound;

import com.taskit.mstareas.Application.Ports.Inbound.*;
import com.taskit.mstareas.Domain.Model.TaskStatus;
import com.taskit.mstareas.Infrastucture.Inbound.DTO.TaskCompletionStatusDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private CreateTaskUserCase createTaskUserCase;
    @MockBean private EditTaskUseCase editTaskUseCase;
    @MockBean private DeleteTaskUseCase deleteTaskUseCase;
    @MockBean private AcceptTaskUseCase acceptTaskUseCase;
    @MockBean private ReportTaskUseCase reportTaskUseCase;
    @MockBean private DisputeTaskUseCase disputeTaskUseCase;
    @MockBean private SearchTasksUseCase searchTasksUseCase;
    @MockBean private GetTaskFeedUseCase getTaskFeedUseCase;
    @MockBean private GetTaskHistoryUseCase getTaskHistoryUseCase;
    @MockBean private CompleteTaskUseCase completeTaskUseCase;
    @MockBean private GetTaskStatusUseCase getTaskStatusUseCase;

    @Test
    public void whenSearchWithUndefinedCategory_thenReturnsBadRequestWithCustomMessage() throws Exception {
        mockMvc.perform(get("/api/tasks/search")
                .param("category", "undefined"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Parámetro inválido"))
                .andExpect(jsonPath("$.message").value("El parámetro 'category' debe ser de tipo 'TaskCategory'. Valor recibido: 'undefined'. (Error frecuente del Frontend al enviar variables no inicializadas)"));
    }

    @Test
    public void whenFeedWithUndefinedCategories_thenReturnsBadRequestWithCustomMessage() throws Exception {
        mockMvc.perform(get("/api/tasks/feed")
                .param("categories", "undefined"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Parámetro inválido"))
                .andExpect(jsonPath("$.message").value("El parámetro 'categories' debe ser de tipo 'List'. Valor recibido: 'undefined'. (Error frecuente del Frontend al enviar variables no inicializadas)"));
    }

    @Test
    public void whenGetTaskStatus_thenReturnsStatus() throws Exception {
        String taskId = "task123";
        TaskCompletionStatusDTO statusDTO = new TaskCompletionStatusDTO(
                taskId,
                TaskStatus.SEMI_COMPLETED,
                true,
                false,
                "owner1",
                "assignee1"
        );

        when(getTaskStatusUseCase.getTaskStatus(taskId)).thenReturn(statusDTO);

        mockMvc.perform(get("/api/tasks/" + taskId + "/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(taskId))
                .andExpect(jsonPath("$.status").value("SEMI_COMPLETED"))
                .andExpect(jsonPath("$.ownerApproved").value(true))
                .andExpect(jsonPath("$.assigneeApproved").value(false))
                .andExpect(jsonPath("$.ownerId").value("owner1"))
                .andExpect(jsonPath("$.assigneeId").value("assignee1"));
    }
}
