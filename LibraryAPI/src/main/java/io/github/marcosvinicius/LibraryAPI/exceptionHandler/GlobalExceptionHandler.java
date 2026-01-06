package io.github.marcosvinicius.LibraryAPI.exceptionHandler;

import io.github.marcosvinicius.LibraryAPI.controller.dto.ErroCampos;
import io.github.marcosvinicius.LibraryAPI.controller.dto.ErroSimples;
import io.github.marcosvinicius.LibraryAPI.controller.dto.ErroUnprocessableEntity;
import io.github.marcosvinicius.LibraryAPI.exceptions.RegistroDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<ErroSimples> handleRegistroDuplicadoException(RegistroDuplicadoException e) {

       var erroSimplesConflito = ErroSimples.conflito();

       return ResponseEntity.status(erroSimplesConflito.code()).body(erroSimplesConflito);

   }

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ErroUnprocessableEntity> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

       List<FieldError> fieldErrors = e.getFieldErrors();
       List<ErroCampos> erroCampos = fieldErrors
               .stream()
               .map(fe -> new ErroCampos(fe.getField(), fe.getDefaultMessage()))
               .toList();

       var erroUnprocessableDTO = new ErroUnprocessableEntity(HttpStatus.valueOf(422).value(),
               "Erro de validação nos seguintes campos: ", erroCampos);

       return ResponseEntity.status(erroUnprocessableDTO.status()).body(erroUnprocessableDTO);

   }

   @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroSimples> handleDefaultErros(RuntimeException e) {

        ErroSimples erroSimplesDto = new ErroSimples(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro inesperado. Entre em contato com a administração.");

        return ResponseEntity.internalServerError().body(erroSimplesDto);

   }

}
