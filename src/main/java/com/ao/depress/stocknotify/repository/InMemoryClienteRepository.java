package com.ao.depress.stocknotify.repository;

import com.ao.depress.stocknotify.model.observer.Cliente;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryClienteRepository implements ClienteRepository {
    private final Map<UUID, Cliente> clientes = new ConcurrentHashMap<>();

    @Override
    public Cliente salvar(Cliente cliente) {
        clientes.put(cliente.getId(), cliente);
        return cliente;
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        return Optional.ofNullable(clientes.get(id));
    }

    @Override
    public Collection<Cliente> listarTodos() {
        return clientes.values();
    }
}
