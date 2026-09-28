package com.ao.depress.stocknotify.adapter.bovespa;

import java.util.ArrayList;
import java.util.List;

public class SistemaRemotoBovespa {
    private final List<RetornoBovespa> retornos = new ArrayList<>();

    public void adicionarRetorno(RetornoBovespa retorno) { retornos.add(retorno); }

    public void simulaVariacao(String ativo, double variacaoPercentual) {
        for (RetornoBovespa retorno : retornos) retorno.notifica(ativo, variacaoPercentual);
    }
}
