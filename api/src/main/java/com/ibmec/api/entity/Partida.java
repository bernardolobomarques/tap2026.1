package com.ibmec.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partidas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Partida da Copa do Mundo")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da partida", example = "1")
    private Long id;

    @NotNull(message = "Data da partida é obrigatória")
    @Column(nullable = false)
    @Schema(description = "Data da partida", example = "2026-06-15")
    private LocalDate data;

    @NotBlank(message = "Estádio é obrigatório")
    @Column(nullable = false)
    @Schema(description = "Nome do estádio", example = "Maracanã")
    private String estadio;

    @NotBlank(message = "Fase da competição é obrigatória")
    @Column(nullable = false)
    @Schema(description = "Fase da competição", example = "Fase de Grupos")
    private String fase;

    @Schema(description = "Placar da partida (preenchido após o jogo)", example = "2 x 1")
    private String placar;

    // N:N — uma Partida tem muitas Seleções (normalmente duas)
    @ManyToMany
    @JoinTable(
        name = "partida_selecao",
        joinColumns = @JoinColumn(name = "partida_id"),
        inverseJoinColumns = @JoinColumn(name = "selecao_id")
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({"partidas", "jogadores"})
    private List<Selecao> selecoes = new ArrayList<>();
}
