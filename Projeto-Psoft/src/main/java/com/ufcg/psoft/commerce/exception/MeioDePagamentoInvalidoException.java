package com.ufcg.psoft.commerce.exception;

public class MeioDePagamentoInvalidoException extends CommerceException{
    public MeioDePagamentoInvalidoException(){
        super("O meio de pagamento consultado nao existe!");
    }
}
