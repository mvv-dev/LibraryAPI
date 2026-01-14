package io.github.marcosvinicius.LibraryAPI.controller.dto;

public record ErroCampos(
        String campo,
        String mensagem
) {
}
