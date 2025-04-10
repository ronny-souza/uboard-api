package br.com.uboard.core.model.operations;

import br.com.uboard.core.model.enums.TaskOperationEnum;
import br.com.uboard.core.model.transport.SessionUserDTO;

public class CreateTaskForm {
    private TaskOperationEnum operation;
    private final Object payload;
    private String detail;
    private SessionUserDTO sessionUser;

    private CreateTaskForm(TaskOperationEnum operation, Object payload, String detail, SessionUserDTO sessionUser) {
        this.operation = operation;
        this.payload = payload;
        this.detail = detail;
        this.sessionUser = sessionUser;
    }

    public static CreateTaskForm newInstance(TaskOperationEnum operation,
                                             Object payload,
                                             String detail,
                                             SessionUserDTO sessionUser) {
        return new CreateTaskForm(operation, payload, detail, sessionUser);
    }

    public TaskOperationEnum getOperation() {
        return operation;
    }

    public Object getPayload() {
        return payload;
    }

    public String getDetail() {
        return detail;
    }

    public SessionUserDTO getSessionUser() {
        return sessionUser;
    }
}
