package com.ao.depress.stocknotify.dto;

import com.ao.depress.stocknotify.model.Direcao;
import com.ao.depress.stocknotify.model.Notificacao;

import java.time.Instant;

public record NotificacaoResponse(String bolsa, Direcao direcao, double valor, Instant recebidaEm) {
    public static NotificacaoResponse de(Notificacao notificacao) {
        return new NotificacaoResponse(
                notificacao.evento().nomeBolsa(),
                notificacao.evento().direcao(),
                notificacao.evento().novoValor(),
                notificacao.recebidaEm());
    }
}
