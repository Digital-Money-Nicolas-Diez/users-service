package dh.backend.users.domain.repository.user;
import java.util.UUID;

import dh.backend.users.domain.model.user.User;
import dh.backend.users.domain.model.user.UserStatus;
import dh.backend.users.infrastructure.persistence.entity.UserEntity;

public interface UserRepository{
    void save(User user);
    void updateStatus(UUID userId, UserStatus status);
}

