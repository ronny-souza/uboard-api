package br.com.uboard.common;

import br.com.uboard.exception.UboardJsonProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomObjectMapper {

    private final ObjectMapper objectMapper;

    public CustomObjectMapper() {
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String toJson(Object content) throws UboardJsonProcessingException {
        try {
            return this.objectMapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            throw new UboardJsonProcessingException(e.getMessage());
        }
    }

    public <T> T fromJson(String content, Class<T> classType) throws UboardJsonProcessingException {
        try {
            return this.objectMapper.readValue(content, classType);
        } catch (JsonProcessingException e) {
            throw new UboardJsonProcessingException(e.getMessage());
        }
    }
}
