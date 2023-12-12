package br.edu.ufcg.computacao.psoft.commerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class Logradouro {
    @Id
    private Long id; // Unico, NÃO PODE SER VAZIO
    private String tipoLogradouro; // NÃO PODE SER VAZIO
    private String nomeLogradouro; // NÃO PODE SER VAZIO
    private String bairro; // NÃO PODE SER VAZIO
    private String cidade; // NÃO PODE SER VAZIO
    private String estado; // NÃO PODE SER VAZIO
    private String pais; // NÃO PODE SER VAZIA
    private String cep; // NÃO PODE SER VAZIA
    private String complemento; // opcional

    // Na classe Logradouro
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Logradouro that = (Logradouro) o;
        return Objects.equals(id, that.id); // Comparando apenas pelo ID, você pode ajustar os campos conforme necessário
    }


}