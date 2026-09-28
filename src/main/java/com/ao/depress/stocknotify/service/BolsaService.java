package com.ao.depress.stocknotify.service;

import com.ao.depress.stocknotify.exception.BolsaNaoEncontradaException;
import com.ao.depress.stocknotify.exception.ClienteNaoEncontradoException;
import com.ao.depress.stocknotify.model.Direcao;
import com.ao.depress.stocknotify.model.observer.Bolsa;
import com.ao.depress.stocknotify.model.observer.Cliente;
import com.ao.depress.stocknotify.model.strategy.Strategy;
import com.ao.depress.stocknotify.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BolsaService {

    private final BolsaRegistry bolsaRegistry;
    private final ClienteRepository clienteRepository;

    public List<Map.Entry<String, Bolsa>> listar() {
        return bolsaRegistry.entradas();
    }

    public void inscrever(UUID bolsaId, UUID clienteId) {
        buscarBolsa(bolsaId).subscribe(buscarCliente(clienteId));
    }

    public void desinscrever(UUID bolsaId, UUID clienteId) {
        buscarBolsa(bolsaId).unsubscribe(buscarCliente(clienteId));
    }

    public void definirPolitica(UUID bolsaId, Direcao direcao, Strategy politica) {
        buscarBolsa(bolsaId).definirPolitica(direcao, politica);
    }

    private Bolsa buscarBolsa(UUID bolsaId) {
        return bolsaRegistry.buscarPorId(bolsaId)
                .orElseThrow(() -> new BolsaNaoEncontradaException(bolsaId));
    }

    private Cliente buscarCliente(UUID clienteId) {
        return clienteRepository.buscarPorId(clienteId)
                .orElseThrow(() -> new ClienteNaoEncontradoException(clienteId));
    }
}
