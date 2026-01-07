package io.github.marcosvinicius.LibraryAPI.repository;


import io.github.marcosvinicius.LibraryAPI.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID>, JpaSpecificationExecutor<Livro> {

    Optional<Livro> findByIsbn(String isbn);

}
