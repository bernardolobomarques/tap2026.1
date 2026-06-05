package com.ibmec.api.service;

import com.ibmec.api.entity.Categoria;
import com.ibmec.api.entity.Produto;
import com.ibmec.api.exception.ResourceNotFoundException;
import com.ibmec.api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService implements IProdutoService {

    private final ProdutoRepository repository;
    private final ICategoriaService categoriaService;

    @Override
    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    @Override
    public List<Produto> listarPorCategoria(Long categoriaId) {
        return repository.findByCategoriaId(categoriaId);
    }

    @Override
    public Produto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", id));
    }

    @Override
    public Produto salvar(Long categoriaId, Produto produto) {
        Categoria categoria = categoriaService.buscarPorId(categoriaId);
        produto.setCategoria(categoria);
        return repository.save(produto);
    }

    @Override
    public Produto atualizar(Long id, Produto dados) {
        Produto existente = buscarPorId(id);
        existente.setNome(dados.getNome());
        existente.setDescricao(dados.getDescricao());
        existente.setPreco(dados.getPreco());
        return repository.save(existente);
    }

    @Override
    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
