package com.ao.depress.stocknotify.adapter.nasdaq;

import com.ao.depress.stocknotify.model.Direcao;
import com.ao.depress.stocknotify.model.observer.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Adapter NASDAQ - tradução de eventos do sistema remoto")
class AdaptadorBolsaNasdaqTest {

    @Test
    @DisplayName("Deve gerar evento de alta quando o preço sobe")
    void precoSubindoGeraEventoDeAltaParaTodos() {
        SistemaRemotoNasdaq remoto = new SistemaRemotoNasdaq();
        AdaptadorBolsaNasdaq adaptador = new AdaptadorBolsaNasdaq(remoto);
        Cliente cliente = new Cliente("Carlos", false);
        adaptador.subscribe(cliente);

        remoto.simularMudancaPreco("AAPL", 150.0, 155.0);

        assertThat(cliente.getNotificacoes()).hasSize(1);
        var evento = cliente.getNotificacoes().getFirst().evento();
        assertThat(evento.nomeBolsa()).isEqualTo("NASDAQ:AAPL");
        assertThat(evento.direcao()).isEqualTo(Direcao.ALTA);
        assertThat(evento.novoValor()).isEqualTo(155.0);
    }

    @Test
    @DisplayName("Deve gerar evento de baixa e notificar somente premium quando o preço cai")
    void precoCaindoGeraEventoDeBaixaESoNotificaPremium() {
        SistemaRemotoNasdaq remoto = new SistemaRemotoNasdaq();
        AdaptadorBolsaNasdaq adaptador = new AdaptadorBolsaNasdaq(remoto);
        Cliente comum = new Cliente("Carlos", false);
        Cliente premium = new Cliente("Ana", true);
        adaptador.subscribe(comum);
        adaptador.subscribe(premium);

        remoto.simularMudancaPreco("AAPL", 155.0, 150.0);

        assertThat(comum.getNotificacoes()).isEmpty();
        assertThat(premium.getNotificacoes()).hasSize(1);
        assertThat(premium.getNotificacoes().getFirst().evento().direcao()).isEqualTo(Direcao.BAIXA);
    }
}
