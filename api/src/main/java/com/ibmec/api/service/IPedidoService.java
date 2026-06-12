package com.ibmec.api.service;

import com.ibmec.api.entity.Pedido;

import java.util.List;

/**
 * Padrão de Projeto: Strategy
 * Define o contrato (algoritmo) de negócio para Pedido.
 * O controller depende desta interface, não da implementação concreta,
 * permitindo trocar a implementação sem alterar o código que a usa.
 */
public interface IPedidoService {
    List<Pedido> listarTodos();
    Pedido buscarPorId(Long id);
    Pedido salvar(Pedido pedido);
    Pedido atualizar(Long id, String novoCliente);
    Pedido adicionarProduto(Long pedidoId, Long produtoId);
    Pedido removerProduto(Long pedidoId, Long produtoId);
    void deletar(Long id);
}
