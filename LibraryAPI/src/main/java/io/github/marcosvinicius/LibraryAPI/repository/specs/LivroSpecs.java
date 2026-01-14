package io.github.marcosvinicius.LibraryAPI.repository.specs;

import io.github.marcosvinicius.LibraryAPI.model.Genero;
import io.github.marcosvinicius.LibraryAPI.model.Livro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class LivroSpecs {

    // Configuração das specs

    public static Specification<Livro> isbnEqual(String isbn) {
        return (
                root, query, cb) -> cb.equal(root.get("isbn"), isbn);
    }

    public static Specification<Livro> tituloLike(String titulo) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("titulo")), "%" + titulo.toUpperCase() + "%");
    }

    public static Specification<Livro> generoEqual(Genero genero) {
        return (
                root, query, cb) -> cb.equal(root.get("genero"), genero);
    }

    public static Specification<Livro> anoPublicacaoEqual(Integer anoPublicaco) {
        // select to_char(data_publicacao, 'YYYY') = :anoPublicacao
        // select to_char(data_publicacao, 'YYYY') from livro
        return (
                root, query, cb) -> cb.equal(
                cb.function("to_char", String.class, root.get("dataPublicacao"), cb.literal("YYYY")),
                anoPublicaco.toString());

    }

    public static Specification<Livro> nomeAutorLike(String nome) {

//        select * from livro l
//        join autor a on l.id_autor = a.id
//        where upper(a.nome) like upper ('%%')

//        return (root, query, cb) ->
//                cb.like(cb.upper(root.get("autor").get("nome")), "%" + nome.toUpperCase() + "%");

        return (root, query, criteriaBuilder) -> {

            Join<Object, Object> joinAutor = root.join("autor", JoinType.INNER);
            return criteriaBuilder.like(
                    criteriaBuilder.upper(joinAutor.get("nome")), "%" + nome.toUpperCase() + "%"
            );


        };

    }


}
