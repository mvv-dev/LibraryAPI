package io.github.marcosvinicius.LibraryAPI.controller;

import io.github.marcosvinicius.LibraryAPI.controller.dto.CadastroAutorDTO;
import io.github.marcosvinicius.LibraryAPI.controller.dto.ResponseCadastroAutorDTO;
import io.github.marcosvinicius.LibraryAPI.controller.mapper.AutorMapper;
import io.github.marcosvinicius.LibraryAPI.model.Autor;
import io.github.marcosvinicius.LibraryAPI.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.Servlet;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
@Tag(name = "Autores")
@Slf4j // Acesso ao objeto log do lombok
public class AutorController implements GenericController {

    private final AutorService service;
    private final AutorMapper mapper;

    @PostMapping
    @Operation(summary = "Cadastrar", description = "Cadastrar um novo autor - Acesso: Somente GERENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com Sucesso"),
            @ApiResponse(responseCode = "422", description = "Erro validação"),
            @ApiResponse(responseCode = "409", description = "Autor já cadastrado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão")
    })
    public ResponseEntity<ResponseCadastroAutorDTO> cadastrar(@RequestBody @Valid CadastroAutorDTO autorDTO) {


        log.info("Tentativa de cadastro de autor: Nome: {}", autorDTO.nome());

        var autor = mapper.toEntity(autorDTO);
        var responseCadastroDto = service.cadastrar(autor);

        log.info("Autor cadastrado com sucesso: Nome: {}", autorDTO.nome());

        URI location = gerarHeaderLocation(autor.getId());


        return ResponseEntity.created(location).body(responseCadastroDto);

    }

    @GetMapping("/{id}")
    @PostMapping
    @Operation(summary = "Buscar", description = "Buscar Autor por ID - Acesso: GERENTE e OPERADOR")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor Encontrado"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
    })
    public ResponseEntity<ResponseCadastroAutorDTO> buscaSimples(@PathVariable String id) {

        log.info("Tentativa de busca por ID (Autor) | ID: {}", id);

        var autor = service.buscarPorId(id);
        ResponseCadastroAutorDTO responseDTO = mapper.toResponseCadastroAutorDTO(autor);

        log.info("Busca realizado com sucesso | Autor: {}", autor.getNome());

        return ResponseEntity.ok().body(responseDTO);


    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar", description = "Deleta um autor existente - Acesso: Somente GERENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Autor deletado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Autor possui livro cadastrados"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado")
    })
    public ResponseEntity<Void> deletar(@PathVariable("id") String id) {

        log.warn("Tentativa de delatar autor | id: {} ", id);
        service.deletarPorId(id);
        log.info("Autor deletado com sucesso!");

        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar", description = "Atualiza os dados de um autor existente - Acesso: Somente GERENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Autor atualizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Erro validação"),
            @ApiResponse(responseCode = "409", description = "Autor já cadastrado"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão")
    })
    public ResponseEntity<Void> atualizar(@RequestBody @Valid CadastroAutorDTO autorDTO, @PathVariable String id) {

        log.warn("Tentativa de atualizar autor | id: {} ", id);
        service.atualizarPorId(id, mapper.toEntity(autorDTO));
        log.info("Autor atualizado com sucesso");

        return ResponseEntity.noContent().build();

    }

    @GetMapping
    @Operation(summary = "Pesquisar", description = "Realiza pesquisa de autores por parâmetros - Acesso: GERENTE e OPERADOR")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pesquisa bem sucedida"),
    })
    public ResponseEntity<List<ResponseCadastroAutorDTO>> buscarPorNomeNacionalidade(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionaliade) {

        log.info("Iniciando pesquisa de autor");

        return ResponseEntity.ok().body(
                service.buscarPorNomeNacionalidade(nome, nacionaliade).stream().map(
                        mapper::toResponseCadastroAutorDTO
                ).collect(Collectors.toList())
        );


    }



}
