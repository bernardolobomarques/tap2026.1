package com.ibmec.api.service;

import com.ibmec.api.entity.Partida;

import java.util.List;

/**
 * Padrão Strategy — define o contrato de negócio para Partida.
 * O controller depende desta interface, não da implementação concreta,
 * permitindo trocar a implementação sem alterar o código que a usa.
 */
public interface IPartidaService {
    List<Partida> listarTodos();
    Partida buscarPorId(Long id);
    Partida salvar(Partida partida);
    Partida atualizar(Long id, Partida partida);
    Partida adicionarSelecao(Long partidaId, Long selecaoId);
    Partida removerSelecao(Long partidaId, Long selecaoId);
    void deletar(Long id);
}
