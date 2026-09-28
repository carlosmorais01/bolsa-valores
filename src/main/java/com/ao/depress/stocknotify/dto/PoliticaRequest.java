package com.ao.depress.stocknotify.dto;

import com.ao.depress.stocknotify.model.Direcao;
import io.swagger.v3.oas.annotations.media.Schema;

public record PoliticaRequest(
        @Schema(description = "Direção do evento a que a política se aplica") Direcao direcao,
        @Schema(description = "Estratégia de notificação a adotar") TipoPolitica tipo) {
}
