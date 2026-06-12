package com.ibmec.api.controller;

import com.ibmec.api.dto.SelecaoRequest;
import com.ibmec.api.entity.Selecao;
import com.ibmec.api.service.ISelecaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/selecoes")
@RequiredArgsConstructor
@Tag(name = "Seleções", description = "Gerenciamento de seleções da Copa do Mundo")
public class SelecaoController {

    private final ISelecaoService service;

    @GetMapping
    @Operation(summary = "Listar todas as seleções")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public List<Selecao> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar seleção por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Seleção encontrada"),
        @ApiResponse(responseCode = "404", description = "Seleção não encontrada")
    })
    public Selecao buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cadastrar nova seleção")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Seleção cadastrada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Selecao> criar(@Valid @RequestBody SelecaoRequest request) {
        Selecao selecao = Selecao.builder()
                .nomePais(request.getNomePais())
                .tecnico(request.getTecnico())
                .rankingFifa(request.getRankingFifa())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(selecao));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar seleção existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Seleção atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Seleção não encontrada")
    })
    public Selecao atualizar(@PathVariable Long id, @Valid @RequestBody SelecaoRequest request) {
        Selecao selecao = Selecao.builder()
                .nomePais(request.getNomePais())
                .tecnico(request.getTecnico())
                .rankingFifa(request.getRankingFifa())
                .build();
        return service.atualizar(id, selecao);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover seleção")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Seleção removida com sucesso"),
        @ApiResponse(responseCode = "404", description = "Seleção não encontrada")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
