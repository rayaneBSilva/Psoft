package com.ufcg.psoft.commerce.exception;

public class CancelamentoDePedidoInvalidoException extends CommerceException{
    public CancelamentoDePedidoInvalidoException(){
        super("Cancelamento de entrega invalido!");
    }
}