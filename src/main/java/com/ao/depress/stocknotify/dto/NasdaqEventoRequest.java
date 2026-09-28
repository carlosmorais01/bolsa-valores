package com.ao.depress.stocknotify.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Formato próprio da NASDAQ: preço antigo e novo, sem indicar direção explicitamente")
public record NasdaqEventoRequest(
        @Schema(description = "Símbolo do ativo", example = "AAPL") String simbolo,
        @Schema(description = "Preço antes da variação", example = "150.0") double precoAntigo,
        @Schema(description = "Preço após a variação", example = "155.0") double precoNovo) {
}
