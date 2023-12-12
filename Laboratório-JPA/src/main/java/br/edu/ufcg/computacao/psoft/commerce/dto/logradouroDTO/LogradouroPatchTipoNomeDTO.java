package br.edu.ufcg.computacao.psoft.commerce.dto.logradouroDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogradouroPatchTipoNomeDTO {

    @NotBlank
    @JsonProperty("tipoLogradouro")
    private String tipoLogradouro;
    @NotBlank
    @JsonProperty("nomeLogradouro")
    private String nomeLogradouro;

}