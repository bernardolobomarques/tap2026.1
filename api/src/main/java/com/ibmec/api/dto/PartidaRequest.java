package com.ibmec.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Dados para cadastro ou atualização de uma partida")
public class PartidaRequest {

    @NotNull(message = "Data da partida é obrigatória")
    @Schema(description = "Data da partida", example = "2026-06-15")
    private LocalDate data;

    @NotBlank(message = "Estádio é obrigatório")
    @Schema(description = "Nome do estádio", example = "Maracanã")
    private String estadio;

    @NotBlank(message = "Fase da competição é obrigatória")
    @Schema(description = "Fase da competição", example = "Fase de Grupos")
    private String fase;

    @Schema(description = "Placar da partida (pode ser preenchido após o jogo)", example = "2 x 1")
    private String placar;
}
