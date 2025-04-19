package br.com.uboard.common;

import br.com.uboard.exception.UboardJsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomJsonMapperTest {

    @Test
    @DisplayName("Should build a JSON string")
    void shouldBuildJsonString() throws UboardJsonProcessingException {
        String jsonString = CustomJsonMapper
                .newInstance()
                .addProperty("name", "name")
                .build();

        Assertions.assertNotNull(jsonString);
    }

    @Test
    @DisplayName("Should build a JSON string when adding a custom object")
    void shouldBuildJsonStringWhenAddingCustomObject() throws UboardJsonProcessingException {
        String jsonString = CustomJsonMapper
                .newInstance()
                .addProperty("name", CustomJsonMapper.newInstance().addProperty("value", "value"))
                .build();

        Assertions.assertNotNull(jsonString);
    }

    @Test
    @DisplayName("Should throw exception when JSON processing fails")
    void shouldThrowExceptionWhenJsonProcessingFails() throws UboardJsonProcessingException {
        CustomJsonMapper customJsonMapper = CustomJsonMapper
                .newInstance()
                .addProperty(null, "");
        Assertions.assertThrows(UboardJsonProcessingException.class, customJsonMapper::build);
    }
}