package com.ao.depress.stocknotify.controller;

import com.ao.depress.stocknotify.adapter.nasdaq.SistemaRemotoNasdaq;
import com.ao.depress.stocknotify.dto.NasdaqEventoRequest;
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
@RequestMapping("/api/bolsas/nasdaq")
@RequiredArgsConstructor
@Tag(name = "NASDAQ", description = "Porta de entrada usada pelo subsistema remoto da NASDAQ para enviar eventos de mercado")
public class NasdaqEventoController {

    private final SistemaRemotoNasdaq sistemaRemotoNasdaq;

    @PostMapping("/eventos")
    @Operation(
            summary = "Recebe um evento de variação de preço da NASDAQ",
            description = "Traduz o formato próprio da NASDAQ (preço antigo/novo) para o evento de domínio comum via Adapter, e publica para os clientes inscritos conforme a política de notificação vigente.")
    @ApiResponse(responseCode = "202", description = "Evento aceito e processado")
    public ResponseEntity<Void> receberEvento(@RequestBody NasdaqEventoRequest request) {
        log.info("Evento recebido da NASDAQ: {}", request);
        sistemaRemotoNasdaq.simularMudancaPreco(request.simbolo(), request.precoAntigo(), request.precoNovo());
        return ResponseEntity.accepted().build();
    }
}
