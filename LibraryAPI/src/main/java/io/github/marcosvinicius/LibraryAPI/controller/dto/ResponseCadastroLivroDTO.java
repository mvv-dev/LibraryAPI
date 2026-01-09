package io.github.marcosvinicius.LibraryAPI.controller.dto;

import io.github.marcosvinicius.LibraryAPI.model.Genero;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "Cadastro Livro Response")
public record ResponseCadastroLivroDTO(
        String isbn,
        String titulo,
        LocalDate dataPublicacao,
        Genero genero,
        BigDecimal preco,
        CadastroAutorDTO autor,
        LocalDateTime dataCadastro
) {
}
