package dh.backend.users.messages.rabbitmq.consumer;

import dh.backend.users.application.RegisterUserUseCase;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Service
public class UsersRabbitmqConsumer {

    Logger log = LoggerFactory.getLogger(UsersRabbitmqConsumer.class);
    private final RegisterUserUseCase registerUseCase;

    public UsersRabbitmqConsumer(RegisterUserUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    @RabbitListener(queues = "users.register")
    public void consumer(String message) throws IOException {
        registerUseCase.execute(message);
    }

}
