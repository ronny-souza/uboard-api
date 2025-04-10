package br.com.uboard.common;

import br.com.uboard.exception.UboardJsonProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CustomJsonMapper {
    private final ObjectMapper objectMapper;
    private final ObjectNode objectNode;

    private CustomJsonMapper() {
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectNode = JsonNodeFactory.instance.objectNode();
    }

    public static CustomJsonMapper newInstance() {
        return new CustomJsonMapper();
    }

    public CustomJsonMapper addProperty(String property, Object content) throws UboardJsonProcessingException {
        if (content instanceof CustomJsonMapper customJsonMapper) {
            this.objectNode.put(property, customJsonMapper.build());
        } else {
            this.objectNode.putPOJO(property, content);
        }

        return this;
    }

    public String build() throws UboardJsonProcessingException {
        try {
            return this.objectMapper.writeValueAsString(this.objectNode);
        } catch (JsonProcessingException e) {
            throw new UboardJsonProcessingException(e.getMessage());
        }
    }
}
