package com.ao.depress.stocknotify.model.strategy;

import com.ao.depress.stocknotify.model.observer.Observer;

public class PoliticaNotificarTodos implements Strategy {
    public boolean deveNotificar(Observer o) {
        return true;
    }
}
