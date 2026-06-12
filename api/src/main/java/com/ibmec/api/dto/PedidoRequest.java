package com.ibmec.api.dto;

import jakarta.validation.constraints.NotBlank;

public class PedidoRequest {

    @NotBlank(message = "Nome do cliente é obrigatório")
    private String cliente;

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
}
