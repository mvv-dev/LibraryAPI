package io.github.marcosvinicius.LibraryAPI.exceptions;

public class OperacaoNaoPermitda extends RuntimeException {
    public OperacaoNaoPermitda(String message) {
        super(message);
    }
}
