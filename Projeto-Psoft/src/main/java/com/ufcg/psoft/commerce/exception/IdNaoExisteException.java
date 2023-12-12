package com.ufcg.psoft.commerce.exception;

public class IdNaoExisteException extends CommerceException{
    public IdNaoExisteException(){
        super("O id consultado é invalido");
    }
}
