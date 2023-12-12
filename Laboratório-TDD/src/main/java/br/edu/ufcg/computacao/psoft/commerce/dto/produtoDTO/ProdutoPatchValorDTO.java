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
public class ProdutoPatchValorDTO {

    @JsonProperty("valor")
    private double valor; // não pode ser vazio
}