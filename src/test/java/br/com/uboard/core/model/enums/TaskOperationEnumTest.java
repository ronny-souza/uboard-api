package br.com.uboard.core.model.enums;

import br.com.uboard.builder.CreateCredentialTaskStagesBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskOperationEnumTest {

    @Test
    @DisplayName("Should return the create credential stage builder class")
    void shouldReturnTheCreateCredentialStageBuilderClass() {
        assertEquals(CreateCredentialTaskStagesBuilder.class, TaskOperationEnum.CREATE_CREDENTIAL.getTaskStagesBuilder());
    }
}