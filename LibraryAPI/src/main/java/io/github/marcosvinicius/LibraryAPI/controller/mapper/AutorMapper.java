package io.github.marcosvinicius.LibraryAPI.controller.mapper;

import io.github.marcosvinicius.LibraryAPI.controller.dto.CadastroAutorDTO;
import io.github.marcosvinicius.LibraryAPI.controller.dto.ResponseCadastroAutorDTO;
import io.github.marcosvinicius.LibraryAPI.model.Autor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    Autor toEntity(CadastroAutorDTO cadastroAutorDTO);

    ResponseCadastroAutorDTO toResponseCadastroAutorDTO(Autor autor);

}
