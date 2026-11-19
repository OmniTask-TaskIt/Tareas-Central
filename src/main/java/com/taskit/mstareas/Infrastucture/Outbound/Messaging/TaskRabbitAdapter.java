package com.taskit.mstareas.Infrastucture.Outbound.Messaging;

import com.taskit.mstareas.Application.Ports.Outbound.TaskEventPublisher;
import com.taskit.mstareas.Infrastucture.Outbound.Config.RabbitConfig;
import com.taskit.mstareas.Infrastucture.Outbound.Messaging.DTO.CreateChatRequestDTO;
import com.taskit.mstareas.Infrastucture.Outbound.Messaging.DTO.DeleteChatRequestDTO;
import com.taskit.mstareas.Infrastucture.Outbound.Messaging.DTO.TaskAcceptedPaymentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Infrastucture.Outbound.Messaging.DTO.AitaTaskEventDTO;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class TaskRabbitAdapter implements TaskEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void createChat(String taskId, String taskName, String taskOwner, String taskAssignee) {
        CreateChatRequestDTO event = new CreateChatRequestDTO(
                taskId,
                taskName,
                taskOwner,
                taskAssignee
        );
        rabbitTemplate.convertAndSend(
                RabbitConfig.CHAT_EXCHANGE,
                RabbitConfig.ROUTING_KEY_CHAT_CREATE,
                event
        );
    }

    @Override
    public void deleteChat(String taskId) {
        DeleteChatRequestDTO event = new DeleteChatRequestDTO(taskId);
        rabbitTemplate.convertAndSend(
                RabbitConfig.CHAT_EXCHANGE,
                RabbitConfig.ROUTING_KEY_CHAT_DELETE,
                event
        );
    }

    @Override
    public void notifyTaskAccepted(String taskId, String taskOwner, String taskAssignee, long amount) {
        TaskAcceptedPaymentDTO event = new TaskAcceptedPaymentDTO(taskId, taskOwner, taskAssignee, amount);
        rabbitTemplate.convertAndSend(
                RabbitConfig.PAYMENT_EXCHANGE,
                RabbitConfig.ROUTING_KEY_PAYMENT_NOTIFY,
                event
        );
    }
    //  AITA ---
    public void notifyTaskCreatedForAita(Task task) {
        String nowIso = java.time.OffsetDateTime.now().format(java.time.format.DateTimeFormatter.ISO_INSTANT);

        var taskPayload = AitaTaskEventDTO.AitaTaskPayloadDTO.builder()
                .taskId(task.getTaskId())
                .taskName(task.getTaskName())
                .taskDescription(task.getTaskDescription())
                .taskStatus(task.getTaskStatus() != null ? task.getTaskStatus().name() : "ACTIVE")
                .taskOwner(task.getTaskOwner())
                .taskAssignee(task.getTaskAssignee())
                .creationDate(task.getCreationDate() != null ? task.getCreationDate().atOffset(java.time.ZoneOffset.UTC).format(java.time.format.DateTimeFormatter.ISO_INSTANT) : nowIso)
                .category(task.getCategory() != null ? task.getCategory().name() : "ARREGLOS")
                .price(task.getPrice())
                .reportReason(task.getReportReason())
                .disputeReason(task.getDisputeReason())
                .ownerApproval(task.isOwnerApproval())
                .assigneeApproval(task.isAssigneeApproval())
                .build();

        AitaTaskEventDTO event = AitaTaskEventDTO.builder()
                .eventId("evt-task-" + java.util.UUID.randomUUID().toString())
                .eventType("TaskCreatedEvent")
                .occurredAt(nowIso)
                .task(taskPayload)
                .build();
                
        log.info("Enviando evento TaskCreatedEvent a AITA para la tarea ID: {}", task.getTaskId());
        rabbitTemplate.convertAndSend(
                RabbitConfig.AITA_EXCHANGE,        
                RabbitConfig.ROUTING_KEY_AITA_EVENTS, 
                event
        );
      
        log.info("¡Evento enviado con éxito a AITA!");
    }
}
