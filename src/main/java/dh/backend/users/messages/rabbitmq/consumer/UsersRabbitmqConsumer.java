package dh.backend.users.messages.rabbitmq.consumer;

import dh.backend.users.application.RegisterUserUseCase;

import dh.backend.users.domain.model.user.User;
import dh.backend.users.infrastructure.adapter.UserTransform;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import org.springframework.amqp.core.Message;

@Service
public class UsersRabbitmqConsumer {

    Logger log = LoggerFactory.getLogger(UsersRabbitmqConsumer.class);
    private final RegisterUserUseCase registerUseCase;
    private final UserTransform transform;

    public UsersRabbitmqConsumer(RegisterUserUseCase registerUseCase, UserTransform transform) {
        this.registerUseCase = registerUseCase;
        this.transform = transform;
    }

    @RabbitListener(queues = "users.register", ackMode = "MANUAL")
    public void userRegister(Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();
        try {
            String messageBody = new String(message.getBody());
            log.info(messageBody);

            User user = this.transform.jsonToUser(messageBody);
            registerUseCase.execute(user);
        } finally {
            channel.basicAck(tag, false);
        }

    }

}
