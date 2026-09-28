package com.ao.depress.stocknotify.adapter.nasdaq;

import com.ao.depress.stocknotify.model.Direcao;
import com.ao.depress.stocknotify.model.EventoMercado;
import com.ao.depress.stocknotify.model.observer.Bolsa;

public class AdaptadorBolsaNasdaq extends Bolsa implements OuvinteNasdaq {
    public AdaptadorBolsaNasdaq(SistemaRemotoNasdaq remoto) {
        remoto.registrarOuvinte(this);
    }

    @Override
    public void aoMudarPreco(String simbolo, double precoAntigo, double precoNovo) {
        Direcao direcao = precoNovo >= precoAntigo ? Direcao.ALTA : Direcao.BAIXA;
        publicar(new EventoMercado("NASDAQ:" + simbolo, direcao, precoNovo));
    }
}
