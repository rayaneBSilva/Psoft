package com.ufcg.psoft.commerce.exception;

public class SaborNaoExisteException extends CommerceException{
    public SaborNaoExisteException(){
        super("O sabor consultado nao existe!");
    }
}
