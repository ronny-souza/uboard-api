package br.com.uboard.core.model;

import br.com.uboard.core.model.enums.TaskOperationStageEnum;
import br.com.uboard.core.model.enums.TaskStatusEnum;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class TaskStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String uuid;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    private Integer priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(32)")
    private TaskStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "TEXT")
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

    public TaskStage() {
    }

    public TaskStage(String description, TaskOperationStageEnum stage, String payload) {
        this.uuid = UUID.randomUUID().toString();
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

    public String getUuid() {
        return uuid;
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

    public String getPayload() {
        return payload;
    }

    public void setError(String error) {
        this.error = error;
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
}
