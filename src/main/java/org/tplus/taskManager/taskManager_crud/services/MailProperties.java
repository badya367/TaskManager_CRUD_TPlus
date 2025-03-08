package org.tplus.taskManager.taskManager_crud.services;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Конфигурационные свойства для email-уведомлений о задачах.
 * <p>
 * Этот класс позволяет загружать параметры из application.yml или application.properties
 * с префиксом {@code task-notification.mail}.
 * </p>
 * <p>
 * Пример конфигурации в application.yml:
 * <pre>
 * task-notification:
 *   mail:
 *     recipient: "example@domain.com"
 *     subject: "Уведомление о задаче"
 * </pre>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-03-02
 */
@Data
@ConfigurationProperties(prefix = "task-notification.mail")
public class MailProperties {

    /**
     * Адрес получателя уведомлений.
     */
    private String recipient;

    /**
     * Тема письма.
     */
    private String subject;
}
