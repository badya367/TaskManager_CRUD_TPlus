package org.tplus.taskManager.taskManager_crud.dto;

import lombok.*;

/**
 * DTO (Data Transfer Object) для передачи информации об обновлении статуса задачи.
 * <p>
 * Используется при отправке данных в Kafka или при передаче статуса задачи между сервисами.
 * </p>
 * <p>
 * Аннотации Lombok автоматически генерируют конструкторы, геттеры, сеттеры и билдер.
 * </p>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-03-03
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusUpdateDto {

    /**
     * Идентификатор задачи.
     */
    private Long id;

    /**
     * Новый статус задачи.
     */
    private TaskStatus status;
}
