package br.com.uboard.command;

import br.com.uboard.exception.UboardApplicationException;

import java.util.Optional;

public interface TaskStageCommand {

    Optional<Object> execute(String payload) throws UboardApplicationException;
}
