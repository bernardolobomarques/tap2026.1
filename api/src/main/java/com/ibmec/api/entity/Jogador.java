package com.ibmec.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "jogadores")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Jogador de uma seleção")
public class Jogador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do jogador", example = "1")
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    @Schema(description = "Nome completo do jogador", example = "Vinicius Jr.")
    private String nome;

    @NotNull(message = "Número da camisa é obrigatório")
    @Positive(message = "Número da camisa deve ser maior que zero")
    @Column(nullable = false)
    @Schema(description = "Número da camisa", example = "7")
    private Integer numeroCamisa;

    @NotBlank(message = "Posição é obrigatória")
    @Column(nullable = false)
    @Schema(description = "Posição em campo", example = "Atacante")
    private String posicao;

    @NotNull(message = "Idade é obrigatória")
    @Positive(message = "Idade deve ser maior que zero")
    @Column(nullable = false)
    @Schema(description = "Idade do jogador", example = "24")
    private Integer idade;

    // N:1 — muitos Jogadores pertencem a uma Seleção
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selecao_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({"jogadores", "partidas"})
    private Selecao selecao;
}
