package br.com.uboard.core.model;

import br.com.uboard.core.model.enums.MilestoneStateEnum;
import br.com.uboard.core.model.enums.SynchronizeMilestoneFrequencyEnum;
import br.com.uboard.core.model.external.GitMilestoneInterface;
import br.com.uboard.core.model.operations.SynchronizeMilestoneInDatabaseForm;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Milestone extends BaseEntity {

    @Column(nullable = false)
    private Long providerId;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50)")
    private MilestoneStateEnum state;

    @Column(nullable = false)
    private LocalDate createdAt;

    private LocalDate finishedAt;

    @Column(nullable = false)
    private LocalDateTime synchronizedAt;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @OneToMany(mappedBy = "milestone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Issue> issues = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isAutoSync;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50)")
    private SynchronizeMilestoneFrequencyEnum frequency;

    private Integer hours;

    private Integer minutes;

    private Integer weekDay;

    public Milestone() {

    }

    public Milestone(SynchronizeMilestoneInDatabaseForm form, GitMilestoneInterface milestoneForSynchronization, Organization organization) {
        this.setUuid(UUID.randomUUID().toString());
        this.setProviderId(milestoneForSynchronization.getId());
        this.setTitle(milestoneForSynchronization.getTitle());
        this.setState(milestoneForSynchronization.getState() != null ? MilestoneStateEnum.valueOf(milestoneForSynchronization.getState().toUpperCase()) : null);
        this.setCreatedAt(milestoneForSynchronization.getStartDate());
        this.setFinishedAt(milestoneForSynchronization.getDueDate());
        this.setOrganization(organization);
        this.setSynchronizedAt(LocalDateTime.now());
        this.setAutoSync(form.isAutoSync());
        this.setFrequency(form.frequency() != null ? form.frequency() : SynchronizeMilestoneFrequencyEnum.NONE);
        this.setHours(form.hours());
        this.setMinutes(form.minutes());
        this.setWeekDay(form.weekDay());
    }

    public String configureSynchronizationFrequency() {
        if (this.getFrequency() != null) {
            return switch (this.getFrequency()) {
                case EVERY_30_MINUTES, EVERY_3_HOURS, EVERY_6_HOURS, EVERY_12_HOURS -> this.getFrequency().getCron();
                case EVERY_DAY -> this.getFrequency().configureEveryDayCron(this.getMinutes(), this.getHours());
                case ONCE_A_WEEK ->
                        this.getFrequency().configureOncePerWeekCron(this.getMinutes(), this.getHours(), this.getWeekDay());
                default -> null;
            };
        }
        return null;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MilestoneStateEnum getState() {
        return state;
    }

    public void setState(MilestoneStateEnum state) {
        this.state = state;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDate finishedAt) {
        this.finishedAt = finishedAt;
    }

    public LocalDateTime getSynchronizedAt() {
        return synchronizedAt;
    }

    public void setSynchronizedAt(LocalDateTime synchronizedAt) {
        this.synchronizedAt = synchronizedAt;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public boolean isAutoSync() {
        return isAutoSync;
    }

    public void setAutoSync(boolean autoSync) {
        isAutoSync = autoSync;
    }

    public SynchronizeMilestoneFrequencyEnum getFrequency() {
        return frequency;
    }

    public void setFrequency(SynchronizeMilestoneFrequencyEnum frequency) {
        this.frequency = frequency;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(Integer weekDay) {
        this.weekDay = weekDay;
    }
}
