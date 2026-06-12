package com.ibmec.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "Dados para cadastro ou atualização de um jogador")
public class JogadorRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome completo do jogador", example = "Vinicius Jr.")
    private String nome;

    @NotNull(message = "Número da camisa é obrigatório")
    @Positive(message = "Número da camisa deve ser maior que zero")
    @Schema(description = "Número da camisa", example = "7")
    private Integer numeroCamisa;

    @NotBlank(message = "Posição é obrigatória")
    @Schema(description = "Posição em campo", example = "Atacante")
    private String posicao;

    @NotNull(message = "Idade é obrigatória")
    @Positive(message = "Idade deve ser maior que zero")
    @Schema(description = "Idade do jogador", example = "24")
    private Integer idade;
}
