package dh.backend.users.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import dh.backend.users.domain.model.user.User;
import dh.backend.users.domain.model.user.UserStatus;

@Setter
@Getter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone", nullable = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Column(name = "dni", nullable = true)
    private String dni;

    protected UserEntity() {
        // for JPA
    }

    public static UserEntity fromDomain(User user) {
        return new UserEntity(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhone(),
                user.getStatus(), user.getDni());
    }

    public UserEntity(UUID id, String email, String firstName, String lastName, String phone, UserStatus status,
            String dni) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.status = status;
        this.dni = dni;

    }
}
