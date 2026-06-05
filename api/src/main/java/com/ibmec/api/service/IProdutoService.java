package com.ibmec.api.service;

import com.ibmec.api.entity.Produto;

import java.util.List;

public interface IProdutoService {
    List<Produto> listarTodos();
    List<Produto> listarPorCategoria(Long categoriaId);
    Produto buscarPorId(Long id);
    Produto salvar(Long categoriaId, Produto produto);
    Produto atualizar(Long id, Produto produto);
    void deletar(Long id);
}
