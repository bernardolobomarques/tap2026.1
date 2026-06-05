package com.ibmec.api.service;

import com.ibmec.api.entity.Pedido;
import com.ibmec.api.entity.Produto;
import com.ibmec.api.exception.ResourceNotFoundException;
import com.ibmec.api.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService implements IPedidoService {

    private final PedidoRepository repository;
    private final IProdutoService produtoService;

    @Override
    public List<Pedido> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Pedido buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));
    }

    @Override
    public Pedido salvar(Pedido pedido) {
        return repository.save(pedido);
    }

    @Override
    public Pedido adicionarProduto(Long pedidoId, Long produtoId) {
        Pedido pedido = buscarPorId(pedidoId);
        Produto produto = produtoService.buscarPorId(produtoId);
        pedido.getProdutos().add(produto);
        return repository.save(pedido);
    }

    @Override
    public Pedido removerProduto(Long pedidoId, Long produtoId) {
        Pedido pedido = buscarPorId(pedidoId);
        pedido.getProdutos().removeIf(p -> p.getId().equals(produtoId));
        return repository.save(pedido);
    }

    @Override
    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
