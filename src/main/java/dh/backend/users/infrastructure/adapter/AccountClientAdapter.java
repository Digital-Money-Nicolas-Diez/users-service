package dh.backend.users.infrastructure.adapter;

import dh.backend.users.domain.clients.AccountClient;
import dh.backend.users.infrastructure.web.clients.FeignAccountClient;
import dh.backend.users.infrastructure.web.dto.CreateAccountDto;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountClientAdapter implements AccountClient {
    private final FeignAccountClient feign;
    private final Retry retry;
    private final CircuitBreaker circuitBreaker;


    public AccountClientAdapter(FeignAccountClient feign, CircuitBreakerRegistry circuitBreakerRegistry, RetryRegistry retryRegistry) {
        this.feign = feign;
        this.retry = retryRegistry.retry("accountService");
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("accountService");
    }

    @Override
    public void createAccount(UUID user) {
        CreateAccountDto dto = new CreateAccountDto(user);
        Runnable runnable = CircuitBreaker.decorateRunnable(circuitBreaker, () -> this.feign.createAccount(dto));
        Retry.decorateRunnable(this.retry, runnable).run();
    }
}
