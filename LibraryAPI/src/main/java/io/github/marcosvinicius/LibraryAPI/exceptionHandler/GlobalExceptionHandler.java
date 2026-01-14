package io.github.marcosvinicius.LibraryAPI.exceptionHandler;

import io.github.marcosvinicius.LibraryAPI.controller.dto.ErroCampos;
import io.github.marcosvinicius.LibraryAPI.controller.dto.ErroSimples;
import io.github.marcosvinicius.LibraryAPI.controller.dto.ErroUnprocessableEntity;
import io.github.marcosvinicius.LibraryAPI.exceptions.IllegalArgumentException;
import io.github.marcosvinicius.LibraryAPI.exceptions.ItemNotFoundException;
import io.github.marcosvinicius.LibraryAPI.exceptions.OperacaoNaoPermitda;
import io.github.marcosvinicius.LibraryAPI.exceptions.RegistroDuplicadoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<ErroSimples> handleRegistroDuplicadoException(RegistroDuplicadoException e) {

        log.error("Erro de registro duplicado: {} ", e.getMessage());

        var erroSimplesConflito = new ErroSimples(HttpStatus.CONFLICT.value(), e.getMessage());
        return ResponseEntity.status(erroSimplesConflito.status()).body(erroSimplesConflito);

   }

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ErroUnprocessableEntity> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

       log.error("Erro de validação: {} ", e.getMessage());

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

       log.error("Erro campo inválido: {} ", e.getMessage());
        ErroSimples erroSimples = new ErroSimples(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.badRequest().body(erroSimples);

   }

   @ExceptionHandler(ItemNotFoundException.class)
   public ResponseEntity<ErroSimples> handleItemNotFound(ItemNotFoundException e) {

       log.error("Erro item not found: {} ", e.getMessage());
        ErroSimples erroSimples = new ErroSimples(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroSimples);
   }

   @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroSimples> handleDefaultErros(RuntimeException e) {

        log.error("Erro inesperado", e);

        ErroSimples erroSimplesDto = new ErroSimples(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro inesperado. Entre em contato com a administração.");

        return ResponseEntity.internalServerError().body(erroSimplesDto);

   }

   @ExceptionHandler(OperacaoNaoPermitda.class)
    public ResponseEntity<ErroSimples> handleOperacaoNaoPermitida(OperacaoNaoPermitda e) {

       log.error("Operação não permitida: {} ", e.getMessage());
        ErroSimples erroSimplesDto = new ErroSimples(HttpStatus.CONFLICT.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erroSimplesDto);
   }

   @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErroSimples> handleUsernameNotFoundException(UsernameNotFoundException e) {

       log.error("Usuário não encotrado: {} ", e.getMessage());
        ErroSimples erroSimplesDto = new ErroSimples(HttpStatus.UNAUTHORIZED.value(),
                e.getMessage());

        return ResponseEntity.status(erroSimplesDto.status()).body(erroSimplesDto);
   }

   @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroSimples> handleBadCredentialsException(BadCredentialsException e) {
       log.error("Erro credenciais inválidas: {} ", e.getMessage());
        ErroSimples erroSimplesDto = new ErroSimples(HttpStatus.UNAUTHORIZED.value(),
                "Erro! Credenciais inválidas");

        return ResponseEntity.status(erroSimplesDto.status()).body(erroSimplesDto);
   }


}
