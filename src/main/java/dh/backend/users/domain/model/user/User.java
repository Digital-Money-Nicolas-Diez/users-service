package dh.backend.users.domain.model.user;
import java.util.UUID;

import lombok.Getter;

@Getter
public class User {

    private final UUID id;

    private final String email;
    private final String firstName;
    private final String lastName;
    private final String phone;

    private final String dni;
    private final UserStatus status;

    public User(UUID id, String email, String firstName, String lastName, String dni, UserStatus status, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dni = dni;
        this.phone = phone;
        this.status = status != null ? status : UserStatus.PENDING_PROFILE;
    }
}
