package dh.backend.users.domain.clients;

import dh.backend.users.infrastructure.web.dto.CreateAccountDto;

import java.util.UUID;

public interface AccountClient {
    void createAccount(UUID user);
}
