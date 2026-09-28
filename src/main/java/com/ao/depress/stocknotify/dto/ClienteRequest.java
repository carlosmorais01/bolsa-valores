package com.ao.depress.stocknotify.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ClienteRequest(
        @Schema(description = "Nome do cliente", example = "Ana") String nome,
        @Schema(description = "Se true, o cliente também é notificado quando uma bolsa cai") boolean premium) {
}
