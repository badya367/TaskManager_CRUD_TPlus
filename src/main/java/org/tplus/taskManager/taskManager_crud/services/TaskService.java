package org.tplus.taskManager.taskManager_crud.services;

import org.springframework.stereotype.Service;
import org.tplus.taskManager.taskManager_crud.model.Task;
import org.tplus.taskManager.taskManager_crud.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

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
@Service
public class TaskService {
    /**
     * Репозиторий для работы с задачами в базе данных.
     */
    private final TaskRepository taskRepository;

    /**
     * Конструктор для внедрения зависимости {@link TaskRepository}.
     *
     * @param taskRepository репозиторий задач
     */
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Получает список всех задач.
     *
     * @return список задач
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Получает задачу по ее идентификатору.
     *
     * @param id идентификатор задачи
     * @return найденная задача
     * @throws RuntimeException если задача не найдена
     */
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    /**
     * Создает новую задачу и сохраняет ее в базе данных.
     *
     * @param task объект задачи
     * @return сохраненная задача
     */
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    /**
     * Обновляет существующую задачу по ее идентификатору.
     *
     * @param id   идентификатор задачи
     * @param task данные для обновления
     * @return обновленная задача
     * @throws RuntimeException если задача не найдена
     */
    public Task updateTask(Long id, Task task) {
        Optional<Task> existingTask = taskRepository.findById(id);
        if (existingTask.isPresent()) {
            Task updatedTask = existingTask.get();
            updatedTask.setTitle(task.getTitle());
            updatedTask.setDescription(task.getDescription());
            updatedTask.setUserId(task.getUserId());
            return taskRepository.save(updatedTask);
        } else {
            throw new RuntimeException("Task not found");
        }
    }

    /**
     * Удаляет задачу по ее идентификатору.
     *
     * @param id идентификатор задачи
     * @throws RuntimeException если задача не найдена
     */
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }
}
