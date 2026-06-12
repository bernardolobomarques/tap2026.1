package com.ibmec.api.service;

import com.ibmec.api.entity.Categoria;

import java.util.List;

/**
 * Padrão de Projeto: Strategy
 * Define o contrato (algoritmo) de negócio para Categoria.
 * O controller depende desta interface, não da implementação concreta,
 * permitindo trocar a implementação sem alterar o código que a usa.
 */
public interface ICategoriaService {
    List<Categoria> listarTodos();
    Categoria buscarPorId(Long id);
    Categoria salvar(Categoria categoria);
    Categoria atualizar(Long id, Categoria categoria);
    void deletar(Long id);
}
