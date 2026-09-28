package com.ao.depress.stocknotify.controller;

import com.ao.depress.stocknotify.dto.PoliticaRequest;
import com.ao.depress.stocknotify.service.BolsaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/bolsas")
@RequiredArgsConstructor
@Tag(name = "Política de notificação", description = "Configuração em tempo de execução da estratégia de notificação de cada bolsa")
public class PoliticaController {

    private final BolsaService bolsaService;

    @PutMapping("/{bolsaId}/politica")
    @Operation(
            summary = "Troca a política de notificação de uma bolsa em runtime",
            description = "Define, para uma direção (ALTA/BAIXA) de uma bolsa específica, qual Strategy (TODOS ou SOMENTE_PREMIUM) passa a decidir quem é notificado, sem reiniciar a API.")
    @ApiResponse(responseCode = "204", description = "Política atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Bolsa não encontrada")
    public ResponseEntity<Void> definirPolitica(
            @Parameter(description = "Identificador da bolsa") @PathVariable UUID bolsaId,
            @RequestBody PoliticaRequest request) {
        bolsaService.definirPolitica(bolsaId, request.direcao(), request.tipo().criar());
        return ResponseEntity.noContent().build();
    }
}
