package com.ao.depress.stocknotify.adapter.bovespa;

public interface RetornoBovespa {
    void notifica(String ativo, double variacaoPercentual);
}
