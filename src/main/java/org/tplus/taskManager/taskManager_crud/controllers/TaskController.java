package org.tplus.taskManager.taskManager_crud.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tplus.taskManager.taskManager_crud.aspects.LogExecutionTime;
import org.tplus.taskManager.taskManager_crud.dto.TaskDto;
import org.tplus.taskManager.taskManager_crud.dto.TaskStatusUpdateDto;
import org.tplus.taskManager.taskManager_crud.kafka.KafkaClientProducer;
import org.tplus.taskManager.taskManager_crud.services.TaskService;

import java.util.List;

/**
 * Контроллер для обработки HTTP-запросов, связанных с сущностью {@link TaskDto}.
 *
 * <p>Этот контроллер предоставляет CRUD-операции для управления задачами.
 * Все методы логируются с использованием {@code SLF4J} и аннотации {@code @Slf4j}.
 * Также для измерения времени выполнения некоторых методов используется аннотация {@link LogExecutionTime}.</p>
 *
 * <p>Аннотации:</p>
 * <ul>
 *     <li>{@code @RestController} – указывает, что этот класс является REST-контроллером</li>
 *     <li>{@code @RequestMapping("/tasks")} – задает базовый URL для всех эндпоинтов контроллера</li>
 *     <li>{@code @Slf4j} – включает логирование с использованием SLF4J</li>
 * </ul>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-02-22
 */
@RestController
@RequestMapping("/tasks")
@Slf4j
public class TaskController {
    /**
     * Сервис для работы с задачами.
     */
    private final TaskService taskService;

    /**
     * Конструктор контроллера с внедрением зависимости {@link TaskService}.
     *
     * @param taskService сервис для управления задачами
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Получает список всех задач.
     *
     * @return список задач
     */
    @GetMapping
    @ResponseBody
    @LogExecutionTime
    public List<TaskDto> getAllTasks() {
        return taskService.getAllTasks();
    }

    /**
     * Получает задачу по ее идентификатору.
     *
     * @param id идентификатор задачи
     * @return найденная задача
     */
    @GetMapping("/{id}")
    @LogExecutionTime
    public TaskDto getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    /**
     * Создает новую задачу.
     *
     * @param task объект задачи, переданный в теле запроса
     * @return созданная задача
     */
    @PostMapping
    @LogExecutionTime
    public TaskDto createTask(@RequestBody TaskDto task) {
        return taskService.createTask(task);
    }

    /**
     * Обновляет существующую задачу по ее идентификатору.
     *
     * @param id   идентификатор задачи
     * @param task объект задачи с обновленными данными
     * @return обновленная задача
     */
    @PutMapping("/{id}")
    @LogExecutionTime
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskDto task) {
        return taskService.updateTask(id, task);
    }
    /**
     * Удаляет задачу по ее идентификатору.
     *
     * @param id идентификатор задачи
     */
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
