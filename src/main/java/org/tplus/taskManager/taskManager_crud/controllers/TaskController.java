package org.tplus.taskManager.taskManager_crud.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.tplus.logStarter.my_LogStarter.aspects.annotations.LogExecution;
import org.tplus.logStarter.my_LogStarter.aspects.annotations.LogExecutionHttp;
import org.tplus.logStarter.my_LogStarter.aspects.annotations.LogExecutionTime;
import org.tplus.taskManager.taskManager_crud.dto.TaskDto;
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
    @LogExecution
    @LogExecutionTime
    @LogExecutionHttp
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
    @LogExecution
    @LogExecutionTime
    @LogExecutionHttp
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
    @LogExecution
    @LogExecutionTime
    @LogExecutionHttp
    public TaskDto createTask(@Valid @RequestBody TaskDto task) {return taskService.createTask(task);}

    /**
     * Обновляет существующую задачу по ее идентификатору.
     *
     * @param id   идентификатор задачи
     * @param task объект задачи с обновленными данными
     * @return обновленная задача
     */
    @PutMapping("/{id}")
    @LogExecution
    @LogExecutionTime
    @LogExecutionHttp
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskDto task) {
        return taskService.updateTask(id, task);
    }

    /**
     * Удаляет задачу по ее идентификатору.
     *
     * @param id идентификатор задачи
     */
    @DeleteMapping("/{id}")
    @LogExecution
    @LogExecutionHttp
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
