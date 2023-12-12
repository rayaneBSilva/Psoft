package br.edu.ufcg.computacao.psoft.commerce.model;


import lombok.Data;

@Data
public class Endereco {

    private Long id;
    private String tipoLogradouro;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String pais;
    private String cep;
}
