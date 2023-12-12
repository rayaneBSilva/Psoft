package br.edu.ufcg.computacao.psoft.commerce.exception;

public class PrecoAbaixoIgualZeroException extends  CommerceException{
    public PrecoAbaixoIgualZeroException() {
        super("O preço está abaixo ou igual a zero!");
    }
}