package br.com.uboard.common;

import br.com.uboard.exception.UboardJsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomObjectMapperTest {

    private CustomObjectMapper customObjectMapper;

    @BeforeEach
    void init() {
        this.customObjectMapper = new CustomObjectMapper();
    }

    @Test
    @DisplayName("Should convert an object in a JSON string")
    void shouldConvertAnObjectInJsonString() throws UboardJsonProcessingException {
        JsonTestObject jsonTestObject = new JsonTestObject("name");
        String jsonString = this.customObjectMapper.toJson(jsonTestObject);
        assertNotNull(jsonString);
    }

    @Test
    @DisplayName("Should convert a JSON string in an object")
    void shouldConvertJsonStringInObject() throws UboardJsonProcessingException {
        JsonTestObject jsonTestObject = this.customObjectMapper.fromJson("{\"name\":\"name\"}", JsonTestObject.class);
        assertNotNull(jsonTestObject);
    }

    @Test
    @DisplayName("Should throw exception when is trying to convert an invalid object")
    void shouldThrowExceptionWhenIsTryingToConvertAnInvalidObject() {
        assertThrows(UboardJsonProcessingException.class, () -> this.customObjectMapper.toJson(new Object()));
    }

    @Test
    @DisplayName("Should throw exception when is trying to convert an invalid JSON")
    void shouldThrowExceptionWhenIsTryingToConvertAnInvalidJson() {
        assertThrows(UboardJsonProcessingException.class, () -> this.customObjectMapper.fromJson("{\"name\":\"name\"", JsonTestObject.class));
    }

    @Test
    @DisplayName("Should convert a JSON string in a type reference object")
    void shouldConvertJsonStringInTypeReferenceObject() throws UboardJsonProcessingException {
        String jsonString = "{\"key\":\"value\"}";
        TypeReference<Map<String, String>> typeReference = new TypeReference<>() {
        };

        Map<String, String> response = this.customObjectMapper.fromJson(jsonString, typeReference);
        assertNotNull(response);
    }

    @Test
    @DisplayName("Should throw exception when is trying to convert an invalid JSON in type reference")
    void shouldThrowExceptionWhenIsTryingToConvertAnInvalidJsonInTypeReference() {
        String jsonString = "{ invalid json }";
        TypeReference<Map<String, String>> typeReference = new TypeReference<>() {
        };

        assertThrows(UboardJsonProcessingException.class, () -> this.customObjectMapper.fromJson(jsonString, typeReference));
    }

    protected record JsonTestObject(String name) {
    }
}