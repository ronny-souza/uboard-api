package br.com.uboard.builder;

import br.com.uboard.core.model.Task;
import br.com.uboard.core.model.TaskStage;
import br.com.uboard.core.model.transport.SessionUserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class TaskBuilderTest {

    @Test
    @DisplayName("Should create an instance with default attribute values")
    void shouldCreateAnInstanceWithDefaultAttributeValues() {
        Task taskAsMock = mock(Task.class);
        SessionUserDTO sessionUserDTOAsMock = mock(SessionUserDTO.class);

        TaskBuilder taskBuilder = TaskBuilder.startWithTaskAndUser(taskAsMock, sessionUserDTOAsMock);

        assertNotNull(taskBuilder);
        assertTrue(taskBuilder.getStages().isEmpty());
        assertEquals(0, taskBuilder.getPriority());
        assertEquals(sessionUserDTOAsMock, taskBuilder.getSessionUser());
    }

    @Test
    @DisplayName("Should update attributes when adding a new stage")
    void shouldUpdateAttributesWhenAddingNewStage() {
        Task taskAsMock = mock(Task.class);
        SessionUserDTO sessionUserDTOAsMock = mock(SessionUserDTO.class);
        TaskStage taskStageAsSpy = spy(TaskStage.class);

        TaskBuilder taskBuilder = TaskBuilder.startWithTaskAndUser(taskAsMock, sessionUserDTOAsMock);
        taskBuilder.withStage(taskStageAsSpy);

        assertFalse(taskBuilder.getStages().isEmpty());
        assertEquals(1, taskBuilder.getPriority());
        assertEquals(1, taskBuilder.getStages().getFirst().getPriority());
        assertEquals(taskAsMock, taskBuilder.getStages().getFirst().getTask());
    }
}