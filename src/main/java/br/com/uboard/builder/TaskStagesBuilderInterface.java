package br.com.uboard.builder;

import br.com.uboard.exception.UboardApplicationException;

public interface TaskStagesBuilderInterface {

    void configureTaskStages(String payload, TaskBuilder taskBuilder) throws UboardApplicationException;
}
