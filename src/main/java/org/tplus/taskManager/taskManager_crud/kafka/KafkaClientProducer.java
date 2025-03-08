package org.tplus.taskManager.taskManager_crud.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Kafka-продюсер для отправки сообщений в брокер Kafka.
 * <p>
 * Этот класс отвечает за отправку сообщений в Kafka, как в топик по умолчанию, так и в произвольный топик.
 * Вся отправка логируется, при этом в случае ошибки отправки логируется соответствующая информация.
 * </p>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-03-03
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaClientProducer {
    /**
     * Шаблон для отправки сообщений в Kafka.
     */
    private final KafkaTemplate template;

    /**
     * Отправляет сообщение с идентификатором задачи в топик по умолчанию.
     * <p>
     * Идентификатор отправляется как значение сообщения, а в качестве ключа используется случайный UUID.
     * </p>
     *
     * @param id идентификатор задачи
     */
    public void send(Long id) {
        template.sendDefault(UUID.randomUUID().toString(), id)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error("Ошибка отправки сообщения с id={}: {}", id, exception);
                    } else {
                        log.info("Сообщение с id={} успешно отправлено в партицию ", id);
                    }
                });
    }

    /**
     * Отправляет произвольный объект в указанный топик.
     * <p>
     * Сообщение отправляется в заданный топик, после чего вызывается метод {@code flush()} для немедленной отправки.
     * </p>
     *
     * @param topic имя топика
     * @param o     объект, который будет отправлен в виде значения сообщения
     */
    public void sendTo(String topic, Object o) {
        try {
            template.send(topic, o)
                    .whenComplete((result, exception) -> {
                        if (exception != null) {
                            log.error("Ошибка отправки сообщения в топик {} {}", topic, exception);
                        } else {
                            log.info("Сообщение успешно отправлено в топик {}: {}", topic, result);
                        }
                    });
            template.flush();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
