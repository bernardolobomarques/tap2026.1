package com.ibmec.api.service;

import com.ibmec.api.entity.Jogador;
import com.ibmec.api.entity.Selecao;
import com.ibmec.api.exception.ResourceNotFoundException;
import com.ibmec.api.repository.JogadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JogadorService implements IJogadorService {

    private final JogadorRepository repository;
    private final ISelecaoService selecaoService;

    @Override
    public List<Jogador> listarTodos() {
        return repository.findAll();
    }

    @Override
    public List<Jogador> listarPorSelecao(Long selecaoId) {
        selecaoService.buscarPorId(selecaoId);
        return repository.findBySelecaoId(selecaoId);
    }

    @Override
    public Jogador buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jogador", id));
    }

    @Override
    public Jogador salvar(Long selecaoId, Jogador jogador) {
        Selecao selecao = selecaoService.buscarPorId(selecaoId);
        jogador.setSelecao(selecao);
        return repository.save(jogador);
    }

    @Override
    public Jogador atualizar(Long id, Jogador dados) {
        Jogador existente = buscarPorId(id);
        existente.setNome(dados.getNome());
        existente.setNumeroCamisa(dados.getNumeroCamisa());
        existente.setPosicao(dados.getPosicao());
        existente.setIdade(dados.getIdade());
        return repository.save(existente);
    }

    @Override
    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
