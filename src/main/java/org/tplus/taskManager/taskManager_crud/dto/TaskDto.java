package org.tplus.taskManager.taskManager_crud.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    /**
     * Уникальный идентификатор задачи.
     * Генерируется автоматически при сохранении в базу данных.
     */
    private Long id;
    /**
     * Название задачи.
     * Определяет краткое описание задачи.
     */
    @NotBlank(message = "Название не может быть пустым")
    private String title;
    /**
     * Подробное описание задачи.
     * Может содержать дополнительную информацию о задаче.
     */
    private String description;
    /**
     * Идентификатор пользователя, которому принадлежит задача.
     * Используется для связи задачи с конкретным пользователем.
     */
    private Long userId;
    /**
     * Статус задачи
     */
    private TaskStatus status;

}
