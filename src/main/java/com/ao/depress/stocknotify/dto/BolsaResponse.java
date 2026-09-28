package com.ao.depress.stocknotify.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record BolsaResponse(
        @Schema(description = "Identificador único da bolsa, usado nos endpoints de inscrição") UUID id,
        @Schema(description = "Nome da bolsa, usado nos endpoints de eventos e política", example = "nasdaq") String nome) {
}
