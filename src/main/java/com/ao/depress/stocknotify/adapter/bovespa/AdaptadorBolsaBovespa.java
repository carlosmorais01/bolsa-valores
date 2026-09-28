package com.ao.depress.stocknotify.adapter.bovespa;

import com.ao.depress.stocknotify.model.Direcao;
import com.ao.depress.stocknotify.model.EventoMercado;
import com.ao.depress.stocknotify.model.observer.Bolsa;

public class AdaptadorBolsaBovespa extends Bolsa implements RetornoBovespa {
    public AdaptadorBolsaBovespa(SistemaRemotoBovespa remoto) {
        remoto.adicionarRetorno(this);
    }

    @Override
    public void notifica(String ativo, double variacaoPercentual) {
        Direcao direcao = variacaoPercentual >= 0 ? Direcao.ALTA : Direcao.BAIXA;
        publicar(new EventoMercado("BOVESPA:" + ativo, direcao, variacaoPercentual));
    }
}
