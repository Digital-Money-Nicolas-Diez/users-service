package dh.backend.users.application;

import dh.backend.users.domain.model.user.User;
import dh.backend.users.domain.model.user.UserStatus;
import dh.backend.users.domain.repository.user.UserRepository;
import dh.backend.users.infrastructure.adapter.UserAdapter;
import dh.backend.users.infrastructure.persistence.entity.UserEntity;
import dh.backend.users.infrastructure.web.clients.AccountClient;
import dh.backend.users.infrastructure.web.dto.CreateAccountDto;
import feign.FeignException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;

@Service
public class RegisterUserUseCase {

    private final UserRepository repository;
    private final UserAdapter adapter;
    private final AccountClient accountClient;
    Logger log = LoggerFactory.getLogger(RegisterUserUseCase.class);

    public RegisterUserUseCase(UserRepository repository, UserAdapter adapter, AccountClient accountClient) {
        this.repository = repository;
        this.adapter = adapter;
        this.accountClient = accountClient;
    }

    public void execute(String user) {

        UserEntity savedUser = null;

        try {

            User userModel = this.adapter.jsonToUser(user);
            UserEntity userEntity = UserEntity.fromDomain(userModel);
            savedUser = this.repository.save(userEntity);
            accountClient.createAccount(new CreateAccountDto(savedUser.getId()));

        } catch (FeignException e) {
            log.error("AMQP Error during user registration: {}", e.getMessage());
            int status = e.status();
            if (status >= 400 && status < 500) {
                if (savedUser != null) {
                    this.repository.updateStatus(savedUser.getId(), UserStatus.ACCOUNT_FAILED);
                }

                throw new AmqpRejectAndDontRequeueException(e);
            }

            throw e;
        } catch (MismatchedInputException e) {
            log.error("JSON Mismatched Input Error during user registration: {}", e.getMessage());
            throw new AmqpRejectAndDontRequeueException(e);
        } catch (Exception e) {
            throw e;
        }

    }
}
