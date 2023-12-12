package com.ufcg.psoft.commerce.dto.entregador;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
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
public class EntregadorPostPutRequestDTO {
    @Id
    @JsonProperty("id")
    private Long id;

    @JsonProperty("Nome")
    @NotBlank(message = "Nome e obrigatorio")
    private String nome;

    @JsonProperty("Placa")
    @NotBlank(message = "Placa do veiculo e obrigatoria")
    private String placaVeiculo;

    @JsonProperty("Tipo_Veículo")
    @NotBlank(message = "Tipo do veiculo e obrigatorio")
    @Pattern(regexp = "moto|carro", message = "Tipo do veiculo deve ser moto ou carro")
    private String tipoVeiculo;

    @JsonProperty("Cor_Veículo")
    @NotBlank(message = "Cor do veiculo e obrigatoria")
    private String corVeiculo;

    @JsonProperty("codigoAcesso")
    @Pattern(regexp = "\\d{6}", message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    @NotBlank(message = "Codigo de acesso Obrigatorio!")
    private String codigoAcesso;

}
