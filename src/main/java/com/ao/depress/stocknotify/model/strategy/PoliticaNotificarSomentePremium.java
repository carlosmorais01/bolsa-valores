package com.ao.depress.stocknotify.model.strategy;

import com.ao.depress.stocknotify.model.observer.CientePremium;
import com.ao.depress.stocknotify.model.observer.Observer;

public class PoliticaNotificarSomentePremium implements Strategy {
    public boolean deveNotificar(Observer o) {
        return o instanceof CientePremium cliente && cliente.ehPremium();
    }
}
