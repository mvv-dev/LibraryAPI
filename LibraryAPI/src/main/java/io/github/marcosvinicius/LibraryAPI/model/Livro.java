package io.github.marcosvinicius.LibraryAPI.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Data
@EntityListeners(AuditingEntityListener.class)
public class Livro {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String isbn;

    @Column
    private String titulo;

    @Column(name = "data_publicacao")
    private LocalDate dataPublicacao;

    @Column
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Column
    private BigDecimal preco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_autor")
    private Autor autor;

    @Column(name = "data_cadastro")
    @CreatedDate
    private LocalDateTime dataCadastro;

    @Column(name = "data_ultima_atualizacao")
    @LastModifiedDate
    private LocalDateTime dataUltimaAtualizacao;




}
