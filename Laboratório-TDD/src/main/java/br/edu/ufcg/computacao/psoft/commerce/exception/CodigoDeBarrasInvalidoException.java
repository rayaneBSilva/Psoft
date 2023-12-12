package br.edu.ufcg.computacao.psoft.commerce.exception;

public class CodigoDeBarrasInvalidoException extends  CommerceException{
    public CodigoDeBarrasInvalidoException() {
        super("O codigo de Barra está inválido!");
    }
}