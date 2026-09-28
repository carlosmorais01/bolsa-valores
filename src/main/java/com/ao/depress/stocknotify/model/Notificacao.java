package com.ao.depress.stocknotify.model;

import java.time.Instant;

public record Notificacao(EventoMercado evento, Instant recebidaEm) {
    public static Notificacao agora(EventoMercado evento) {
        return new Notificacao(evento, Instant.now());
    }
}
