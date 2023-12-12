package br.edu.ufcg.computacao.psoft.commerce.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "enderecos")
public class Endereco {


        @JsonProperty
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(name = "pk_id_endereco")
        private Long id;

        @JsonProperty
        @ManyToOne
        @JoinColumn(name = "logradouro_id")
        private Logradouro logradouro;

        @JsonProperty
        @Column(nullable = false, name = "desc_numero")
        private int numero;

        @JsonProperty
        @Column(name = "desc_complemento")
        private String complemento;

        @ManyToOne
        @JoinColumn(name = "pessoa_id")
        private Pessoa pessoa;
}


