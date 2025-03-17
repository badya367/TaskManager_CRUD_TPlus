package org.tplus.taskManager.taskManager_crud;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.tplus.taskManager.taskManager_crud.config.TestContainersConfig;

@SpringBootTest
public class TaskManagerCrudApplicationTests extends TestContainersConfig {
    @Test
    @DisplayName("Тест инициализации контекста")
    void contextLoads(){
    }
}
