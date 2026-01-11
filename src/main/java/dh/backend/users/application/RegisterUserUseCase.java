package dh.backend.users.application;

import dh.backend.users.domain.model.user.User;
import dh.backend.users.domain.repository.user.UserRepository;
import dh.backend.users.infrastructure.adapter.UserAdapter;
import dh.backend.users.infrastructure.persistence.entity.UserEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;

@Service
public class RegisterUserUseCase {

    private final UserRepository repository;
    private final UserAdapter adapter;
    Logger log = LoggerFactory.getLogger(RegisterUserUseCase.class);

    public RegisterUserUseCase(UserRepository repository, UserAdapter adapter) {
        this.repository = repository;
        this.adapter = adapter;
    }

    public void execute(String user) {

        try {

            User userModel = this.adapter.jsonToUser(user);
            UserEntity userEntity = new UserEntity(userModel);
            this.repository.save(userEntity);
            
        } catch (Exception e) {

            if (e instanceof MismatchedInputException) {
                throw new AmqpRejectAndDontRequeueException(e);
            }

            throw e;
        }

    }
}
