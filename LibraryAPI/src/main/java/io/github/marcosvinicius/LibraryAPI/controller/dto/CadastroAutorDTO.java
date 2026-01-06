package io.github.marcosvinicius.LibraryAPI.controller.dto;

import java.time.LocalDate;

public record CadastroAutorDTO(
        String nome,
        LocalDate dataNascimento,
        String nacionalidade
) {
}
