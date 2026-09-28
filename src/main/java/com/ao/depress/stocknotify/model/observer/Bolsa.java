package com.ao.depress.stocknotify.model.observer;

import com.ao.depress.stocknotify.model.Direcao;
import com.ao.depress.stocknotify.model.EventoMercado;
import com.ao.depress.stocknotify.model.strategy.PoliticaNotificarSomentePremium;
import com.ao.depress.stocknotify.model.strategy.PoliticaNotificarTodos;
import com.ao.depress.stocknotify.model.strategy.Strategy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Getter
public abstract class Bolsa implements Subject {
    private final UUID id = UUID.randomUUID();
    private final Set<Observer> observadores = new CopyOnWriteArraySet<>();

    private final Map<Direcao, Strategy> politicas = new EnumMap<>(Direcao.class);

    protected Bolsa() {
        politicas.put(Direcao.ALTA, new PoliticaNotificarTodos());
        politicas.put(Direcao.BAIXA, new PoliticaNotificarSomentePremium());
    }

    public void definirPolitica(Direcao direcao, Strategy politica) {
        log.info("Politica de notificacao para direcao {} alterada em runtime para {}", direcao, politica.getClass().getSimpleName());
        politicas.put(direcao, politica);
    }

    @Override
    public void subscribe(Observer o) {
        observadores.add(o);
    }

    @Override
    public void unsubscribe(Observer o) {
        observadores.remove(o);
    }

    @Override
    public boolean isSubscribed(Observer o) {
        return observadores.contains(o);
    }

    protected void publicar(EventoMercado evento) {
        log.info("Evento recebido: {}", evento);
        Strategy politica = politicas.get(evento.direcao());
        for (Observer o : observadores) {
            if (politica.deveNotificar(o)) {
                o.update(evento);
            }
        }
    }
}
