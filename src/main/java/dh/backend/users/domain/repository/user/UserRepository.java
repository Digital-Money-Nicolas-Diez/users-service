package dh.backend.users.domain.repository.user;
import java.util.UUID;

import dh.backend.users.domain.model.user.UserStatus;
import dh.backend.users.infrastructure.persistence.entity.UserEntity;

public interface UserRepository{
    UserEntity save(UserEntity user);
    void updateStatus(UUID userId, UserStatus status);
}

