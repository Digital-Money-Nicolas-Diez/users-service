package dh.backend.users.infrastructure.web.dto;

import java.util.UUID;

public record CreateAccountDto(
        UUID user
) {}