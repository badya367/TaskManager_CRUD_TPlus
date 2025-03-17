package org.tplus.taskManager.taskManager_crud.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tplus.taskManager.taskManager_crud.dto.TaskDto;
import org.tplus.taskManager.taskManager_crud.dto.TaskStatus;
import org.tplus.taskManager.taskManager_crud.dto.TaskStatusUpdateDto;
import org.tplus.taskManager.taskManager_crud.model.Task;

import static org.junit.jupiter.api.Assertions.*;

public class TaskMapperTest {

    private final TaskMapper taskMapper = new TaskMapper();

    private Task task;
    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Тестовая задача");
        task.setDescription("Тестовое описание");
        task.setUserId(10L);
        task.setStatus(TaskStatus.NEW);

        taskDto = new TaskDto(1L, "Тестовая задача", "Тестовое описание", 10L, TaskStatus.NEW);
    }

    @Test
    void toTaskDto_ShouldMapTaskToTaskDto() {
        TaskDto mappedDto = taskMapper.toTaskDto(task);

        assertNotNull(mappedDto);
        assertEquals(task.getId(), mappedDto.getId());
        assertEquals(task.getTitle(), mappedDto.getTitle());
        assertEquals(task.getDescription(), mappedDto.getDescription());
        assertEquals(task.getUserId(), mappedDto.getUserId());
        assertEquals(task.getStatus(), mappedDto.getStatus());
    }

    @Test
    void toTask_ShouldMapTaskDtoToTask() {
        Task mappedTask = taskMapper.toTask(taskDto);

        assertNotNull(mappedTask);
        assertEquals(taskDto.getId(), mappedTask.getId());
        assertEquals(taskDto.getTitle(), mappedTask.getTitle());
        assertEquals(taskDto.getDescription(), mappedTask.getDescription());
        assertEquals(taskDto.getUserId(), mappedTask.getUserId());
        assertEquals(taskDto.getStatus(), mappedTask.getStatus());
    }

    @Test
    void toStatusUpdateDto_ShouldMapTaskToTaskStatusUpdateDto() {
        TaskStatusUpdateDto statusUpdateDto = taskMapper.toStatusUpdateDto(task);

        assertNotNull(statusUpdateDto);
        assertEquals(task.getId(), statusUpdateDto.getId());
        assertEquals(task.getStatus(), statusUpdateDto.getStatus());
    }
}