package com.ao.depress.stocknotify.controller;

import com.ao.depress.stocknotify.adapter.bovespa.SistemaRemotoBovespa;
import com.ao.depress.stocknotify.dto.BovespaEventoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/bolsas/bovespa")
@RequiredArgsConstructor
@Tag(name = "BOVESPA", description = "Porta de entrada usada pelo subsistema remoto da BOVESPA para enviar eventos de mercado")
public class BovespaEventoController {

    private final SistemaRemotoBovespa sistemaRemotoBovespa;

    @PostMapping("/eventos")
    @Operation(
            summary = "Recebe um evento de variação percentual da BOVESPA",
            description = "Traduz o formato próprio da BOVESPA (variação percentual) para o evento de domínio comum via Adapter, e publica para os clientes inscritos conforme a política de notificação vigente.")
    @ApiResponse(responseCode = "202", description = "Evento aceito e processado")
    public ResponseEntity<Void> receberEvento(@RequestBody BovespaEventoRequest request) {
        log.info("Evento recebido da BOVESPA: {}", request);
        sistemaRemotoBovespa.simulaVariacao(request.ativo(), request.variacaoPercentual());
        return ResponseEntity.accepted().build();
    }
}
