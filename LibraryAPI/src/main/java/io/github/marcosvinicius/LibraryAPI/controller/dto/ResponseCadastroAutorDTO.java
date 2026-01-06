package io.github.marcosvinicius.LibraryAPI.controller.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ResponseCadastroAutorDTO (
        String nome,
        LocalDate dataNascimento,
        String nacionalidade,
        LocalDateTime dataCadastro
){
}
