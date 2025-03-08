package org.tplus.taskManager.taskManager_crud.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.tplus.taskManager.taskManager_crud.services.MailProperties;

/**
 * Конфигурационный класс для включения поддержки конфигурационных свойств
 * для почтовых уведомлений.
 * <p>
 * Этот класс активирует загрузку свойств из application.yml или application.properties
 * с префиксом {@code task-notification.mail}.
 * </p>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-03-08
 */
@Configuration
@EnableConfigurationProperties(MailProperties.class)
public class MailConfig {
}
