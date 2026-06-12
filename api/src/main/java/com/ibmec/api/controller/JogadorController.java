package com.ibmec.api.controller;

import com.ibmec.api.dto.JogadorRequest;
import com.ibmec.api.entity.Jogador;
import com.ibmec.api.service.IJogadorService;
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
@RequestMapping("/jogadores")
@RequiredArgsConstructor
@Tag(name = "Jogadores", description = "Gerenciamento de jogadores das seleções")
public class JogadorController {

    private final IJogadorService service;

    @GetMapping
    @Operation(summary = "Listar todos os jogadores")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public List<Jogador> listar() {
        return service.listarTodos();
    }

    @GetMapping("/selecao/{selecaoId}")
    @Operation(summary = "Listar jogadores de uma seleção", description = "Retorna todos os jogadores pertencentes à seleção informada")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Seleção não encontrada")
    })
    public List<Jogador> listarPorSelecao(@PathVariable Long selecaoId) {
        return service.listarPorSelecao(selecaoId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar jogador por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Jogador encontrado"),
        @ApiResponse(responseCode = "404", description = "Jogador não encontrado")
    })
    public Jogador buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping("/selecao/{selecaoId}")
    @Operation(summary = "Cadastrar jogador em uma seleção")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Jogador cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Seleção não encontrada")
    })
    public ResponseEntity<Jogador> criar(@PathVariable Long selecaoId, @Valid @RequestBody JogadorRequest request) {
        Jogador jogador = Jogador.builder()
                .nome(request.getNome())
                .numeroCamisa(request.getNumeroCamisa())
                .posicao(request.getPosicao())
                .idade(request.getIdade())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(selecaoId, jogador));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar jogador existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Jogador atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Jogador não encontrado")
    })
    public Jogador atualizar(@PathVariable Long id, @Valid @RequestBody JogadorRequest request) {
        Jogador jogador = Jogador.builder()
                .nome(request.getNome())
                .numeroCamisa(request.getNumeroCamisa())
                .posicao(request.getPosicao())
                .idade(request.getIdade())
                .build();
        return service.atualizar(id, jogador);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover jogador")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Jogador removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Jogador não encontrado")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
