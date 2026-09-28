package com.ao.depress.stocknotify.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Formato próprio da BOVESPA: variação percentual direta, sem preço absoluto")
public record BovespaEventoRequest(
        @Schema(description = "Código do ativo", example = "PETR4") String ativo,
        @Schema(description = "Variação percentual (negativa indica queda)", example = "-1.2") double variacaoPercentual) {
}
