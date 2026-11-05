package com.taskit.mstareas.Infrastucture.Inbound;

import com.taskit.mstareas.Application.Ports.Inbound.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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
}
