package dh.backend.users.infrastructure.persistence.repository;

import dh.backend.users.domain.model.user.UserStatus;
import dh.backend.users.domain.repository.user.UserRepository;
import dh.backend.users.infrastructure.persistence.entity.UserEntity;

import java.util.UUID;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao implements UserRepository {

    private final UserJpaORM jpa;

    public UserDao(UserJpaORM jpa) {
        this.jpa = jpa;
    }

    public UserEntity save(UserEntity user) {
        try {

            UserEntity userEntity = this.jpa.save(user);
            return userEntity;

        } catch (Exception e) {
            if (e instanceof DataIntegrityViolationException) {
                throw new AmqpRejectAndDontRequeueException(e);
            }

            throw e;
        }
    }

    @Override
    public void updateStatus(UUID userId, UserStatus status) {
        UserEntity user = this.jpa.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(status);
        this.jpa.save(user);
    }

}
