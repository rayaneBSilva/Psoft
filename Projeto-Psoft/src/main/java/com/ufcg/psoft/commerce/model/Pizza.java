package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pizzas")


public class Pizza {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("sabor1")
    @JoinColumn(name = "sabor1_id")
    @OneToOne
    private Sabor sabor1;

    @JsonProperty("sabor2")
    @JoinColumn(name = "sabor2_id")
    @OneToOne
    private Sabor sabor2;

    @JsonProperty("tamanho")
    @Column(nullable = false)
    private String tamanho;

    @JsonProperty("preco")
    @Column(nullable = false)
    @Builder.Default
    private Double preco = 0.0;


    public void calcularPreco() {
        if (tamanho.equals("media") && sabor1 != null) {
            this.preco = (sabor1.getPrecoM() != null) ? sabor1.getPrecoM() : 0.0;
        } else if (tamanho.equals("grande") && sabor2 == null) {
            this.preco = (sabor1.getPrecoG() != null) ? sabor1.getPrecoG() : 0.0;
        } else if (tamanho.equals("grande") && sabor1 != null && sabor2 != null) {
            double precoSabor1 = (sabor1.getPrecoG() != null) ? sabor1.getPrecoG() : 0.0;
            double precoSabor2 = (sabor2.getPrecoG() != null) ? sabor2.getPrecoG() : 0.0;
            this.preco = (precoSabor1 + precoSabor2) / 2;
        }
    }
}
