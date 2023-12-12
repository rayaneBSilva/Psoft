package com.ufcg.psoft.commerce.exception;

public class SaborJaIndisponivelException extends CommerceException{
    public SaborJaIndisponivelException(){
        super("O sabor consultado ja esta indisponivel!");
    }
}
