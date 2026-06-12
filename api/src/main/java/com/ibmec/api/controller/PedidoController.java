package com.ibmec.api.controller;

import com.ibmec.api.builder.PedidoBuilder;
import com.ibmec.api.dto.PedidoRequest;
import com.ibmec.api.entity.Pedido;
import com.ibmec.api.service.IPedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final IPedidoService service;

    @GetMapping
    public List<Pedido> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Pedido buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Pedido> criar(@Valid @RequestBody PedidoRequest request) {
        Pedido pedido = new PedidoBuilder()
                .comCliente(request.getCliente())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(pedido));
    }

    @PutMapping("/{id}")
    public Pedido atualizar(@PathVariable Long id, @Valid @RequestBody PedidoRequest request) {
        return service.atualizar(id, request.getCliente());
    }

    @PostMapping("/{pedidoId}/produtos/{produtoId}")
    public Pedido adicionarProduto(@PathVariable Long pedidoId, @PathVariable Long produtoId) {
        return service.adicionarProduto(pedidoId, produtoId);
    }

    @DeleteMapping("/{pedidoId}/produtos/{produtoId}")
    public Pedido removerProduto(@PathVariable Long pedidoId, @PathVariable Long produtoId) {
        return service.removerProduto(pedidoId, produtoId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
