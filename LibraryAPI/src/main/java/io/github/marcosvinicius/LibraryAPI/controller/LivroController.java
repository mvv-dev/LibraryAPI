package io.github.marcosvinicius.LibraryAPI.controller;

import io.github.marcosvinicius.LibraryAPI.controller.dto.CadastroLivroDTO;
import io.github.marcosvinicius.LibraryAPI.controller.dto.ResponseCadastroLivroDTO;
import io.github.marcosvinicius.LibraryAPI.controller.mapper.LivroMapper;
import io.github.marcosvinicius.LibraryAPI.model.Genero;
import io.github.marcosvinicius.LibraryAPI.model.Livro;
import io.github.marcosvinicius.LibraryAPI.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
public class LivroController implements GenericController{

    private final LivroService service;
    private final LivroMapper mapper;

    @PostMapping
    public ResponseEntity<ResponseCadastroLivroDTO> cadastrar(@RequestBody @Valid CadastroLivroDTO cadastroLivroDTO) {

        var livroCadastrado = service.cadastrar(cadastroLivroDTO);
        URI location = gerarHeaderLocation(livroCadastrado.getId());

        ResponseCadastroLivroDTO responseCadastroDto = mapper.entityToReponseCadastroLivroDto(livroCadastrado);

        return ResponseEntity.created(location).body(responseCadastroDto);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCadastroLivroDTO> buscarPorId(@PathVariable("id") String id) {

        ResponseCadastroLivroDTO response = service.buscarPorId(id);

        return ResponseEntity.ok().body(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable("id") String id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ResponseCadastroLivroDTO>> buscarPorParametros(
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "nome_autor", required = false) String nomeAutor,
            @RequestParam(value = "ano_publicacao", required = false) Integer anoPublicacao,
            @RequestParam(value = "genero", required = false) Genero genero) {

        List<Livro> listaLivro = service.buscarPorParametros(
                isbn, titulo, nomeAutor, anoPublicacao, genero
        );

        List<ResponseCadastroLivroDTO> listaReponse = listaLivro
                .stream()
                .map(mapper::entityToReponseCadastroLivroDto)
                .toList();

        return ResponseEntity.ok().body(listaReponse);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarPorId(@RequestBody @Valid CadastroLivroDTO livroDTO, @PathVariable("id") String id) {

        service.atualizarPorId(id, livroDTO);

        return ResponseEntity.noContent().build();

    }






}
