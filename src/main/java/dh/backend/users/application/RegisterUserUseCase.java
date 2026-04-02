package dh.backend.users.application;

import dh.backend.users.domain.model.user.User;
import dh.backend.users.domain.model.user.UserStatus;
import dh.backend.users.domain.repository.user.UserRepository;
import dh.backend.users.infrastructure.adapter.UserAdapter;
import dh.backend.users.infrastructure.persistence.entity.UserEntity;
import dh.backend.users.infrastructure.web.clients.FeignAccountClient;
import dh.backend.users.infrastructure.web.dto.CreateAccountDto;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.Retry;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import tools.jackson.core.JacksonException;

@Service
public class RegisterUserUseCase {

    private final UserRepository repository;
    private final UserAdapter adapter;
    private final FeignAccountClient accountClient;
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;

    public RegisterUserUseCase(UserRepository repository, UserAdapter adapter, FeignAccountClient accountClient,
                               CircuitBreakerRegistry circuitBreakerRegistry, RetryRegistry retryRegistry) {
        this.repository = repository;
        this.adapter = adapter;
        this.accountClient = accountClient;
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("accountService");
        this.retry = retryRegistry.retry("accountService");
    }

    public void execute(String userRaw) {
        UserEntity user = this.createUser(userRaw);
        this.createAccount(user);
    }

    private UserEntity createUser(String userRaw) {
        User domain = this.adapter.jsonToUser(userRaw);
        UserEntity entity = UserEntity.fromDomain(domain);
        this.repository.save(entity);
        return entity;
    }

    private void createAccount(UserEntity user) {
        try {
            final UUID userId = user.getId();
            CreateAccountDto dto = new CreateAccountDto(userId);
            Runnable runabble = CircuitBreaker.decorateRunnable(circuitBreaker, () -> accountClient.createAccount(dto));
            Retry.decorateRunnable(this.retry, runabble).run();
        } catch (Exception e) {
            this.repository.updateStatus(user.getId(), UserStatus.ACCOUNT_FAILED);
        }

    }


}
