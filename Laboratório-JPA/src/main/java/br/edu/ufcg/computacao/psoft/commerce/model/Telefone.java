package br.edu.ufcg.computacao.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity(name = "telefones")
    public class Telefone {

        @JsonProperty
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(name = "pk_id_produto")
        private Long id;

        @JsonProperty
        @NotEmpty(message = "Numero Obrigatorio!")
        @Column(name = "desc_numero")
        private String numero;

        @ManyToOne
        @JoinColumn(name = "pessoa_id")
        private Pessoa pessoas;
    }


