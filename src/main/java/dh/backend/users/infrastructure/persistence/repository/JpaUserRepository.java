package dh.backend.users.infrastructure.persistence.repository;

import dh.backend.users.domain.repository.user.UserJpaORM;
import dh.backend.users.domain.repository.user.UserRepository;
import dh.backend.users.infrastructure.persistence.entity.UserEntity;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

@Repository
public class JpaUserRepository implements UserRepository {

    private final UserJpaORM jpa; // Implementacion del ORM (Hibernate)

    public JpaUserRepository(UserJpaORM jpa) {
        this.jpa = jpa;
    }

    // Implementacion del repositorio (dominio)
    public UserEntity save(UserEntity user) {
        try {

            UserEntity userEntity = this.jpa.save(user); // Uso del ORM dentro del repositorio
            return userEntity;

        } catch (Exception e) {
            if (e instanceof DataIntegrityViolationException) {
                throw new AmqpRejectAndDontRequeueException(e);
            }

            throw e;
        }
    }

}
