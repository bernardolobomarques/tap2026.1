package com.ibmec.api.service;

import com.ibmec.api.entity.Produto;

import java.util.List;

/**
 * Padrão de Projeto: Strategy
 * Define o contrato (algoritmo) de negócio para Produto.
 * O controller depende desta interface, não da implementação concreta,
 * permitindo trocar a implementação sem alterar o código que a usa.
 */
public interface IProdutoService {
    List<Produto> listarTodos();
    List<Produto> listarPorCategoria(Long categoriaId);
    Produto buscarPorId(Long id);
    Produto salvar(Long categoriaId, Produto produto);
    Produto atualizar(Long id, Produto produto);
    void deletar(Long id);
}
