package br.com.uboard.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import static br.com.uboard.configuration.RabbitMQConfiguration.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RabbitMQConfigurationTest {

    private RabbitMQConfiguration rabbitMQConfiguration;

    @BeforeEach
    void init() {
        this.rabbitMQConfiguration = new RabbitMQConfiguration();
    }

    @Test
    @DisplayName("Should return correct string values for queues")
    void shouldReturnCorrectStringValuesForQueues() {
        assertEquals("uboard.task-execution-event", UBOARD_TASK_EXECUTION_EVENT);
        assertEquals("uboard.task-completed-event", UBOARD_TASK_COMPLETED_EVENT);
        assertEquals("uboard.task-stage-result-event", UBOARD_TASK_STAGE_RESULT_EVENT);
        assertEquals("uboard.run-task-stage", UBOARD_RUN_TASK_STAGE);
    }

    @Test
    @DisplayName("Should return an instance of RabbitTemplate")
    void shouldReturnAnInstanceOfRabbitTemplate() {
        ConnectionFactory connectionFactoryAsMock = mock(ConnectionFactory.class);
        MessageConverter messageConverterAsMock = mock(MessageConverter.class);

        RabbitTemplate rabbitTemplate = this.rabbitMQConfiguration.rabbitTemplate(
                connectionFactoryAsMock, messageConverterAsMock
        );
        assertNotNull(rabbitTemplate);
    }

    @Test
    @DisplayName("Should return an instance of MessageConverter")
    void shouldReturnAnInstanceOfMessageConverter() {
        MessageConverter messageConverter = this.rabbitMQConfiguration.getMessageConverter();
        assertNotNull(messageConverter);
        assertInstanceOf(Jackson2JsonMessageConverter.class, messageConverter);
    }

    @Test
    @DisplayName("Should return the run task stage queue")
    void shouldReturnTheRunTaskStageQueue() {
        Queue queue = this.rabbitMQConfiguration.runTaskStageQueue();
        assertNotNull(queue);
        assertEquals(UBOARD_RUN_TASK_STAGE, queue.getName());
    }

    @Test
    @DisplayName("Should return the task execution event queue")
    void shouldReturnTheTaskExecutionEventQueue() {
        Queue queue = this.rabbitMQConfiguration.emitTaskExecutionEventQueue();
        assertNotNull(queue);
        assertEquals(UBOARD_TASK_EXECUTION_EVENT, queue.getName());
    }

    @Test
    @DisplayName("Should return the task completion event queue")
    void shouldReturnTheTaskCompletionEventQueue() {
        Queue queue = this.rabbitMQConfiguration.emitTaskCompletionEventQueue();
        assertNotNull(queue);
        assertEquals(UBOARD_TASK_COMPLETED_EVENT, queue.getName());
    }

    @Test
    @DisplayName("Should return the task stage result event queue")
    void shouldReturnTheTaskStageResultEventQueue() {
        Queue queue = this.rabbitMQConfiguration.emitTaskStageResultEventQueue();
        assertNotNull(queue);
        assertEquals(UBOARD_TASK_STAGE_RESULT_EVENT, queue.getName());
    }
}