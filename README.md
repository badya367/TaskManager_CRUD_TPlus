# CRUD приложение + логи через аспекты

1. Создать простой RESTful сервис для управления задачами:

Task(id, title, description,userId)

1. POST /tasks — создание новой задачи.

2. GET /tasks/{id} — получение задачи по ID.

3. PUT /tasks/{id} — обновление задачи.

4. DELETE /tasks/{id} — удаление задачи.

5. GET /tasks — получение списка всех задач.

3. Релизуйте класс аспект, со следующими advice:

1. Before

2. AfterThrowing

3. AfterReturning

4. Around (замер выполнения)