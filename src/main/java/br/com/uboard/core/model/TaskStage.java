package br.com.uboard.core.model;

import br.com.uboard.core.model.enums.TaskOperationStageEnum;
import br.com.uboard.core.model.enums.TaskStatusEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class TaskStage extends BaseEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskStage.class);

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    private Integer priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(32)")
    private TaskStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    private TaskOperationStageEnum stage;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(columnDefinition = "TEXT")
    private String error;

    @Column(columnDefinition = "TEXT")
    private String cause;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(columnDefinition = "boolean default false")
    private boolean sensitivePayload;

    @Column(columnDefinition = "TEXT")
    private String result;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;

    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL)
    private List<TaskStagePayloadInjection> payloadInjections = new ArrayList<>();

    public TaskStage() {
    }

    public TaskStage(String description, TaskOperationStageEnum stage, String payload) {
        setUuid(UUID.randomUUID().toString());
        this.description = description;
        this.status = TaskStatusEnum.CREATED;
        this.stage = stage;
        this.payload = payload;
        this.sensitivePayload = false;
        this.createdAt = LocalDateTime.now();
    }

    public TaskStage(String description, TaskOperationStageEnum stage, String payload, boolean sensitivePayload) {
        this(description, stage, payload);
        this.sensitivePayload = sensitivePayload;
    }

    public void cleanPayloadSensitiveData() {
        if (this.isSensitivePayload()) {
            this.payload = null;
        }
    }

    public TaskStage addPayloadInjection(TaskStagePayloadInjection inputInjection) {
        inputInjection.setTarget(this);
        this.payloadInjections.add(inputInjection);
        return this;
    }

    public String getPayloadWithPayloadInjections() {
        String currentPayload = this.payload;
        if (this.payloadInjections.isEmpty()) {
            return currentPayload;
        }

        for (TaskStagePayloadInjection payloadInjection : this.payloadInjections) {
            try {
                currentPayload = payloadInjection.injectAttributesInTargetPayload(this.payload);
            } catch (JsonProcessingException e) {
                LOGGER.error(e.getMessage());
            }
        }

        return currentPayload;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public TaskStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TaskStatusEnum status) {
        this.status = status;
    }

    public TaskOperationStageEnum getStage() {
        return stage;
    }

    public void setStage(TaskOperationStageEnum stage) {
        this.stage = stage;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public boolean isSensitivePayload() {
        return sensitivePayload;
    }

    public void setSensitivePayload(boolean sensitivePayload) {
        this.sensitivePayload = sensitivePayload;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public List<TaskStagePayloadInjection> getPayloadInjections() {
        return payloadInjections;
    }
}
