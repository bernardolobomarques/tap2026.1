package com.ibmec.api.builder;

import com.ibmec.api.entity.Partida;

import java.time.LocalDate;

/**
 * Padrão de Projeto: Builder (GoF — Criacional)
 *
 * Permite construir um objeto Partida passo a passo com uma API fluente,
 * garantindo que os campos obrigatórios sejam informados antes da criação.
 *
 * Uso:
 *   Partida p = new PartidaBuilder()
 *       .naData(LocalDate.of(2026, 6, 15))
 *       .noEstadio("Maracanã")
 *       .naFase("Fase de Grupos")
 *       .build();
 */
public class PartidaBuilder {

    private LocalDate data;
    private String estadio;
    private String fase;
    private String placar;

    public PartidaBuilder naData(LocalDate data) {
        this.data = data;
        return this;
    }

    public PartidaBuilder noEstadio(String estadio) {
        this.estadio = estadio;
        return this;
    }

    public PartidaBuilder naFase(String fase) {
        this.fase = fase;
        return this;
    }

    public PartidaBuilder comPlacar(String placar) {
        this.placar = placar;
        return this;
    }

    public Partida build() {
        if (data == null) throw new IllegalArgumentException("Data da partida é obrigatória");
        if (estadio == null || estadio.isBlank()) throw new IllegalArgumentException("Estádio é obrigatório");
        if (fase == null || fase.isBlank()) throw new IllegalArgumentException("Fase é obrigatória");

        return Partida.builder()
                .data(data)
                .estadio(estadio)
                .fase(fase)
                .placar(placar)
                .build();
    }
}
