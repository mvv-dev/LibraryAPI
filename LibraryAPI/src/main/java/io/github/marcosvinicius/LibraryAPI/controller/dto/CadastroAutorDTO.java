package io.github.marcosvinicius.LibraryAPI.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CadastroAutorDTO(

        @NotBlank(message = "Campo Obrigatório")
        @Size(max = 100, message = "Campo excede 100 caracteres")
        String nome,
        @NotNull(message = "Campo Obrigatório")
        @Past(message = "Data de nascimento não pode ultrapassar a data atual")
        LocalDate dataNascimento,
        @NotBlank(message = "Campo Obrigatório")
        @Size(max = 60, message = "Campo excede 60 caracteres")
        String nacionalidade

) {
}
