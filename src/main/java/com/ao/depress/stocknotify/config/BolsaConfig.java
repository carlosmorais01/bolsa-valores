package com.ao.depress.stocknotify.config;

import com.ao.depress.stocknotify.adapter.bovespa.AdaptadorBolsaBovespa;
import com.ao.depress.stocknotify.adapter.bovespa.SistemaRemotoBovespa;
import com.ao.depress.stocknotify.adapter.nasdaq.AdaptadorBolsaNasdaq;
import com.ao.depress.stocknotify.adapter.nasdaq.SistemaRemotoNasdaq;
import com.ao.depress.stocknotify.model.observer.Bolsa;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BolsaConfig {

    @Bean
    public SistemaRemotoNasdaq sistemaRemotoNasdaq() {
        return new SistemaRemotoNasdaq();
    }

    @Bean
    public SistemaRemotoBovespa sistemaRemotoBovespa() {
        return new SistemaRemotoBovespa();
    }

    @Bean
    public Bolsa nasdaq(SistemaRemotoNasdaq sistemaRemotoNasdaq) {
        return new AdaptadorBolsaNasdaq(sistemaRemotoNasdaq);
    }

    @Bean
    public Bolsa bovespa(SistemaRemotoBovespa sistemaRemotoBovespa) {
        return new AdaptadorBolsaBovespa(sistemaRemotoBovespa);
    }
}
