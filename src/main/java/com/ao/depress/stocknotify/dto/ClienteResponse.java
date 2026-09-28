package com.ao.depress.stocknotify.dto;

import com.ao.depress.stocknotify.model.observer.Cliente;

import java.util.UUID;

public record ClienteResponse(UUID id, String nome, boolean premium) {
    public static ClienteResponse de(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.ehPremium());
    }
}
