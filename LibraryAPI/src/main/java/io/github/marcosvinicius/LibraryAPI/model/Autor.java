package io.github.marcosvinicius.LibraryAPI.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Data
@EntityListeners(AuditingEntityListener.class) // permite auditoria
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column
    private String nacionalidade;

    @Column(name = "data_cadastro")
    @CreatedDate // Insere a data atual a cada persist
    private LocalDateTime dataCadastro;

    @Column(name = "data_ultima_atualizacao")
    @LastModifiedDate // Insere a data atual a cada update
    private LocalDateTime dataUltimaAtualizacao;

    @OneToMany(mappedBy = "autor") // A relação autor -> livro já é mapeada do outro lado, no atributo autor de Livro.
    private List<Livro> livros;



}
