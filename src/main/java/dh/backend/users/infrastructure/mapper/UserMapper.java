package dh.backend.users.infrastructure.mapper;

import dh.backend.users.domain.model.user.User;
import dh.backend.users.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity fromDomain(User user) {
        return new UserEntity(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhone(),
                user.getStatus(), user.getDni());
    }

}
