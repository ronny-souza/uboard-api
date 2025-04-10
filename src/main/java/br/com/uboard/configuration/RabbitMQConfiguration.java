package br.com.uboard.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfiguration {

    public static final String UBOARD_TASK_EXECUTION_EVENT = "uboard.task-execution-event";
    public static final String UBOARD_TASK_COMPLETED_EVENT = "uboard.task-completed-event";
    public static final String UBOARD_TASK_STAGE_RESULT_EVENT = "uboard.task-stage-result-event";
    public static final String UBOARD_RUN_TASK_STAGE = "uboard.run-task-stage";

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    MessageConverter getMessageConverter() {
        return new Jackson2JsonMessageConverter(new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
    }

    @Bean
    Queue runTaskStageQueue() {
        return QueueBuilder.durable(UBOARD_RUN_TASK_STAGE).build();
    }

    @Bean
    Queue emitTaskExecutionEventQueue() {
        return QueueBuilder.durable(UBOARD_TASK_EXECUTION_EVENT).build();
    }

    @Bean
    Queue emitTaskCompletionEventQueue() {
        return QueueBuilder.durable(UBOARD_TASK_COMPLETED_EVENT).build();
    }

    @Bean
    Queue emitTaskStageResultEventQueue() {
        return QueueBuilder.durable(UBOARD_TASK_STAGE_RESULT_EVENT).build();
    }
}
