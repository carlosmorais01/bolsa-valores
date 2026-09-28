package com.ao.depress.stocknotify.dto;

import com.ao.depress.stocknotify.model.strategy.PoliticaNotificarSomentePremium;
import com.ao.depress.stocknotify.model.strategy.PoliticaNotificarTodos;
import com.ao.depress.stocknotify.model.strategy.Strategy;

public enum TipoPolitica {
    TODOS {
        public Strategy criar() { return new PoliticaNotificarTodos(); }
    },
    SOMENTE_PREMIUM {
        public Strategy criar() { return new PoliticaNotificarSomentePremium(); }
    };

    public abstract Strategy criar();
}
