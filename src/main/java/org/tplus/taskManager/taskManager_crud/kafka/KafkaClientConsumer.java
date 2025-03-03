package org.tplus.taskManager.taskManager_crud.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.tplus.taskManager.taskManager_crud.dto.TaskStatusUpdateDto;
import org.tplus.taskManager.taskManager_crud.services.NotificationService;

import java.util.List;

/**
 * Kafka-консьюмер для обработки событий обновления статуса задач.
 * <p>
 * Этот класс отвечает за получение сообщений из Kafka-топика "t_plus_tasks_update_status".
 * После получения сообщений вызывает {@link NotificationService} для обработки каждого сообщения.
 * </p>
 * <p>
 * Аннотация {@code @KafkaListener} на методе указывает, что этот метод будет вызван при поступлении новых сообщений в указанный топик.
 * </p>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-03-03
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaClientConsumer {

    /**
     * Сервис для отправки уведомлений при обновлении статуса задачи.
     */
    private final NotificationService notificationService;

    /**
     * Обработчик сообщений из Kafka.
     * <p>
     * Получает список сообщений типа {@link TaskStatusUpdateDto}, обрабатывает каждое сообщение и вызывает
     * метод уведомления в {@link NotificationService}.
     * После успешной обработки всех сообщений выполняет ручное подтверждение получения (acknowledge).
     * </p>
     *
     * @param messages список полученных сообщений
     * @param ack      объект для ручного подтверждения получения сообщений
     */
    @KafkaListener(id = "t_plus_tasks_name",
            topics = "t_plus_tasks_update_status",
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload List<TaskStatusUpdateDto> messages, Acknowledgment ack) {
        log.debug("consumer: Обнаружена новая пачка сообщений для обработки. Количество: {}", messages.size());
        try {
            for (TaskStatusUpdateDto message : messages) {
                notificationService.taskUpdateNotification(message);
            }
        } finally {
            ack.acknowledge();
        }
        log.debug("consumer: Пачка сообщений успешно обработана");
    }
}
