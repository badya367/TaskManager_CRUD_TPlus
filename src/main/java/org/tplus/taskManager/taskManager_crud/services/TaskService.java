package org.tplus.taskManager.taskManager_crud.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tplus.taskManager.taskManager_crud.dto.TaskDto;
import org.tplus.taskManager.taskManager_crud.kafka.KafkaClientProducer;
import org.tplus.taskManager.taskManager_crud.mapper.TaskMapper;
import org.tplus.taskManager.taskManager_crud.model.Task;
import org.tplus.taskManager.taskManager_crud.repository.TaskRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Сервисный класс для управления задачами {@link Task}.
 *
 * <p>Этот класс предоставляет методы для работы с задачами, включая создание, получение,
 * обновление и удаление. Использует {@link TaskRepository} для взаимодействия с базой данных.</p>
 *
 * <p>Аннотация {@code @Service} указывает, что данный класс является сервисом в Spring.</p>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-02-22
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class TaskService {
    /**
     * Репозиторий для работы с задачами в базе данных.
     */
    private final TaskRepository taskRepository;
    /**
     * Маппер для преобразования сущностей Task
     */
    private final TaskMapper taskMapper;
    /**
     * Kafka-продюсер для отправки событий об обновлении статуса задачи.
     */
    private final KafkaClientProducer kafkaClientProducer;
    /**
     * Название Kafka-топика для событий об обновлении статуса задачи.
     */
    @Value("t_plus_tasks_update_status")
    private String updateTopic;

    /**
     * Получает список всех задач.
     *
     * @return список задач
     */
    public List<TaskDto> getAllTasks() {

        return taskRepository.findAll().stream()
                .map(taskMapper::toTaskDto)
                .toList();
    }

    /**
     * Получает задачу по ее идентификатору.
     *
     * @param id идентификатор задачи
     * @return найденная задача
     * @throws RuntimeException если задача не найдена
     */
    public TaskDto getTaskById(Long id) {
        return taskRepository.findById(id).map(taskMapper::toTaskDto)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
    }

    /**
     * Создает новую задачу и сохраняет ее в базе данных.
     *
     * @param task объект задачи
     * @return сохраненная задача
     */
    public TaskDto createTask(TaskDto task) {

        Task saveTask = taskRepository.save(taskMapper.toTask(task));
        return taskMapper.toTaskDto(saveTask);
    }

    /**
     * Обновляет существующую задачу по идентификатору.
     * <p>
     * Если статус задачи изменился, сервис отправляет событие в Kafka.
     * </p>
     *
     * @param id   идентификатор задачи
     * @param task данные для обновления задачи
     * @return обновленная задача в виде DTO
     * @throws NoSuchElementException если задача не найдена
     */
    public TaskDto updateTask(Long id, TaskDto task) {
        TaskDto existingTask = getTaskById(id);

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setUserId(task.getUserId());

        boolean statusChanged = !Objects.equals(existingTask.getStatus(), task.getStatus());
        existingTask.setStatus(task.getStatus());

        taskRepository.save(taskMapper.toTask(existingTask));

        if (statusChanged) {
            try {
                kafkaClientProducer.sendTo(updateTopic, taskMapper.toStatusUpdateDto(taskMapper.toTask(existingTask)));
            } catch (Exception e) {
                log.error("Не удалось отправить событие в Kafka для таски с id={}: {}", id, e.getMessage(), e);
            }
        }

        return existingTask;
    }

    /**
     * Удаляет задачу по ее идентификатору.
     *
     * @param id идентификатор задачи
     * @throws RuntimeException если задача не найдена
     */
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task not found");
        }
        taskRepository.deleteById(id);
    }

}
