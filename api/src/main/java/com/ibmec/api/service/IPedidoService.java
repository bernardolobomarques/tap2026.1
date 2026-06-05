package com.ibmec.api.service;

import com.ibmec.api.entity.Pedido;

import java.util.List;

public interface IPedidoService {
    List<Pedido> listarTodos();
    Pedido buscarPorId(Long id);
    Pedido salvar(Pedido pedido);
    Pedido adicionarProduto(Long pedidoId, Long produtoId);
    Pedido removerProduto(Long pedidoId, Long produtoId);
    void deletar(Long id);
}
