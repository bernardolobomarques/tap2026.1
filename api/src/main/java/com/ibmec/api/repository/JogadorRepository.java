package com.ibmec.api.repository;

import com.ibmec.api.entity.Jogador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JogadorRepository extends JpaRepository<Jogador, Long> {
    List<Jogador> findBySelecaoId(Long selecaoId);
}
