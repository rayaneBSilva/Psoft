package br.edu.ufcg.computacao.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "pessoas")
public class Pessoa {
    @JsonProperty
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "pk_id_pessoa")
    private Long id;

    @NotBlank(message = "Nome Obrigatorio!")
    @JsonProperty("nome")
    @Column(nullable = false, name = "desc_nome")
    private String nome;

    @NotBlank(message = "CPF Obrigatorio!")
    @JsonProperty("cpf")
    @Column(unique = true, nullable = false, name = "desc_cpf")
    private String cpf;

    @NotBlank(message = "Email Obrigatorio!")
    @JsonProperty("email")
    @Column(unique = true, nullable = false, name = "desc_email")
    private String email;

    @NotEmpty(message = "Telefone Obrigatorio!")
    @JsonProperty("telefones")
    @OneToMany(mappedBy = "pessoas")
    private Set<Telefone> telefones;

    @JsonProperty("dataNascimento")
    @Column(name = "desc_data_de_nascimento")
    private String dataNascimento;

    @NotEmpty(message = "Endereco Obrigatorio!")
    @JsonProperty("enderecos")
    @OneToMany(mappedBy = "pessoa")
    private Set<Endereco> enderecos;

    @NotBlank(message = "Profissao Obrigatorio!")
    @JsonProperty("profissao")
    @Column(nullable = false, name = "desc_profissao")
    private String profissao;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pessoa pessoa = (Pessoa) o;
        return Objects.equals(nome, pessoa.nome) && Objects.equals(cpf, pessoa.cpf) && Objects.equals(dataNascimento, pessoa.dataNascimento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, cpf, dataNascimento);
    }
}

