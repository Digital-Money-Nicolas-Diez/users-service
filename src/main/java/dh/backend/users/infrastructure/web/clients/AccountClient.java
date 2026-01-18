package dh.backend.users.infrastructure.web.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import dh.backend.users.infrastructure.config.OAuthFeignConfig;
import dh.backend.users.infrastructure.web.dto.CreateAccountDto;

@FeignClient(
    name="accounts-service",
    url="http://localhost:8082",
    configuration = OAuthFeignConfig.class
)
public interface AccountClient {

    @PostMapping("/api/accounts/create")
    void createAccount(@RequestBody CreateAccountDto createAccountDto);

    
}