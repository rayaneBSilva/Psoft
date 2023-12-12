package com.ufcg.psoft.commerce.dto.sabor;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaborGetRequestDTO {

    @JsonProperty("nome")
    @NotBlank(message = "Nome obrigatorio")
    private String nome;

    @JsonProperty("precoM")
    @NotBlank(message = "Preco obrigatorio")
    private double precoM;

    @JsonProperty("precoG")
    @NotBlank(message = "Preco obrigatorio")
    private double precoG;

    @JsonProperty("disponivel")
    @NotBlank(message = "Disponibilidade obrigatoria")
    private boolean disponivel;
}