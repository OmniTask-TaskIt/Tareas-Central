package com.taskit.mstareas.Infrastucture.Inbound.Messaging;

import com.taskit.mstareas.Domain.Model.TaskPaymentStatus;
import com.taskit.mstareas.Domain.Repository.TaskRepository;
import com.taskit.mstareas.Infrastucture.Inbound.DTO.PaymentEventDTO;
import com.taskit.mstareas.Infrastucture.Outbound.Config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final TaskRepository taskRepository;

    @RabbitListener(queues = RabbitConfig.TASKS_PAYMENT_QUEUE)
    public void onPaymentEvent(PaymentEventDTO event) {
        log.info("[AUDIT][RABBITMQ-IN] queue={} eventId={} eventType={} transactionId={} taskId={} occurredAt={}",
                RabbitConfig.TASKS_PAYMENT_QUEUE,
                event.eventId(),
                event.eventType(),
                event.transactionId(),
                event.taskId(),
                event.occurredAt());

        if (event.taskId() == null || event.eventType() == null) {
            log.warn("[AUDIT][RABBITMQ-IN][IGNORED] Evento de pago incompleto: {}", event);
            return;
        }

        taskRepository.findById(event.taskId()).ifPresentOrElse(task -> {
            TaskPaymentStatus previousPaymentStatus = task.getPaymentStatus();
            TaskPaymentStatus newPaymentStatus = switch (event.eventType()) {
                case "PaymentAuthorizedEvent" -> TaskPaymentStatus.RETAINED;
                case "FundsReleasedEvent" -> TaskPaymentStatus.RELEASED;
                case "PaymentFailedEvent" -> TaskPaymentStatus.FAILED;
                case "PaymentRefundedEvent" -> TaskPaymentStatus.REFUNDED;
                default -> previousPaymentStatus;
            };

            task.setPaymentStatus(newPaymentStatus);
            taskRepository.save(task);

            log.info("[AUDIT][PAYMENT-SYNC] taskId={} eventType={} transactionId={} paymentStatus={}->{} reason={}",
                    task.getTaskId(),
                    event.eventType(),
                    event.transactionId(),
                    previousPaymentStatus,
                    newPaymentStatus,
                    event.reason());
        }, () -> log.warn("[AUDIT][RABBITMQ-IN][NOT-FOUND] No existe la tarea taskId={} para aplicar eventType={}",
                event.taskId(), event.eventType()));
    }
}