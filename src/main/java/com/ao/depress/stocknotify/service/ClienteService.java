package com.ao.depress.stocknotify.service;

import com.ao.depress.stocknotify.exception.ClienteNaoEncontradoException;
import com.ao.depress.stocknotify.model.Notificacao;
import com.ao.depress.stocknotify.model.observer.Bolsa;
import com.ao.depress.stocknotify.model.observer.Cliente;
import com.ao.depress.stocknotify.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final BolsaRegistry bolsaRegistry;

    public Cliente cadastrar(String nome, boolean premium) {
        Cliente cliente = new Cliente(nome, premium);
        bolsaRegistry.todas().forEach(bolsa -> bolsa.subscribe(cliente));
        clienteRepository.salvar(cliente);
        return cliente;
    }

    public Collection<Cliente> listar() {
        return clienteRepository.listarTodos();
    }

    public List<Notificacao> consultarNotificacoes(UUID clienteId) {
        return buscarCliente(clienteId).getNotificacoes();
    }

    public List<Map.Entry<String, Bolsa>> listarBolsas(UUID clienteId) {
        Cliente cliente = buscarCliente(clienteId);
        return bolsaRegistry.inscricoesDe(cliente);
    }

    private Cliente buscarCliente(UUID clienteId) {
        return clienteRepository.buscarPorId(clienteId)
                .orElseThrow(() -> new ClienteNaoEncontradoException(clienteId));
    }
}
