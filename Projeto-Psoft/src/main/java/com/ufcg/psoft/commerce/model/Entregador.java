package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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
@Entity
@Table(name = "entregadores")
public class Entregador {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "pk_id_entregador")
    @JsonProperty("id")
    private Long id;

    @JsonProperty("Nome")
    @Column(name = "nome")
    @NotBlank(message = "Nome Completo Obrigatorio")
    private String nome;

    @JsonProperty("Placa")
    @Column(name = "placa")
    @NotBlank(message = "Placa do veiculo")
    private String placaVeiculo;

    @JsonProperty("Tipo_Veículo")
    @Column(name = "tipo_veiculo")
    @NotBlank(message = "Tipo do Veiculo Obrigatório")
    private String tipoVeiculo;

    @JsonProperty("Cor_Veículo")
    @Column(name = "cor_veiculo")
    @NotBlank(message = "Cor do Veiculo Obrigatório")
    private String corVeiculo;

    @JsonProperty("codigoAcesso")
    @Pattern(regexp = "\\d{6}", message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    @NotBlank(message = "Codigo de acesso Obrigatorio!")
    @Column(name = "desc_codigoAcesso")
    private String codigoAcesso;

    @JsonProperty("disponilibidade")
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean disponibilidade;

    @Builder.Default
    @JsonProperty("Status_Entregador")
    @Column(name = "status_entregador")
    @NotBlank(message = "Status do entregador Obrigatório")
    private String StatusEntregador = "Descanso";
}
