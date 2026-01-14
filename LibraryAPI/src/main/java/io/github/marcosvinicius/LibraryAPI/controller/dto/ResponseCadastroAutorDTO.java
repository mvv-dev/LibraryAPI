package io.github.marcosvinicius.LibraryAPI.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(name = "Cadastro Autor Response")
public record ResponseCadastroAutorDTO (
        String nome,
        LocalDate dataNascimento,
        String nacionalidade,
        LocalDateTime dataCadastro
){
}
