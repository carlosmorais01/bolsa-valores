package com.ao.depress.stocknotify.controller;

import com.ao.depress.stocknotify.dto.BolsaResponse;
import com.ao.depress.stocknotify.service.BolsaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bolsas")
@RequiredArgsConstructor
@Tag(name = "Bolsas", description = "Listagem de bolsas e gerenciamento de inscrição de clientes")
public class BolsaController {

    private final BolsaService bolsaService;

    @GetMapping
    @Operation(summary = "Lista as bolsas registradas")
    public List<BolsaResponse> listar() {
        return bolsaService.listar().stream()
                .map(e -> new BolsaResponse(e.getValue().getId(), e.getKey()))
                .toList();
    }

    @PostMapping("/{bolsaId}/clientes/{clienteId}")
    @Operation(
            summary = "Inscreve um cliente numa bolsa",
            description = "O cliente passa a ser notificado conforme a política vigente da bolsa. Chamar de novo para um cliente já inscrito não tem efeito adicional.")
    @ApiResponse(responseCode = "204", description = "Cliente inscrito")
    @ApiResponse(responseCode = "404", description = "Bolsa ou cliente não encontrado")
    public ResponseEntity<Void> inscrever(
            @Parameter(description = "Identificador da bolsa") @PathVariable UUID bolsaId,
            @Parameter(description = "Identificador do cliente") @PathVariable UUID clienteId) {
        bolsaService.inscrever(bolsaId, clienteId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bolsaId}/clientes/{clienteId}")
    @Operation(summary = "Remove a inscrição de um cliente numa bolsa")
    @ApiResponse(responseCode = "204", description = "Cliente desinscrito")
    @ApiResponse(responseCode = "404", description = "Bolsa ou cliente não encontrado")
    public ResponseEntity<Void> desinscrever(
            @Parameter(description = "Identificador da bolsa") @PathVariable UUID bolsaId,
            @Parameter(description = "Identificador do cliente") @PathVariable UUID clienteId) {
        bolsaService.desinscrever(bolsaId, clienteId);
        return ResponseEntity.noContent().build();
    }
}
