package org.tplus.taskManager.taskManager_crud.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tplus.taskManager.taskManager_crud.dto.TaskDto;
import org.tplus.taskManager.taskManager_crud.dto.TaskStatus;
import org.tplus.taskManager.taskManager_crud.kafka.KafkaClientProducer;
import org.tplus.taskManager.taskManager_crud.mapper.TaskMapper;
import org.tplus.taskManager.taskManager_crud.model.Task;
import org.tplus.taskManager.taskManager_crud.repository.TaskRepository;
import org.tplus.taskManager.taskManager_crud.services.TaskService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private KafkaClientProducer kafkaClientProducer;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        task = new Task(1L, "Тестовая задача", "Тестовое описание", 1L, TaskStatus.NEW);
        taskDto = new TaskDto(1L, "Тестовая задача", "Тестовое описание", 1L, TaskStatus.NEW);
    }

    @Test
    @DisplayName("Должен вернуть список всех задач")
    void getAllTasks_ShouldReturnTaskList() {
        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.toTaskDto(task)).thenReturn(taskDto);

        List<TaskDto> result = taskService.getAllTasks();

        assertEquals(1, result.size());
        assertEquals(taskDto, result.get(0));
    }

    @Test
    @DisplayName("Должен вернуть задачу по ID")
    void getTaskById_ShouldReturnTaskDto() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toTaskDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(taskDto, result);
    }

    @Test
    @DisplayName("Должен выбрасывать EntityNotFoundException, если задача не найдена")
    void getTaskById_ShouldThrowExceptionWhenNotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.getTaskById(999L));
    }

    @Test
    @DisplayName("Должен создать и вернуть новую задачу")
    void createTask_ShouldSaveAndReturnTaskDto() {
        when(taskMapper.toTask(taskDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toTaskDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.createTask(taskDto);

        assertNotNull(result);
        assertEquals(taskDto, result);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Должен обновить и вернуть обновленную задачу")
    void updateTask_ShouldUpdateTaskAndReturnUpdatedDto() {
        TaskDto updateDto = new TaskDto(1L, "Обновленная задача",
                "Обновленное описание",
                2L,
                TaskStatus.IN_PROGRESS);
        Task updatedTask = new Task(1L, "Обновленная задача",
                "Обновленное описание",
                2L,
                TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toTaskDto(any(Task.class))).thenReturn(updateDto);
        when(taskMapper.toTask(updateDto)).thenReturn(updatedTask);
        when(taskRepository.save(updatedTask)).thenReturn(updatedTask);

        TaskDto result = taskService.updateTask(1L, updateDto);

        assertNotNull(result);
        assertEquals(updateDto, result);
        verify(taskRepository, times(1)).save(updatedTask);
    }

    @Test
    @DisplayName("Должен удалить задачу по ID")
    void deleteTask_ShouldDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Должен выбрасывать EntityNotFoundException при удалении, если задача не найдена")
    void deleteTask_ShouldThrowExceptionWhenNotFound() {
        when(taskRepository.existsById(999L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> taskService.deleteTask(999L));
    }
}