package dh.backend.users.domain.clients;

import java.util.UUID;

public interface AccountClient {
    void createAccount(UUID user);
}
