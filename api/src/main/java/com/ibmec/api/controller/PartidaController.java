package com.ibmec.api.controller;

import com.ibmec.api.builder.PartidaBuilder;
import com.ibmec.api.dto.PartidaRequest;
import com.ibmec.api.entity.Partida;
import com.ibmec.api.service.IPartidaService;
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
@RequestMapping("/partidas")
@RequiredArgsConstructor
@Tag(name = "Partidas", description = "Gerenciamento de partidas da Copa do Mundo")
public class PartidaController {

    private final IPartidaService service;

    @GetMapping
    @Operation(summary = "Listar todas as partidas")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public List<Partida> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar partida por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partida encontrada"),
        @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    public Partida buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cadastrar nova partida", description = "Utiliza o padrão Builder para construção do objeto Partida")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Partida cadastrada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Partida> criar(@Valid @RequestBody PartidaRequest request) {
        Partida partida = new PartidaBuilder()
                .naData(request.getData())
                .noEstadio(request.getEstadio())
                .naFase(request.getFase())
                .comPlacar(request.getPlacar())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(partida));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar partida existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partida atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    public Partida atualizar(@PathVariable Long id, @Valid @RequestBody PartidaRequest request) {
        Partida partida = new PartidaBuilder()
                .naData(request.getData())
                .noEstadio(request.getEstadio())
                .naFase(request.getFase())
                .comPlacar(request.getPlacar())
                .build();
        return service.atualizar(id, partida);
    }

    @PostMapping("/{partidaId}/selecoes/{selecaoId}")
    @Operation(summary = "Adicionar seleção a uma partida", description = "Registra a participação de uma seleção em uma partida (relação N:N)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Seleção adicionada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Partida ou seleção não encontrada")
    })
    public Partida adicionarSelecao(@PathVariable Long partidaId, @PathVariable Long selecaoId) {
        return service.adicionarSelecao(partidaId, selecaoId);
    }

    @DeleteMapping("/{partidaId}/selecoes/{selecaoId}")
    @Operation(summary = "Remover seleção de uma partida")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Seleção removida com sucesso"),
        @ApiResponse(responseCode = "404", description = "Partida ou seleção não encontrada")
    })
    public Partida removerSelecao(@PathVariable Long partidaId, @PathVariable Long selecaoId) {
        return service.removerSelecao(partidaId, selecaoId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover partida")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Partida removida com sucesso"),
        @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
