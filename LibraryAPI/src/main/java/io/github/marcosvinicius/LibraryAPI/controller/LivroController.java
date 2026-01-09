package io.github.marcosvinicius.LibraryAPI.controller;

import io.github.marcosvinicius.LibraryAPI.controller.dto.CadastroLivroDTO;
import io.github.marcosvinicius.LibraryAPI.controller.dto.ResponseCadastroLivroDTO;
import io.github.marcosvinicius.LibraryAPI.controller.mapper.LivroMapper;
import io.github.marcosvinicius.LibraryAPI.model.Genero;
import io.github.marcosvinicius.LibraryAPI.model.Livro;
import io.github.marcosvinicius.LibraryAPI.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
@Tag(name = "Livros")
public class LivroController implements GenericController{

    private final LivroService service;
    private final LivroMapper mapper;

    @PostMapping
    @Operation(summary = "Cadastrar", description = "Cadastrar um novo livro - Acesso: GERENTE e OPERADOR")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com Sucesso"),
            @ApiResponse(responseCode = "422", description = "Erro validação"),
            @ApiResponse(responseCode = "409", description = "Livro já cadastrado"),
            @ApiResponse(responseCode = "403", description = "Usuário não tem permissão"),
    })
    public ResponseEntity<ResponseCadastroLivroDTO> cadastrar(@RequestBody @Valid CadastroLivroDTO cadastroLivroDTO) {

        var livroCadastrado = service.cadastrar(cadastroLivroDTO);
        URI location = gerarHeaderLocation(livroCadastrado.getId());

        ResponseCadastroLivroDTO responseCadastroDto = mapper.entityToReponseCadastroLivroDto(livroCadastrado);

        return ResponseEntity.created(location).body(responseCadastroDto);

    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar", description = "Buscar um livro por ID - Acesso: GERENTE e OPERADOR")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro encontrado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "403", description = "Usuário não tem permissão"),
    })
    public ResponseEntity<ResponseCadastroLivroDTO> buscarPorId(@PathVariable("id") String id) {

        ResponseCadastroLivroDTO response = service.buscarPorId(id);

        return ResponseEntity.ok().body(response);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar", description = "Deletar um livro por ID - Acesso: GERENTE e OPERADOR")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livro deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "403", description = "Usuário não tem permissão"),
    })
    public ResponseEntity<Void> deletarPorId(@PathVariable("id") String id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Pesquisar", description = "Pesquisar por parâmetros - Acesso: GERENTE e OPERADOR")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pesquisa bem sucedida"),
            @ApiResponse(responseCode = "403", description = "Usuário não tem permissão"),
    })
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
    @Operation(summary = "Atualizar", description = "Atualizar um livro existente - Acesso: GERENTE e OPERADOR")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livro atualizado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário não tem permissão"),
            @ApiResponse(responseCode = "422", description = "Erro validação"),
            @ApiResponse(responseCode = "409", description = "Livro já cadastrado"),
            @ApiResponse(responseCode = "400", description = "Autor do livro inexistente"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<Void> atualizarPorId(@RequestBody @Valid CadastroLivroDTO livroDTO, @PathVariable("id") String id) {

        service.atualizarPorId(id, livroDTO);

        return ResponseEntity.noContent().build();

    }






}
