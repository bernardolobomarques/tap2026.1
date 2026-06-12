package com.ibmec.api.service;

import com.ibmec.api.entity.Selecao;

import java.util.List;

/**
 * Padrão Strategy — define o contrato de negócio para Seleção.
 * O controller depende desta interface, não da implementação concreta,
 * permitindo trocar a implementação sem alterar o código que a usa.
 */
public interface ISelecaoService {
    List<Selecao> listarTodos();
    Selecao buscarPorId(Long id);
    Selecao salvar(Selecao selecao);
    Selecao atualizar(Long id, Selecao selecao);
    void deletar(Long id);
}
