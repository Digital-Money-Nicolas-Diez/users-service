package dh.backend.users.domain.repository.user;
import dh.backend.users.infrastructure.persistence.entity.UserEntity;

public interface UserRepository{
    UserEntity save(UserEntity user);
}

