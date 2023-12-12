package com.ufcg.psoft.commerce.dto.cliente;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientePostPutRequestDTO {

    @Id
    @JsonProperty("id")
    private Long id;

    @JsonProperty("nome")
    @NotBlank(message = "Nome obrigatorio")
    private String nome;

    @JsonProperty("endereço")
    @NotBlank(message = "Endereco obrigatorio")
    private String endereco;

    @JsonProperty("codigoAcesso")
    @NotBlank(message = "Codigo de acesso obrigatorio")
    @Pattern(regexp = "^\\d{6}$", message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    private String codigoAcesso;

    @Email
    @JsonProperty("email")
    private String email;
}