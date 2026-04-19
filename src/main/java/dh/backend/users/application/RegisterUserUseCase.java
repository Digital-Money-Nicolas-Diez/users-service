package dh.backend.users.application;

import dh.backend.users.domain.clients.AccountClient;
import dh.backend.users.domain.model.user.User;
import dh.backend.users.domain.model.user.UserStatus;
import dh.backend.users.domain.repository.user.UserRepository;

import java.util.UUID;

import org.springframework.stereotype.Service;


@Service
public class RegisterUserUseCase {

    private final UserRepository repository;
    private final AccountClient accountClient;

    public RegisterUserUseCase(UserRepository repository, AccountClient accountClient) {
        this.repository = repository;
        this.accountClient = accountClient;
    }

    private User createUser(User user) {
        this.repository.save(user);
        return user;
    }

    private void createAccount(User user) {
        try {
            final UUID userId = user.getId();
            accountClient.createAccount(userId);
        } catch (Exception e) {
            this.repository.updateStatus(user.getId(), UserStatus.ACCOUNT_FAILED);
        }
    }

    public void execute(User userPayload) {
        User user = this.createUser(userPayload);
        this.createAccount(user);
    }
}
