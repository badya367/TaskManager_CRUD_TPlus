package org.tplus.taskManager.taskManager_crud.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.tplus.logStarter.my_LogStarter.aspects.annotations.LogExecution;
import org.tplus.taskManager.taskManager_crud.dto.TaskStatusUpdateDto;

/**
 * Сервис для отправки уведомлений по электронной почте при обновлении статуса задачи.
 * <p>
 * Данный сервис получает данные об обновлении задачи и отправляет email-сообщение
 * на указанный адрес с информацией о новом статусе.
 * </p>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-03-03
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {
    /**
     * Компонент для отправки email-сообщений.
     */
    private final JavaMailSender mailSender;
    /**
     * Пользовательские настройки для email-уведомлений (получатель, тема и т.д.).
     */
    private final MailProperties mailProperties;
    /**
     * Настройки почтового сервера из spring-boot (логин отправителя и прочее).
     */
    private final org.springframework.boot.autoconfigure.mail.MailProperties springMailProperties;

    /**
     * Метод для обработки события обновления задачи.
     * <p>
     * Логирует информацию об обновлении и вызывает метод для отправки email.
     * </p>
     *
     * @param taskDto данные о задаче и её новом статусе
     */
    @LogExecution
    public void taskUpdateNotification(TaskStatusUpdateDto taskDto) {
        log.info("Task с id: {}, обновил статус: {}", taskDto.getId(), taskDto.getStatus());
        sendEmail(taskDto);
    }

    /**
     * Отправляет email-уведомление с информацией об обновлении задачи.
     *
     * @param taskDto данные о задаче и её новом статусе
     */
    private void sendEmail(TaskStatusUpdateDto taskDto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(springMailProperties.getUsername());
            helper.setTo(mailProperties.getRecipient());
            helper.setSubject(mailProperties.getSubject());
            helper.setText("Task c id: " + taskDto.getId() + " был обновлён");

            mailSender.send(message);
            log.info("Email успешно отправлен");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}