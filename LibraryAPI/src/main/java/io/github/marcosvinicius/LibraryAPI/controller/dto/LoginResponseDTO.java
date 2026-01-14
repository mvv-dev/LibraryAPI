package io.github.marcosvinicius.LibraryAPI.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Login Response (JWT Token)")
public record LoginResponseDTO(
        String token
) {
}
