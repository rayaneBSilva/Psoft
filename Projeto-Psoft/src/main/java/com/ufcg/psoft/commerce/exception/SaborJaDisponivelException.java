package com.ufcg.psoft.commerce.exception;

public class SaborJaDisponivelException extends CommerceException{
    public SaborJaDisponivelException(){
        super("O sabor consultado ja esta disponivel!");
    }
}
