package io.github.marcosvinicius.LibraryAPI.repository;

import io.github.marcosvinicius.LibraryAPI.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {


}
