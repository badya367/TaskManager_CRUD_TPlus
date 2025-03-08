# CRUD приложение TaskManager

## Задание №1

### Создать простой RESTful сервис для управления задачами:

Task(id, title, description,userId)

1. POST /tasks — создание новой задачи.

2. GET /tasks/{id} — получение задачи по ID.

3. PUT /tasks/{id} — обновление задачи.

4. DELETE /tasks/{id} — удаление задачи.

5. GET /tasks — получение списка всех задач.

### Реализовать класс аспект, со следующими advice:

1. Before

2. AfterThrowing

3. AfterReturning

4. Around (замер выполнения)

## Задание №2 - Добавление Kafka

1. Использовать docker-compose для установки Kafka в Docker
2. Сконфигурировать Kafka, Producer, Consumer в вашем сервисе рабты с Task.
3. Продюсер пишет в топик id и новый статус task у которых он изменился, при соответствующем входящем запросе (обновления task).
4. Консьюмер слушает этот топик, читает оттуда сообщения, и отправляет в NotificationService, этот класс должен отправлять уведомление на почту о смене статуса, полученном от консьюмера
5. Использовать spring-boot-starter-mail, для отправки email в NotificationService


# Запуск приложения:

```
mvn clean package
docker-compose up --build
```
