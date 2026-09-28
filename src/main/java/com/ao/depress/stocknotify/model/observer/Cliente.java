package com.ao.depress.stocknotify.model.observer;

import com.ao.depress.stocknotify.model.EventoMercado;
import com.ao.depress.stocknotify.model.Notificacao;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Getter
@Setter
public class Cliente implements Observer, CientePremium {

    private final UUID id = UUID.randomUUID();
    private final String nome;
    private final boolean premium;
    private final List<Notificacao> notificacoes = new CopyOnWriteArrayList<>();

    public Cliente(String nome, boolean premium) {
        this.nome = nome;
        this.premium = premium;
    }

    @Override
    public boolean ehPremium() {
        return premium;
    }

    @Override
    public void update(EventoMercado evento) {
        notificacoes.add(Notificacao.agora(evento));
        log.info("[NOTIFICACAO] {} ({}) recebeu: {} {} -> {}",
                nome, premium ? "premium" : "comum",
                evento.nomeBolsa(), evento.direcao(), evento.novoValor());
    }
}
