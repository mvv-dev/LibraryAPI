package io.github.marcosvinicius.LibraryAPI.controller.dto;

import io.github.marcosvinicius.LibraryAPI.model.Genero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CadastroLivroDTO(

        @NotBlank(message = "Campo obrigatório")
        @ISBN
        @Size(max = 20, message = "Isbn excede 20 caracteres")
        String isbn,

        @NotBlank(message = "Campo obrigatório")
        @Size(max = 80, message = "Título excede 80 caracteres")
        String titulo,

        @NotNull(message = "Campo obrigatório")
        @Past(message = "Data de publicação não pode ser futura")
        LocalDate dataPublicacao,

        @NotNull(message = "Campo obrigatório")
        Genero genero,

        @NotNull(message = "Campo obrigatório")
        BigDecimal preco,

        @NotNull(message = "Campo obrigatório")
        UUID id_autor
) {
}
