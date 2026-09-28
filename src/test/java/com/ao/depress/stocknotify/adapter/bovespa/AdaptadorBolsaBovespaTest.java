package com.ao.depress.stocknotify.adapter.bovespa;

import com.ao.depress.stocknotify.model.Direcao;
import com.ao.depress.stocknotify.model.observer.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Adapter BOVESPA - tradução de eventos do sistema remoto")
class AdaptadorBolsaBovespaTest {

    @Test
    @DisplayName("Deve gerar evento de alta quando a variação é positiva")
    void variacaoPositivaGeraEventoDeAltaParaTodos() {
        SistemaRemotoBovespa remoto = new SistemaRemotoBovespa();
        AdaptadorBolsaBovespa adaptador = new AdaptadorBolsaBovespa(remoto);
        Cliente cliente = new Cliente("Carlos", false);
        adaptador.subscribe(cliente);

        remoto.simulaVariacao("PETR4", 2.5);

        assertThat(cliente.getNotificacoes()).hasSize(1);
        var evento = cliente.getNotificacoes().getFirst().evento();
        assertThat(evento.nomeBolsa()).isEqualTo("BOVESPA:PETR4");
        assertThat(evento.direcao()).isEqualTo(Direcao.ALTA);
    }

    @Test
    @DisplayName("Deve gerar evento de baixa e notificar somente premium quando a variação é negativa")
    void variacaoNegativaGeraEventoDeBaixaESoNotificaPremium() {
        SistemaRemotoBovespa remoto = new SistemaRemotoBovespa();
        AdaptadorBolsaBovespa adaptador = new AdaptadorBolsaBovespa(remoto);
        Cliente comum = new Cliente("Carlos", false);
        Cliente premium = new Cliente("Ana", true);
        adaptador.subscribe(comum);
        adaptador.subscribe(premium);

        remoto.simulaVariacao("PETR4", -1.2);

        assertThat(comum.getNotificacoes()).isEmpty();
        assertThat(premium.getNotificacoes()).hasSize(1);
        assertThat(premium.getNotificacoes().getFirst().evento().direcao()).isEqualTo(Direcao.BAIXA);
    }
}
