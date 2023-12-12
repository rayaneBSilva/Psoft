package com.ufcg.psoft.commerce.dto.estabelecimento;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Sabor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstabelecimentoPostPutRequestDTO {

    @JsonProperty("sabores")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Sabor> sabores;

    @JsonProperty("codigoAcesso")
    @Pattern(regexp = "\\d{6}", message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    @NotBlank(message = "Codigo de acesso Obrigatorio!")
    private String codigoAcesso;

    @JsonProperty("entregadores")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "entregadores")
    private List<Entregador> entregadoresAprovados = new LinkedList<>();

}
