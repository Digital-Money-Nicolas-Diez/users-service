package dh.backend.users.infrastructure.errors;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitListenerConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);

        factory.setErrorHandler(error -> {
            System.err.println("❌ Message discarded by ErrorHandler");
            System.err.println(error.getCause() != null
                    ? error.getCause().getMessage()
                    : error.getMessage());
        });

        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}
