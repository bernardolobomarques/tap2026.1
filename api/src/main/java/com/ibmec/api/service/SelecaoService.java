package com.ibmec.api.service;

import com.ibmec.api.entity.Selecao;
import com.ibmec.api.exception.ResourceNotFoundException;
import com.ibmec.api.repository.SelecaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SelecaoService implements ISelecaoService {

    private final SelecaoRepository repository;

    @Override
    public List<Selecao> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Selecao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seleção", id));
    }

    @Override
    public Selecao salvar(Selecao selecao) {
        return repository.save(selecao);
    }

    @Override
    public Selecao atualizar(Long id, Selecao dados) {
        Selecao existente = buscarPorId(id);
        existente.setNomePais(dados.getNomePais());
        existente.setTecnico(dados.getTecnico());
        existente.setRankingFifa(dados.getRankingFifa());
        return repository.save(existente);
    }

    @Override
    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
