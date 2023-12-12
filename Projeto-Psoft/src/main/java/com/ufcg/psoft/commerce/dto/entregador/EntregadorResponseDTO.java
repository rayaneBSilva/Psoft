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
public class EntregadorResponseDTO {
    @Id
    @JsonProperty("id")
    private Long id;

    @JsonProperty("Nome")
    @NotBlank(message = "Nome Completo Obrigatório")
    private String nome;

    @JsonProperty("Placa")
    @NotBlank(message = "Placa do veiculo")
    private String placaVeiculo;

    @JsonProperty("Tipo_Veículo")
    @NotBlank(message = "Tipo do Veiculo Obrigatório")
    private String tipoVeiculo;

    @JsonProperty("Cor_Veículo")
    @NotBlank(message = "Cor do Veiculo Obrigatório")
    private String corVeiculo;

    @JsonProperty("codigoAcesso")
    @Pattern(regexp = "\\d{6}", message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    @NotBlank(message = "Codigo de acesso Obrigatorio!")
    private String codigoAcesso;

    @JsonProperty("disponilibidade")
    private boolean disponibilidade;

    @JsonProperty("Status_Entregador")
    @NotBlank(message = "Status do entregador Obrigatório")
    @Builder.Default
    private String StatusEntregador = "Descanso";

}
