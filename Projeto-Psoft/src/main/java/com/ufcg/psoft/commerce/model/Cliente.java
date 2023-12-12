package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "pk_id_cliente")
    @JsonProperty("id")
    private Long id;

    @JsonProperty("nome")
    @NotBlank(message = "Nome Obrigátorio!")
    @Column(nullable = false, name = "desc_nome")
    private String nome;

    @JsonProperty("endereço")
    @NotBlank(message = "Endereco obrigatorio")
    @Column(nullable = false, name = "desc_endereco")
    private String endereco;

    @JsonProperty("codigoAcesso")
    @NotBlank(message = "Codigo não existe")
    @Column(nullable = false, name = "desc_codigoAcesso")
    private String codigoAcesso;

    @JsonProperty("email")
    @Column(name = "desc_email")
    private String email;
}