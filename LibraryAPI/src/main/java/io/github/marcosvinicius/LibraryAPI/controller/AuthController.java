package io.github.marcosvinicius.LibraryAPI.controller;

import io.github.marcosvinicius.LibraryAPI.config.TokenService;
import io.github.marcosvinicius.LibraryAPI.controller.dto.LoginResponseDTO;
import io.github.marcosvinicius.LibraryAPI.controller.dto.UserLoginDTO;
import io.github.marcosvinicius.LibraryAPI.controller.dto.UserRegisterDTO;
import io.github.marcosvinicius.LibraryAPI.model.User;
import io.github.marcosvinicius.LibraryAPI.repository.UserRepository;
import io.github.marcosvinicius.LibraryAPI.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Autenticação - Acesso: PÚBLICO")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login bem sucedido"),
            @ApiResponse(responseCode = "422", description = "Erro validação"),
    })
    @SecurityRequirements
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid UserLoginDTO userLoginDTO) {

        // Spring, tente autenticar esse usuário com esse login e essa senha
        var usernamePassword = new UsernamePasswordAuthenticationToken(userLoginDTO.login(),
                userLoginDTO.password());

        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));

    }


    @PostMapping("/register")
    @Operation(summary = "Cadastrar", description = "Cadastrar um novo Usuário - Acesso: Somente GERENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Erro validação"),
            @ApiResponse(responseCode = "409", description = "Usuário já existente"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão")
    })
    public ResponseEntity<Void> register(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {

        userService.register(userRegisterDTO);

        return ResponseEntity.ok().build();

    }


}
