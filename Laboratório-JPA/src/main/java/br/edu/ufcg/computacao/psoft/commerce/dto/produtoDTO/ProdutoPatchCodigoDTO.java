package br.edu.ufcg.computacao.psoft.commerce.dto.produtoDTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoPatchCodigoDTO {

    @JsonProperty("codigoBarras")
    private String codigoBarras; // Nao podde ser vazio
}
