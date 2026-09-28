package com.ao.depress.stocknotify.model.observer;

import com.ao.depress.stocknotify.model.EventoMercado;

public interface Observer {
    void update(EventoMercado evento);
}
