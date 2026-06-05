package com.ibmec.api.service;

import com.ibmec.api.entity.Categoria;

import java.util.List;

public interface ICategoriaService {
    List<Categoria> listarTodos();
    Categoria buscarPorId(Long id);
    Categoria salvar(Categoria categoria);
    Categoria atualizar(Long id, Categoria categoria);
    void deletar(Long id);
}
