package br.com.uboard.core.model;

import br.com.uboard.core.model.enums.TaskOperationEnum;
import br.com.uboard.core.model.enums.TaskStatusEnum;
import br.com.uboard.core.model.operations.CreateTaskForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskTest {

    @Test
    @DisplayName("Should return an instance from the empty constructor with the data filled in via setters")
    void shouldReturnAnInstanceFromTheEmptyConstructorWithTheDataFilledInViaSetters() {
        User userAsMock = mock(User.class);
        LocalDateTime currentDate = LocalDateTime.now();

        Task task = new Task();
        task.setId(1L);
        task.setUuid("uuid");
        task.setUser(userAsMock);
        task.setOperation(TaskOperationEnum.CREATE_CREDENTIAL);
        task.setStatus(TaskStatusEnum.COMPLETED);
        task.setDetail("detail");
        task.setProgress(100);
        task.setCreatedAt(currentDate);
        task.setFinishedAt(currentDate);
        task.setStages(new ArrayList<>());

        assertNotNull(task);
        assertTrue(task.getStages().isEmpty());
        assertEquals(1L, task.getId());
        assertEquals("uuid", task.getUuid());
        assertEquals(userAsMock, task.getUser());
        assertEquals(TaskOperationEnum.CREATE_CREDENTIAL, task.getOperation());
        assertEquals(TaskStatusEnum.COMPLETED, task.getStatus());
        assertEquals("detail", task.getDetail());
        assertEquals(100, task.getProgress());
        assertEquals(currentDate, task.getCreatedAt());
        assertEquals(currentDate, task.getFinishedAt());
    }

    @Test
    @DisplayName("Should return an instance from the form args constructor")
    void shouldReturnAnInstanceFromTheFormArgsConstructor() {
        CreateTaskForm formAsMock = mock(CreateTaskForm.class);
        User userAsMock = mock(User.class);

        when(formAsMock.getOperation()).thenReturn(TaskOperationEnum.CREATE_CREDENTIAL);
        when(formAsMock.getDetail()).thenReturn("detail");

        Task task = new Task(formAsMock, userAsMock);
        assertNotNull(task);
        assertNotNull(task.getUuid());
        assertEquals(userAsMock, task.getUser());
        assertEquals(TaskOperationEnum.CREATE_CREDENTIAL, task.getOperation());
        assertEquals(TaskStatusEnum.CREATED, task.getStatus());
        assertEquals("detail", task.getDetail());
        assertEquals(0, task.getProgress());
        assertNotNull(task.getCreatedAt());
        assertTrue(task.getStages().isEmpty());
    }

    @Test
    @DisplayName("Should calculate the percentage of completion")
    void shouldCalculateThePercentageOfCompletion() {
        TaskStage taskStageAsFirstMock = mock(TaskStage.class);
        TaskStage taskStageAsSecondMock = mock(TaskStage.class);
        List<TaskStage> stagesAsMocks = List.of(taskStageAsFirstMock, taskStageAsSecondMock);

        when(taskStageAsFirstMock.getStatus()).thenReturn(TaskStatusEnum.COMPLETED);
        when(taskStageAsSecondMock.getStatus()).thenReturn(TaskStatusEnum.RUNNING);

        Task task = new Task();
        task.setStages(stagesAsMocks);

        assertEquals(0, task.getProgress());
        task.calculateProgress();
        assertEquals(50, task.getProgress());
    }
}