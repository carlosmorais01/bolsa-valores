package com.ao.depress.stocknotify.service;

import com.ao.depress.stocknotify.model.observer.Bolsa;
import com.ao.depress.stocknotify.model.observer.Observer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class BolsaRegistry {
    private final Map<String, Bolsa> bolsas;

    public BolsaRegistry(Map<String, Bolsa> bolsas) {
        this.bolsas = bolsas;
    }

    public Optional<Bolsa> buscarPorId(UUID id) {
        return bolsas.values().stream().filter(bolsa -> bolsa.getId().equals(id)).findFirst();
    }

    public Collection<Bolsa> todas() {
        return bolsas.values();
    }

    public List<Map.Entry<String, Bolsa>> entradas() {
        return List.copyOf(bolsas.entrySet());
    }

    public List<Map.Entry<String, Bolsa>> inscricoesDe(Observer observer) {
        return bolsas.entrySet().stream()
                .filter(e -> e.getValue().isSubscribed(observer))
                .toList();
    }
}
