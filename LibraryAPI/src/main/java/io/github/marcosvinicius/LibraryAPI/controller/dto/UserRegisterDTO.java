package io.github.marcosvinicius.LibraryAPI.controller.dto;

import io.github.marcosvinicius.LibraryAPI.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRegisterDTO(
        @NotBlank(message = "Campo Obrigatório")
        @Size(max = 80, message = "Login excede 80 caracteres")
        @Email(message = "Email Inválido")
        String login,
        @Size(max = 255, message = "Login excede 255 caracteres")
        @NotBlank(message = "Campo Obrigatório")
        String password,
        @NotNull
        UserRole role
) {
}
