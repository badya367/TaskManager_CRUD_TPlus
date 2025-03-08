package org.tplus.taskManager.taskManager_crud.mapper;

import org.springframework.stereotype.Component;
import org.tplus.taskManager.taskManager_crud.dto.TaskDto;
import org.tplus.taskManager.taskManager_crud.dto.TaskStatusUpdateDto;
import org.tplus.taskManager.taskManager_crud.model.Task;

/**
 * Утилитный класс для преобразования между сущностью {@link Task} и различными DTO.
 * <p>
 * Содержит статические методы для конвертации данных, а также метод для проверки изменений статуса.
 * </p>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-03-02
 */
@Component
public class TaskMapper {
    /**
     * Преобразует сущность {@link Task} в {@link TaskDto}.
     *
     * @param task сущность задачи
     * @return DTO задачи
     */
    public TaskDto toTaskDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getUserId(),
                task.getStatus()
        );
    }

    /**
     * Преобразует {@link TaskDto} в сущность {@link Task}.
     *
     * @param taskDto DTO задачи
     * @return сущность задачи
     */
    public Task toTask(TaskDto taskDto) {
        return new Task(
                taskDto.getId(),
                taskDto.getTitle(),
                taskDto.getDescription(),
                taskDto.getUserId(),
                taskDto.getStatus()
        );
    }

    /**
     * Создает {@link TaskStatusUpdateDto} на основе данных задачи {@link Task}.
     * <p>
     * Это DTO используется для отправки события об обновлении статуса задачи.
     * </p>
     *
     * @param task сущность задачи
     * @return DTO обновления статуса задачи
     */
    public TaskStatusUpdateDto toStatusUpdateDto(Task task) {
        return TaskStatusUpdateDto.builder()
                .id(task.getId())
                .status(task.getStatus())
                .build();
    }
}
