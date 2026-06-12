package com.ibmec.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "selecoes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Seleção participante da Copa do Mundo")
public class Selecao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da seleção", example = "1")
    private Long id;

    @NotBlank(message = "Nome do país é obrigatório")
    @Column(nullable = false)
    @Schema(description = "Nome do país", example = "Brasil")
    private String nomePais;

    @NotBlank(message = "Nome do técnico é obrigatório")
    @Column(nullable = false)
    @Schema(description = "Nome do técnico", example = "Dorival Júnior")
    private String tecnico;

    @NotNull(message = "Ranking FIFA é obrigatório")
    @Positive(message = "Ranking FIFA deve ser maior que zero")
    @Column(nullable = false)
    @Schema(description = "Posição no ranking FIFA", example = "5")
    private Integer rankingFifa;

    // 1:N — uma Seleção tem muitos Jogadores
    @OneToMany(mappedBy = "selecao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties("selecao")
    private List<Jogador> jogadores = new ArrayList<>();

    // N:N lado inverso — uma Seleção participa de muitas Partidas
    @ManyToMany(mappedBy = "selecoes")
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties("selecoes")
    private List<Partida> partidas = new ArrayList<>();
}
