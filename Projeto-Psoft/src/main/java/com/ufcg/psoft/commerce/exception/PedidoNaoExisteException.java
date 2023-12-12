package com.ufcg.psoft.commerce.exception;

public class PedidoNaoExisteException extends CommerceException{
    public PedidoNaoExisteException(){
        super("O pedido consultado nao existe!");
    }
}
