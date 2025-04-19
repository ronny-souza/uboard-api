package br.com.uboard.builder;

import br.com.uboard.core.model.Task;
import br.com.uboard.core.model.TaskStage;
import br.com.uboard.core.model.transport.SessionUserDTO;

import java.util.ArrayList;
import java.util.List;

public class TaskBuilder {

    private int priority;
    private final Task task;
    private final SessionUserDTO sessionUser;
    private final List<TaskStage> stages;

    private TaskBuilder(Task task, SessionUserDTO sessionUser) {
        this.priority = 0;
        this.task = task;
        this.sessionUser = sessionUser;
        this.stages = new ArrayList<>();
    }

    public static TaskBuilder startWithTaskAndUser(Task task, SessionUserDTO sessionUser) {
        return new TaskBuilder(task, sessionUser);
    }

    public void withStage(TaskStage taskStage) {
        this.priority++;
        taskStage.setPriority(this.priority);
        taskStage.setTask(this.task);
        this.stages.add(taskStage);
    }

    public int getPriority() {
        return priority;
    }

    public List<TaskStage> getStages() {
        return stages;
    }

    public SessionUserDTO getSessionUser() {
        return sessionUser;
    }
}
