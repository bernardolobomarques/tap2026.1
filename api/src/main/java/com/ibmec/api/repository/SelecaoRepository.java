package com.ibmec.api.repository;

import com.ibmec.api.entity.Selecao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SelecaoRepository extends JpaRepository<Selecao, Long> {
    Optional<Selecao> findByNomePais(String nomePais);
}
