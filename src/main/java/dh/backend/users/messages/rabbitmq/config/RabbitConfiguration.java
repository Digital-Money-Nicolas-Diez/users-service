package dh.backend.users.messages.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    public static final String KEYCLOAK_EXCHANGE = "keycloak.events";

    public static final String USERS_QUEUE = "users.register";
    public static final String USERS_RETRY_QUEUE = "users.register.retry";
    public static final String USERS_DLQ = "users.register.dlq";

    public static final String USERS_RETRY_EXCHANGE = "users.retry.exchange";
    public static final String USERS_DLX = "users.dlx";

    public static final String ROUTING_KEY = "REGISTER";

    /* ========= EXCHANGES ========= */

    @Bean
    TopicExchange keycloakExchange() {
        return new TopicExchange(KEYCLOAK_EXCHANGE, true, false);
    }

    @Bean
    DirectExchange usersRetryExchange() {
        return new DirectExchange(USERS_RETRY_EXCHANGE);
    }

    @Bean
    DirectExchange usersDlx() {
        return new DirectExchange(USERS_DLX);
    }

    /* ========= QUEUES ========= */

    @Bean
    Queue usersRegisterQueue() {
        return QueueBuilder.durable(USERS_QUEUE)
                .withArgument("x-dead-letter-exchange", USERS_RETRY_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", USERS_RETRY_QUEUE)
                .build();
    }

    @Bean
    Queue usersRegisterRetryQueue() {
        return QueueBuilder.durable(USERS_RETRY_QUEUE)
                .withArgument("x-message-ttl", 5000) 
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", USERS_QUEUE)
                .build();
    }

    @Bean
    Queue usersRegisterDlqQueue() {
        return QueueBuilder.durable(USERS_DLQ).build();
    }

    /* ========= BINDINGS ========= */

    @Bean
    Binding registerBinding() {
        return BindingBuilder
                .bind(usersRegisterQueue())
                .to(keycloakExchange())
                .with(ROUTING_KEY);
    }

    @Bean
    Binding retryBinding() {
        return BindingBuilder
                .bind(usersRegisterRetryQueue())
                .to(usersRetryExchange())
                .with(USERS_RETRY_QUEUE);
    }

    @Bean
    Binding dlqBinding() {
        return BindingBuilder
                .bind(usersRegisterDlqQueue())
                .to(usersDlx())
                .with(USERS_DLQ);
    }
}
