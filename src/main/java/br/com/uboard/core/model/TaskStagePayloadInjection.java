package br.com.uboard.core.model;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Entity
public class TaskStagePayloadInjection extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "target_id", nullable = false)
    private TaskStage target;

    @ManyToOne
    @JoinColumn(name = "source_id", nullable = false)
    private TaskStage source;

    @Column(nullable = false, length = 128)
    private String targetAttribute;

    @Column(nullable = false, length = 128)
    private String sourceAttribute;

    public TaskStagePayloadInjection() {

    }

    public TaskStagePayloadInjection(TaskStage source, String sourceAttribute, String targetAttribute) {
        setUuid(UUID.randomUUID().toString());
        this.source = source;
        this.sourceAttribute = sourceAttribute;
        this.targetAttribute = targetAttribute;
    }

    public String injectAttributesInTargetPayload(String payload) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JsonNode attributeAsJsonNodeInSource = this.getAttributeAsJsonNodeInSource(objectMapper);
        if (attributeAsJsonNodeInSource == null) {
            return payload;
        }

        JsonNode payloadAsJsonNode = objectMapper.readTree(payload);
        if (!this.targetAttribute.contains(".")) {
            ((ObjectNode) payloadAsJsonNode).set(this.targetAttribute, attributeAsJsonNodeInSource);
        } else {
            String[] attributes = this.getSourceAttribute().split("\\.");
            String attribute = attributes[attributes.length - 1];
            List<String> attributesAsList = Arrays.asList(attributes);
            attributesAsList = attributesAsList.subList(0, attributesAsList.size() - 1);
            payloadAsJsonNode
                    .withObject("/" + String.join("/", attributesAsList))
                    .set(attribute, attributeAsJsonNodeInSource);
        }

        return payloadAsJsonNode.toString();
    }

    private JsonNode getAttributeAsJsonNodeInSource(ObjectMapper objectMapper) throws JsonProcessingException {
        JsonNode sourceResultAsJsonNode = objectMapper.readTree(this.getSource().getResult());
        if (!this.getSourceAttribute().contains(".")) {
            JsonPointer jsonPointer = JsonPointer.compile("/" + this.getSourceAttribute());
            return sourceResultAsJsonNode.at(jsonPointer);
        } else {
            String[] attributes = this.getSourceAttribute().split("\\.");
            JsonPointer jsonPointer = JsonPointer.compile("/" + String.join("/", attributes));
            return sourceResultAsJsonNode.at(jsonPointer);
        }
    }

    public void setTarget(TaskStage target) {
        this.target = target;
    }

    public TaskStage getSource() {
        return source;
    }

    public String getSourceAttribute() {
        return sourceAttribute;
    }
}
