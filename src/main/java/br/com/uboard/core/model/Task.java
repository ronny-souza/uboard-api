package br.com.uboard.core.model;

import br.com.uboard.core.model.enums.TaskOperationEnum;
import br.com.uboard.core.model.enums.TaskStatusEnum;
import br.com.uboard.core.model.operations.CreateTaskForm;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String userIdentifier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(32)")
    private TaskOperationEnum operation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(32)")
    private TaskStatusEnum status;

    @Column(nullable = false)
    private String detail;

    @Column(nullable = false)
    private Integer progress;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "task")
    @Cascade(CascadeType.ALL)
    private List<TaskStage> stages = new ArrayList<>();

    public Task() {

    }

    public Task(CreateTaskForm form) {
        this.uuid = UUID.randomUUID().toString();
        this.userIdentifier = form.getSessionUser().id();
        this.operation = form.getOperation();
        this.status = TaskStatusEnum.CREATED;
        this.detail = form.getDetail();
        this.progress = 0;
        this.createdAt = LocalDateTime.now();
    }

    public void calculateProgress() {
        double totalNumberOfStages = this.stages.size();
        double totalNumberOfSuccessfullyStages = this.stages.stream()
                .filter(taskStage -> taskStage.getStatus().equals(TaskStatusEnum.COMPLETED)).count();

        double percentageOfCompletion = ((totalNumberOfSuccessfullyStages * 100) / totalNumberOfStages);
        this.setProgress((int) percentageOfCompletion);
    }

    public String getUuid() {
        return uuid;
    }

    public TaskStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TaskStatusEnum status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public TaskOperationEnum getOperation() {
        return operation;
    }

    public List<TaskStage> getStages() {
        return stages;
    }

    public void setStages(List<TaskStage> stages) {
        this.stages = stages;
    }
}
