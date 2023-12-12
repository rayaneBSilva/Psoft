package br.edu.ufcg.computacao.psoft.commerce.dto.pessoaDTO;

import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaPatchEmailDTO {
    @JsonProperty("email")
    @NotBlank
    @Column(unique = true)
    @NotEmpty(message = "Email obrigatorio!")
    private String email;
}