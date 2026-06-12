package com.ibmec.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "Dados para cadastro ou atualização de uma seleção")
public class SelecaoRequest {

    @NotBlank(message = "Nome do país é obrigatório")
    @Schema(description = "Nome do país", example = "Brasil")
    private String nomePais;

    @NotBlank(message = "Nome do técnico é obrigatório")
    @Schema(description = "Nome do técnico", example = "Dorival Júnior")
    private String tecnico;

    @NotNull(message = "Ranking FIFA é obrigatório")
    @Positive(message = "Ranking FIFA deve ser maior que zero")
    @Schema(description = "Posição no ranking FIFA", example = "5")
    private Integer rankingFifa;
}
