package br.com.uboard.core.model;

import br.com.uboard.core.model.enums.TaskOperationStageEnum;
import br.com.uboard.core.model.enums.TaskStatusEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TaskStageTest {

    @Test
    @DisplayName("Should return an instance from the empty constructor with the data filled in via setters")
    void shouldReturnAnInstanceFromTheEmptyConstructorWithTheDataFilledInViaSetters() {
        Task taskAsMock = mock(Task.class);
        LocalDateTime currentDate = LocalDateTime.now();

        TaskStage taskStage = this.createInstance(taskAsMock, currentDate);

        assertNotNull(taskStage);
        assertEquals(1L, taskStage.getId());
        assertEquals("uuid", taskStage.getUuid());
        assertEquals("description", taskStage.getDescription());
        assertEquals(1, taskStage.getPriority());
        assertEquals(TaskStatusEnum.CREATED, taskStage.getStatus());
        assertEquals(TaskOperationStageEnum.PERSIST_GIT_CREDENTIAL_IN_DATABASE, taskStage.getStage());
        assertEquals("payload", taskStage.getPayload());
        assertEquals("error", taskStage.getError());
        assertEquals("cause", taskStage.getCause());
        assertEquals(taskAsMock, taskStage.getTask());
        assertFalse(taskStage.isSensitivePayload());
        assertEquals("result", taskStage.getResult());
        assertEquals(currentDate, taskStage.getCreatedAt());
        assertEquals(currentDate, taskStage.getFinishedAt());
    }

    @Test
    @DisplayName("Should return an instance from the constructor without sensitive payload")
    void shouldReturnAnInstanceFromTheConstructorWithoutSensitivePayload() {
        TaskStage taskStage = new TaskStage(
                "description",
                TaskOperationStageEnum.PERSIST_GIT_CREDENTIAL_IN_DATABASE,
                "payload"
        );

        assertNotNull(taskStage);
        assertNotNull(taskStage.getUuid());
        assertEquals("description", taskStage.getDescription());
        assertEquals(TaskStatusEnum.CREATED, taskStage.getStatus());
        assertEquals(TaskOperationStageEnum.PERSIST_GIT_CREDENTIAL_IN_DATABASE, taskStage.getStage());
        assertEquals("payload", taskStage.getPayload());
        assertFalse(taskStage.isSensitivePayload());
        assertNotNull(taskStage.getCreatedAt());
    }

    @Test
    @DisplayName("Should return an instance from the constructor with sensitive payload")
    void shouldReturnAnInstanceFromTheConstructorWithSensitivePayload() {
        TaskStage taskStage = new TaskStage(
                "description",
                TaskOperationStageEnum.PERSIST_GIT_CREDENTIAL_IN_DATABASE,
                "payload",
                true
        );

        assertNotNull(taskStage);
        assertNotNull(taskStage.getUuid());
        assertEquals("description", taskStage.getDescription());
        assertEquals(TaskStatusEnum.CREATED, taskStage.getStatus());
        assertEquals(TaskOperationStageEnum.PERSIST_GIT_CREDENTIAL_IN_DATABASE, taskStage.getStage());
        assertEquals("payload", taskStage.getPayload());
        assertTrue(taskStage.isSensitivePayload());
        assertNotNull(taskStage.getCreatedAt());
    }

    @Test
    @DisplayName("Should clear the payload when it is sensitive and clean method is called")
    void shouldClearThePayloadWhenItIsSensitiveAndCleanMethodIsCalled() {
        TaskStage taskStage = new TaskStage(
                "description",
                TaskOperationStageEnum.PERSIST_GIT_CREDENTIAL_IN_DATABASE,
                "payload",
                true
        );

        assertNotNull(taskStage);
        assertEquals("payload", taskStage.getPayload());

        taskStage.cleanPayloadSensitiveData();
        assertNull(taskStage.getPayload());
    }

    private TaskStage createInstance(Task taskAsMock, LocalDateTime currentDate) {
        TaskStage taskStage = new TaskStage();
        taskStage.setId(1L);
        taskStage.setUuid("uuid");
        taskStage.setDescription("description");
        taskStage.setPriority(1);
        taskStage.setStatus(TaskStatusEnum.CREATED);
        taskStage.setStage(TaskOperationStageEnum.PERSIST_GIT_CREDENTIAL_IN_DATABASE);
        taskStage.setPayload("payload");
        taskStage.setError("error");
        taskStage.setCause("cause");
        taskStage.setTask(taskAsMock);
        taskStage.setSensitivePayload(false);
        taskStage.setResult("result");
        taskStage.setCreatedAt(currentDate);
        taskStage.setFinishedAt(currentDate);
        return taskStage;
    }
}