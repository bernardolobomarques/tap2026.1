package com.ibmec.api.repository;

import com.ibmec.api.entity.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    List<Partida> findByFase(String fase);
}
