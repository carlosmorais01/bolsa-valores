package com.ao.depress.stocknotify.model.strategy;

import com.ao.depress.stocknotify.model.observer.Observer;

public interface Strategy {
    boolean deveNotificar(Observer o);
}
