package com.ao.depress.stocknotify.adapter.nasdaq;

import java.util.ArrayList;
import java.util.List;

public class SistemaRemotoNasdaq {
    private final List<OuvinteNasdaq> ouvintes = new ArrayList<>();

    public void registrarOuvinte(OuvinteNasdaq ouvinte) { ouvintes.add(ouvinte); }

    public void simularMudancaPreco(String simbolo, double precoAntigo, double precoNovo) {
        for (OuvinteNasdaq ouvinte : ouvintes) ouvinte.aoMudarPreco(simbolo, precoAntigo, precoNovo);
    }
}
