package dh.backend.users.infrastructure.persistence.dao;

import dh.backend.users.domain.model.user.User;
import dh.backend.users.domain.model.user.UserStatus;
import dh.backend.users.domain.repository.user.UserRepository;
import dh.backend.users.infrastructure.mapper.UserMapper;
import dh.backend.users.infrastructure.persistence.entity.UserEntity;

import java.util.UUID;

import dh.backend.users.infrastructure.persistence.repository.UserJpa;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao implements UserRepository {

    private final UserJpa jpa;
    private UserMapper mapper;

    public UserDao(UserJpa jpa) {
        this.jpa = jpa;
    }

    public void save(User user) {
        this.jpa.save(this.mapper.fromDomain(user));
    }

    @Override
    public void updateStatus(UUID userId, UserStatus status) {
        UserEntity user = this.jpa.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(status);
        this.jpa.save(user);
    }

}
