package org.tplus.taskManager.taskManager_crud.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс {@code Task} представляет собой сущность задачи, используемую в системе управления задачами.
 *
 * <p>Каждая задача имеет уникальный идентификатор, название, описание и идентификатор пользователя, которому она принадлежит.
 *
 * <p>Этот класс аннотирован для использования с JPA, что позволяет автоматически сохранять его в базе данных.
 * Также используются аннотации Lombok для сокращения количества шаблонного кода (геттеры, сеттеры, конструкторы).
 *
 * <p>Аннотации:
 * <ul>
 *     <li>{@code @Entity} - указывает, что класс является сущностью JPA</li>
 *     <li>{@code @Table(name = "tasks")} - задает имя таблицы в базе данных</li>
 *     <li>{@code @Id} - определяет первичный ключ</li>
 *     <li>{@code @GeneratedValue(strategy = GenerationType.IDENTITY)} - указывает способ генерации идентификаторов</li>
 *     <li>{@code @Getter}, {@code @Setter} - автоматически генерируют геттеры и сеттеры (Lombok)</li>
 *     <li>{@code @NoArgsConstructor}, {@code @AllArgsConstructor} - генерируют конструкторы без параметров и со всеми полями (Lombok)</li>
 * </ul>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-02-22
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    /**
     * Уникальный идентификатор задачи.
     * Генерируется автоматически при сохранении в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Название задачи.
     * Определяет краткое описание задачи.
     */
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
     * статус задачи
     */
    private String status;
}
