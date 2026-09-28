package com.ao.depress.stocknotify.model.observer;

import com.ao.depress.stocknotify.model.Direcao;
import com.ao.depress.stocknotify.model.EventoMercado;
import com.ao.depress.stocknotify.model.strategy.PoliticaNotificarSomentePremium;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Bolsa - regra de notificação por direção")
class BolsaTest {

    private final Bolsa bolsa = new Bolsa() {};

    @Test
    @DisplayName("Deve notificar todos os clientes quando a bolsa sobe")
    void altaNotificaTodosOsClientesPorPadrao() {
        Cliente comum = new Cliente("Carlos", false);
        Cliente premium = new Cliente("Ana", true);
        bolsa.subscribe(comum);
        bolsa.subscribe(premium);

        bolsa.publicar(new EventoMercado("NASDAQ:AAPL", Direcao.ALTA, 155.0));

        assertThat(comum.getNotificacoes()).hasSize(1);
        assertThat(premium.getNotificacoes()).hasSize(1);
    }

    @Test
    @DisplayName("Deve notificar somente clientes premium quando a bolsa cai")
    void baixaNotificaSomentePremiumPorPadrao() {
        Cliente comum = new Cliente("Carlos", false);
        Cliente premium = new Cliente("Ana", true);
        bolsa.subscribe(comum);
        bolsa.subscribe(premium);

        bolsa.publicar(new EventoMercado("BOVESPA:PETR4", Direcao.BAIXA, -1.2));

        assertThat(comum.getNotificacoes()).isEmpty();
        assertThat(premium.getNotificacoes()).hasSize(1);
    }

    @Test
    @DisplayName("Não deve notificar cliente que foi desinscrito")
    void clienteDesinscritoNaoRecebeMaisNotificacao() {
        Cliente premium = new Cliente("Ana", true);
        bolsa.subscribe(premium);
        bolsa.unsubscribe(premium);

        bolsa.publicar(new EventoMercado("NASDAQ:AAPL", Direcao.ALTA, 155.0));

        assertThat(premium.getNotificacoes()).isEmpty();
    }

    @Test
    @DisplayName("Deve aplicar a nova política ao trocar a estratégia em runtime")
    void definirPoliticaTrocaComportamentoEmRuntime() {
        Cliente comum = new Cliente("Carlos", false);
        bolsa.subscribe(comum);
        bolsa.definirPolitica(Direcao.ALTA, new PoliticaNotificarSomentePremium());

        bolsa.publicar(new EventoMercado("NASDAQ:AAPL", Direcao.ALTA, 155.0));

        assertThat(comum.getNotificacoes()).isEmpty();
    }
}
