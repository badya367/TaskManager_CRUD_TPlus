package org.tplus.taskManager.taskManager_crud.integration;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.tplus.taskManager.taskManager_crud.config.TestContainersConfig;
import org.tplus.taskManager.taskManager_crud.dto.TaskStatus;
import org.tplus.taskManager.taskManager_crud.model.Task;
import org.tplus.taskManager.taskManager_crud.repository.TaskRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskControllerTest extends TestContainersConfig {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskRepository taskRepository;

    private Task task;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();

        task = new Task();
        task.setTitle("Тестовая задача");
        task.setDescription("Тестовое описание");
        task.setUserId(1L);
        task.setStatus(TaskStatus.NEW);
        task = taskRepository.save(task);
    }

    @Test
    @DisplayName("Тест - получения списка всех задач")
    void getAllTasks_TestReturnTaskList() throws Exception {
        Task task2 = new Task();
        task2.setTitle("Тестовая задача 2");
        task2.setDescription("Тестовое описание 2");
        task2.setUserId(1L);
        task2.setStatus(TaskStatus.NEW);
        taskRepository.save(task2);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(taskRepository.count()))
                .andExpect(jsonPath("$[1].title").value("Тестовая задача 2"))
                .andExpect(jsonPath("$[1].description").value("Тестовое описание 2"))
                .andExpect(jsonPath("$[1].userId").value(1L))
                .andExpect(jsonPath("$[1].status").value(TaskStatus.NEW.toString()));
    }
    @Test
    @DisplayName("Тест получение задачи по id")
    void getTaskById_TestReturnTaskDto() throws Exception {
        mockMvc.perform(get("/tasks/{id}", task.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Тестовая задача"))
                .andExpect(jsonPath("$.description").value("Тестовое описание"))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.status").value(TaskStatus.NEW.toString()));
    }

    @Test
    @DisplayName("Тест получение задачи по несуществующему id")
    void getTaskById_TestThrowExceptionWhenNotFound() throws Exception {
        Long taskId = 999999L;
        mockMvc.perform(get("/tasks/{id}", taskId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found"));
    }

    @Test
    @DisplayName("Тест создание задачи")
    void createTask_TestSaveAndReturnTaskDto() throws Exception {
        String requestJson = """
            {
                "title": "Создание: Тестовая задача",
                "description": "Создание: Тестовое описание",
                "userId": 1,
                "status": "NEW"
            }
        """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Создание: Тестовая задача"))
                .andExpect(jsonPath("$.description").value("Создание: Тестовое описание"))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.status").value(TaskStatus.NEW.toString()));
    }

    @Test
    @DisplayName("Тест создание задачи с некорректными статусом")
    void createTask_TestInvalidStatus() throws Exception {
        String requestJson = """
            {
                "title": "Создание: Тестовая задача_ПроверкаСтатуса",
                "description": "Создание: Тестовое описание_ПроверкаСтатуса",
                "userId": 1,
                "status": "NOT_SUPPORTED"
            }
        """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.detail").value(Matchers.
                        containsString("Failed to read request")));
    }

    @Test
    @DisplayName("Тест создание задачи с пустым названием")
    void createTask_TestEmptyTitle() throws Exception {
        String requestJson = """
            {
                "title": "",
                "description": "",
                "userId": null,
                "status": "NEW"
            }
        """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Invalid request content")));
    }

    @Test
    @DisplayName("Тест обновления задачи")
    void updateTask_TestUpdateTaskAndReturnUpdatedDto() throws Exception {
        String updateJson = """
            {
                "title": "Обновлённая тестовая задача",
                "description": "Обновлённое тестовое описание",
                "userId": 2,
                "status": "IN_PROGRESS"
            }
        """;

        mockMvc.perform(put("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Обновлённая тестовая задача"))
                .andExpect(jsonPath("$.description").value("Обновлённое тестовое описание"))
                .andExpect(jsonPath("$.userId").value(2L))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

    }

    @Test
    @DisplayName("Тест обновления задачи с некорректными данными")
    void updateTask_TestInvalidData() throws Exception {
        String updateJson = """
            {
                "title": null,
                "description": "",
                "userId": null,
                "status": ""
            }
        """;

        mockMvc.perform(put("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Failed to read request")));
    }

    @Test
    @DisplayName("Тест обновления несуществующей задачи")
    void updateTask_TestNotFound() throws Exception {
        String updateJson = """
            {
                "title": "Обновлённая несуществующая задача",
                "description": "Обновлённое несуществующее описание",
                "userId": 2,
                "status": "IN_PROGRESS"
            }
        """;

        Long taskId = 999999L;
        mockMvc.perform(put("/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found"));
    }

    @Test
    @DisplayName("Тест удаления задачи")
    void deleteTask_TestDeleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", task.getId()))
                .andExpect(status().isOk());

        Assertions.assertFalse(taskRepository.findById(task.getId()).isPresent());
    }

    @Test
    @DisplayName("Тест удаления несуществующей задачи")
    void deleteTask_TestNotFound() throws Exception {
        Long taskId = 999999L;
        mockMvc.perform(delete("/tasks/{id}", taskId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found"));
    }
}

