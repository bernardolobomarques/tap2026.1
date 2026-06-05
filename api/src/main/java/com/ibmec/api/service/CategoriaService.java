package com.ibmec.api.service;

import com.ibmec.api.entity.Categoria;
import com.ibmec.api.exception.ResourceNotFoundException;
import com.ibmec.api.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService implements ICategoriaService {

    private final CategoriaRepository repository;

    @Override
    public List<Categoria> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Categoria buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
    }

    @Override
    public Categoria salvar(Categoria categoria) {
        return repository.save(categoria);
    }

    @Override
    public Categoria atualizar(Long id, Categoria dados) {
        Categoria existente = buscarPorId(id);
        existente.setNome(dados.getNome());
        existente.setDescricao(dados.getDescricao());
        return repository.save(existente);
    }

    @Override
    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
