package org.tplus.taskManager.taskManager_crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tplus.taskManager.taskManager_crud.model.Task;

/**
 * Репозиторий для управления сущностью {@link Task} в базе данных.
 *
 * <p>Этот интерфейс расширяет {@link JpaRepository}, что позволяет использовать
 * встроенные методы CRUD (создание, чтение, обновление, удаление) без необходимости их реализации.</p>
 *
 * <p>Основные возможности, предоставляемые {@code JpaRepository}:</p>
 * <ul>
 *     <li>{@code save()} – сохранение сущности</li>
 *     <li>{@code findById()} – поиск по идентификатору</li>
 *     <li>{@code findAll()} – получение всех записей</li>
 *     <li>{@code deleteById()} – удаление по идентификатору</li>
 * </ul>
 *
 * <p>Дополнительные методы могут быть добавлены при необходимости.</p>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-02-22
 */

public interface TaskRepository extends JpaRepository<Task, Long> {

}
