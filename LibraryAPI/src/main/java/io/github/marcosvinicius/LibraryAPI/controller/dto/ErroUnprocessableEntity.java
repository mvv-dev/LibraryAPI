package io.github.marcosvinicius.LibraryAPI.controller.dto;

import java.util.List;

public record ErroUnprocessableEntity(
        int status,
        String mensagem,
        List<ErroCampos> erros
) {
}
