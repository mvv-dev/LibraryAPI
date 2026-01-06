package io.github.marcosvinicius.LibraryAPI.controller;

import io.github.marcosvinicius.LibraryAPI.controller.dto.CadastroAutorDTO;
import io.github.marcosvinicius.LibraryAPI.controller.dto.ResponseCadastroAutorDTO;
import io.github.marcosvinicius.LibraryAPI.controller.mapper.AutorMapper;
import io.github.marcosvinicius.LibraryAPI.model.Autor;
import io.github.marcosvinicius.LibraryAPI.service.AutorService;
import jakarta.servlet.Servlet;
import lombok.RequiredArgsConstructor;
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
public class AutorController {

    private final AutorService service;
    private final AutorMapper mapper;

    @PostMapping
    public ResponseEntity<ResponseCadastroAutorDTO> cadastrar(@RequestBody CadastroAutorDTO autorDTO) {

        var autor = mapper.toEntity(autorDTO);
        var responseCadastroDto = service.cadastrar(autor);

        URI location = ServletUriComponentsBuilder  // Uri Location
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(autor.getId())
                .toUri();


        return ResponseEntity.created(location).body(responseCadastroDto);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCadastroAutorDTO> buscarSimples(@PathVariable String id) {

        var autor = service.buscarPorId(id);
        ResponseCadastroAutorDTO responseDTO = mapper.toResponseCadastroAutorDTO(autor);

        return ResponseEntity.ok().body(responseDTO);


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {

        service.deletarPorId(id);

        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@RequestBody CadastroAutorDTO autorDTO, @PathVariable String id) {

        service.atualizarPorId(id, mapper.toEntity(autorDTO));
        return ResponseEntity.noContent().build();

    }

    @GetMapping
    public ResponseEntity<List<ResponseCadastroAutorDTO>> buscarPorNomeNacionalidade(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionaliade) {

        return ResponseEntity.ok().body(
                service.buscarPorNomeNacionalidade(nome, nacionaliade).stream().map(
                        mapper::toResponseCadastroAutorDTO
                ).collect(Collectors.toList())
        );


    }



}
