package io.github.marcosvinicius.LibraryAPI.controller.dto;

import org.springframework.http.HttpStatus;

public record ErroSimples(
        int status,
        String mensagem
) {

    public static ErroSimples conflito_autor() {
        return new ErroSimples(HttpStatus.CONFLICT.value(),
                "Registro Duplicado, já existe um autor com esses dados!");
    }

    public static ErroSimples notFound() {
        return new ErroSimples(HttpStatus.NOT_FOUND.value(),
                "Não foi possível encotrar um autor com esse id");
    }

}
