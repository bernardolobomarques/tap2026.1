package com.ibmec.api.service;

import com.ibmec.api.entity.Partida;
import com.ibmec.api.entity.Selecao;
import com.ibmec.api.exception.ResourceNotFoundException;
import com.ibmec.api.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartidaService implements IPartidaService {

    private final PartidaRepository repository;
    private final ISelecaoService selecaoService;

    @Override
    public List<Partida> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Partida buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partida", id));
    }

    @Override
    public Partida salvar(Partida partida) {
        return repository.save(partida);
    }

    @Override
    public Partida atualizar(Long id, Partida dados) {
        Partida existente = buscarPorId(id);
        existente.setData(dados.getData());
        existente.setEstadio(dados.getEstadio());
        existente.setFase(dados.getFase());
        existente.setPlacar(dados.getPlacar());
        return repository.save(existente);
    }

    @Override
    public Partida adicionarSelecao(Long partidaId, Long selecaoId) {
        Partida partida = buscarPorId(partidaId);
        Selecao selecao = selecaoService.buscarPorId(selecaoId);
        boolean jaExiste = partida.getSelecoes().stream()
                .anyMatch(s -> s.getId().equals(selecaoId));
        if (!jaExiste) {
            partida.getSelecoes().add(selecao);
            return repository.save(partida);
        }
        return partida;
    }

    @Override
    public Partida removerSelecao(Long partidaId, Long selecaoId) {
        Partida partida = buscarPorId(partidaId);
        boolean existia = partida.getSelecoes().removeIf(s -> s.getId().equals(selecaoId));
        if (!existia) {
            throw new ResourceNotFoundException("Seleção na partida", selecaoId);
        }
        return repository.save(partida);
    }

    @Override
    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
