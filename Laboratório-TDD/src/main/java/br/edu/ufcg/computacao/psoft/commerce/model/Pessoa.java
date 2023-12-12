package br.edu.ufcg.computacao.psoft.commerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


import lombok.Builder;
import lombok.Data;


import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Pessoa {
    @Id
    @Column(unique = true)
    private Long id;
    @NotBlank
    private String nome;
    @NotBlank
    @Column(unique = true)
    private String cpf;
    @NotBlank
    @Column(unique = true)
    private String email;
    @NotEmpty
    private List<String> listaTelefones;
    @NotBlank
    private String dataNascimento;
    @NotEmpty
    private List<Logradouro> listaEnderecos;
    @NotBlank
    @Column(unique = true)
    private String profissao;

}

