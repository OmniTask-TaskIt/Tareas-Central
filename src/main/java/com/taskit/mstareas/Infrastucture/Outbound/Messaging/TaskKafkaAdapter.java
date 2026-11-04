package com.taskit.mstareas.Infrastucture.Outbound.Messaging;

import com.taskit.mstareas.Application.Ports.Outbound.TaskEventPublisher;
import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Infrastucture.Outbound.Config.TopicsKafka;
import com.taskit.mstareas.Infrastucture.Outbound.Messaging.DTO.CreateChatRequestDTO;
import com.taskit.mstareas.Infrastucture.Outbound.Messaging.DTO.DeleteChatRequestDTO;
import com.taskit.mstareas.Infrastucture.Outbound.Messaging.DTO.TaskAcceptedPaymentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskKafkaAdapter implements TaskEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void createChat(String taskId, String taskName, String taskOwner, String taskAssignee) {
        CreateChatRequestDTO event = new CreateChatRequestDTO(
                taskId,
                taskName,
                taskOwner,
                taskAssignee
        );
        kafkaTemplate.send(TopicsKafka.CHAT_EVENTS, taskId, event);
    }

    @Override
    public void deleteChat(String taskId) {
        DeleteChatRequestDTO event = new DeleteChatRequestDTO(taskId);
        kafkaTemplate.send(TopicsKafka.CHAT_EVENTS, taskId, event);
    }

    @Override
    public void notifyTaskAccepted(String taskId, String taskOwner, String taskAssignee, long amount) {
        TaskAcceptedPaymentDTO event = new TaskAcceptedPaymentDTO(taskId, taskOwner, taskAssignee, amount);
        kafkaTemplate.send(TopicsKafka.PAYMENT_EVENTS, taskId, event);
    }
}