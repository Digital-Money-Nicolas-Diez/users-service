package dh.backend.users.domain.repository.user;

import dh.backend.users.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserJpaORM extends JpaRepository<UserEntity, UUID> {}
