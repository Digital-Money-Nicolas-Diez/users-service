package dh.backend.users.infrastructure.web.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import dh.backend.users.infrastructure.config.OAuthFeignInterceptor;
import dh.backend.users.infrastructure.web.dto.CreateAccountDto;

@FeignClient(
    name="accounts-service",
    url="http://localhost:8082",
    configuration = OAuthFeignInterceptor.class
)
public interface FeignAccountClient {

    @PostMapping("/api/accounts")
    void createAccount(@RequestBody CreateAccountDto createAccountDto);

    
}