package com.ufcg.psoft.commerce.exception;

public class EstabelecimentoNaoExisteException extends CommerceException{
    public EstabelecimentoNaoExisteException(){
        super("O estabelecimento consultado nao existe!");
    }
}
