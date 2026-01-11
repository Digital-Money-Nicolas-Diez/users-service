package dh.backend.users.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

import dh.backend.users.domain.model.user.User;
import dh.backend.users.domain.model.user.UserStatus;

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
    private Long dni;

    protected UserEntity() {
        // for JPA
    }

    public UserEntity(User user) {
        this.id = Objects.requireNonNull(user.getId(),"id field is invalid" );
        this.email = Objects.requireNonNull(user.getEmail(),"email field is invalid" );
        this.firstName = Objects.requireNonNull(user.getFirstName(),"firstName field is invalid" );
        this.lastName = Objects.requireNonNull(user.getLastName(),"lastName field is invalid" );
        this.status = Objects.requireNonNull(user.getStatus(),"status field is invalid");
        this.dni = user.getDni();
        this.phone = user.getPhone();
    }
}
