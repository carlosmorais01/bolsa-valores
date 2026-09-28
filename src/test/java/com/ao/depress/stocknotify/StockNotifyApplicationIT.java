package com.ao.depress.stocknotify;

import com.ao.depress.stocknotify.dto.BovespaEventoRequest;
import com.ao.depress.stocknotify.dto.ClienteRequest;
import com.ao.depress.stocknotify.dto.ClienteResponse;
import com.ao.depress.stocknotify.dto.NasdaqEventoRequest;
import com.ao.depress.stocknotify.dto.NotificacaoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("API - fluxo de ponta a ponta")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StockNotifyApplicationIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Deve subir o contexto da aplicação")
    void contextLoads() {
    }

    @Test
    @DisplayName("Deve notificar os clientes corretamente através dos endpoints da API")
    void fluxoCompletoDeNotificacaoViaApi() {
        ClienteResponse comum = restTemplate.postForObject(
                "/api/clientes", new ClienteRequest("Carlos", false), ClienteResponse.class);
        ClienteResponse premium = restTemplate.postForObject(
                "/api/clientes", new ClienteRequest("Ana", true), ClienteResponse.class);

        ResponseEntity<Void> eventoAlta = restTemplate.postForEntity(
                "/api/bolsas/nasdaq/eventos", new NasdaqEventoRequest("AAPL", 150.0, 155.0), Void.class);
        assertThat(eventoAlta.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        ResponseEntity<Void> eventoBaixa = restTemplate.postForEntity(
                "/api/bolsas/bovespa/eventos", new BovespaEventoRequest("PETR4", -1.2), Void.class);
        assertThat(eventoBaixa.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        NotificacaoResponse[] notificacoesComum = restTemplate.getForObject(
                "/api/clientes/" + comum.id() + "/notificacoes", NotificacaoResponse[].class);
        NotificacaoResponse[] notificacoesPremium = restTemplate.getForObject(
                "/api/clientes/" + premium.id() + "/notificacoes", NotificacaoResponse[].class);

        assertThat(notificacoesComum).hasSize(1);
        assertThat(notificacoesPremium).hasSize(2);
    }

    @Test
    @DisplayName("Deve retornar 404 ao consultar notificações de cliente inexistente")
    void consultarNotificacoesDeClienteInexistenteRetorna404() {
        ResponseEntity<String> resposta = restTemplate.getForEntity(
                "/api/clientes/" + UUID.randomUUID() + "/notificacoes", String.class);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
