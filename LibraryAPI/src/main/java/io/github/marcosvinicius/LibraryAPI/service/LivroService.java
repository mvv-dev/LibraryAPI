package io.github.marcosvinicius.LibraryAPI.service;

import io.github.marcosvinicius.LibraryAPI.controller.dto.CadastroLivroDTO;
import io.github.marcosvinicius.LibraryAPI.controller.dto.ResponseCadastroLivroDTO;
import io.github.marcosvinicius.LibraryAPI.controller.mapper.LivroMapper;
import io.github.marcosvinicius.LibraryAPI.exceptions.IllegalArgumentException;
import io.github.marcosvinicius.LibraryAPI.exceptions.ItemNotFoundException;
import io.github.marcosvinicius.LibraryAPI.exceptions.RegistroDuplicadoException;
import io.github.marcosvinicius.LibraryAPI.model.Genero;
import io.github.marcosvinicius.LibraryAPI.model.Livro;
import io.github.marcosvinicius.LibraryAPI.repository.AutorRepository;
import io.github.marcosvinicius.LibraryAPI.repository.LivroRepository;
import io.github.marcosvinicius.LibraryAPI.repository.specs.LivroSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroMapper mapper;
    private final LivroRepository repository;
    private final AutorRepository autorRepository;

    // Tratado
    public Livro cadastrar(CadastroLivroDTO cadastroLivroDTO) {

        // Verifica se esse autor passado no dto existe no banco
        var autorExistente = autorRepository.findById(cadastroLivroDTO.id_autor()).orElse(null);
        if (autorExistente == null) {
            throw new IllegalArgumentException("Erro, é necessário cadastrar um autor já existente no banco");
        }

        var livro = mapper.cadastroDtoToEntity(cadastroLivroDTO);

        var livroComMesmoIsbn = repository.findByIsbn(livro.getIsbn()).orElse(null);

        // Verifica se o ISBN é duplicado
        if (livroComMesmoIsbn != null) {
            throw new RegistroDuplicadoException("Erro! Já existe um livro com esse ISBN");
        }



        repository.save(livro);
        return livro;


    }

    public ResponseCadastroLivroDTO buscarPorId(String id) {

        var livro = repository.findById(UUID.fromString(id)).orElse(null);
        if (livro == null) {
            throw new ItemNotFoundException("Não existe um livro com esse ID");
        }

        return mapper.entityToReponseCadastroLivroDto(livro);


    }

    // Tratado
    public void deletar(String id) {

        var livroADeletar = repository.findById(UUID.fromString(id)).orElse(null);
        if (livroADeletar == null) {
            throw new ItemNotFoundException("Não existe um livro com esse ID");
        }

        repository.deleteById(UUID.fromString(id));

    }

    public List<Livro> buscarPorParametros(
            String isbn, String titulo, String nomeAutor, Integer anoPublicacao, Genero genero
    ) {

        // Primeira spec, sem nenhum filtro aplicado
        Specification<Livro> specs = Specification.where(
                (root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        // Se o campo não for nulo, a spec desse campo será adicionada
        if (isbn != null) {
            specs = specs.and(LivroSpecs.isbnEqual(isbn));
        }

        if (titulo != null) {
            specs = specs.and(LivroSpecs.tituloLike(titulo));
        }

        if (genero != null) {
            specs = specs.and(LivroSpecs.generoEqual(genero));
        }

        if(anoPublicacao != null) {
            specs = specs.and(LivroSpecs.anoPublicacaoEqual(anoPublicacao));
        }

        if(nomeAutor != null) {
            specs = specs.and(LivroSpecs.nomeAutorLike(nomeAutor));
        }

        return repository.findAll(specs);

    }

    // Tratado
    public void atualizarPorId(String id, CadastroLivroDTO cadastroLivroDTO) {

        var livroEncontrado = repository.findById(UUID.fromString(id)).orElse(null);

        if (livroEncontrado != null) {

            var livroComMesmoIsbn = repository.findByIsbn(cadastroLivroDTO.isbn()).orElse(null);

            if (livroComMesmoIsbn != null) {
                throw new RegistroDuplicadoException("Erro! Já existe um livro com esse ISBN");
            }

            livroEncontrado.setIsbn(cadastroLivroDTO.isbn());
            livroEncontrado.setTitulo(cadastroLivroDTO.titulo());
            livroEncontrado.setDataPublicacao(cadastroLivroDTO.dataPublicacao());
            livroEncontrado.setGenero(cadastroLivroDTO.genero());
            livroEncontrado.setPreco(cadastroLivroDTO.preco());

            // Adicionar validação para caso o autor não exista no banco

            var autorExistente = autorRepository.findById(cadastroLivroDTO.id_autor()).orElse(null);
            if (autorExistente == null) {
                throw new IllegalArgumentException("Erro, é necessário cadastrar um autor já existente no banco");
            }

            livroEncontrado.setAutor(autorRepository.findById(cadastroLivroDTO.id_autor()).get());
            repository.save(livroEncontrado);

        } else {
            throw new ItemNotFoundException("Não existe um livro com esse ID");
        }


    }

}
