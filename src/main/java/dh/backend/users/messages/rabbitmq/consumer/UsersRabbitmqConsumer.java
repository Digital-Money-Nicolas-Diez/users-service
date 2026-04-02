package dh.backend.users.messages.rabbitmq.consumer;

import dh.backend.users.application.RegisterUserUseCase;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
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

    public UsersRabbitmqConsumer(RegisterUserUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    @RabbitListener(queues = "users.register", ackMode = "MANUAL")
    public void consumer(Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();
        try {
            String messageBody = new String(message.getBody());
            log.info(messageBody);
            registerUseCase.execute(messageBody);
        } finally {
            channel.basicAck(tag, false);

        }

    }

}
