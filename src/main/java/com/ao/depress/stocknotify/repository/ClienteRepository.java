package com.ao.depress.stocknotify.repository;

import com.ao.depress.stocknotify.model.observer.Cliente;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository {
    Cliente salvar(Cliente cliente);
    Optional<Cliente> buscarPorId(UUID id);
    Collection<Cliente> listarTodos();
}
