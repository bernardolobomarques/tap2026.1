package com.ibmec.api.service;

import com.ibmec.api.entity.Jogador;

import java.util.List;

/**
 * Padrão Strategy — define o contrato de negócio para Jogador.
 * O controller depende desta interface, não da implementação concreta,
 * permitindo trocar a implementação sem alterar o código que a usa.
 */
public interface IJogadorService {
    List<Jogador> listarTodos();
    List<Jogador> listarPorSelecao(Long selecaoId);
    Jogador buscarPorId(Long id);
    Jogador salvar(Long selecaoId, Jogador jogador);
    Jogador atualizar(Long id, Jogador jogador);
    void deletar(Long id);
}
