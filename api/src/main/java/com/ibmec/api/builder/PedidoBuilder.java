package com.ibmec.api.builder;

import com.ibmec.api.entity.Pedido;

/**
 * Padrão de Projeto: Builder (GoF)
 * Constrói um objeto Pedido passo a passo, garantindo que todos os
 * campos obrigatórios estejam preenchidos antes da criação.
 */
public class PedidoBuilder {

    private String cliente;

    public PedidoBuilder comCliente(String cliente) {
        this.cliente = cliente;
        return this;
    }

    public Pedido build() {
        if (cliente == null || cliente.isBlank()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório");
        }
        return Pedido.builder()
                .cliente(cliente)
                .build();
    }
}
