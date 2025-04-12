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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
        this.progress = 0;
    }

    public Task(CreateTaskForm form, User user) {
        this.uuid = UUID.randomUUID().toString();
        this.user = user;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TaskOperationEnum getOperation() {
        return operation;
    }

    public void setOperation(TaskOperationEnum operation) {
        this.operation = operation;
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

    public void setDetail(String detail) {
        this.detail = detail;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public List<TaskStage> getStages() {
        return stages;
    }

    public void setStages(List<TaskStage> stages) {
        this.stages = stages;
    }
}
