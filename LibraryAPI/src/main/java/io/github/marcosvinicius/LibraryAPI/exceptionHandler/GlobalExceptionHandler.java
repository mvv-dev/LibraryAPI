package io.github.marcosvinicius.LibraryAPI.exceptionHandler;

import io.github.marcosvinicius.LibraryAPI.controller.dto.ErroCampos;
import io.github.marcosvinicius.LibraryAPI.controller.dto.ErroSimples;
import io.github.marcosvinicius.LibraryAPI.controller.dto.ErroUnprocessableEntity;
import io.github.marcosvinicius.LibraryAPI.exceptions.IllegalArgumentException;
import io.github.marcosvinicius.LibraryAPI.exceptions.ItemNotFoundException;
import io.github.marcosvinicius.LibraryAPI.exceptions.OperacaoNaoPermitda;
import io.github.marcosvinicius.LibraryAPI.exceptions.RegistroDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<ErroSimples> handleRegistroDuplicadoException(RegistroDuplicadoException e) {

       var erroSimplesConflito = new ErroSimples(HttpStatus.CONFLICT.value(), e.getMessage());

       return ResponseEntity.status(erroSimplesConflito.status()).body(erroSimplesConflito);

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

   @ExceptionHandler(IllegalArgumentException.class)
   public ResponseEntity<ErroSimples> handleCampoInvalidoException(IllegalArgumentException e) {

        ErroSimples erroSimples = new ErroSimples(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.badRequest().body(erroSimples);

   }

   @ExceptionHandler(ItemNotFoundException.class)
   public ResponseEntity<ErroSimples> handleItemNotFound(ItemNotFoundException e) {
        ErroSimples erroSimples = new ErroSimples(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroSimples);
   }

   @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroSimples> handleDefaultErros(RuntimeException e) {

        ErroSimples erroSimplesDto = new ErroSimples(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro inesperado. Entre em contato com a administração.");

        return ResponseEntity.internalServerError().body(erroSimplesDto);

   }

   @ExceptionHandler(OperacaoNaoPermitda.class)
    public ResponseEntity<ErroSimples> handleOperacaoNaoPermitida(OperacaoNaoPermitda e) {
        ErroSimples erroSimplesDto = new ErroSimples(HttpStatus.CONFLICT.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erroSimplesDto);
   }

   @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErroSimples> handleUsernameNotFoundException(UsernameNotFoundException e) {
        ErroSimples erroSimplesDto = new ErroSimples(HttpStatus.UNAUTHORIZED.value(),
                e.getMessage());

        return ResponseEntity.status(erroSimplesDto.status()).body(erroSimplesDto);
   }

   @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroSimples> handleBadCredentialsException(BadCredentialsException e) {
        ErroSimples erroSimplesDto = new ErroSimples(HttpStatus.UNAUTHORIZED.value(),
                "Erro! Credenciais inválidas");

        return ResponseEntity.status(erroSimplesDto.status()).body(erroSimplesDto);
   }

}
