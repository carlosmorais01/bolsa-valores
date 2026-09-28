package com.ao.depress.stocknotify.exception;

import java.util.UUID;

public class BolsaNaoEncontradaException extends RuntimeException {
    public BolsaNaoEncontradaException(UUID id) {
        super("Bolsa não encontrada: " + id);
    }
}
