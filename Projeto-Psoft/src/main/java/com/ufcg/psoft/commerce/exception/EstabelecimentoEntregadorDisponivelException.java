package com.ufcg.psoft.commerce.exception;

public class EstabelecimentoEntregadorDisponivelException extends CommerceException {
    public EstabelecimentoEntregadorDisponivelException() {
        super("Existe entregador disponivel no estabelecimento!");
    }
}