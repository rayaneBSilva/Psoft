package br.edu.ufcg.computacao.psoft.commerce.model;
import lombok.Data;

import java.util.List;


@Data
public class Pessoa {

    private long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private int dataNascimento;
    private List<Long> enderecoIds;

    private String profissao;

}
