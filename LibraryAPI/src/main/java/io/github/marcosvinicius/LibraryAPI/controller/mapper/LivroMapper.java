package io.github.marcosvinicius.LibraryAPI.controller.mapper;

import io.github.marcosvinicius.LibraryAPI.controller.dto.CadastroLivroDTO;
import io.github.marcosvinicius.LibraryAPI.controller.dto.ResponseCadastroLivroDTO;
import io.github.marcosvinicius.LibraryAPI.model.Livro;
import io.github.marcosvinicius.LibraryAPI.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = AutorMapper.class)
public abstract class LivroMapper {

    @Autowired
    AutorRepository autorRepository;


    @Mapping(target = "autor", expression =
            "java( autorRepository.findById(dto.id_autor()).orElse(null) )")
    public abstract Livro cadastroDtoToEntity(CadastroLivroDTO dto);

    /*
     * Mapeamento da entidade Livro para DTO de resposta.
     *
     * Livro possui um campo do tipo autor e a resposta um campo do tipo CadastroAutorDTO
     *
     * - Como os tipos são diferentes (Autor -> CadastroAutorDTO),
     *   o MapStruct utiliza automaticamente o AutorMapper (definido em 'uses')
     *   para realizar a conversão entre os objetos.
     *
     * - O atributo 'uses = AutorMapper.class' no @Mapper informa ao MapStruct
     *   que ele pode delegar esse mapeamento para outro mapper quando necessário.
     */

    public abstract ResponseCadastroLivroDTO entityToReponseCadastroLivroDto(Livro livro);


}
