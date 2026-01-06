package io.github.marcosvinicius.LibraryAPI.service;

import io.github.marcosvinicius.LibraryAPI.controller.dto.ErroSimples;
import io.github.marcosvinicius.LibraryAPI.controller.dto.ResponseCadastroAutorDTO;
import io.github.marcosvinicius.LibraryAPI.controller.mapper.AutorMapper;
import io.github.marcosvinicius.LibraryAPI.exceptions.RegistroDuplicadoException;
import io.github.marcosvinicius.LibraryAPI.model.Autor;
import io.github.marcosvinicius.LibraryAPI.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorMapper mapper;

    private final AutorRepository repository;

    public ResponseCadastroAutorDTO cadastrar(Autor autor) {

        Autor autorProcurado = repository.findByNomeAndDataNascimentoAndNacionalidade(autor.getNome(),
                autor.getDataNascimento(), autor.getNacionalidade()).orElse(null);

        // Validação de autores duplicados

        if (autorProcurado != null) {

            throw new RegistroDuplicadoException("Registro Duplicado, já existe um autor com esses dados!");

        }

        repository.save(autor);
        return mapper.toResponseCadastroAutorDTO(autor);

    }

    public Autor buscarPorId(String id) {

        return repository.findById(UUID.fromString(id)).orElse(null);

    }

    public void deletarPorId(String id) {

        // Adicionar o erro de não poder excluir autores com livros cadastrados

        repository.deleteById(UUID.fromString(id));

    }

    public void atualizarPorId(String id, Autor autor) {

        var autorEncotrado = repository.findById(UUID.fromString(id));

        // Validação de atualização de um registro duplicado


        if (autorEncotrado.isPresent()) {

            var autorJaExistente = repository.findByNomeAndDataNascimentoAndNacionalidade(
                    autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade()
            );

            if (autorJaExistente.isPresent()) {

                throw new RegistroDuplicadoException("Registro Duplicado, já existe um autor com esses dados!");

            }

            autorEncotrado.get().setNome(autor.getNome());
            autorEncotrado.get().setDataNascimento(autor.getDataNascimento());
            autorEncotrado.get().setNacionalidade(autor.getNacionalidade());
            repository.save(autorEncotrado.get());

        }



    }

    public List<Autor> buscarPorNomeNacionalidade(String nome, String nacionalidade) {

        // Pesquisa com filtros dinâmicos

        Autor autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        ExampleMatcher exampleMatcher = ExampleMatcher
                .matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Autor> autorExample = Example.of(autor, exampleMatcher);
        return repository.findAll(autorExample);

    }

}
